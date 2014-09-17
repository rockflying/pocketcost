package com.hzm.lczs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hzm.lczs.db.MyDbHelper;
import com.hzm.lczs.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class BankMainActivity extends Activity 
{
    private BankAdapter adapater;
    public List<BankInfo>      bList=null;       //链表
    private MyDbHelper db = null;
    private PopupWindow popupWindow;  
    private ListView listView;  
    private Button button;  
    private int iFirstOrLastItemSelected; 
    int POSTION=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         
        setContentView(R.layout.bank_main);
    
        updateUI();
    } 
    @Override
    public void onResume(){
    	super.onResume();
    	
    	updateUI();
    }
    
    private void updateUI(){
        
        init_list();
        show_list();
        
    }
    
    
    private void  init_list(){
        
        bList = new ArrayList<BankInfo>();//
        
        //{"ID","CARD_NUM","BANK_ID","CARD_TYPE","BILL_DATE","REPAYMENT_TYPE","REPAYMENT_DATE","CREDIT_LIMIT","REMARK"},
        db = MyDbHelper.getInstance(BankMainActivity.this);
        db.open();
        Cursor cursor = db.rawQuery("select  *  from TBL_BANK_CARD_INFO" ,null);
        while (cursor.moveToNext()) {   
                Log.e("hzm",  "ge"+cursor.getCount());
                
                int id=cursor.getInt(cursor.getColumnIndex("ID"));
                String cardNum=cursor.getString(cursor.getColumnIndex("CARD_NUM"));
                int bankID=cursor.getInt(cursor.getColumnIndex("BANK_ID"));
                int cardType=cursor.getInt(cursor.getColumnIndex("CARD_TYPE"));
                int billDate=cursor.getInt(cursor.getColumnIndex("BILL_DATE"));
                int rPayType=cursor.getInt(cursor.getColumnIndex("REPAYMENT_TYPE"));
                int rPayDate=cursor.getInt(cursor.getColumnIndex("REPAYMENT_DATE"));
                double cLimit=cursor.getDouble(cursor.getColumnIndex("CREDIT_LIMIT"));  
                String REMARK=cursor.getString(cursor.getColumnIndex("REMARK"));
                int alarmID=cursor.getInt(cursor.getColumnIndex("ALARM_ID"));
                BankInfo bInfo=new BankInfo(id,cardNum,bankID,cardType,billDate,rPayType,rPayDate,cLimit,REMARK,alarmID);
                bList.add(bInfo);
               
        }
        cursor.close();
        db.close();
        
       // if(bList.size()>0)bList.remove(0);

    }
    private void  show_list(){
        
       adapater = new BankAdapter(BankMainActivity.this,bList);  //
       ListView Msglist=(ListView)findViewById(R.id.Banklist);
       Msglist.setAdapter(adapater);
       
       Msglist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               public void onItemClick(AdapterView<?> adapterView, View view, int position,
                       long id) {
              
                   initControls(view);
                   POSTION=position;
                }
         });

    }
    
    private void initControls(View v) {  
        LayoutInflater inflater = LayoutInflater.from(this);  
        View view = inflater.inflate(R.layout.listview_pop, null);  
  
        SimpleAdapter adapter = new SimpleAdapter(this, getData(),   
                R.layout.pop_listview_item,  
                new String[] { "text" },  
                new int[] { R.id.item });  
        listView = (ListView) view.findViewById(R.id.listview);  
        listView.setAdapter(adapter);  
  
        //自适配长、框设置   
        popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,  
                LayoutParams.WRAP_CONTENT);  
        popupWindow.setBackgroundDrawable(new BitmapDrawable());  // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景 
        popupWindow.setOutsideTouchable(true);  
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);  
        int xoff = popupWindow.getWidth()/2-v.getWidth()/2;
        popupWindow.update();  
        popupWindow.setTouchable(true);  
        popupWindow.setFocusable(true);  
        if (popupWindow != null) {  
            popupWindow.dismiss();  
        }  
        
        int[] location = new int[2];  
        v.getLocationOnScreen(location);  //window.getWidth()/2-parent.getWidth()/2
          
        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, -xoff, location[1]-popupWindow.getHeight());  
