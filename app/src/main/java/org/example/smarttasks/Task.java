package org.example.smarttasks;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class Task {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private Priority priority;
    private Importance importance;
    private long dueDate; // timestamp
    private Status status;
    private long createdAt;

    public Task(String title, String description, Priority priority, Importance importance, long dueDate) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.importance = importance;
        this.dueDate = dueDate;
        this.status = Status.PENDING;
        this.createdAt = System.currentTimeMillis();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public Importance getImportance() { return importance; }
    public void setImportance(Importance importance) { this.importance = importance; }

    public long getDueDate() { return dueDate; }
    public void setDueDate(long dueDate) { this.dueDate = dueDate; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public enum Priority {
        LOW, MEDIUM, HIGH
    }

    public enum Importance {
        LOW, MEDIUM, HIGH
    }

    public enum Status {
        PENDING, COMPLETED
    }
}
