package com.lab.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 学院表，对应表 college
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class College {

    private Integer id;

    /**
     * 学院名称
     */
    private String collegeName;

    /**
     * 学院代码
     */
    private String collegeCode;

    /**
     * 学院描述
     */
    private String description;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}


