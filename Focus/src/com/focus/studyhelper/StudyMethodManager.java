package com.focus.studyhelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class StudyMethodManager {
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String BOLD = "\033[0;1m";
    public static final String RESET = "\033[0m";

    private List<StudyMethod> methods = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);

    public StudyMethodManager() {
        // Pomodoro
        List<String> pomodoroSteps = Arrays.asList(
            "1. Pick a task.",
            "2. Set timer for 25 minutes.",
            "3. Work until timer rings.",
            "4. Take a short 5 minute break."
        );
        methods.add(new StudyMethod("Pomodoro", "Standard interval training", StudyMethod.MethodType.POMODORO, pomodoroSteps));

        // Feynman
        List<String> feynmanSteps = Arrays.asList(
            "1. Choose a concept to study.",
            "2. Study Phase: Read and research.",
            "3. Breakdown Phase: Explain it in simple terms (as if to a child).",
            "4. Review and refine."
        );
        methods.add(new StudyMethod("Feynman Technique", "Deep understanding through simplification", StudyMethod.MethodType.FEYNMAN, feynmanSteps));

        // Active Recall
        List<String> activeRecallSteps = Arrays.asList(
            "1. Review topic.",
            "2. Close book/notes.",
            "3. Test yourself (Flashcards recommended).",
            "4. Check answers."
        );
        methods.add(new StudyMethod("Active Recall", "Testing yourself to boost memory", StudyMethod.MethodType.ACTIVE_RECALL, activeRecallSteps));
    }

    public void showMethods() {
        System.out.println(BOLD + ANSI_GREEN + "\n=== Study Methods Library ===" + ANSI_RESET + RESET);
        for (int i = 0; i < methods.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + methods.get(i).getName());
        }
    }

    public StudyMethod chooseMethod() {
        showMethods();
        System.out.print(ANSI_YELLOW + "Select a method to view details/start: " + ANSI_RESET);
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice > 0 && choice <= methods.size()) {
                StudyMethod m = methods.get(choice - 1);
                printMethodDetails(m);
                return m;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
        return methods.get(0); 
    }

    private void printMethodDetails(StudyMethod m) {
        System.out.println("\n" + BOLD + "Selected: " + m.getName() + RESET);
        System.out.println("Description: " + m.getDescription());
        System.out.println(ANSI_YELLOW + "Steps:" + ANSI_RESET);
        for(String step : m.getSteps()) {
            System.out.println(" " + step);
        }
    }
}