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
package com.aofei.schedule.util;

import com.aofei.base.exception.ApplicationException;
import com.aofei.schedule.model.request.GeneralScheduleRequest;
import com.aofei.utils.DateUtils;
import com.aofei.utils.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerUtils;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class QuartzUtil {

    private static Logger logger = LoggerFactory.getLogger(QuartzUtil.class);
    /**
     * 执行一次
     */
    public static final int MODE_ONCE = 1;
    /**
     * 每几秒执行一次
     */
    public static final int MODE_SECOND = 2;
    /**
     * 每几分钟执行一次
     */
    public static final int MODE_MINUTE = 3;
    /**
     * 每几小时执行一次
     */
    public static final int MODE_HOUR = 4;

    /**
     * 周期天
     */
    public static final int MODE_DAY = 5;
    /**
     * 周期星期
     */
    public static final int MODE_WEEK = 6;
    /**
     * 周期月
     */
    public static final int MODE_MONTH = 7;
    /**
     * 周期年
     */
    public static final int MODE_YEAR = 8;

    public static final  String kg = " ";
    public static final  String wh = "?";
    public static final  String xx = "*";
    public static final  String xg = "/";

    public static Trigger getTrigger(GeneralScheduleRequest request, String group){


        Date satrtDate = request.getStartTime();
        Date endDate = request.getEndTime();
        String name = request.getJobName();

        String cron = getCron(request);

        logger.debug(cron.toString());
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(name, group)
                .startAt(satrtDate)
                .endAt(endDate)
                .withSchedule(CronScheduleBuilder.cronSchedule(cron.toString()))
                .build();
        return trigger;

    }

    public static String getCron(GeneralScheduleRequest request) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(request.getStartTime());

        StringBuffer cronString = new StringBuffer();
        switch (request.getCycle()) {
            //执行一次
            case MODE_ONCE:
                long repeatInterval = ca.getTime().getTime() - new Date().getTime();
                if(repeatInterval>0){
                    /**
                     * 只执行一次的Cron表达式 如: 2020年3月11日13点27分15秒,?指的是不考虑星期几
                     * 15 27 13 11 3 ? 2020
                     */
                    cronString.append(ca.get(Calendar.SECOND)).append(kg)
                            .append(ca.get(Calendar.MINUTE)).append(kg)
                            .append(ca.get(Calendar.HOUR_OF_DAY)).append(kg)
                            .append(ca.get(Calendar.DATE)).append(kg)
                            .append(ca.get(Calendar.MONTH)+1).append(kg)
                            .append(wh).append(kg)
                            .append(ca.get(Calendar.YEAR));

                }else{

                }

                break;
            case MODE_SECOND:
                //周期秒 每?几秒执行一次
                //每隔5秒执行一次：*/5 * * * * ?

                cronString.append(xx).append(xg).append(request.getCycleNum()).append(kg)
                        .append(xx).append(kg)
                        .append(xx).append(kg)
                        .append(xx).append(kg)
                        .append(xx).append(kg)
                        .append(wh);

                break;
            case MODE_MINUTE:

                //周期分钟 每?几分钟执行一次
                //每隔1分钟执行一次：0 */1 * * * ?
                cronString.append(ca.get(Calendar.SECOND)).append(kg)
                        .append(xx).append(xg).append(request.getCycleNum()).append(kg)
                        .append(xx).append(kg)
                        .append(xx).append(kg)
                        .append(xx).append(kg)
                        .append(wh);

                break;
            case MODE_HOUR:

                //周期小时 每?几小时执行一次
                //每隔1分钟执行一次：0 0 */1  * * ?
                cronString.append(ca.get(Calendar.SECOND)).append(kg)
                        .append(ca.get(Calendar.MINUTE)).append(kg)
                        .append(xx).append(xg).append(request.getCycleNum()).append(kg)
                        .append(xx).append(kg)
                        .append(xx).append(kg)
                        .append(wh);
                break;
            case MODE_DAY:


                if(1 == request.getDayType()){

                    cronString.append(ca.get(Calendar.SECOND)).append(kg)
                            .append(ca.get(Calendar.MINUTE)).append(kg)
                            .append( ca.get(Calendar.HOUR_OF_DAY)).append(kg)
                            .append(wh).append(kg)
                            .append(xx).append(kg)
                            .append(xx);

                }else if(2 == request.getDayType()){

                    cronString.append(ca.get(Calendar.SECOND)).append(kg)
                            .append(ca.get(Calendar.MINUTE)).append(kg)
                            .append( ca.get(Calendar.HOUR_OF_DAY)).append(kg)
                            .append(wh).append(kg)
                            .append(xx).append(kg)
                            .append("MON-FRI");
                }else{
                    throw new ApplicationException();
                }

                break;
            case MODE_WEEK:

                cronString.append(ca.get(Calendar.SECOND)).append(kg)
                        .append(ca.get(Calendar.MINUTE)).append(kg)
                        .append( ca.get(Calendar.HOUR_OF_DAY)).append(kg)
                        .append(wh).append(kg)
                        .append(xx).append(kg)
                        .append(request.getCycleNum());

                break;
            case MODE_MONTH:
                if(1 == request.getMonthType()){

                    cronString.append(ca.get(Calendar.SECOND)).append(kg)
                            .append(ca.get(Calendar.MINUTE)).append(kg)
                            .append( ca.get(Calendar.HOUR_OF_DAY)).append(kg)
                            .append(request.getCycleNum()).append(kg)
                            .append(xx).append(kg)
                            .append(wh);



                }else if(2 == request.getMonthType()){
                    String weeknum = StringUtils.defaultString(request.getWeekNum()) ;
                    String daynum = StringUtils.defaultString(request.getDayNum());

                    if(!"L".equals(weeknum)){
                        weeknum = "#" + weeknum;
                    }

                    cronString.append(ca.get(Calendar.SECOND)).append(kg)
                            .append(ca.get(Calendar.MINUTE)).append(kg)
                            .append( ca.get(Calendar.HOUR_OF_DAY)).append(kg)
                            .append(wh).append(kg)
                            .append(xx).append(kg)
                            .append(daynum).append(weeknum);

                }


                break;
            case MODE_YEAR:

                if(1 == request.getYearType()){//month and day
                    String[] monthAndDay = request.getCycleNum().split("-");

                    cronString.append(ca.get(Calendar.SECOND)).append(kg)
                            .append(ca.get(Calendar.MINUTE)).append(kg)
                            .append( ca.get(Calendar.HOUR_OF_DAY)).append(kg)
                            .append(monthAndDay[1]).append(kg)
                            .append(monthAndDay[0]).append(kg)
                            .append(wh);


                }else if(2 == request.getYearType()){//month week and day
                    String monthnum = StringUtils.defaultString(request.getMonthNum()) ;
                    String weeknum = StringUtils.defaultString(request.getWeekNum()) ;
                    String daynum = StringUtils.defaultString(request.getDayNum());

                    if(!"L".equals(weeknum)){
                        weeknum = "#" + weeknum;
                    }

                    cronString.append(ca.get(Calendar.SECOND)).append(kg)
                            .append(ca.get(Calendar.MINUTE)).append(kg)
                            .append( ca.get(Calendar.HOUR_OF_DAY)).append(kg)
                            .append(wh).append(kg)
                            .append(monthnum).append(kg).append(daynum).append(weeknum);

                }

                break;
        }

        return cronString.toString();
    }

    /**
     *
     * @param cron cron  表达式
     * @throws ParseException
     * @throws InterruptedException
     */
    public static List<Date> getQuartzPlan(String cron,Date satrtTime) throws ParseException {

        CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
        cronTriggerImpl.setCronExpression(cron);//这里写要准备猜测的cron表达式

        Date origin = DateUtils.startOfDay(new Date());
        if(origin.getTime() < satrtTime.getTime()){
            origin = satrtTime;
        }

        Date end= DateUtils.endOfDay(new Date());
        //这个是重点，计算两个时间之间的触发时间
         return TriggerUtils.computeFireTimesBetween(cronTriggerImpl, null, origin, end);

    }




}
