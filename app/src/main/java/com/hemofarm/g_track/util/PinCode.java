package com.hemofarm.g_track.util;

public enum PinCode {
    DEFAULT("1389");

    private final String pin;

    PinCode(String pin) {
        this.pin = pin;
    }

    public String ucitajPin() {
        return pin;
    }
}
