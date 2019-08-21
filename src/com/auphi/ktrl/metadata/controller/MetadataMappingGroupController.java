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
import com.auphi.ktrl.system.user.bean.UserBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ApiIgnore
@Controller("metadataMappingGroup")
public class MetadataMappingGroupController extends BaseMultiActionController {

    private final static String INDEX = "admin/metadata/metadataMappingGroup";

    @Autowired
    private MetadataMappingGroupService metadataMappingGroupService;


    public ModelAndView index(HttpServletRequest req, HttpServletResponse resp){

        return new ModelAndView(INDEX);
    }

    public ModelAndView query(HttpServletRequest req,HttpServletResponse resp) throws IOException {
        Dto<String,Object> dto = new BaseDto();

        try {
            this.setPageParam(dto, req);
            PaginationSupport<MetadataMappingGroup> page = metadataMappingGroupService.getPage(dto);
            String jsonString = JsonHelper.encodeObject2Json(page);
            write(jsonString, resp);
        } catch (Exception e) {
            e.printStackTrace();
            this.setFailTipMsg("获取失败", resp);
        }
        return null;
    }


    public ModelAndView save(HttpServletRequest req,HttpServletResponse resp,MetadataMappingGroup mapping) throws IOException{
        try{


            if(mapping.getId()==null ){
                UserBean user = (UserBean)req.getSession().getAttribute("userBean");
                mapping.setCreateUser(user!=null? user.getUser_id():null);
                mapping.setUpdateUser(mapping.getCreateUser());
                this.metadataMappingGroupService.save(mapping);
                this.setOkTipMsg("添加成功", resp);
            }else{
                this.metadataMappingGroupService.update(mapping);
                this.setOkTipMsg("修改成功", resp);
            }


        } catch(Exception e){
            e.printStackTrace();
            this.setFailTipMsg("添加失败", resp);
        }
        return null;
    }

    public ModelAndView delete(HttpServletRequest req,HttpServletResponse resp) throws IOException{
        try{
            String ids = ServletRequestUtils.getStringParameter(req,"ids");
            Dto dto = new BaseDto();
            dto.put("ids",ids);
            this.metadataMappingGroupService.delete(dto);
            this.setOkTipMsg("删除成功", resp);
        }catch(Exception e){
            this.setFailTipMsg(e.getMessage(), resp);
        }
        return null;
    }

    public ModelAndView get(HttpServletRequest req,HttpServletResponse resp) throws IOException{
        try{

            String id = ServletRequestUtils.getStringParameter(req,"id");

            MetadataMappingGroup mapping =  this.metadataMappingGroupService.get(id);
            this.setOkTipMsg("SUCCESS",mapping, resp);
        } catch(Exception e){
            e.printStackTrace();
            this.setFailTipMsg("获取失败", resp);
        }
        return null;
    }


}
