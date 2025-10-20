package org.example.smarttasks;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple rule-based AI organizer for tasks.
 * It organizes tasks based on importance, priority, and due date.
 */
public class TaskOrganizerAI {

    /**
     * Organizes the list of tasks in place.
     * @param tasks List of tasks to organize.
     */
    public static void organizeTasks(List<Task> tasks) {
        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task t1, Task t2) {
                // Sort by importance (HIGH first)
                int importanceCompare = t1.getImportance().compareTo(t2.getImportance());
                if (importanceCompare != 0) {
                    return importanceCompare;
                }
                // Sort by priority (HIGH first)
                int priorityCompare = t1.getPriority().compareTo(t2.getPriority());
                if (priorityCompare != 0) {
                    return priorityCompare;
                }
                // Sort by due date (earlier first)
                return Long.compare(t1.getDueDate(), t2.getDueDate());
            }
        });
        // Reverse the list to have HIGH importance and priority first
        Collections.reverse(tasks);
    }
}
