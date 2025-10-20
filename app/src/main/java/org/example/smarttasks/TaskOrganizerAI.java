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
        if (tasks == null || tasks.isEmpty()) {
            return;
        }
        
        // Sort using the AI comparator
        Collections.sort(tasks, new TaskComparator());
        
        // Debug logging to verify sorting
        System.out.println("AI Sorting Results:");
        for (int i = 0; i < Math.min(tasks.size(), 5); i++) {
            Task task = tasks.get(i);
            System.out.println((i+1) + ". " + task.getTitle() + 
                             " (Importance: " + task.getImportance() + 
                             ", Priority: " + task.getPriority() + 
                             ", Score: " + getTaskPriorityScore(task) + ")");
        }
    }

    /**
     * AI-powered comparator that implements smart task prioritization.
     */
    private static class TaskComparator implements Comparator<Task> {
        @Override
        public int compare(Task t1, Task t2) {
            // Use the priority score for more accurate sorting
            int score1 = getTaskPriorityScore(t1);
            int score2 = getTaskPriorityScore(t2);
            
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
