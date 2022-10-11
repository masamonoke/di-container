package org.vsu.rudakov.factory;

import lombok.Getter;
import org.vsu.rudakov.annotation.DataAccessObject;
import org.vsu.rudakov.annotation.Inject;
import org.vsu.rudakov.configurator.BeanConfigurator;
import org.vsu.rudakov.configurator.JavaBeanConfigurator;
import org.vsu.rudakov.context.AppContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class BeanFactory {
    @Getter
    private final BeanConfigurator beanConfigurator;
    private final AppContext appContext;

    public BeanFactory(AppContext appContext) {
        beanConfigurator = new JavaBeanConfigurator(appContext.getConfiguration().getPackageToScan(), appContext.getConfiguration().getInterfaceToImpl());
        this.appContext = appContext;
        appContext.getConfiguration().getConfig();
    }

    public <T> T getBean(Class<T> tClass) {
        Class<? extends T> implClass = tClass;
        if (implClass.isInterface() || Modifier.isAbstract(implClass.getModifiers())) {
            implClass = beanConfigurator.getImplementationClass(implClass);
        }
        T bean = null;
        try {
            var constructors = implClass.getDeclaredConstructors();
            Constructor<? extends T> constructor = null;
            for (var c : constructors) {
                if (c.getParameterCount() == 0) {
                    bean = (T) c.newInstance();
                    break;
                }
                constructor = (Constructor<? extends T>) c;
            }
            if (bean == null) {
                if (implClass.isAnnotationPresent(DataAccessObject.class)) {
                    bean = constructor.newInstance(appContext.getConfiguration().getConfig().get("db.url"));
                }
            }

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        for (var field : Arrays.stream(implClass.getDeclaredFields()).filter(f -> f.isAnnotationPresent(Inject.class)).toList()) {
            field.setAccessible(true);
            try {
                field.set(bean, appContext.getBean(field.getType()));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return bean;
    }
}
