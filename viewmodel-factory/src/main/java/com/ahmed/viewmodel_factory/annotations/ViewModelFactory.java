package com.ahmed.viewmodel_factory.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface ViewModelFactory {
    String factoryName();
    String[] parametersNames() default { };
    String[] parametersTypes() default { };
}
