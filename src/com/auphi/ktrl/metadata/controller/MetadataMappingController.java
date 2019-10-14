package com.auphi.ktrl.metadata.controller;

import com.auphi.data.hub.core.BaseMultiActionController;
import com.auphi.data.hub.core.PaginationSupport;
import com.auphi.data.hub.core.struct.BaseDto;
import com.auphi.data.hub.core.struct.Dto;
import com.auphi.data.hub.core.util.JsonHelper;
import com.auphi.ktrl.mdm.domain.TextValue;
import com.auphi.ktrl.metadata.domain.MetadataMapping;
import com.auphi.ktrl.metadata.domain.MetadataMappingGroup;
import com.auphi.ktrl.metadata.service.MetadataMappingGroupService;
import com.auphi.ktrl.metadata.service.MetadataMappingService;
import com.auphi.ktrl.schedule.util.MarketUtil;
import com.auphi.ktrl.system.user.bean.UserBean;
import com.auphi.ktrl.util.StringUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.lang.StringUtils;
import org.pentaho.di.core.database.Database;
import org.pentaho.di.core.exception.KettleDatabaseException;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.util.*;

@ApiIgnore
@Controller("metadataMapping")
public class MetadataMappingController extends BaseMultiActionController {


    private final static String INDEX = "admin/metadata/metadataMapping";

    @Autowired
    private MetadataMappingService metadataMappingService;

    @Autowired
    private MetadataMappingGroupService metadataMappingGroupService;

    public ModelAndView index(HttpServletRequest req, HttpServletResponse resp){
        return new ModelAndView(INDEX);
    }



    public ModelAndView query(HttpServletRequest req,HttpServletResponse resp,MetadataMapping metadataMapping) throws IOException {
        Dto<String,Object> dto = new BaseDto();

        try {

            PaginationSupport<MetadataMapping> page = metadataMappingService.getPage(metadataMapping);
            String jsonString = JsonHelper.encodeObject2Json(page);
            write(jsonString, resp);
        } catch (Exception e) {
            e.printStackTrace();
            this.setFailTipMsg(e.getMessage(), resp);
        }
        return null;
    }


    public ModelAndView getMappingGroupList(HttpServletRequest req,HttpServletResponse resp) throws IOException {
        try {
            List<MetadataMappingGroup> list = metadataMappingGroupService.getMappingGroupList();
            this.setOkTipMsg("SUCCESS",list, resp);
        } catch (Exception e) {
            e.printStackTrace();
            this.setFailTipMsg(e.getMessage(), resp);
        }
        return null;
    }

    public ModelAndView save(HttpServletRequest req,HttpServletResponse resp,MetadataMapping mapping) throws IOException{

        Database database = null;

        try{

            UserBean user = (UserBean)req.getSession().getAttribute("userBean");
            if(mapping.getId()==null){

                database = MarketUtil.getDatabase(mapping.getSourceDbId().intValue());
                database.connect();


                String[] sourceTableNames = null;

                if(StringUtils.isEmpty(mapping.getSourceTableName())){
                    sourceTableNames = getTables(mapping.getSourceDbId().intValue(),mapping.getSourceSchemaName());

                }else{
                    sourceTableNames = mapping.getSourceTableName().split(",");
                }

                List<MetadataMapping> mappings = new ArrayList<>();

                mapping.setCreateUser(user.getUser_id());
                mapping.setUpdateUser(user.getUser_id());
                mapping.setCreateTime(new Date());
                mapping.setUpdateTime(new Date());
                mapping.setExtractStatus(0);

                for(String tableName :sourceTableNames){

                    MetadataMapping metadataMapping = new MetadataMapping();


                    BeanUtilsBean.getInstance().getConvertUtils()
                            .register(new SqlDateConverter(null), Date.class);
                    BeanUtils.copyProperties(metadataMapping,mapping);


                    metadataMapping.setSourceTableName(database.getDatabaseMeta().getQuotedSchemaTableCombination(null,tableName));
                    metadataMapping.setDestTableName(tableName);

                    int count = metadataMappingService.countSourceTable(metadataMapping);

                    if(count>0){
                        continue;
                    }

                    mappings.addAll(getTableColumn(database,metadataMapping));
                }

                this.metadataMappingService.save(mappings);
                this.setOkTipMsg("添加成功", resp);
            }else{

                MetadataMapping ex = metadataMappingService.get(mapping.getId());
                ex.setIsPk(mapping.getIsPk());
                ex.setIsIncrementalColumn(mapping.getIsIncrementalColumn());
                ex.setSourceColumnOrder(mapping.getSourceColumnOrder());
                ex.setSourceColumnComments(mapping.getSourceColumnComments());

                ex.setSourceColumnName(mapping.getSourceColumnName());
                ex.setSourceColumnScale(mapping.getSourceColumnScale());
                ex.setSourceColumnTypeEtl(mapping.getSourceColumnTypeEtl());
                ex.setSourceColumnType(mapping.getSourceColumnType());
                ex.setSourceColumnLength(mapping.getSourceColumnLength());

                ex.setDestColumnName(mapping.getDestColumnName());
                ex.setDestColumnScale(mapping.getDestColumnScale());
                ex.setDestColumnTypeEtl(mapping.getSourceColumnTypeEtl());
                ex.setDestColumnType(mapping.getDestColumnType());
                ex.setDestColumnLength(mapping.getDestColumnLength());

                ex.setMappingGroupId(mapping.getMappingGroupId());

                metadataMappingService.update(ex);

                ex.setDestDbId(mapping.getDestDbId());
                ex.setDestSchemaName(mapping.getDestSchemaName());
                ex.setDestTableName(mapping.getDestTableName());
                ex.setMappingGroupId(mapping.getMappingGroupId());
                ex.setExtractStyle(mapping.getExtractStyle());
                metadataMappingService.updateDestTable(ex);

                this.setOkTipMsg("更新成功", resp);
            }


        } catch(Exception e){
            e.printStackTrace();
            this.setFailTipMsg(e.getMessage(), resp);
        }finally {
            if(database !=null){
                database.disconnect();
            }
        }
        return null;
    }


