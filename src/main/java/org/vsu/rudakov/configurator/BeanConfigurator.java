package org.vsu.rudakov.configurator;

import org.reflections.Reflections;

public interface BeanConfigurator {
    <T> Class<? extends T> getImplementationClass(Class<T> interfaceClass);
    Reflections getScanner();
}
