package com.focus.studyhelper;

import java.util.List;

public abstract class StudyMethod {
	protected String name;
    protected String description;
    protected List<String> steps;

    public StudyMethod(String name, String description, List<String> steps) {
        this.name = name;
        this.description = description;
        this.steps = steps;
    }

	public String getName() { return name; }
    public String getDescription() { return description; }
    public List<String> getSteps() { return steps; }

    public abstract void initializeTimeline(FocusSession session);
    public abstract void handlePhaseChange(FocusSession session);
    
    public boolean isConsoleRunnable() {
        return true; 
    }

    @Override
    public String toString() {
        return name;
    }
}
