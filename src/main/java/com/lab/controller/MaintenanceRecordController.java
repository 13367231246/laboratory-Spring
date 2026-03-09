package com.lab.controller;

import com.lab.pojo.MaintenanceApplyRequest;
import com.lab.pojo.MaintenanceRecord;
import com.lab.pojo.Result;
import com.lab.service.MaintenanceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/maintenance")
@Validated
public class MaintenanceRecordController {

    @Autowired
    private MaintenanceRecordService maintenanceRecordService;

    /**
     * 用户：提交维修申请（实验室/设备二选一）
     */
    @PostMapping("/apply")
    public Result<MaintenanceRecord> apply(@RequestBody MaintenanceApplyRequest request) {
        MaintenanceRecord record = new MaintenanceRecord();
        record.setLaboratoryId(request.getLaboratoryId());
        record.setEquipmentId(request.getEquipmentId());
        record.setIssueType(request.getIssueType());
        record.setDescription(request.getDescription());
        record.setPhotos(request.getPhotos());

        MaintenanceRecord created = maintenanceRecordService.apply(record);
        return Result.success(created);
    }
}
