package org.example.smarttasks;

import android.app.Application;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Utility class to populate the database with dummy data for demonstration purposes.
 */
public class DummyDataGenerator {
    
    public static void populateDatabase(Application application) {
        TaskRepository repository = new TaskRepository(application);
        
        // Clear existing data first
        clearAllTasks(repository);
        
        // Generate dummy tasks
        List<Task> dummyTasks = generateDummyTasks();
        
        // Insert dummy tasks
        for (Task task : dummyTasks) {
            repository.insert(task);
        }
    }
    
    private static void clearAllTasks(TaskRepository repository) {
        // This would require adding a clearAll method to repository
        // For now, we'll just add new tasks
    }
    
    public static List<Task> generateDummyTasks() {
        List<Task> tasks = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        
        // High Priority, High Importance - Urgent
        calendar.set(2024, Calendar.JANUARY, 15); // Due in 3 days
        tasks.add(new Task(
            "Complete Project Proposal",
            "Finish the quarterly project proposal for the new mobile app development initiative. Include budget estimates, timeline, and resource requirements.",
            Task.Priority.HIGH,
            Task.Importance.HIGH,
            calendar.getTimeInMillis()
        ));
        
        // High Priority, Medium Importance - Urgent
        calendar.set(2024, Calendar.JANUARY, 16); // Due in 4 days
        tasks.add(new Task(
            "Client Meeting Preparation",
            "Prepare presentation slides and gather all necessary documents for the upcoming client meeting on Friday.",
            Task.Priority.HIGH,
            Task.Importance.MEDIUM,
            calendar.getTimeInMillis()
        ));
        
        // Medium Priority, High Importance
        calendar.set(2024, Calendar.JANUARY, 25); // Due in 2 weeks
        tasks.add(new Task(
            "Code Review Session",
            "Conduct thorough code review for the authentication module and provide feedback to the development team.",
            Task.Priority.MEDIUM,
            Task.Importance.HIGH,
            calendar.getTimeInMillis()
        ));
        
        // High Priority, Low Importance
        calendar.set(2024, Calendar.JANUARY, 20); // Due in 1 week
        tasks.add(new Task(
            "Update Documentation",
            "Update the API documentation with the latest endpoint changes and examples.",
            Task.Priority.HIGH,
            Task.Importance.LOW,
            calendar.getTimeInMillis()
        ));
        
        // Medium Priority, Medium Importance
        calendar.set(2024, Calendar.FEBRUARY, 1); // Due in 3 weeks
        tasks.add(new Task(
            "Team Building Event",
            "Organize and plan the quarterly team building event. Book venue, arrange catering, and send invitations.",
            Task.Priority.MEDIUM,
            Task.Importance.MEDIUM,
            calendar.getTimeInMillis()
        ));
        
        // Low Priority, High Importance
        calendar.set(2024, Calendar.FEBRUARY, 10); // Due in 1 month
        tasks.add(new Task(
            "Research New Technologies",
            "Research and evaluate new frontend frameworks for potential adoption in upcoming projects.",
            Task.Priority.LOW,
            Task.Importance.HIGH,
            calendar.getTimeInMillis()
        ));
        
        // Medium Priority, Low Importance
        calendar.set(2024, Calendar.JANUARY, 30); // Due in 2 weeks
        tasks.add(new Task(
            "Office Supplies Order",
            "Order new office supplies including notebooks, pens, and coffee for the development team.",
            Task.Priority.MEDIUM,
            Task.Importance.LOW,
            calendar.getTimeInMillis()
        ));
        
        // Low Priority, Medium Importance
        calendar.set(2024, Calendar.FEBRUARY, 15); // Due in 1 month
        tasks.add(new Task(
            "Update LinkedIn Profile",
            "Update professional LinkedIn profile with recent projects and achievements.",
            Task.Priority.LOW,
            Task.Importance.MEDIUM,
            calendar.getTimeInMillis()
        ));
        
        // Low Priority, Low Importance
        calendar.set(2024, Calendar.MARCH, 1); // Due in 2 months
        tasks.add(new Task(
            "Spring Cleaning",
            "Organize and clean up the workspace, including desk drawers and file cabinets.",
            Task.Priority.LOW,
            Task.Importance.LOW,
            calendar.getTimeInMillis()
        ));
        
        // High Priority, High Importance - Completed Task
        calendar.set(2024, Calendar.JANUARY, 10); // Past due (completed)
        Task completedTask = new Task(
            "Database Migration",
            "Migrate the production database to the new schema with zero downtime.",
            Task.Priority.HIGH,
            Task.Importance.HIGH,
            calendar.getTimeInMillis()
        );
        completedTask.setStatus(Task.Status.COMPLETED);
        tasks.add(completedTask);
        
        // Medium Priority, Medium Importance - Completed Task
        calendar.set(2024, Calendar.JANUARY, 8); // Past due (completed)
        Task completedTask2 = new Task(
            "Weekly Team Standup",
            "Conduct weekly team standup meeting and update project status dashboard.",
            Task.Priority.MEDIUM,
            Task.Importance.MEDIUM,
            calendar.getTimeInMillis()
        );
        completedTask2.setStatus(Task.Status.COMPLETED);
        tasks.add(completedTask2);
        
        // Low Priority, Low Importance - Completed Task
        calendar.set(2024, Calendar.JANUARY, 5); // Past due (completed)
        Task completedTask3 = new Task(
            "Coffee Machine Maintenance",
            "Schedule and complete routine maintenance on the office coffee machine.",
            Task.Priority.LOW,
            Task.Importance.LOW,
            calendar.getTimeInMillis()
        );
        completedTask3.setStatus(Task.Status.COMPLETED);
        tasks.add(completedTask3);
        
        return tasks;
    }
}
