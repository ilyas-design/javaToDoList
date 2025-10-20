package org.example.smarttasks;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * AI-powered task organizer that intelligently sorts and manages tasks.
 * Uses rule-based AI with smart prioritization algorithms.
 */
public class TaskOrganizerAI {

    /**
     * Organizes the list of tasks using AI-powered sorting algorithm.
     * @param tasks List of tasks to organize.
     */
    public static void organizeTasks(List<Task> tasks) {
        Collections.sort(tasks, new TaskComparator());
    }

    /**
     * AI-powered comparator that implements smart task prioritization.
     */
    private static class TaskComparator implements Comparator<Task> {
        @Override
        public int compare(Task t1, Task t2) {
            // AI Rule 1: Importance is the primary factor
            int importanceCompare = compareImportance(t1.getImportance(), t2.getImportance());
            if (importanceCompare != 0) {
                return importanceCompare;
            }

            // AI Rule 2: Priority is the secondary factor
            int priorityCompare = comparePriority(t1.getPriority(), t2.getPriority());
            if (priorityCompare != 0) {
                return priorityCompare;
            }

            // AI Rule 3: Due date urgency (earlier = higher priority)
            int dueDateCompare = Long.compare(t1.getDueDate(), t2.getDueDate());
            if (dueDateCompare != 0) {
                return dueDateCompare;
            }

            // AI Rule 4: Creation time (older tasks get slight priority)
            return Long.compare(t1.getCreatedAt(), t2.getCreatedAt());
        }

        private int compareImportance(Task.Importance i1, Task.Importance i2) {
            return getImportanceValue(i2) - getImportanceValue(i1); // Higher importance first
        }

        private int comparePriority(Task.Priority p1, Task.Priority p2) {
            return getPriorityValue(p2) - getPriorityValue(p1); // Higher priority first
        }

        private int getImportanceValue(Task.Importance importance) {
            switch (importance) {
                case HIGH: return 3;
                case MEDIUM: return 2;
                case LOW: return 1;
                default: return 0;
            }
        }

        private int getPriorityValue(Task.Priority priority) {
            switch (priority) {
                case HIGH: return 3;
                case MEDIUM: return 2;
                case LOW: return 1;
                default: return 0;
            }
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
        int score = 0;
        
        // Importance scoring
        switch (task.getImportance()) {
            case HIGH: score += 30; break;
            case MEDIUM: score += 20; break;
            case LOW: score += 10; break;
        }
        
        // Priority scoring
        switch (task.getPriority()) {
            case HIGH: score += 20; break;
            case MEDIUM: score += 15; break;
            case LOW: score += 10; break;
        }
        
        // Urgency bonus
        if (isTaskUrgent(task)) {
            score += 25;
        }
        
        return score;
    }
}
