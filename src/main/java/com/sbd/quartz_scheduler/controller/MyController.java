package com.sbd.quartz_scheduler.controller;

import com.sbd.quartz_scheduler.entities.SchedulerJobInfo;
import com.sbd.quartz_scheduler.service.SchedulerJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/quartz_scheduler")
public class MyController {

    @Autowired
    SchedulerJobService schedulerJobService;

    @PostMapping("/create")
    public void createTask(@RequestBody SchedulerJobInfo myJob){
        log.info(String.valueOf(myJob));
        try {
            schedulerJobService.saveOrUpdate(myJob);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/check")
    @ResponseBody
    public String checkStatus(){
        return "OK";
    }

    @GetMapping("/delete/{name}")
    @ResponseBody
    public String deleteTask(@PathVariable String name){
        SchedulerJobInfo myJob = new SchedulerJobInfo();
        myJob.setJobName(name);
        if (schedulerJobService.deleteJob(myJob)){
            log.info("Job deleted!!!");
        }else{
            log.info("Error deleting!!!");
        }
        return "Bye";
    }
}
