package com.focus.tasks;

import java.time.LocalDate;
import java.io.Serializable;

public class Task implements Serializable {
    private static final long serialVersionUID = 1L; 
    
    protected String title;
    protected boolean completed;
    protected LocalDate dueDate;

    public Task(String title, LocalDate dueDate) {
        this.title = title;
        this.dueDate = dueDate;
        this.completed = false;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    // getters and setters for javafx
    public String getTitle() { return title; }
    public LocalDate getDueDate() { return dueDate; }
    public boolean isCompleted() { return completed; }

    public String getType() {
        return "General";
    }

    @Override
    public String toString() {
        return title;
    }
}
