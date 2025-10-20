package org.example.smarttasks;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> tasks;
    private OnTaskClickListener listener;

    public interface OnTaskClickListener {
        void onTaskClick(Task task);
        void onTaskComplete(Task task);
        void onTaskDelete(Task task);
    }

    public TaskAdapter(OnTaskClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task currentTask = tasks.get(position);
        holder.bind(currentTask);
    }

    @Override
    public int getItemCount() {
        return tasks != null ? tasks.size() : 0;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView taskTitle;
        private TextView taskDescription;
        private TextView taskPriority;
        private TextView taskDueDate;
        private CheckBox taskCheckbox;
        private ImageButton deleteButton;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.taskTitle);
            taskDescription = itemView.findViewById(R.id.taskDescription);
            taskPriority = itemView.findViewById(R.id.taskPriority);
            taskDueDate = itemView.findViewById(R.id.taskDueDate);
            taskCheckbox = itemView.findViewById(R.id.taskCheckbox);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            // Edit task on click
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onTaskClick(tasks.get(position));
                }
            });

            // Complete task on checkbox change
            taskCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    Task task = tasks.get(position);
                    task.setStatus(isChecked ? Task.Status.COMPLETED : Task.Status.PENDING);
                    listener.onTaskComplete(task);
                }
            });

            // Delete task on delete button click
            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onTaskDelete(tasks.get(position));
                }
            });
        }

        public void bind(Task task) {
            taskTitle.setText(task.getTitle());
            taskDescription.setText(task.getDescription());
            taskPriority.setText(task.getPriority().name());
            
            // Set checkbox state
            taskCheckbox.setChecked(task.getStatus() == Task.Status.COMPLETED);
            
            // Visual styling for completed tasks
            if (task.getStatus() == Task.Status.COMPLETED) {
                taskTitle.setAlpha(0.6f);
                taskDescription.setAlpha(0.6f);
                taskPriority.setAlpha(0.6f);
                taskDueDate.setAlpha(0.6f);
            } else {
                taskTitle.setAlpha(1.0f);
                taskDescription.setAlpha(1.0f);
                taskPriority.setAlpha(1.0f);
                taskDueDate.setAlpha(1.0f);
            }
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String dueDateText = "Due: " + sdf.format(new Date(task.getDueDate()));
            
            // AI-powered urgency indicator
            if (TaskOrganizerAI.isTaskUrgent(task)) {
                dueDateText += " ⚠️ URGENT";
                taskDueDate.setTextColor(taskDueDate.getContext().getResources().getColor(android.R.color.holo_red_dark));
            } else {
                taskDueDate.setTextColor(taskDueDate.getContext().getResources().getColor(R.color.text_secondary));
            }
            
            taskDueDate.setText(dueDateText);
        }
    }
}
