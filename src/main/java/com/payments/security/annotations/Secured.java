package com.payments.security.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={METHOD,TYPE})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface Secured {
    String[] permissions() default {};
}

