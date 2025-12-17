package com.focus.flashcards;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FlashcardManager {
    private final Scanner sc = new Scanner(System.in);
    private final List<FlashcardSet> sets = new ArrayList<>();

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        for (int i = 0; i < 20; i++) System.out.println();
    }

    private int readInt() {
        try { return Integer.parseInt(sc.nextLine().trim()); }
        catch (Exception e) { return -1; }
    }

    private void promptContinue() {
        System.out.print("Press Enter to continue...");
        sc.nextLine();
        clearScreen();
    }

    public void start() {
        int choice;
        do {
            clearScreen();
            System.out.println("===== FLASHCARDS MAIN MENU =====");
            System.out.println("1. Create Flashcard Set");
            System.out.println("2. Open Set");
            System.out.println("3. Delete Set");
            System.out.println("4. View Sets");
            System.out.println("0. Exit");
            System.out.print("Choose: ");
            choice = readInt();

            switch (choice) {
                case 1 -> createSet();
                case 2 -> openSetList();
                case 3 -> deleteSetList();
                case 4 -> { viewSets(); promptContinue(); }
                case 0 -> System.out.println("Exiting...");
                default -> { System.out.println("Invalid choice."); promptContinue(); }
            }
        } while (choice != 0);
    }

    private void createSet() {
        clearScreen();
        System.out.println("0. Back");
        System.out.print("Enter set name: ");
        String name = sc.nextLine().trim();
        if (name.equals("0") || name.isEmpty()) return;
        FlashcardSet set = new FlashcardSet(name);
        sets.add(set);
        promptContinue();
        runSetMenu(set);
    }

    private void viewSets() {
        clearScreen();
        System.out.println("===== AVAILABLE SETS =====");
        if (sets.isEmpty()) {
            System.out.println("No sets available.");
            return;
        }
        for (int i = 0; i < sets.size(); i++) {
            System.out.println((i + 1) + ". " + sets.get(i).getName() + " (" + sets.get(i).size() + " cards)");
        }
    }

    private void openSetList() {
        while (true) {
            clearScreen();
            System.out.println("0. Back");
            if (sets.isEmpty()) {
                System.out.print("Press Enter to go back...");
                sc.nextLine();
                return;
            }
            for (int i = 0; i < sets.size(); i++) {
                System.out.println((i + 1) + ". " + sets.get(i).getName());
            }
            System.out.print("Choose set: ");
            int choice = readInt();
            if (choice == 0) return;
            if (choice > 0 && choice <= sets.size())
                runSetMenu(sets.get(choice - 1));
        }
    }

    private void deleteSetList() {
        while (true) {
            clearScreen();
            System.out.println("0. Back");
            if (sets.isEmpty()) {
                System.out.print("Press Enter...");
                sc.nextLine();
                return;
            }
            for (int i = 0; i < sets.size(); i++)
                System.out.println((i + 1) + ". " + sets.get(i).getName());
            System.out.print("Delete set: ");
            int choice = readInt();
            if (choice == 0) return;
            if (choice > 0 && choice <= sets.size()) {
                sets.remove(choice - 1);
                promptContinue();
                return;
            }
        }
    }

    private void runSetMenu(FlashcardSet set) {
        int choice;
        do {
            clearScreen();
            System.out.println("0. Back");
            System.out.println("===== SET: " + set.getName() + " =====");
            System.out.println("1. Add Flashcard");
            System.out.println("2. Edit Flashcard");
            System.out.println("3. Delete Flashcard");
            System.out.println("4. Review Flashcards");
            System.out.println("5. View All Flashcards");
            System.out.println("6. Rename Set");
            System.out.print("Choose: ");
            choice = readInt();

            switch (choice) {
                case 1 -> addFlashcard(set);
                case 2 -> editFlashcard(set);
                case 3 -> deleteFlashcard(set);
                case 4 -> reviewFlashcards(set);
                case 5 -> { viewAll(set); promptContinue(); }
                case 6 -> renameSet(set);
            }
        } while (choice != 0);
    }

    private void addFlashcard(FlashcardSet set) {
        clearScreen();
        System.out.print("How many flashcards? ");
        int n = readInt();
        for (int i = 0; i < n; i++) {
            System.out.print("Question: ");
            String q = sc.nextLine();
            System.out.print("Answer: ");
            String a = sc.nextLine();
            set.addFlashcard(new Flashcard(q, a));
        }
        promptContinue();
    }

    private void editFlashcard(FlashcardSet set) {
        clearScreen();
        for (int i = 0; i < set.size(); i++)
            System.out.println((i + 1) + ". " + set.getCards().get(i).getQuestion());
        System.out.print("Choose: ");
        int idx = readInt() - 1;
        if (idx >= 0 && idx < set.size()) {
            System.out.print("New question: ");
            String q = sc.nextLine();
            System.out.print("New answer: ");
            String a = sc.nextLine();
            set.editFlashcard(idx, q, a);
        }
        promptContinue();
    }

    private void deleteFlashcard(FlashcardSet set) {
        clearScreen();
        for (int i = 0; i < set.size(); i++)
            System.out.println((i + 1) + ". " + set.getCards().get(i).getQuestion());
        System.out.print("Choose: ");
        int idx = readInt() - 1;
        set.removeFlashcard(idx);
        promptContinue();
    }

    private void reviewFlashcards(FlashcardSet set) {
        for (Flashcard c : set.getCards()) {
            clearScreen();
            System.out.println(c.getQuestion());
            sc.nextLine();
            System.out.println(c.getAnswer());
            sc.nextLine();
        }
    }

    private void viewAll(FlashcardSet set) {
        clearScreen();
        for (Flashcard c : set.getCards())
            System.out.println(c);
    }

    private void renameSet(FlashcardSet set) {
        clearScreen();
        System.out.print("New name: ");
        String name = sc.nextLine();
        if (!name.isEmpty()) set.setName(name);
        promptContinue();
    }
}
