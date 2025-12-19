package com.focus.flashcards;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FlashcardService { // old flashcard manager, changed to this to handle lists of all sets
    private ArrayList<FlashcardSet> sets;
    private final String FILE_NAME = "flashcards.dat";

    public FlashcardService() {
        loadSets();
    }

    public void addSet(String name) {
        sets.add(new FlashcardSet(name));
        saveSets();
    }

    public void deleteSet(FlashcardSet set) {
        sets.remove(set);
        saveSets();
    }

    public ArrayList<FlashcardSet> getSets() {
        return sets;
    }

    public void saveChanges() {
        saveSets();
    }

    private void saveSets() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(sets);
        } catch (IOException e) {
            System.out.println("Error saving flashcards: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadSets() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            sets = new ArrayList<>();
            return;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            sets = (ArrayList<FlashcardSet>) in.readObject();
        } catch (Exception e) {
            sets = new ArrayList<>();
        }
    }
}