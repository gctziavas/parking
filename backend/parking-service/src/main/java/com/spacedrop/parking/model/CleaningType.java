package com.spacedrop.parking.model;

public enum CleaningType {
    INSIDE("Inside cleaning"),
    OUTSIDE("Outside cleaning"),
    INSIDE_AND_OUTSIDE("Inside + Outside cleaning");

    private final String displayName;

    CleaningType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
