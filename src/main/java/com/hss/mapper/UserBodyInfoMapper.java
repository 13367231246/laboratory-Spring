package com.hss.mapper;

import com.hss.pojo.UserBodyInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserBodyInfoMapper {
    // 根据用户id查询用户身体信息
    @Select("SELECT * FROM user_body_info WHERE user_id = #{id}")
    UserBodyInfo findByUserId(Integer id);

    // 根据用户id更新用户身体信息
    @Update("UPDATE user_body_info SET " +
            "systolic_bp = #{userBodyInfo.systolicBp}, " +
            "diastolic_bp = #{userBodyInfo.diastolicBp}, " +
            "heart_rate = #{userBodyInfo.heartRate}, " +
            "sleep_duration = #{userBodyInfo.sleepDuration}, " +
            "blood_sugar = #{userBodyInfo.bloodSugar}, " +
            "cholesterol = #{userBodyInfo.cholesterol}, " +
            "left_vision = #{userBodyInfo.leftVision}, " +
            "right_vision = #{userBodyInfo.rightVision}, " +
            "create_date = #{userBodyInfo.createDate}, " +
            "gender = #{userBodyInfo.gender}, " +
            "age = #{userBodyInfo.age}, " +
            "height = #{userBodyInfo.height}, " +
            "weight = #{userBodyInfo.weight} " +
            "WHERE user_id = #{UserId}")
    void update(UserBodyInfo userBodyInfo, Integer UserId);

    // 插入用户身体信息
    @Insert("INSERT INTO user_body_info (user_id, systolic_bp, diastolic_bp, heart_rate, " +
            "sleep_duration, blood_sugar, cholesterol, " +
            "left_vision, right_vision, create_date, " +
            "gender, age, height, weight) " +
            "VALUES (#{userId}, #{systolicBp}, #{diastolicBp}, #{heartRate}, " +
            "#{sleepDuration}, #{bloodSugar}, #{cholesterol}, " +
            "#{leftVision}, #{rightVision}, #{createDate}, " +
            "#{gender}, #{age}, #{height}, #{weight})")
    void insert(UserBodyInfo userBodyInfom);
}
