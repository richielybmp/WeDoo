package com.espfullstack.wedoo.events;

import com.espfullstack.wedoo.pojo.ToDoo;

public class ToDooItemClickedEvent {
    private final ToDoo toDoo;
    private int position;

    public ToDooItemClickedEvent(ToDoo toDoo, int position) {
        this.toDoo = toDoo;
        this.position = position;
    }

    public ToDoo getToDoo() {
        return toDoo;
    }

    public int getPosition() {
        return position;
    }
}
