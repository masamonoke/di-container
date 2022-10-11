package org.vsu.rudakov.responce.file;

import org.vsu.rudakov.annotation.mapping.UpdateMapping;
import org.vsu.rudakov.request.Request;
import org.vsu.rudakov.responce.Update;
import org.vsu.rudakov.utils.RequestUtilities;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


public class UpdateResponse implements Update {

    @Override
    public Object response(List<?> controllers, Request request) {
        try {
            return update(controllers, request.getMapping(), request.getBody());
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static Object update(List<?> controllers, String request, String body)
            throws InvocationTargetException, IllegalAccessException {
        if (body == null) {
            throw new RuntimeException();
        }
        body = body.substring(1, body.length() - 1);
        var stringEntities = RequestUtilities.splitEntities(body);
        for (var controller : controllers) {
            for (var method : controller.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(UpdateMapping.class) &&
                        method.getAnnotation(UpdateMapping.class).url().equals(request)) {
                    return RequestUtilities.invoke(method, controller, stringEntities);
                }
            }
        }
        return null;
    }
}
