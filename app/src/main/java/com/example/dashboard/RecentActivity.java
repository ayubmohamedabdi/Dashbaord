package com.example.dashboard;

public class RecentActivity {
    private String taskName;
    private String timeAgo;
    private int iconResId; // Add this field for icons

    public RecentActivity(String taskName, String timeAgo, int iconResId) {
        this.taskName = taskName;
        this.timeAgo = timeAgo;
        this.iconResId = iconResId;
    }

    // Getters
    public String getTaskName() {
        return taskName;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public int getIconResId() {
        return iconResId;
    }
}
