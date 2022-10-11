package org.vsu.rudakov.configurator;

import lombok.Getter;
import org.reflections.Reflections;

import java.util.Map;

public class JavaBeanConfigurator implements BeanConfigurator {
    @Getter
    private final Reflections scanner;
    private final Map<Class, Class> interfaceToImpl;

    public JavaBeanConfigurator(String packageToScan, Map<Class, Class> interfaceToImpl) {
        scanner = new Reflections(packageToScan);
        this.interfaceToImpl = interfaceToImpl;
    }

    @Override
    public <T> Class<? extends T> getImplementationClass(Class<T> interfaceClass) {
        return interfaceToImpl.computeIfAbsent(interfaceClass, c -> {
            var implClass = scanner.getSubTypesOf(interfaceClass);
            if (implClass.size() != 1) {
                throw new RuntimeException("Interface has 0 or more than 1 implementation");
            }
            return implClass.stream().findAny().get();
        });
    }
}
