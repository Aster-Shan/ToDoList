package com.example.assignment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class TaskAdapter extends ArrayAdapter<TaskInfo> {
    Context context;
    int layoutResourceId;
    ArrayList<TaskInfo> data;
    DBHelper db;
    public TaskAdapter(Context context, int layoutResourceId, ArrayList<TaskInfo> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        db = new DBHelper(context);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TaskInfoHolder holder;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new TaskInfoHolder();
            holder.checkBox = row.findViewById(R.id.chkTask);
            holder.categoryView = row.findViewById(R.id.txtCategory);
            holder.editButton = row.findViewById(R.id.btnEdit);
            holder.deleteButton = row.findViewById(R.id.btnDelete);
            row.setTag(holder);
        } else {
            holder = (TaskInfoHolder) row.getTag();
        }
        TaskInfo taskInfo = data.get(position);
        holder.checkBox.setText(taskInfo.getTaskName());
        holder.checkBox.setChecked(taskInfo.isStatus());
        holder.categoryView.setText(taskInfo.getCategory());
        setCheckBoxDesign(holder.checkBox);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                taskInfo.setStatus(cb.isChecked());
                db.updateTaskData(taskInfo.getTaskName(), cb.isChecked() ? 1 : 0);
                setCheckBoxDesign(cb);
            }
        });
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(taskInfo);
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog(taskInfo);
            }
        });
        return row;
    }

    private void showEditDialog(final TaskInfo taskInfo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Task Name");
        final EditText input = new EditText(context);
        input.setText(taskInfo.getTaskName());
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newTaskName = input.getText().toString().trim();
                if (!newTaskName.isEmpty()) {
                    taskInfo.setTaskName(newTaskName);
                    notifyDataSetChanged();
                    db.updateTaskName(taskInfo.getTaskName(), newTaskName);
                    Toast.makeText(context, "Task name updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Task name cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void showConfirmDialog(final TaskInfo taskInfo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this item?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteData(taskInfo);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }private void deleteData(TaskInfo taskInfo) {
        db.deleteTaskData(taskInfo.getTaskName());
        data.remove(taskInfo);
        notifyDataSetChanged();
        Toast.makeText(context, "Task deleted", Toast.LENGTH_SHORT).show();
    }private void setCheckBoxDesign(CheckBox cb) {
        if (cb.isChecked()) {
            cb.setAlpha(0.3f);
            cb.setTypeface(null, Typeface.ITALIC);
            cb.setTextColor(Color.RED);
        } else {
            cb.setAlpha(1.0f);
            cb.setTypeface(null, Typeface.BOLD);
            cb.setTextColor(Color.BLUE);
        }
    }

    static class TaskInfoHolder {
        CheckBox checkBox;
        TextView categoryView;
        Button editButton;
        Button deleteButton;
    }
}
