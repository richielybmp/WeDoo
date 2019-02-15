package com.espfullstack.wedoo.events;

import com.espfullstack.wedoo.pojo.ToDoo;

public class ToDooSavedEvent {
    private ToDoo toDoo;

    public ToDooSavedEvent(ToDoo toDoo) {
        this.toDoo = toDoo;
    }

    public ToDoo getToDoo() {
        return toDoo;
    }
}
