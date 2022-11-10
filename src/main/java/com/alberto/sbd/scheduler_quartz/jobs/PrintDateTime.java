package com.alberto.sbd.scheduler_quartz.jobs;


import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@DisallowConcurrentExecution
public class PrintDateTime extends QuartzJobBean implements InterruptableJob {

    private AtomicBoolean isInterrupted = new AtomicBoolean(false);
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            log.info("Initializing Task Print {} with thread: {} , name : {} ",context.getJobDetail().toString(), Thread.currentThread().getId(), Thread.currentThread().getName());
            Thread.sleep(5000);
            if(this.isInterrupted.get()){
                log.info("Task Print with thread {} and name : {} is interrupted!",Thread.currentThread().getId(), Thread.currentThread().getName());
                return;
            }
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

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        this.isInterrupted.set(true);
    }
}