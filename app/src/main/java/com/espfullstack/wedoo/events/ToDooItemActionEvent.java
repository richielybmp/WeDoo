package com.espfullstack.wedoo.events;

import com.espfullstack.wedoo.pojo.ToDooItem;

public class ToDooItemActionEvent {
    public enum ToDooItemAction {
        SAVED,
        UPDATED,
        DELETED,
        STATUS_UPDATED,
    }
    private ToDooItem toDooItem;
    private ToDooItemAction action;

    public ToDooItemActionEvent(ToDooItem toDooItem, ToDooItemAction action) {
        this.toDooItem = toDooItem;
        this.action = action;
    }

    public ToDooItem getToDooItem() {
        return toDooItem;
    }

    public ToDooItemAction getAction() {
        return action;
    }
}