    private List<MetadataMapping> getTableColumn(Database database,MetadataMapping mapping ) throws KettleDatabaseException, InvocationTargetException, IllegalAccessException {

        List<MetadataMapping> list = new ArrayList<>();

        String tableName = database.getDatabaseMeta().getQuotedSchemaTableCombination(mapping.getSourceSchemaName(),mapping.getSourceTableName());

        List<ValueMetaInterface> valueMetaInterfaces =  database.getTableFields(tableName).getValueMetaList();

        long i = 0;

       String[] primaryKey = database.getPrimaryKeyColumnNames(tableName);

        for(ValueMetaInterface valueMetaInterface : valueMetaInterfaces){

            MetadataMapping metadataMapping = new MetadataMapping();

            BeanUtils.copyProperties(metadataMapping,mapping);

            metadataMapping.setSourceColumnComments(valueMetaInterface.getComments());

            metadataMapping.setSourceColumnName(valueMetaInterface.getName());
            metadataMapping.setDestColumnName(valueMetaInterface.getName());

            metadataMapping.setSourceColumnLength(String.valueOf(valueMetaInterface.getLength()));
            metadataMapping.setDestColumnLength(metadataMapping.getSourceColumnLength());

            metadataMapping.setSourceColumnType(valueMetaInterface.getOriginalColumnTypeName().toUpperCase());
            metadataMapping.setDestColumnType(metadataMapping.getSourceColumnType());

            metadataMapping.setSourceColumnTypeEtl(valueMetaInterface.getTypeDesc());
            metadataMapping.setDestColumnTypeEtl(metadataMapping.getSourceColumnTypeEtl());

            metadataMapping.setSourceColumnScale(String.valueOf(valueMetaInterface.getOriginalScale()));
            metadataMapping.setDestColumnScale(metadataMapping.getSourceColumnScale());

            metadataMapping.setSourceColumnOrder(i);
            boolean isPk  = Arrays.asList(primaryKey).contains(valueMetaInterface.getName());
            metadataMapping.setIsPk(isPk?1:0);

            list.add(metadataMapping);



            i++;
        }


        return list;
    }



      private String[]  getTables(Integer dbId,String schemaname) throws KettleDatabaseException {
          Database database = null;
          try {

              database = MarketUtil.getDatabase(dbId);
              database.connect();


              String names[] = database.getTablenames(schemaname,false);
              database.disconnect();
              return names;
          }catch (Exception e){

              if(database !=null){
                  database.disconnect();
              }
              throw e;
          }

      }




    public ModelAndView getTypeInfo(HttpServletRequest req,HttpServletResponse resp) throws IOException{

        Database database = null;

        List<TextValue> list = new ArrayList<TextValue>();

        try{

            int database_id = ServletRequestUtils.getIntParameter(req,"id_database");

            database = MarketUtil.getDatabase(database_id);
            database.connect();

            ResultSet rs   =  database.getDatabaseMetaData().getTypeInfo();

            while(rs.next()){
                String typeName = rs.getString(1);
                list.add(new TextValue(typeName.toUpperCase(),typeName.toUpperCase()));
            }
            rs.close();

            this.setOkTipMsg("successful",list, resp);
        } catch(Exception e){
            e.printStackTrace();
            this.setFailTipMsg(e.getMessage(), resp);
        }finally {
            if(database!=null){
                database.disconnect();
            }
        }
        return null;
    }

    public ModelAndView update(HttpServletRequest req,HttpServletResponse resp,MetadataMapping mapping) throws IOException{
        try{
            this.metadataMappingService.update(mapping);
            this.setOkTipMsg("编辑成功", resp);
        } catch(Exception e){
            e.printStackTrace();
            this.setFailTipMsg("编辑失败", resp);
        }
        return null;
    }

    public ModelAndView delete(HttpServletRequest req,HttpServletResponse resp) throws IOException{
        try{
            String ids = ServletRequestUtils.getStringParameter(req,"ids");
            Dto dto = new BaseDto();
            dto.put("ids",ids);
            this.metadataMappingService.delete(dto);
            this.setOkTipMsg("删除成功", resp);
        }catch(Exception e){
            this.setFailTipMsg(e.getMessage(), resp);
        }
        return null;
    }

    public ModelAndView get(HttpServletRequest req,HttpServletResponse resp) throws IOException{
        try{

            Long id = ServletRequestUtils.getLongParameter(req,"id");

            MetadataMapping mapping =  this.metadataMappingService.get(id);
            this.setOkTipMsg("SUCCESS",mapping, resp);
        } catch(Exception e){
            e.printStackTrace();
            this.setFailTipMsg("获取失败", resp);
        }
        return null;
    }
}

