package com.focus.studyhelper;

import java.util.List;

public class FeynmanMethod extends StudyMethod {
	private static final long serialVersionUID = 1L;

    public FeynmanMethod(String name, String description, List<String> steps) {
        super(name, description, steps);
    }

    @Override
    public void initializeTimeline(FocusSession session) {
        long totalSeconds = session.getTotalDurationMinutes() * 60;
        long totalBreakSeconds = session.getBreakCount() * session.getBreakDurationMinutes() * 60;
        long availableWorkSeconds = totalSeconds - totalBreakSeconds;
        
        int chunks = session.getBreakCount() + 1;
        long blockSeconds = availableWorkSeconds / chunks;
        
        // Splits chunks for study and breakdown
        long breakdownSeconds = session.getBreakdownDurationMinutes() * 60;
        long studySeconds = blockSeconds - breakdownSeconds;
        
        if (studySeconds <= 0) studySeconds = 300;
        
        session.setStudyIntervalSeconds((int) studySeconds);
        session.setRemainingTimeSeconds(studySeconds);
        session.setCurrentPhase(FocusSession.SessionPhase.STUDY);
    }

    @Override
    public void handlePhaseChange(FocusSession session) {
        if (session.getCurrentPhase() == FocusSession.SessionPhase.STUDY) {
            // switch phase study to breakdown
            System.out.println("Switching to BREAKDOWN");
            session.setCurrentPhase(FocusSession.SessionPhase.BREAKDOWN);
            session.setRemainingTimeSeconds(session.getBreakdownDurationMinutes() * 60L);
            
        } else if (session.getCurrentPhase() == FocusSession.SessionPhase.BREAKDOWN) {
            // switch phase breakdown to break or finish
            if (session.getCurrentIntervalIndex() < session.getBreakCount()) {
                System.out.println("Switching to BREAK");
                session.setCurrentPhase(FocusSession.SessionPhase.BREAK);
                session.setRemainingTimeSeconds(session.getBreakDurationMinutes() * 60L);
                session.incrementIntervalIndex();
            } else {
                session.completeSession();
            }
            
        } else if (session.getCurrentPhase() == FocusSession.SessionPhase.BREAK) {
            // switch phase break to study
            System.out.println("Switching to STUDY");
            session.setCurrentPhase(FocusSession.SessionPhase.STUDY);
            session.setRemainingTimeSeconds(session.getStudyIntervalSeconds());
        }
    }
}