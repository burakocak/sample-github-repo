package com.burakocak.githubrepo.model;

public class EventBusObject {
    private int key;
    private Object object;

    public EventBusObject(int key, Object object) {
        this.key = key;
        this.object = object;
    }

    public EventBusObject(int key) {
        this(key, null);
    }

    public int getKey() {
        return key;
    }

    public Object getObject() {
        return object;
    }
}
