package org.example.smarttasks;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class TaskTest {

    @Test
    public void testTaskCreation() {
        String title = "Test Task";
        String description = "Test Description";
        Task.Priority priority = Task.Priority.HIGH;
        Task.Importance importance = Task.Importance.MEDIUM;
        long dueDate = System.currentTimeMillis();

        Task task = new Task(title, description, priority, importance, dueDate);

        assertEquals(title, task.getTitle());
        assertEquals(description, task.getDescription());
        assertEquals(priority, task.getPriority());
        assertEquals(importance, task.getImportance());
        assertEquals(dueDate, task.getDueDate());
        assertEquals(Task.Status.PENDING, task.getStatus());
        assertNotNull(task.getCreatedAt());
    }

    @Test
    public void testTaskSetters() {
        Task task = new Task("Title", "Desc", Task.Priority.LOW, Task.Importance.LOW, 0);

        task.setId(1);
        task.setTitle("New Title");
        task.setDescription("New Desc");
        task.setPriority(Task.Priority.HIGH);
        task.setImportance(Task.Importance.HIGH);
        task.setDueDate(123456);
        task.setStatus(Task.Status.COMPLETED);
        task.setCreatedAt(789012);

        assertEquals(1, task.getId());
        assertEquals("New Title", task.getTitle());
        assertEquals("New Desc", task.getDescription());
        assertEquals(Task.Priority.HIGH, task.getPriority());
        assertEquals(Task.Importance.HIGH, task.getImportance());
        assertEquals(123456, task.getDueDate());
        assertEquals(Task.Status.COMPLETED, task.getStatus());
        assertEquals(789012, task.getCreatedAt());
    }
}
