package com.koen.bfe.essentials;

public class Register {
    public String name;
    public int value;

    public Register(String name) {
        this.name = name;
        this.value = 0;
    }

    public void loadValue(int val) {
        value = val;
    }

    public int getValue() {
        return value;
    }
}
