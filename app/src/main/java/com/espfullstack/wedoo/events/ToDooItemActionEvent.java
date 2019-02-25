package com.espfullstack.wedoo.events;

import com.espfullstack.wedoo.pojo.ToDooItem;

public class ToDooItemSavedEvent {
    private ToDooItem toDooItem;

    public ToDooItemSavedEvent(ToDooItem toDooItem) {
        this.toDooItem = toDooItem;
    }

    public ToDooItem getToDooItem() {
        return toDooItem;
    }
}
