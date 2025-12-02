package com.focus.studyhelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FocusManager {
    private Scanner scanner = new Scanner(System.in);
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String BOLD = "\033[0;1m";
    public static final String RESET = "\033[0m";
    
    private List<FocusSession> sessionHistory = new ArrayList<>();
    private StudyMethodManager methodManager = new StudyMethodManager();
    
    private FocusSession currentSession; 

    public void start() {
        boolean running = true;

        while (running) {
            System.out.println(BOLD + ANSI_GREEN + "\n=== Focus Study Helper ===" + ANSI_RESET + RESET);
            System.out.println("[1] New Focus Session");
            System.out.println("[2] Study Methods Library");
            System.out.println("[3] Session History");
            System.out.println(ANSI_RED + "[4] Return to Home" + ANSI_RESET);
            System.out.print("> ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> setupSession();
                    case 2 -> methodManager.chooseMethod();
                    case 3 -> viewHistory();
                    case 4 -> running = false;
                    default -> System.out.println("Invalid selection.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input.");
            }
        }
    }

    private void setupSession() {
        System.out.println("\n--- Session Setup ---");
        
        // sesh label
        System.out.print("Session Label (e.g. 'Math Review'): ");
        String label = scanner.nextLine();
        
        // sesh method
        StudyMethod method = methodManager.chooseMethod();
        
        if (method.getType() == StudyMethod.MethodType.ACTIVE_RECALL) {
            System.out.println(ANSI_CYAN + "\n[INFO] For Active Recall, please use the Flashcards section of the app." + ANSI_RESET);
            return;
        }

        // sesh config
        System.out.print("Total Duration (minutes): ");
        int duration = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Number of breaks: ");
        int breaks = Integer.parseInt(scanner.nextLine());
        
        int breakDuration = 0;
        if (breaks > 0) {
            System.out.print("Duration per break (minutes): ");
            breakDuration = Integer.parseInt(scanner.nextLine());
        }

        currentSession = new FocusSession(label, method, duration, breaks, breakDuration);
        runSessionConsoleLoop();
        
        sessionHistory.add(currentSession);
    }

    private void runSessionConsoleLoop() {
        System.out.println("\nStarting Session... (Press Enter to Pause/Distraction menu)");
        
        boolean sessionActive = true;
        long lastUpdate = System.currentTimeMillis();
        
        while (sessionActive) {
            try {
                Thread.sleep(1000); 
                
                sessionActive = currentSession.tick();
                
                System.out.print("\r" + currentSession.getStatusDisplay() + "   ");
             
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
        }
        System.out.println("\n" + ANSI_GREEN + "Session Complete!" + ANSI_RESET);
    }
    
    private void viewHistory() {
        System.out.println("\n=== Session History ===");
        if (sessionHistory.isEmpty()) {
            System.out.println("No past sessions found.");
        } else {
            for (FocusSession s : sessionHistory) {
                System.out.println("-------------------------");
                System.out.println(s);
            }
        }
    }
}