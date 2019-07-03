package com.sichuan.sichuanproject.controller;

import com.sichuan.sichuanproject.common.result.BaseResult;
import com.sichuan.sichuanproject.domain.ScheduleJob;
import com.sichuan.sichuanproject.service.ScheduleJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author
 */

@CrossOrigin
@RestController
public class ScheduleJobController {

    @Autowired
    private ScheduleJobService scheduleJobService;

    @RequestMapping(value = "/risk-warning/schedule-job/add", method = RequestMethod.POST)
    public BaseResult addTask(@RequestBody ScheduleJob scheduleJob) {
        scheduleJobService.addTask(scheduleJob);
        return new BaseResult(1,"success", "新增任务成功");
    }

    @RequestMapping(value = "/risk-warning/schedule-job/pause", method = RequestMethod.POST)
    public BaseResult pauseJob(@RequestParam(value = "jobId") Long jobId) {
        scheduleJobService.pauseJob(jobId);
        return new BaseResult(1,"success","暂停任务成功");
    }

    @RequestMapping(value = "/risk-warning/schedule-job/resume", method = RequestMethod.POST)
    public BaseResult resumeJob(@RequestParam(value = "jobId") Long jobId) {
        scheduleJobService.resumeJob(jobId);
        return new BaseResult(1,"success","恢复任务成功");
    }

    @RequestMapping(value = "/risk-warning/schedule-job/remove", method = RequestMethod.POST)
    public BaseResult removeJob(@RequestParam(value = "jobId") Long jobId) {
        scheduleJobService.removeJob(jobId);
        return new BaseResult(1,"success","删除任务成功");
    }
}
