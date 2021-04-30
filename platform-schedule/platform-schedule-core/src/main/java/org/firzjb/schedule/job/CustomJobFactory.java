package org.firzjb.schedule.job;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

/**
 * @auther 制证数据实时汇聚系统
 * @create 2018-10-02 19:46
 */
public class CustomJobFactory extends SpringBeanJobFactory {

    @Autowired
    private AutowireCapableBeanFactory capableBeanFactory;


    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {

        //调用父类的方法
        Object jobInstance = super.createJobInstance(bundle);
        //进行注入
        capableBeanFactory.autowireBean(jobInstance);

        return jobInstance;
    }
}
