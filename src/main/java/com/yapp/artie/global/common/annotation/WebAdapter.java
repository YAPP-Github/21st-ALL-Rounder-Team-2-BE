package com.yapp.artie.global.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@RestController
public @interface WebAdapter {

  @AliasFor(annotation = Controller.class)
  String value() default "";
}
