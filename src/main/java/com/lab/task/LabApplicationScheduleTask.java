package com.lab.task;

import com.lab.service.LabApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 实验室申请定时任务：超过结束时间+3小时仍未完成的“使用中”申请自动置为“已完成”
 */
@Component
public class LabApplicationScheduleTask {

    @Autowired
    private LabApplicationService labApplicationService;

    @Scheduled(cron = "0 0 * * * ?")
    public void autoCompleteOverdueApplications() {
        labApplicationService.autoCompleteOverdue();
    }
}
