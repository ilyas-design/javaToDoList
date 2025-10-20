package org.example.smarttasks.api;

import org.example.smarttasks.BuildConfig;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/** Lightweight client to get importance/urgency predictions from Hugging Face. */
public class HfClient {

    public interface ScoreCallback {
        void onScores(Double importanceHighProb, Double urgencyHighProb);
        void onError(String message);
    }

    private final TaskApiService service;

    public HfClient() {
        this.service = ApiClient.getInstance().getApiService();
    }

    /**
     * Calls zero-shot classification with candidate labels and returns HIGH probabilities.
     */
    public void scoreImportanceAndUrgency(String text, ScoreCallback cb) {
        if (!BuildConfig.HF_ENABLED) {
            cb.onError("HF disabled");
            return;
        }

        Map<String, Object> body = new HashMap<>();
        body.put("inputs", text);
        Map<String, Object> params = new HashMap<>();
        params.put("candidate_labels", new String[]{"HIGH", "MEDIUM", "LOW"});
        body.put("parameters", params);

        // Importance request
        Call<List<List<Map<String, Object>>>> callImp = service.zeroShot(
                "Bearer " + BuildConfig.HF_API_TOKEN,
                BuildConfig.HF_MODEL_PATH,
                body
        );

        callImp.enqueue(new Callback<List<List<Map<String, Object>>>>() {
            @Override
            public void onResponse(Call<List<List<Map<String, Object>>>> call, Response<List<List<Map<String, Object>>>> response) {
                Double impHigh = extractHigh(response.body());

                // Urgency: reuse same request; in a real setup we may use a different prompt or labels
                Call<List<List<Map<String, Object>>>> callUrg = service.zeroShot(
                        "Bearer " + BuildConfig.HF_API_TOKEN,
                        BuildConfig.HF_MODEL_PATH,
                        body
                );

                callUrg.enqueue(new Callback<List<List<Map<String, Object>>>>() {
                    @Override
                    public void onResponse(Call<List<List<Map<String, Object>>>> call2, Response<List<List<Map<String, Object>>>> resp2) {
                        Double urgHigh = extractHigh(resp2.body());
                        cb.onScores(impHigh, urgHigh);
                    }

                    @Override
                    public void onFailure(Call<List<List<Map<String, Object>>>> call2, Throwable t2) {
                        cb.onError(t2.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(Call<List<List<Map<String, Object>>>> call, Throwable t) {
                cb.onError(t.getMessage());
            }
        });
    }

    private Double extractHigh(List<List<Map<String, Object>>> body) {
        try {
            if (body == null || body.isEmpty()) return null;
            List<Map<String, Object>> results = body.get(0);
            for (Map<String, Object> item : results) {
                if ("HIGH".equalsIgnoreCase(String.valueOf(item.get("label")))) {
                    Object scoreObj = item.get("score");
                    if (scoreObj instanceof Number) return ((Number) scoreObj).doubleValue();
                }
            }
        } catch (Exception ignored) {}
        return null;
    }
}


