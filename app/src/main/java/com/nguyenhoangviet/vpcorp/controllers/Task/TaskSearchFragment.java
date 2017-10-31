package com.nguyenhoangviet.vpcorp.controllers.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.nguyenhoangviet.vpcorp.controllers.FragmentOfMainActivity;
import com.nguyenhoangviet.vpcorp.core.model.SpinnerItemModel;
import com.nguyenhoangviet.vpcorp.core.views.widgets.CoreSpinner;
import com.nguyenhoangviet.vpcorp.models.Task;
import com.nguyenhoangviet.vpcorp.vnote2.R;

public class TaskSearchFragment extends FragmentOfMainActivity{

	public static final String	KEY_TASK_SEARCH_TEXT		= "task_search_by_text";
	public static final String	KEY_TASK_SEARCH_PRIORITY	= "task_search_by_priority";
	public static final String	KEY_TASK_SEARCH_STATUS		= "task_search_by_status";
	public static final String	KEY_TASK_SEARCH_CONDITION	= "task_search_condition";

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
		getMainActivity().footer.hide();
	}

	private void buildSearchOnClick(){
		getEditText(R.id.edt_fragment_task_search_text).requestFocus();
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
				activity.backToFragment(TaskListFragment.class, KEY_TASK_SEARCH_CONDITION, new HashMap<String, Object>());
			}
		});
	}

	private void buildPriority(){
		final LinearLayout lnrPriority = getLinearLayout(R.id.lnr_fragment_task_search_priority);

		CoreSpinner coreSpinnerPriority = (CoreSpinner)getView(R.id.spn_fragment_task_search_priority);
		coreSpinnerPriority.initSpinner(activity, getSpinnerItemModelsForPriority(), new CoreSpinner.OnSpinnerItemSelected() {

			@Override
			public void onSpinnerItemSelected(SpinnerItemModel spinnerItemModel){
				priority = spinnerItemModel.getName();
			}
		});
	}

	private List<SpinnerItemModel> getSpinnerItemModelsForPriority(){
		List<SpinnerItemModel> spinnerItemModelList = new ArrayList<SpinnerItemModel>();
		for(String priority : Task.TASK_PRIORITIES_WITH_ANY){
			SpinnerItemModel spinnerItemModel = new SpinnerItemModel(priority, priority);
			spinnerItemModelList.add(spinnerItemModel);
		}
		return spinnerItemModelList;
	}

	private List<SpinnerItemModel> getSpinnerItemModelsForStatus(){
		List<SpinnerItemModel> spinnerItemModelList = new ArrayList<SpinnerItemModel>();
		for(int i = 0; i < Task.STATUS.length; i++){
			SpinnerItemModel spinnerItemModel = new SpinnerItemModel(Task.STATUS[i], i);
			spinnerItemModelList.add(spinnerItemModel);
		}
		return spinnerItemModelList;
	}

	private void buildStatus(){
		CoreSpinner coreSpinnerStatus = (CoreSpinner)getView(R.id.spn_fragment_task_search_status);
		coreSpinnerStatus.initSpinner(activity, getSpinnerItemModelsForStatus(), new CoreSpinner.OnSpinnerItemSelected() {

			@Override
			public void onSpinnerItemSelected(SpinnerItemModel spinnerItemModel){
				taskStatus = (int)spinnerItemModel.originModel;
			}
		});
	}

	private void onClickSearchText(){
		hideSofeKeyboard();
		text = getEditText(R.id.edt_fragment_task_search_text).getText().toString();
		Map<String, Object> conditions = buildSearchConditions();
		activity.backToFragment(TaskListFragment.class, KEY_TASK_SEARCH_CONDITION, conditions);
	}

	private void buildPreCondition(){
		Map<String, Object> conditions = (Map<String, Object>)getUpdatedData(KEY_TASK_SEARCH_CONDITION, new HashMap<String, String>());
		if(conditions.containsKey(KEY_TASK_SEARCH_TEXT)){
			text = (String)conditions.get(KEY_TASK_SEARCH_TEXT);
			setTextFor(R.id.edt_fragment_task_search_text, text);
		}
		if(conditions.containsKey(KEY_TASK_SEARCH_PRIORITY)){
			priority = (String)conditions.get(KEY_TASK_SEARCH_PRIORITY);
			int position = Arrays.asList(Task.TASK_PRIORITIES_WITH_ANY).indexOf(priority);
			((CoreSpinner)getView(R.id.spn_fragment_task_search_priority)).setSelection(position);
		}
		if(conditions.containsKey(KEY_TASK_SEARCH_STATUS)){
			taskStatus = (int)conditions.get(KEY_TASK_SEARCH_STATUS);
			((CoreSpinner)getView(R.id.spn_fragment_task_search_status)).setSelection(taskStatus);
		}
	}

	private Map<String, Object> buildSearchConditions(){
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put(KEY_TASK_SEARCH_TEXT, text);
		conditions.put(KEY_TASK_SEARCH_PRIORITY, priority);
		conditions.put(KEY_TASK_SEARCH_STATUS, taskStatus);
		return conditions;
	}
}
