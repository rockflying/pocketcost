package com.hzm.lczs;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TabWidget;
import android.widget.TextView;

import com.hzm.lczs.api.TabHostActivity;
import com.hzm.lczs.api.TabItem;


/**
 * 
整个流程就像使用ListView自定BaseAdapter一样

 * 
 * 
如果要自定义TabHostActivity的Theme，并且不想要头部阴影
 * 一定要添加这个android:windowContentOverlay = null

 * 
 * 
如果想在别的项目里面使用TabHostActivity
 * 可以项目的属性里面找到Android，然后在Library部分添加这个项目(Api)
 * http://www.cnblogs.com/qianxudetianxia/archive/2011/05/01/2030232.html">如何添加

 * */
public class MainTabActivity extends TabHostActivity {

    List<TabItem> mItems;
    private LayoutInflater mLayoutInflater;
    
    /**在初始化TabWidget前调用
     * 和TabWidget有关的必须在这里初始化*/
    @Override
    protected void prepare() {
        TabItem home = new TabItem(
                "记账",                                    // title
               // R.drawable.icon_home,    
                R.drawable.nav_record,   
                R.drawable.example_tab_item_bg,            // background
                new Intent(this, MainActivity.class));    // intent
        
        TabItem info = new TabItem(
                "账户",
                R.drawable.nav_account,//icon_selfinfo,
                R.drawable.example_tab_item_bg,
                new Intent(this, SettingAccountActivity.class));
        
        TabItem msg = new TabItem(
                "还款",
                R.drawable.nav_repay,//icon_meassage,
                R.drawable.example_tab_item_bg,
                new Intent(this, BankMainActivity.class));//CalendarActivity  AddBankRecordActivity TestActivity
        
        TabItem square = new TabItem(
                "预算",
                R.drawable.nav_budget,//icon_square,
                R.drawable.example_tab_item_bg,
                new Intent(this, BudgetActivity.class));
        
        TabItem more = new TabItem(
                "统计",
                R.drawable.nav_report,//R.drawable.icon_more,
                R.drawable.example_tab_item_bg,
                new Intent(this, AchartengineActivity.class));
        
        mItems = new ArrayList<TabItem>();
        mItems.add(home);
        mItems.add(info);
        mItems.add(msg);
        mItems.add(square);
        mItems.add(more);

        // 设置分割线
        TabWidget tabWidget = getTabWidget();
        tabWidget.setDividerDrawable(R.drawable.tab_divider);
        
        mLayoutInflater = getLayoutInflater();
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCurrentTab(0);
    }
 
    /**tab的title，icon，边距设定等等*/
    @Override
    protected void setTabItemTextView(TextView textView, int position) {
        textView.setPadding(3, 3, 3, 3);
        textView.setText(mItems.get(position).getTitle());
        textView.setBackgroundResource(mItems.get(position).getBg());
        textView.setCompoundDrawablesWithIntrinsicBounds(0, mItems.get(position).getIcon(), 0, 0);
        
    }
    
    /**tab唯一的id*/
    @Override
    protected String getTabItemId(int position) {
        return mItems.get(position).getTitle();    // 我们使用title来作为id，你也可以自定
    }

    /**点击tab时触发的事件*/
    @Override
    protected Intent getTabItemIntent(int position) {
        return mItems.get(position).getIntent();
    }

    @Override
    protected int getTabItemCount() {
        return mItems.size();
    }
    
    /**自定义头部文件*/
    @Override
    protected View getTop() {
        return mLayoutInflater.inflate(R.layout.example_top, null);
    }
    
    
    
    @Override
    protected void onTabChangedListen(String tabid,View topView) {
    	
    	TextView topTitleText =(TextView)topView.findViewById(R.id.example_center);
    	ImageView topTitleImg =(ImageView)topView.findViewById(R.id.example_right);
    	//@+id/example_right
    	//form_add_btn
    	
    	if((tabid).equals("记账"))  { topTitleText.setText("记账"); topTitleImg.setVisibility(View.INVISIBLE); }
    	
    	else if((tabid).equals("账户"))  { topTitleText.setText("账户管理"); topTitleImg.setVisibility(View.INVISIBLE); }
    	
    	else if((tabid).equals("还款")) 
    	{  
    	   topTitleText.setText("添加银行卡信息");
    	   topTitleImg.setVisibility(View.VISIBLE);
    	   topTitleImg.setBackgroundResource(R.drawable.form_add_btn);
    	   topTitleImg.setOnClickListener(new OnClickListener(){
            
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				 Intent in = new Intent(MainTabActivity.this, AddBankRecordActivity.class);
                 startActivity(in);
                // finish();
				
			}
    		   
    		   
    	   });
        }
    	else if((tabid).equals("预算"))  { topTitleText.setText("预算管理"); topTitleImg.setVisibility(View.INVISIBLE); }
    	else if((tabid).equals("统计"))  { topTitleText.setText("图表统计"); topTitleImg.setVisibility(View.INVISIBLE); }
    }
    
}
