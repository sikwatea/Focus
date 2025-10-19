package com.focus.studyhelper;

import java.util.Scanner;

public class FocusManager {
	private Scanner scanner = new Scanner(System.in);
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String BOLD = "\033[0;1m";
    public static final String RESET = "\033[0m";
    
	public void start() {
        boolean running = true;

        while (running) {
            System.out.println(BOLD + ANSI_GREEN + "\n=== Focus Study Helper ===" + ANSI_RESET + RESET);
            System.out.println("[1] Start a Focus Session");
            System.out.println("[2] Study Techniques");
            System.out.println("[3] Session History");
            System.out.println("[4] Return to Home");
            System.out.println("[5] asdfwafdsfdasdf");
            System.out.println(ANSI_RED + "[4] Return to Home" + ANSI_RESET);
            System.out.println("[5] asdfwafdsfdasdf");
            System.out.print("> ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                //case 1 -> startSession();
                //case 2 -> viewHistory();
                //case 3 -> methodManager.showMethods();
                case 4 -> running = false;
                default -> System.out.println("\nNot in selection, Please try again :)");
            }
        }
    }
}