package org.firzjb.base.model.response;

import lombok.Data;

/**
 * @auther 制证数据实时汇聚系统
 * @create 2018-09-18 12:45
 */
@Data
public class CurrentUserResponse {

    private Long userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机号
     */
    private String mobile;

    /**
     * 组织ID
     */
    private Long organizerId;

    /**
     * 部门id
     */
    private Long unitId;

    private String unitCode;

    private String unitName;

    /**
     * 磁盘空间 字节 默认   1073741824字节
     */
    private Long diskSpace;

    /**
     * 组织ID
     */
    private String organizerName;


}
