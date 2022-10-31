package com.alberto.sbd.scheduler_quartz.jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Slf4j
@DisallowConcurrentExecution
public class CountToTen extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            log.info("Initializing Task Count with thread: {} , name : {} ",Thread.currentThread().getId(), Thread.currentThread().getName());
            Integer i = 0;
            for(i=0;i <= 10; i++){
                Thread.sleep(2000);
                log.info("Loop in: {}", i);
            }
            log.info("Finishing Task Count...");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            return;
        }

    }
}
