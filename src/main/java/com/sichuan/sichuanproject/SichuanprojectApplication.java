package com.sichuan.sichuanproject;

import com.sichuan.sichuanproject.schedule.MyJobFactory;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
public class SichuanprojectApplication {

    @Autowired
    private MyJobFactory myJobFactory;

    public static ApplicationContext applicationContext;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobFactory(myJobFactory);
        System.out.println("myJobFactory:"+myJobFactory);
        return schedulerFactoryBean;
    }

    @Bean
    public Scheduler scheduler() {
        return schedulerFactoryBean().getScheduler();
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SichuanprojectApplication.class, args);
        SichuanprojectApplication.applicationContext = context;
    }

}
