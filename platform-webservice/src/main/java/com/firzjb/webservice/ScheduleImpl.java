package com.firzjb.webservice;

import org.firzjb.schedule.model.response.JobDetailsResponse;
import org.firzjb.schedule.service.ICycleScheduleService;
import org.firzjb.schedule.service.IJobDetailsService;
import com.firzjb.webservice.controller.Schedule;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jws.WebService;

@WebService(endpointInterface = "com.firzjb.webservice.controller.Schedule")
public class ScheduleImpl implements Schedule {



    @Autowired
    private IJobDetailsService jobDetailsService;

    @Autowired
    private ICycleScheduleService cycleScheduleService;

    @Override
    public Boolean execute(String jobName) {

        JobDetailsResponse response =  jobDetailsService.getScheduleByName(jobName);
        if(response!=null){
            try {
                return cycleScheduleService.execute(response.getJobName(),response.getJobGroup(),null);
            } catch (SchedulerException e) {
                e.printStackTrace();
                return false;
            }
        }

        return false;
    }
}
