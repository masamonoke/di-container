package org.vsu.rudakov.utils;

import org.vsu.rudakov.annotation.mapping.RequestBody;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RequestUtilities {
    public static Map<String, String> getMapParamToValue(String request, String mapping) {
        var paramName = new StringBuilder();
        var paramValue = new StringBuilder();
        var map = new LinkedHashMap<String, String>();
        var isData = false;
        StringBuilder test1 = new StringBuilder();
        StringBuilder test2 = new StringBuilder();
        var idx = 0;
        for (int i = 0; i < mapping.length(); i++) {
            if (request.charAt(idx) != mapping.charAt(i)) {
                while (idx != request.length() && request.charAt(idx) != '/') {
                    paramValue.append(request.charAt(idx));
                    idx++;
                }
                while (i != mapping.length() && mapping.charAt(i) != '/') {
                    paramName.append(mapping.charAt(i++));
                }
                isData = true;
            }
            if (isData) {
                if (!(paramName.toString().startsWith("{") && paramName.toString().endsWith("}"))) {
                    return null;
                }
                map.put(paramName.substring(1, paramName.length() - 1), paramValue.toString());
                paramName.setLength(0);
                paramValue.setLength(0);
                isData = false;
            }
            if (idx < request.length()) {
                test1.append(request.charAt(idx));
            }
            if (i < mapping.length()) {
                test2.append(mapping.charAt(i));
            }
            idx++;
        }
        if (idx < request.length()) {
            while (idx < request.length()) {
                test1.append(request.charAt(idx));
                idx++;
            }
        }
        if (!test1.toString().equals(test2.toString())) {
            return null;
        }
        return map;
    }

    public static Map<String, String> splitEntities(String body) {
        var isOpened = false;
        var isClosed = false;
        var stringBuilder = new StringBuilder();
        var map = new LinkedHashMap<String, String>();
        for (var c : body.toCharArray()) {
            if (c == '{') {
                isOpened = true;
            }
            if (c == '}') {
                isClosed = true;
            }
            if (c == ' ') {
                if (isOpened && isClosed) {
                    String[] parts = stringBuilder.toString().split("=");
                    parts[1] = parts[1].substring(0, parts[1].length() - 1);
                    map.put(parts[0], parts[1]);
                    isOpened = false;
                    isClosed = false;
                    stringBuilder.setLength(0);
                    continue;
                }
            }
            stringBuilder.append(c);
        }
        if (isOpened && isClosed) {
            String[] parts = stringBuilder.toString().split("=");
            map.put(parts[0], parts[1]);
        }
        return map;
    }

    public static Object invoke(Method method, Object controller, Map<String, String> stringEntities) throws InvocationTargetException, IllegalAccessException {
        var parameters = new ArrayList<>();
        for (var parameter : method.getParameters()) {
            if (parameter.isAnnotationPresent(RequestBody.class)) {
                for (var key : stringEntities.keySet()) {
                    if (key.equals(parameter.getAnnotation(RequestBody.class).name())) {
                        var parser = new Parser(parameter.getType());
                        var obj = parser.parse(stringEntities.get(key));
                        parameters.add(obj);
                    }
                }
            }
            if (method.getParameterCount() == 1) {
                return method.invoke(controller, parameters.get(0));
            } else {
                return method.invoke(controller, parameters);
            }
        }
        return null;
    }

    public static List<Object> extractParams(Map<String, String> m, ArrayList<? extends Class<?>> paramsTypes) {
        var keys = m.keySet().toArray();
        var objectParams = new ArrayList<>();
        var mapper = new Mapper();
        var idx = 0;
        for (var type : paramsTypes) {
            objectParams.add(mapper.map(type.getTypeName(), m.get(keys[idx])));
        }
        return objectParams;
    }
}
