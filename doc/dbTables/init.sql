
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for COMPARE_SQL
-- ----------------------------
DROP TABLE IF EXISTS `COMPARE_SQL`;
CREATE TABLE `COMPARE_SQL` (
  `ID_COMPARE_SQL` bigint(64) NOT NULL AUTO_INCREMENT,
  `REPOSITORY_NAME` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '资源库ID',
  `ID_DATABASE` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'ID in r_databsae table',
  `ID_REFERENCE_DB` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '参考sql数据库ID',
  `ID_COMPARE_TABLE_GROUP` bigint(64) DEFAULT NULL COMMENT 'ID in profile_table_group',
  `COMPARE_NAME` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'profile_name',
  `COMPARE_DESC` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'compare desc',
  `COMPARE_TYPE` int(11) DEFAULT '1' COMMENT '1 for one value compare, 2 for multi-value compare, default 1',
  `SQL` longtext COLLATE utf8_unicode_ci,
  `REFERENCE_SQL` longtext COLLATE utf8_unicode_ci,
  `CREATE_TIME` datetime DEFAULT NULL,
  `USER_ID` bigint(64) DEFAULT NULL,
  `CREATE_USER` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改用户',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  `DEL_FLAG` int(11) DEFAULT NULL COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`ID_COMPARE_SQL`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for COMPARE_SQL_COLUMN
-- ----------------------------
DROP TABLE IF EXISTS `COMPARE_SQL_COLUMN`;
CREATE TABLE `COMPARE_SQL_COLUMN` (
  `ID_COMPARE_SQL_COLUMN` bigint(64) NOT NULL AUTO_INCREMENT,
  `COLUMN_NAME` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `COLUMN_TYPE` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `REFERENCE_COLUMN_NAME` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `COLUMN_DESC` varchar(300) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ID_COMPARE_SQL` bigint(64) NOT NULL DEFAULT '0',
  `COMPARE_STYLE` int(11) DEFAULT NULL COMMENT '0 等值比较，1 范围比较',
  `MIN_RATIO` decimal(20,5) DEFAULT NULL,
  `MAX_RATIO` decimal(20,5) DEFAULT NULL,
  `CREATE_USER` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改用户',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  `DEL_FLAG` int(11) DEFAULT NULL COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`ID_COMPARE_SQL_COLUMN`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for COMPARE_SQL_RESULT
-- ----------------------------
DROP TABLE IF EXISTS `COMPARE_SQL_RESULT`;
CREATE TABLE `COMPARE_SQL_RESULT` (
  `ID_COMPARE_SQL_RESULT` bigint(64) NOT NULL AUTO_INCREMENT,
  `ID_COMPARE_SQL_COLUMN` bigint(64) DEFAULT NULL,
  `COLUMN_VALUE` varchar(300) COLLATE utf8_unicode_ci DEFAULT NULL,
  `REFERENCE_COLUMN_VALUE` varchar(300) COLLATE utf8_unicode_ci DEFAULT NULL,
  `COMPARE_RESULT` int(11) DEFAULT NULL COMMENT '1 equals,   0  not equals',
  `CREATE_TIME` datetime DEFAULT NULL,
  `CREATE_USER` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改用户',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  `DEL_FLAG` int(11) DEFAULT NULL COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`ID_COMPARE_SQL_RESULT`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for DATASERVICE_AUTH
-- ----------------------------
DROP TABLE IF EXISTS `DATASERVICE_AUTH`;
CREATE TABLE `DATASERVICE_AUTH` (
  `AUTH_ID` bigint(64) NOT NULL,
  `ORGANIZER_ID` bigint(20) DEFAULT NULL COMMENT '组织 ID',
  `USER_ID` bigint(64) DEFAULT NULL COMMENT '授权用户',
  `SERVICE_ID` bigint(64) DEFAULT NULL COMMENT '授权服务接口',
  `AUTH_IP` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '授权 IP',
  `USE_DESC` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '业务用途',
  `USE_DEPT` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '使用部门',
  `USER_NAME` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '使用人员',
  `CREATE_USER` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改用户',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  `DEL_FLAG` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`AUTH_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='服务接口授权';

-- ----------------------------
-- Table structure for DATASERVICE_INTERFACE
-- ----------------------------
DROP TABLE IF EXISTS `DATASERVICE_INTERFACE`;
CREATE TABLE `DATASERVICE_INTERFACE` (
  `SERVICE_ID` bigint(11) NOT NULL,
  `ORGANIZER_ID` bigint(64) DEFAULT NULL COMMENT '组织 id',
  `SERVICE_NAME` varchar(100) DEFAULT NULL COMMENT '接口名称',
  `SERVICE_IDENTIFY` varchar(100) DEFAULT NULL COMMENT 'Client调用时唯一识别的标示',
  `SERVICE_URL` varchar(200) DEFAULT NULL,
  `INTERFACE_TYP` int(11) DEFAULT NULL COMMENT '接口类型1:发布数据 2:接受数据',
  `JOB_TYPE` int(11) DEFAULT NULL COMMENT '1表示job，2表示trans，3表示自定义',
  `TRANS_NAME` varchar(20) DEFAULT NULL,
  `RETURN_TYPE` int(11) DEFAULT NULL COMMENT '用户可以自己选的，只支持FTP和Webservice\r\n            1表示FTP，2表示Webservice\r\n            ',
  `DATASOURCE` varchar(100) DEFAULT NULL,
  `TIMEOUT` int(11) DEFAULT NULL COMMENT '服务接口生成的结果数据超时时间，超过这个时间就要删除数据，单位分钟',
  `IS_COMPRESS` int(11) DEFAULT NULL COMMENT '1表示压缩，0表示不压缩',
  `ID_DATABASE` bigint(20) DEFAULT NULL COMMENT '数据源 ID',
  `SCHEMA_NAME` varchar(100) DEFAULT NULL COMMENT '连接模式名',
  `TABLE_NAME` varchar(100) DEFAULT NULL COMMENT '连接表名',
  `DELIMITER` varchar(5) DEFAULT NULL COMMENT '分隔符',
  `FIELDS` varchar(200) DEFAULT NULL COMMENT '输出字段',
  `CONDITIONS` varchar(200) DEFAULT NULL COMMENT '条件表达式',
  `INTERFACE_DESC` varchar(1000) DEFAULT NULL COMMENT '接口说明',
  `JOB_CONFIG_ID` int(11) DEFAULT NULL,
  `CREATE_USER` varchar(20) DEFAULT NULL COMMENT '创建者',
  `UPDATE_USER` varchar(20) DEFAULT NULL COMMENT '最后修改用户',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '最后时间更新',
  `DEL_FLAG` tinyint(4) DEFAULT '0' COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`SERVICE_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据接口管理';

-- ----------------------------
-- Table structure for DATASERVICE_INTERFACE_FIELD
-- ----------------------------
DROP TABLE IF EXISTS `DATASERVICE_INTERFACE_FIELD`;
CREATE TABLE `DATASERVICE_INTERFACE_FIELD` (
  `FIELD_ID` bigint(64) NOT NULL COMMENT '字段 ID',
  `SERVICE_ID` bigint(20) DEFAULT NULL COMMENT '所属接口 ID',
  `FIELD_NAME` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '字段名称',
  `FIELD_TYPE` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '字段类型',
  `JSON_PATH` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '路径$开头',
  `CREATE_USER` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UPDATE_USER` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `DEL_FLAG` int(11) DEFAULT NULL,
  PRIMARY KEY (`FIELD_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for DATASERVICE_JOB_LOG
-- ----------------------------
DROP TABLE IF EXISTS `DATASERVICE_JOB_LOG`;
CREATE TABLE `DATASERVICE_JOB_LOG` (
  `ID_JOB` bigint(64) NOT NULL,
  `MONITOR_ID` bigint(64) DEFAULT NULL,
  `JOB_CONFIG_ID` bigint(64) DEFAULT NULL COMMENT '对应r_job表中的ID_JOB主键',
  `CHANNEL_ID` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '唯一，通UUID表示\r\n            ',
  `JOBName` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `JOB_CN_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `STATUS` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `LINES_READ` bigint(20) DEFAULT NULL,
  `LINES_WRITTEN` bigint(20) DEFAULT NULL,
  `LINES_UPDATED` bigint(20) DEFAULT NULL,
  `LINES_INPUT` bigint(20) DEFAULT NULL,
  `LINES_OUTPUT` bigint(20) DEFAULT NULL,
  `LINES_REJECTED` bigint(20) DEFAULT NULL,
  `ERRORS` bigint(20) DEFAULT NULL,
  `STARTDATE` datetime DEFAULT NULL,
  `ENDDATE` datetime DEFAULT NULL,
  `LOGDATE` datetime DEFAULT NULL,
  `DEPDATE` datetime DEFAULT NULL,
  `REPLAYDATE` datetime DEFAULT NULL,
  `LOG_FIELD` mediumtext COLLATE utf8_unicode_ci,
  `EXECUTING_SERVER` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `EXECUTING_USER` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `EXCUTOR_TYPE` tinyint(4) DEFAULT NULL COMMENT '1:表示本地，2表示远程，3表示集群',
  `JOB_LOG` text COLLATE utf8_unicode_ci,
  `CREATE_USER` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改用户',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  `DEL_FLAG` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`ID_JOB`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for DATASERVICE_MONITOR
-- ----------------------------
DROP TABLE IF EXISTS `DATASERVICE_MONITOR`;
CREATE TABLE `DATASERVICE_MONITOR` (
  `MONITOR_ID` bigint(64) NOT NULL,
  `ORGANIZER_ID` bigint(64) DEFAULT NULL COMMENT '组织 ID',
  `SERVICE_ID` bigint(64) DEFAULT NULL COMMENT '服务接口 ID',
  `START_TIME` datetime DEFAULT NULL COMMENT '开始时间',
  `END_TIME` datetime DEFAULT NULL COMMENT '结束时间',
  `STATUS` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '状态',
  `SERVICE_USER_ID` bigint(64) DEFAULT NULL COMMENT '执行的用户',
  `SYSTEM_NAME` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '系统名称',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  `DEL_FLAG` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`MONITOR_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for DATASERVICE_MONITOR_STEP_INFO
-- ----------------------------
DROP TABLE IF EXISTS `DATASERVICE_MONITOR_STEP_INFO`;
CREATE TABLE `DATASERVICE_MONITOR_STEP_INFO` (
  `STEP_ID` bigint(64) NOT NULL,
  `MONITOR_ID` bigint(64) DEFAULT NULL,
  `STEPNAME` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `READRECORDCOUNT` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `RETURNRECORDCOUNT` bit(20) DEFAULT NULL,
  `STARTDATE` datetime DEFAULT NULL,
  `ENDDATE` datetime DEFAULT NULL,
  `COSTTIME` int(11) DEFAULT NULL,
  `STATUS` int(11) DEFAULT NULL,
  `LOGINFO` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `CREATE_USER` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改用户',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  `DEL_FLAG` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`STEP_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for DATASERVICE_RECEIVE_INTERFACE
-- ----------------------------
DROP TABLE IF EXISTS `DATASERVICE_RECEIVE_INTERFACE`;
CREATE TABLE `DATASERVICE_RECEIVE_INTERFACE` (
  `SERVICE_ID` bigint(64) NOT NULL,
  `SERVICE_NAME` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `SERVICE_IDENTIFY` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Client调用时唯一识别的标示',
  `SERVICE_URL` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DATASOURCE` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `TABLENAME` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `CREATEDATE` datetime DEFAULT NULL,
  `INTERFACE_DESC` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ID_DATABASE` bigint(20) DEFAULT NULL,
  `CREATE_USER` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改用户',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  `DEL_FLAG` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`SERVICE_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for DATASERVICE_RECEIVE_INTERFACE_FIELDS
-- ----------------------------
DROP TABLE IF EXISTS `DATASERVICE_RECEIVE_INTERFACE_FIELDS`;
CREATE TABLE `DATASERVICE_RECEIVE_INTERFACE_FIELDS` (
  `FIELD_ID` bigint(64) NOT NULL,
  `SERVICE_ID` bigint(64) NOT NULL,
  `FIELD_NAME` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '字段名',
  `JSON_PATH` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '提交的Json数据的路径，定位字段值',
  `CREATE_USER` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改用户',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  `DEL_FLAG` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`FIELD_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for DATASERVICE_USER
-- ----------------------------
DROP TABLE IF EXISTS `DATASERVICE_USER`;
CREATE TABLE `DATASERVICE_USER` (
  `USER_ID` bigint(64) NOT NULL COMMENT '用户 id',
  `ORGANIZER_ID` bigint(64) DEFAULT NULL COMMENT '组织 id',
  `USERNAME` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户名',
  `PASSWORD` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '密码',
  `SYSTEM_NAME` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '系统名称',
  `SYSTEM_IP` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '系统 IP',
  `SYSTEM_DESC` varchar(400) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '系统描述',
  `CREATE_USER` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改用户',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  `DEL_FLAG` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`USER_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='数据发布访问用户\n';

-- ----------------------------
-- Table structure for DATASOURCE_FTP
-- ----------------------------
DROP TABLE IF EXISTS `DATASOURCE_FTP`;
CREATE TABLE `DATASOURCE_FTP` (
  `ID_FTP` bigint(64) NOT NULL,
  `NAME` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `HOST_NAME` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PORT` int(11) DEFAULT NULL,
  `USERNAME` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PASSWORD` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ORGANIZER_ID` bigint(20) DEFAULT NULL,
  `CREATE_USER` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '最后修改用户',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '最后时间更新',
  `DEL_FLAG` tinyint(4) DEFAULT '0' COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`ID_FTP`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='FTP管理';

-- ----------------------------
-- Table structure for DATASOURCE_HADOOP
-- ----------------------------
DROP TABLE IF EXISTS `DATASOURCE_HADOOP`;
CREATE TABLE `DATASOURCE_HADOOP` (
  `ID` bigint(11) NOT NULL,
  `SERVER` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PORT` int(11) DEFAULT NULL,
  `USERID` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PASSWORD` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ORGANIZER_ID` bigint(20) DEFAULT NULL,
  `CREATE_USER` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '最后修改用户',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '最后时间更新',
  `DEL_FLAG` tinyint(4) DEFAULT '0' COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='HADOOP管理';

-- ----------------------------
-- Table structure for DATASOURCE_INTERFACE
-- ----------------------------
DROP TABLE IF EXISTS `DATASOURCE_INTERFACE`;
CREATE TABLE `DATASOURCE_INTERFACE` (
  `INTERFACE_ID` bigint(11) NOT NULL,
  `ORGANIZER_ID` bigint(64) DEFAULT NULL COMMENT '组织 id',
  `INTERFACE_NAME` varchar(100) DEFAULT NULL COMMENT '接口名称',
  `INTERFACE_URL` varchar(200) DEFAULT NULL,
  `REQUEST_TYPE` varchar(100) DEFAULT NULL COMMENT '接口请求方式, 1 get 2 post',
  `INTERFACE_DESC` varchar(1000) DEFAULT NULL COMMENT '接口说明',
  `RETURN_DESC` varchar(1000) DEFAULT NULL COMMENT '返回数据格式的描述',
  `TIMEOUT` int(11) DEFAULT NULL COMMENT '访问超时时间,单位分钟',
  `CREATE_USER` varchar(20) DEFAULT NULL COMMENT '创建者',
  `UPDATE_USER` varchar(20) DEFAULT NULL COMMENT '最后修改用户',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '最后时间更新',
  `DEL_FLAG` tinyint(4) DEFAULT '0' COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`INTERFACE_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据接口管理';

-- ----------------------------
-- Table structure for DATASOURCE_INTERFACE_PARAMETER
-- ----------------------------
DROP TABLE IF EXISTS `DATASOURCE_INTERFACE_PARAMETER`;
CREATE TABLE `DATASOURCE_INTERFACE_PARAMETER` (
  `INTERFACE_PARAMETER_ID` bigint(11) NOT NULL,
  `ORGANIZER_ID` bigint(64) DEFAULT NULL COMMENT '组织 id',
  `INTERFACE_ID` bigint(11) DEFAULT NULL COMMENT '接口id',
  `PARAMETER_NAME` varchar(100) DEFAULT NULL COMMENT '参数名称',
  `PARAMETER_TYPE` varchar(200) DEFAULT NULL COMMENT '参数数据类型',
  `PARAMETER_DESC` varchar(1000) DEFAULT NULL COMMENT '参数说明',
  `CREATE_USER` varchar(20) DEFAULT NULL COMMENT '创建者',
  `UPDATE_USER` varchar(20) DEFAULT NULL COMMENT '最后修改用户',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '最后时间更新',
  `DEL_FLAG` tinyint(4) DEFAULT '0' COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`INTERFACE_PARAMETER_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据接口参数管理';

-- ----------------------------
-- Table structure for DATASOURCE_INTERFACE_RESPONSE_MAPPING
-- ----------------------------
DROP TABLE IF EXISTS `DATASOURCE_INTERFACE_RESPONSE_MAPPING`;
CREATE TABLE `DATASOURCE_INTERFACE_RESPONSE_MAPPING` (
  `INTERFACE_RESPONSE_MAPPING_ID` bigint(11) NOT NULL,
  `ORGANIZER_ID` bigint(64) DEFAULT NULL COMMENT '组织 id',
  `INTERFACE_ID` bigint(11) DEFAULT NULL COMMENT '接口id',
  `JSON_PATH` varchar(100) DEFAULT NULL COMMENT '要解析的JSON路径',
  `FIELD_NAME` varchar(200) DEFAULT NULL COMMENT 'JSON路径对应的新的字段名',
  `FIELD_DESC` varchar(1000) DEFAULT NULL COMMENT '字段说明',
  `CREATE_USER` varchar(20) DEFAULT NULL COMMENT '创建者',
  `UPDATE_USER` varchar(20) DEFAULT NULL COMMENT '最后修改用户',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '最后时间更新',
  `DEL_FLAG` tinyint(4) DEFAULT '0' COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`INTERFACE_RESPONSE_MAPPING_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据接口返回值字段映射';

-- ----------------------------
-- Table structure for DATA_QUALITY_CHECK_RESULT
-- ----------------------------
DROP TABLE IF EXISTS `DATA_QUALITY_CHECK_RESULT`;
CREATE TABLE `DATA_QUALITY_CHECK_RESULT` (
  `CHECK_RESULT_ID` bigint(20) NOT NULL COMMENT '结果id',
  `RULE_ID` bigint(20) DEFAULT NULL COMMENT '规则ID',
  `ORGANIZER_ID` bigint(20) DEFAULT NULL COMMENT '组织ID',
  `TOTAL_CHECKED_NUM` bigint(20) DEFAULT NULL COMMENT '检查总记录数',
  `PASSED_NUM` bigint(20) DEFAULT NULL COMMENT '通过记录数',
  `NOT_PASSED_NUM` bigint(20) DEFAULT NULL COMMENT '未通过记录数',
  `CHECK_START_TIME` datetime DEFAULT NULL COMMENT '检查开始时间',
  `CHECK_END_TIME` datetime DEFAULT NULL COMMENT '检查结束时间',
  `CREATE_USER` varchar(50) DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(50) DEFAULT NULL COMMENT '最后修改用户',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后时间更新',
  `DEL_FLAG` tinyint(1) DEFAULT NULL COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`CHECK_RESULT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据质量管理-规则检查结果';

-- ----------------------------
-- Table structure for DATA_QUALITY_CHECK_RESULT_ERR
-- ----------------------------
DROP TABLE IF EXISTS `DATA_QUALITY_CHECK_RESULT_ERR`;
CREATE TABLE `DATA_QUALITY_CHECK_RESULT_ERR` (
  `CHECK_RESULT_ERR_ID` bigint(20) NOT NULL COMMENT '错误记录id',
  `ORGANIZER_ID` bigint(20) DEFAULT NULL COMMENT '组织ID',
  `CHECK_RESULT_ID` bigint(20) NOT NULL COMMENT '结果id',
  `ERROR_VALUE` text COMMENT '错误数据记录',
  `ERROR_DESC` text COMMENT '错误描述',
  `ERROR_SUGGESTION` text COMMENT '修改建议',
  `CREATE_USER` varchar(50) DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(50) DEFAULT NULL COMMENT '最后修改用户',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后时间更新',
  `DEL_FLAG` tinyint(1) DEFAULT NULL COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`CHECK_RESULT_ERR_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据质量管理-错误字段值记录';

-- ----------------------------
-- Table structure for DATA_QUALITY_COMPARE_SQL
-- ----------------------------
DROP TABLE IF EXISTS `DATA_QUALITY_COMPARE_SQL`;
CREATE TABLE `DATA_QUALITY_COMPARE_SQL` (
  `COMPARE_SQL_ID` bigint(64) NOT NULL,
  `ORGANIZER_ID` bigint(20) DEFAULT NULL,
  `REPOSITORY_NAME` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '资源库ID',
  `DATABASE_ID` bigint(100) DEFAULT NULL COMMENT 'ID in r_databsae table',
  `REF_DATABASE_ID` bigint(100) DEFAULT NULL COMMENT '参考sql数据库ID',
  `RULE_GROUP_ID` bigint(64) DEFAULT NULL COMMENT 'ID in profile_table_group',
  `COMPARE_NAME` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'profile_name',
  `COMPARE_DESC` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'compare desc',
  `COMPARE_TYPE` int(11) DEFAULT '1' COMMENT '1 for one value compare, 2 for multi-value compare, default 1',
  `SQL` longtext COLLATE utf8_unicode_ci,
  `REF_SQL` longtext COLLATE utf8_unicode_ci,
  `CREATE_TIME` datetime DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  `CREATE_USER` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改用户',
  `DEL_FLAG` int(11) DEFAULT NULL COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`COMPARE_SQL_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for DATA_QUALITY_COMPARE_SQL_FIELD
-- ----------------------------
DROP TABLE IF EXISTS `DATA_QUALITY_COMPARE_SQL_FIELD`;
CREATE TABLE `DATA_QUALITY_COMPARE_SQL_FIELD` (
  `COMPARE_SQL_FIELD_ID` bigint(64) NOT NULL COMMENT 'ID',
  `COMPARE_SQL_ID` bigint(64) NOT NULL DEFAULT '0' COMMENT '稽核ID',
  `FIELD_NAME` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '字段名称',
  `FIELD_TYPE` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '字段类型',
  `REF_FIELD_NAME` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '比较的字段名称',
  `FIELD_DESC` varchar(300) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '字段说明',
  `FIELD_COMPARE_TYPE` int(11) DEFAULT NULL COMMENT '0 等值比较，1 范围比较',
  `MIN_RATIO` decimal(20,5) DEFAULT NULL,
  `MAX_RATIO` decimal(20,5) DEFAULT NULL,
  `CREATE_USER` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改用户',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  `DEL_FLAG` int(11) DEFAULT NULL COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`COMPARE_SQL_FIELD_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for DATA_QUALITY_COMPARE_SQL_RESULT
-- ----------------------------
DROP TABLE IF EXISTS `DATA_QUALITY_COMPARE_SQL_RESULT`;
CREATE TABLE `DATA_QUALITY_COMPARE_SQL_RESULT` (
  `COMPARE_SQL_RESULT_ID` bigint(64) NOT NULL,
  `COMPARE_SQL_FIELD_ID` bigint(64) DEFAULT NULL,
  `ORGANIZER_ID` bigint(64) DEFAULT NULL,
  `FIELD_VALUE` varchar(300) COLLATE utf8_unicode_ci DEFAULT NULL,
  `REF_FIELD_VALUE` varchar(300) COLLATE utf8_unicode_ci DEFAULT NULL,
  `COMPARE_RESULT` int(11) DEFAULT NULL COMMENT '1 equals,   0  not equals',
  `CREATE_TIME` datetime DEFAULT NULL,
  `CREATE_USER` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改用户',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  `DEL_FLAG` int(11) DEFAULT NULL COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`COMPARE_SQL_RESULT_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for DATA_QUALITY_RULE
-- ----------------------------
DROP TABLE IF EXISTS `DATA_QUALITY_RULE`;
CREATE TABLE `DATA_QUALITY_RULE` (
  `RULE_ID` bigint(20) NOT NULL COMMENT '规则id',
  `ORGANIZER_ID` bigint(20) DEFAULT NULL COMMENT '组织ID',
  `RULE_GROUP_ID` bigint(20) DEFAULT NULL COMMENT '分组id',
  `DESCRIPTION` varchar(255) DEFAULT NULL COMMENT '规则描述',
  `RULE_TYPE` int(11) DEFAULT NULL COMMENT '规则类型',
  `DATABASE_ID` bigint(20) DEFAULT NULL COMMENT '数据库ID',
  `SCHEMA_NAME` varchar(200) DEFAULT NULL COMMENT '模式名',
  `TABLE_NAME` varchar(200) DEFAULT NULL COMMENT '主题表名',
  `FIELD_NAME` varchar(200) DEFAULT NULL COMMENT '字段名',
  `FIELD_TYPE` int(11) DEFAULT NULL COMMENT '字段类型',
  `FIELD_ORIGINAL_TYPE` varchar(50) DEFAULT NULL COMMENT '字段原生类型',
  `IS_ENABLE` tinyint(1) DEFAULT '1' COMMENT '是否启用',
  `CONDITION` varchar(255) DEFAULT NULL COMMENT '条件',
  `CREATE_USER` varchar(50) DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(50) DEFAULT NULL COMMENT '最后修改用户',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '最后时间更新',
  `DEL_FLAG` tinyint(1) DEFAULT NULL COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`RULE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据质量管理-规则管理';

-- ----------------------------
-- Table structure for DATA_QUALITY_RULE_ATTR
-- ----------------------------
DROP TABLE IF EXISTS `DATA_QUALITY_RULE_ATTR`;
CREATE TABLE `DATA_QUALITY_RULE_ATTR` (
  `RULE_ATTR_ID` bigint(20) NOT NULL COMMENT '规则属性值ID',
  `ORGANIZER_ID` bigint(20) DEFAULT NULL COMMENT '组织ID',
  `RULE_ID` bigint(20) NOT NULL COMMENT '规则id',
  `CODE` varchar(50) DEFAULT NULL,
  `VALUE_STR` varchar(255) DEFAULT NULL,
  `VALUE_RANK` int(11) DEFAULT NULL COMMENT '排序',
  `CREATE_USER` varchar(50) DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(50) DEFAULT NULL COMMENT '最后修改用户',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '最后时间更新',
  `DEL_FLAG` tinyint(1) DEFAULT NULL COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`RULE_ATTR_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据质量管理-规则属性值';

-- ----------------------------
-- Table structure for DATA_QUALITY_RULE_GROUP
-- ----------------------------
DROP TABLE IF EXISTS `DATA_QUALITY_RULE_GROUP`;
CREATE TABLE `DATA_QUALITY_RULE_GROUP` (
  `GROUP_ID` bigint(20) NOT NULL COMMENT '分组ID',
  `ORGANIZER_ID` bigint(20) NOT NULL COMMENT '组织ID',
  `GROUP_NAME` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '分组名称',
  `DESCRIPTION` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '分组描述',
  `CREATE_USER` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '最后修改用户',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '最后时间更新',
  `DEL_FLAG` tinyint(4) DEFAULT '0' COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`GROUP_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='数据质量规则分组';

-- ----------------------------
-- Table structure for HA_CLUSTER
-- ----------------------------
DROP TABLE IF EXISTS `HA_CLUSTER`;
CREATE TABLE `HA_CLUSTER` (
  `ID_CLUSTER` bigint(20) NOT NULL,
  `NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BASE_PORT` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `SOCKETS_BUFFER_SIZE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `SOCKETS_FLUSH_INTERVAL` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `SOCKETS_COMPRESSED` char(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DYNAMIC_CLUSTER` char(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `CREATE_USER` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改用户',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  `DEL_FLAG` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`ID_CLUSTER`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for HA_CLUSTER_SLAVE
-- ----------------------------
DROP TABLE IF EXISTS `HA_CLUSTER_SLAVE`;
CREATE TABLE `HA_CLUSTER_SLAVE` (
  `ID_CLUSTER_SLAVE` bigint(20) NOT NULL,
  `ID_CLUSTER` int(11) DEFAULT NULL,
  `ID_SLAVE` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID_CLUSTER_SLAVE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for HA_SLAVE
-- ----------------------------
DROP TABLE IF EXISTS `HA_SLAVE`;
CREATE TABLE `HA_SLAVE` (
  `ID_SLAVE` bigint(20) NOT NULL,
  `NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `HOST_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PORT` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `WEB_APP_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `USERNAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PASSWORD` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PROXY_HOST_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PROXY_PORT` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `NON_PROXY_HOSTS` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `MASTER` char(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `CREATE_USER` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改用户',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  `DEL_FLAG` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`ID_SLAVE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for HA_SLAVE_STATUS
-- ----------------------------
DROP TABLE IF EXISTS `HA_SLAVE_STATUS`;
CREATE TABLE `HA_SLAVE_STATUS` (
  `ID_STATUS` int(11) DEFAULT NULL,
  `ID_SLAVE` int(11) DEFAULT NULL,
  `IS_RUNING` int(11) DEFAULT NULL,
  `CPU_USAGE` float DEFAULT NULL,
  `MEMORY_USAGE` float DEFAULT NULL,
  `RUNING_JOBS_NUM` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for LOG_JOB
-- ----------------------------
DROP TABLE IF EXISTS `LOG_JOB`;
CREATE TABLE `LOG_JOB` (
  `LOG_JOB_ID` bigint(64) NOT NULL,
  `JOB_CONFIG_ID` bigint(20) DEFAULT NULL COMMENT '对应r_job表中的ID_JOB主键',
  `CHANNEL_ID` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '唯一，通UUID表示\r\n            ',
  `JOB_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `JOB_CN_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `STATUS` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `LINES_READ` bigint(20) DEFAULT NULL,
  `LINES_WRITTEN` bigint(20) DEFAULT NULL,
  `LINES_UPDATED` bigint(20) DEFAULT NULL,
  `LINES_INPUT` bigint(20) DEFAULT NULL,
  `LINES_OUTPUT` bigint(20) DEFAULT NULL,
  `LINES_REJECTED` bigint(20) DEFAULT NULL,
  `ERRORS` bigint(20) DEFAULT NULL,
  `STARTDATE` datetime DEFAULT NULL,
  `ENDDATE` datetime DEFAULT NULL,
  `LOGDATE` datetime DEFAULT NULL,
  `DEPDATE` datetime DEFAULT NULL,
  `REPLAYDATE` datetime DEFAULT NULL,
  `LOG_FIELD` mediumtext COLLATE utf8_unicode_ci,
  `EXECUTING_SERVER` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `EXECUTING_USER` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `EXCUTOR_TYPE` tinyint(4) DEFAULT NULL COMMENT '1:表示本地，2表示远程，3表示集群',
  `JOB_LOG` text COLLATE utf8_unicode_ci,
  `QRTZ_JOB_NAME` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `QRTZ_JOB_GROUP` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `FIRE_TIME` datetime DEFAULT NULL COMMENT '调度计划执行时间',
  PRIMARY KEY (`LOG_JOB_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for LOG_JOB_STEP
-- ----------------------------
DROP TABLE IF EXISTS `LOG_JOB_STEP`;
CREATE TABLE `LOG_JOB_STEP` (
  `LOG_JOB_STEP_ID` bigint(64) NOT NULL,
  `LOG_JOB_ID` bigint(64) DEFAULT NULL,
  `CHANNEL_ID` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `LOG_DATE` datetime DEFAULT NULL,
  `JOBNAME` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '父JOB的名称',
  `STEPNAME` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `LINES_READ` bigint(20) DEFAULT NULL,
  `LINES_WRITTEN` bigint(20) DEFAULT NULL,
  `LINES_UPDATED` bigint(20) DEFAULT NULL,
  `LINES_INPUT` bigint(20) DEFAULT NULL,
  `LINES_OUTPUT` bigint(20) DEFAULT NULL,
  `LINES_REJECTED` bigint(20) DEFAULT NULL,
  `ERRORS` bigint(20) DEFAULT NULL,
  `RESULT` bigint(20) DEFAULT NULL,
  `NR_RESULT_ROWS` tinyint(4) DEFAULT NULL,
  `NR_RESULT_FILES` bigint(20) DEFAULT NULL,
  `LOG_FIELD` bigint(20) DEFAULT NULL COMMENT '日志字段为这个特定的工作条目包含错误日志日志LOG_FIELD',
  `COPY_NR` bigint(20) DEFAULT NULL,
  `SETP_LOG` text COLLATE utf8_unicode_ci,
  PRIMARY KEY (`LOG_JOB_STEP_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for LOG_TRANS
-- ----------------------------
DROP TABLE IF EXISTS `LOG_TRANS`;
CREATE TABLE `LOG_TRANS` (
  `LOG_TRANS_ID` bigint(64) NOT NULL,
  `TRANS_CONFIG_ID` bigint(20) NOT NULL COMMENT '对应转换表 R_TRANSFORMATION 中的ID_TRANSFORMATION 字段',
  `CHANNEL_ID` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `TRANSNAME` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `STATUS` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `LINES_READ` bigint(20) DEFAULT NULL,
  `LINES_WRITTEN` bigint(20) DEFAULT NULL,
  `LINES_UPDATED` bigint(20) DEFAULT NULL,
  `LINES_INPUT` bigint(20) DEFAULT NULL,
  `LINES_OUTPUT` bigint(20) DEFAULT NULL,
  `LINES_REJECTED` bigint(20) DEFAULT NULL,
  `ERRORS` bigint(20) DEFAULT NULL,
  `STARTDATE` datetime DEFAULT NULL,
  `ENDDATE` datetime DEFAULT NULL,
  `LOGDATE` datetime DEFAULT NULL,
  `DEPDATE` datetime DEFAULT NULL,
  `REPLAYDATE` datetime DEFAULT NULL,
  `LOG_FIELD` mediumtext COLLATE utf8_unicode_ci,
  `EXECUTING_SERVER` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `EXECUTING_USER` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `EXCUTOR_TYPE` tinyint(4) DEFAULT NULL COMMENT '1:表示本地，2表示远程，3表示集群',
  `LOGINFO` text COLLATE utf8_unicode_ci,
  `TRANS_CN_NAME` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `QRTZ_JOB_NAME` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `QRTZ_JOB_GROUP` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `FIRE_TIME` datetime DEFAULT NULL COMMENT '调度计划执行时间',
  PRIMARY KEY (`LOG_TRANS_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for LOG_TRANS_STEP
-- ----------------------------
DROP TABLE IF EXISTS `LOG_TRANS_STEP`;
CREATE TABLE `LOG_TRANS_STEP` (
  `LOG_TRANS_ID` bigint(64) DEFAULT NULL,
  `LOG_STEP_ID` bigint(20) NOT NULL,
  `CHANNEL_ID` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `LOG_DATE` datetime DEFAULT NULL,
  `TRANSNAME` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `STEPNAME` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `STEP_COPY` int(11) DEFAULT NULL,
  `LINES_READ` bigint(20) DEFAULT NULL,
  `LINES_WRITTEN` bigint(20) DEFAULT NULL,
  `LINES_UPDATED` bigint(20) DEFAULT NULL,
  `LINES_INPUT` bigint(20) DEFAULT NULL,
  `LINES_OUTPUT` bigint(20) DEFAULT NULL,
  `LINES_REJECTED` bigint(20) DEFAULT NULL,
  `ERRORS` bigint(20) DEFAULT NULL,
  `SETP_LOG` text COLLATE utf8_unicode_ci,
  `COSTTIME` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `SPEED` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `STATUS` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`LOG_STEP_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for MDM_DATA_CLEAN
-- ----------------------------
DROP TABLE IF EXISTS `MDM_DATA_CLEAN`;
CREATE TABLE `MDM_DATA_CLEAN` (
  `ID` bigint(64) NOT NULL,
  `ID_MODEL` bigint(64) DEFAULT NULL,
  `ATTRIBUTE_MODEL` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `REPOSITORY_NAME` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `MDM_ID_DATABASE` bigint(20) DEFAULT NULL,
  `MDM_SCHEMA_NAME` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `MDM_TABLE_NAME` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `MDM_PRIMARY_KEY` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `MDM_COLUMN_NAME` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `MDM_WHERE_CONDITION` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `MAPING_MODE` int(11) DEFAULT NULL,
  `MAPING_ID_DATABASE` bigint(20) DEFAULT NULL,
  `MAPING_SCHEMA_NAME` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `MAPING_TABLE_NAME` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `CREATE_USER` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '最后修改用户',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '最后时间更新',
  `DEL_FLAG` tinyint(4) DEFAULT '0' COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='数据映射';

-- ----------------------------
-- Table structure for MDM_MODEL
-- ----------------------------
DROP TABLE IF EXISTS `MDM_MODEL`;
CREATE TABLE `MDM_MODEL` (
  `ID_MODEL` bigint(64) NOT NULL DEFAULT '0',
  `MODEL_NAME` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `MODEL_DESC` varchar(300) COLLATE utf8_unicode_ci DEFAULT NULL,
  `MODEL_STATUS` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `MODEL_AUTHOR` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `MODEL_NOTE` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `MODEL_CODE` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ORGANIZER_ID` bigint(64) NOT NULL,
  `CREATE_USER` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '最后修改用户',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '最后时间更新',
  `DEL_FLAG` tinyint(4) DEFAULT '0' COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`ID_MODEL`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='主数据模型';

-- ----------------------------
-- Table structure for MDM_MODEL_ATTRIBUTE
-- ----------------------------
DROP TABLE IF EXISTS `MDM_MODEL_ATTRIBUTE`;
CREATE TABLE `MDM_MODEL_ATTRIBUTE` (
  `ID_ATTRIBUTE` bigint(64) NOT NULL DEFAULT '0',
  `ID_MODEL` bigint(64) DEFAULT NULL,
  `ATTRIBUTE_ORDER` int(11) DEFAULT NULL,
  `ATTRIBUTE_NAME` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `STATISTIC_TYPE` int(11) DEFAULT NULL COMMENT '1.枚举，2.计算数值 3非结构化文本 4其它',
  `FIELD_NAME` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '字段名称',
  `FIELD_TYPE` int(11) DEFAULT NULL COMMENT 'kettle 里的数据类型编码',
  `FIELD_LENGTH` int(11) DEFAULT NULL COMMENT '字段长度',
  `IS_PRIMARY` char(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `FIELD_PRECISION` int(11) DEFAULT NULL,
  `CREATE_USER` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '最后修改用户',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '最后时间更新',
  `DEL_FLAG` tinyint(4) DEFAULT '0' COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`ID_ATTRIBUTE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='主数据模型属性';

-- ----------------------------
-- Table structure for MDM_MODEL_CONSTAINT
-- ----------------------------
DROP TABLE IF EXISTS `MDM_MODEL_CONSTAINT`;
CREATE TABLE `MDM_MODEL_CONSTAINT` (
  `ID_CONSTAINT` bigint(64) NOT NULL DEFAULT '0',
  `CONSTAINT_ORDER` int(11) DEFAULT NULL,
  `CONSTAINT_TYPE` int(11) DEFAULT NULL COMMENT '1 唯一  2 非空  3外键',
  `CONSTAINT_NAME` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '约束名称',
  `ID_ATTRIBUTE` bigint(64) DEFAULT NULL,
  `REFERENCE_ID_MODEL` bigint(64) DEFAULT NULL,
  `REFERENCE_ID_ATTRIBUTE` bigint(64) DEFAULT NULL,
  `ALIAS_TABLE_FLAG` int(11) DEFAULT '0' COMMENT '是否为字符类型的唯一约束数据，创建别名表（0否   1 是）',
  `CREATE_USER` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '最后修改用户',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '最后时间更新',
  `DEL_FLAG` tinyint(4) DEFAULT '0' COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`ID_CONSTAINT`) USING BTREE,
  KEY `mdm_model_constaint1` (`ID_ATTRIBUTE`) USING BTREE,
  CONSTRAINT `mdm_model_constaint1` FOREIGN KEY (`ID_ATTRIBUTE`) REFERENCES `MDM_MODEL_ATTRIBUTE` (`ID_ATTRIBUTE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='主数据模型属性';

-- ----------------------------
-- Table structure for MDM_REL_CONS_ATTR
-- ----------------------------
DROP TABLE IF EXISTS `MDM_REL_CONS_ATTR`;
CREATE TABLE `MDM_REL_CONS_ATTR` (
  `ID_REL_CONS_ATTR` bigint(64) NOT NULL AUTO_INCREMENT,
  `ID_CONSTAINT` bigint(64) DEFAULT NULL,
  `ID_ATTRIBUTE` bigint(64) DEFAULT NULL,
  PRIMARY KEY (`ID_REL_CONS_ATTR`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for MDM_TABLE
-- ----------------------------
DROP TABLE IF EXISTS `MDM_TABLE`;
CREATE TABLE `MDM_TABLE` (
  `ID_TABLE` bigint(64) NOT NULL DEFAULT '0',
  `ID_MODEL` bigint(64) DEFAULT NULL COMMENT 'ID in model table',
  `REPOSITORY_NAME` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '资源库名称',
  `ID_DATABASE` bigint(20) DEFAULT NULL COMMENT 'ID in r_databsae table',
  `SCHEMA_NAME` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `TABLE_NAME` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `CREATE_USER` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '最后修改用户',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '最后时间更新',
  `DEL_FLAG` tinyint(4) DEFAULT '0' COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`ID_TABLE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='主数据表';

-- ----------------------------
-- Table structure for PROFILE_TABLE
-- ----------------------------
DROP TABLE IF EXISTS `PROFILE_TABLE`;
CREATE TABLE `PROFILE_TABLE` (
  `ID_PROFILE_TABLE` bigint(64) NOT NULL AUTO_INCREMENT,
  `REPOSITORY_NAME` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '资源库名称',
  `ID_DATABASE` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'ID in r_databsae table',
  `ID_PROFIEL_TABLE_GROUP` bigint(64) DEFAULT NULL COMMENT 'ID in profile_table_group',
  `PROFIEL_NAME` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'profile_name',
  `PROFIEL_DESC` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'profile_name',
  `SCHEMA_NAME` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `TABLE_NAME` longtext COLLATE utf8_unicode_ci,
  `CONDITION` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  `USER_ID` bigint(64) DEFAULT NULL,
  `TABLE_NAME_TAG` int(11) DEFAULT NULL COMMENT '1:表示TABLE_NAME为表名 2：TABLE_NAME为sql',
  `CREATE_USER` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改用户',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  `DEL_FLAG` int(11) DEFAULT NULL COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`ID_PROFILE_TABLE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for PROFILE_TABLE_COLUMN
-- ----------------------------
DROP TABLE IF EXISTS `PROFILE_TABLE_COLUMN`;
CREATE TABLE `PROFILE_TABLE_COLUMN` (
  `ID_PROFILE_TABLE_COLUMN` bigint(64) NOT NULL AUTO_INCREMENT,
  `ID_PROFILE_TABLE` bigint(64) NOT NULL DEFAULT '0',
  `PROFILE_TABLE_COLUMN_NAME` varchar(300) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PROFILE_TABLE_COLUMN_TYPE` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '字段类型',
  `PROFILE_TABLE_COLUMN_SIZE` int(11) DEFAULT NULL,
  `PROFILE_TABLE_COLUMN_PRECISION` int(11) DEFAULT NULL COMMENT '精度',
  `PROFILE_TABLE_COLUMN_DESC` varchar(300) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PROFILE_TABLE_COLUMN_ORDER` int(11) DEFAULT NULL,
  `CREATE_USER` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改用户',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  `DEL_FLAG` int(11) DEFAULT NULL COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`ID_PROFILE_TABLE_COLUMN`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for PROFILE_TABLE_GROUP
-- ----------------------------
DROP TABLE IF EXISTS `PROFILE_TABLE_GROUP`;
CREATE TABLE `PROFILE_TABLE_GROUP` (
  `ID_PROFIEL_TABLE_GROUP` bigint(64) NOT NULL AUTO_INCREMENT,
  `PROFIEL_TABLE_GROUP_NAME` varchar(300) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PROFIEL_TABLE_GROUP_DESC` varchar(300) COLLATE utf8_unicode_ci NOT NULL DEFAULT '0',
  `CREATE_USER` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改用户',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  `DEL_FLAG` int(11) DEFAULT NULL COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`ID_PROFIEL_TABLE_GROUP`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of PROFILE_TABLE_GROUP
-- ----------------------------
BEGIN;
INSERT INTO `PROFILE_TABLE_GROUP` VALUES (1, 'A', 'A', NULL, NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for PROFILE_TABLE_RESULT
-- ----------------------------
DROP TABLE IF EXISTS `PROFILE_TABLE_RESULT`;
CREATE TABLE `PROFILE_TABLE_RESULT` (
  `ID_PROFILE_TABLE_RESULT` bigint(64) NOT NULL AUTO_INCREMENT,
  `ID_PROFILE_TABLE_COLUMN` bigint(64) NOT NULL COMMENT 'COLUMN ID',
  `INDICATOR_DATA_TYPE` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `INDICATOR_DATA_LENGTH` int(11) DEFAULT NULL,
  `INDICATOR_DATA_PRECISION` int(11) DEFAULT NULL,
  `INDICATOR_DATA_SCALE` int(11) DEFAULT NULL,
  `INDICATOR_ALL_COUNT` int(11) DEFAULT NULL,
  `INDICATOR_DISTINCT_COUNT` int(11) DEFAULT NULL,
  `INDICATOR_NULL_COUNT` int(11) DEFAULT NULL,
  `INDICATOR_ZERO_COUNT` int(11) DEFAULT NULL,
  `INDICATOR_AGG_AVG` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `INDICATOR_AGG_MAX` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `INDICATOR_AGG_MIN` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  `EXECUTE_SQL` longtext COLLATE utf8_unicode_ci,
  `CREATE_USER` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改用户',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  `DEL_FLAG` int(11) DEFAULT NULL COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`ID_PROFILE_TABLE_RESULT`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for QRTZ_BLOB_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_BLOB_TRIGGERS`;
CREATE TABLE `QRTZ_BLOB_TRIGGERS` (
  `SCHED_NAME` varchar(120) COLLATE utf8_unicode_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
  KEY `SCHED_NAME` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for QRTZ_CALENDARS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_CALENDARS`;
CREATE TABLE `QRTZ_CALENDARS` (
  `SCHED_NAME` varchar(120) COLLATE utf8_unicode_ci NOT NULL,
  `CALENDAR_NAME` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for QRTZ_CRON_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_CRON_TRIGGERS`;
CREATE TABLE `QRTZ_CRON_TRIGGERS` (
  `SCHED_NAME` varchar(120) COLLATE utf8_unicode_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `CRON_EXPRESSION` varchar(120) COLLATE utf8_unicode_ci NOT NULL,
  `TIME_ZONE_ID` varchar(80) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for QRTZ_FIRED_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_FIRED_TRIGGERS`;
CREATE TABLE `QRTZ_FIRED_TRIGGERS` (
  `SCHED_NAME` varchar(120) COLLATE utf8_unicode_ci NOT NULL,
  `ENTRY_ID` varchar(95) COLLATE utf8_unicode_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `INSTANCE_NAME` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `SCHED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) COLLATE utf8_unicode_ci NOT NULL,
  `JOB_NAME` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `JOB_GROUP` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`) USING BTREE,
  KEY `IDX_QRTZ_FT_TRIG_INST_NAME` (`SCHED_NAME`,`INSTANCE_NAME`) USING BTREE,
  KEY `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY` (`SCHED_NAME`,`INSTANCE_NAME`,`REQUESTS_RECOVERY`) USING BTREE,
  KEY `IDX_QRTZ_FT_J_G` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_FT_JG` (`SCHED_NAME`,`JOB_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_FT_T_G` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_FT_TG` (`SCHED_NAME`,`TRIGGER_GROUP`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for QRTZ_GROUP
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_GROUP`;
CREATE TABLE `QRTZ_GROUP` (
  `ID_GROUP` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT '分组ID',
  `ORGANIZER_ID` bigint(20) NOT NULL COMMENT '组织ID',
  `GROUP_NAME` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '分组名称',
  `DESCRIPTION` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '分组描述',
  `CREATE_USER` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '最后修改用户',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '最后时间更新',
  `DEL_FLAG` tinyint(4) DEFAULT '0' COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`ID_GROUP`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='调度分组';

-- ----------------------------
-- Table structure for QRTZ_JOB_DETAILS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_JOB_DETAILS`;
CREATE TABLE `QRTZ_JOB_DETAILS` (
  `SCHED_NAME` varchar(120) COLLATE utf8_unicode_ci NOT NULL,
  `JOB_NAME` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `JOB_GROUP` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DESCRIPTION` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) COLLATE utf8_unicode_ci NOT NULL,
  `IS_DURABLE` varchar(1) COLLATE utf8_unicode_ci NOT NULL,
  `IS_NONCONCURRENT` varchar(1) COLLATE utf8_unicode_ci NOT NULL,
  `IS_UPDATE_DATA` varchar(1) COLLATE utf8_unicode_ci NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) COLLATE utf8_unicode_ci NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_J_REQ_RECOVERY` (`SCHED_NAME`,`REQUESTS_RECOVERY`) USING BTREE,
  KEY `IDX_QRTZ_J_GRP` (`SCHED_NAME`,`JOB_GROUP`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for QRTZ_JOB_ORGANIZER
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_JOB_ORGANIZER`;
CREATE TABLE `QRTZ_JOB_ORGANIZER` (
  `ORGANIZER_ID` bigint(64) NOT NULL COMMENT '组织ID',
  `JOB_NAME` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT '调度名称',
  `JOB_GROUP` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT '调度分组名称',
  PRIMARY KEY (`JOB_NAME`,`JOB_GROUP`,`ORGANIZER_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='调度用户表';

-- ----------------------------
-- Table structure for QRTZ_LOCKS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_LOCKS`;
CREATE TABLE `QRTZ_LOCKS` (
  `SCHED_NAME` varchar(120) COLLATE utf8_unicode_ci NOT NULL,
  `LOCK_NAME` varchar(40) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of QRTZ_LOCKS
-- ----------------------------
BEGIN;
INSERT INTO `QRTZ_LOCKS` VALUES ('quartzScheduler', 'TRIGGER_ACCESS');
COMMIT;

-- ----------------------------
-- Table structure for QRTZ_PAUSED_TRIGGER_GRPS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_PAUSED_TRIGGER_GRPS`;
CREATE TABLE `QRTZ_PAUSED_TRIGGER_GRPS` (
  `SCHED_NAME` varchar(120) COLLATE utf8_unicode_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for QRTZ_SCHEDULER_STATE
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SCHEDULER_STATE`;
CREATE TABLE `QRTZ_SCHEDULER_STATE` (
  `SCHED_NAME` varchar(120) COLLATE utf8_unicode_ci NOT NULL,
  `INSTANCE_NAME` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for QRTZ_SIMPLE_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SIMPLE_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPLE_TRIGGERS` (
  `SCHED_NAME` varchar(120) COLLATE utf8_unicode_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for QRTZ_SIMPROP_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SIMPROP_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPROP_TRIGGERS` (
  `SCHED_NAME` varchar(120) COLLATE utf8_unicode_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `STR_PROP_1` varchar(512) COLLATE utf8_unicode_ci DEFAULT NULL,
  `STR_PROP_2` varchar(512) COLLATE utf8_unicode_ci DEFAULT NULL,
  `STR_PROP_3` varchar(512) COLLATE utf8_unicode_ci DEFAULT NULL,
  `INT_PROP_1` int(11) DEFAULT NULL,
  `INT_PROP_2` int(11) DEFAULT NULL,
  `LONG_PROP_1` bigint(20) DEFAULT NULL,
  `LONG_PROP_2` bigint(20) DEFAULT NULL,
  `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
  `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for QRTZ_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_TRIGGERS`;
CREATE TABLE `QRTZ_TRIGGERS` (
  `SCHED_NAME` varchar(120) COLLATE utf8_unicode_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `JOB_NAME` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `JOB_GROUP` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `DESCRIPTION` varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) COLLATE utf8_unicode_ci NOT NULL,
  `TRIGGER_TYPE` varchar(8) COLLATE utf8_unicode_ci NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) DEFAULT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_T_J` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_T_JG` (`SCHED_NAME`,`JOB_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_T_C` (`SCHED_NAME`,`CALENDAR_NAME`) USING BTREE,
  KEY `IDX_QRTZ_T_G` (`SCHED_NAME`,`TRIGGER_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_T_STATE` (`SCHED_NAME`,`TRIGGER_STATE`) USING BTREE,
  KEY `IDX_QRTZ_T_N_STATE` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`) USING BTREE,
  KEY `IDX_QRTZ_T_N_G_STATE` (`SCHED_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`) USING BTREE,
  KEY `IDX_QRTZ_T_NEXT_FIRE_TIME` (`SCHED_NAME`,`NEXT_FIRE_TIME`) USING BTREE,
  KEY `IDX_QRTZ_T_NFT_ST` (`SCHED_NAME`,`TRIGGER_STATE`,`NEXT_FIRE_TIME`) USING BTREE,
  KEY `IDX_QRTZ_T_NFT_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`) USING BTREE,
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_STATE`) USING BTREE,
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_GROUP`,`TRIGGER_STATE`) USING BTREE,
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `QRTZ_JOB_DETAILS` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_CLUSTER
-- ----------------------------
DROP TABLE IF EXISTS `R_CLUSTER`;
CREATE TABLE `R_CLUSTER` (
  `ID_CLUSTER` bigint(20) NOT NULL,
  `NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `BASE_PORT` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `SOCKETS_BUFFER_SIZE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `SOCKETS_FLUSH_INTERVAL` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `SOCKETS_COMPRESSED` tinyint(1) DEFAULT NULL,
  `DYNAMIC_CLUSTER` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID_CLUSTER`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_CLUSTER_SLAVE
-- ----------------------------
DROP TABLE IF EXISTS `R_CLUSTER_SLAVE`;
CREATE TABLE `R_CLUSTER_SLAVE` (
  `ID_CLUSTER_SLAVE` bigint(20) NOT NULL,
  `ID_CLUSTER` int(11) DEFAULT NULL,
  `ID_SLAVE` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID_CLUSTER_SLAVE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_CONDITION
-- ----------------------------
DROP TABLE IF EXISTS `R_CONDITION`;
CREATE TABLE `R_CONDITION` (
  `ID_CONDITION` bigint(20) NOT NULL,
  `ID_CONDITION_PARENT` int(11) DEFAULT NULL,
  `NEGATED` tinyint(1) DEFAULT NULL,
  `OPERATOR` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `LEFT_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `CONDITION_FUNCTION` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `RIGHT_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ID_VALUE_RIGHT` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID_CONDITION`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_DATABASE
-- ----------------------------
DROP TABLE IF EXISTS `R_DATABASE`;
CREATE TABLE `R_DATABASE` (
  `ID_DATABASE` bigint(20) NOT NULL,
  `NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ID_DATABASE_TYPE` int(11) DEFAULT NULL,
  `ID_DATABASE_CONTYPE` int(11) DEFAULT NULL,
  `HOST_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DATABASE_NAME` mediumtext COLLATE utf8_unicode_ci,
  `PORT` int(11) DEFAULT NULL,
  `USERNAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PASSWORD` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `SERVERNAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DATA_TBS` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `INDEX_TBS` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ORGANIZER_ID` bigint(64) NOT NULL,
  `CREATE_USER` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UPDATE_USER` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID_DATABASE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_DATABASE_ATTRIBUTE
-- ----------------------------
DROP TABLE IF EXISTS `R_DATABASE_ATTRIBUTE`;
CREATE TABLE `R_DATABASE_ATTRIBUTE` (
  `ID_DATABASE_ATTRIBUTE` bigint(20) NOT NULL,
  `ID_DATABASE` int(11) DEFAULT NULL,
  `CODE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `VALUE_STR` mediumtext COLLATE utf8_unicode_ci,
  PRIMARY KEY (`ID_DATABASE_ATTRIBUTE`) USING BTREE,
  UNIQUE KEY `IDX_RDAT` (`ID_DATABASE`,`CODE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_DATABASE_CONTYPE
-- ----------------------------
DROP TABLE IF EXISTS `R_DATABASE_CONTYPE`;
CREATE TABLE `R_DATABASE_CONTYPE` (
  `ID_DATABASE_CONTYPE` bigint(20) NOT NULL,
  `CODE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DESCRIPTION` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID_DATABASE_CONTYPE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of R_DATABASE_CONTYPE
-- ----------------------------
BEGIN;
INSERT INTO `R_DATABASE_CONTYPE` VALUES (1, 'Native', 'Native (JDBC)');
INSERT INTO `R_DATABASE_CONTYPE` VALUES (2, 'ODBC', 'ODBC');
INSERT INTO `R_DATABASE_CONTYPE` VALUES (3, 'OCI', 'OCI');
INSERT INTO `R_DATABASE_CONTYPE` VALUES (4, 'Plugin', 'Plugin specific access method');
INSERT INTO `R_DATABASE_CONTYPE` VALUES (5, 'JNDI', 'JNDI');
INSERT INTO `R_DATABASE_CONTYPE` VALUES (6, ',', 'Custom');
COMMIT;

-- ----------------------------
-- Table structure for R_DATABASE_TYPE
-- ----------------------------
DROP TABLE IF EXISTS `R_DATABASE_TYPE`;
CREATE TABLE `R_DATABASE_TYPE` (
  `ID_DATABASE_TYPE` bigint(20) NOT NULL,
  `CODE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DESCRIPTION` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID_DATABASE_TYPE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of R_DATABASE_TYPE
-- ----------------------------
BEGIN;
INSERT INTO `R_DATABASE_TYPE` VALUES (1, 'INGRES', 'Ingres');
INSERT INTO `R_DATABASE_TYPE` VALUES (2, 'MARIADB', 'MariaDB');
INSERT INTO `R_DATABASE_TYPE` VALUES (3, 'INTERBASE', 'Borland Interbase');
INSERT INTO `R_DATABASE_TYPE` VALUES (4, 'INFOBRIGHT', 'Infobright');
INSERT INTO `R_DATABASE_TYPE` VALUES (5, 'HIVE', 'Hadoop Hive');
INSERT INTO `R_DATABASE_TYPE` VALUES (6, 'ORACLE', 'Oracle');
INSERT INTO `R_DATABASE_TYPE` VALUES (7, 'EXTENDB', 'ExtenDB');
INSERT INTO `R_DATABASE_TYPE` VALUES (8, 'MSACCESS', 'MS Access');
INSERT INTO `R_DATABASE_TYPE` VALUES (9, 'SYBASE', 'Sybase');
INSERT INTO `R_DATABASE_TYPE` VALUES (10, 'PALO', 'Palo MOLAP Server');
INSERT INTO `R_DATABASE_TYPE` VALUES (11, 'INFORMIX', 'Informix');
INSERT INTO `R_DATABASE_TYPE` VALUES (12, 'LucidDB', 'LucidDB');
INSERT INTO `R_DATABASE_TYPE` VALUES (13, 'SPARKSIMBA', 'SparkSQL');
INSERT INTO `R_DATABASE_TYPE` VALUES (14, 'TERADATA', 'Teradata');
INSERT INTO `R_DATABASE_TYPE` VALUES (15, 'UNIVERSE', 'UniVerse database');
INSERT INTO `R_DATABASE_TYPE` VALUES (16, 'HIVE2', 'Hadoop Hive 2');
INSERT INTO `R_DATABASE_TYPE` VALUES (17, 'MONETDB', 'MonetDB');
INSERT INTO `R_DATABASE_TYPE` VALUES (18, 'CACHE', 'Intersystems Cache');
INSERT INTO `R_DATABASE_TYPE` VALUES (19, 'MSSQL', 'MS SQL Server');
INSERT INTO `R_DATABASE_TYPE` VALUES (20, 'GREENPLUM', 'Greenplum');
INSERT INTO `R_DATABASE_TYPE` VALUES (21, 'GENERIC', 'Generic database');
INSERT INTO `R_DATABASE_TYPE` VALUES (22, 'SQLITE', 'SQLite');
INSERT INTO `R_DATABASE_TYPE` VALUES (23, 'REMEDY-AR-SYSTEM', 'Remedy Action Request System');
INSERT INTO `R_DATABASE_TYPE` VALUES (24, 'MONDRIAN', 'Native Mondrian');
INSERT INTO `R_DATABASE_TYPE` VALUES (25, 'NETEZZA', 'Netezza');
INSERT INTO `R_DATABASE_TYPE` VALUES (26, 'VERTICA5', 'Vertica 5+');
INSERT INTO `R_DATABASE_TYPE` VALUES (27, 'POSTGRESQL', 'PostgreSQL');
INSERT INTO `R_DATABASE_TYPE` VALUES (28, 'KettleThin', 'Pentaho Data Services');
INSERT INTO `R_DATABASE_TYPE` VALUES (29, 'EXASOL4', 'Exasol 4');
INSERT INTO `R_DATABASE_TYPE` VALUES (30, 'HYPERSONIC', 'Hypersonic');
INSERT INTO `R_DATABASE_TYPE` VALUES (31, 'AS/400', 'AS/400');
INSERT INTO `R_DATABASE_TYPE` VALUES (32, 'ORACLERDB', 'Oracle RDB');
INSERT INTO `R_DATABASE_TYPE` VALUES (33, 'DBASE', 'dBase III, IV or 5');
INSERT INTO `R_DATABASE_TYPE` VALUES (34, 'KINGBASEES', 'KingbaseES');
INSERT INTO `R_DATABASE_TYPE` VALUES (35, 'SAPR3', 'SAP ERP System');
INSERT INTO `R_DATABASE_TYPE` VALUES (36, 'SQLBASE', 'Gupta SQL Base');
INSERT INTO `R_DATABASE_TYPE` VALUES (37, 'DERBY', 'Apache Derby');
INSERT INTO `R_DATABASE_TYPE` VALUES (38, 'VERTICA', 'Vertica');
INSERT INTO `R_DATABASE_TYPE` VALUES (39, 'INFINIDB', 'Calpont InfiniDB');
INSERT INTO `R_DATABASE_TYPE` VALUES (40, 'MYSQL', 'MySQL');
INSERT INTO `R_DATABASE_TYPE` VALUES (41, 'MSSQLNATIVE', 'MS SQL Server (Native)');
INSERT INTO `R_DATABASE_TYPE` VALUES (42, 'H2', 'H2');
INSERT INTO `R_DATABASE_TYPE` VALUES (43, 'IMPALA', 'Impala');
INSERT INTO `R_DATABASE_TYPE` VALUES (44, 'SAPDB', 'MaxDB (SAP DB)');
INSERT INTO `R_DATABASE_TYPE` VALUES (45, 'VECTORWISE', 'Ingres VectorWise');
INSERT INTO `R_DATABASE_TYPE` VALUES (46, 'DB2', 'IBM DB2');
INSERT INTO `R_DATABASE_TYPE` VALUES (47, 'NEOVIEW', 'Neoview');
INSERT INTO `R_DATABASE_TYPE` VALUES (48, 'IMPALASIMBA', 'Cloudera Impala');
INSERT INTO `R_DATABASE_TYPE` VALUES (49, 'SYBASEIQ', 'SybaseIQ');
INSERT INTO `R_DATABASE_TYPE` VALUES (50, 'REDSHIFT', 'Redshift');
INSERT INTO `R_DATABASE_TYPE` VALUES (51, 'FIREBIRD', 'Firebird SQL');
INSERT INTO `R_DATABASE_TYPE` VALUES (52, 'OpenERPDatabaseMeta', 'OpenERP Server');
INSERT INTO `R_DATABASE_TYPE` VALUES (53, 'GOOGLEBIGQUERY', 'Google BigQuery');
COMMIT;

-- ----------------------------
-- Table structure for R_DEPENDENCY
-- ----------------------------
DROP TABLE IF EXISTS `R_DEPENDENCY`;
CREATE TABLE `R_DEPENDENCY` (
  `ID_DEPENDENCY` bigint(20) NOT NULL,
  `ID_TRANSFORMATION` int(11) DEFAULT NULL,
  `ID_DATABASE` int(11) DEFAULT NULL,
  `TABLE_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `FIELD_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID_DEPENDENCY`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_DIRECTORY
-- ----------------------------
DROP TABLE IF EXISTS `R_DIRECTORY`;
CREATE TABLE `R_DIRECTORY` (
  `ID_DIRECTORY` bigint(20) NOT NULL,
  `ID_DIRECTORY_PARENT` int(11) DEFAULT NULL,
  `DIRECTORY_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID_DIRECTORY`) USING BTREE,
  UNIQUE KEY `IDX_RDIR` (`ID_DIRECTORY_PARENT`,`DIRECTORY_NAME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_ELEMENT
-- ----------------------------
DROP TABLE IF EXISTS `R_ELEMENT`;
CREATE TABLE `R_ELEMENT` (
  `ID_ELEMENT` bigint(20) NOT NULL,
  `ID_ELEMENT_TYPE` int(11) DEFAULT NULL,
  `NAME` text COLLATE utf8_unicode_ci,
  PRIMARY KEY (`ID_ELEMENT`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_ELEMENT_ATTRIBUTE
-- ----------------------------
DROP TABLE IF EXISTS `R_ELEMENT_ATTRIBUTE`;
CREATE TABLE `R_ELEMENT_ATTRIBUTE` (
  `ID_ELEMENT_ATTRIBUTE` bigint(20) NOT NULL,
  `ID_ELEMENT` int(11) DEFAULT NULL,
  `ID_ELEMENT_ATTRIBUTE_PARENT` int(11) DEFAULT NULL,
  `ATTR_KEY` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ATTR_VALUE` mediumtext COLLATE utf8_unicode_ci,
  PRIMARY KEY (`ID_ELEMENT_ATTRIBUTE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_ELEMENT_TYPE
-- ----------------------------
DROP TABLE IF EXISTS `R_ELEMENT_TYPE`;
CREATE TABLE `R_ELEMENT_TYPE` (
  `ID_ELEMENT_TYPE` bigint(20) NOT NULL,
  `ID_NAMESPACE` int(11) DEFAULT NULL,
  `NAME` text COLLATE utf8_unicode_ci,
  `DESCRIPTION` mediumtext COLLATE utf8_unicode_ci,
  PRIMARY KEY (`ID_ELEMENT_TYPE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_JOB
-- ----------------------------
DROP TABLE IF EXISTS `R_JOB`;
CREATE TABLE `R_JOB` (
  `ID_JOB` bigint(20) NOT NULL,
  `ID_DIRECTORY` int(11) DEFAULT NULL,
  `NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DESCRIPTION` mediumtext COLLATE utf8_unicode_ci,
  `EXTENDED_DESCRIPTION` mediumtext COLLATE utf8_unicode_ci,
  `JOB_VERSION` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `JOB_STATUS` int(11) DEFAULT NULL,
  `ID_DATABASE_LOG` int(11) DEFAULT NULL,
  `TABLE_NAME_LOG` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `CREATED_USER` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `MODIFIED_USER` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `MODIFIED_DATE` datetime DEFAULT NULL,
  `USE_BATCH_ID` tinyint(1) DEFAULT NULL,
  `PASS_BATCH_ID` tinyint(1) DEFAULT NULL,
  `USE_LOGFIELD` tinyint(1) DEFAULT NULL,
  `SHARED_FILE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID_JOB`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_JOBENTRY
-- ----------------------------
DROP TABLE IF EXISTS `R_JOBENTRY`;
CREATE TABLE `R_JOBENTRY` (
  `ID_JOBENTRY` bigint(20) NOT NULL,
  `ID_JOB` int(11) DEFAULT NULL,
  `ID_JOBENTRY_TYPE` int(11) DEFAULT NULL,
  `NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DESCRIPTION` mediumtext COLLATE utf8_unicode_ci,
  PRIMARY KEY (`ID_JOBENTRY`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_JOBENTRY_ATTRIBUTE
-- ----------------------------
DROP TABLE IF EXISTS `R_JOBENTRY_ATTRIBUTE`;
CREATE TABLE `R_JOBENTRY_ATTRIBUTE` (
  `ID_JOBENTRY_ATTRIBUTE` bigint(20) NOT NULL,
  `ID_JOB` int(11) DEFAULT NULL,
  `ID_JOBENTRY` int(11) DEFAULT NULL,
  `NR` int(11) DEFAULT NULL,
  `CODE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `VALUE_NUM` double DEFAULT NULL,
  `VALUE_STR` mediumtext COLLATE utf8_unicode_ci,
  PRIMARY KEY (`ID_JOBENTRY_ATTRIBUTE`) USING BTREE,
  UNIQUE KEY `IDX_RJEA` (`ID_JOBENTRY_ATTRIBUTE`,`CODE`,`NR`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_JOBENTRY_COPY
-- ----------------------------
DROP TABLE IF EXISTS `R_JOBENTRY_COPY`;
CREATE TABLE `R_JOBENTRY_COPY` (
  `ID_JOBENTRY_COPY` bigint(20) NOT NULL,
  `ID_JOBENTRY` int(11) DEFAULT NULL,
  `ID_JOB` int(11) DEFAULT NULL,
  `ID_JOBENTRY_TYPE` int(11) DEFAULT NULL,
  `NR` int(11) DEFAULT NULL,
  `GUI_LOCATION_X` int(11) DEFAULT NULL,
  `GUI_LOCATION_Y` int(11) DEFAULT NULL,
  `GUI_DRAW` tinyint(1) DEFAULT NULL,
  `PARALLEL` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID_JOBENTRY_COPY`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_JOBENTRY_DATABASE
-- ----------------------------
DROP TABLE IF EXISTS `R_JOBENTRY_DATABASE`;
CREATE TABLE `R_JOBENTRY_DATABASE` (
  `ID_JOB` int(11) DEFAULT NULL,
  `ID_JOBENTRY` int(11) DEFAULT NULL,
  `ID_DATABASE` int(11) DEFAULT NULL,
  KEY `IDX_RJD1` (`ID_JOB`) USING BTREE,
  KEY `IDX_RJD2` (`ID_DATABASE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_JOBENTRY_TYPE
-- ----------------------------
DROP TABLE IF EXISTS `R_JOBENTRY_TYPE`;
CREATE TABLE `R_JOBENTRY_TYPE` (
  `ID_JOBENTRY_TYPE` bigint(20) NOT NULL,
  `CODE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DESCRIPTION` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID_JOBENTRY_TYPE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of R_JOBENTRY_TYPE
-- ----------------------------
BEGIN;
INSERT INTO `R_JOBENTRY_TYPE` VALUES (1, 'HadoopTransJobExecutorPlugin', 'Pentaho MapReduce');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (2, 'WEBSERVICE_AVAILABLE', '检查web服务是否可用');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (3, 'MYSQL_BULK_FILE', '从 Mysql 批量导出到文件');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (4, 'COPY_MOVE_RESULT_FILENAMES', '复制/移动结果文件');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (5, 'XSD_VALIDATOR', 'XSD Validator');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (6, 'SparkSubmit', 'Spark Submit');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (7, 'OozieJobExecutor', 'Oozie Job Executor');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (8, 'SPECIAL', '特殊作业项');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (9, 'FILE_COMPARE', '比较文件');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (10, 'CREATE_FOLDER', '创建一个目录');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (11, 'MAIL_VALIDATOR', '邮件验证');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (12, 'PALO_CUBE_DELETE', 'Palo Cube Delete');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (13, 'MAIL', '发送邮件');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (14, 'TRUNCATE_TABLES', '清空表');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (15, 'MSGBOX_INFO', '显示消息对话框');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (16, 'WAIT_FOR_SQL', '等待SQL');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (17, 'FTPS_GET', 'FTPS 下载');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (18, 'FTP_DELETE', 'FTP 删除');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (19, 'COLUMNS_EXIST', '检查列是否存在');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (20, 'CHECK_FILES_LOCKED', '检查文件是否被锁');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (21, 'UNZIP', '解压缩文件');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (22, 'JOB', '作业');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (23, 'SqoopImport', 'Sqoop Import');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (24, 'DELETE_FILE', '删除一个文件');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (25, 'SHELL', 'Shell');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (26, 'ABORT', '中止作业');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (27, 'HiveJobExecutorPlugin', 'Amazon Hive Job Executor');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (28, 'XML_WELL_FORMED', 'Check if XML file is well formed');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (29, 'SFTP', 'SFTP 下载');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (30, 'HTTP', 'HTTP');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (31, 'HadoopCopyFilesPlugin', 'Hadoop Copy Files');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (32, 'FTP_PUT', 'FTP 上传');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (33, 'SQL', 'SQL');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (34, 'WRITE_TO_FILE', '写入文件');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (35, 'PGP_VERIFY_FILES', '用PGP验证文件签名');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (36, 'DOS_UNIX_CONVERTER', 'DOS和UNIX之间的文本转换');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (37, 'PGP_DECRYPT_FILES', '用PGP解密文件');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (38, 'TALEND_JOB_EXEC', 'Talend 作业执行');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (39, 'EVAL', '使用 JavaScript 脚本验证');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (40, 'HadoopPigScriptExecutorPlugin', 'Pig Script Executor');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (41, 'DELAY', '等待');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (42, 'HL7MLLPAcknowledge', 'HL7 MLLP Acknowledge');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (43, 'PALO_CUBE_CREATE', 'Palo Cube Create');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (44, 'FTP', 'FTP 下载');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (45, 'DATASOURCE_PUBLISH', 'Publish Model');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (46, 'FOLDERS_COMPARE', '比较目录');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (47, 'HadoopJobExecutorPlugin', 'Hadoop Job Executor ');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (48, 'ZIP_FILE', 'Zip 压缩文件');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (49, 'GET_POP', 'POP 收信');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (50, 'TRANS', '转换');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (51, 'SEND_NAGIOS_PASSIVE_CHECK', '发送Nagios 被动检查');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (52, 'SET_VARIABLES', '设置变量');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (53, 'MS_ACCESS_BULK_LOAD', 'MS Access Bulk Load');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (54, 'DummyJob', 'Example Job');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (55, 'COPY_FILES', '复制文件');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (56, 'EVAL_FILES_METRICS', '计算文件大小或个数');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (57, 'DataRefineryBuildModel', 'Build Model');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (58, 'PING', 'Ping 一台主机');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (59, 'ADD_RESULT_FILENAMES', '添加文件到结果文件中');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (60, 'DELETE_FOLDERS', '删除目录');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (61, 'CHECK_DB_CONNECTIONS', '检查数据库连接');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (62, 'FILE_EXISTS', '检查一个文件是否存在');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (63, 'EVAL_TABLE_CONTENT', '计算表中的记录数');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (64, 'FILES_EXIST', '检查多个文件是否存在');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (65, 'SFTPPUT', 'SFTP 上传');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (66, 'FTPS_PUT', 'FTPS 上传');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (67, 'DELETE_RESULT_FILENAMES', '从结果文件中删除文件');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (68, 'DELETE_FILES', '删除多个文件');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (69, 'PGP_ENCRYPT_FILES', '用PGP加密文件');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (70, 'SqoopExport', 'Sqoop Export');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (71, 'WRITE_TO_LOG', '写日志');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (72, 'SUCCESS', '成功');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (73, 'WAIT_FOR_FILE', '等待文件');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (74, 'MSSQL_BULK_LOAD', 'SQLServer 批量加载');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (75, 'TELNET', '远程登录一台主机');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (76, 'MOVE_FILES', '移动文件');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (77, 'XSLT', 'XSL Transformation');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (78, 'EMRJobExecutorPlugin', 'Amazon EMR Job Executor');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (79, 'DTD_VALIDATOR', 'DTD Validator');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (80, 'HL7MLLPInput', 'HL7 MLLP Input');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (81, 'FOLDER_IS_EMPTY', '检查目录是否为空');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (82, 'SIMPLE_EVAL', '检验字段的值');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (83, 'EXPORT_REPOSITORY', '导出资源库到XML文件');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (84, 'TABLE_EXISTS', '检查表是否存在');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (85, 'SYSLOG', '用 Syslog 发送信息');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (86, 'MYSQL_BULK_LOAD', 'Mysql 批量加载');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (87, 'CREATE_FILE', '创建文件');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (88, 'SNMP_TRAP', '发送 SNMP 自陷');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (89, 'CONNECTED_TO_REPOSITORY', '检查是否连接到资源库');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (90, 'GoogleBigQueryStorageLoad', 'Google BigQuery Loader');
INSERT INTO `R_JOBENTRY_TYPE` VALUES (91, 'OBSPUT', 'OBS上传');
COMMIT;

-- ----------------------------
-- Table structure for R_JOB_ATTRIBUTE
-- ----------------------------
DROP TABLE IF EXISTS `R_JOB_ATTRIBUTE`;
CREATE TABLE `R_JOB_ATTRIBUTE` (
  `ID_JOB_ATTRIBUTE` bigint(20) NOT NULL,
  `ID_JOB` int(11) DEFAULT NULL,
  `NR` int(11) DEFAULT NULL,
  `CODE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `VALUE_NUM` bigint(20) DEFAULT NULL,
  `VALUE_STR` mediumtext COLLATE utf8_unicode_ci,
  PRIMARY KEY (`ID_JOB_ATTRIBUTE`) USING BTREE,
  UNIQUE KEY `IDX_JATT` (`ID_JOB`,`CODE`,`NR`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_JOB_HOP
-- ----------------------------
DROP TABLE IF EXISTS `R_JOB_HOP`;
CREATE TABLE `R_JOB_HOP` (
  `ID_JOB_HOP` bigint(20) NOT NULL,
  `ID_JOB` int(11) DEFAULT NULL,
  `ID_JOBENTRY_COPY_FROM` int(11) DEFAULT NULL,
  `ID_JOBENTRY_COPY_TO` int(11) DEFAULT NULL,
  `ENABLED` tinyint(1) DEFAULT NULL,
  `EVALUATION` tinyint(1) DEFAULT NULL,
  `UNCONDITIONAL` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID_JOB_HOP`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_JOB_LOCK
-- ----------------------------
DROP TABLE IF EXISTS `R_JOB_LOCK`;
CREATE TABLE `R_JOB_LOCK` (
  `ID_JOB_LOCK` bigint(20) NOT NULL,
  `ID_JOB` int(11) DEFAULT NULL,
  `ID_USER` int(11) DEFAULT NULL,
  `LOCK_MESSAGE` mediumtext COLLATE utf8_unicode_ci,
  `LOCK_DATE` datetime DEFAULT NULL,
  PRIMARY KEY (`ID_JOB_LOCK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_JOB_NOTE
-- ----------------------------
DROP TABLE IF EXISTS `R_JOB_NOTE`;
CREATE TABLE `R_JOB_NOTE` (
  `ID_JOB` int(11) DEFAULT NULL,
  `ID_NOTE` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_LOG
-- ----------------------------
DROP TABLE IF EXISTS `R_LOG`;
CREATE TABLE `R_LOG` (
  `ID_LOG` bigint(20) NOT NULL,
  `NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ID_LOGLEVEL` int(11) DEFAULT NULL,
  `LOGTYPE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `FILENAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `FILEEXTENTION` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ADD_DATE` tinyint(1) DEFAULT NULL,
  `ADD_TIME` tinyint(1) DEFAULT NULL,
  `ID_DATABASE_LOG` int(11) DEFAULT NULL,
  `TABLE_NAME_LOG` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID_LOG`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_LOGLEVEL
-- ----------------------------
DROP TABLE IF EXISTS `R_LOGLEVEL`;
CREATE TABLE `R_LOGLEVEL` (
  `ID_LOGLEVEL` bigint(20) NOT NULL,
  `CODE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DESCRIPTION` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID_LOGLEVEL`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of R_LOGLEVEL
-- ----------------------------
BEGIN;
INSERT INTO `R_LOGLEVEL` VALUES (1, 'Error', '错误日志');
INSERT INTO `R_LOGLEVEL` VALUES (2, 'Minimal', '最小日志');
INSERT INTO `R_LOGLEVEL` VALUES (3, 'Basic', '基本日志');
INSERT INTO `R_LOGLEVEL` VALUES (4, 'Detailed', '详细日志');
INSERT INTO `R_LOGLEVEL` VALUES (5, 'Debug', '调试');
INSERT INTO `R_LOGLEVEL` VALUES (6, 'Rowlevel', '行级日志(非常详细)');
COMMIT;

-- ----------------------------
-- Table structure for R_NAMESPACE
-- ----------------------------
DROP TABLE IF EXISTS `R_NAMESPACE`;
CREATE TABLE `R_NAMESPACE` (
  `ID_NAMESPACE` bigint(20) NOT NULL,
  `NAME` text COLLATE utf8_unicode_ci,
  PRIMARY KEY (`ID_NAMESPACE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_NOTE
-- ----------------------------
DROP TABLE IF EXISTS `R_NOTE`;
CREATE TABLE `R_NOTE` (
  `ID_NOTE` bigint(20) NOT NULL,
  `VALUE_STR` mediumtext COLLATE utf8_unicode_ci,
  `GUI_LOCATION_X` int(11) DEFAULT NULL,
  `GUI_LOCATION_Y` int(11) DEFAULT NULL,
  `GUI_LOCATION_WIDTH` int(11) DEFAULT NULL,
  `GUI_LOCATION_HEIGHT` int(11) DEFAULT NULL,
  `FONT_NAME` mediumtext COLLATE utf8_unicode_ci,
  `FONT_SIZE` int(11) DEFAULT NULL,
  `FONT_BOLD` tinyint(1) DEFAULT NULL,
  `FONT_ITALIC` tinyint(1) DEFAULT NULL,
  `FONT_COLOR_RED` int(11) DEFAULT NULL,
  `FONT_COLOR_GREEN` int(11) DEFAULT NULL,
  `FONT_COLOR_BLUE` int(11) DEFAULT NULL,
  `FONT_BACK_GROUND_COLOR_RED` int(11) DEFAULT NULL,
  `FONT_BACK_GROUND_COLOR_GREEN` int(11) DEFAULT NULL,
  `FONT_BACK_GROUND_COLOR_BLUE` int(11) DEFAULT NULL,
  `FONT_BORDER_COLOR_RED` int(11) DEFAULT NULL,
  `FONT_BORDER_COLOR_GREEN` int(11) DEFAULT NULL,
  `FONT_BORDER_COLOR_BLUE` int(11) DEFAULT NULL,
  `DRAW_SHADOW` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID_NOTE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_PARTITION
-- ----------------------------
DROP TABLE IF EXISTS `R_PARTITION`;
CREATE TABLE `R_PARTITION` (
  `ID_PARTITION` bigint(20) NOT NULL,
  `ID_PARTITION_SCHEMA` int(11) DEFAULT NULL,
  `PARTITION_ID` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID_PARTITION`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_PARTITION_SCHEMA
-- ----------------------------
DROP TABLE IF EXISTS `R_PARTITION_SCHEMA`;
CREATE TABLE `R_PARTITION_SCHEMA` (
  `ID_PARTITION_SCHEMA` bigint(20) NOT NULL,
  `NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DYNAMIC_DEFINITION` tinyint(1) DEFAULT NULL,
  `PARTITIONS_PER_SLAVE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID_PARTITION_SCHEMA`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_REPOSITORY_LOG
-- ----------------------------
DROP TABLE IF EXISTS `R_REPOSITORY_LOG`;
CREATE TABLE `R_REPOSITORY_LOG` (
  `ID_REPOSITORY_LOG` bigint(20) NOT NULL,
  `REP_VERSION` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `LOG_DATE` datetime DEFAULT NULL,
  `LOG_USER` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `OPERATION_DESC` mediumtext COLLATE utf8_unicode_ci,
  PRIMARY KEY (`ID_REPOSITORY_LOG`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_SLAVE
-- ----------------------------
DROP TABLE IF EXISTS `R_SLAVE`;
CREATE TABLE `R_SLAVE` (
  `ID_SLAVE` bigint(20) NOT NULL,
  `NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `HOST_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PORT` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `WEB_APP_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `USERNAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PASSWORD` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PROXY_HOST_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PROXY_PORT` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `NON_PROXY_HOSTS` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `MASTER` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID_SLAVE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_STEP
-- ----------------------------
DROP TABLE IF EXISTS `R_STEP`;
CREATE TABLE `R_STEP` (
  `ID_STEP` bigint(20) NOT NULL,
  `ID_TRANSFORMATION` int(11) DEFAULT NULL,
  `NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DESCRIPTION` mediumtext COLLATE utf8_unicode_ci,
  `ID_STEP_TYPE` int(11) DEFAULT NULL,
  `DISTRIBUTE` char(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `COPIES` int(11) DEFAULT NULL,
  `GUI_LOCATION_X` int(11) DEFAULT NULL,
  `GUI_LOCATION_Y` int(11) DEFAULT NULL,
  `GUI_DRAW` char(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `COPIES_STRING` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID_STEP`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_STEP_ATTRIBUTE
-- ----------------------------
DROP TABLE IF EXISTS `R_STEP_ATTRIBUTE`;
CREATE TABLE `R_STEP_ATTRIBUTE` (
  `ID_STEP_ATTRIBUTE` bigint(20) NOT NULL,
  `ID_TRANSFORMATION` int(11) DEFAULT NULL,
  `ID_STEP` int(11) DEFAULT NULL,
  `NR` int(11) DEFAULT NULL,
  `CODE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `VALUE_NUM` bigint(20) DEFAULT NULL,
  `VALUE_STR` mediumtext COLLATE utf8_unicode_ci,
  PRIMARY KEY (`ID_STEP_ATTRIBUTE`) USING BTREE,
  UNIQUE KEY `IDX_RSAT` (`ID_STEP`,`CODE`,`NR`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_STEP_DATABASE
-- ----------------------------
DROP TABLE IF EXISTS `R_STEP_DATABASE`;
CREATE TABLE `R_STEP_DATABASE` (
  `ID_TRANSFORMATION` int(11) DEFAULT NULL,
  `ID_STEP` int(11) DEFAULT NULL,
  `ID_DATABASE` int(11) DEFAULT NULL,
  KEY `IDX_RSD1` (`ID_TRANSFORMATION`) USING BTREE,
  KEY `IDX_RSD2` (`ID_DATABASE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_STEP_TYPE
-- ----------------------------
DROP TABLE IF EXISTS `R_STEP_TYPE`;
CREATE TABLE `R_STEP_TYPE` (
  `ID_STEP_TYPE` bigint(20) NOT NULL,
  `CODE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DESCRIPTION` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `HELPTEXT` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID_STEP_TYPE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of R_STEP_TYPE
-- ----------------------------
BEGIN;
INSERT INTO `R_STEP_TYPE` VALUES (1, 'CsvInput', 'CSV文件输入', 'Simple CSV file input');
INSERT INTO `R_STEP_TYPE` VALUES (2, 'CubeInput', 'Cube 文件输入', '从一个cube读取记录.');
INSERT INTO `R_STEP_TYPE` VALUES (3, 'CubeOutput', 'Cube输出', '把数据写入一个cube');
INSERT INTO `R_STEP_TYPE` VALUES (4, 'TypeExitEdi2XmlStep', 'EDI to XML', 'Converts Edi text to generic XML');
INSERT INTO `R_STEP_TYPE` VALUES (5, 'ExcelInput', 'Excel输入', '从一个微软的Excel文件里读取数据. 兼容Excel 95, 97 and 2000.');
INSERT INTO `R_STEP_TYPE` VALUES (6, 'ExcelOutput', 'Excel输出', 'Stores records into an Excel (XLS) document with formatting information.');
INSERT INTO `R_STEP_TYPE` VALUES (7, 'GetSlaveSequence', 'Get ID from slave server', 'Retrieves unique IDs in blocks from a slave server.  The referenced sequence needs to be configured on the slave server in the XML configuration file.');
INSERT INTO `R_STEP_TYPE` VALUES (8, 'RecordsFromStream', 'Get records from stream', 'This step allows you to read records from a streaming step.');
INSERT INTO `R_STEP_TYPE` VALUES (9, 'ParallelGzipCsvInput', 'GZIP CSV input', 'Parallel GZIP CSV file input reader');
INSERT INTO `R_STEP_TYPE` VALUES (10, 'HTTP', 'HTTP client', 'Call a web service over HTTP by supplying a base URL by allowing parameters to be set dynamically');
INSERT INTO `R_STEP_TYPE` VALUES (11, 'HTTPPOST', 'HTTP post', 'Call a web service request over HTTP by supplying a base URL by allowing parameters to be set dynamically');
INSERT INTO `R_STEP_TYPE` VALUES (12, 'InfobrightOutput', 'Infobright 批量加载', 'Load data to an Infobright database table');
INSERT INTO `R_STEP_TYPE` VALUES (13, 'VectorWiseBulkLoader', 'Ingres VectorWise 批量加载', 'This step interfaces with the Ingres VectorWise Bulk Loader \"COPY TABLE\" command.');
INSERT INTO `R_STEP_TYPE` VALUES (14, 'UserDefinedJavaClass', 'Java 代码', 'This step allows you to program a step using Java code');
INSERT INTO `R_STEP_TYPE` VALUES (15, 'ScriptValueMod', 'JavaScript代码', 'This is a modified plugin for the Scripting Values with improved interface and performance.\nWritten & donated to open source by Martin Lange, Proconis : http://www.proconis.de');
INSERT INTO `R_STEP_TYPE` VALUES (16, 'LDAPInput', 'LDAP 输入', 'Read data from LDAP host');
INSERT INTO `R_STEP_TYPE` VALUES (17, 'LDAPOutput', 'LDAP 输出', 'Perform Insert, upsert, update, add or delete operations on records based on their DN (Distinguished  Name).');
INSERT INTO `R_STEP_TYPE` VALUES (18, 'LDIFInput', 'LDIF 输入', 'Read data from LDIF files');
INSERT INTO `R_STEP_TYPE` VALUES (19, 'TypeExitExcelWriterStep', 'Microsoft Excel 输出', 'Writes or appends data to an Excel file');
INSERT INTO `R_STEP_TYPE` VALUES (20, 'MondrianInput', 'Mondrian 输入', 'Execute and retrieve data using an MDX query against a Pentaho Analyses OLAP server (Mondrian)');
INSERT INTO `R_STEP_TYPE` VALUES (21, 'MonetDBAgileMart', 'MonetDB Agile Mart', 'Load data into MonetDB for Agile BI use cases');
INSERT INTO `R_STEP_TYPE` VALUES (22, 'MonetDBBulkLoader', 'MonetDB 批量加载', 'Load data into MonetDB by using their bulk load command in streaming mode.');
INSERT INTO `R_STEP_TYPE` VALUES (23, 'MultiwayMergeJoin', 'Multiway merge join', 'Multiway merge join');
INSERT INTO `R_STEP_TYPE` VALUES (24, 'MySQLBulkLoader', 'MySQL 批量加载', 'MySQL bulk loader step, loading data over a named pipe (not available on MS Windows)');
INSERT INTO `R_STEP_TYPE` VALUES (25, 'OlapInput', 'OLAP 输入', 'Execute and retrieve data using an MDX query against any XML/A OLAP datasource using olap4j');
INSERT INTO `R_STEP_TYPE` VALUES (26, 'OraBulkLoader', 'Oracle 批量加载', 'Use Oracle bulk loader to load data');
INSERT INTO `R_STEP_TYPE` VALUES (27, 'PentahoReportingOutput', 'Pentaho 报表输出', 'Executes an existing report (PRPT)');
INSERT INTO `R_STEP_TYPE` VALUES (28, 'PGPDecryptStream', 'PGP decrypt stream', 'Decrypt data stream with PGP');
INSERT INTO `R_STEP_TYPE` VALUES (29, 'PGPEncryptStream', 'PGP encrypt stream', 'Encrypt data stream with PGP');
INSERT INTO `R_STEP_TYPE` VALUES (30, 'PGBulkLoader', 'PostgreSQL 批量加载', 'PostgreSQL Bulk Loader');
INSERT INTO `R_STEP_TYPE` VALUES (31, 'Rest', 'REST client', 'Consume RESTfull services.\nREpresentational State Transfer (REST) is a key design idiom that embraces a stateless client-server\narchitecture in which the web services are viewed as resources and can be identified by their URLs');
INSERT INTO `R_STEP_TYPE` VALUES (32, 'RssInput', 'RSS 输入', 'Read RSS feeds');
INSERT INTO `R_STEP_TYPE` VALUES (33, 'RssOutput', 'RSS 输出', 'Read RSS stream.');
INSERT INTO `R_STEP_TYPE` VALUES (34, 'SASInput', 'SAS 输入', 'This step reads files in sas7bdat (SAS) native format');
INSERT INTO `R_STEP_TYPE` VALUES (35, 'Script', 'Script', 'Calculate values by scripting in Ruby, Python, Groovy, JavaScript, ... (JSR-223)');
INSERT INTO `R_STEP_TYPE` VALUES (36, 'SFTPPut', 'SFTP put', 'Upload a file or a stream file to remote host via SFTP');
INSERT INTO `R_STEP_TYPE` VALUES (37, 'SimpleMapping', 'Simple mapping (sub-transformation)', 'Run a mapping (sub-transformation), use MappingInput and MappingOutput to specify the fields interface.  This is the simplified version only allowing one input and one output data set.');
INSERT INTO `R_STEP_TYPE` VALUES (38, 'SingleThreader', 'Single threader', 'Executes a transformation snippet in a single thread.  You need a standard mapping or a transformation with an Injector step where data from the parent transformation will arive in blocks.');
INSERT INTO `R_STEP_TYPE` VALUES (39, 'SocketWriter', 'Socket 写', 'Socket writer.  A socket server that can send rows of data to a socket reader.');
INSERT INTO `R_STEP_TYPE` VALUES (40, 'SocketReader', 'Socket 读', 'Socket reader.  A socket client that connects to a server (Socket Writer step).');
INSERT INTO `R_STEP_TYPE` VALUES (41, 'SQLFileOutput', 'SQL 文件输出', 'Output SQL INSERT statements to file');
INSERT INTO `R_STEP_TYPE` VALUES (42, 'SwitchCase', 'Switch / case', 'Switch a row to a certain target step based on the case value in a field.');
INSERT INTO `R_STEP_TYPE` VALUES (43, 'TableAgileMart', 'Table Agile Mart', 'Load data into a table for Agile BI use cases');
INSERT INTO `R_STEP_TYPE` VALUES (44, 'TeraFast', 'Teradata Fastload 批量加载', 'The Teradata Fastload bulk loader');
INSERT INTO `R_STEP_TYPE` VALUES (45, 'OldTextFileInput', 'Text file input', '从一个文本文件（几种格式）里读取数据{0}这些数据可以被传递到下一个步骤里...');
INSERT INTO `R_STEP_TYPE` VALUES (46, 'TextFileOutputLegacy', 'Text file output', '写记录到一个文本文件.');
INSERT INTO `R_STEP_TYPE` VALUES (47, 'TransExecutor', 'Transformation executor', 'This step executes a Pentaho Data Integration transformation, sets parameters and passes rows.');
INSERT INTO `R_STEP_TYPE` VALUES (48, 'WebServiceLookup', 'Web 服务查询', '使用 Web 服务查询信息');
INSERT INTO `R_STEP_TYPE` VALUES (49, 'XBaseInput', 'XBase输入', '从一个XBase类型的文件(DBF)读取记录');
INSERT INTO `R_STEP_TYPE` VALUES (50, 'YamlInput', 'YAML 输入', 'Read YAML source (file or stream) parse them and convert them to rows and writes these to one or more output.');
INSERT INTO `R_STEP_TYPE` VALUES (51, 'ZipFile', 'Zip 文件', 'Zip a file.\nFilename will be extracted from incoming stream.');
INSERT INTO `R_STEP_TYPE` VALUES (52, 'FilesFromResult', '从结果获取文件', 'This step allows you to read filenames used or generated in a previous entry in a job.');
INSERT INTO `R_STEP_TYPE` VALUES (53, 'RowsFromResult', '从结果获取记录', '这个允许你从同一个任务的前一个条目里读取记录.');
INSERT INTO `R_STEP_TYPE` VALUES (54, 'ValueMapper', '值映射', 'Maps values of a certain field from one value to another');
INSERT INTO `R_STEP_TYPE` VALUES (55, 'Formula', '公式', '使用 Pentaho 的公式库来计算公式');
INSERT INTO `R_STEP_TYPE` VALUES (56, 'WriteToLog', '写日志', 'Write data to log');
INSERT INTO `R_STEP_TYPE` VALUES (57, 'GroupBy', '分组', '以分组的形式创建聚合.{0}这个仅仅在一个已经排好序的输入有效.{1}如果输入没有排序, 仅仅两个连续的记录行被正确处理.');
INSERT INTO `R_STEP_TYPE` VALUES (58, 'SplitFieldToRows3', '列拆分为多行', 'Splits a single string field by delimiter and creates a new row for each split term');
INSERT INTO `R_STEP_TYPE` VALUES (59, 'Denormaliser', '列转行', 'Denormalises rows by looking up key-value pairs and by assigning them to new fields in the输出 rows.{0}This method aggregates and needs the输入 rows to be sorted on the grouping fields');
INSERT INTO `R_STEP_TYPE` VALUES (60, 'Delete', '删除', '基于关键字删除记录');
INSERT INTO `R_STEP_TYPE` VALUES (61, 'Janino', '利用Janino计算Java表达式', 'Calculate the result of a Java Expression using Janino');
INSERT INTO `R_STEP_TYPE` VALUES (62, 'StringCut', '剪切字符串', 'Strings cut (substring).');
INSERT INTO `R_STEP_TYPE` VALUES (63, 'UnivariateStats', '单变量统计', 'This step computes some simple stats based on a single input field');
INSERT INTO `R_STEP_TYPE` VALUES (64, 'Unique', '去除重复记录', '去除重复的记录行，保持记录唯一{0}这个仅仅基于一个已经排好序的输入.{1}如果输入没有排序, 仅仅两个连续的记录行被正确处理.');
INSERT INTO `R_STEP_TYPE` VALUES (65, 'SyslogMessage', '发送信息至syslog', 'Send message to syslog server');
INSERT INTO `R_STEP_TYPE` VALUES (66, 'Mail', '发送邮件', 'Send eMail.');
INSERT INTO `R_STEP_TYPE` VALUES (67, 'MergeRows', '合并记录', '合并两个数据流, 并根据某个关键字排序.  这两个数据流被比较，以标识相等的、变更的、删除的和新建的记录.');
INSERT INTO `R_STEP_TYPE` VALUES (68, 'ExecProcess', '启动一个进程', 'Execute a process and return the result');
INSERT INTO `R_STEP_TYPE` VALUES (69, 'UniqueRowsByHashSet', '唯一行 (哈希值)', 'Remove double rows and leave only unique occurrences by using a HashSet.');
INSERT INTO `R_STEP_TYPE` VALUES (70, 'FixedInput', '固定宽度文件输入', 'Fixed file input');
INSERT INTO `R_STEP_TYPE` VALUES (71, 'MemoryGroupBy', '在内存中分组', 'Builds aggregates in a group by fashion.\nThis step doesn\'t require sorted input.');
INSERT INTO `R_STEP_TYPE` VALUES (72, 'Constant', '增加常量', '给记录增加一到多个常量');
INSERT INTO `R_STEP_TYPE` VALUES (73, 'Sequence', '增加序列', '从序列获取下一个值');
INSERT INTO `R_STEP_TYPE` VALUES (74, 'ProcessFiles', '处理文件', 'Process one file per row (copy or move or delete).\nThis step only accept filename in input.');
INSERT INTO `R_STEP_TYPE` VALUES (75, 'FilesToResult', '复制文件到结果', 'This step allows you to set filenames in the result of this transformation.\nSubsequent job entries can then use this information.');
INSERT INTO `R_STEP_TYPE` VALUES (76, 'RowsToResult', '复制记录到结果', '使用这个步骤把记录写到正在执行的任务.{0}信息将会被传递给同一个任务里的下一个条目.');
INSERT INTO `R_STEP_TYPE` VALUES (77, 'SelectValues', '字段选择', '选择或移除记录里的字。{0}此外，可以设置字段的元数据: 类型, 长度和精度.');
INSERT INTO `R_STEP_TYPE` VALUES (78, 'StringOperations', '字符串操作', 'Apply certain operations like trimming, padding and others to string value.');
INSERT INTO `R_STEP_TYPE` VALUES (79, 'ReplaceString', '字符串替换', 'Replace all occurences a word in a string with another word.');
INSERT INTO `R_STEP_TYPE` VALUES (80, 'SymmetricCryptoTrans', '对称加密', 'Encrypt or decrypt a string using symmetric encryption.\nAvailable algorithms are DES, AES, TripleDES.');
INSERT INTO `R_STEP_TYPE` VALUES (81, 'SetValueConstant', '将字段值设置为常量', 'Set value of a field to a constant');
INSERT INTO `R_STEP_TYPE` VALUES (82, 'Delay', '延迟行', 'Output each input row after a delay');
INSERT INTO `R_STEP_TYPE` VALUES (83, 'DynamicSQLRow', '执行Dynamic SQL', 'Execute dynamic SQL statement build in a previous field');
INSERT INTO `R_STEP_TYPE` VALUES (84, 'ExecSQL', '执行SQL脚本', '执行一个SQL脚本, 另外，可以使用输入的记录作为参数');
INSERT INTO `R_STEP_TYPE` VALUES (85, 'ExecSQLRow', '执行SQL脚本(字段流替换)', 'Execute SQL script extracted from a field\ncreated in a previous step.');
INSERT INTO `R_STEP_TYPE` VALUES (86, 'JobExecutor', '执行作业', 'This step executes a Pentaho Data Integration job, sets parameters and passes rows.');
INSERT INTO `R_STEP_TYPE` VALUES (87, 'FieldSplitter', '拆分字段', '当你想把一个字段拆分成多个时，使用这个类型.');
INSERT INTO `R_STEP_TYPE` VALUES (88, 'SortedMerge', '排序合并', 'Sorted merge');
INSERT INTO `R_STEP_TYPE` VALUES (89, 'SortRows', '排序记录', '基于字段值把记录排序(升序或降序)');
INSERT INTO `R_STEP_TYPE` VALUES (90, 'InsertUpdate', '插入 / 更新', '基于关键字更新或插入记录到数据库.');
INSERT INTO `R_STEP_TYPE` VALUES (91, 'NumberRange', '数值范围', 'Create ranges based on numeric field');
INSERT INTO `R_STEP_TYPE` VALUES (92, 'SynchronizeAfterMerge', '数据同步', 'This step perform insert/update/delete in one go based on the value of a field.');
INSERT INTO `R_STEP_TYPE` VALUES (93, 'DBLookup', '数据库查询', '使用字段值在数据库里查询值');
INSERT INTO `R_STEP_TYPE` VALUES (94, 'DBJoin', '数据库连接', '使用数据流里的值作为参数执行一个数据库查询');
INSERT INTO `R_STEP_TYPE` VALUES (95, 'Validator', '数据检验', 'Validates passing data based on a set of rules');
INSERT INTO `R_STEP_TYPE` VALUES (96, 'PrioritizeStreams', '数据流优先级排序', 'Prioritize streams in an order way.');
INSERT INTO `R_STEP_TYPE` VALUES (97, 'ReservoirSampling', '数据采样', '[Transform] Samples a fixed number of rows from the incoming stream');
INSERT INTO `R_STEP_TYPE` VALUES (98, 'LoadFileInput', '文件内容加载至内存', 'Load file content in memory');
INSERT INTO `R_STEP_TYPE` VALUES (99, 'TextFileInput', '文本文件输入', '从一个文本文件（几种格式）里读取数据{0}这些数据可以被传递到下一个步骤里...');
INSERT INTO `R_STEP_TYPE` VALUES (100, 'TextFileOutput', '文本文件输出', '写记录到一个文本文件.');
INSERT INTO `R_STEP_TYPE` VALUES (101, 'Mapping', '映射 (子转换)', '运行一个映射 (子转换), 使用MappingInput和MappingOutput来指定接口的字段');
INSERT INTO `R_STEP_TYPE` VALUES (102, 'MappingInput', '映射输入规范', '指定一个映射的字段输入');
INSERT INTO `R_STEP_TYPE` VALUES (103, 'MappingOutput', '映射输出规范', '指定一个映射的字段输出');
INSERT INTO `R_STEP_TYPE` VALUES (104, 'Update', '更新', '基于关键字更新记录到数据库');
INSERT INTO `R_STEP_TYPE` VALUES (105, 'IfNull', '替换NULL值', 'Sets a field value to a constant if it is null.');
INSERT INTO `R_STEP_TYPE` VALUES (106, 'SampleRows', '样本行', 'Filter rows based on the line number.');
INSERT INTO `R_STEP_TYPE` VALUES (107, 'JavaFilter', '根据Java代码过滤记录', 'Filter rows using java code');
INSERT INTO `R_STEP_TYPE` VALUES (108, 'FieldsChangeSequence', '根据字段值来改变序列', 'Add sequence depending of fields value change.\nEach time value of at least one field change, PDI will reset sequence.');
INSERT INTO `R_STEP_TYPE` VALUES (109, 'WebServiceAvailable', '检查web服务是否可用', 'Check if a webservice is available');
INSERT INTO `R_STEP_TYPE` VALUES (110, 'FileExists', '检查文件是否存在', 'Check if a file exists');
INSERT INTO `R_STEP_TYPE` VALUES (111, 'FileLocked', '检查文件是否已被锁定', 'Check if a file is locked by another process');
INSERT INTO `R_STEP_TYPE` VALUES (112, 'TableExists', '检查表是否存在', 'Check if a table exists on a specified connection');
INSERT INTO `R_STEP_TYPE` VALUES (113, 'DetectEmptyStream', '检测空流', 'This step will output one empty row if input stream is empty\n(ie when input stream does not contain any row)');
INSERT INTO `R_STEP_TYPE` VALUES (114, 'CreditCardValidator', '检验信用卡号码是否有效', 'The Credit card validator step will help you tell:\n(1) if a credit card number is valid (uses LUHN10 (MOD-10) algorithm)\n(2) which credit card vendor handles that number\n(VISA, MasterCard, Diners Club, EnRoute, American Express (AMEX),...)');
INSERT INTO `R_STEP_TYPE` VALUES (115, 'MailValidator', '检验邮件地址', 'Check if an email address is valid.');
INSERT INTO `R_STEP_TYPE` VALUES (116, 'FuzzyMatch', '模糊匹配', 'Finding approximate matches to a string using matching algorithms.\nRead a field from a main stream and output approximative value from lookup stream.');
INSERT INTO `R_STEP_TYPE` VALUES (117, 'RegexEval', '正则表达式', 'Regular expression Evaluation\nThis step uses a regular expression to evaluate a field. It can also extract new fields out of an existing field with capturing groups.');
INSERT INTO `R_STEP_TYPE` VALUES (118, 'TableCompare', '比较表', 'Compares 2 tables and gives back a list of differences');
INSERT INTO `R_STEP_TYPE` VALUES (119, 'StreamLookup', '流查询', '从转换中的其它流里查询值.');
INSERT INTO `R_STEP_TYPE` VALUES (120, 'StepMetastructure', '流的元数据', 'This is a step to read the metadata of the incoming stream.');
INSERT INTO `R_STEP_TYPE` VALUES (121, 'SecretKeyGenerator', '生成密钥', 'Generate secret key for algorithms such as DES, AES, TripleDES.');
INSERT INTO `R_STEP_TYPE` VALUES (122, 'RowGenerator', '生成记录', '产生一些空记录或相等的行.');
INSERT INTO `R_STEP_TYPE` VALUES (123, 'RandomValue', '生成随机数', 'Generate random value');
INSERT INTO `R_STEP_TYPE` VALUES (124, 'RandomCCNumberGenerator', '生成随机的信用卡号', 'Generate random valide (luhn check) credit card numbers');
INSERT INTO `R_STEP_TYPE` VALUES (125, 'RandomRowGenerator', '生成随机记录', '按照指定规则生成随机数据.');
INSERT INTO `R_STEP_TYPE` VALUES (126, 'Dummy', '空操作 (什么也不做)', '这个步骤类型什么都不作.{0} 当你想测试或拆分数据流的时候有用.');
INSERT INTO `R_STEP_TYPE` VALUES (127, 'DimensionLookup', '维度查询/更新', '在一个数据仓库里更新一个渐变维 {0} 或者在这个维里查询信息.');
INSERT INTO `R_STEP_TYPE` VALUES (128, 'DataGrid', '自定义常量数据', 'Enter rows of static data in a grid, usually for testing, reference or demo purpose');
INSERT INTO `R_STEP_TYPE` VALUES (129, 'GetVariable', '获取变量', 'Determine the values of certain (environment or Kettle) variables and put them in field values.');
INSERT INTO `R_STEP_TYPE` VALUES (130, 'GetSubFolders', '获取子目录名', 'Read a parent folder and return all subfolders');
INSERT INTO `R_STEP_TYPE` VALUES (131, 'GetFileNames', '获取文件名', 'Get file names from the operating system and send them to the next step.');
INSERT INTO `R_STEP_TYPE` VALUES (132, 'GetFilesRowsCount', '获取文件行数', 'Returns rows count for text files.');
INSERT INTO `R_STEP_TYPE` VALUES (133, 'SystemInfo', '获取系统信息', '获取系统信息，例如时间、日期.');
INSERT INTO `R_STEP_TYPE` VALUES (134, 'GetTableNames', '获取表名', 'Get table names from database connection and send them to the next step');
INSERT INTO `R_STEP_TYPE` VALUES (135, 'GetRepositoryNames', '获取资源库配置', 'Lists detailed information about transformations and/or jobs in a repository');
INSERT INTO `R_STEP_TYPE` VALUES (136, 'Flattener', '行扁平化', 'Flattens consequetive rows based on the order in which they appear in the输入 stream');
INSERT INTO `R_STEP_TYPE` VALUES (137, 'Normaliser', '行转列', 'De-normalised information can be normalised using this step type.');
INSERT INTO `R_STEP_TYPE` VALUES (138, 'TableInput', '表输入', '从数据库表里读取信息.');
INSERT INTO `R_STEP_TYPE` VALUES (139, 'TableOutput', '表输出', '写信息到一个数据库表');
INSERT INTO `R_STEP_TYPE` VALUES (140, 'Calculator', '计算器', '通过执行简单的计算创建一个新字段');
INSERT INTO `R_STEP_TYPE` VALUES (141, 'JoinRows', '记录关联 (笛卡尔输出)', '这个步骤的输出是输入流的笛卡尔的结果.{0} 输出结果的记录数是输入流记录之间的乘积.');
INSERT INTO `R_STEP_TYPE` VALUES (142, 'Injector', '记录注射', 'Injector step to allow to inject rows into the transformation through the java API');
INSERT INTO `R_STEP_TYPE` VALUES (143, 'MergeJoin', '记录集连接', 'Joins two streams on a given key and outputs a joined set. The input streams must be sorted on the join key');
INSERT INTO `R_STEP_TYPE` VALUES (144, 'NullIf', '设置值为NULL', '如果一个字段值等于某个固定值，那么把这个字段值设置成null');
INSERT INTO `R_STEP_TYPE` VALUES (145, 'SetVariable', '设置变量', 'Set environment variables based on a single input row.');
INSERT INTO `R_STEP_TYPE` VALUES (146, 'SetValueField', '设置字段值', 'Set value of a field with another value field');
INSERT INTO `R_STEP_TYPE` VALUES (147, 'DetectLastRow', '识别流的最后一行', 'Last row will be marked');
INSERT INTO `R_STEP_TYPE` VALUES (148, 'DBProc', '调用DB存储过程', '通过调用数据库存储过程获得返回值.');
INSERT INTO `R_STEP_TYPE` VALUES (149, 'StepsMetrics', '转换步骤信息统计', 'Return metrics for one or several steps');
INSERT INTO `R_STEP_TYPE` VALUES (150, 'FilterRows', '过滤记录', '使用简单的相等来过滤记录');
INSERT INTO `R_STEP_TYPE` VALUES (151, 'SSH', '运行SSH命令', 'Run SSH commands and returns result.');
INSERT INTO `R_STEP_TYPE` VALUES (152, 'MailInput', '邮件信息输入', 'Read POP3/IMAP server and retrieve messages');
INSERT INTO `R_STEP_TYPE` VALUES (153, 'PropertyInput', '配置文件输入', 'Read data (key, value) from properties files.');
INSERT INTO `R_STEP_TYPE` VALUES (154, 'PropertyOutput', '配置文件输出', 'Write data to properties file');
INSERT INTO `R_STEP_TYPE` VALUES (155, 'BlockUntilStepsFinish', '阻塞数据直到步骤都完成', 'Block this step until selected steps finish.');
INSERT INTO `R_STEP_TYPE` VALUES (156, 'OpenAPIClient', 'Open API客户端', '运行远程服务器上的API方法');
INSERT INTO `R_STEP_TYPE` VALUES (157, 'JsonInput', 'JSON input', 'Extract relevant portions out of JSON structures (file or incoming field) and output rows');
INSERT INTO `R_STEP_TYPE` VALUES (158, 'JsonOutput', 'JSON output', 'Create JSON block and output it in a field or a file.');
COMMIT;

-- ----------------------------
-- Table structure for R_TRANSFORMATION
-- ----------------------------
DROP TABLE IF EXISTS `R_TRANSFORMATION`;
CREATE TABLE `R_TRANSFORMATION` (
  `ID_TRANSFORMATION` bigint(20) NOT NULL,
  `ID_DIRECTORY` int(11) DEFAULT NULL,
  `NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DESCRIPTION` mediumtext COLLATE utf8_unicode_ci,
  `EXTENDED_DESCRIPTION` mediumtext COLLATE utf8_unicode_ci,
  `TRANS_VERSION` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `TRANS_STATUS` int(11) DEFAULT NULL,
  `ID_STEP_READ` int(11) DEFAULT NULL,
  `ID_STEP_WRITE` int(11) DEFAULT NULL,
  `ID_STEP_INPUT` int(11) DEFAULT NULL,
  `ID_STEP_OUTPUT` int(11) DEFAULT NULL,
  `ID_STEP_UPDATE` int(11) DEFAULT NULL,
  `ID_DATABASE_LOG` int(11) DEFAULT NULL,
  `TABLE_NAME_LOG` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `USE_BATCHID` tinyint(1) DEFAULT NULL,
  `USE_LOGFIELD` tinyint(1) DEFAULT NULL,
  `ID_DATABASE_MAXDATE` int(11) DEFAULT NULL,
  `TABLE_NAME_MAXDATE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `FIELD_NAME_MAXDATE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `OFFSET_MAXDATE` double DEFAULT NULL,
  `DIFF_MAXDATE` double DEFAULT NULL,
  `CREATED_USER` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `MODIFIED_USER` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `MODIFIED_DATE` datetime DEFAULT NULL,
  `SIZE_ROWSET` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID_TRANSFORMATION`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_TRANS_ATTRIBUTE
-- ----------------------------
DROP TABLE IF EXISTS `R_TRANS_ATTRIBUTE`;
CREATE TABLE `R_TRANS_ATTRIBUTE` (
  `ID_TRANS_ATTRIBUTE` bigint(20) NOT NULL,
  `ID_TRANSFORMATION` int(11) DEFAULT NULL,
  `NR` int(11) DEFAULT NULL,
  `CODE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `VALUE_NUM` bigint(20) DEFAULT NULL,
  `VALUE_STR` mediumtext COLLATE utf8_unicode_ci,
  PRIMARY KEY (`ID_TRANS_ATTRIBUTE`) USING BTREE,
  UNIQUE KEY `IDX_TATT` (`ID_TRANSFORMATION`,`CODE`,`NR`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_TRANS_CLUSTER
-- ----------------------------
DROP TABLE IF EXISTS `R_TRANS_CLUSTER`;
CREATE TABLE `R_TRANS_CLUSTER` (
  `ID_TRANS_CLUSTER` bigint(20) NOT NULL,
  `ID_TRANSFORMATION` int(11) DEFAULT NULL,
  `ID_CLUSTER` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID_TRANS_CLUSTER`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_TRANS_HOP
-- ----------------------------
DROP TABLE IF EXISTS `R_TRANS_HOP`;
CREATE TABLE `R_TRANS_HOP` (
  `ID_TRANS_HOP` bigint(20) NOT NULL,
  `ID_TRANSFORMATION` int(11) DEFAULT NULL,
  `ID_STEP_FROM` int(11) DEFAULT NULL,
  `ID_STEP_TO` int(11) DEFAULT NULL,
  `ENABLED` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID_TRANS_HOP`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_TRANS_LOCK
-- ----------------------------
DROP TABLE IF EXISTS `R_TRANS_LOCK`;
CREATE TABLE `R_TRANS_LOCK` (
  `ID_TRANS_LOCK` bigint(20) NOT NULL,
  `ID_TRANSFORMATION` int(11) DEFAULT NULL,
  `ID_USER` int(11) DEFAULT NULL,
  `LOCK_MESSAGE` mediumtext COLLATE utf8_unicode_ci,
  `LOCK_DATE` datetime DEFAULT NULL,
  PRIMARY KEY (`ID_TRANS_LOCK`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_TRANS_NOTE
-- ----------------------------
DROP TABLE IF EXISTS `R_TRANS_NOTE`;
CREATE TABLE `R_TRANS_NOTE` (
  `ID_TRANSFORMATION` int(11) DEFAULT NULL,
  `ID_NOTE` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_TRANS_PARTITION_SCHEMA
-- ----------------------------
DROP TABLE IF EXISTS `R_TRANS_PARTITION_SCHEMA`;
CREATE TABLE `R_TRANS_PARTITION_SCHEMA` (
  `ID_TRANS_PARTITION_SCHEMA` bigint(20) NOT NULL,
  `ID_TRANSFORMATION` int(11) DEFAULT NULL,
  `ID_PARTITION_SCHEMA` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID_TRANS_PARTITION_SCHEMA`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_TRANS_SLAVE
-- ----------------------------
DROP TABLE IF EXISTS `R_TRANS_SLAVE`;
CREATE TABLE `R_TRANS_SLAVE` (
  `ID_TRANS_SLAVE` bigint(20) NOT NULL,
  `ID_TRANSFORMATION` int(11) DEFAULT NULL,
  `ID_SLAVE` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID_TRANS_SLAVE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_TRANS_STEP_CONDITION
-- ----------------------------
DROP TABLE IF EXISTS `R_TRANS_STEP_CONDITION`;
CREATE TABLE `R_TRANS_STEP_CONDITION` (
  `ID_TRANSFORMATION` int(11) DEFAULT NULL,
  `ID_STEP` int(11) DEFAULT NULL,
  `ID_CONDITION` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for R_USER
-- ----------------------------
DROP TABLE IF EXISTS `R_USER`;
CREATE TABLE `R_USER` (
  `ID_USER` bigint(20) NOT NULL,
  `LOGIN` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PASSWORD` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DESCRIPTION` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ENABLED` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID_USER`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of R_USER
-- ----------------------------
BEGIN;
INSERT INTO `R_USER` VALUES (1, 'admin', '2be98afc86aa7f2e4cb79ce71da9fa6d4', 'Administrator', 'User manager', 1);
INSERT INTO `R_USER` VALUES (2, 'guest', '2be98afc86aa7f2e4cb79ce77cb97bcce', 'Guest account', 'Read-only guest account', 1);
COMMIT;

-- ----------------------------
-- Table structure for R_VALUE
-- ----------------------------
DROP TABLE IF EXISTS `R_VALUE`;
CREATE TABLE `R_VALUE` (
  `ID_VALUE` bigint(20) NOT NULL,
  `NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `VALUE_TYPE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `VALUE_STR` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `IS_NULL` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID_VALUE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of R_VALUE
-- ----------------------------
BEGIN;
INSERT INTO `R_VALUE` VALUES (1, 'constant', 'Integer', '2', 0);
INSERT INTO `R_VALUE` VALUES (2, 'constant', 'String', 'a', 0);
INSERT INTO `R_VALUE` VALUES (3, 'constant', 'Integer', '1', 0);
INSERT INTO `R_VALUE` VALUES (4, 'constant', 'String', 'CK-TEMP-100000000', 0);
COMMIT;

-- ----------------------------
-- Table structure for R_VERSION
-- ----------------------------
DROP TABLE IF EXISTS `R_VERSION`;
CREATE TABLE `R_VERSION` (
  `ID_VERSION` bigint(20) NOT NULL,
  `MAJOR_VERSION` int(11) DEFAULT NULL,
  `MINOR_VERSION` int(11) DEFAULT NULL,
  `UPGRADE_DATE` datetime DEFAULT NULL,
  `IS_UPGRADE` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID_VERSION`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of R_VERSION
-- ----------------------------
BEGIN;
INSERT INTO `R_VERSION` VALUES (1, 5, 0, '2018-08-21 16:48:35', 0);
INSERT INTO `R_VERSION` VALUES (2, 5, 0, '2018-12-10 17:02:10', 1);
COMMIT;

-- ----------------------------
-- Table structure for SYS_CONFIG
-- ----------------------------
DROP TABLE IF EXISTS `SYS_CONFIG`;
CREATE TABLE `SYS_CONFIG` (
  `CONFIG_ID` bigint(20) NOT NULL,
  `NAME` varchar(100) DEFAULT NULL COMMENT '名称',
  `VALUE` text COMMENT '值',
  `CREATE_USER` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '最后修改用户',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '最后时间更新',
  `DEL_FLAG` tinyint(4) DEFAULT '0' COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`CONFIG_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of SYS_CONFIG
-- ----------------------------
BEGIN;
INSERT INTO `SYS_CONFIG` VALUES (1, 'sys_name_zh', '航天大数据融合平台', NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_CONFIG` VALUES (2, 'sys_name_en', NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_CONFIG` VALUES (3, 'show_login_home_button', 'N', NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_CONFIG` VALUES (4, 'show_login_help_button', 'N', NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_CONFIG` VALUES (5, 'show_login_support_button', 'N', NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_CONFIG` VALUES (6, 'sys_logo', NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_CONFIG` VALUES (7, 'show_login_i18n_button', 'N', NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_CONFIG` VALUES (8, 'show_login_register_button', 'N', NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_CONFIG` VALUES (9, 'sys_login_copyright', '©2020  航天大数据融合平台', NULL, NULL, NULL, NULL, 0);
COMMIT;

-- ----------------------------
-- Table structure for SYS_MENU
-- ----------------------------
DROP TABLE IF EXISTS `SYS_MENU`;
CREATE TABLE `SYS_MENU` (
  `MENU_ID` bigint(64) NOT NULL,
  `PARENT_ID` bigint(64) DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `NAME` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '菜单名称',
  `URL` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '菜单URL',
  `PERMS` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `TYPE` int(11) DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `ICON` varchar(100) COLLATE utf8_unicode_ci DEFAULT 'NULL' COMMENT '菜单图标',
  `ORDER_NUM` int(11) DEFAULT NULL COMMENT '排序',
  `STATUS` int(1) DEFAULT NULL COMMENT '状态  0：正常   1：禁用\n',
  `CREATE_USER` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '最后修改用户',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '最后时间更新',
  `DEL_FLAG` tinyint(4) DEFAULT '0' COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`MENU_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='菜单管理';

-- ----------------------------
-- Records of SYS_MENU
-- ----------------------------
BEGIN;
INSERT INTO `SYS_MENU` VALUES (1, 0, '作业管理', 'designer', 'designer', 1, 'NULL', NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (10000, 0, '任务管理', 'schedule', 'schedule', 0, 'table', 0, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (11000, 10000, '任务查询', 'task-query', 'task-query', 1, 'NULL', 1, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (12000, 10000, '任务启动', 'task-start', 'task-start', 1, 'NULL', 2, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (13000, 10000, '任务分组', 'schedule-group', 'schedule-group', 1, 'NULL', NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (14000, 10000, '任务调度', 'schedule-cycle', 'schedule-cycle', 1, 'NULL', 3, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (15000, 10000, '调度计划', 'schedule-plan', 'schedule-plan', 1, 'NULL', NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (40000, 0, '数据源管理', 'data-source', 'data-source', 0, 'NULL', NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (41000, 40000, '数据库管理', 'data-source-database', 'data-source-database', 1, 'NULL', NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (42000, 40000, '本地文件管理', 'data-source-disk', 'data-source-disk', 1, 'NULL', NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (50000, 0, '质量管理', 'data-quality', 'data-quality', 0, 'NULL', NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (51000, 50000, '质量管理分组', 'data-quality-rule-group', 'data-quality-rule-group', 1, 'NULL', NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (52000, 50000, '质量规则管理', 'data-quality-rule', 'data-quality-rule', 1, 'NULL', NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (53000, 50000, '质量分析', 'data-quality-overview', 'data-quality-overview', 1, 'NULL', NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (54000, 50000, '数据稽核', 'data-quality-compare', 'data-quality-compare', 1, 'NULL', NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (80000, 0, '查询统计', 'query-statistics', 'query-statistics', 0, 'NULL', NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (81000, 80000, '文件接收记录查询', 'query-statistics-file-recv', 'query-statistics-file-recv', 1, 'NULL', NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (82000, 80000, '备份频率统计', 'backup-frequency-statistics', 'backup-frequency-statistics', 1, 'NULL', NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (83000, 80000, '备份记录查询', 'backup-record-query', 'backup-record-query', 1, 'NULL', NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (84000, 80000, '详细制证记录查询', 'certificate-record', 'certificate-record', 1, 'NULL', NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (85000, 80000, '服务错误数据查询', 'serror-data-query', 'serror-data-query', 1, 'NULL', NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (86000, 80000, '数据抽取记录查询', 'pexact-data-query', 'pexact-data-query', 1, 'NULL', NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (87000, 80000, '制证错误数据查询', 'perror-data-query', 'perror-data-query', 1, 'NULL', NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (140000, 0, '系统管理', 'sys-manage', 'sys-manage', 0, 'NULL', NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (141000, 140000, '用户管理', 'sys-user-list', 'sys-user-list', 1, 'NULL', NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (142000, 140000, '系统配置', 'sys-config', 'sys-config', 1, 'NULL', NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (143000, 140000, '组织管理', 'sys-organizer-list', 'sys-organizer-list', 1, 'NULL', NULL, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (144000, 140000, '角色管理', 'sys-role-list', 'sys-role-list', 1, 'NULL', NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (150001, 150000, '权限配置', 'sys-user-role', 'sys-user-role', 2, 'NULL', NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (170000, 0, '系统监控', 'sys-monitor', 'sys-monitor', 0, 'NULL', NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (171000, 170000, '任务运行情况', 'schedule-monitor', 'schedule-monitor', 1, 'NULL', NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (172000, 170000, '登录日志', 'login-log', 'login-log', 1, 'NULL', NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (173000, 170000, '调度日志', 'schedule-log', 'schedule-log', 1, 'NULL', NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (174000, 170000, '操作日志', 'menu-log', 'menu-log', 1, 'NULL', NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (190000, 0, '数据生命周期管理', 'data-lifecycle-manage', 'data-lifecycle-manage', 0, 'NULL', NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (191000, 190000, '数据状态标识', 'data-status-log', 'data-status-log', 1, 'NULL', NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (192000, 190000, '数据变更轨迹', 'data-change-trajectory', 'data-change-trajectory', 1, 'NULL', NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (193000, 190000, '数据变更流程', 'data-change-process', 'data-change-process', 1, 'NULL', NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (230000, 0, '元数据管理', 'metadata-manage', 'metadata-manage', 1, 'NULL', NULL, 0, NULL, NULL, NULL, NULL, 0);
INSERT INTO `SYS_MENU` VALUES (240000, 0, '资源目录管理', 'dir-manage', 'dir-manage', 1, 'NULL', NULL, 0, NULL, NULL, NULL, NULL, 0);
COMMIT;

-- ----------------------------
-- Table structure for SYS_ORGANIZER
-- ----------------------------
DROP TABLE IF EXISTS `SYS_ORGANIZER`;
CREATE TABLE `SYS_ORGANIZER` (
  `ORGANIZER_ID` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '组织ID',
  `ORGANIZER_NAME` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '组织名称',
  `ORGANIZER_CONTACT` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ORGANIZER_EMAIL` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ORGANIZER_TELPHONE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ORGANIZER_MOBILE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ORGANIZER_ADDRESS` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ORGANIZER_VERIFY_CODE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ORGANIZER_STATUS` int(11) DEFAULT '0' COMMENT '0 已注册未验证通过，1已注册并验证通过， 2 注销',
  `CREATE_USER` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '最后修改用户',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '最后时间更新',
  `DEL_FLAG` tinyint(4) DEFAULT '0' COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`ORGANIZER_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1068523747487088642 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='组织信息';

-- ----------------------------
-- Records of SYS_ORGANIZER
-- ----------------------------
BEGIN;
INSERT INTO `SYS_ORGANIZER` VALUES (1068523747487088641, '北京傲飞商智软件有限公司', NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, 0);
COMMIT;

-- ----------------------------
-- Table structure for SYS_PLATFORM_LOG
-- ----------------------------
DROP TABLE IF EXISTS `SYS_PLATFORM_LOG`;
CREATE TABLE `SYS_PLATFORM_LOG` (
  `PLATFORM_LOG_ID` bigint(64) NOT NULL,
  `USERNAME` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户名',
  `MODULE` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '模块',
  `OPERATION` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户操作',
  `METHOD` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '请求方法',
  `PARAMS` varchar(5000) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '请求参数',
  `IP` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'IP地址',
  `CREATE_DATE` datetime DEFAULT NULL COMMENT '创建时间',
  `DEL_FLAG` tinyint(4) DEFAULT '0' COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`PLATFORM_LOG_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='系统日志';



-- ----------------------------
-- Table structure for SYS_REPOSITORY
-- ----------------------------
DROP TABLE IF EXISTS `SYS_REPOSITORY`;
CREATE TABLE `SYS_REPOSITORY` (
  `ID_REPOSITORY` bigint(64) NOT NULL COMMENT '资源库ID',
  `ID_REPOSITORY_CONNECTION` bigint(64) NOT NULL COMMENT '资源数据库链接ID',
  `REPOSITORY_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '资源库名称',
  `IS_DEFAULT` int(11) DEFAULT NULL COMMENT '是否默认资源库 0:否 1:是',
  `ORGANIZER_ID` bigint(64) DEFAULT NULL,
  `CREATE_USER` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '最后修改用户',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '最后时间更新',
  `DEL_FLAG` tinyint(4) DEFAULT '0' COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`ID_REPOSITORY`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='资源库管理';

-- ----------------------------
-- Records of SYS_REPOSITORY
-- ----------------------------
BEGIN;
INSERT INTO `SYS_REPOSITORY` VALUES (1, 1043540675649384450, 'kettle', 0, 1, 'admin', 'admin', '2018-10-09 15:36:24', '2018-10-09 15:36:16', 0);
COMMIT;

-- ----------------------------
-- Table structure for SYS_REPOSITORY_DATABASE
-- ----------------------------
DROP TABLE IF EXISTS `SYS_REPOSITORY_DATABASE`;
CREATE TABLE `SYS_REPOSITORY_DATABASE` (
  `ID_REPOSITORY_CONNECTION` bigint(64) NOT NULL COMMENT '资源数据库链接ID',
  `REPOSITORY_CONNECTION_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '资源数据库链接名称',
  `DATABASE_TYPE` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据库类型',
  `DATABASE_CONTYPE` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据库连接方式',
  `HOST_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '主机名',
  `DATABASE_NAME` mediumtext COLLATE utf8_unicode_ci COMMENT '数据库名',
  `PORT` int(11) DEFAULT NULL COMMENT '端口',
  `USERNAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户名',
  `PASSWORD` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '密码',
  `SERVERNAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '服务器名',
  `DATA_TBS` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据表空间',
  `INDEX_TBS` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '索引表空间',
  `CREATE_USER` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '最后修改用户',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '最后时间更新',
  `DEL_FLAG` tinyint(4) DEFAULT '0' COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`ID_REPOSITORY_CONNECTION`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='资源库';

-- ----------------------------
-- Records of SYS_REPOSITORY_DATABASE
-- ----------------------------
BEGIN;
INSERT INTO `SYS_REPOSITORY_DATABASE` VALUES (1043540675649384450, 'localhost', 'MYSQL', '0', 'localhost', 'kettle', 3306, 'root', 'Encrypted 2be98afc86aa7f2e4cb79ff228dc6fa8c', NULL, NULL, NULL, NULL, NULL, '2018-09-23 00:40:59', '2018-09-23 00:40:59', 0);
COMMIT;

-- ----------------------------
-- Table structure for SYS_REPOSITORY_DATABASE_ATTRIBUTE
-- ----------------------------
DROP TABLE IF EXISTS `SYS_REPOSITORY_DATABASE_ATTRIBUTE`;
CREATE TABLE `SYS_REPOSITORY_DATABASE_ATTRIBUTE` (
  `ID_REPOSITORY_DATABASE_ATTRIBUTE` bigint(64) NOT NULL COMMENT '资源库属性ID',
  `ID_REPOSITORY_CONNECTION` bigint(64) NOT NULL COMMENT '资源库ID',
  `CODE` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '属性名',
  `VALUE_STR` mediumtext COLLATE utf8_unicode_ci COMMENT '属性值',
  PRIMARY KEY (`ID_REPOSITORY_DATABASE_ATTRIBUTE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='资源库链接属性';

-- ----------------------------
-- Records of SYS_REPOSITORY_DATABASE_ATTRIBUTE
-- ----------------------------
BEGIN;
INSERT INTO `SYS_REPOSITORY_DATABASE_ATTRIBUTE` VALUES (1, 1043540675649384450, 'FORCE_IDENTIFIERS_TO_LOWERCASE', 'N');
INSERT INTO `SYS_REPOSITORY_DATABASE_ATTRIBUTE` VALUES (2, 1043540675649384450, 'FORCE_IDENTIFIERS_TO_UPPERCASE', 'N');
INSERT INTO `SYS_REPOSITORY_DATABASE_ATTRIBUTE` VALUES (3, 1043540675649384450, 'IS_CLUSTERED', 'N');
INSERT INTO `SYS_REPOSITORY_DATABASE_ATTRIBUTE` VALUES (4, 1043540675649384450, 'PRESERVE_RESERVED_WORD_CASE', 'Y');
INSERT INTO `SYS_REPOSITORY_DATABASE_ATTRIBUTE` VALUES (5, 1043540675649384450, 'QUOTE_ALL_FIELDS', 'N');
INSERT INTO `SYS_REPOSITORY_DATABASE_ATTRIBUTE` VALUES (6, 1043540675649384450, 'STREAM_RESULTS', 'Y');
INSERT INTO `SYS_REPOSITORY_DATABASE_ATTRIBUTE` VALUES (7, 1043540675649384450, 'SUPPORTS_BOOLEAN_DATA_TYPE', 'Y');
INSERT INTO `SYS_REPOSITORY_DATABASE_ATTRIBUTE` VALUES (8, 1043540675649384450, 'SUPPORTS_TIMESTAMP_DATA_TYPE', 'Y');
INSERT INTO `SYS_REPOSITORY_DATABASE_ATTRIBUTE` VALUES (9, 1043540675649384450, 'USE_POOLING', 'N');
COMMIT;

-- ----------------------------
-- Table structure for SYS_ROLE
-- ----------------------------
DROP TABLE IF EXISTS `SYS_ROLE`;
CREATE TABLE `SYS_ROLE` (
  `C_ROLE_ID` bigint(64) NOT NULL,
  `C_ROLE_NAME` varchar(100) DEFAULT NULL COMMENT '角色名称',
  `C_DESCRIPTION` varchar(100) DEFAULT NULL COMMENT '备注',
  `C_ORGANIZER_ID` bigint(64) DEFAULT NULL,
  `C_PRIVILEDGES` bigint(20) DEFAULT NULL COMMENT '设计器权限',
  `C_ISSYSTEMROLE` int(11) DEFAULT NULL COMMENT '是否是系统保留权限',
  `CREATE_USER` varchar(64) DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(64) DEFAULT NULL COMMENT '最后修改用户',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '最后时间更新',
  `DEL_FLAG` tinyint(4) DEFAULT '0' COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`C_ROLE_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色';

-- ----------------------------
-- Records of SYS_ROLE
-- ----------------------------
BEGIN;
INSERT INTO `SYS_ROLE` VALUES (1, '超级管理员', '系统保留不可修改', 0, NULL, 1, 'admin', 'admin', '2020-11-09 16:19:50', '2020-11-10 10:39:39', 0);
INSERT INTO `SYS_ROLE` VALUES (1325789439473860609, '服务端用户', '服务端用户', 1068523747487088641, NULL, 0, 'doetl', 'doetl', '2020-11-09 21:16:49', '2020-11-10 18:17:16', 0);
INSERT INTO `SYS_ROLE` VALUES (1326106719193686018, '制证中心用户', '制证中心用户', 1068523747487088641, NULL, 0, 'doetl', 'doetl', '2020-11-10 18:17:34', '2020-11-10 18:17:34', 0);
COMMIT;

-- ----------------------------
-- Table structure for SYS_ROLE_MENU
-- ----------------------------
DROP TABLE IF EXISTS `SYS_ROLE_MENU`;
CREATE TABLE `SYS_ROLE_MENU` (
  `ROLE_ID` bigint(64) NOT NULL COMMENT '角色ID',
  `MENU_ID` bigint(64) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`ROLE_ID`,`MENU_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色与菜单对应关系';

-- ----------------------------
-- Records of SYS_ROLE_MENU
-- ----------------------------
BEGIN;
INSERT INTO `SYS_ROLE_MENU` VALUES (1, 1);
INSERT INTO `SYS_ROLE_MENU` VALUES (1, 10000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1, 13000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1, 14000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1, 15000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1, 40000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1, 41000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1, 42000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1, 50000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1, 51000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1, 52000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1, 53000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1, 54000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1, 80000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1, 81000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1, 82000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1, 83000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1, 84000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1, 85000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1, 86000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1, 87000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1, 140000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1, 141000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1, 142000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1, 144000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1, 170000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1, 171000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1, 172000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1, 173000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1, 174000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1, 190000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1, 191000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1, 192000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1, 193000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1, 230000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1, 240000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1325789439473860609, 10000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1325789439473860609, 13000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1325789439473860609, 14000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1325789439473860609, 15000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1325789439473860609, 40000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1325789439473860609, 41000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1325789439473860609, 42000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1325789439473860609, 50000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1325789439473860609, 51000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1325789439473860609, 52000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1325789439473860609, 53000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1325789439473860609, 54000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1325789439473860609, 81000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1325789439473860609, 82000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1325789439473860609, 83000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1325789439473860609, 84000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1325789439473860609, 85000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1325789439473860609, 140000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1325789439473860609, 141000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1325789439473860609, 142000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1325789439473860609, 144000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1325789439473860609, 170000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1325789439473860609, 171000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1325789439473860609, 172000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1325789439473860609, 173000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1325789439473860609, 174000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1325789439473860609, 190000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1325789439473860609, 191000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1325789439473860609, 192000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1325789439473860609, 193000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1325789439473860609, 230000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1325789439473860609, 240000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1326106719193686018, 10000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1326106719193686018, 13000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1326106719193686018, 14000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1326106719193686018, 15000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1326106719193686018, 50000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1326106719193686018, 51000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1326106719193686018, 52000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1326106719193686018, 53000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1326106719193686018, 54000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1326106719193686018, 85000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1326106719193686018, 87000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1326106719193686018, 140000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1326106719193686018, 141000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1326106719193686018, 142000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1326106719193686018, 144000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1326106719193686018, 170000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1326106719193686018, 171000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1326106719193686018, 172000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1326106719193686018, 173000);
INSERT INTO `SYS_ROLE_MENU` VALUES (1326106719193686018, 174000);
COMMIT;

-- ----------------------------
-- Table structure for SYS_ROLE_OPERATION
-- ----------------------------
DROP TABLE IF EXISTS `SYS_ROLE_OPERATION`;
CREATE TABLE `SYS_ROLE_OPERATION` (
  `ID_OPERATION` bigint(64) NOT NULL COMMENT '操作ID',
  `OPERATION_NAME` varchar(32) DEFAULT NULL COMMENT '操作名称',
  PRIMARY KEY (`ID_OPERATION`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='设计器操作';

-- ----------------------------
-- Records of SYS_ROLE_OPERATION
-- ----------------------------
BEGIN;
INSERT INTO `SYS_ROLE_OPERATION` VALUES (1, '新建');
INSERT INTO `SYS_ROLE_OPERATION` VALUES (2, '删除');
INSERT INTO `SYS_ROLE_OPERATION` VALUES (4, '修改');
INSERT INTO `SYS_ROLE_OPERATION` VALUES (8, '执行');
INSERT INTO `SYS_ROLE_OPERATION` VALUES (16, '浏览');
COMMIT;

-- ----------------------------
-- Table structure for SYS_ROLE_PRIVILEDGE
-- ----------------------------
DROP TABLE IF EXISTS `SYS_ROLE_PRIVILEDGE`;
CREATE TABLE `SYS_ROLE_PRIVILEDGE` (
  `ID_PRIVILEDGE` bigint(64) NOT NULL COMMENT '权限ID',
  `ID_RESOURCE_TYPE` bigint(64) NOT NULL COMMENT '类型ID',
  `ID_OPERATION` bigint(64) NOT NULL COMMENT '操作ID',
  PRIMARY KEY (`ID_PRIVILEDGE`,`ID_RESOURCE_TYPE`,`ID_OPERATION`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='设计器权限';

-- ----------------------------
-- Records of SYS_ROLE_PRIVILEDGE
-- ----------------------------
BEGIN;
INSERT INTO `SYS_ROLE_PRIVILEDGE` VALUES (1, 1, 1);
INSERT INTO `SYS_ROLE_PRIVILEDGE` VALUES (2, 1, 4);
INSERT INTO `SYS_ROLE_PRIVILEDGE` VALUES (4, 1, 2);
INSERT INTO `SYS_ROLE_PRIVILEDGE` VALUES (8, 1, 16);
INSERT INTO `SYS_ROLE_PRIVILEDGE` VALUES (16, 1, 8);
INSERT INTO `SYS_ROLE_PRIVILEDGE` VALUES (32, 2, 1);
INSERT INTO `SYS_ROLE_PRIVILEDGE` VALUES (64, 2, 4);
INSERT INTO `SYS_ROLE_PRIVILEDGE` VALUES (128, 2, 2);
INSERT INTO `SYS_ROLE_PRIVILEDGE` VALUES (256, 2, 16);
INSERT INTO `SYS_ROLE_PRIVILEDGE` VALUES (512, 4, 1);
INSERT INTO `SYS_ROLE_PRIVILEDGE` VALUES (1024, 4, 4);
INSERT INTO `SYS_ROLE_PRIVILEDGE` VALUES (2048, 4, 2);
INSERT INTO `SYS_ROLE_PRIVILEDGE` VALUES (4096, 8, 1);
INSERT INTO `SYS_ROLE_PRIVILEDGE` VALUES (8192, 8, 4);
INSERT INTO `SYS_ROLE_PRIVILEDGE` VALUES (16384, 8, 2);
INSERT INTO `SYS_ROLE_PRIVILEDGE` VALUES (32768, 8, 16);
COMMIT;

-- ----------------------------
-- Table structure for SYS_ROLE_RESOURCE_TYPE
-- ----------------------------
DROP TABLE IF EXISTS `SYS_ROLE_RESOURCE_TYPE`;
CREATE TABLE `SYS_ROLE_RESOURCE_TYPE` (
  `ID_RESOURCE_TYPE` bigint(64) NOT NULL COMMENT '设计器资源ID',
  `RESOURCE_TYPE_NAME` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '设计器资源名称(文件 数据库连接等)',
  PRIMARY KEY (`ID_RESOURCE_TYPE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='设计器权限类型';

-- ----------------------------
-- Records of SYS_ROLE_RESOURCE_TYPE
-- ----------------------------
BEGIN;
INSERT INTO `SYS_ROLE_RESOURCE_TYPE` VALUES (1, '文件');
INSERT INTO `SYS_ROLE_RESOURCE_TYPE` VALUES (2, '目录');
INSERT INTO `SYS_ROLE_RESOURCE_TYPE` VALUES (4, '集群');
INSERT INTO `SYS_ROLE_RESOURCE_TYPE` VALUES (8, '数据源');
COMMIT;

-- ----------------------------
-- Table structure for SYS_SMS_COUNTRY
-- ----------------------------
DROP TABLE IF EXISTS `SYS_SMS_COUNTRY`;
CREATE TABLE `SYS_SMS_COUNTRY` (
  `COUNTRY_CODE` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `COUNTRY_NAME` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`COUNTRY_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of SYS_SMS_COUNTRY
-- ----------------------------
BEGIN;
INSERT INTO `SYS_SMS_COUNTRY` VALUES ('001', 'America');
INSERT INTO `SYS_SMS_COUNTRY` VALUES ('33', 'France');
INSERT INTO `SYS_SMS_COUNTRY` VALUES ('34', 'Spain');
INSERT INTO `SYS_SMS_COUNTRY` VALUES ('351', 'Portugal');
INSERT INTO `SYS_SMS_COUNTRY` VALUES ('44', 'United Kingdom');
INSERT INTO `SYS_SMS_COUNTRY` VALUES ('49', 'Germany');
INSERT INTO `SYS_SMS_COUNTRY` VALUES ('60', 'Malaysia');
INSERT INTO `SYS_SMS_COUNTRY` VALUES ('62', 'Indonesia');
INSERT INTO `SYS_SMS_COUNTRY` VALUES ('65', 'Singapore');
INSERT INTO `SYS_SMS_COUNTRY` VALUES ('86', '中国');
COMMIT;

-- ----------------------------
-- Table structure for SYS_USER
-- ----------------------------
DROP TABLE IF EXISTS `SYS_USER`;
CREATE TABLE `SYS_USER` (
  `C_USER_ID` bigint(64) NOT NULL COMMENT '主键',
  `C_USER_NAME` varchar(32) DEFAULT NULL COMMENT '用户名',
  `C_PASSWORD` varchar(32) DEFAULT NULL COMMENT '密码',
  `C_NICK_NAME` varchar(32) DEFAULT NULL COMMENT '昵称（名称）',
  `C_EMAIL` varchar(32) DEFAULT NULL COMMENT '邮箱',
  `C_COUNTRY_CODE` varchar(20) DEFAULT NULL COMMENT '国家代码',
  `C_MOBILEPHONE` varchar(32) DEFAULT NULL COMMENT '手机',
  `C_DESCRIPTION` varchar(255) DEFAULT NULL COMMENT '描述',
  `C_IS_SYSTEM_USER` int(11) DEFAULT NULL COMMENT '系统用户',
  `C_ORGANIZER_ID` bigint(64) DEFAULT NULL COMMENT '组织ID',
  `C_USER_STATUS` int(11) DEFAULT NULL COMMENT '用户状态',
  `LAST_LOGIN_TIME` datetime DEFAULT NULL COMMENT '最后一次登录时间',
  `LAST_LOGIN_IP` varchar(20) DEFAULT NULL COMMENT '最后一次登录IP',
  `C_DISK_SPACE` bigint(20) DEFAULT '1073741824' COMMENT '磁盘空间',
  `CREATE_USER` varchar(64) DEFAULT NULL COMMENT '创建用户',
  `UPDATE_USER` varchar(64) DEFAULT NULL COMMENT '最后修改用户',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '最后时间更新',
  `DEL_FLAG` tinyint(4) DEFAULT '0' COMMENT '是否删除  1：已删除  0：正常',
  PRIMARY KEY (`C_USER_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户表';

-- ----------------------------
-- Records of SYS_USER
-- ----------------------------
BEGIN;
INSERT INTO `SYS_USER` VALUES (1, 'admin', '96e79218965eb72c92a549dd5a330112', 'admin', '381906259@qq.com', NULL, '13614019065', '管理员', 1, 0, 0, '2020-11-10 21:07:48', '0:0:0:0:0:0:0:1', 1073741824, NULL, 'doetl', '2020-10-28 16:47:53', '2020-10-28 10:32:23', 0);
INSERT INTO `SYS_USER` VALUES (1077874329775038466, 'doetl', '96e79218965eb72c92a549dd5a330112', 'doetl', NULL, '86', '13380348067', NULL, 0, 1068523747487088641, 0, '2021-01-25 16:51:30', '120.244.32.192', 1073741824, NULL, NULL, '2020-10-28 16:47:56', '2020-10-28 16:47:41', 0);
INSERT INTO `SYS_USER` VALUES (1326317578678726658, 'doetl1', '96e79218965eb72c92a549dd5a330112', '服务', '123456@qq.com', NULL, '13311111111', '服务端用户', 0, 1068523747487088641, 0, '2021-01-25 09:27:25', '183.30.88.235', 1073741824, 'doetl', 'doetl', '2020-11-11 08:15:27', '2020-11-11 08:15:27', 0);
INSERT INTO `SYS_USER` VALUES (1326318037690773505, 'doetl2', '96e79218965eb72c92a549dd5a330112', '制证中心', '1234567@qq.com', NULL, '13322222222', '制证中心用户', 0, 1068523747487088641, 0, '2021-01-06 14:25:41', '117.136.0.250', 1073741824, 'doetl', 'doetl', '2020-11-11 08:17:16', '2020-11-11 08:17:16', 0);
INSERT INTO `SYS_USER` VALUES (1333339728962940930, 'test1', '120705de7e61c5b322ad798b8ef225a7', 'zhangsan', '1234@qq.com', NULL, '13355456356', '', NULL, 1068523747487088641, NULL, NULL, NULL, 1073741824, 'doetl', 'doetl', '2020-11-30 17:18:58', '2020-11-30 17:18:58', 0);
COMMIT;

-- ----------------------------
-- Table structure for SYS_USER_ROLE
-- ----------------------------
DROP TABLE IF EXISTS `SYS_USER_ROLE`;
CREATE TABLE `SYS_USER_ROLE` (
  `C_USER_ID` bigint(64) NOT NULL DEFAULT '0',
  `C_ROLE_ID` bigint(64) NOT NULL DEFAULT '0',
  PRIMARY KEY (`C_USER_ID`,`C_ROLE_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of SYS_USER_ROLE
-- ----------------------------
-- ----------------------------
-- Records of SYS_USER_ROLE
-- ----------------------------
BEGIN;
INSERT INTO `SYS_USER_ROLE` VALUES (1077874329775038466, 1);
INSERT INTO `SYS_USER_ROLE` VALUES (1326317578678726658, 1325789439473860609);
INSERT INTO `SYS_USER_ROLE` VALUES (1326318037690773505, 1326106719193686018);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
