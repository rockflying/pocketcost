
package com.hzm.lczs.calendar;

import com.hzm.lczs.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CalendarActivity extends Activity  implements CalendarView.OnCellTouchListener{
	public static final String MIME_TYPE = "vnd.android.cursor.dir/vnd.exina.android.calendar.date";
	private CalendarView mView = null;
	private TextView mHit;
	private Handler mHandler = new Handler();
	private  Button btCenter;
	private Rect ecBounds;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {//
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        setContentView(R.layout.calendar_main);
        mView = (CalendarView)findViewById(R.id.calendar);
        mView.setOnCellTouchListener(this);
        
        if(getIntent().getAction().equals(Intent.ACTION_PICK))
        	findViewById(R.id.hint).setVisibility(View.GONE);

        
        btCenter = (Button) findViewById(R.id.btCenter);
        btCenter.setText(mView.getYear() + "-" + (mView.getMonth() + 1));
        Button btLeft = (Button) findViewById(R.id.btnLeft);
        btLeft.setText("上月");
        Button btRight = (Button) findViewById(R.id.btRight);
        btRight.setText("下月");
        btLeft.setOnClickListener(new OnClickListener() { 
			public void onClick(View v) {
				mView.previousMonth(); 
				btCenter.setText(mView.getYear() + "-" + (mView.getMonth() + 1));
			}
		});
        btRight.setOnClickListener(new OnClickListener() { 
			public void onClick(View v) {
				mView.nextMonth(); 
				btCenter.setText(mView.getYear() + "-" + (mView.getMonth() + 1));
			}
		});
        
        btCenter.setVisibility(View.GONE);
        btLeft.setVisibility(View.GONE);
        btRight.setVisibility(View.GONE);
        
    }

	public void onTouch(Cell cell) {
		
		Intent intent = getIntent();
		String action = intent.getAction();
		if(cell.mPaint.getColor() == Color.TRANSPARENT) 
		{  
		    //hzm 什么也不做
		} else if(cell.mPaint.getColor() == Color.GRAY) {
			// 上一跃
			//mView.previousMonth(); 
			//btCenter.setText(mView.getYear() + "-" + (mView.getMonth() + 1));
		} else if(cell.mPaint.getColor() == Color.LTGRAY) {
			// 下一月
			//mView.nextMonth(); 
			//btCenter.setText(mView.getYear() + "-" + (mView.getMonth() + 1));
		} else {  //  点击本月
			if(action.equals(Intent.ACTION_PICK) || action.equals(Intent.ACTION_GET_CONTENT)) {
				Intent ret = new Intent();
				ret.putExtra("year", mView.getYear());
				ret.putExtra("month", mView.getMonth());
				ret.putExtra("day", cell.getDayOfMonth());
				 
				ecBounds = cell.getBound();
				mView.getDate();
				
				mView.mDecoraClick.setBounds(ecBounds);
				mView.invalidate();
				
				this.setResult(RESULT_OK, ret);
				finish();
				return;
			}
		}
//		int day = cell.getDayOfMonth();
//		if(mView.firstDay(day))
//			mView.previousMonth();
//		else if(mView.lastDay(day))
//			mView.nextMonth();
//		else
//			return;

		mHandler.post(new Runnable() {
			public void run() {
				btCenter.setText(mView.getYear() + "-" + (mView.getMonth() + 1));
				Toast.makeText(CalendarActivity.this, DateUtils.getMonthString(mView.getMonth(), DateUtils.LENGTH_LONG) + " "+mView.getYear(), Toast.LENGTH_SHORT).show();
			}
		});
	}
 
}