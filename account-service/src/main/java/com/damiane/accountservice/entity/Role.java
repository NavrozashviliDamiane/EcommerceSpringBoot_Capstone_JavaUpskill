package com.damiane.accountservice.entity;

public enum Role {
    ADMIN,
    CUSTOMER;

    public String getName() {
        return this.name();
    }
}
