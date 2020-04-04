package com.pramod.login_mvvm;

public class Event<T> {
    private final T content;
    private boolean isHandled = false;

    private Event(T content) {
        this.content = content;
    }


    public static <T> Event<T> init(T content) {
        return new Event<>(content);
    }

    public T getContentIfNotHandled() {
        if (!isHandled) {
            isHandled = true;
            return content;
        }
        return null;
    }

    public T peekContent() {
        return content;
    }
}
