package com.sichuan.sichuanproject.service;

import com.sichuan.sichuanproject.domain.ScheduleJob;
import org.springframework.stereotype.Service;

/**
 * @author
 */

@Service
public interface ScheduleJobService {

    /**
     * 新增任务
     *
     * @param scheduleJob
     */
    void addTask(ScheduleJob scheduleJob);


    /**
     * 暂停定时任务
     *
     * @param jobId
     */
    void pauseJob(Long jobId);

    /**
     * 恢复一个定时任务
     *
     * @param jobId
     */
    void resumeJob(Long jobId);

    /**
     * 删除一个定时任务
     *
     * @param jobId
     */
    void removeJob(Long jobId);

    /**
     * 立即执行一个定时任务
     *
     * @param jobId
     */
    void runOnce(Long jobId);

    /**
     * 更新时间表达式
     *
     * @param id
     * @param cronExpression
     */
    void updateCron(Long id, String cronExpression);
}
