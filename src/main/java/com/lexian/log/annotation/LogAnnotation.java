package com.lexian.log.annotation;

import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {

    RequestMethod logType() default RequestMethod.GET;
}
