package com.example.assignment;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listViewTasks;
    TaskAdapter taskAdapter;
    ArrayList<TaskInfo> taskList;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewTasks = findViewById(R.id.listViewTasks);
        dbHelper = new DBHelper(this);

        taskList = dbHelper.getAllTasks();
        taskAdapter = new TaskAdapter(this, R.layout.activitytasks, taskList); // Update to use correct layout
        listViewTasks.setAdapter(taskAdapter);

        // Find the ImageButton and set OnClickListener
        ImageButton btnAddTask = findViewById(R.id.btnNew);
        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertNewTask();
            }
        });
    }

    private void insertNewTask() {
        // Get references to EditText and Spinner for task input
        EditText edtNewTask = findViewById(R.id.edtNewTask);
        Spinner spinnerCategory = findViewById(R.id.spinnerCategory);

        String taskName = edtNewTask.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();

        if (!taskName.isEmpty()) {
            boolean inserted = dbHelper.insertTaskData(taskName, category);
            if (inserted) {
                Toast.makeText(MainActivity.this, "Task added successfully", Toast.LENGTH_SHORT).show();
                updateTaskList();
            } else {
                Toast.makeText(MainActivity.this, "Failed to add task", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "Task name cannot be empty", Toast.LENGTH_SHORT).show();
        }
        edtNewTask.setText("");
    }
    private void updateTaskList() {
        // Update your ListView or RecyclerView here if needed
        taskList.clear();
        taskList.addAll(dbHelper.getAllTasks());
        taskAdapter.notifyDataSetChanged();
    }
}
