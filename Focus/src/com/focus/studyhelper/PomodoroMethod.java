package com.focus.studyhelper;

import java.util.List;

public class PomodoroMethod extends StudyMethod {

    public PomodoroMethod(String name, String description, List<String> steps) {
        super(name, description, steps);
    }

    @Override
    public void initializeTimeline(FocusSession session) {
    	int totalStudySeconds = session.getTotalDurationMinutes() * 60;
    	int chunks = session.getBreakCount() + 1; // break everything to chunks so timer + breaks
        int interval = totalStudySeconds / chunks;
        
        session.setStudyIntervalSeconds(interval);
        session.setRemainingTimeSeconds(interval);
    }

    @Override
    public void handlePhaseChange(FocusSession session) {
        if (session.getCurrentPhase() == FocusSession.SessionPhase.STUDY) {
            if (session.getCurrentIntervalIndex() < session.getBreakCount()) {
                System.out.println("\n*** TIME FOR A BREAK! ***");
                session.setCurrentPhase(FocusSession.SessionPhase.BREAK);
                session.setRemainingTimeSeconds(session.getBreakDurationMinutes() * 60);
                session.incrementIntervalIndex();
            } else {
                session.completeSession();
            }
        } else if (session.getCurrentPhase() == FocusSession.SessionPhase.BREAK) {
            System.out.println("\n*** BREAK OVER! ***");
            session.setCurrentPhase(FocusSession.SessionPhase.STUDY);
            session.setRemainingTimeSeconds(session.getStudyIntervalSeconds());
        }
    }
}