package com.focus.studyhelper;

import java.util.List;

public class FeynmanMethod extends StudyMethod {

    public FeynmanMethod(String name, String description, List<String> steps) {
        super(name, description, steps);
    }

    @Override
    public void initializeTimeline(FocusSession session) {
        int totalSeconds = session.getTotalDurationMinutes() * 60;
        session.setRemainingTimeSeconds(totalSeconds);
    }

    @Override
    public void handlePhaseChange(FocusSession session) {
        if (session.getCurrentPhase() == FocusSession.SessionPhase.STUDY || 
            session.getCurrentPhase() == FocusSession.SessionPhase.BREAKDOWN) {
            
            if (session.getCurrentIntervalIndex() < session.getBreakCount()) {
                System.out.println("\n*** TIME FOR A BREAK! (" + session.getBreakDurationMinutes() + " mins) ***");
                session.setCurrentPhase(FocusSession.SessionPhase.BREAK);
                session.setRemainingTimeSeconds(session.getBreakDurationMinutes() * 60);
                session.incrementIntervalIndex();
            } else {
                session.completeSession();
            }
        } else if (session.getCurrentPhase() == FocusSession.SessionPhase.BREAK) {
            System.out.println("\n*** BREAK OVER! Prepare to Explain. ***");
            // breakdown phase
            session.setCurrentPhase(FocusSession.SessionPhase.BREAKDOWN);
            System.out.println("Phase: Breakdown/Explanation (Simplifying concept...)");
            session.setRemainingTimeSeconds(session.getStudyIntervalSeconds() > 0 ? session.getStudyIntervalSeconds() : 300);
        }
    }
}