package com.example.assignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Tododata.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, "Tododata.db", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the tasks table with an additional category column
        db.execSQL("CREATE TABLE tasks(task TEXT PRIMARY KEY, status INTEGER DEFAULT 0, category TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the old table if it exists and create a new one
        db.execSQL("DROP TABLE IF EXISTS tasks");
        onCreate(db);
    }

    // Insert a new task with a specified category
    public boolean insertTaskData(String task, String category) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("task", task);
            contentValues.put("category", category);
            long result = db.insert("tasks", null, contentValues);
            return result != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    // Update the status of an existing task
    public boolean updateTaskData(String task, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);

        int result = db.update("tasks", contentValues, "task=?", new String[]{task});
        db.close();  // Close the database connection

        return result > 0;
    }

    // Update the name of an existing task
    public boolean updateTaskName(String oldTaskName, String newTaskName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("task", newTaskName);

        int result = db.update("tasks", contentValues, "task=?", new String[]{oldTaskName});
        db.close();  // Close the database connection

        return result > 0;
    }

    // Delete an existing task
    public boolean deleteTaskData(String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("tasks", "task=?", new String[]{task});
        db.close();  // Close the database connection

        return result > 0;
    }

    // Select all tasks from the database
    public ArrayList<TaskInfo> getAllTasks() {
        ArrayList<TaskInfo> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM tasks", null);
        if (cursor.moveToFirst()) {
            do {
                String taskName = cursor.getString(cursor.getColumnIndex("task"));
                int status = cursor.getInt(cursor.getColumnIndex("status"));
                String category = cursor.getString(cursor.getColumnIndex("category"));

                TaskInfo taskInfo = new TaskInfo(taskName, status == 1, category);
                taskList.add(taskInfo);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();  // Close the database connection

        return taskList;
    }
}
