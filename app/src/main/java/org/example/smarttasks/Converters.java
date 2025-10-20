package org.example.smarttasks;

import androidx.room.TypeConverter;

public class Converters {
    @TypeConverter
    public static Task.Priority fromPriorityString(String value) {
        return value == null ? null : Task.Priority.valueOf(value);
    }

    @TypeConverter
    public static String priorityToString(Task.Priority priority) {
        return priority == null ? null : priority.name();
    }

    @TypeConverter
    public static Task.Importance fromImportanceString(String value) {
        return value == null ? null : Task.Importance.valueOf(value);
    }

    @TypeConverter
    public static String importanceToString(Task.Importance importance) {
        return importance == null ? null : importance.name();
    }

    @TypeConverter
    public static Task.Status fromStatusString(String value) {
        return value == null ? null : Task.Status.valueOf(value);
    }

    @TypeConverter
    public static String statusToString(Task.Status status) {
        return status == null ? null : status.name();
    }
}
