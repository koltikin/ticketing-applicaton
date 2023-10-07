package com.cydeo.enums;

import lombok.Getter;

@Getter
public enum Status {
    OPEN("Open"),IN_PROGRESS("In Progress"), COMPLETE("Completed");

    private final String value;
    Status(String value) {
        this.value = value;
    }
}
