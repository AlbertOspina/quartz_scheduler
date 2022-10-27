package com.sbd.quartz_scheduler.jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Random;

@Slf4j
public class ProbabilityJob extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            log.info("Initializing Task Probability with thread: {} , name : {} ",Thread.currentThread().getId(), Thread.currentThread().getName());
            Thread.sleep(1000);
            Random random = new Random();
            int randomWithNextIntWithinARange = random.nextInt(10 - 0) + 0;
            log.info("Random number: {} !!!", randomWithNextIntWithinARange);
            if (randomWithNextIntWithinARange == 9){
                Exception e = new Exception();
                throw e;
            }
            Thread.sleep(3000);
            log.info("Finishing Task Probability");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (Exception e){
            return;
        }

    }
}
