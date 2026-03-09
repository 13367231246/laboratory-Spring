package com.lab.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 实验室内的设备项（用于根据实验室id查设备接口）
 * equipmentList JSON 可能含 model、manufacturer、availableQuantity 等字段，此处忽略
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LabEquipmentItem {

    /** 设备ID */
    private Integer id;
    /** 设备名称（JSON 字段为 equipmentName） */
    @JsonProperty("equipmentName")
    private String name;
    /** 该实验室内数量 */
    private Integer count;
}
