package com.web.servlet.entity;

import java.util.Arrays;

public class User {
    private String name;
    private String[] hobbies;

    private int op = 0;

    public int getOp() {
        return op;
    }

    public void setOp(int op) {
        this.op = op;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", hobbies=" + Arrays.toString(hobbies) +
                ", op=" + op +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getHobbies() {
        return hobbies;
    }

    public void setHobbies(String[] hobbies) {
        this.hobbies = hobbies;
    }
}
