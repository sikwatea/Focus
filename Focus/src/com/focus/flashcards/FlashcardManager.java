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
        for(int i=0;i<20;i++) System.out.println();
    }

    private int readInt() {
        String line = sc.nextLine().trim();
        try { return Integer.parseInt(line); } catch(Exception e){ return -1; }
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
            System.out.println("[1] Create Flashcard Set");
            System.out.println("[2] Open Set");
            System.out.println("[3] Delete Set");
            System.out.println("[4] View Sets");
            System.out.println("[0] Exit");
            System.out.print("> ");
            choice = readInt();

            switch(choice){
                case 1 -> createSet();
                case 2 -> openSetList();
                case 3 -> deleteSetList();
                case 4 -> { viewSets(); promptContinue(); }
                case 0 -> System.out.println("Exiting...");
                default -> { System.out.println("Invalid choice."); promptContinue(); }
            }
        } while(choice != 0);
    }

    private void createSet() {
        clearScreen();
        System.out.println("[0] Back");
        System.out.print("Enter set name: ");
        String name = sc.nextLine().trim();
        if(name.equals("0") || name.isEmpty()) return;
        FlashcardSet newSet = new FlashcardSet(name);
        sets.add(newSet);
        System.out.println("Set created. Opening...");
        promptContinue();
        runSetMenu(newSet);
    }

    private void viewSets() {
        clearScreen();
        System.out.println("===== AVAILABLE SETS =====");
        if(sets.isEmpty()){
            System.out.println("No sets available.");
            return;
        }
        for(int i=0;i<sets.size();i++){
            System.out.println((i+1)+". "+sets.get(i).getName()+" ("+sets.get(i).size()+" cards)");
        }
    }

    private void openSetList() {
        while(true){
            clearScreen();
            System.out.println("[0] Back");
            System.out.println("===== OPEN A SET =====");
            if(sets.isEmpty()){
                System.out.println("No sets to open.");
                System.out.print("Press Enter to go back...");
                sc.nextLine();
                return;
            }
            for(int i=0;i<sets.size();i++){
                System.out.println((i+1)+". "+sets.get(i).getName()+" ("+sets.get(i).size()+" cards)");
            }
            System.out.print("Choose set number (0 to back): ");
            int choice = readInt();
            if(choice==0) return;
            int idx = choice-1;
            if(idx>=0 && idx<sets.size()) runSetMenu(sets.get(idx));
            else { System.out.println("Invalid choice."); promptContinue(); }
        }
    }

    private void deleteSetList() {
        while(true){
            clearScreen();
            System.out.println("[0] Back");
            System.out.println("===== DELETE A SET =====");
            if(sets.isEmpty()){
                System.out.println("No sets to delete.");
                System.out.print("Press Enter to go back...");
                sc.nextLine();
                return;
            }
            for(int i=0;i<sets.size();i++){
                System.out.println((i+1)+". "+sets.get(i).getName());
            }
            System.out.print("Choose set number to delete (0 to back): ");
            int choice = readInt();
            if(choice==0) return;
            int idx = choice-1;
            if(idx>=0 && idx<sets.size()){
                System.out.print("Confirm delete? (y/n): ");
                String conf = sc.nextLine().trim().toLowerCase();
                if(conf.equals("y")) {
                    sets.remove(idx);
                    System.out.println("Deleted.");
                    promptContinue();
                    return;
                } else { System.out.println("Cancelled."); promptContinue(); return; }
            } else { System.out.println("Invalid choice."); promptContinue(); }
        }
    }

    private void runSetMenu(FlashcardSet set) {
        int choice;
        do {
            clearScreen();
            System.out.println("[0] Back");
            System.out.println("===== SET: "+set.getName()+" =====");
            System.out.println("[1] Add Flashcard");
            System.out.println("[2] Edit Flashcard");
            System.out.println("[3] Delete Flashcard");
            
            System.out.println("[4] Review Flashcards");
            System.out.println("[5] View All Flashcards");
            System.out.println("[6] Rename Set");
            System.out.print("Choose: ");
            choice = readInt();

            switch(choice){
                case 0 -> {}
                case 1 -> addFlashcard(set);
                case 2 -> editFlashcard(set);
                case 3 -> deleteFlashcard(set);
                case 4 -> reviewFlashcards(set);
                case 5 -> { viewAll(set); promptContinue(); }
                case 6 -> renameSet(set);
                default -> { System.out.println("Invalid choice."); promptContinue(); }
            }
        } while(choice != 0);
    }

    private void addFlashcard(FlashcardSet set){
        while(true){
            clearScreen();
            System.out.println("[0] Back");
            System.out.println("Add Flashcards");
            System.out.print("Choose (0 to back, 1 to proceed): ");
            int choice = readInt();
            if(choice==0) return;
            if(choice==1){
                clearScreen();
                System.out.print("How many flashcards do you want to add? ");
                int n = readInt();
                if(n <= 0){
                    System.out.println("Invalid number.");
                    promptContinue();
                    return;
                }
                for(int i=1; i<=n; i++){
                    clearScreen();
                    System.out.println("Adding flashcard "+i+" of "+n);
                    System.out.print("Question: ");
                    String q = sc.nextLine();
                    System.out.print("Answer: ");
                    String a = sc.nextLine();
                    if(!q.isEmpty()){
                        set.addFlashcard(new Flashcard(q,a));
                        System.out.println("Flashcard added.");
                    } else {
                        System.out.println("Question empty. Skipping this flashcard.");
                    }
                    if(i < n){
                        System.out.print("Press Enter to continue to next flashcard...");
                        sc.nextLine();
                    }
                }
                System.out.println("Finished adding flashcards.");
                promptContinue();
                return;
            } else {
                System.out.println("Invalid choice.");
                promptContinue();
            }
        }
    }

    private void editFlashcard(FlashcardSet set){
        while(true){
            clearScreen();
            System.out.println("[0] Back");
            System.out.println("===== EDIT FLASHCARD =====");
            if(set.size()==0){
                System.out.println("No flashcards to edit.");
                System.out.print("Press Enter to go back...");
                sc.nextLine();
                return;
            }
            for(int i=0;i<set.size();i++){
                System.out.println((i+1)+". "+set.getCards().get(i).getQuestion());
            }
            System.out.print("Choose card (0 to back): ");
            int choice = readInt();
            if(choice==0) return;
            int idx=choice-1;
            if(idx>=0 && idx<set.size()){
                Flashcard c = set.getCards().get(idx);
                System.out.println("Current Q: "+c.getQuestion());
                System.out.print("New Q (blank=keep): ");
                String nq=sc.nextLine();
                System.out.println("Current A: "+c.getAnswer());
                System.out.print("New A (blank=keep): ");
                String na=sc.nextLine();
                if(nq.isEmpty()) nq=c.getQuestion();
                if(na.isEmpty()) na=c.getAnswer();
                set.editFlashcard(idx,nq,na);
                System.out.println("Updated.");
                promptContinue();
                return;
            } else { System.out.println("Invalid choice."); promptContinue(); }
        }
    }

    private void deleteFlashcard(FlashcardSet set){
        while(true){
            clearScreen();
            System.out.println("[0] Back");
            System.out.println("===== DELETE FLASHCARD =====");
            if(set.size()==0){
                System.out.println("No flashcards.");
                System.out.print("Press Enter to go back...");
                sc.nextLine();
                return;
            }
            for(int i=0;i<set.size();i++){
                System.out.println((i+1)+". "+set.getCards().get(i).getQuestion());
            }
            System.out.print("Choose card (0 to back): ");
            int choice = readInt();
            if(choice==0) return;
            int idx=choice-1;
            if(idx>=0 && idx<set.size()){
                System.out.print("Confirm delete? (y/n): ");
                String conf=sc.nextLine().trim().toLowerCase();
                if(conf.equals("y")){
                    set.removeFlashcard(idx);
                    System.out.println("Deleted.");
                    promptContinue();
                    return;
                } else { System.out.println("Cancelled."); promptContinue(); return; }
            } else { System.out.println("Invalid choice."); promptContinue(); }
        }
    }

    private void reviewFlashcards(FlashcardSet set){
        while(true){
            clearScreen();
            System.out.println("[0] Back");
            if(set.size()==0){
                System.out.println("No flashcards to review.");
                System.out.print("Press Enter to go back...");
                sc.nextLine();
                return;
            }
            System.out.println("1. Start Review");
            System.out.print("Choose (0 to back, 1 to start): ");
            int choice=readInt();
            if(choice==0) return;
            if(choice==1){
                for(int i=0;i<set.size();i++){
                    clearScreen();
                    Flashcard c=set.getCards().get(i);
                    System.out.println("Q"+(i+1)+"/"+set.size());
                    System.out.println(c.getQuestion());
                    System.out.print("Press Enter to show answer...");
                    sc.nextLine();
                    System.out.println("Answer: "+c.getAnswer());
                    if(i<set.size()-1){
                        System.out.print("Next? Press Enter...");
                        sc.nextLine();
                    } else {
                        System.out.print("Review finished. Press Enter...");
                        sc.nextLine();
                    }
                }
                clearScreen();
                return;
            } else { System.out.println("Invalid choice."); promptContinue(); }
        }
    }

    private void viewAll(FlashcardSet set){
        clearScreen();
        System.out.println("[0] Back");
        System.out.println("===== ALL FLASHCARDS IN SET: "+set.getName()+" =====");
        if(set.size()==0) System.out.println("No flashcards.");
        else {
            for(int i=0;i<set.size();i++){
                System.out.println((i+1)+". "+set.getCards().get(i));
            }
        }
    }

    private void renameSet(FlashcardSet set){
        while(true){
            clearScreen();
            System.out.println("[0] Back");
            System.out.println("Current name: "+set.getName());
            System.out.print("Enter new name (0 to back): ");
            String name = sc.nextLine().trim();
            if(name.equals("0")) return;
            if(!name.isEmpty()){
                set.setName(name);
                System.out.println("Renamed.");
                promptContinue();
                return;
            } else { System.out.println("Invalid name."); promptContinue(); }
        }
    }
}
