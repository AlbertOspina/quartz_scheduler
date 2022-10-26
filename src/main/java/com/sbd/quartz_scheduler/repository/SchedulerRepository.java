package com.sbd.quartz_scheduler.repository;

import com.sbd.quartz_scheduler.entities.SchedulerJobInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchedulerRepository extends JpaRepository<SchedulerJobInfo, Long> {
    SchedulerJobInfo findByJobName(String jobName);
}
