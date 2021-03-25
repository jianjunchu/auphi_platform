/*******************************************************************************
 *
 * Auphi Data Integration PlatformKettle Platform
 * Copyright C 2011-2018 by Auphi BI : http://www.doetl.com

 * Support：support@pentahochina.com
 *
 *******************************************************************************
 *
 * Licensed under the LGPL License, Version 3.0 the "License";
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    https://opensource.org/licenses/LGPL-3.0

 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/
package org.firzjb.admin.init;


import com.alibaba.druid.pool.DruidDataSource;
import org.firzjb.kettle.App;
import org.firzjb.kettle.core.PropsUI;
import org.firzjb.sys.utils.RepositoryCodec;
import org.firzjb.utils.CsvUtils;
import org.firzjb.utils.StringUtils;
import org.joda.time.DateTime;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.Props;
import org.pentaho.di.core.database.Database;
import org.pentaho.di.core.encryption.Encr;
import org.pentaho.di.core.exception.KettleDatabaseException;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.core.variables.Variables;
import org.pentaho.di.i18n.LanguageChoice;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;


/**
 * 系统启动Init类
 * @auther 傲飞数据整合平台
 * @create 2018-09-21 18:13
 */
@Component
public class SystemInitializingBean implements InitializingBean, DisposableBean {


    @Autowired
    private DruidDataSource dataSource;

    private static Logger logger = LoggerFactory.getLogger(SystemInitializingBean.class);

    private final Timer repositoryTimer = new Timer();




    /**
     * 系统初始化
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        long start = System.currentTimeMillis();
        String simple_jndi = System.getProperty("etl_platform.root")+ File.separator+"simple-jndi";
        String plugins = System.getProperty("etl_platform.root")+ File.separator+"plugins";
        System.setProperty("org.osjava.sj.root", simple_jndi);
        System.setProperty("KETTLE_JNDI_ROOT", simple_jndi);
        System.setProperty("KETTLE_PLUGIN_BASE_FOLDERS",plugins);
        logger.info("********************************************");
        logger.info("********北京傲飞商智软件有限公司***************");
        logger.info("********傲飞数据整合平台**********************");
        logger.info("********系统开始启动字典装载程序***************");
        logger.info("********开始加载资源库************************");
        logger.info("********************************************");
        LanguageChoice.getInstance().setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        KettleLogStore.init( 5000000, 720 );

        PropsUI.init( "KettleWebConsole", Props.TYPE_PROPERTIES_KITCHEN );

        //KettleDatabaseRepository repository =  RepositoryCodec.decodeDefault(dataSource);
        //repository.getDatabase().getDatabaseMeta().setSupportsBooleanDataType(true);
        //repository.connect(Const.REPOSITORY_USERNAME,Const.REPOSITORY_PASSWORD);
        //App.getInstance().setRepository(repository);
        //CheckRepositoryTimerTask checkRepositoryTimerTask = new CheckRepositoryTimerTask();
        //repositoryTimer.schedule(checkRepositoryTimerTask,0,1000*60*1);
        applyVariables();
        App.getInstance().setKettleDatabaseRepositoryMeta(RepositoryCodec.getDatabaseRepositoryMeta(dataSource));



        long timeSec = (System.currentTimeMillis() - start) / 1000;
        logger.info("****************************************************************************************");
        logger.info("平台启动成功[" + DateTime.now().toString() + "]");
        logger.info("启动总耗时: " + timeSec / 60 + "分 " + timeSec % 60 + "秒 ");
        logger.info("****************************************************************************************");

        System.setProperty("org.osjava.sj.root", simple_jndi);
        System.setProperty("KETTLE_JNDI_ROOT", simple_jndi);
        System.setProperty("KETTLE_PLUGIN_BASE_FOLDERS",plugins);
    }

    private void applyVariables() throws KettleException, IOException {
        App.space =  Variables.getADefaultVariableSpace();
        String sysPath = System.getProperty("user.dir");
        String filePath = sysPath+ File.separator+"config.csv";
        File file = new File(filePath);
        if(file.exists()){
            List<String[]> list = CsvUtils.read(filePath,true);
            for(String[] cls : list){
                if(cls!=null && cls.length > 0){
                    String kv = cls[0];
                    logger.info("********************************************");
                    logger.info(kv);
                    if(!StringUtils.isEmpty(kv) && !kv.startsWith("#") && kv.indexOf("=")>0){
                        String[] kvs = kv.split("=");
                        if(kvs!=null && kvs.length ==1){
                            String key = kvs[0];
                            App.space.setVariable(key,null);
                        }else if(kvs!=null && kvs.length ==2){
                            String key = kvs[0];
                            String value = kvs[1];
                            if("HisUserName".equals(key)
                                    || "HisUserPwd".equals(key)
                                    || "HisYdUserName".equals(key)
                                    || "HisYdUserPwd".equals(key)
                                    || "HisGatUserName".equals(key)
                                    || "HisGatUserPwd".equals(key)
                                    || "PhisUserName".equals(key)
                                    || "PhisUserPwd".equals(key)){

                                value = Encr.PASSWORD_ENCRYPTED_PREFIX + value;
                            }

                            App.space.setVariable(key,value);
                        }
                    }
                    logger.info("********************************************");
                }
            }

        }

        KettleEnvironment.init();
    }

    /**
     * 系统停止时要执行的方法
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        logger.info("********************************************");
        logger.info("******************正在停止系统****************");
        Map<String, Repository> repositorys = App.getInstance().getRepositories();
        for(String key : repositorys.keySet()){

            KettleDatabaseRepository repository = (KettleDatabaseRepository) repositorys.get(key);
            if(repository.isConnected()){
                repository.disconnect();
                if(!repository.getDatabase().getConnection().isClosed()){
                    repository.getDatabase().getConnection().close();
                }
                logger.info("disconnect=>"+key);
            }
        }
    }

    /**
     * 检查资源库的连接;连接断开要重新连接的
     */
    class CheckRepositoryTimerTask extends TimerTask {
        private  Logger logger = LoggerFactory.getLogger(CheckRepositoryTimerTask.class);


        @Override
        public void run() {
            KettleDatabaseRepository repository = (KettleDatabaseRepository) App.getInstance().getRepository();
            Database database = repository.getDatabase();
            try {
                logger.info("==============check repository connect=================");
                database.openQuery("SELECT 'x'");
            } catch (KettleDatabaseException e) {
                try {
                    repository.disconnect();
                    repository.setConnected(false);
                    repository.connect(org.firzjb.base.common.Const.REPOSITORY_USERNAME,org.firzjb.base.common.Const.REPOSITORY_PASSWORD);
                    logger.info("==============重新链接资源库=================");

                } catch (KettleException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        }
    }
}
