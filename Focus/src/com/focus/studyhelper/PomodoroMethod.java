package com.focus.studyhelper;

import java.util.List;

public class PomodoroMethod extends StudyMethod {

    public PomodoroMethod(String name, String description, List<String> steps) {
        super(name, description, steps);
    }

    @Override
    public void initializeTimeline(FocusSession session) {
        int totalSeconds = session.getTotalDurationMinutes() * 60;
        int totalBreakSeconds = (session.getBreakCount() * session.getBreakDurationMinutes()) * 60;
        int totalStudySeconds = totalSeconds - totalBreakSeconds;
        
        int chunks = session.getBreakCount() + 1;
        int interval = totalStudySeconds / chunks;

        session.setStudyIntervalSeconds(interval);
        session.setRemainingTimeSeconds(interval);
    }

    @Override
    public void handlePhaseChange(FocusSession session) {
        if (session.getCurrentPhase() == FocusSession.SessionPhase.STUDY) {
            if (session.getCurrentIntervalIndex() < session.getBreakCount()) {
                System.out.println("\n*** TIME FOR A BREAK! (" + session.getBreakDurationMinutes() + " mins) ***");
                session.setCurrentPhase(FocusSession.SessionPhase.BREAK);
                session.setRemainingTimeSeconds(session.getBreakDurationMinutes() * 60);
                session.incrementIntervalIndex();
            } else {
                session.completeSession();
            }
        } else if (session.getCurrentPhase() == FocusSession.SessionPhase.BREAK) {
            System.out.println("\n*** BREAK OVER! Back to Focus. ***");
            session.setCurrentPhase(FocusSession.SessionPhase.STUDY);
            session.setRemainingTimeSeconds(session.getStudyIntervalSeconds());
        }
    }
}