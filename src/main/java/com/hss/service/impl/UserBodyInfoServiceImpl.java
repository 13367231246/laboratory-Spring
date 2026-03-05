package com.hss.service.impl;

import com.hss.mapper.UserBodyInfoMapper;
import com.hss.pojo.UserBodyInfo;
import com.hss.service.UserBodyInfoService;
import com.hss.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class UserBodyInfoServiceImpl implements UserBodyInfoService {
    @Autowired
    private UserBodyInfoMapper userBodyInfoMapper;
    @Override
    public UserBodyInfo getUserBodyInfo() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        return userBodyInfoMapper.findByUserId(id);
    }

    //更新用户身体信息
    @Override
    public void updateUserBodyInfo(UserBodyInfo userBodyInfo) {
        // 先查询用户身体信息是否存在
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer UserId = (Integer) map.get("id");
        UserBodyInfo existingInfo = userBodyInfoMapper.findByUserId(UserId);
        if (existingInfo!= null) {
            userBodyInfo.setCreateDate(LocalDateTime.now());
            userBodyInfoMapper.update(userBodyInfo,UserId);
        } else {
            userBodyInfo.setUserId(UserId);
            userBodyInfo.setCreateDate(LocalDateTime.now());
            userBodyInfoMapper.insert(userBodyInfo);
        }
    }

    @Override
    // 提供健康建议
    public String getHealthAdvice(UserBodyInfo userBodyInfo) {
        StringBuilder advice = new StringBuilder("健康建议：\n");

        // 血压建议
        if (userBodyInfo.getSystolicBp() > 120 || userBodyInfo.getDiastolicBp() > 90) {
            advice.append("- 您的血压偏高：尽量减少盐分的摄入；增加蔬菜和水果的摄入量；定期进行有氧运动，如快步走、游泳或骑自行车。避免过量饮酒和吸烟。\n" +
                    "如果情况没有改善，请咨询医生。\n\n");
        } else if (userBodyInfo.getSystolicBp() < 80 || userBodyInfo.getDiastolicBp() < 60) {
            advice.append("- 您的血压偏低，请确保摄入足够的水分和营养；适量增加盐分摄入（除非有其他健康问题限制）；\n" +
                    "起身或站立时动作要缓慢，以防止晕厥。如果症状持续或加重，请及时就医。\n\n");
        }else {
            // 血压处于正常范围内的建议
            advice.append("- 您的血压目前处于正常范围内，继续保持良好的生活习惯非常重要：\n");
            advice.append("  - 均衡饮食，包括丰富的水果、蔬菜、全谷物以及低脂肪的蛋白质来源。\n");
            advice.append("  - 定期进行中等强度的体育活动，例如每天至少30分钟的步行、慢跑或者骑自行车。\n");
            advice.append("  - 控制体重在健康范围内，避免超重或肥胖。\n");
            advice.append("  - 限制酒精摄入，并且不要吸烟。\n");
            advice.append("  - 管理压力，保持心理健康，充足的睡眠也是维持血压稳定的重要因素。\n");
            advice.append("  - 即使血压正常，也应每年至少测量一次血压，以便早期发现任何潜在的变化。\n\n");
        }

        // 心率建议
        if (userBodyInfo.getHeartRate() > 100) {
            advice.append("- 您的心率偏快，请避免过度劳累；避免过度劳累，保证充足的休息；学会管理压力，" +
                    "尝试冥想或其他放松技巧；减少咖啡因和其他兴奋剂的摄入。如有必要，请咨询医生了解是否需要进一步检查。\n\n");
        } else if (userBodyInfo.getHeartRate() < 60) {
            advice.append("- 您的心率偏慢，可能需要注意以下事项：如果您不是运动员或经常锻炼的人，低心率可能是不正常的；" +
                    "注意是否有头晕、乏力等不适症状。在出现任何不适的情况下，尽快寻求医疗帮助。\n\n");
        } else {
            // 心率处于正常范围内的建议
            advice.append("- 您的心率目前处于正常范围内，这表明您的心脏功能良好。保持以下习惯有助于维持心率稳定：\n");
            advice.append("  - 定期进行中等强度的体育活动，如快步走、游泳或骑自行车，以增强心脏功能。\n");
            advice.append("  - 确保饮食均衡，富含水果、蔬菜、全谷物以及瘦肉蛋白，有助于支持心血管健康。\n");
            advice.append("  - 控制体重，超重会增加心脏负担。\n");
            advice.append("  - 学习有效的压力管理技巧，比如深呼吸、瑜伽或正念冥想。\n");
            advice.append("  - 避免吸烟和过量饮酒，这些习惯会对心脏造成伤害。\n");
            advice.append("  - 定期监测心率，并与医生讨论任何异常变化。\n\n");
        }

        // 血糖建议
        if (userBodyInfo.getBloodSugar().compareTo(new BigDecimal("6.1")) > 0) {
            advice.append("- 您的血糖偏高，应该采取措施来控制它： 控制饮食中的糖分摄入，选择低GI食品；增加体力活动，" +
                    "如散步或做家务；维持健康的体重。定期监测血糖水平，并与医生讨论治疗方案。\n\n");
        }else if (userBodyInfo.getBloodSugar().compareTo(new BigDecimal("5.9")) < 0){
            advice.append("- 您的血糖偏低，请立即采取措施提高血糖水平：摄入15到20克快速吸收的碳水化合物，" +
                    "例如：葡萄糖片或凝胶，果汁或普通汽水，蜂蜜或含糖糖果等。在摄入上述食物后约15分钟重新测量血糖水平。\n\n");
        } else {
            // 血糖处于正常范围内的建议
            advice.append("- 您的血糖目前处于正常范围内，这是预防糖尿病和其他代谢性疾病的关键。为了保持这一状态，您可以考虑以下几点：\n");
            advice.append("  - 均衡饮食，限制简单碳水化合物的摄入，优先选择复合碳水化合物和高纤维食品。\n");
            advice.append("  - 维持规律的身体活动，每天至少30分钟的中等强度运动。\n");
            advice.append("  - 如果您有超重或肥胖的问题，努力减轻体重可以显著改善血糖控制。\n");
            advice.append("  - 定期监测血糖水平，特别是如果您有家族病史或其他风险因素。\n");
            advice.append("  - 保持良好的生活习惯，包括充足的睡眠、戒烟限酒、有效应对压力。\n\n");
        }

        // BMI 建议;
        // 计算 BMI
        // 获取体重和身高
        BigDecimal weight = userBodyInfo.getWeight(); // 体重 (kg)
        BigDecimal height = userBodyInfo.getHeight(); // 身高 (cm)
        // 身高转换为米 (height / 100)
        BigDecimal heightInMeters = height.divide(new BigDecimal("100.0"), 2, RoundingMode.HALF_UP);

        // BMI = weight / (heightInMeters * heightInMeters)
        BigDecimal bmi = weight.divide(heightInMeters.multiply(heightInMeters), 2, RoundingMode.HALF_UP);
        // 将 BigDecimal 转换为 double
        double bmiValue = bmi.doubleValue();
        // 根据 BMI 值给出建议
        if (bmiValue < 18.5) {
            advice.append("- 您的 BMI 偏低，建议多吃蔬菜、水果、肉类、鱼类等富含营养的食物，适当增强体质。\n\n");
        } else if (bmiValue >= 18.5 && bmiValue < 25) {
            advice.append("- 您的 BMI 正常，请继续保持健康的饮食习惯和规律的身体活动。\n\n");
        } else if (bmiValue >= 25 && bmiValue < 28) {
            advice.append("- 您的 BMI 偏高，建议减少高糖、高脂肪食物的摄入，增加蔬菜和水果的比例。\n\n");
        } else {
            advice.append("- 您的 BMI 过高，强烈建议在医生或营养专家的指导下制定减肥计划，包括合理的饮食调整和持续的体育锻炼。\n\n");
        }

        BigDecimal leftVision = userBodyInfo.getLeftVision();
        BigDecimal rightVision = userBodyInfo.getRightVision();
        BigDecimal normalVisionThreshold = new BigDecimal("1.0");
        if (leftVision.compareTo(normalVisionThreshold) < 0 || rightVision.compareTo(normalVisionThreshold) < 0) {
            advice.append("- 您的视力低于正常水平（1.0），建议采取以下措施保护视力：\n" +
                    "1. 遵循“20—20—20”法则：每用眼20分钟，休息20秒，远眺20英尺（约6米）的地方。\n" +
                    "2. 保持正确用眼姿势，避免在光线不足或过强的环境下用眼。3. 减少电子设备使用时间，尤其是手机和电脑。\n" +
                    "4. 增加户外活动时间，每天至少保证2小时户外活动。\n" +
                    "5. 定期检查视力，必要时佩戴合适的眼镜或进行视力矫正。\n\n");
        } else if (leftVision.compareTo(normalVisionThreshold) >= 0 && rightVision.compareTo(normalVisionThreshold) >= 0) {
            advice.append("- 您的视力正常，请继续保持良好的用眼习惯，定期检查视力。\n\n");
        }

        //睡眠
        BigDecimal sleepDuration = userBodyInfo.getSleepDuration();
        if (sleepDuration.compareTo(new BigDecimal("7.0")) < 0) {
            advice.append("- 您的睡眠时长不足（<7小时），建议采取以下措施改善睡眠：1. 保持规律的作息时间，每天固定时间上床和起床。" +
                    "2. 避免在睡前使用电子设备，减少蓝光对睡眠的影响。" +
                    "3. 睡前避免饮用含咖啡因的饮料，如咖啡、茶等。" +
                    "4. 创造一个安静、舒适的睡眠环境，确保卧室温度适宜。" +
                    "5. 如果长期睡眠不足，建议咨询医生或睡眠专家。\n\n");
        } else if (sleepDuration.compareTo(new BigDecimal("9.0")) > 0) {
            advice.append("- 您的睡眠时长过长（>9小时），建议适当减少睡眠时间，保持7-9小时的睡眠时长。\n\n");
        } else {
            advice.append("- 您的睡眠时长正常（7-9小时），请继续保持良好的睡眠习惯。\n");
        }
        return advice.toString();
    }
}
