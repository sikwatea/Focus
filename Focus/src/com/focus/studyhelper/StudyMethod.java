package com.focus.studyhelper;

import java.util.ArrayList;
import java.util.List;

public class StudyMethod {
    public enum MethodType {
        POMODORO, FEYNMAN, ACTIVE_RECALL
    }

    private String name;
    private String description;
    private List<String> steps;
    private MethodType type;

    public StudyMethod(String name, String description, MethodType type, List<String> steps) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.steps = steps;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public MethodType getType() { return type; }
    public List<String> getSteps() { return steps; }

    @Override
    public String toString() {
        return name + " (" + type + ")";
    }
}