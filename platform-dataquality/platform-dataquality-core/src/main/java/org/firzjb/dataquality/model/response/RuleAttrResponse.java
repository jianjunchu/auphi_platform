package org.firzjb.dataquality.model.response;

import org.firzjb.base.entity.DataEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 数据质量管理-规则属性值
 * </p>
 *
 * @author Tony
 * @since 2020-11-04
 */
@Data
public class RuleAttrResponse  {

    private static final long serialVersionUID = 1L;


    /**
     * 规则id
     */
    private Long ruleId;

    private String code;

    private String valueStr;

    private String valueRank;

}
