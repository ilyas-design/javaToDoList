package org.example.smarttasks;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class TaskOrganizerAITest {

    @Test
    public void testOrganizeTasksByImportance() {
        List<Task> tasks = new ArrayList<>();
        Task task1 = new Task("Low importance", "Desc", Task.Priority.HIGH, Task.Importance.LOW, System.currentTimeMillis());
        Task task2 = new Task("High importance", "Desc", Task.Priority.LOW, Task.Importance.HIGH, System.currentTimeMillis());
        tasks.add(task1);
        tasks.add(task2);

        TaskOrganizerAI.organizeTasks(tasks);

        assertEquals(Task.Importance.HIGH, tasks.get(0).getImportance());
        assertEquals(Task.Importance.LOW, tasks.get(1).getImportance());
    }

    @Test
    public void testOrganizeTasksByPriority() {
        List<Task> tasks = new ArrayList<>();
        Task task1 = new Task("Low priority", "Desc", Task.Priority.LOW, Task.Importance.HIGH, System.currentTimeMillis());
        Task task2 = new Task("High priority", "Desc", Task.Priority.HIGH, Task.Importance.HIGH, System.currentTimeMillis());
        tasks.add(task1);
        tasks.add(task2);

        TaskOrganizerAI.organizeTasks(tasks);

        assertEquals(Task.Priority.HIGH, tasks.get(0).getPriority());
        assertEquals(Task.Priority.LOW, tasks.get(1).getPriority());
    }

    @Test
    public void testOrganizeTasksByDueDate() {
        List<Task> tasks = new ArrayList<>();
        long now = System.currentTimeMillis();
        Task task1 = new Task("Later due", "Desc", Task.Priority.HIGH, Task.Importance.HIGH, now + 10000);
        Task task2 = new Task("Earlier due", "Desc", Task.Priority.HIGH, Task.Importance.HIGH, now);
        tasks.add(task1);
        tasks.add(task2);

        TaskOrganizerAI.organizeTasks(tasks);

        assertEquals("Earlier due", tasks.get(0).getTitle());
        assertEquals("Later due", tasks.get(1).getTitle());
    }
}
