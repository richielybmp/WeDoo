package com.espfullstack.wedoo.Interface;

import com.espfullstack.wedoo.pojo.ToDoo;

public interface IToDooAction {
    void onToDooSaved(ToDoo toDoo);
    void onToDooUpdated(ToDoo toDoo, int position);
}