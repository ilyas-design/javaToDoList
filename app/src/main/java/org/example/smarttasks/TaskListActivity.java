package org.example.smarttasks;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.example.smarttasks.sync.SyncManager;

import java.util.List;

public class TaskListActivity extends AppCompatActivity implements TaskAdapter.OnTaskClickListener {
    
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private TaskViewModel taskViewModel;
    private TaskAdapter adapter;
    private Task.Status currentFilter = Task.Status.PENDING; // Default filter
    private SyncManager syncManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        
        // Set the title
        setTitle("Smart Tasks");

        initializeViews();
        setupRecyclerView();
        setupClickListeners();
        setupViewModel();
        setupSyncManager();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);
    }

    private void setupRecyclerView() {
        adapter = new TaskAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupClickListeners() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskListActivity.this, AddEditTaskActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void setupViewModel() {
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(this, tasks -> {
            // Apply AI organization to tasks
            TaskOrganizerAI.organizeTasks(tasks);
            // Apply current filter
            List<Task> filteredTasks = filterTasks(tasks);
            adapter.setTasks(filteredTasks);
        });
    }

    private List<Task> filterTasks(List<Task> tasks) {
        if (currentFilter == null) {
            return tasks; // Show all tasks
        }
        return tasks.stream()
                .filter(task -> task.getStatus() == currentFilter)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public void onTaskClick(Task task) {
        Intent intent = new Intent(TaskListActivity.this, AddEditTaskActivity.class);
        intent.putExtra(AddEditTaskActivity.EXTRA_ID, task.getId());
        intent.putExtra(AddEditTaskActivity.EXTRA_TITLE, task.getTitle());
        intent.putExtra(AddEditTaskActivity.EXTRA_DESCRIPTION, task.getDescription());
        intent.putExtra(AddEditTaskActivity.EXTRA_PRIORITY, task.getPriority().name());
        intent.putExtra(AddEditTaskActivity.EXTRA_IMPORTANCE, task.getImportance().name());
        intent.putExtra(AddEditTaskActivity.EXTRA_DUE_DATE, task.getDueDate());
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditTaskActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditTaskActivity.EXTRA_DESCRIPTION);
            Task.Priority priority = Task.Priority.valueOf(data.getStringExtra(AddEditTaskActivity.EXTRA_PRIORITY));
            Task.Importance importance = Task.Importance.valueOf(data.getStringExtra(AddEditTaskActivity.EXTRA_IMPORTANCE));
            long dueDate = data.getLongExtra(AddEditTaskActivity.EXTRA_DUE_DATE, System.currentTimeMillis());

            Task task = new Task(title, description, priority, importance, dueDate);
            
            int id = data.getIntExtra(AddEditTaskActivity.EXTRA_ID, -1);
            if (id != -1) {
                task.setId(id);
                taskViewModel.update(task);
            } else {
                taskViewModel.insert(task);
            }
        }
    }

    @Override
    public void onTaskComplete(Task task) {
        taskViewModel.update(task);
    }

    @Override
    public void onTaskDelete(Task task) {
        taskViewModel.delete(task);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.task_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.filter_all) {
            currentFilter = null;
            updateTitle("All Tasks");
            refreshTasks();
            return true;
        } else if (item.getItemId() == R.id.filter_pending) {
            currentFilter = Task.Status.PENDING;
            updateTitle("Pending Tasks");
            refreshTasks();
            return true;
        } else if (item.getItemId() == R.id.filter_completed) {
            currentFilter = Task.Status.COMPLETED;
            updateTitle("Completed Tasks");
            refreshTasks();
            return true;
        } else if (item.getItemId() == R.id.sync_tasks) {
            performSync();
            return true;
        } else if (item.getItemId() == R.id.load_demo_data) {
            loadDemoData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateTitle(String title) {
        setTitle(title);
    }

    private void refreshTasks() {
        taskViewModel.getAllTasks().observe(this, tasks -> {
            TaskOrganizerAI.organizeTasks(tasks);
            List<Task> filteredTasks = filterTasks(tasks);
            adapter.setTasks(filteredTasks);
        });
    }

    private void setupSyncManager() {
        syncManager = SyncManager.getInstance(this);
        syncManager.setSyncCallback(new SyncManager.SyncCallback() {
            @Override
            public void onSyncSuccess(List<Task> tasks) {
                runOnUiThread(() -> {
                    Toast.makeText(TaskListActivity.this, "Sync successful!", Toast.LENGTH_SHORT).show();
                    // Update local database with synced tasks
                    for (Task task : tasks) {
                        taskViewModel.update(task);
                    }
                });
            }

            @Override
            public void onSyncError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(TaskListActivity.this, "Sync failed: " + error, Toast.LENGTH_LONG).show();
                });
            }

            @Override
            public void onSyncProgress(String message) {
                runOnUiThread(() -> {
                    Toast.makeText(TaskListActivity.this, message, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void performSync() {
        if (!taskViewModel.isOnline()) {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        taskViewModel.getAllTasks().observe(this, tasks -> {
            syncManager.syncTasks(tasks);
        });
    }

    private void loadDemoData() {
        Toast.makeText(this, "Loading demo data...", Toast.LENGTH_SHORT).show();
        taskViewModel.populateDummyData();
        Toast.makeText(this, "Demo data loaded successfully!", Toast.LENGTH_SHORT).show();
    }
}
