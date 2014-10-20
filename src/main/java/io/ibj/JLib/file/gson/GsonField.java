package io.ibj.JLib.file.gson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Joe on 10/1/2014.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GsonField {
    public String value() default "";   //Empty string represents a field that will simply inherit its field name
}
