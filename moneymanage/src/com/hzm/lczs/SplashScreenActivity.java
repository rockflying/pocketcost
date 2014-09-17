/**
 * 
 */
package com.hzm.lczs;

import java.util.Timer;
import java.util.TimerTask;

import cn.casee.adsdk.common.ErrorCode;
import cn.casee.adsdk.interstitial.CaseeInterstitialAd;
import cn.casee.adsdk.interstitial.InterstitialAdListener;

import com.hzm.lczs.db.MyDbHelper;
import com.hzm.lczs.R;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class SplashScreenActivity extends Activity  {
	 public static MyDbHelper db = null;
	 private Timer mTimer;  
	 private TimerTask mTimerTask; 
	 private Handler handler;
	 LinearLayout layout;
	 //private CaseeInterstitialAd interstitial;
	 
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen_activity);
		layout = (LinearLayout) findViewById(R.id.layout1);
//		interstitial = new CaseeInterstitialAd(SplashScreenActivity.this, "6CB378D55833D9C1D015BD0A2C705BE8", "casee_cid");
//
//		interstitial.registInterstitialAdListener(new InterstitialAdListener() {
//            @Override
//            public void onReceiveAd() {
//                interstitial.show();
//            }
//
//            @Override
//            public void onFailedToReceiveAd(ErrorCode errorCode) {
//                
//              Intent i=new Intent(SplashScreenActivity.this,MainTabActivity.class);//  MainActivity MainTabActivity
//              startActivity(i);
//              SplashScreenActivity.this.finish();
//            }
//
//            @Override
//            public void onPresentScreen() {
//            }
//
//            @Override
//            public void onLeaveApplication() {
//            }
//
//            @Override
//            public void onDismissScreen() {
//                
//                Intent i=new Intent(SplashScreenActivity.this,MainTabActivity.class);//  MainActivity MainTabActivity
//                startActivity(i);
//                SplashScreenActivity.this.finish();
//            }
//        });
		
		
		 mTimer = new Timer();  
         handler=new Handler(){
             @Override
             public void handleMessage(Message msg){

               switch(msg.what){     
                     case 1:
                         
                         if (mTimer != null) {
                             mTimer.cancel( );
                             mTimer = null;                        
                           }
                         if(mTimerTask !=null) {
                              mTimerTask.cancel();
                              mTimerTask=null;                    
                           }
                         initialDBData();
                         CommonData.getInstance().load(SplashScreenActivity.this); 
                         //interstitial.loadAd();
                         Intent i=new Intent(SplashScreenActivity.this,MainTabActivity.class);//  MainActivity MainTabActivity
                         startActivity(i);
                         SplashScreenActivity.this.finish();
                         break;                 
                     default :
                         break;
                    }
             
               super.handleMessage(msg);
               }
             };
             
             mTimerTask = new TimerTask() {  
                 @Override  
                 public void run() {  
                         Message msg =new Message();
                         msg.what=1;
                         handler.sendMessage(msg);

                     }  
                 };  
          mTimer.schedule(mTimerTask, 1500,1500);  

		
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		//activity jump
//	    if (mTimer != null) {
//            mTimer.cancel( );
//            mTimer = null;                        
//          }
//        if(mTimerTask !=null) {
//             mTimerTask.cancel();
//             mTimerTask=null;                    
//          }
//		Intent i=new Intent(SplashScreenActivity.this,MainTabActivity.class);//  MainActivity MainTabActivity
//		startActivity(i);
//		this.finish();
		return super.onTouchEvent(event);
	}
	
	private void initialDBData() {
		// 建立数据库
//		deleteDatabase("mydb");
		db = MyDbHelper.getInstance(this.getApplicationContext());
		Resources res = this.getResources();
		db.open();
		Cursor cursor = db.select("TBL_EXPENDITURE_CATEGORY", new String[] {//判断数据库中是否为空，为空才去插入表
				"ID", "NAME", "BUDGET" }, null, null, null, null, null);
		if (cursor.moveToNext()) {
			cursor.close();
			db.close();
			return;
		}
		// 插入支出类别
		String[] arr = res.getStringArray(R.array.TBL_EXPENDITURE_CATEGORY);
		for (int i = 0; i < arr.length; i++) {
			Log.i("TEST", arr[i]);
			db.insert("TBL_EXPENDITURE_CATEGORY", new String[] { "NAME",
					"BUDGET" }, new String[] { arr[i], "0" });

		}
		// 插入支出子类别
		arr = res.getStringArray(R.array.TBL_EXPENDITURE_SUB_CATEGORY_1);
		for (int i = 0; i < arr.length; i++) {
			Log.i("TEST", arr[i]);
			db.insert("TBL_EXPENDITURE_SUB_CATEGORY", new String[] { "NAME",
					"PARENT_CATEGORY_ID" }, new String[] { arr[i], "1" });

		}
		arr = res.getStringArray(R.array.TBL_EXPENDITURE_SUB_CATEGORY_2);
		for (int i = 0; i < arr.length; i++) {
			Log.i("TEST", arr[i]);
			db.insert("TBL_EXPENDITURE_SUB_CATEGORY", new String[] { "NAME",
					"PARENT_CATEGORY_ID" }, new String[] { arr[i], "2" });

		}
		arr = res.getStringArray(R.array.TBL_EXPENDITURE_SUB_CATEGORY_3);
		for (int i = 0; i < arr.length; i++) {
			Log.i("TEST", arr[i]);
			db.insert("TBL_EXPENDITURE_SUB_CATEGORY", new String[] { "NAME",
					"PARENT_CATEGORY_ID" }, new String[] { arr[i], "3" });

		}
		arr = res.getStringArray(R.array.TBL_EXPENDITURE_SUB_CATEGORY_4);
		for (int i = 0; i < arr.length; i++) {
			Log.i("TEST", arr[i]);
			db.insert("TBL_EXPENDITURE_SUB_CATEGORY", new String[] { "NAME",
					"PARENT_CATEGORY_ID" }, new String[] { arr[i], "4" });

		}
		arr = res.getStringArray(R.array.TBL_EXPENDITURE_SUB_CATEGORY_5);
		for (int i = 0; i < arr.length; i++) {
			Log.i("TEST", arr[i]);
			db.insert("TBL_EXPENDITURE_SUB_CATEGORY", new String[] { "NAME",
					"PARENT_CATEGORY_ID" }, new String[] { arr[i], "5" });

		}
		arr = res.getStringArray(R.array.TBL_EXPENDITURE_SUB_CATEGORY_6);
		for (int i = 0; i < arr.length; i++) {
			Log.i("TEST", arr[i]);
			db.insert("TBL_EXPENDITURE_SUB_CATEGORY", new String[] { "NAME",
					"PARENT_CATEGORY_ID" }, new String[] { arr[i], "6" });

		}
		arr = res.getStringArray(R.array.TBL_EXPENDITURE_SUB_CATEGORY_7);
		for (int i = 0; i < arr.length; i++) {
			Log.i("TEST", arr[i]);
			db.insert("TBL_EXPENDITURE_SUB_CATEGORY", new String[] { "NAME",
					"PARENT_CATEGORY_ID" }, new String[] { arr[i], "7" });
		}
		arr = res.getStringArray(R.array.TBL_EXPENDITURE_SUB_CATEGORY_8);
		for (int i = 0; i < arr.length; i++) {
			Log.i("TEST", arr[i]);
			db.insert("TBL_EXPENDITURE_SUB_CATEGORY", new String[] { "NAME",
					"PARENT_CATEGORY_ID" }, new String[] { arr[i], "8" });
		}
		arr = res.getStringArray(R.array.TBL_EXPENDITURE_SUB_CATEGORY_9);
		for (int i = 0; i < arr.length; i++) {
			Log.i("TEST", arr[i]);
			db.insert("TBL_EXPENDITURE_SUB_CATEGORY", new String[] { "NAME",
					"PARENT_CATEGORY_ID" }, new String[] { arr[i], "9" });

		}

		arr = res.getStringArray(R.array.TBL_EXPENDITURE_SUB_CATEGORY_10);
		for (int i = 0; i < arr.length; i++) {
			Log.i("TEST", arr[i]);
			db.insert("TBL_EXPENDITURE_SUB_CATEGORY", new String[] { "NAME",
					"PARENT_CATEGORY_ID" }, new String[] { arr[i], "10" });

		}
		arr = res.getStringArray(R.array.TBL_EXPENDITURE_SUB_CATEGORY_11);
		for (int i = 0; i < arr.length; i++) {
			Log.i(" TEST", arr[i]);
			db.insert("TBL_EXPENDITURE_SUB_CATEGORY", new String[] { "NAME",
					"PARENT_CATEGORY_ID" }, new String[] { arr[i], "11" });

		}
		// 插入收入类别
		arr = res.getStringArray(R.array.TBL_INCOME_CATEGORY);
		for (int i = 0; i < arr.length; i++) {
			Log.i("TEST", arr[i]);
			db.insert("TBL_INCOME_CATEGORY", new String[] { "NAME" },
					new String[] { arr[i] });

		}
		// 插入子收入类别
		arr = res.getStringArray(R.array.TBL_INCOME_SUB_CATEGORY_1);
		for (int i = 0; i < arr.length; i++) {
			Log.i("TEST", arr[i]);
			db.insert("TBL_INCOME_SUB_CATEGORY", new String[] { "NAME",
					"PARENT_CATEGORY_ID" }, new String[] { arr[i], "1" });
		}
		arr = res.getStringArray(R.array.TBL_INCOME_SUB_CATEGORY_2);
		for (int i = 0; i < arr.length; i++) {
			Log.i("TEST", arr[i]);
			db.insert("TBL_INCOME_SUB_CATEGORY", new String[] { "NAME",
					"PARENT_CATEGORY_ID" }, new String[] { arr[i], "2" });

		}

		// 插入账户类别
		arr = res.getStringArray(R.array.TBL_ACCOUNT_TYPE);
		for (int i = 0; i < arr.length; i++) {
			Log.i("TEST", arr[i]);
			db.insert("TBL_ACCOUNT_TYPE", new String[] { "NAME", "POSTIVE" },
					new String[] { arr[i].substring(0, arr[i].indexOf(",")),
							arr[i].substring(arr[i].indexOf(",") + 1) });

		}
		// 插入账户子类别
		arr = res.getStringArray(R.array.TBL_ACCOUNT_SUB_TYPE_1);
		for (int i = 0; i < arr.length; i++) {
			Log.i("TEST", arr[i]);
			db.insert("TBL_ACCOUNT_SUB_TYPE", new String[] { "NAME",
					"PARENT_TYPE_ID" }, new String[] { arr[i], "1" });

		}
		arr = res.getStringArray(R.array.TBL_ACCOUNT_SUB_TYPE_2);
		for (int i = 0; i < arr.length; i++) {
			Log.i("TEST", arr[i]);
			db.insert("TBL_ACCOUNT_SUB_TYPE", new String[] { "NAME",
					"PARENT_TYPE_ID" }, new String[] { arr[i], "2" });

		}
		arr = res.getStringArray(R.array.TBL_ACCOUNT_SUB_TYPE_3);
		for (int i = 0; i < arr.length; i++) {
			Log.i("TEST", arr[i]);
			db.insert("TBL_ACCOUNT_SUB_TYPE", new String[] { "NAME",
					"PARENT_TYPE_ID" }, new String[] { arr[i], "3" });

		}
		arr = res.getStringArray(R.array.TBL_ACCOUNT_SUB_TYPE_4);
		for (int i = 0; i < arr.length; i++) {
			Log.i("TEST", arr[i]);
			db.insert("TBL_ACCOUNT_SUB_TYPE", new String[] { "NAME",
					"PARENT_TYPE_ID" }, new String[] { arr[i], "4" });

		}
		arr = res.getStringArray(R.array.TBL_ACCOUNT_SUB_TYPE_5);
		for (int i = 0; i < arr.length; i++) {
			Log.i("TEST", arr[i]);
			db.insert("TBL_ACCOUNT_SUB_TYPE", new String[] { "NAME",
					"PARENT_TYPE_ID" }, new String[] { arr[i], "5" });

		}
		// 插入账户
		arr = res.getStringArray(R.array.TBL_ACCOUNT);
		for (int i = 0; i < arr.length; i++) {
			Log.i("TEST", arr[i]);
			db.insert("TBL_ACCOUNT", new String[] { "NAME", "TYPE_ID",
					"SUB_TYPE_ID", "ACCOUNT_BALANCE" }, arr[i].split(","));
		}
		// 插入商家
		arr = res.getStringArray(R.array.TBL_STORE);
		for (int i = 0; i < arr.length; i++) {
			Log.i("TEST", arr[i]);
			db.insert("TBL_STORE", new String[] { "NAME" },
					new String[] { arr[i] });
		}
		// 插入项目
		arr = res.getStringArray(R.array.TBL_ITEM);
		for (int i = 0; i < arr.length; i++) {
			Log.i("TEST", arr[i]);
			db.insert("TBL_ITEM", new String[] { "NAME" },
					new String[] { arr[i] });
		}
		
		// 银行名称
        arr = res.getStringArray(R.array.TBL_BANK_NAME);
        for (int i = 0; i < arr.length; i++) {
            Log.i("TEST", arr[i]);
            db.insert("TBL_BANK_TYPE", new String[] { "NAME", "SHORTNAME"}, arr[i].split(","));
        }
        
     // 银行卡种类
        arr = res.getStringArray(R.array.TBL_BANK_CARD_TYPE);
        for (int i = 0; i < arr.length; i++) {
            Log.i("TEST", arr[i]);
            db.insert("TBL_BANK_CARD_TYPE", new String[] { "NAME" },
                    new String[] { arr[i] });
        }
        
     // 还款日类型
        arr = res.getStringArray(R.array.TBL_BANK_BACK_DATE_TYPE);
        for (int i = 0; i < arr.length; i++) {
            Log.i("TEST", arr[i]);
            db.insert("TBL_BANK_BACK_DATE_TYPE", new String[] { "NAME" },
                    new String[] { arr[i] });
        }
        
        
     // 加入一条默认的银行卡信息以便初始化rowid   
       // db.insert("TBL_BANK_CARD_INFO", new String[]{"CARD_NUM","BANK_ID"},new String[]{ "1010101010", "1" });

		/*
		 * cursor =db.select("TBL_EXPENDITURE_CATEGORY", new
		 * String[]{"ID","NAME","BUDGET"}, null, null, null, null, null);
		 * while(cursor.moveToNext()) {
		 * Log.i("TBL_EXPENDITURE_CATEGORY",cursor.getString
		 * (0)+","+cursor.getString(1)); }
		 */
		db.close();

	}

}
