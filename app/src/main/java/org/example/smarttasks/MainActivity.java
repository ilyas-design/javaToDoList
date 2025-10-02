package org.example.smarttasks;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private MaterialButton getStartedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        getStartedButton = findViewById(R.id.getStartedButton);
    }

    private void setupClickListeners() {
        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Navigate to main task list activity
                Toast.makeText(MainActivity.this, "Welcome to Smart Tasks!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
