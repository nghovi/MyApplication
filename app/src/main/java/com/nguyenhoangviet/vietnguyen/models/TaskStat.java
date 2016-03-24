package com.nguyenhoangviet.vietnguyen.models;

import java.util.Date;
import java.util.List;

/**
 * Created by viet on 3/24/2016.
 */
public class TaskStat{

	public class DailyTask{

		public Date	date;
		public int	taskFinishedCount;
		public int	taskUnFinishedCount;
	}

	public List<DailyTask>	daily_tasks;
	public int				totalFinished;
	public int				totalUnfinished;

}
