package com.nguyenhoangviet.vietnguyen.core.views.widgets;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by viet on 9/3/2015.
 * Known bug on simulator: set onDateSet twice
 */
public class DatePickerFragment extends DialogFragment{

	private DatePickerDialog.OnDateSetListener	listener;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this.listener, year, month, day);
	}

	// @Override
	// public void onDateSet(DatePicker view, int year, int month, int day) {
	// // Do something with the date chosen by the user
	// if (listener != null) {
	// listener.onDateSet(view, year, month, day);
	// }
	// }

	public void setOnDateSetListener(DatePickerDialog.OnDateSetListener listener){
		this.listener = listener;
	}

}