package com.espfullstack.wedoo.events;

import com.espfullstack.wedoo.pojo.ToDoo;

public class ToDooHasChangedEvent {
    private ToDoo toDoo;

    public ToDooHasChangedEvent(ToDoo toDoo) {
        this.toDoo = toDoo;
    }

    public ToDoo getToDoo() {
        return toDoo;
    }
}
