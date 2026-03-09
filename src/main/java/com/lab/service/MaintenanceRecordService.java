package com.lab.service;

import com.lab.pojo.MaintenanceRecord;

public interface MaintenanceRecordService {

    /**
     * 用户：提交维修申请
     */
    MaintenanceRecord apply(MaintenanceRecord record);
}
