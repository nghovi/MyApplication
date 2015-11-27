package com.example.vietnguyen.controllers.Task;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.vietnguyen.core.controllers.DialogBuilder;
import com.example.vietnguyen.core.controllers.MyFragment;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.models.Task;
import com.example.vietnguyen.myapplication.R;

public class TaskSearchFragment extends MyFragment{

	public static final String	KEY_TASK_SEARCH_RESULT		= "task_search_result";
	public static final String	KEY_TASK_SEARCH_FLAG		= "task_search_flag";
	// public static final String KEY_TASK_SEARCH_LIST = "task_list";
	public static final String	KEY_TASK_SEARCH_TEXT		= "task_search_by_text";
	public static final String	KEY_TASK_SEARCH_PRIORITY	= "task_search_by_priority";
	public static final String	KEY_TASK_SEARCH_STATUS		= "task_search_by_status";
	public static final String	KEY_TASK_SEARCH_CONDITION	= "task_search_condition";

	private List<Task>			tasks;
	private String				priority					= Task.TASK_PRIORITIES_WITH_ANY[0];
	private int					taskStatus					= Task.STATUS_ANY;
	private String				text;															// search key word

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_task_search, container, false);
	}

	@Override
	protected void buildLayout(){
		super.buildLayout();
		buildSearchOnClick();
		buildCancelOnClick();
		buildPriority();
		buildStatus();
		buildPreCondition();
	}

	private void buildPreCondition(){
		Map<String, Object> conditions = (Map<String, Object>)getUpdatedData(KEY_TASK_SEARCH_CONDITION, new HashMap<String, String>());

		if(conditions.containsKey(KEY_TASK_SEARCH_TEXT)){
			text = (String)conditions.get(KEY_TASK_SEARCH_TEXT);
			setTextFor(R.id.edt_fragment_task_search_text, text);
		}
		if(conditions.containsKey(KEY_TASK_SEARCH_PRIORITY)){
			priority = (String)conditions.get(KEY_TASK_SEARCH_PRIORITY);
			setTextFor(R.id.txt_fragment_task_search_priority, priority);
		}
		if(conditions.containsKey(KEY_TASK_SEARCH_STATUS)){
			taskStatus = (int)conditions.get(KEY_TASK_SEARCH_STATUS);
			setTextFor(R.id.txt_fragment_task_search_status, Task.STATUS[taskStatus]);
		}
	}

	private void buildSearchOnClick(){
		setOnClickFor(R.id.txt_fragment_task_search_search, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				onClickSearchText();
			}
		});
	}

	private void buildCancelOnClick(){
		setOnClickFor(R.id.txt_fragment_task_search_cancel, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				setTextFor(R.id.edt_fragment_task_search_text, "");
				priority = Task.TASK_PRIORITIES_WITH_ANY[0];
				taskStatus = Task.STATUS_ANY;
				onClickSearchText();
			}
		});
	}

	private void buildPriority(){
		final LinearLayout lnrPriority = getLinearLayout(R.id.lnr_fragment_task_search_priority);
		final DialogBuilder.OnNumberPickerBtnOkClickListener listener = new DialogBuilder.OnNumberPickerBtnOkClickListener() {

			@Override
			public void onClick(int selectedValue, String displayedValue){
				priority = displayedValue;
				setTextFor(R.id.txt_fragment_task_search_priority, String.valueOf(displayedValue));
			}
		};
		lnrPriority.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				dlgBuilder.buildDialogNumberPicker(activity, "Please choose priority", Task.TASK_PRIORITIES_WITH_ANY, listener, 0).show();
			}
		});
	}

	private void buildStatus(){
		setOnClickFor(R.id.lnr_fragment_task_search_status, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				dlgBuilder.build3OptsDlgTopDown("Unfinished", "Finished", "Any", new DialogBuilder.OnNumberPickerBtnOkClickListener() {

					@Override
					public void onClick(int selectedValue, String displayedValue){
						setTextFor(R.id.txt_fragment_task_search_status, displayedValue);
						taskStatus = selectedValue;
					}
				}).show();
			}
		});
	}

	private void onClickSearchText(){
		text = getEditText(R.id.edt_fragment_task_search_text).getText().toString();
		Map<String, Object> conditions = buildSearchConditions();
		tasks = AbstractTaskFragment.searchWithConditions(conditions);
		activity.backToFragment(TaskListFragment.class, KEY_TASK_SEARCH_RESULT, buildSearchResult(conditions));
	}

	private Map<String, Object> buildSearchResult(Map<String, Object> conditions){
		boolean hasFiltered = !MU.isEmpty(text) || priority != Task.TASK_PRIORITIES_WITH_ANY[0] || taskStatus != Task.STATUS_ANY;
		Map searchResult = new HashMap<String, Object>();
		searchResult.put(KEY_TASK_SEARCH_FLAG, hasFiltered);
		searchResult.put(KEY_TASK_SEARCH_CONDITION, conditions);
		return searchResult;
	}

	private Map<String, Object> buildSearchConditions(){
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put(KEY_TASK_SEARCH_TEXT, text);
		conditions.put(KEY_TASK_SEARCH_PRIORITY, priority);
		conditions.put(KEY_TASK_SEARCH_STATUS, taskStatus);
		return conditions;
	}

}
