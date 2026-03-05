package com.hss.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBodyInfo {
    private Integer id;
    @JsonIgnore
    private Integer userId;
    // 年龄必须为0以上的数字
    @Min(value = 0, message = "年龄必须大于等于0")
    @Max(value = 120, message = "年龄必须小于等于120")

    private Integer age;
    // 性别只能是男或女或保密
    @Pattern(regexp = "^(男|女|保密)$", message = "性别只能是男或女或保密")
    private String gender;
    @Min(value = 130, message = "身高必须大于等于130cm")
    private BigDecimal height;
    @Min(value = 30, message = "体重必须大于等于30kg")
    private BigDecimal weight;

    private BigDecimal bmi;

    private Integer systolicBp; //收缩压
    private Integer diastolicBp; //舒张压
    private Integer heartRate;  //心率
    private BigDecimal sleepDuration;//睡眠 //BigDecimal的作用是防止精度丢失
    private BigDecimal bloodSugar; //血糖
    private BigDecimal cholesterol; //胆固醇
    private BigDecimal leftVision; //视力左侧
    private BigDecimal rightVision; //视力右侧
    private LocalDateTime createDate;
}
