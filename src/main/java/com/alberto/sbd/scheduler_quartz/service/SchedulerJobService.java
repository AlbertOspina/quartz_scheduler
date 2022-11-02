package com.alberto.sbd.scheduler_quartz.service;

import com.alberto.sbd.scheduler_quartz.jobs.CreatorQuartz;
import com.alberto.sbd.scheduler_quartz.model.JobDTO;
import com.alberto.sbd.scheduler_quartz.jobs.PrintDateTime;
import com.alberto.sbd.scheduler_quartz.jobs.SimpleCronJob;
import com.alberto.sbd.scheduler_quartz.repository.SchedulerRepository;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Objects;

@Transactional
@Service
@Slf4j
public class SchedulerJobService {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private SchedulerRepository schedulerRepository;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private CreatorQuartz scheduleCreator;

    private void scheduleNewJob(JobDTO jobInfo) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();

            JobDetail jobDetail = JobBuilder
                    .newJob((Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass()))
                    .withIdentity(jobInfo.getJobName(), jobInfo.getJobGroup()).build();
            if (!scheduler.checkExists(jobDetail.getKey())) {

                jobDetail = scheduleCreator.createJob(
                        (Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass()), false, context,
                        jobInfo.getJobName(), jobInfo.getJobGroup());

                Trigger trigger;
                if (jobInfo.getCronJob()) {
                    trigger = scheduleCreator.createCronTrigger(
                            jobInfo.getJobName(),
                            new Date(),
                            jobInfo.getCronExpression(),
                            SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_EXISTING_COUNT);
                } else {
                    trigger = scheduleCreator.createSimpleTrigger(
                            jobInfo.getJobName(),
                            new Date(),
                            jobInfo.getRepeatTime(),
                            SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_EXISTING_COUNT);
                }
                scheduler.scheduleJob(jobDetail, trigger);
                jobInfo.setJobStatus("SCHEDULED");
                schedulerRepository.save(jobInfo);
                log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " scheduled.");
            } else {
                log.error("scheduleNewJobRequest.jobAlreadyExist");
            }
        } catch (ClassNotFoundException e) {
            log.error("Class Not Found - {}", jobInfo.getJobClass(), e);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void saveOrUpdate(JobDTO scheduleJob) throws Exception {
        JobDTO repo = schedulerRepository.findByJobName(scheduleJob.getJobName());
        String myClass = "com.alberto.sbd.scheduler_quartz.jobs." + scheduleJob.getJobClass();

        if (!Objects.isNull(repo)){
            scheduleJob.setJobId(repo.getJobId());
        }
        if (scheduleJob.getCronExpression().length() > 0) {
            scheduleJob.setJobClass(SimpleCronJob.class.getName());
            scheduleJob.setCronJob(true);
        } else {
            scheduleJob.setJobClass(myClass);
            scheduleJob.setCronJob(false);
            //scheduleJob.setRepeatTime(5000L);
        }
        if (Objects.isNull(scheduleJob.getJobId())) {
            log.info("Job Info: {}", scheduleJob);
            scheduleNewJob(scheduleJob);
        } else {
            updateScheduleJob(scheduleJob);
        }
        scheduleJob.setJobDesc("i am job number " + scheduleJob.getJobId());
        scheduleJob.setInterfaceName("interface_" + scheduleJob.getJobId());
        log.info(">>>>> jobName = [" + scheduleJob.getJobName() + "]" + " created.");
    }

    private void updateScheduleJob(JobDTO jobInfo) {
        Trigger newTrigger;
        if (jobInfo.getCronJob()) {
            newTrigger = scheduleCreator.createCronTrigger(jobInfo.getJobName(), new Date(),
                    jobInfo.getCronExpression(), SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        } else {
            newTrigger = scheduleCreator.createSimpleTrigger(jobInfo.getJobName(), new Date(), jobInfo.getRepeatTime(),
                    SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        }
        try {
            schedulerFactoryBean.getScheduler().rescheduleJob(TriggerKey.triggerKey(jobInfo.getJobName()), newTrigger);
            jobInfo.setJobStatus("EDITED & SCHEDULED");
            schedulerRepository.save(jobInfo);
            log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " updated and scheduled.");
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

    public boolean deleteJob(JobDTO jobInfo) {
        try {
            JobDTO JobRepo = schedulerRepository.findByJobName(jobInfo.getJobName());

            if (schedulerFactoryBean.getScheduler().deleteJob(new JobKey(JobRepo.getJobName(), JobRepo.getJobGroup())))
            {
                schedulerRepository.delete(JobRepo);
                log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " deleted.");
                return true;
            }
            log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " could not be deleted.");
            return false;
        } catch (SchedulerException e) {
            log.error("Failed to delete job - {}", jobInfo.getJobName(), e);
            return false;
        }
    }
}