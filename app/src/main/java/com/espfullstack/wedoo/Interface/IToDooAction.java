package com.espfullstack.wedoo.Interface;

import com.espfullstack.wedoo.pojo.ToDoo;

public interface IToDooAction {
    void onToDooInserted(ToDoo toDoo);
    void onToDooUpdated(ToDoo toDoo, int position);
    void onToDooDeleted(ToDoo toDoo);
}