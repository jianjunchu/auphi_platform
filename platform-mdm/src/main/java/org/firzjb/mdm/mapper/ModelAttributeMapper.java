package org.firzjb.mdm.mapper;

import org.firzjb.base.annotation.MyBatisMapper;
import org.firzjb.base.mapper.BaseMapper;
import org.firzjb.mdm.entity.ModelAttribute;
import org.apache.ibatis.annotations.Param;
import org.firzjb.mdm.entity.ModelAttribute;

import java.util.List;

/**
 * <p>
 * 主数据模型属性 Mapper 接口
 * </p>
 *
 * @author Tony
 * @since 2018-10-25
 */
@MyBatisMapper
public interface ModelAttributeMapper extends BaseMapper<ModelAttribute> {

    int deleteByModelId(@Param("modelId")Long modelId);


    List<ModelAttribute> findListByModelId(@Param("modelId")Long modelId);
}
