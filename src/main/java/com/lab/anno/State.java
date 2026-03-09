package com.lab.anno;

import com.lab.validation.StateValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented //元注解
//也是元注解，用来标注这个state可以用在哪些地方 是类上，属性上，方法上...
@Target({ FIELD })
//元注解，用来标识state注解在哪个阶段会被保留，编译？源码？运行阶段？
@Retention(RUNTIME)//运行阶段
@Constraint(validatedBy = {StateValidation.class})//指定将来谁给注解提供校验规则
public @interface State {
    //提供校验失败后的提示信息
    String message() default "state参数的值只能是已发布或草稿";
    //指定分组
    Class<?>[] groups() default { };
    //负载 获取到state注解的附加信息
    Class<? extends Payload>[] payload() default { };
}
