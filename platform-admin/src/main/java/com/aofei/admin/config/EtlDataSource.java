package com.aofei.admin.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.pentaho.di.core.encryption.KettleTwoWayPasswordEncoder;

public class EtlDataSource extends DruidDataSource {

    @Override
    public void setUsername(String username) {
        this.username = KettleTwoWayPasswordEncoder.decryptPassword(username);
    }

    @Override
    public void setPassword(String password) {
        this.password =  KettleTwoWayPasswordEncoder.decryptPassword(password);
    }


}
