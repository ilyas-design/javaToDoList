package org.example.smarttasks.api;

import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/** Hugging Face Inference API subset for zero-shot classification */
public interface TaskApiService {

    /**
     * POST /models/{model}
     * Body example:
     * { "inputs": "text", "parameters": { "candidate_labels": ["HIGH","MEDIUM","LOW"] } }
     */
    @POST("{modelPath}")
    Call<List<List<Map<String, Object>>>> zeroShot(
            @Header("Authorization") String bearer,
            @retrofit2.http.Path("modelPath") String modelPath,
            @Body Map<String, Object> body
    );
}
