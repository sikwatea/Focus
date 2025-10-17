package com.focus.main;

import com.focus.core.StudySession;

public class FocusApp {
    public static void main(String[] args) {
        System.out.println("Welcome to F.ocus — Your Study Companion!");
        System.out.println("-------------------------------------------");

        StudySession session = new StudySession();
        session.startSession();
    }
}