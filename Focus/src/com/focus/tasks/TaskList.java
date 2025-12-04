package com.focus.tasks;

import java.util.*;
import java.io.*;
import java.time.LocalDate;

public class TaskList {
    private ArrayList<Task> tasks = new ArrayList<>();
    private final String FILE_NAME = "tasks.txt";

    public TaskList() {
        loadFromFile();
    }

    public void addTask(String title, LocalDate date) {
        tasks.add(new Task(title, date, false));
        saveToFile();
        System.out.println("Task added.");
    }

    public void showTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks.");
            return;
        }

        System.out.println("\n--- TASK LIST ---");
        System.out.printf("%-3s %-20s | %-12s | %s\n",
                "#", "Title", "Due Date", "Status");
        System.out.println("-----------------------------------------------");

        for (int i = 0; i < tasks.size(); i++) {
            System.out.printf("%-3d %s\n", (i + 1), tasks.get(i));
        }
    }

    public void completeTask(int index) {
        if (!isValidIndex(index)) return;

        tasks.get(index).markCompleted();
        saveToFile();
        System.out.println("Task completed.");
    }

    public void editTask(int index, String newTitle) {
        if (!isValidIndex(index)) return;

        tasks.get(index).setTitle(newTitle);
        saveToFile();
        System.out.println("Task updated.");
    }

    public void deleteTask(int index) {
        if (!isValidIndex(index)) return;

        tasks.remove(index);
        saveToFile();
        System.out.println("Task deleted.");
    }

    public void changeDueDate(int index, LocalDate newDate) {
        if (!isValidIndex(index)) return;

        tasks.get(index).setDueDate(newDate);
        saveToFile();
        System.out.println("Due date updated.");
    }

    public void clearCompleted() {
        tasks.removeIf(Task::isCompleted);
        saveToFile();
        System.out.println("Cleared completed tasks.");
    }

    private boolean isValidIndex(int index) {
        if (index < 0 || index >= tasks.size()) {
            System.out.println("❌ Invalid task number.");
            return false;
        }
        return true;
    }


    public void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Task t : tasks) {
                writer.println(t.toDataString());
            }
        } catch (IOException e) {
            System.out.println("❌ Error saving tasks.");
        }
    }

    public void loadFromFile() {
        File f = new File(FILE_NAME);
        if (!f.exists()) return;

        try (Scanner reader = new Scanner(f)) {
            while (reader.hasNextLine()) {
                String[] parts = reader.nextLine().split("\\|");
                String title = parts[0];
                LocalDate date = LocalDate.parse(parts[1]);
                boolean completed = Boolean.parseBoolean(parts[2]);

                tasks.add(new Task(title, date, completed));
            }
        } catch (Exception e) {
            System.out.println("❌ Error loading tasks. File may be corrupted.");
        }
    }
}
