package com.sichuan.sichuanproject.common.utils;

import com.sichuan.sichuanproject.SichuanprojectApplication;
import com.sichuan.sichuanproject.domain.ScheduleJob;
import com.sichuan.sichuanproject.mapper.PostMapper;
import com.sichuan.sichuanproject.schedule.TaskTest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author
 */

@Slf4j
public class TaskUtils {


    public void invokeMethod(ScheduleJob scheduleJob) {
        Object object = null;
        Class clazz = null;
        if (StringUtils.isNotBlank(scheduleJob.getBeanClass())) {
            try {
                clazz = Class.forName(scheduleJob.getBeanClass());
                object = clazz.newInstance();
            } catch (Exception e) {
                log.error("CatchException:",e);
            }
        }
        if (object == null) {
            log.error("任务名称 = [" + scheduleJob.getJobName() + "]---------------未启动成功，请检查是否配置正确！！！");
            System.out.println("任务名称 = [" + scheduleJob.getJobName() + "]---------------未启动成功，请检查是否配置正确！！！");
            return;
        }

        Method method = null;
        try {
            ApplicationContext context = SichuanprojectApplication.applicationContext;
            object = context.getBean(clazz);
            context.getAutowireCapableBeanFactory().autowireBean(object);

            method = clazz.getDeclaredMethod(scheduleJob.getMethodName(), String.class, String.class, Long.class);
            method.setAccessible(true);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("任务名称 = [" + scheduleJob.getJobName() + "]---------------未启动成功，方法名设置错误！！！");
            System.out.println("任务名称 = [" + scheduleJob.getJobName() + "]---------------未启动成功，方法名设置错误！！！");
        }
        if (method != null) {
            try {
                method.invoke(object, "post_"+scheduleJob.getId(), "comment_"+scheduleJob.getId(), scheduleJob.getId());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        log.info("任务名称 = [" + scheduleJob.getJobName() + "]----------启动成功");
    }
}
