package org.example.smarttasks.sync;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import org.example.smarttasks.Task;
import org.example.smarttasks.api.ApiClient;
import org.example.smarttasks.api.TaskApiModel;
import org.example.smarttasks.api.TaskApiService;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncManager {
    private static final String TAG = "SyncManager";
    private static SyncManager instance;
    private Context context;
    private TaskApiService apiService;
    private SyncCallback callback;

    public interface SyncCallback {
        void onSyncSuccess(List<Task> tasks);
        void onSyncError(String error);
        void onSyncProgress(String message);
    }

    private SyncManager(Context context) {
        this.context = context.getApplicationContext();
        this.apiService = ApiClient.getInstance().getApiService();
    }

    public static synchronized SyncManager getInstance(Context context) {
        if (instance == null) {
            instance = new SyncManager(context);
        }
        return instance;
    }

    public void setSyncCallback(SyncCallback callback) {
        this.callback = callback;
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager = 
            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public void syncTasks(List<Task> localTasks) {
        if (!isOnline()) {
            if (callback != null) {
                callback.onSyncError("No internet connection");
            }
            return;
        }

        if (callback != null) {
            callback.onSyncProgress("Starting sync...");
        }

        // Download tasks from server
        downloadTasksFromServer(localTasks);
    }

    private void downloadTasksFromServer(List<Task> localTasks) {
        Call<List<TaskApiModel>> call = apiService.getAllTasks();
        call.enqueue(new Callback<List<TaskApiModel>>() {
            @Override
            public void onResponse(Call<List<TaskApiModel>> call, Response<List<TaskApiModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Task> serverTasks = convertApiModelsToTasks(response.body());
                    List<Task> mergedTasks = mergeTasks(localTasks, serverTasks);
                    
                    if (callback != null) {
                        callback.onSyncSuccess(mergedTasks);
                    }
                } else {
                    if (callback != null) {
                        callback.onSyncError("Failed to download tasks from server");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TaskApiModel>> call, Throwable t) {
                Log.e(TAG, "Sync failed", t);
                if (callback != null) {
                    callback.onSyncError("Network error: " + t.getMessage());
                }
            }
        });
    }

    public void uploadTask(Task task) {
        if (!isOnline()) {
            Log.w(TAG, "Cannot upload task - no internet connection");
            return;
        }

        TaskApiModel apiModel = convertTaskToApiModel(task);
        Call<TaskApiModel> call = apiService.createTask(apiModel);
        
        call.enqueue(new Callback<TaskApiModel>() {
            @Override
            public void onResponse(Call<TaskApiModel> call, Response<TaskApiModel> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Task uploaded successfully");
                } else {
                    Log.e(TAG, "Failed to upload task: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<TaskApiModel> call, Throwable t) {
                Log.e(TAG, "Upload failed", t);
            }
        });
    }

    public void updateTaskOnServer(Task task) {
        if (!isOnline()) {
            Log.w(TAG, "Cannot update task - no internet connection");
            return;
        }

        TaskApiModel apiModel = convertTaskToApiModel(task);
        Call<TaskApiModel> call = apiService.updateTask(task.getId(), apiModel);
        
        call.enqueue(new Callback<TaskApiModel>() {
            @Override
            public void onResponse(Call<TaskApiModel> call, Response<TaskApiModel> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Task updated successfully");
                } else {
                    Log.e(TAG, "Failed to update task: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<TaskApiModel> call, Throwable t) {
                Log.e(TAG, "Update failed", t);
            }
        });
    }

    public void deleteTaskOnServer(int taskId) {
        if (!isOnline()) {
            Log.w(TAG, "Cannot delete task - no internet connection");
            return;
        }

        Call<Void> call = apiService.deleteTask(taskId);
        
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Task deleted successfully");
                } else {
                    Log.e(TAG, "Failed to delete task: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Delete failed", t);
            }
        });
    }

    private List<Task> convertApiModelsToTasks(List<TaskApiModel> apiModels) {
        List<Task> tasks = new ArrayList<>();
        for (TaskApiModel apiModel : apiModels) {
            Task task = new Task(
                apiModel.getTitle(),
                apiModel.getDescription(),
                Task.Priority.valueOf(apiModel.getPriority()),
                Task.Importance.valueOf(apiModel.getImportance()),
                apiModel.getDueDate()
            );
            task.setId(apiModel.getId());
            task.setStatus(Task.Status.valueOf(apiModel.getStatus()));
            task.setCreatedAt(apiModel.getCreatedAt());
            tasks.add(task);
        }
        return tasks;
    }

    private TaskApiModel convertTaskToApiModel(Task task) {
        TaskApiModel apiModel = new TaskApiModel();
        apiModel.setId(task.getId());
        apiModel.setTitle(task.getTitle());
        apiModel.setDescription(task.getDescription());
        apiModel.setPriority(task.getPriority().name());
        apiModel.setImportance(task.getImportance().name());
        apiModel.setDueDate(task.getDueDate());
        apiModel.setStatus(task.getStatus().name());
        apiModel.setCreatedAt(task.getCreatedAt());
        apiModel.setUpdatedAt(System.currentTimeMillis());
        return apiModel;
    }

    private List<Task> mergeTasks(List<Task> localTasks, List<Task> serverTasks) {
        // Simple merge strategy: server wins for conflicts
        // In a real app, you'd implement more sophisticated conflict resolution
        List<Task> mergedTasks = new ArrayList<>(serverTasks);
        
        // Add local tasks that don't exist on server
        for (Task localTask : localTasks) {
            boolean existsOnServer = false;
            for (Task serverTask : serverTasks) {
                if (localTask.getId() == serverTask.getId()) {
                    existsOnServer = true;
                    break;
                }
            }
            if (!existsOnServer) {
                mergedTasks.add(localTask);
            }
        }
        
        return mergedTasks;
    }
}
