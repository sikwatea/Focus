package com.focus.studyhelper;

import java.util.List;

public class PomodoroMethod extends StudyMethod {
	private static final long serialVersionUID = 1L;

    public PomodoroMethod(String name, String description, List<String> steps) {
        super(name, description, steps);
    }

    @Override
    public void initializeTimeline(FocusSession session) {
        long totalSeconds = session.getTotalDurationMinutes() * 60;
        long totalBreakSeconds = session.getBreakCount() * session.getBreakDurationMinutes() * 60;
        long availableStudySeconds = totalSeconds - totalBreakSeconds;
        int chunks = session.getBreakCount() + 1;
        long intervalSeconds = availableStudySeconds / chunks;
        
        if (intervalSeconds <= 0) intervalSeconds = 60; 
        
        session.setStudyIntervalSeconds((int) intervalSeconds);
        session.setRemainingTimeSeconds(intervalSeconds);
        session.setCurrentPhase(FocusSession.SessionPhase.STUDY);
    }

    @Override
    public void handlePhaseChange(FocusSession session) {
        if (session.getCurrentPhase() == FocusSession.SessionPhase.STUDY) {
            // Check if there are stil breaks
            if (session.getCurrentIntervalIndex() < session.getBreakCount()) {
                System.out.println("Switching to BREAK");
                session.setCurrentPhase(FocusSession.SessionPhase.BREAK);
                session.setRemainingTimeSeconds(session.getBreakDurationMinutes() * 60L);
                session.incrementIntervalIndex();
            } else {
                session.completeSession(); // done if no breaks left
            }
        } else if (session.getCurrentPhase() == FocusSession.SessionPhase.BREAK) {
            System.out.println("Switching to STUDY");
            session.setCurrentPhase(FocusSession.SessionPhase.STUDY);
            session.setRemainingTimeSeconds(session.getStudyIntervalSeconds());
        }
    }
}