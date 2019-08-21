package com.auphi.ktrl.metadata.controller;

import com.auphi.data.hub.core.BaseMultiActionController;
import com.auphi.data.hub.core.PaginationSupport;
import com.auphi.data.hub.core.struct.BaseDto;
import com.auphi.data.hub.core.struct.Dto;
import com.auphi.data.hub.core.util.JsonHelper;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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



    public ModelAndView query(HttpServletRequest req,HttpServletResponse resp) throws IOException {
        Dto<String,Object> dto = new BaseDto();

        try {
            this.setPageParam(dto, req);
            dto.put("mappingGroupId", ServletRequestUtils.getStringParameter(req,"mappingGroupId"));
            PaginationSupport<MetadataMapping> page = metadataMappingService.getPage(dto);
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

                    metadataMapping.setSourceTableName(tableName);
                    metadataMapping.setDestTableName(tableName);
                    metadataMapping.setSourceColumnName(getTableColumn(database,metadataMapping.getSourceSchemaName(),metadataMapping.getSourceTableName()));
                    metadataMapping.setDestColumnName(metadataMapping.getSourceColumnName());
                    mappings.add(metadataMapping);
                }

                this.metadataMappingService.save(mappings);
                this.setOkTipMsg("添加成功", resp);
            }else{

                MetadataMapping ex = metadataMappingService.get(mapping.getId());

                database = MarketUtil.getDatabase(ex.getSourceDbId().intValue());
                database.connect();

                ex.setMappingGroupId(mapping.getMappingGroupId());
                ex.setDestDbId(mapping.getDestDbId());
                ex.setDestSchemaName(mapping.getDestSchemaName());
                ex.setDestTableName(mapping.getDestTableName());
                ex.setUpdateTime(new Date());
                ex.setUpdateUser(user.getUser_id());
                ex.setSourceColumnName(getTableColumn(database,ex.getSourceSchemaName(),ex.getSourceTableName()));
                ex.setDestColumnName(ex.getSourceColumnName());
                metadataMappingService.update(ex);
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


    private String getTableColumn(Database database,String schemaname,String tablename) throws KettleDatabaseException {

        String tname = StringUtils.isEmpty(schemaname) ? tablename : schemaname +"."+tablename ;
        String[] fieldNames =  database.getTableFields(tname).getFieldNames();
        return StringUtils.join(fieldNames,",");

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

