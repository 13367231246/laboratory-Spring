package com.lab.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 维修汇总统计
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceSummary {

    /** 待维护（等待维修） */
    private Long waiting;

    /** 维护中（维修中） */
    private Long repairing;

    /** 已完成 */
    private Long completed;

    /** 不需要维护 */
    private Long noNeed;
}
