package com.hzm.lczs.utility;

import com.hzm.lczs.R;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ImageView.ScaleType;

public class MyProcessBar extends RelativeLayout {
	protected static final int STOP = 0x108;
	protected static final int NEXT = 0x109;

	private int m_max = 100;
	private int m_process = 0;
	private int c_process = 0;
	private ImageView mImageView = null;
	private LayoutParams params;
	private Handler mHandler;
	private boolean isModified = false;
	
	int i=0;
	private Thread mThread = new Thread(new Runnable() {
		@Override
		public void run() {
				try {
//					
				    if(m_process==0){
				        
				        if (mImageView != null)
                            removeView(mImageView);
				    }
				    
					if(c_process<m_process){
					    
					    mThread.sleep(20);
					    c_process++;
					    setProgressVaule(c_process);
					    reflashPorcess(c_process);
					  
					    // 界面的修改，交由线程来处理
					    
					    Log.e("hzm xia","c_process:"+c_process);
	                    Log.e("hzm xia","m_process:"+m_process);
					}else {
					    
					    c_process=0;
					    
					}
				
					//setProgress(c_process);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e("hzm xia","xuo:");
				}
		}
	});

	public MyProcessBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MyProcessBar(Context context) {
		super(context);
		init();
	}

	private void init() {
//		setBackgroundResource(R.drawable.widget_battery_bg);
		mHandler = new Handler(getContext().getMainLooper());
		params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//		mThread.start();
	}

	public void setMax(int max) {
		m_max = max;
	}

	public int getMax() {
		return m_max;
	}

	public void setProgress(int process) {
		if (process <= m_max) {
			m_process = process;
			mHandler.post(mThread);
		}
	}

	public void setProgressVaule(int process) {
        if (process <= m_max) {
           
            mHandler.post(mThread);
        }
    }
	
	
	public int getProgress() {
		return m_process;
	}

	private int getCountLength() {
		return (getHeight() - 16) * c_process / m_max;
	}

	private void reflashPorcess(int process) {
		if (mImageView != null)
			removeView(mImageView);
		mImageView = null;
		mImageView = new ImageView(getContext());
		mImageView.setAdjustViewBounds(true);
		mImageView.setScaleType(ScaleType.FIT_XY);
		mImageView.setImageResource(R.drawable.widget_battery_bg1);
		params.height = getCountLength();
		addView(mImageView, params);
		
	}
}