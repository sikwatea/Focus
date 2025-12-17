package com.focus.tasks;

import java.time.LocalDate;
import java.io.Serializable;

public class Task implements Serializable {
    protected String title;
    protected boolean completed;
    protected LocalDate dueDate;

    public Task(String title, LocalDate dueDate) {
        this.title = title;
        this.dueDate = dueDate;
        this.completed = false;
    }

    public void markCompleted() {
        completed = true;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String getType() {
        return "General";
    }

    public String toString() {
        return String.format("%-20s | %-12s | %-8s | %s",
                title,
                dueDate,
                getType(),
                completed ? "✔" : "✖");
    }
}

class StudyTask extends Task {

    public StudyTask(String title, LocalDate dueDate) {
        super(title, dueDate);
    }

    public String getType() {
        return "Study";
    }
}



