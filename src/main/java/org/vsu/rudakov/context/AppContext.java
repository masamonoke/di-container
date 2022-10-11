package org.vsu.rudakov.context;

import lombok.Getter;
import lombok.Setter;
import org.vsu.rudakov.configuration.Configuration;
import org.vsu.rudakov.configuration.JavaConfiguration;
import org.vsu.rudakov.factory.BeanFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AppContext {
    @Setter
    private BeanFactory beanFactory;
    private final Map<Class, Object> beanMap = new ConcurrentHashMap<>();
    @Getter
    private final Configuration configuration = new JavaConfiguration();

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> tClass) {
        if (beanMap.containsKey(tClass)) {
            return (T) beanMap.get(tClass);
        }
        var bean = beanFactory.getBean(tClass);
        beanMap.put(tClass, bean);
        return bean;
    }
}
