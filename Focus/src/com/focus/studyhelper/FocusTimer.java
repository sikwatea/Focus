package com.focus.studyhelper;

public class FocusTimer {

    public void start(int minutes) {
        System.out.println("Focus timer started for " + minutes + " minute(s)...");
        try {
            Thread.sleep(1000); // Simulate 1-second delay (replace with real logic if needed)
        } catch (InterruptedException e) {
            System.out.println("Timer interrupted.");
        }
        System.out.println("Time's up!");
    }
}