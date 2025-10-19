package com.focus.studyhelper;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class FocusManager {
	private Scanner scanner = new Scanner(System.in);
	private List<FocusSession> sessions = new ArrayList<>();
	private StudyMethodManager methodManager = new StudyMethodManager();
	
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String BOLD = "\033[0;1m";
    public static final String RESET = "\033[0m";
    
	public void start() {
        boolean running = true;

        while (running) {
            System.out.println(BOLD + ANSI_GREEN + "\n=== Focus Study Helper ===" + ANSI_RESET + RESET);
            System.out.println("[1] Start a Focus Session");
            System.out.println("[2] Study Techniques");
            System.out.println("[3] Session History");
<<<<<<< Upstream, based on origin/master
            System.out.println("[4] Return to Home");
            System.out.println("[5] asdfwafdsfdasdf");
=======
            System.out.println(ANSI_RED + "[4] Return to Home" + ANSI_RESET);
>>>>>>> cb76a46 test
            System.out.print("> ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> startSession();
                case 2 -> methodManager.showMethods();
                case 3 -> viewHistory();
                case 4 -> running = false;
                default -> System.out.println("\nNot in selection, Please try again :)");
            }
        }
    }

    private void startSession() {
        System.out.println("\nSelect a study method first:");
        StudyMethod method = methodManager.chooseMethod();
        FocusSession session = new FocusSession(method);

        System.out.print("Enter duration (minutes): ");
        int duration = Integer.parseInt(scanner.nextLine());
        session.start(duration);

        sessions.add(session);
    }
	
	private void viewHistory() {
	    System.out.println(BOLD + "\n=== Session History ===" + RESET);
	    if (sessions.isEmpty()) {
	        System.out.println("No past sessions found.");
	    } else {
	        for (FocusSession temp : sessions) {
	            System.out.println(temp);
	        }
	    }
	}
}