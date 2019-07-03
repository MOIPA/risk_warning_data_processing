package com.sichuan.sichuanproject.service.impl;


import com.sichuan.sichuanproject.common.result.Constant;
import com.sichuan.sichuanproject.domain.ScheduleJob;
import com.sichuan.sichuanproject.mapper.ScheduleJobMapper;
import com.sichuan.sichuanproject.schedule.QuartzJobFactory;
import com.sichuan.sichuanproject.service.ScheduleJobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author
 */

@Component
@Slf4j
public class ScheduleJobServiceImpl implements ScheduleJobService {

    @Autowired
    private ScheduleJobMapper scheduleJobMapper;

    @Resource
    private Scheduler scheduler;

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }


    /**
     * 初始化方法
     * 在系统部署时执行一次
     * 执行数据库中持久化的任务
     */
    @PostConstruct
    public void init() {
        List<ScheduleJob> scheduleJobList = scheduleJobMapper.listAllJob();
        if (scheduleJobList.size() != 0) {
            for (ScheduleJob scheduleJob : scheduleJobList) {
                addJob(scheduleJob);
            }
        }
    }

    @Override
    public void addTask(ScheduleJob scheduleJob) {
        ScheduleJob job = getScheduleJobByPrimaryKey(scheduleJob.getId());
        if (job == null) {
            //新建任务
            addJob(scheduleJob);
            scheduleJobMapper.addJob(scheduleJob);
        }else {
            //重启任务
            resumeJob(scheduleJob.getId());
        }
    }

    /**
     * 新增任务
     *
     * @param scheduleJob
     */
    private void addJob(ScheduleJob scheduleJob) {
        try {
            log.info("初始化");
            TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

            //不存在，则创建
            if (null == trigger) {
                Class clazz = QuartzJobFactory.class;
                JobDetail jobDetail = JobBuilder.
                        newJob(clazz).
                        withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup()).
                        build();
                jobDetail.getJobDataMap().put("scheduleJob", scheduleJob);

                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());

                //withIdentity中写jobName和groupName
                trigger = TriggerBuilder.
                        newTrigger().
                        withIdentity(scheduleJob.getJobName(), scheduleJob.getJobGroup())
                        .withSchedule(scheduleBuilder)
                        .build();
                scheduler.scheduleJob(jobDetail, trigger);
                //如果定时任务是暂停状态
                if(scheduleJob.getStatus() == Constant.STATUS_NOT_RUNNING){
                    pauseJob(scheduleJob.getId());
                }
            } else {
                // Trigger已存在，那么更新相应的定时设置
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());

                // 按新的cronExpression表达式重新构建trigger
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

                // 按新的trigger重新设置job执行
                scheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (Exception e) {
            log.error("添加任务失败", e);
        }
    }

    /**
     * 暂停任务
     *
     * @param jobId
     */
    @Override
    public void pauseJob(Long jobId) {
        ScheduleJob scheduleJob = getScheduleJobByPrimaryKey(jobId);
        scheduleJob.setId(jobId);
        scheduleJob.setStatus(Constant.STATUS_NOT_RUNNING);
        try {
            //暂停一个job
            JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
            scheduler.pauseJob(jobKey);
        }catch (Exception e){
            log.error("CatchException:暂停任务失败",e);
        }
        updateJobStatusById(scheduleJob);
    }

    /**
     * 恢复一个已暂停的任务
     *
     * @param jobId
     */
    @Override
    public void resumeJob(Long jobId) {
        ScheduleJob scheduleJob = getScheduleJobByPrimaryKey(jobId);
        scheduleJob.setStatus(Constant.STATUS_RUNNING);
        try{
            //恢复一个定时任务
            JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
            scheduler.resumeJob(jobKey);
        }catch (Exception e){
            log.error("CatchException:恢复定时任务失败",e);
        }
        updateJobStatusById(scheduleJob);
    }

    @Override
    public void removeJob(Long jobId) {
        ScheduleJob scheduleJob = getScheduleJobByPrimaryKey(jobId);

        if (scheduleJob == null) {
            return;
        }

        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
            JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(jobKey);
            scheduleJobMapper.deleteJobById(jobId);
        }catch (Exception e) {
            log.error("CatchException:删除定时任务失败",e);
        }
    }

    /**
     * 立即执行任务
     *
     * @param jobId
     */
    @Override
    public void runOnce(Long jobId) {
        try{
            ScheduleJob scheduleJob = getScheduleJobByPrimaryKey(jobId);
            JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
            scheduler.triggerJob(jobKey);
        }catch (Exception e){
            log.error("CatchException:恢复定时任务失败",e);
        }

    }

    /**
     * 更新时间表达式
     *
     * @param id
     * @param cronExpression
     */
    @Override
    public void updateCron(Long id, String cronExpression) {
        ScheduleJob scheduleJob = getScheduleJobByPrimaryKey(id);
        scheduleJob.setCronExpression(cronExpression);
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            scheduler.rescheduleJob(triggerKey,trigger);
        }catch(Exception e){
            log.error("CatchException:更新时间表达式失败",e);
        }
        updateJobCronExpressionById(scheduleJob);

    }

    /**
     * 修改定时任务状态
     *
     * @param scheduleJob
     */
    private void updateJobStatusById(ScheduleJob scheduleJob){
        scheduleJobMapper.updateJobStatusById(scheduleJob);
    }

    /**
     * 修改定时任务时间
     *
     * @param scheduleJob
     */
    private void updateJobCronExpressionById(ScheduleJob scheduleJob){
        scheduleJobMapper.updateJobCronExpressionById(scheduleJob);
    }

    /**
     * 根据id查找定时任务
     *
     * @param id
     * @return
     */
    private ScheduleJob getScheduleJobByPrimaryKey(Long id){
        return scheduleJobMapper.getScheduleJobByPrimaryKey(id);
    }


}
