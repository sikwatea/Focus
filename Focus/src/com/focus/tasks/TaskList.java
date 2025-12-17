package com.focus.tasks;

import java.util.*;
import java.io.*;
import java.time.LocalDate;

public class TaskList {
    private ArrayList<Task> tasks;
    private final String FILE_NAME = "tasks.dat";

    public TaskList() {
        loadTasks();
    }

    public void addTask(String title, LocalDate date) {
        tasks.add(new StudyTask(title, date)); 
        saveTasks();
        System.out.println("Task added.");
    }

    public void showTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks available.");
            return;
        }

        System.out.printf("\n#  %-20s | %-12s | %-8s | Done\n", "Title", "Due Date", "Type");
        System.out.println("--------------------------------------------------------");

        for (int i = 0; i < tasks.size(); i++) {
            System.out.printf("%-2d %s\n", i + 1, tasks.get(i));
        }
    }

    public void completeTask(int index) {
        if (!isValidIndex(index)) return;
        tasks.get(index).markCompleted();
        saveTasks();
        System.out.println("Task completed.");
    }

    public void deleteTask(int index) {
        if (!isValidIndex(index)) return;
        tasks.remove(index);
        saveTasks();
        System.out.println("Task deleted.");
    }

    private boolean isValidIndex(int index) {
        if (index < 0 || index >= tasks.size()) {
            System.out.println("Invalid task number.");
            return false;
        }
        return true;
    }

    private void saveTasks() {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(tasks);
        } catch (IOException e) {
            System.out.println("Error saving tasks.");
        }
    }

    @SuppressWarnings("unchecked")
    private void loadTasks() {
        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            tasks = (ArrayList<Task>) in.readObject();
        } catch (Exception e) {
            tasks = new ArrayList<>();
        }
    }
}
