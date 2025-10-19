package com.focus.studyhelper;

public class StudyMethod {
    private String name;
    private String description;

    public StudyMethod(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return name + ": " + description;
    }
}
