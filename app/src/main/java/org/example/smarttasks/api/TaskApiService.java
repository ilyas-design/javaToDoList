package org.example.smarttasks.api;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TaskApiService {
    
    @GET("tasks")
    Call<List<TaskApiModel>> getAllTasks();
    
    @GET("tasks/{id}")
    Call<TaskApiModel> getTaskById(@Path("id") int id);
    
    @POST("tasks")
    Call<TaskApiModel> createTask(@Body TaskApiModel task);
    
    @PUT("tasks/{id}")
    Call<TaskApiModel> updateTask(@Path("id") int id, @Body TaskApiModel task);
    
    @DELETE("tasks/{id}")
    Call<Void> deleteTask(@Path("id") int id);
    
    @GET("tasks/sync")
    Call<List<TaskApiModel>> getTasksSince(@Path("timestamp") long timestamp);
}
