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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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
    private AutoCompleteTextView spinnerPriority;
    private AutoCompleteTextView spinnerImportance;
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

        setupSpinners();
        setupClickListeners();
        loadTaskData();
    }

    private void setupSpinners() {
        ArrayAdapter<Task.Priority> priorityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, Task.Priority.values());
        spinnerPriority.setAdapter(priorityAdapter);

        ArrayAdapter<Task.Importance> importanceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, Task.Importance.values());
        spinnerImportance.setAdapter(importanceAdapter);
    }

    private void setupClickListeners() {
        buttonDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
    }

    private void loadTaskData() {
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Task");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            
            String priorityStr = intent.getStringExtra(EXTRA_PRIORITY);
            String importanceStr = intent.getStringExtra(EXTRA_IMPORTANCE);
            
            spinnerPriority.setText(priorityStr, false);
            spinnerImportance.setText(importanceStr, false);
            
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
        
        String priorityStr = spinnerPriority.getText().toString();
        String importanceStr = spinnerImportance.getText().toString();

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(priorityStr)) {
            Toast.makeText(this, "Please select a priority", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(importanceStr)) {
            Toast.makeText(this, "Please select an importance level", Toast.LENGTH_SHORT).show();
            return;
        }

        Task.Priority priority = Task.Priority.valueOf(priorityStr);
        Task.Importance importance = Task.Importance.valueOf(importanceStr);

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
        // Add save button to app bar
        MenuItem saveItem = menu.add(Menu.NONE, R.id.save_task, Menu.NONE, "Save");
        saveItem.setIcon(R.drawable.ic_save);
        saveItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_task) {
            saveTask();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
