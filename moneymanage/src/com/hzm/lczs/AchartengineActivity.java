package com.hzm.lczs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class AchartengineActivity extends Activity implements OnClickListener,
		OnCheckedChangeListener, OnItemSelectedListener {
	private LinearLayout layoutPie, layoutHistogram;
	public static String str_startTime = "startTime";
	public static String str_endTime = "endTime";
	Date date1, date2;
	final static int INCOME_MODE = 0;
	final static int PAYOUT_MODE = 1;
	private int type = PAYOUT_MODE;// 操作类型0收入、1支出

	final static int DAY_MODE = 0;
	final static int WEEK_MODE = 1;
	final static int MONTH_MODE = 2;
	final static int YEAR_MODE = 3;

	private int dateType = DAY_MODE;// 操作类型0日、1周，2月，3年

	public long start_time, end_time;
	private RadioGroup trans_type_tab_rg = null;
	private RadioButton rb1 = null;
	private RadioButton rb2 = null;
	private RadioGroup date_tab = null;
	private RadioButton day = null;
	private RadioButton week = null;
	private RadioButton month = null;
	private RadioButton year = null;

	// 画图表的
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Intent intent = ChartFactory.getPieChartIntent(this,
		// buildCategoryDataset("Project budget", values), renderer, "Budget");
		// startActivity(intent);
		setContentView(R.layout.chart_activity);
		init();
		drawPie();
		drawHistongram();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		drawPie();
		drawHistongram();

	}

	private void drawHistongram() {
		// DrawHistogram
		View viewHistogram = new DrawHistogram()
				.execute(AchartengineActivity.this); // 画条形图
		layoutHistogram.removeAllViews();
		layoutHistogram.setBackgroundColor(Color.TRANSPARENT);
		layoutHistogram.addView(viewHistogram);
	}

	private void drawPie() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();

		switch (dateType) {
		case DAY_MODE:
			c = Calendar.getInstance();
			date1 = date2 = c.getTime();
			//
			break;

		case WEEK_MODE:
			c = Calendar.getInstance();
			int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
			if (day_of_week == 0)
				day_of_week = 7;
			c.add(Calendar.DATE, -day_of_week + 1);
			date1 = c.getTime();

			c = Calendar.getInstance();
			day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
			if (day_of_week == 0)
				day_of_week = 7;
			c.add(Calendar.DATE, -day_of_week + 7);
			date2 = c.getTime();

			break;

		case MONTH_MODE:
			c = Calendar.getInstance();
			c.set(Calendar.DAY_OF_MONTH, 1);// 本月第一天
			date1 = c.getTime();
			c = Calendar.getInstance();
			c.add(Calendar.MONTH, 1);// 本月最后一天
			c.set(Calendar.DAY_OF_MONTH, 1);
			c.add(Calendar.DAY_OF_MONTH, -1);
			date2 = c.getTime();

			break;

		case YEAR_MODE:
			c = Calendar.getInstance();
			c.set(Calendar.DAY_OF_YEAR, 1);// 本年第一天
			date1 = c.getTime();
			c = Calendar.getInstance();
			c.add(Calendar.YEAR, 1);
			c.set(Calendar.DAY_OF_YEAR, 1);// 本年最后一天
			c.add(Calendar.DAY_OF_YEAR, -1);
			date2 = c.getTime();

			break;

		default:

			c = Calendar.getInstance();
			date1 = date2 = c.getTime();
			break;

		}

		DrawPie pieChart = new DrawPie(AchartengineActivity.this);
		int Count = 0;

		if (type == INCOME_MODE)
			Count = pieChart.IncomeDataPrepare(date1, date2); // 收入图
		else
			Count = pieChart.ExpenseDataPrepare(date1, date2); // 消费图

		if (Count > 0) {
			View viewPie = pieChart.execute(); // 画饼图
			layoutPie.removeAllViews();
			layoutPie.setBackgroundColor(Color.TRANSPARENT);
			layoutPie.addView(viewPie);
		} else {
			layoutPie.removeAllViews();
			layoutPie.setBackgroundColor(Color.TRANSPARENT);
		}

	}

	private void init() {

		layoutHistogram = (LinearLayout) findViewById(R.id.chartlayout);
		layoutPie = (LinearLayout) findViewById(R.id.pielayout);

		trans_type_tab_rg = (RadioGroup) findViewById(R.id.trans_type_tab_rg);
		rb1 = (RadioButton) findViewById(R.id.payout_tab_rb);// 支出
		rb2 = (RadioButton) findViewById(R.id.income_tab_rb);// 收入

		rb1.setChecked(true); // 默认 选中 支出
		rb1.setOnCheckedChangeListener(this);
		rb2.setOnCheckedChangeListener(this);

		date_tab = (RadioGroup) findViewById(R.id.date_tab);
		day = (RadioButton) findViewById(R.id.day);// 支出
		week = (RadioButton) findViewById(R.id.week);// 收入
		month = (RadioButton) findViewById(R.id.month);
		year = (RadioButton) findViewById(R.id.year);

		day.setChecked(true); // 默认 选中 支出
		day.setOnCheckedChangeListener(this);
		week.setOnCheckedChangeListener(this);
		month.setOnCheckedChangeListener(this);
		year.setOnCheckedChangeListener(this);

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		// 支出

		if (rb1.isChecked()) {
			type = PAYOUT_MODE;

		}
		if (rb2.isChecked())// 收入
		{
			type = INCOME_MODE;

		}

		if (day.isChecked())// 收入
		{
			dateType = DAY_MODE;

		}

		if (week.isChecked())// 收入
		{

			dateType = WEEK_MODE;
		}
		if (month.isChecked())// 收入
		{

			dateType = MONTH_MODE;
		}
		if (year.isChecked())// 收入
		{
			dateType = YEAR_MODE;

		}

		drawPie();

	}

	// public void onBackPressed() {
	//
	// Intent i=new Intent(AchartengineActivity.this,MainTabActivity.class);//
	// MainActivity MainTabActivity
	// startActivity(i);
	// AchartengineActivity.this.finish();
	// super.onBackPressed();
	// }

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
