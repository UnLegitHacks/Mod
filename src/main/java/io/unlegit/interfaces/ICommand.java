package io.unlegit.interfaces;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ICommand
{
    String name();
    String shortForm();
    String usage();
}
