package org.vsu.rudakov.responce.console;

import org.vsu.rudakov.annotation.mapping.GetMapping;
import org.vsu.rudakov.annotation.mapping.PathVariable;
import org.vsu.rudakov.request.Request;
import org.vsu.rudakov.responce.Get;
import org.vsu.rudakov.utils.RequestUtilities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public class GetResponse implements Get {
    Map<Object, Set<Method>> methodToControllerMap;

    public GetResponse() {
        methodToControllerMap = new HashMap<>();
    }

    public Object response(List<?> controllers, Request request) {
        try {
            return get(controllers, request.getMapping());
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Object get(List<?> controllers, String mapping)
            throws InvocationTargetException, IllegalAccessException {
        if (methodToControllerMap.isEmpty()) {
            for (var controller : controllers) {
                var set = new HashSet<Method>();
                for (var method : controller.getClass().getDeclaredMethods()) {
                    if (method.isAnnotationPresent(GetMapping.class)) {
                        set.add(method);
                    }
                }
                methodToControllerMap.put(controller, set);
            }
        }

        for (var controller : controllers) {
            for (var method : methodToControllerMap.get(controller)) {
                if (method.getAnnotation(GetMapping.class).url().equals(mapping)) {
                    return method.invoke(controller);
                }
            }
            for (var method : methodToControllerMap.get(controller)) {
                var value = method.getAnnotation(GetMapping.class).url();
                var paramsTypes = Arrays.stream(method.getParameters())
                        .filter(p -> p.isAnnotationPresent(PathVariable.class))
                        .map(Parameter::getType)
                        .collect(Collectors.toCollection(ArrayList::new));
                var m = RequestUtilities.getMapParamToValue(mapping, value);
                if (m != null) {
                    var objectParams = RequestUtilities.extractParams(m, paramsTypes);
                    return method.invoke(controller, objectParams.toArray());
                }
            }
        }
        return null;
    }
}
