package org.vsu.rudakov.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Request {
    protected String mapping;
    protected String param;
    protected String body;

    public abstract void setInput(String input);
}
