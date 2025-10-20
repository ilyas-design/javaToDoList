package org.example.smarttasks.api;

import com.google.gson.annotations.SerializedName;

public class TaskApiModel {
    @SerializedName("id")
    private int id;
    
    @SerializedName("title")
    private String title;
    
    @SerializedName("description")
    private String description;
    
    @SerializedName("priority")
    private String priority;
    
    @SerializedName("importance")
    private String importance;
    
    @SerializedName("dueDate")
    private long dueDate;
    
    @SerializedName("status")
    private String status;
    
    @SerializedName("createdAt")
    private long createdAt;
    
    @SerializedName("updatedAt")
    private long updatedAt;

    // Constructors
    public TaskApiModel() {}

    public TaskApiModel(int id, String title, String description, String priority, 
                       String importance, long dueDate, String status, 
                       long createdAt, long updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.importance = importance;
        this.dueDate = dueDate;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getImportance() { return importance; }
    public void setImportance(String importance) { this.importance = importance; }

    public long getDueDate() { return dueDate; }
    public void setDueDate(long dueDate) { this.dueDate = dueDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
}
