package com.focus.studyhelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class StudyMethodManager {
    private List<StudyMethod> methods = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);

    public StudyMethodManager() {
        methods.add(new PomodoroMethod(
            "Pomodoro", 
            "Standard interval training", 
            Arrays.asList("1. Set timer", "2. Work", "3. Break")
        ));

        methods.add(new FeynmanMethod(
            "Feynman Technique", 
            "Deep understanding through simplification", 
            Arrays.asList("1. Study", "2. Explain", "3. Refine")
        ));

        methods.add(new ActiveRecallMethod(
            "Active Recall", 
            "Testing yourself to boost memory", 
            Arrays.asList("1. Review", "2. Test", "3. Check")
        ));
    }
    
    public void showMethods() {
        System.out.println("\n=== Study Methods Library ===");
        for (int i = 0; i < methods.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + methods.get(i).getName());
        }
    }

    public StudyMethod chooseMethod() {
        showMethods();
        System.out.print("Select a method: ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice > 0 && choice <= methods.size()) {
                return methods.get(choice - 1);
            }
        } catch (Exception e) {}
        return methods.get(0); 
    }
}