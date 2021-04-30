package org.firzjb.base.logs;

import org.apache.log4j.*;
import org.apache.log4j.helpers.AppenderAttachableImpl;
import org.apache.log4j.spi.LoggerRepository;

public class EtlLogger extends Logger {
    protected EtlLogger(String name) {
        super(name);
    }

    //Appender的管理类，用于维护和管理Logger中的Appender集合
    AppenderAttachableImpl aai;


    public EtlLogger(String name, String logID, Level level) {
        super(name);

        PatternLayout layout = new PatternLayout();
        layout.setConversionPattern("%-d{yyyy-MM-dd HH:mm:ss} [%t %l] %m%n");

        RollingFileAppender appender = new RollingFileAppender();
        appender.setLayout(layout);
        appender.setFile("../logs/kettle/"+logID+".log");
        appender.setEncoding("UTF-8");
        appender.setAppend(true);
        appender.activateOptions();

        //repository是Category中的一个成员变量，具体作用未深入研究，似乎和日志等级有关。repository的设置必须在addAppender()之前。
        this.repository = Logger.getRootLogger().getLoggerRepository();
        this.addAppender(appender);
        this.setLevel(level);
        this.parent = Logger.getRootLogger();
    }

    public synchronized void addAppender(Appender newAppender) {
        if (aai == null)
            aai = new AppenderAttachableImpl();
        aai.addAppender(newAppender);
        repository.fireAddAppenderEvent(this, newAppender);
    }

    final void setHierarchy(LoggerRepository repository) {
        this.repository = repository;
    }


}
