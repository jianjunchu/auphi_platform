package org.firzjb.dataservice.utils;

import org.firzjb.dataservice.model.response.ServiceInterfaceResponse;
import org.firzjb.utils.StringUtils;
import org.firzjb.dataservice.model.response.ServiceInterfaceResponse;

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
