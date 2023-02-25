package org.vsu.rudakov.responce.console;

import lombok.extern.slf4j.Slf4j;
import org.vsu.rudakov.annotation.mapping.DeleteMapping;
import org.vsu.rudakov.annotation.mapping.PathVariable;
import org.vsu.rudakov.request.Request;
import org.vsu.rudakov.responce.Delete;
import org.vsu.rudakov.utils.RequestUtilities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class DeleteResponse implements Delete {
    @Override
    public Object response(List<?> controllers, Request request) {
        try {
            return delete(controllers, request.getMapping());
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean delete(List<?> controllers, String mapping) throws InvocationTargetException, IllegalAccessException {
        for (var controller : controllers) {
            for (var method : controller.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(DeleteMapping.class)) {
                    var value = method.getAnnotation(DeleteMapping.class).url();
                    var paramsTypes = Arrays.stream(method.getParameters())
                            .filter(p -> p.isAnnotationPresent(PathVariable.class))
                            .map(Parameter::getType)
                            .collect(Collectors.toCollection(ArrayList::new));
                    var m = RequestUtilities.getMapParamToValue(mapping, value);
                    if (m != null) {
                        var objectParams = RequestUtilities.extractParams(m, paramsTypes);
                        return (boolean) method.invoke(controller, objectParams.toArray());
                    }
                }
            }
        }
        return false;
    }
}
