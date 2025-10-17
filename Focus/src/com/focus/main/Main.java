package com.focus.main;

import java.util.Scanner;
import com.focus.tasks.TaskManager;
import com.focus.flashcards.FlashcardManager;
import com.focus.studyhelper.FocusManager;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String BOLD = "\033[0;1m";
    public static final String RESET = "\033[0m";
    
    public static void main(String[] args) {
        boolean running = true;
        
        while (running) {
            System.out.println(BOLD + ANSI_GREEN + "\n=== Focus App ===" + ANSI_RESET + RESET);
            System.out.println("[1] Task Manager");
            System.out.println("[2] Flashcards");
            System.out.println("[3] Focus Study Helper");
            System.out.println(ANSI_RED + "[4] Exit" + ANSI_RESET);
            System.out.print("> ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                //case 1 -> new TaskManager().start();
                //case 2 -> new FlashcardManager().start();
                case 3 -> new FocusManager().start();
                case 4 -> running = false;
                default -> System.out.println("\nNot in selection, Please try again :)");
            }
        }

        System.out.println("NOOOOOOOO!");
    }
}