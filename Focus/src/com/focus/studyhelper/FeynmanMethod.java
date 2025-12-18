package com.focus.studyhelper;

import java.util.List;

public class FeynmanMethod extends StudyMethod {

    public FeynmanMethod(String name, String description, List<String> steps) {
        super(name, description, steps);
    }

    @Override
    public void initializeTimeline(FocusSession session) {
        int totalSeconds = session.getTotalDurationMinutes() * 60;
        
        session.setStudyIntervalSeconds(totalSeconds);      
        session.setRemainingTimeSeconds(totalSeconds);
    }

    @Override
    public void handlePhaseChange(FocusSession session) {
        if (session.getCurrentPhase() == FocusSession.SessionPhase.STUDY) {
            if (session.getBreakCount() > 0 && session.getCurrentIntervalIndex() < session.getBreakCount()) {
                session.setCurrentPhase(FocusSession.SessionPhase.BREAK);
                session.setRemainingTimeSeconds(session.getBreakDurationMinutes() * 60);
                session.incrementIntervalIndex();
            } else {
            	startBreakdown(session);
            }
        } 
        else if (session.getCurrentPhase() == FocusSession.SessionPhase.BREAK) {
            startBreakdown(session);
        }
        else if (session.getCurrentPhase() == FocusSession.SessionPhase.BREAKDOWN) {
            session.completeSession();
        }
    }

    private void startBreakdown(FocusSession session) {
        System.out.println("\n*** BREAK OVER! Prepare to Explain. ***");
        session.setCurrentPhase(FocusSession.SessionPhase.BREAKDOWN);
        
        int breakdownSeconds = session.getBreakdownDurationMinutes() * 60;
        session.setRemainingTimeSeconds(breakdownSeconds);
    }
}