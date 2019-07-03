package com.sichuan.sichuanproject.mapper;

import com.sichuan.sichuanproject.domain.ScheduleJob;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author
 */

@Mapper
@Repository
public interface ScheduleJobMapper {

    /**
     *新增定时任务
     *
     * @param scheduleJob
     */
    @Insert("insert into schedule_job(id, job_name, job_group, method_name, bean_class, status, cron_expression, params, remark, create_time, modify_time) values(#{id},#{jobName},#{jobGroup},#{methodName},#{beanClass},#{status},#{cronExpression},#{params},#{remark},#{createTime},#{modifyTime})")
    void addJob(ScheduleJob scheduleJob);

    /**
     * 查询所有的定时任务
     *
     * @return List<ScheduleJob>
     */
    @Select("select * from schedule_job")
    List<ScheduleJob> listAllJob();

    /**
     * 更新定时任务状态
     *
     * @param scheduleJob
     */
    @Update("update schedule_job set status = #{status} where id = #{id}")
    void updateJobStatusById(ScheduleJob scheduleJob);

    /**
     * 根据主键查询定时任务
     *
     * @param id
     * @return ScheduleJob
     */
    @Select("select * from schedule_job where id = #{id}")
    ScheduleJob getScheduleJobByPrimaryKey(@Param("id") Long id);

    /**
     * 更新时间表达式
     *
     * @param scheduleJob
     */
    @Update("update schedule_job set cron_expression = #{cronExpression} where id = #{id}")
    void updateJobCronExpressionById(ScheduleJob scheduleJob);

    /**
     * 删除定时任务
     *
     * @param id
     */
    @Delete("delete from schedule_job where id=#{id}")
    void deleteJobById(@Param("id") Long id);
}
