package com.alberto.sbd.scheduler_quartz.jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@DisallowConcurrentExecution
public class CountToTen extends QuartzJobBean implements InterruptableJob {

    private AtomicBoolean isInterrupted = new AtomicBoolean(false);

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            log.info("Initializing Task Count {} with thread: {} , name : {} ",context.getJobDetail().toString() ,Thread.currentThread().getId(), Thread.currentThread().getName());
            Integer i = 0;
            for(i=0;i <= 10; i++){
                if(this.isInterrupted.get()){
                    log.info("Task Count with thread {} and name : {} is interrupted!",Thread.currentThread().getId(), Thread.currentThread().getName());
                    return;
                }
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

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        this.isInterrupted.set(true);
    }
}
