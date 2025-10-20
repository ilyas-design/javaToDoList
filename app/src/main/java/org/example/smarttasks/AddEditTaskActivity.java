package org.example.smarttasks;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEditTaskActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "org.example.smarttasks.EXTRA_ID";
    public static final String EXTRA_TITLE = "org.example.smarttasks.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "org.example.smarttasks.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "org.example.smarttasks.EXTRA_PRIORITY";
    public static final String EXTRA_IMPORTANCE = "org.example.smarttasks.EXTRA_IMPORTANCE";
    public static final String EXTRA_DUE_DATE = "org.example.smarttasks.EXTRA_DUE_DATE";

    private EditText editTextTitle;
    private EditText editTextDescription;
    private Spinner spinnerPriority;
    private Spinner spinnerImportance;
    private Button buttonDueDate;
    private long dueDate = System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        spinnerPriority = findViewById(R.id.spinner_priority);
        spinnerImportance = findViewById(R.id.spinner_importance);
        buttonDueDate = findViewById(R.id.button_due_date);

        ArrayAdapter<Task.Priority> priorityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Task.Priority.values());
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(priorityAdapter);

        ArrayAdapter<Task.Importance> importanceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Task.Importance.values());
        importanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerImportance.setAdapter(importanceAdapter);

        buttonDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Task");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            spinnerPriority.setSelection(priorityAdapter.getPosition(Task.Priority.valueOf(intent.getStringExtra(EXTRA_PRIORITY))));
            spinnerImportance.setSelection(importanceAdapter.getPosition(Task.Importance.valueOf(intent.getStringExtra(EXTRA_IMPORTANCE))));
            dueDate = intent.getLongExtra(EXTRA_DUE_DATE, System.currentTimeMillis());
            updateDueDateButton();
        } else {
            setTitle("Add Task");
            updateDueDateButton();
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dueDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, day1) -> {
            Calendar selectedCalendar = Calendar.getInstance();
            selectedCalendar.set(year1, month1, day1);
            dueDate = selectedCalendar.getTimeInMillis();
            updateDueDateButton();
        }, year, month, day);
        datePickerDialog.show();
    }

    private void updateDueDateButton() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        buttonDueDate.setText(sdf.format(new Date(dueDate)));
    }

    private void saveTask() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        Task.Priority priority = (Task.Priority) spinnerPriority.getSelectedItem();
        Task.Importance importance = (Task.Importance) spinnerImportance.getSelectedItem();

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_PRIORITY, priority.name());
        data.putExtra(EXTRA_IMPORTANCE, importance.name());
        data.putExtra(EXTRA_DUE_DATE, dueDate);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_task:
                saveTask();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
