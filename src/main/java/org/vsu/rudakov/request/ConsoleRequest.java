package org.vsu.rudakov.request;

import java.util.ArrayList;

public class ConsoleRequest extends Request {
    @Override
    public void setInput(String input) {
        var parts = parseInput(input);
        mapping = parts[0];
        param = parts[1];
        body = parts.length == 3 ? parts[2] : null;
    }

    private String[] parseInput(String input) {
        var parts = new ArrayList<String>();
        StringBuilder s = new StringBuilder();
        var isFinalPart = false;
        for (var c : input.toCharArray()) {
            if (c == ' ' && !isFinalPart) {
                if (s.toString().equals(" ")) {
                    continue;
                }
                parts.add(s.toString().trim());
                s = new StringBuilder();
            }
            if (c == '[') {
                isFinalPart = true;
            }
            s.append(c);
        }
        parts.add(s.toString().trim());
        return parts.toArray(new String[0]);
    }
}
