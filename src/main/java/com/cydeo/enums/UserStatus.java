package com.cydeo.enums;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserStatus {
    TRUE(true,"Enabled"),FALSE(false,"Disabled");
    private final Boolean value;
    private final String description;
}
