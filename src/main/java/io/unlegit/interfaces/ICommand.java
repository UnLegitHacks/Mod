package io.unlegit.interfaces;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ICommand
{
    public String name();
    public String shortForm();
    public String exampleUse();
}
