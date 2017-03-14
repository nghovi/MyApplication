package com.nguyenhoangviet.vpcorp_purchase.core.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.nguyenhoangviet.vpcorp_purchase.myapplication.R;

/**
 */
public class DateTimePicker{

	private final String	COMMON_DATE_TIME_FORMAT_24H	= "yyyy/MM/dd HH:mm";

	protected Dialog		alertDialog;

	protected Calendar		dateTime;

	protected DatePicker	datePicker;
	protected TimePicker	timePicker;

	public DateTimePicker(Context context){
		alertDialog = new Dialog(context);
		alertDialog.setContentView(R.layout.core_date_time_picker);

		alertDialog.findViewById(R.id.txt_id_cancel).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				alertDialog.dismiss();
			}
		});

		datePicker = (DatePicker)alertDialog.findViewById(R.id.date_picker);
		try{
			datePicker.getCalendarView().setShowWeekNumber(false);
		}catch(UnsupportedOperationException e){
			e.printStackTrace();
		}

		timePicker = (TimePicker)alertDialog.findViewById(R.id.time_picker);
		timePicker.setIs24HourView(true);
	}

	public void show(String msg){
		alertDialog.setTitle(msg);
		alertDialog.show();
		Calendar calendar = Calendar.getInstance();
		if(dateTime != null){
			calendar = dateTime;
			datePicker.init(dateTime.get(Calendar.YEAR), dateTime.get(Calendar.MONTH), dateTime.get(Calendar.DAY_OF_MONTH), null);
		}

		timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
		timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
	}

	public Calendar getDateTime(){
		return dateTime;
	}

	public void setDateTime(Calendar dateTime){
		this.dateTime = dateTime;
	}

	public String getDateTimeString(){
		if(dateTime == null){
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat(COMMON_DATE_TIME_FORMAT_24H);
		String dtString = format.format(dateTime.getTime());
		return dtString;
	}

	public void setCommonDateTime(){
		dateTime = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute());
		alertDialog.dismiss();
	}

	public void setDateTimeListener(final View.OnClickListener listener){

		alertDialog.findViewById(R.id.txt_id_set).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				setCommonDateTime();
				alertDialog.dismiss();
				listener.onClick(view);
			}
		});
	}

	public void setMaxDate(long maxDate){
		datePicker.setMaxDate(maxDate);
	}

	public void setMinDate(long minDate){
		datePicker.setMinDate(minDate);
	}

}
