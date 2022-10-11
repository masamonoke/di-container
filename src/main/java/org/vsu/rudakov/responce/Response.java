package org.vsu.rudakov.responce;

import org.vsu.rudakov.request.Request;

import java.util.List;

public interface Response {
    Object response(List<?> controllers, Request request);
}
