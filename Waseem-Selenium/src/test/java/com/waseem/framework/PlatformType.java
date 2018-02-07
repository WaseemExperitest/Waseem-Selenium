package com.waseem.framework;
//
public enum PlatformType {
    NONE("none"),
    IE("internet explorer"),
    FIREFOX("firefox"),
    CHROME("chrome"),
    IOS("ios"),
    ANDROID("android");

    private String value;

    private PlatformType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