//        popupWindow.showAsDropDown(v, -xoff, location[1]-popupWindow.getHeight());
      //监听PopupWindow中的选中(高亮)的item  
        listView.setOnItemSelectedListener(itemSelectedListener);  
        //监听键盘按键  
        listView.setOnKeyListener(popuWindowListener);  
        //监听并响应所点击的item  
        listView.setOnItemClickListener(clickListener);  
  
    }  
  
    
    OnItemClickListener clickListener = new OnItemClickListener() {  
        
        @Override  
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {  
            //在此可实现响应item被点击后的功能  
            popupWindow.dismiss();  
            Intent in = null;
            switch(arg2){
                case 0: //添加
                    in = new Intent(BankMainActivity.this, AddBankRecordActivity.class);
                    startActivity(in);
                    //finish();
                    break;
                
                case 1://删除

                    AlertDialog.Builder builder = new AlertDialog.Builder(BankMainActivity.this);
                    builder.setTitle("警告");
                    builder.setMessage("确定要删除吗？");

                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        	BankRecordAlarm alarm=new BankRecordAlarm(BankMainActivity.this);
                        	alarm.CancelAlarmNotify(bList.get(POSTION).getAlarmId());//取消之前的闹钟
                            delOneCardInfo(bList.get(POSTION).getId());
                            bList.remove(POSTION);
                            updateUI();
                        }
                    });
                    
                    builder.setNegativeButton("取消", null);
                    builder.create().show();
                
                    break;
                    
                case 2://编辑
                    BankInfo data = (BankInfo)bList.get(POSTION);
                    if(data != null){
                        Intent intent = new Intent(BankMainActivity.this, AddBankRecordActivity.class);
                        intent.putExtra("mode", AddBankRecordActivity.EDIT_MODE);
                        intent.putExtra("data", data);
                        startActivityForResult(intent, 0);  
                    }
                    break;
                
                default:
                    
                    break;
                
            }
            
            
        }  
    };  
    OnItemSelectedListener itemSelectedListener = new OnItemSelectedListener() {  
  
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {  
            iFirstOrLastItemSelected = position;  
        }  
  
        public void onNothingSelected(AdapterView<?> parent) {  
  
        }  
  
    };  
  
    private OnKeyListener popuWindowListener = new OnKeyListener() {  
  
        @Override  
        public boolean onKey(View v, int keyCode, KeyEvent event) {  
            if (event.getAction() == KeyEvent.ACTION_DOWN) {  
                switch (keyCode) {  
                case KeyEvent.KEYCODE_DPAD_UP:  
                    if (iFirstOrLastItemSelected == 0) {  
                        listView.setSelection(getData().size() - 1);  
                    }  
                    break;  
  
                case KeyEvent.KEYCODE_DPAD_DOWN:  
                    if (iFirstOrLastItemSelected == getData().size() - 1) {  
                        listView.setSelection(0);  
                    }  
                    break;  
                }  
            }  
  
            return false;  
        }  
  
    };  

    
    
    
    private List<Map<String, String>> getData() {  
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();  
  
        Map<String, String> map = new HashMap<String, String>();  
        map.put("text", "添加");  
        list.add(map);  
  
        map = new HashMap<String, String>();  
        map.put("text", "删除");  
        list.add(map);  
  
        map = new HashMap<String, String>();  
        map.put("text", "编辑");  
        list.add(map);  
  
        return list;  
    }  
  
    
    private void delOneCardInfo(int id)  //删除一条msg
    {
        try{
        db = MyDbHelper.getInstance(BankMainActivity.this);
        db.open();
        db.delete("TBL_BANK_CARD_INFO",   "ID =?", new String[] { String.valueOf(id)});   
        db.close();
        }catch ( Exception e ){
            e.printStackTrace();
           
        }
        
    }
//public void onBackPressed() {
////        
////        Intent i=new Intent(BankMainActivity.this,MainTabActivity.class);//  MainActivity MainTabActivity
////        startActivity(i);
////        BankMainActivity.this.finish();
//    }

}
    
