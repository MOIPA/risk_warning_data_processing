package com.sichuan.sichuanproject.schedule;

import com.sichuan.sichuanproject.common.utils.TaskUtils;
import com.sichuan.sichuanproject.domain.ScheduleJob;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

/**
 * @author
 */

@Component
public class QuartzJobFactory implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("定时任务运行中...");
        ScheduleJob scheduleJob = (ScheduleJob) jobExecutionContext.getMergedJobDataMap().get("scheduleJob");
        TaskUtils taskUtils = new TaskUtils();
        taskUtils.invokeMethod(scheduleJob);
    }
}
