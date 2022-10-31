package com.alberto.sbd.scheduler_quartz.repository;

import com.alberto.sbd.scheduler_quartz.model.JobDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchedulerRepository extends JpaRepository<JobDTO, Long> {
    JobDTO findByJobName(String jobName);
}
