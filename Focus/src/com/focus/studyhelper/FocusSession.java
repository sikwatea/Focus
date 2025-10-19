package com.focus.studyhelper;

public class FocusSession {
	private StudyMethod method;
    private int duration; // in minutes
    private FocusTimer timer;

    public FocusSession(StudyMethod method) {
        this.method = method;
        this.timer = new FocusTimer();
    }

    public void start(int duration) {
        this.duration = duration;
        System.out.println("Starting session using: " + method.getName());
        timer.start(duration);
        System.out.println("Session complete!");
    }

    @Override
    public String toString() {
        return "Method: " + method.getName() + " | Duration: " + duration + " mins";
    }
}