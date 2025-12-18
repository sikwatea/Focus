package com.focus.studyhelper;

import java.util.ArrayList;
import java.util.List;

public class FocusSession {
	public enum SessionStatus { RUNNING, PAUSED, COMPLETED }
	public enum SessionPhase { STUDY, BREAKDOWN, BREAK }
    
    private String label;
    private StudyMethod method;
    private int totalDurationMinutes;
    private int breakCount;
    private int breakDurationMinutes;
    
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
        
        method.initializeTimeline(this);
    }

    public boolean tick() {
        if (status != SessionStatus.RUNNING) return true;

        remainingTimeSeconds--;

        if (remainingTimeSeconds <= 0) {
            method.handlePhaseChange(this);
        }
        
        return status != SessionStatus.COMPLETED;
    }

    public void logDistraction(String reason) {
        distractions.add(new Distraction(reason));
        System.out.println("Distraction logged.");
    }

    public void pause() {
        if (status == SessionStatus.RUNNING && currentPhase != SessionPhase.BREAK) {
            status = SessionStatus.PAUSED;
            System.out.println("Session PAUSED.");
        } else {
            System.out.println("Cannot pause right now.");
        }
    }

    public void resume() {
        if (status == SessionStatus.PAUSED) {
            status = SessionStatus.RUNNING;
            System.out.println("Session RESUMED.");
        }
    }
    
    public void completeSession() {
        status = SessionStatus.COMPLETED;
        currentPhase = null; 
    }

    public String getStatusDisplay() {
        long mins = remainingTimeSeconds / 60;
        long secs = remainingTimeSeconds % 60;
        return String.format("[%s] %s - %02d:%02d", status, currentPhase != null ? currentPhase : "DONE", mins, secs);
    }

    @Override
    public String toString() {
        return "Label: " + label + " | Method: " + method.getName();
    }

    // all getters and setters
    public int getTotalDurationMinutes() { return totalDurationMinutes; }
    public int getBreakCount() { return breakCount; }
    public int getBreakDurationMinutes() { return breakDurationMinutes; }
    public SessionPhase getCurrentPhase() { return currentPhase; }
    public void setCurrentPhase(SessionPhase phase) { this.currentPhase = phase; }
    public int getStudyIntervalSeconds() { return studyIntervalSeconds; }
    public void setStudyIntervalSeconds(int seconds) { this.studyIntervalSeconds = seconds; }
    public long getRemainingTimeSeconds() { return remainingTimeSeconds; }
    public void setRemainingTimeSeconds(long seconds) { this.remainingTimeSeconds = seconds; }
    public int getCurrentIntervalIndex() { return currentIntervalIndex; }
    public void incrementIntervalIndex() { this.currentIntervalIndex++; }
    public SessionStatus getStatus() { return status; }
}
