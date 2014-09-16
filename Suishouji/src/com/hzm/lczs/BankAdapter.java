package com.hzm.lczs;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.hzm.lczs.db.MyDbHelper;
import com.hzm.lczs.R;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BankAdapter extends BaseAdapter{

    private Context mContext;
    public List<BankInfo> bankList;
    private MyDbHelper db = null;
 
    private String cardNum;
    private int alarmId; //作为闹钟ID
    private int alarmType;
    private int billDate;             //getNumbers;
    private int repayDate;

    public BankAdapter(Context context ,List<BankInfo> appList) {
        super();
        
        mContext = context;
        bankList=appList;
    }
    
    
    public int getCount() {
        return bankList.size();
    }

    public Object getItem(int position) {
        return bankList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
      
        if(convertView==null) convertView = LayoutInflater.from(mContext).inflate(R.layout.bank_list, null);
        
        RelativeLayout  list = (RelativeLayout) convertView.findViewById(R.id.list);
        TextView     bankName = (TextView) convertView.findViewById(R.id.bank_name);
        ImageView    line=(ImageView) convertView.findViewById(R.id.line);
        TextView     cardNum = (TextView) convertView.findViewById(R.id.card_num);
        TextView     detailText=(TextView) convertView.findViewById(R.id.detail);
               
        BankInfo wb = bankList.get(position);
        
        if(wb!=null){
            convertView.setTag(wb.getId());  
            bankName.setText(getBankName(wb));
            cardNum.setText(formatNum(wb.getCardNum()));
            detailText.setText("距离还款日还有"+getRepayDay(wb.getId())+"天");
            int a=wb.getId()%6;
            switch(a)
            {
             
              case 0: 
            	
            	  list.setBackgroundResource(R.drawable.card_list_bg1_normal);//R.drawable.
            	  line.setBackgroundResource(R.drawable.card_list_line1);
            	  break;
              case 1: 
            	  list.setBackgroundResource(R.drawable.card_list_bg2_normal);
            	  line.setBackgroundResource(R.drawable.card_list_line2);
            	  break;
              case 2: 
            	  list.setBackgroundResource(R.drawable.card_list_bg3_normal);
            	  line.setBackgroundResource(R.drawable.card_list_line3);
            	  break;
              case 3: 
            	  list.setBackgroundResource(R.drawable.card_list_bg4_normal);
            	  line.setBackgroundResource(R.drawable.card_list_line4);
            	  break;
              case 4: 
            	  list.setBackgroundResource(R.drawable.card_list_bg5_normal);
            	  line.setBackgroundResource(R.drawable.card_list_line5);
            	  break;
              case 5: 
            	  list.setBackgroundResource(R.drawable.card_list_bg6_normal);
            	  line.setBackgroundResource(R.drawable.card_list_line6);
            	  break;
              
              default: 
            	  list.setBackgroundResource(R.drawable.card_list_bg1_normal);
            	  line.setBackgroundResource(R.drawable.card_list_line1);
            	  break;
            
            } 
        }
        
        return convertView;
        }
    
    private String getBankName(BankInfo wb){
        String pName=null;
        db = MyDbHelper.getInstance(mContext);
        db.open();
//        select  NAME  from TBL_BANK_TYPE
        Cursor cursor = db.rawQuery("select SHORTNAME  from TBL_BANK_TYPE  where ID=?"
                ,new String[]{ String.valueOf(wb.getBankId()) });
        
        while (cursor.moveToNext()) {
            if(cursor.getString(0) != null)
            { //     
               pName=cursor.getString(cursor.getColumnIndex("SHORTNAME"));
               
                Log.e("hzm",  pName);
            }
            
        }
        cursor.close();
        db.close();
      
        return pName;
        
    }
    
    private int getRepayDay(int id){
        
        db = MyDbHelper.getInstance(mContext);
        db.open();
        Cursor cursor = db.rawQuery("select  *  from TBL_BANK_CARD_INFO where ID=? " ,new String[]{
                String.valueOf(id)});
        while (cursor.moveToNext()) {   
                Log.e("hzm",  "ge"+cursor.getCount());
                
                 cardNum=cursor.getString(cursor.getColumnIndex("CARD_NUM"));
                 alarmId=cursor.getInt(cursor.getColumnIndex("ID")); //作为闹钟ID
                 alarmType=cursor.getInt(cursor.getColumnIndex("REPAYMENT_TYPE")); //还款类型
                 billDate=cursor.getInt(cursor.getColumnIndex("BILL_DATE"));       //账单日
                 repayDate=cursor.getInt(cursor.getColumnIndex("REPAYMENT_DATE")); //还款日
                 
        }
        cursor.close();
        db.close();
        
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(System.currentTimeMillis());
        c1.set(Calendar.HOUR_OF_DAY, 9);  //将时间设置为当日 9:00
        c1.set(Calendar.MINUTE, 0);
        
        BankRecordAlarm alarm=new BankRecordAlarm(mContext);
        Calendar c2=alarm.calculateAlarmDate(alarmType, billDate, repayDate);

        return daysBetween(c1,c2);
    }
    
     
    private  int daysBetween(Calendar cal1,Calendar cal2)     
    {      
        long time1 = cal1.getTimeInMillis();                      
        long time2 = cal2.getTimeInMillis();          
        long between_days=(time2-time1)/(1000*3600*24);     
             
       return Integer.parseInt(String.valueOf(between_days));            
    }   
    
    private String formatNum(String s) {
        
        String a=null;
        int i=0;
        while ((s.length()-i)>3)  {
            
            if(a==null) a=s.substring(i, i+4)+" ";
            else a=a+s.substring(i, i+4)+" ";
            i=i+4;
            
        }
        a=a+s.substring(i, s.length());
        return a;
       }
    
    
}    

