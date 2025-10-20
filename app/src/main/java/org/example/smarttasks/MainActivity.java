package org.example.smarttasks;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Skip the intro page and go directly to task list
        Intent intent = new Intent(MainActivity.this, TaskListActivity.class);
        startActivity(intent);
        finish(); // Close the intro activity
    }
}
