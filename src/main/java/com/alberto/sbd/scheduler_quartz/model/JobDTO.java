package com.alberto.sbd.scheduler_quartz.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "scheduler_job_info")
public class JobDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;
    private String jobName;
    private String jobGroup;
    private String jobStatus; //Setted by service
    private String jobClass;
    private String cronExpression;
    private String jobDesc; //Setted by service
    private String interfaceName; //Setted by service
    private Long repeatTime;
    private Boolean cronJob; //Setted by service
}


/*
{
        "jobName": "Print0842",
        "jobGroup": "Test4",
        "jobClass": "PrintDateTime",
        "repeatTime": 17000 }*/