package com.hzm.lczs;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.text.method.NumberKeyListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hzm.lczs.calendar.CalendarActivity;
import com.hzm.lczs.db.MyDbHelper;
import com.hzm.lczs.db.MyDbInfo;
import com.hzm.lczs.R;

public class AddBankRecordActivity  extends Activity implements OnClickListener,OnCheckedChangeListener,OnItemSelectedListener

{
    //新增银行记录
    private Button card_num_btn;
    private Spinner bank_name_btn;
    private Spinner card_catery_btn;
    private Button record_date_btn;
    private Spinner  account_date_catgeroy_spn;
    private Button back_date_btn;
    private Button count_btn;
    private Button other_record_btn;
    private Button save_btn;
    private EditText edit;
    private AlertDialog dialog = null;
    private List<String> list = null;
    private MyDbHelper db = null;
    private ArrayAdapter<String> adapter;
    private String  value="0";

    final static int ADD_MODE = 1;
    final static int EDIT_MODE = 2;
    private int type = ADD_MODE;//操作类型、1新增、2编辑
    BankInfo data;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bank_record);
        
        Intent intent = getIntent();
        type = intent.getIntExtra("mode", 1);
        data = (BankInfo)getIntent().getParcelableExtra("data");
        
        loadingFormation();
        initdata();
               
    }
    
    private void initdata(){
        
        if(type==EDIT_MODE)  //编辑模式 初始化值
        {
            card_num_btn.setText(data.CardNum);  
            record_date_btn.setText("每月"+data.BillDate+"日");
            
//            back_date_btn.setText(String.valueOf(data.RePayDate));
            if(account_date_catgeroy_spn.getSelectedItemId()==0) 
                
                back_date_btn.setText("账单之后"+data.RePayDate+"天还款");
            else 
                back_date_btn.setText("每月"+data.RePayDate+"日");
            
            count_btn.setText(DecimalFormat.getCurrencyInstance().format(data.CreditLimit));
            value = String.valueOf(String.format("%.2f", data.CreditLimit));
            other_record_btn.setText(data.Remark); 
        }      
        BankNameSpn();
        CardTypeSpn();
        RePayDateSpn();
        

    }
    private void loadingFormation(){
      
        card_num_btn=(Button)findViewById(R.id.card_num_btn); //卡号
        card_num_btn.setOnClickListener(this);
        
        
        bank_name_btn=(Spinner)findViewById(R.id.bank_name_btn); //银行
        bank_name_btn.setOnItemSelectedListener(this);
        
        card_catery_btn=(Spinner)findViewById(R.id.card_catery_btn);//类别
        card_catery_btn.setOnItemSelectedListener(this);
        
        record_date_btn=(Button)findViewById(R.id.record_date_btn); //账单日
        record_date_btn.setOnClickListener(this);
        
        account_date_catgeroy_spn = (Spinner) findViewById(R.id.account_date_catgeroy_spn); //还款日类型
        account_date_catgeroy_spn.setOnItemSelectedListener(this);
        
        back_date_btn=(Button)findViewById(R.id.back_date_btn);  //还款日
        back_date_btn.setOnClickListener(this);
        
        count_btn=(Button)findViewById(R.id.count_btn); //额度
        count_btn.setOnClickListener(this);
          
        other_record_btn = (Button) findViewById(R.id.other_record_btn); //备注
        other_record_btn.setOnClickListener(this);
        
        save_btn = (Button) findViewById(R.id.save_btn); //保存
        save_btn.setOnClickListener(this);
        
          
    }
    
       
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if(resultCode==RESULT_OK) {
            int year = data.getIntExtra("year", 0);
            int month = data.getIntExtra("month", 0);
            int day = data.getIntExtra("day", 0);
            final Calendar dat = Calendar.getInstance();
            dat.set(Calendar.YEAR, year);
            dat.set(Calendar.MONTH, month);
            dat.set(Calendar.DAY_OF_MONTH, day);
            
            SimpleDateFormat format = new SimpleDateFormat("yyyy MMM dd");
            
            //Toast.makeText(AddBankRecordActivity.this, format.format(dat.getTime()), Toast.LENGTH_LONG).show();
            
            if(requestCode==10)  record_date_btn.setText("每月"+String.valueOf(day)+"日");//getNumbers
            if(requestCode==20)  //back_date_btn.setText(String.valueOf(day));
            {
                
                if(account_date_catgeroy_spn.getSelectedItemId()==0) 
                    
                    back_date_btn.setText("账单之后"+day+"天还款");
                else 
                    back_date_btn.setText("每月"+day+"日");
              
            }
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View view, int position, long arg3)
    {
        // TODO Auto-generated method stub
      
        if(view == bank_name_btn.getSelectedView()){//银行名称

//            if(type == EDIT_MODE){            
//                bank_name_btn.setSelection(data.BankId-1);
//            }
        }
        if(view == card_catery_btn.getSelectedView()){//类别
            
//            if(type == EDIT_MODE){
//                card_catery_btn.setSelection(data.CardType-1);
//            }
            
        }
        if(view == account_date_catgeroy_spn.getSelectedView()){//还款日类型
            
//            if(type == EDIT_MODE){
//                account_date_catgeroy_spn.setSelection(data.RePayType-1);
//            }
            
            if(account_date_catgeroy_spn.getSelectedItemId()==0&&getNumbers(back_date_btn.getText().toString())!=null) 
            
                back_date_btn.setText("账单之后"+getNumbers(back_date_btn.getText().toString())+"天还款");
            else if(getNumbers(back_date_btn.getText().toString())!=null)
                back_date_btn.setText("每月"+getNumbers(back_date_btn.getText().toString())+"日");
          }
        
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onClick(View v)
    {
            
        if(v == card_num_btn){ //卡号
            
            edit = new EditText(this);  
            
            DigitsKeyListener numericOnlyListener = new DigitsKeyListener(false, true); //只输入数字
            edit.setKeyListener(numericOnlyListener);
            
            edit.setLines(1);
            edit.setText(card_num_btn.getText());
            dialog = new AlertDialog.Builder(this)
            .setTitle("银行卡帐号")
            .setView(edit)
            .setPositiveButton(getString(R.string.dialog_memo_ok), new DialogInterface.OnClickListener() {
                
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    card_num_btn.setText(edit.getText());
                }
            }).setNegativeButton(getString(R.string.dialog_memo_cancle), new DialogInterface.OnClickListener() {
                
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub               
                }
            }).show();
           
        }
       
        if(v == record_date_btn){//账单日
            startActivityForResult(new Intent(Intent.ACTION_PICK).setDataAndType(null, CalendarActivity.MIME_TYPE), 10);
        }
        if(v == back_date_btn){//还款日
            startActivityForResult(new Intent(Intent.ACTION_PICK).setDataAndType(null, CalendarActivity.MIME_TYPE), 20);
        }
        if(v == count_btn){//额度
            
            edit = new EditText(this);
            
            DigitsKeyListener numericOnlyListener = new DigitsKeyListener(false, true); //只输入数字
            edit.setKeyListener(numericOnlyListener);
            
            edit.setLines(1);
            
            if (!TextUtils.isEmpty(count_btn.getText().toString()))
            edit.setText(DecimalFormat.getCurrencyInstance().format(Double.parseDouble(count_btn.getText().toString().replaceAll("\\￥", "").replaceAll("\\,", ""))));
            dialog = new AlertDialog.Builder(this)
            .setTitle("额度")
            .setView(edit)
            .setPositiveButton(getString(R.string.dialog_memo_ok), new DialogInterface.OnClickListener() {
                
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    if(edit.getText().toString().length()>0){
                    count_btn.setText(DecimalFormat.getCurrencyInstance().format(Double.parseDouble(edit.getText().toString().replaceAll("\\￥", "").replaceAll("\\,", ""))));
                    value = String.valueOf(String.format("%.2f", Double.parseDouble(edit.getText().toString().replaceAll("\\￥", "").replaceAll("\\,", ""))));
                    }
                }
            }).setNegativeButton(getString(R.string.dialog_memo_cancle), new DialogInterface.OnClickListener() {
                
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub               
                }
            }).show();
            
        }
        if(v == other_record_btn){//备注
           
            edit = new EditText(this);
            edit.setLines(5);
            edit.setText(other_record_btn.getText());
            dialog = new AlertDialog.Builder(this)
            .setTitle("备注")
            .setView(edit)
            .setPositiveButton(getString(R.string.dialog_memo_ok), new DialogInterface.OnClickListener() {
                
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    other_record_btn.setText(edit.getText());
                }
            }).setNegativeButton(getString(R.string.dialog_memo_cancle), new DialogInterface.OnClickListener() {
                
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub               
                }
            }).show();
        }
        if(v == save_btn){
            saveInfo();
        }
        // TODO Auto-generated method stub
        
    }
    
    
    private void BankNameSpn(){
        
        list = new ArrayList<String>();//子类别
        
        db = MyDbHelper.getInstance(AddBankRecordActivity.this);
        db.open();
        Cursor cursor = db.rawQuery("select  NAME  from TBL_BANK_TYPE" ,null);
        while (cursor.moveToNext()) {   
                Log.e("hzm",  "ge"+cursor.getCount());
                
                String bName=cursor.getString(cursor.getColumnIndex("NAME"));
                list.add(bName);
                Log.e("hzm",  bName);
        }
        cursor.close();
        db.close();
   
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bank_name_btn.setAdapter(adapter);
        
        if(type == EDIT_MODE){            
            bank_name_btn.setSelection(data.BankId-1);
        }

    }
    
    private void CardTypeSpn(){//信用卡类型
        
        list = new ArrayList<String>();//
        
        db = MyDbHelper.getInstance(AddBankRecordActivity.this);
        db.open();
        Cursor cursor = db.rawQuery("select  NAME  from TBL_BANK_CARD_TYPE" ,null);
        while (cursor.moveToNext()) {   
                Log.e("hzm",  "ge"+cursor.getCount());
                
                String bName=cursor.getString(cursor.getColumnIndex("NAME"));
                list.add(bName);
                Log.e("hzm",  bName);
        }
        cursor.close();
        db.close();
   
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        card_catery_btn.setAdapter(adapter);
        
        if(type == EDIT_MODE){
            card_catery_btn.setSelection(data.CardType-1);
        }

    }
    
   private void RePayDateSpn(){ //还款日类型
        
        list = new ArrayList<String>();
        
        db = MyDbHelper.getInstance(AddBankRecordActivity.this);
        db.open();
        Cursor cursor = db.rawQuery("select  NAME  from TBL_BANK_BACK_DATE_TYPE" ,null);
        while (cursor.moveToNext()) {   
                Log.e("hzm",  "ge"+cursor.getCount());
                
                String bName=cursor.getString(cursor.getColumnIndex("NAME"));
                list.add(bName);
                Log.e("hzm",  bName);
        }
        cursor.close();
        db.close();
   
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        account_date_catgeroy_spn.setAdapter(adapter);
        
        if(type == EDIT_MODE){
            account_date_catgeroy_spn.setSelection(data.RePayType-1);
        }
        

    }
   
   private void saveInfo(){
       
		// verify item values
		if (TextUtils.isEmpty(card_num_btn.getText().toString())) {
			Toast.makeText(getApplicationContext(), "卡号不能为空！",Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (card_num_btn.getText().toString().length()>20||card_num_btn.getText().toString().length()<15) {
            Toast.makeText(getApplicationContext(), "卡号不符合规范",Toast.LENGTH_SHORT).show();
            return;
        }
		
		
		if (TextUtils.isEmpty(record_date_btn.getText().toString())) {
			Toast.makeText(getApplicationContext(),  "账单日不能为空！",Toast.LENGTH_SHORT).show();
			return;
		}
		
	    if (TextUtils.isEmpty(back_date_btn.getText().toString())) {
			Toast.makeText(getApplicationContext(),  "还款日不能为空！",Toast.LENGTH_SHORT).show();
			return;
		}
	   
	   
      
       String fieldNames[] = null;
       String values[] = null;
       BankRecordAlarm alarm=new BankRecordAlarm(AddBankRecordActivity.this);
       db = MyDbHelper.getInstance(AddBankRecordActivity.this);
       db.open();
       //{"ID","CARD_NUM","BANK_ID","CARD_TYPE","BILL_DATE","REPAYMENT_TYPE","REPAYMENT_DATE","CREDIT_LIMIT","REMARK"},
    
       int alarmType=account_date_catgeroy_spn.getSelectedItemPosition()+1;
       int billDate= Integer.parseInt(getNumbers(record_date_btn.getText().toString()));             //getNumbers;
       int repayDate= Integer.parseInt(getNumbers(back_date_btn.getText().toString()));
       
       if(type == EDIT_MODE){
    	   
           fieldNames = new String[]{"CARD_NUM","BANK_ID","CARD_TYPE","BILL_DATE","REPAYMENT_TYPE","REPAYMENT_DATE","CREDIT_LIMIT","REMARK","ALARM_ID"};
           values = new String[]{
                   card_num_btn.getText().toString(),
                   String.valueOf(bank_name_btn.getSelectedItemPosition()+1),
                   String.valueOf(card_catery_btn.getSelectedItemPosition()+1),
                   getNumbers(record_date_btn.getText().toString()),               //getNumbers
                   String.valueOf(account_date_catgeroy_spn.getSelectedItemPosition()+1),
                   getNumbers(back_date_btn.getText().toString()),
                   value,
                   other_record_btn.getText().toString(),
                   String.valueOf(data.AlarmId)
           };
    	   
           alarm.CancelAlarmNotify(data.AlarmId);//取消之前的闹钟
           db.update("TBL_BANK_CARD_INFO", fieldNames, values, "ID=" + data.Id, null);
           alarm.setAlarm(alarm.calculateAlarmDate(alarmType, billDate, repayDate), data.AlarmId);//设置新闹钟
       }else{
    	   
    	   int alarmId=-1; //作为闹钟ID 
           Cursor cursor = db.rawQuery("select  ID  from TBL_BANK_CARD_INFO" ,null);
           if(cursor.getCount()>0){
               while (cursor.moveToNext()) {   
                   Log.e("hzm",  "ge"+cursor.getCount());
                   
                   alarmId=cursor.getInt(cursor.getColumnIndex("ID"));
               }   
           } else alarmId=0;
           alarmId=alarmId+1;
    	   fieldNames = new String[]{"CARD_NUM","BANK_ID","CARD_TYPE","BILL_DATE","REPAYMENT_TYPE","REPAYMENT_DATE","CREDIT_LIMIT","REMARK","ALARM_ID"};
           values = new String[]{
                   card_num_btn.getText().toString(),
                   String.valueOf(bank_name_btn.getSelectedItemPosition()+1),
                   String.valueOf(card_catery_btn.getSelectedItemPosition()+1),
                   getNumbers(record_date_btn.getText().toString()),               //getNumbers
                   String.valueOf(account_date_catgeroy_spn.getSelectedItemPosition()+1),
                   getNumbers(back_date_btn.getText().toString()),
                   value,
                   other_record_btn.getText().toString(),
                   String.valueOf(alarmId)
                  
           };
                            
           db.insert("TBL_BANK_CARD_INFO", fieldNames,values);

           cursor.close(); 
           alarm.setAlarm(alarm.calculateAlarmDate(alarmType, billDate, repayDate), alarmId);
       }
       
       db.close();
       exit();
   }
   private void exit(){
       
//           Intent intent = new Intent(AddBankRecordActivity.this,BankMainActivity.class);
//           startActivity(intent);
       AddBankRecordActivity.this.finish();
   }
    
   
   public void onBackPressed() {
	    

		AlertDialog.Builder builder = new AlertDialog.Builder(AddBankRecordActivity.this);
		builder.setTitle("警告");
		builder.setMessage("确定要放弃保存吗？");

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				AddBankRecordActivity.this.finish();
			}
		});
		
		builder.setNegativeButton("取消", null);

		builder.create().show();
	
	}
   
   //截取数字  
   public String getNumbers(String content) {  
       Pattern pattern = Pattern.compile("\\d+");  
       Matcher matcher = pattern.matcher(content);  
       while (matcher.find()) {  
           return matcher.group(0);  
       }  
       return null;  
   }  
   

}
