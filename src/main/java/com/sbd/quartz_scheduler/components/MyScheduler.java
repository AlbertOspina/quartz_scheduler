package com.sbd.quartz_scheduler.components;

import com.sbd.quartz_scheduler.entities.SchedulerJobInfo;
import com.sbd.quartz_scheduler.repository.SchedulerRepository;
import com.sbd.quartz_scheduler.service.SchedulerJobService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

//@Component
@AllArgsConstructor
@Slf4j
public class MyScheduler {

    private final SchedulerJobService schedulerJobService;
    private final SchedulerRepository schedulerRepository;

    //@PostConstruct
    public void testScheduler(){
        SchedulerJobInfo myJob = new SchedulerJobInfo();
        myJob.setJobName("PrintJob1");
        myJob.setJobGroup("Test1");
        myJob.setCronExpression("");
        myJob.setRepeatTime(30000L);
        myJob.setJobClass("PrintDateTime");

        SchedulerJobInfo repo = schedulerRepository.findByJobName(myJob.getJobName());

        if (!Objects.isNull(repo)){
            myJob.setJobId(repo.getJobId());
        }


        try {
            schedulerJobService.saveOrUpdate(myJob);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
