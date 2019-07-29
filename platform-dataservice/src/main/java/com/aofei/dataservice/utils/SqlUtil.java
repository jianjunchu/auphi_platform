package com.aofei.dataservice.utils;

import com.aofei.dataservice.model.response.ServiceInterfaceResponse;
import com.aofei.utils.StringUtils;

public class SqlUtil {


    public static String getSQl(ServiceInterfaceResponse interfaceResponse) {


        StringBuffer stringBuffer = new StringBuffer("SELECT ");
        stringBuffer.append(StringUtils.isEmpty(interfaceResponse.getFields())? " * " : interfaceResponse.getFields() );
        stringBuffer.append(" FROM ").append(interfaceResponse.getTableName());
        if(!StringUtils.isEmpty(interfaceResponse.getConditions())){
            stringBuffer.append(" WHERE ").append(interfaceResponse.getConditions());
        }

        stringBuffer.append(";");
        return stringBuffer.toString();

    }
}
