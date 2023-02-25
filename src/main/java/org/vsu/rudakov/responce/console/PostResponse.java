package org.vsu.rudakov.responce.file;

import lombok.extern.slf4j.Slf4j;
import org.vsu.rudakov.annotation.mapping.PostMapping;
import org.vsu.rudakov.request.Request;
import org.vsu.rudakov.responce.Post;
import org.vsu.rudakov.utils.RequestUtilities;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Slf4j
public class PostResponse implements Post {
    @Override
    public Object response(List<?> controllers, Request request) {
        try {
            return post(controllers, request.getMapping(), request.getBody());
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Object post(List<?> controllers, String mapping, String body) throws InvocationTargetException,
            IllegalAccessException {
        if (body == null) {
            throw new RuntimeException();
        }
        body = body.substring(1, body.length() - 1);
        var stringEntities = RequestUtilities.splitEntities(body);
        for (var controller : controllers) {
            for (var method : controller.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(PostMapping.class) &&
                        method.getAnnotation(PostMapping.class).url().equals(mapping)) {
                    return RequestUtilities.invoke(method, controller, stringEntities);
                }
            }
        }
        return null;
    }
}
