package com.example.assignment;

public class TaskInfo {
    private String taskName;
    private boolean status;
    private String category;

    public TaskInfo(String taskName, boolean status, String category) {
        this.taskName = taskName;
        this.status = status;
        this.category = category;
    }
    public String getTaskName() {
        return taskName;
    }
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
}
