package com.aofei.datasource.model.response;

import lombok.Data;

/**
 * <p>
 * 数据库响应类型
 * </p>
 *
 * @author Tony
 * @since 2020-11-11
 */
@Data
public class DatabaseNameResponse {
    /**
     * 数据库连接名称
     */
    private String name;
}
