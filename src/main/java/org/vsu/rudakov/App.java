package org.vsu.rudakov;

import lombok.Getter;
import lombok.Setter;
import org.vsu.rudakov.context.AppContext;
import org.vsu.rudakov.factory.BeanFactory;

import java.util.List;

@Getter
@Setter
public abstract class App {
    protected AppContext appContext;
    protected BeanFactory beanFactory;
    protected List<?> controllers;

    public abstract void run();

    public abstract void prepare();

    public abstract void clean();
}
