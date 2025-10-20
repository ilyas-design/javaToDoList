package org.example.smarttasks;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.example.smarttasks.sync.SyncManager;

public class TaskRepository {
    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;
    private ExecutorService executorService;
    private SyncManager syncManager;

    public TaskRepository(Application application) {
        TaskDatabase database = TaskDatabase.getInstance(application);
        taskDao = database.taskDao();
        allTasks = taskDao.getAllTasks();
        executorService = Executors.newSingleThreadExecutor();
        syncManager = SyncManager.getInstance(application);
    }

    public void insert(Task task) {
        executorService.execute(() -> {
            taskDao.insert(task);
            // Sync to server
            syncManager.uploadTask(task);
        });
    }

    public void update(Task task) {
        executorService.execute(() -> {
            taskDao.update(task);
            // Sync to server
            syncManager.updateTaskOnServer(task);
        });
    }

    public void delete(Task task) {
        executorService.execute(() -> {
            taskDao.delete(task);
            // Sync to server
            syncManager.deleteTaskOnServer(task.getId());
        });
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public LiveData<Task> getTaskById(int id) {
        return taskDao.getTaskById(id);
    }

    public void syncTasks(List<Task> localTasks) {
        syncManager.syncTasks(localTasks);
    }

    public boolean isOnline() {
        return syncManager.isOnline();
    }

    public void populateDummyData() {
        executorService.execute(() -> {
            // Clear existing tasks
            taskDao.deleteAllTasks();
            
            // Generate and insert dummy tasks
            List<Task> dummyTasks = DummyDataGenerator.generateDummyTasks();
            for (Task task : dummyTasks) {
                taskDao.insert(task);
            }
        });
    }
}
