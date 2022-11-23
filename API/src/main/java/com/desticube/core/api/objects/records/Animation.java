package com.desticube.core.api.objects.records;


import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public record Animation(List<String> text, String id, AtomicInteger line) {
    public Animation(String id, List<String> list) {
        this(list, id, new AtomicInteger(0));
    }

    public String next() {
        if (line.get() >= text.size() - 1) line.set(0);
        else line.incrementAndGet();
        return text.get(line.get());
    }

}
