package com.focus.core;

import java.util.Scanner;

public class StudySession {
    public void startSession() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter study duration (in minutes): ");
        int minutes = scanner.nextInt();
        System.out.println("Starting focus timer for " + minutes + " minutes...");
        
        //test
    }
}
