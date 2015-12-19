package com.example.vietnguyen.controllers.Task;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vietnguyen.core.controller.DateTimePicker;
import com.example.vietnguyen.core.controller.DialogBuilder;
import com.example.vietnguyen.core.controller.MyFragment;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.core.views.widgets.DatePickerFragment;
import com.example.vietnguyen.models.MyModel;
import com.example.vietnguyen.models.Notice;
import com.example.vietnguyen.models.Task;
import com.example.vietnguyen.myapplication.R;
import com.example.vietnguyen.utils.GcmUtil;
import com.example.vietnguyen.views.widgets.notifications.adapters.adapters.NoticeAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AbstractTaskFragment extends MyFragment{

	protected Date			targetDate;
	protected Task			task;
	protected List<Notice>	savedNotices	= new ArrayList<Notice>();
	protected int			priority		= 1;
	protected int			status			= 0;
	private NoticeAdapter	adapter;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		targetDate = new Date();
	}

	@Override
	protected void buildLayout(){
		super.buildLayout();
		prepareTask();
		buildHeaderText();
		buildCalendarPicker();
		builPriority();
		buildStatus();
		buildEditText();
		buildNotices();
	}

	protected void builPriority(){
		priority = task.priority;
		setTextFor(R.id.txt_share_task_edit_priority, String.valueOf(priority));
		final DialogBuilder.OnNumberPickerBtnOkClickListener listener = new DialogBuilder.OnNumberPickerBtnOkClickListener() {

			@Override
			public void onClick(int selectedValue, String displayedValue){
				priority = Integer.valueOf(displayedValue);
				setTextFor(R.id.txt_share_task_edit_priority, displayedValue);
			}
		};
		setOnClickFor(R.id.lnr_share_task_edit_priority, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				dlgBuilder.buildDialogNumberPicker(activity, getString(R.string.fragment_task_list_dlg_choose_priority_title), Task.TASK_PRIORITIES, listener, Arrays.asList(Task.TASK_PRIORITIES).indexOf(String.valueOf(task.priority))).show();
			}
		});
	}

	protected void buildStatus(){
		status = task.status;
		setTextFor(R.id.txt_share_task_edit_status, Task.STATUS[status]);
		setOnClickFor(R.id.lnr_share_task_edit_status, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				showTaskStatusChoosingDialog();
			}
		});
	}

	protected void buildEditText(){
		EditText edtName = getEditText(R.id.edt_share_task_edit_name);
		EditText edtDescription = getEditText(R.id.edt_share_task_edit_description);
		EditText edtComment = getEditText(R.id.edt_share_task_edit_comment);
		edtName.setText(this.task.name);
		edtDescription.setText(this.task.description);
		edtComment.setText(this.task.comment);
	}

	protected void prepareTask(){
	}

	protected void buildHeaderText(){
	}

	@Override
	protected void onClickBackBtn(){
		backToTaskList();
	}

	protected void showTaskStatusChoosingDialog(){
		dlgBuilder.build2OptsDlgTopDown(getString(R.string.fragment_task_mark_tas_status_unfinished), getString(R.string.fragment_task_mark_tas_status_finished), new View.OnClickListener() {

			@Override
			public void onClick(View view){
				status = Task.STATUS_UNFINISHED;
				setTextFor(R.id.txt_share_task_edit_status, Task.STATUS[Task.STATUS_UNFINISHED]);
			}
		}, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				status = Task.STATUS_FINISHED;
				setTextFor(R.id.txt_share_task_edit_status, Task.STATUS[Task.STATUS_FINISHED]);
			}
		}).show();
	}

	protected void buildTaskFromLayout(){
		task.name = getEditText(R.id.edt_share_task_edit_name).getText().toString();
		task.description = getEditText(R.id.edt_share_task_edit_description).getText().toString();
		task.comment = getEditText(R.id.edt_share_task_edit_comment).getText().toString();
		task.priority = priority;
		task.status = status;
		task.date = targetDate;
		task.lastupdated = targetDate;
		task.save();
	}

	protected void buildCalendarPicker(){
		final TextView txtDate = getTextView(R.id.txt_share_task_edit_date);
		targetDate = task.date;
		txtDate.setText(MU.getDateForDisplaying(targetDate));
		View.OnClickListener listener = new View.OnClickListener() {

			@Override
			public void onClick(View view){
				DatePickerFragment datePicker = new DatePickerFragment();
				datePicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker datePicker, int i, int i2, int i3){
						Calendar c = Calendar.getInstance();
						c.set(i, i2, i3);
						targetDate = c.getTime();
						txtDate.setText(MU.getDateForDisplaying(targetDate));
						task.save();
					}
				});
				datePicker.show(activity.getFragmentManager(), "datePicker");
			}
		};
		setOnClickFor(R.id.lnr_share_task_edit_date, listener);
		setOnClickFor(R.id.txt_share_task_edit_date, listener);// todo minor why cannot trigger
	}

	protected void buildNotices(){
		setOnClickFor(R.id.img_share_task_edit_add_remind, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				onClickAddRemind();
			}
		});
		ArrayList<Notice> notices = Notice.getOnGoingNoticesForTask(task);
		ListView lstNotice = getListView(R.id.lst_share_task_edit_remind);
		lstNotice.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
				Notice selectedNotice = adapter.getItem(i);
				onClickUpdateNotice(selectedNotice);
			}
		});
		adapter = new NoticeAdapter(activity, notices, true, new NoticeAdapter.OnNoticeDelete() {

			@Override
			public void onDelete(Notice notice){
				if(savedNotices.contains(notice)){
					savedNotices.remove(notice);
				}
				notice.isDeleted = true;
				notice.save();
				adapter.data.remove(notice);
				adapter.notifyDataSetChanged();
			}
		});
		lstNotice.setAdapter(adapter);
	}

	private void onClickAddRemind(){
		final DateTimePicker dateTimePicker = new DateTimePicker(activity);
		dateTimePicker.setDateTimeListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				addNotice(dateTimePicker.getDateTime());
			}
		});
		dateTimePicker.show("Select remind time");
	}

	private void onClickUpdateNotice(final Notice notice){
		final DateTimePicker dateTimePicker = new DateTimePicker(activity);
		dateTimePicker.setDateTimeListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				updateNotice(dateTimePicker.getDateTime(), notice);
			}
		});
		dateTimePicker.show(getString(R.string.fragment_task_update_notice_time));
	}

	private void addNotice(Calendar c){
		Notice newNotice = new Notice(Notice.NOTICE_TYPE_TASK, task.name, task.description, task.id, c.getTime());
		newNotice.save(); // todo can create redundant notices in database if user cancel adding task.
		savedNotices.add(newNotice);
		task.addNoticeIdWithoutSave(newNotice.getId().toString());
		adapter.data.add(newNotice);
		adapter.notifyDataSetChanged();
	}

	private void updateNotice(Calendar c, Notice notice){
		notice.noticeDate = c.getTime();
		notice.isRemoteSaved = false;
		notice.save();
		adapter.notifyDataSetChanged();
	}

	protected void deleteUnUsedNotices(){
		// for(Notice notice : savedNotices){
		// notice.delete();
		// }
		savedNotices = new ArrayList<Notice>();
	}

	// protected void saveTaskNotices() {
	// for(Notice notice : savedNotices){
	// saveTaskNotice(task.id, notice.id);
	// }
	// savedNotices = new ArrayList<Notice>();
	// }

	// protected void saveTaskNotice(String taskId, String noticeId) {
	// final TaskNotice taskNotice = new TaskNotice(taskId, noticeId);
	// JSONObject param = MU.buildJsonObj(Arrays.asList("taskNotice", taskNotice.toString()));
	// postApi(Const.ADD_TASK_NOTICE, param, new Api.OnCallApiListener() {
	//
	// @Override
	// public void onApiResponse(JSONObject response) {
	// showShortToast("Save new task Notice to server success");
	// taskNotice.id = response.optString("data");
	// taskNotice.isRemoteSaved = true;
	// taskNotice.save();
	// backToTaskList();
	// }
	//
	// @Override
	// public void onApiError(String errorMsg) {
	// showShortToast("Save new task to server failed");
	// taskNotice.save();
	// backToTaskList();
	// }
	// });
	// }

	protected void makeAlarmForNotice(){
		for(Notice notice : savedNotices){
			GcmUtil.makeLocalAlarm(activity, notice);
		}
		savedNotices = new ArrayList<Notice>();
	}

	protected static List<MyModel> searchWithConditions(Map<String, Object> conditions){
		String text = (String)conditions.get(TaskSearchFragment.KEY_TASK_SEARCH_TEXT);
		String priority = (String)conditions.get(TaskSearchFragment.KEY_TASK_SEARCH_PRIORITY);
		int taskStatus = (int)conditions.get(TaskSearchFragment.KEY_TASK_SEARCH_STATUS);

		List<MyModel> tasks = Task.getAllUndeleted(Task.class);
		Iterator<MyModel> ib = tasks.iterator();
		while(ib.hasNext()){
			Task task = (Task)ib.next();
			if(!MU.isEmpty(text) && !MU.checkMatch(task.name, text) && !MU.checkMatch(task.description, text) && !MU.checkMatch(task.comment, text)){
				ib.remove();
				continue;
			}

			// taskStatus = 1 mean user selected "Any" at 3 options dialog
			if(taskStatus != Task.STATUS_ANY && taskStatus != task.status){
				ib.remove();
				continue;
			}

			if(!Task.TASK_PRIORITIES_WITH_ANY[0].equals(priority) && !priority.equals(String.valueOf(task.priority))){
				ib.remove();
				continue;
			}
		}

		return tasks;
	}

	protected void backToTaskList(){
		activity.backToFragment(TaskListFragment.class, TaskListFragment.KEY_TARGET_DATE, targetDate);
		// activity.backOneFragment();
	}
}
