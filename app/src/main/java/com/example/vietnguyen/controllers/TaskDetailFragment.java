package com.example.vietnguyen.controllers;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vietnguyen.core.controllers.MyFragment;
import com.example.vietnguyen.models.Task;
import com.example.vietnguyen.models.Task;
import com.example.vietnguyen.myapplication.R;

public class TaskDetailFragment extends MyFragment{
    private Task task;
    public static final String BUNDLE_KEY_TASK = "TASK";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_task_detail, container, false);
	}

	@Override
	protected void buildLayout(){
        super.buildLayout();
        TextView txtDone = getTextView(R.id.txt_edit);
        txtDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                gotoEdit();
            }
        });
        ImageView imgBack = getImageView(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                backToTaskList();
            }
        });

        TextView txtName = getTextView(R.id.txt_name);
        txtName.setText(this.task.name);
        TextView txtDescription = getTextView(R.id.txt_description);
        txtDescription.setText(this.task.description);
	}

    private void gotoEdit() {
        TaskAddFragment fragment = new TaskAddFragment();
        fragment.setEdit(true, this.task);
        activity.addFragment(fragment);
    }

    private void backToTaskList(){
        activity.backOneFragment();
    }

	@Override
	public void onApiResponse(String url, JSONObject response){

	}

    public void setTask(Task task) {
        this.task = task;
    }
}
