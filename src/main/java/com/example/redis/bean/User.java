package com.example.redis.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User extends Id {
    private String name;

    @Override
    public String toString() {
        return "id = " + this.getId() + "\n" + "name = " + this.getName();
    }
}
