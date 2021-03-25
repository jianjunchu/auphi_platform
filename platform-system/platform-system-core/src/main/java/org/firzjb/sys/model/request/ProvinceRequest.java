package org.firzjb.sys.model.request;

import org.firzjb.base.model.request.BaseRequest;
import com.baomidou.mybatisplus.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 省信息
 * </p>
 *
 * @author Tony
 * @since 2021-03-16
 */
@Data

public class ProvinceRequest extends BaseRequest<ProvinceRequest> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    private String name;




}
