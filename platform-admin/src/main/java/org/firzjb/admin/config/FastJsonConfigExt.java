package org.firzjb.admin.config;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;

import java.math.BigInteger;

/**
 * FastJson Config转换规则
 */
public class FastJsonConfigExt extends FastJsonConfig {

    /**
     * FastJson转换规则
     */
    public FastJsonConfigExt(){
        super();
        SerializeConfig serializeConfig = SerializeConfig.globalInstance;
        /**
         * BigInteger转换成String返回
         */
        serializeConfig.put(BigInteger.class, ToStringSerializer.instance);
        /**
         * Long类型转换成String返回
         */
        serializeConfig.put(Long.class,ToStringSerializer.instance);
        serializeConfig.put(Long.TYPE,ToStringSerializer.instance);
        this.setSerializeConfig(serializeConfig);
    }

}

