package com.focus.studyhelper;

import java.util.ArrayList;
import java.util.List;

public class FocusSession {
    // session statuses
    public enum SessionStatus { RUNNING, PAUSED, COMPLETED }
    public enum SessionPhase { STUDY, BREAKDOWN, BREAK }

    private String label;
    private StudyMethod method;
    private int totalDurationMinutes;
    private int breakCount;
    private int breakDurationMinutes;
    
    // status tracking
    private SessionStatus status;
    private SessionPhase currentPhase;
    private long remainingTimeSeconds;
    private List<Distraction> distractions = new ArrayList<>();
    
    private int studyIntervalSeconds;
    private int currentIntervalIndex = 0;

    public FocusSession(String label, StudyMethod method, int totalDurationMinutes, int breakCount, int breakDurationMinutes) {
        this.label = label;
        this.method = method;
        this.totalDurationMinutes = totalDurationMinutes;
        this.breakCount = breakCount;
        this.breakDurationMinutes = breakDurationMinutes;
        this.status = SessionStatus.RUNNING;
        this.currentPhase = SessionPhase.STUDY;
        
        initializeTimeline();
    }

    // time splitting logic
    private void initializeTimeline() {
        int totalSeconds = totalDurationMinutes * 60;
        
        if (method.getType() == StudyMethod.MethodType.FEYNMAN) {
            this.remainingTimeSeconds = totalSeconds;
        } else {
            int totalBreakSeconds = (breakCount * breakDurationMinutes) * 60;
            int totalStudySeconds = totalSeconds - totalBreakSeconds;
            int numberOfStudyChunks = breakCount + 1; 
            
            this.studyIntervalSeconds = totalStudySeconds / numberOfStudyChunks;
            this.remainingTimeSeconds = studyIntervalSeconds; 
        }
    }

    public void logDistraction(String reason) {
        Distraction d = new Distraction(reason);
        distractions.add(d);
        System.out.println("Distraction logged: " + reason);
    }

    public void pause() {
        if (status == SessionStatus.RUNNING && currentPhase != SessionPhase.BREAK) {
            status = SessionStatus.PAUSED;
            System.out.println("Session PAUSED.");
        } else {
            System.out.println("Cannot pause right now (Maybe it's a break?).");
        }
    }

    public void resume() {
        if (status == SessionStatus.PAUSED) {
            status = SessionStatus.RUNNING;
            System.out.println("Session RESUMED.");
        }
    }

    
    public boolean tick() {
        if (status != SessionStatus.RUNNING) return true;

        remainingTimeSeconds--;

        if (remainingTimeSeconds <= 0) {
            handlePhaseChange();
        }
        
        return currentPhase != null;
    }

    // feynman phases logic
    private void handlePhaseChange() {
        if (currentPhase == SessionPhase.STUDY || currentPhase == SessionPhase.BREAKDOWN) {
            if (currentIntervalIndex < breakCount) {
                System.out.println("\n*** TIME FOR A BREAK! (" + breakDurationMinutes + " mins) ***");
                currentPhase = SessionPhase.BREAK;
                remainingTimeSeconds = breakDurationMinutes * 60;
                currentIntervalIndex++;
            } else {
                completeSession();
            }
        } else if (currentPhase == SessionPhase.BREAK) {
            System.out.println("\n*** BREAK OVER! Back to Focus. ***");
            if (method.getType() == StudyMethod.MethodType.FEYNMAN) {
                currentPhase = SessionPhase.BREAKDOWN; 
                System.out.println("Phase: Breakdown/Explanation");
            } else {
                currentPhase = SessionPhase.STUDY;
            }
            remainingTimeSeconds = studyIntervalSeconds;
        }
    }
    
    public void completeSession() {
        status = SessionStatus.COMPLETED;
        currentPhase = null; 
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Label: ").append(label)
          .append(" | Method: ").append(method.getName())
          .append(" | Total: ").append(totalDurationMinutes).append("m\n")
          .append("   Distractions: ").append(distractions.size());
        
        for(Distraction d : distractions) {
            sb.append("\n    - ").append(d);
        }
        return sb.toString();
    }
    
    public String getStatusDisplay() {
        long mins = remainingTimeSeconds / 60;
        long secs = remainingTimeSeconds % 60;
        return String.format("[%s] %s - %02d:%02d", status, currentPhase, mins, secs);
    }
}