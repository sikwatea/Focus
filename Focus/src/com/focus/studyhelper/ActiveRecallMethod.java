package com.focus.studyhelper;

import java.util.List;

public class ActiveRecallMethod extends StudyMethod {

    public ActiveRecallMethod(String name, String description, List<String> steps) {
        super(name, description, steps);
    }

    @Override
    public void initializeTimeline(FocusSession session) {
        int totalSeconds = session.getTotalDurationMinutes() * 60;
        session.setRemainingTimeSeconds(totalSeconds);
    }

    @Override
    public void handlePhaseChange(FocusSession session) {
        session.completeSession();
    }
}