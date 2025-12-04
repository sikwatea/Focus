package com.focus.tasks;

import java.util.Scanner;
import java.time.LocalDate;

public class TaskManager {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        TaskList list = new TaskList();

        int choice = 0;

        do {
            System.out.println("\n=== TO DO LIST MENU ===");
            System.out.println("[1] Add Task");
            System.out.println("[2] View Tasks");
            System.out.println("[3] Complete Task");
            System.out.println("[4] Edit Task");
            System.out.println("[5] Delete Task");
            System.out.println("[6] Change Due Date");
            System.out.println("[7] Clear Completed");
            System.out.println("[8] Exit");
            System.out.print("> ");

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Task title: ");
                    String title = sc.nextLine();

                    LocalDate date = null;
                    while (date == null) {
                        try {
                            System.out.print("Due date (YYYY-MM-DD): ");
                            date = LocalDate.parse(sc.nextLine());
                        } catch (Exception e) {
                            System.out.println("❌ Invalid date format.");
                        }
                    }

                    list.addTask(title, date);
                    break;

                case 2:
                    list.showTasks();
                    break;

                case 3:
                    list.showTasks();
                    System.out.print("Task #: ");
                    list.completeTask(parseIntSafe(sc.nextLine()) - 1);
                    break;

                case 4:
                    list.showTasks();
                    System.out.print("Task #: ");
                    int editIndex = parseIntSafe(sc.nextLine()) - 1;
                    System.out.print("New title: ");
                    String newTitle = sc.nextLine();
                    list.editTask(editIndex, newTitle);
                    break;

                case 5:
                    list.showTasks();
                    System.out.print("Task #: ");
                    list.deleteTask(parseIntSafe(sc.nextLine()) - 1);
                    break;

                case 6:
                    list.showTasks();
                    System.out.print("Task #: ");
                    int dateIndex = parseIntSafe(sc.nextLine()) - 1;

                    LocalDate newDate = null;
                    while (newDate == null) {
                        try {
                            System.out.print("New date (YYYY-MM-DD): ");
                            newDate = LocalDate.parse(sc.nextLine());
                        } catch (Exception e) {
                            System.out.println("❌ Invalid date format.");
                        }
                    }

                    list.changeDueDate(dateIndex, newDate);
                    break;

                case 7:
                    list.clearCompleted();
                    break;

                case 8:
                    System.out.println("Goodbye!");
                    break;

                default:
                    System.out.println("❌ Invalid choice.");
            }

        } while (choice != 8);

        sc.close();
    }

    private static int parseIntSafe(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return -1;
        }
    }
}
