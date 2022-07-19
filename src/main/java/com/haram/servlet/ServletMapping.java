package com.haram.servlet;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServletMapping {
    String value() default "/";
}
