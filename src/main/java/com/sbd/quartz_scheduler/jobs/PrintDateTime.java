package com.sbd.quartz_scheduler.jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDateTime;

@Slf4j
public class PrintDateTime extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            log.info("Initializing Task Print with thread: {} , name : {} ",Thread.currentThread().getId(), Thread.currentThread().getName());
            Thread.sleep(5000);
            log.info("Hi Alberto, today is {}", LocalDateTime.now());
            Thread.sleep(5000);
            log.info("Finishing Task Print...");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            return;
        }
    }
}
