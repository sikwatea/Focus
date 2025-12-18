package com.focus.flashcards;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

interface FlashcardRepository {
    void addFlashcard(Flashcard card);
    boolean removeFlashcard(int index);
    boolean editFlashcard(int index, String question, String answer);
    List<Flashcard> getCards();
    int size();
}

public class FlashcardSet implements FlashcardRepository, Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private List<Flashcard> cards;

    public FlashcardSet(String name) {
        this.name = name;
        this.cards = new ArrayList<>();
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public List<Flashcard> getCards() { return cards; }

    @Override
    public void addFlashcard(Flashcard card) {
        cards.add(card);
    }

    @Override
    public boolean removeFlashcard(int index) {
        if (index >= 0 && index < cards.size()) {
            cards.remove(index);
            return true;
        }
        return false;
    }
    
    // helper for removal
    public void removeFlashcard(Flashcard card) {
        cards.remove(card);
    }

    @Override
    public boolean editFlashcard(int index, String question, String answer) {
        if (index >= 0 && index < cards.size()) {
            Flashcard c = cards.get(index);
            c.setQuestion(question);
            c.setAnswer(answer);
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return cards.size();
    }
    
    @Override
    public String toString() {
        return name + " (" + cards.size() + " cards)";
    }
}