package org.firzjb.schedule.service.impl;

import org.firzjb.base.common.Const;
import org.firzjb.base.model.response.CurrentUserResponse;
import org.firzjb.base.service.impl.BaseService;
import org.firzjb.kettle.App;
import org.firzjb.schedule.entity.Monitor;
import org.firzjb.schedule.mapper.MonitorMapper;
import org.firzjb.schedule.model.request.MonitorRequest;
import org.firzjb.schedule.model.response.*;
import org.firzjb.schedule.service.IMonitorService;
import org.firzjb.sys.model.response.UserResponse;
import org.firzjb.sys.service.IUserService;
import org.firzjb.utils.BeanCopier;
import com.baomidou.mybatisplus.plugins.Page;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.RepositoryElementMetaInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MonitorService extends BaseService<MonitorMapper, Monitor> implements IMonitorService {

    @Autowired
    private IUserService userService;

    @Override
    public Page<MonitorResponse> getPage(Page<Monitor> page, MonitorRequest request) {
        List<Monitor> list = baseMapper.findList(page, request);
        page.setRecords(list);
        return convert(page, MonitorResponse.class);
    }



    /**
     * 返回首页仪表盘数据
     *     int runCount: 运行中作业
     *     int finishCount:运行完成作业;
     *     int allCount:总作业数
     *     int errorCount:错误作业数
     *     RunTimesResponse runTimes:作业耗时(前五)
     *     RunCountResponse runCounts:近七天完成数和错误数量
     * @param user 当前登录的用户
     * @return
     * @throws KettleException
     */
    @Override
    public DashboardResponse getDashboardCount(CurrentUserResponse user) throws KettleException {
        DashboardResponse response = new DashboardResponse();

        UserResponse userResponse =  userService.get(user.getUserId());
        long organizerId = user.getOrganizerId();
        if(userResponse.getIsSystemUser() == 1){
            organizerId = -1;
        }

        int allCount = baseMapper.countAll(organizerId);//总作业数
        response.setAllCount(allCount);

        int runCount = baseMapper.countRuning(organizerId);//运行中作业
        response.setRunCount(runCount);

        int finishCount = baseMapper.countFinish(organizerId);//运行完成作业
        response.setFinishCount(finishCount);

        int errorCount = baseMapper.countError(organizerId);//运行完成作业
        response.setErrorCount(errorCount);

        Page page =  new Page(1, 5);
        RunTimesResponse runTimes = new RunTimesResponse();

        List<Map<String,Object>> list1 = baseMapper.getTimeConsumingTop5(page,organizerId);
        for(Map<String,Object> map : list1){
            runTimes.getNames().add(map.get("NAME"));
            runTimes.getTimes().add(map.get("TIME"));
        }
        response.setRunTimes(runTimes);


        List<Map<String,Object>> list2 = baseMapper.get7DayErrorsAndFinishs(organizerId);
        RunCountResponse runCounts = new RunCountResponse();
        for(Map<String,Object> map : list2){
            runCounts.getDatetimes().add(map.get("DATETIME"));
            runCounts.getErrors().add( map.get("ERROR"));
            runCounts.getFinishs().add( Integer.valueOf(map.get("FINISH").toString()));
        }
        response.setRunCounts(runCounts);


        return response;
    }

    @Override
    public MonitorResponse getPlanMonitor(MonitorRequest request) {
        List<Monitor> list = baseMapper.findPlanMonitor(request);
        if(list.size()>0){
            return BeanCopier.copy(list.get(0), MonitorResponse.class);
        }
        return null;
    }

    @Override
    public boolean isRun(String jobName, String jobGroup) {

       int count =  baseMapper.countScheduleRuning(jobName,jobGroup);
       if(count>0){
            return true;
       }
        return false;
    }


    private int getAllCount(CurrentUserResponse user) throws KettleException {
        int count = 0;
        Repository repository = App.getInstance().getRepository();
        String root = Const.getRootPath(user.getOrganizerId());
        RepositoryDirectoryInterface dir = repository.findDirectory(root);

        int res =  countChildren(count,repository,dir);

        repository.disconnect();
        return res;

    }

    private int countChildren(int count, Repository repository, RepositoryDirectoryInterface dir) throws KettleException {
        List<RepositoryElementMetaInterface> elements = repository.getTransformationObjects(dir.getObjectId(), false);
        count = count+elements.size();
        elements = repository.getJobObjects(dir.getObjectId(), false);
        count = count+elements.size();
        List<RepositoryDirectoryInterface> directorys = dir.getChildren();
        for(RepositoryDirectoryInterface child : directorys) {
            count = count + countChildren(count,repository,child);
        }
        return count;
    }
}
