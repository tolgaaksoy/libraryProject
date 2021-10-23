package com.troia.libraryproject.model;

public enum Language {
    TURKISH("TURKISH", "Türkçe"),
    ENGLISH("ENGLISH", "İngilizce");

    private final String value;
    private final String turkishValue;

    Language(String value, String turkishValue) {
        this.value = value;
        this.turkishValue = turkishValue;
    }
}
