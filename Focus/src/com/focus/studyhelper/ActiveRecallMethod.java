package com.focus.studyhelper;

import java.util.List;

public class ActiveRecallMethod extends StudyMethod {

    public ActiveRecallMethod(String name, String description, List<String> steps) {
        super(name, description, steps);
    }

    @Override
    public boolean isConsoleRunnable() {
        return false; 
    }

    @Override
    public void initializeTimeline(FocusSession session) {
    }

    @Override
    public void handlePhaseChange(FocusSession session) {
    }
}