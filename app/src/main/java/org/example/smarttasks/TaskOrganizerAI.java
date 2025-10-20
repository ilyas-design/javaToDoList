package org.example.smarttasks;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * AI-powered task organizer that intelligently sorts and manages tasks.
 * Uses rule-based AI with smart prioritization algorithms.
 */
public class TaskOrganizerAI {
    public static boolean useCloudScores = true; // toggled by settings later

    /**
     * Organizes the list of tasks using AI-powered sorting algorithm.
     * @param tasks List of tasks to organize.
     */
    public static void organizeTasks(List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            return;
        }
        // Default weights: Importance 3, Priority 2, Due date 2
        organizeTasksWithWeights(tasks, 3, 2, 2);

        // Debug logging to verify sorting
        System.out.println("AI Sorting Results:");
        for (int i = 0; i < Math.min(tasks.size(), 5); i++) {
            Task task = tasks.get(i);
            System.out.println((i+1) + ". " + task.getTitle() + 
                             " (Importance: " + task.getImportance() + 
                             ", Priority: " + task.getPriority() + 
                             ", DueScore: " + computeDuePoints(task) + 
                             ", Score: " + getTaskPriorityScore(task, 3, 2, 2) + ")");
        }
    }

    /**
     * Sort with explicit weights for (importance, priority, due-date).
     * Higher weights make that criterion contribute more to the final score.
     */
    public static void organizeTasksWithWeights(List<Task> tasks, int importanceWeight, int priorityWeight, int dueWeight) {
        if (tasks == null || tasks.isEmpty()) return;
        // Sort using the parameterized comparator
        Collections.sort(tasks, new TaskComparator(importanceWeight, priorityWeight, dueWeight));
        
    }

    /**
     * AI-powered comparator that implements smart task prioritization.
     */
    private static class TaskComparator implements Comparator<Task> {
        private final int importanceWeight;
        private final int priorityWeight;
        private final int dueWeight;

        private TaskComparator() {
            this(3, 2, 2);
        }

        private TaskComparator(int importanceWeight, int priorityWeight, int dueWeight) {
            this.importanceWeight = importanceWeight;
            this.priorityWeight = priorityWeight;
            this.dueWeight = dueWeight;
        }
        @Override
        public int compare(Task t1, Task t2) {
            int score1 = getTaskPriorityScore(t1, importanceWeight, priorityWeight, dueWeight);
            int score2 = getTaskPriorityScore(t2, importanceWeight, priorityWeight, dueWeight);
            
            // Higher score = higher priority (appears first)
            int scoreCompare = Integer.compare(score2, score1);
            if (scoreCompare != 0) {
                return scoreCompare;
            }
            
            // If scores are equal, use due date as tiebreaker
            int dueDateCompare = Long.compare(t1.getDueDate(), t2.getDueDate());
            if (dueDateCompare != 0) {
                return dueDateCompare;
            }
            
            // Final tiebreaker: creation time (older first)
            return Long.compare(t1.getCreatedAt(), t2.getCreatedAt());
        }
    }

    /**
     * AI method to analyze task urgency based on due date.
     * @param task The task to analyze
     * @return true if task is urgent (due within 3 days)
     */
    public static boolean isTaskUrgent(Task task) {
        long currentTime = System.currentTimeMillis();
        long threeDaysInMillis = 3 * 24 * 60 * 60 * 1000L;
        return (task.getDueDate() - currentTime) <= threeDaysInMillis;
    }

    /**
     * AI method to get task priority score for advanced sorting.
     * @param task The task to score
     * @return Priority score (higher = more important)
     */
    public static int getTaskPriorityScore(Task task) {
        return getTaskPriorityScore(task, 3, 2, 2);
    }

    /** Parameterized scoring using importance, priority and due date weights. */
    public static int getTaskPriorityScore(Task task, int importanceWeight, int priorityWeight, int dueWeight) {
        int importancePoints = mapImportance(task.getImportance()); // 3,2,1
        int priorityPoints = mapPriority(task.getPriority());       // 3,2,1
        int duePoints = computeDuePoints(task);                     // 0..3 (overdue/soon => higher)

        int score = importanceWeight * importancePoints
                + priorityWeight * priorityPoints
                + dueWeight * duePoints;

        // Optional: blend cloud probabilities if available via tags
        try {
            String desc = task.getDescription();
            if (desc != null && desc.startsWith("[IMP:")) {
                int impStart = desc.indexOf("[IMP:") + 5;
                int impEnd = desc.indexOf("]", impStart);
                int urgStart = desc.indexOf("[URG:") + 5;
                int urgEnd = desc.indexOf("]", urgStart);
                double imp = Double.parseDouble(desc.substring(impStart, impEnd));
                double urg = Double.parseDouble(desc.substring(urgStart, urgEnd));
                score += (int) Math.round(0.5 * (imp * 10) + 0.5 * (urg * 10));
            }
        } catch (Exception ignored) { }

        return score;
    }

    private static int mapImportance(Task.Importance imp) {
        switch (imp) {
            case HIGH: return 3;
            case MEDIUM: return 2;
            case LOW: return 1;
            default: return 0;
        }
    }

    private static int mapPriority(Task.Priority pr) {
        switch (pr) {
            case HIGH: return 3;
            case MEDIUM: return 2;
            case LOW: return 1;
            default: return 0;
        }
    }

    /**
     * Convert due date to discrete points 0..3
     * - Overdue or <=3 days: 3
     * - <=7 days: 2
     * - <=30 days: 1
     * - otherwise: 0
     */
    private static int computeDuePoints(Task task) {
        long now = System.currentTimeMillis();
        long diff = task.getDueDate() - now;
        long dayMs = 24L * 60 * 60 * 1000;
        long days = (long) Math.floor((double) diff / dayMs);
        if (days <= 3) return 3;         // includes overdue (negative days)
        if (days <= 7) return 2;
        if (days <= 30) return 1;
        return 0;
    }
}
