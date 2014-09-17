package com.hzm.lczs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;

import com.hzm.lczs.db.MyDbHelper;

public class DrawPie {
	// 画饼图
	private MyDbHelper db = null;
	private double value[];
	private String partName[];
	private Context mContent;
	private String pieTitle;

	private static int color[][] = {
			{ Color.RED },
			{ Color.RED, Color.YELLOW },
			{ Color.RED, Color.YELLOW, Color.BLUE },
			{ Color.RED, Color.YELLOW, Color.BLUE, Color.BLACK },
			{ Color.RED, Color.YELLOW, Color.BLUE, Color.BLACK, Color.CYAN },
			{ Color.RED, Color.YELLOW, Color.BLUE, Color.BLACK, Color.CYAN,
					Color.DKGRAY },
			{ Color.RED, Color.YELLOW, Color.BLUE, Color.BLACK, Color.CYAN,
					Color.DKGRAY, Color.GRAY },
			{ Color.RED, Color.YELLOW, Color.BLUE, Color.BLACK, Color.CYAN,
					Color.DKGRAY, Color.GRAY, Color.GREEN },
			{ Color.RED, Color.YELLOW, Color.BLUE, Color.BLACK, Color.CYAN,
					Color.DKGRAY, Color.GRAY, Color.GREEN, Color.LTGRAY },
			{ Color.RED, Color.YELLOW, Color.BLUE, Color.BLACK, Color.CYAN,
					Color.DKGRAY, Color.GRAY, Color.GREEN, Color.LTGRAY,
					Color.WHITE },
			{ Color.RED, Color.YELLOW, Color.BLUE, Color.BLACK, Color.CYAN,
					Color.DKGRAY, Color.GRAY, Color.GREEN, Color.LTGRAY,
					Color.WHITE, Color.MAGENTA },

	};// 字段名

	public DrawPie(Context context) {

		mContent = context;
	}

	public int IncomeDataPrepare(Date start, Date end) { // 收入查询

		Log.e("hzm", format(start));
		Log.e("hzm", format(end));
		pieTitle = format(start) + "——" + format(end) + "收入统计";
		db = MyDbHelper.getInstance(mContent);
		db.open();
		int i = 0; // 收入二级类别查询
		// select sum(AMOUNT) , NAME from TBL_INCOME , TBL_INCOME_SUB_CATEGORY
		// where TBL_INCOME.INCOME_SUB_CATEGORY_ID=TBL_INCOME_SUB_CATEGORY.ID
		// group by INCOME_SUB_CATEGORY_ID
		Cursor cursor = db
				.rawQuery(
						"select sum(AMOUNT) , NAME  from TBL_INCOME  , TBL_INCOME_SUB_CATEGORY where strftime('%Y-%m-%d',DATE)>=? and strftime('%Y-%m-%d',DATE)<=? and TBL_INCOME.INCOME_SUB_CATEGORY_ID=TBL_INCOME_SUB_CATEGORY.ID group by INCOME_SUB_CATEGORY_ID",
						new String[] { format(start), format(end) });

		partName = new String[cursor.getCount()]; // 字符串数组必须初始化
		value = new double[cursor.getCount()];
		while (cursor.moveToNext()) {
			if (cursor.getString(0) != null) { //
				Log.e("hzm", "ge" + cursor.getCount());

				partName[i] = cursor.getString(cursor.getColumnIndex("NAME"));
				value[i] = cursor.getDouble(cursor
						.getColumnIndex("sum(AMOUNT)"));

				Log.e("hzm", partName[i] + ":" + value[i]);
			}
			i++;
		}
		cursor.close();
		db.close();

		return partName.length;

	}

	public int ExpenseDataPrepare(Date start, Date end) { // 消费查询

		Log.e("hzm start", format(start));
		Log.e("hzm end", format(end));
		pieTitle = format(start) + "——" + format(end) + "消费统计";
		db = MyDbHelper.getInstance(mContent);
		db.open();
		int i = 0; // 收入二级类别查询
		// select sum(AMOUNT) , NAME from TBL_INCOME , TBL_INCOME_SUB_CATEGORY
		// where TBL_INCOME.INCOME_SUB_CATEGORY_ID=TBL_INCOME_SUB_CATEGORY.ID
		// group by INCOME_SUB_CATEGORY_ID
		Cursor cursor = db
				.rawQuery(
						"select sum(AMOUNT) , NAME  from TBL_EXPENDITURE_CATEGORY  , TBL_EXPENDITURE where strftime('%Y-%m-%d',DATE)>=? and strftime('%Y-%m-%d',DATE)<=? and TBL_EXPENDITURE.EXPENDITURE_CATEGORY_ID=TBL_EXPENDITURE_CATEGORY.ID group by EXPENDITURE_CATEGORY_ID",
						new String[] { format(start), format(end) });

		partName = new String[cursor.getCount()]; // 字符串数组必须初始化
		value = new double[cursor.getCount()];
		while (cursor.moveToNext()) {
			if (cursor.getString(0) != null) { //
				Log.e("hzm", "ge" + cursor.getCount());

				partName[i] = cursor.getString(cursor.getColumnIndex("NAME"));
				value[i] = cursor.getDouble(cursor
						.getColumnIndex("sum(AMOUNT)"));

				Log.e("hzm", partName[i] + ":" + value[i]);
			}
			i++;
		}
		cursor.close();
		db.close();

		return partName.length;

	}

	public GraphicalView execute() {

		int[] colors = color[partName.length - 1];

		DefaultRenderer renderer = buildCategoryRenderer(colors);
		CategorySeries categorySeries = new CategorySeries(pieTitle);

		for (int i = 0; i < partName.length; i++) {
			if (value[i] > 0) {

				categorySeries.add(partName[i], value[i]);
				Log.e("hzm execute ", partName[i] + ":" + value[i]);
			}
		}

		return ChartFactory.getPieChartView(mContent, categorySeries, renderer);
	}

	protected DefaultRenderer buildCategoryRenderer(int[] colors) {
		DefaultRenderer renderer = new DefaultRenderer();

		renderer.setLabelsTextSize(14);
		renderer.setChartTitle(pieTitle);
		renderer.setChartTitleTextSize(14);
		renderer.setLegendTextSize(14);
		renderer.setLegendHeight(50);
		renderer.setZoomButtonsVisible(true);
		renderer.setZoomEnabled(true);

		renderer.setApplyBackgroundColor(true);
		renderer.setBackgroundColor(Color.argb(0, 220, 228, 234));//

		for (int color : colors) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(color);
			renderer.addSeriesRenderer(r);
		}
		return renderer;

	}

	private String format(Date date) {
		String str = "";
		SimpleDateFormat ymd = null;
		ymd = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		str = ymd.format(date);
		return str;
	}
}