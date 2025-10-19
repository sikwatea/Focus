package com.focus.studyhelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudyMethodManager {
    private List<StudyMethod> methods = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);

    public StudyMethodManager() {
        methods.add(new StudyMethod("Pomodoro", "Study 25 mins, rest 5 mins."));
        methods.add(new StudyMethod("Feynman Technique", "Explain concepts in simple terms."));
        methods.add(new StudyMethod("SQ3R", "Survey, Question, Read, Recite, Review."));
    }

    public void showMethods() {
        System.out.println("\n=== Study Methods ===");
        for (int i = 0; i < methods.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + methods.get(i));
        }
    }

    public StudyMethod chooseMethod() {
        showMethods();
        System.out.print("Choose a method: ");
        int choice = Integer.parseInt(scanner.nextLine());
        return methods.get(Math.max(0, choice - 1));
    }
}
