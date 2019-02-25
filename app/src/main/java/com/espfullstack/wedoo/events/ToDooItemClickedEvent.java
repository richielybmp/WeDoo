package com.espfullstack.wedoo.events;

import com.espfullstack.wedoo.pojo.ToDooItem;

public class ToDooItemClickedEvent {
    private ToDooItem toDooItem;
    private int position;

    public ToDooItemClickedEvent(ToDooItem toDooItem, int position) {
        this.toDooItem = toDooItem;
        this.position = position;
    }

    public ToDooItem getToDooItem() {
        return toDooItem;
    }

    public int getPosition() {
        return position;
    }
}
