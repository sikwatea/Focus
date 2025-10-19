package com.focus.studyhelper;

public class FocusTimer {

    public void start(int minutes) {
        System.out.println("Focus timer started for " + minutes + " minutes");
        try {
            Thread.sleep(1000); // timer logic here
        } catch (InterruptedException e) {
            System.out.println("Timer interrupted.");
        }
        System.out.println("Time's up!");
    }
}