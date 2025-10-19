package com.focus.studyhelper;

import java.util.ArrayList;
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
        methods.add(new StudyMethod("Pomodoro", "Study for an amount of time and get rests in between"));
        methods.add(new StudyMethod("Feynman Technique", "Explain concepts in simple terms"));
        methods.add(new StudyMethod("SQ3R", "Survey, Question, Read, Recite, and Review"));
    }

    public void showMethods() {
        System.out.println(BOLD + ANSI_GREEN + "\n=== Study Methods ===" + ANSI_RESET + RESET);
        for (int i = 0; i < methods.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + methods.get(i));
        }
    }

    public StudyMethod chooseMethod() {
        showMethods();
        System.out.print(ANSI_YELLOW + "Choose a method: " + ANSI_RESET);
        int choice = Integer.parseInt(scanner.nextLine());
        return methods.get(Math.max(0, choice - 1));
    }
}
