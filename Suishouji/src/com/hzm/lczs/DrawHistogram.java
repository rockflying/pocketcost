package com.hzm.lczs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.achartengine.ChartFactory;  
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;  
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;  
import org.achartengine.renderer.XYSeriesRenderer;  

import com.hzm.lczs.db.MyDbHelper;
  
import android.content.Context;  
import android.content.Intent;  
import android.database.Cursor;
import android.graphics.Color;  
import android.graphics.Paint.Align; 
import android.util.Log;
import android.view.View;

//画柱状图
public class DrawHistogram

{
    
        private MyDbHelper db = null;
        private double  mExpenseValue[]={0,0,0,0,0,0,0,0,0,0,0,0}; 
        private double  mIncomeValue[]={0,0,0,0,0,0,0,0,0,0,0,0}; 
        private double  abundanceValue[]={0,0,0,0,0,0,0,0,0,0,0,0};
        private double  highMax=0;
        private double  lowMin=0;
        
        private String  month[];
        
        private void DataPrepare(Context mContent){ //收入查询
            
        db = MyDbHelper.getInstance(mContent);
        db.open();
        int i=0;  //每月消费统计
       //select sum(AMOUNT) as summount , substr(DATE,6,2) as date from TBL_EXPENDITURE  group by substr(DATE,6,2);
        Cursor cursor = db.rawQuery("select sum(AMOUNT) as summount , substr(DATE,6,2) as month from TBL_EXPENDITURE  group by substr(DATE,6,2)"
                ,null);
        
        month = new String[cursor.getCount()];  //字符串数组必须初始化
      
        while (cursor.moveToNext()) {
            if(cursor.getString(0) != null)
            { //
                Log.e("hzm",  "ge"+cursor.getCount());
                
                month[i]=cursor.getString(cursor.getColumnIndex("month"));
                mExpenseValue[Integer.parseInt(month[i])] = cursor.getDouble(cursor.getColumnIndex("summount"));
                
                Log.e("hzm",  month[i]+"消费:"+mExpenseValue[Integer.parseInt(month[i])]);
            }
            i++;
        }
        cursor.close();
        
        i=0; //每月收入
        cursor = db.rawQuery("select sum(AMOUNT) as summount , substr(DATE,6,2) as month from TBL_INCOME  group by substr(DATE,6,2)"
            ,null);
    
        month = new String[cursor.getCount()];  //字符串数组必须初始化
      
        while (cursor.moveToNext()) {
            if(cursor.getString(0) != null)
            { //
                Log.e("hzm",  "ge"+cursor.getCount());
                
                month[i]=cursor.getString(cursor.getColumnIndex("month"));
                mIncomeValue[Integer.parseInt(month[i])] = cursor.getDouble(cursor.getColumnIndex("summount"));
                
                Log.e("hzm",  month[i]+"收入:"+mIncomeValue[Integer.parseInt(month[i])]);
            }
            i++;
        }
        
        cursor.close(); 
        db.close();
        
    }
    
        
       
       
       public  void getMaxAndMin(double[] arr)  
        {  
            int max = 0;  
            int min = 0; 
      
            for(int x=1; x<arr.length; x++)  
            {  
                if(arr[x]>arr[max])   max = x;  
                if(arr[x]<arr[min])   min = x; 
            }  
            if(arr[max]>highMax) highMax= arr[max];  
            if(arr[min]<lowMin)  lowMin =arr[min]; 
        }  
        
      public GraphicalView execute(Context context) {  

          String[] titles = new String[] { "收入", "消费", "盈余" };
          List<double[]> x = new ArrayList<double[]>();
          for (int i = 0; i < titles.length; i++) {
              x.add(new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 });
          }
          
          DataPrepare(context);
          
          List<double[]> values = new ArrayList<double[]>();
          values.add(mIncomeValue);
          values.add(mExpenseValue);
          
          for (int i=0;i<mIncomeValue.length;i++){
              
              abundanceValue[i]=mIncomeValue[i]-mExpenseValue[i];
              Log.e("hzm",  "盈余"+abundanceValue[i]);
              
          }
          
          values.add(abundanceValue);
          
          getMaxAndMin(mIncomeValue);
          getMaxAndMin(mExpenseValue);
          getMaxAndMin(abundanceValue);
             
          int[] colors = new int[] { Color.BLUE, Color.GREEN, Color.RED };
          
          PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.DIAMOND, PointStyle.SQUARE };
          XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
          int length = renderer.getSeriesRendererCount();
          for (int i = 0; i < length; i++) {
              ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
          }
          setChartSettings(renderer, "全年收支图", "月份", "金额", 0.5, 12.5, lowMin, highMax, Color.WHITE, Color.WHITE);//LTGRAY
          renderer.setXLabels(12);
          renderer.setYLabels(12);
          renderer.setShowGrid(true);
          renderer.setXLabelsAlign(Align.RIGHT);
          renderer.setYLabelsAlign(Align.LEFT);
          renderer.setZoomButtonsVisible(true);
          
          renderer.setApplyBackgroundColor(true);
          renderer.setBackgroundColor(Color.argb(0, 220, 228, 234));//
          renderer.setMarginsColor(Color.argb(0, 220, 228, 234));//
          
          renderer.setPanLimits(new double[] { 0, 12, lowMin, highMax });
          renderer.setZoomLimits(new double[] { 0, 12, lowMin, highMax });
          
          return ChartFactory.getLineChartView(context, buildDataset(titles, x, values), renderer); 
      }  
      

      private XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles) {
          XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
          setRenderer(renderer, colors, styles);
          return renderer;
      }

      private void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors, PointStyle[] styles) {
          renderer.setAxisTitleTextSize(15);
          renderer.setChartTitleTextSize(16);
          renderer.setLabelsTextSize(14);
          renderer.setLegendTextSize(14);
          renderer.setPointSize(5f);
          renderer.setMargins(new int[] { 20, 30, 15, 20 });
          int length = colors.length;
          for (int i = 0; i < length; i++) {
              XYSeriesRenderer r = new XYSeriesRenderer();
              r.setColor(colors[i]);
              r.setPointStyle(styles[i]);
              renderer.addSeriesRenderer(r);
          }
      }

      private void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle, String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor, int labelsColor) {
          renderer.setChartTitle(title);
          renderer.setXTitle(xTitle);
          renderer.setYTitle(yTitle);
          renderer.setXAxisMin(xMin);
          renderer.setXAxisMax(xMax);
          renderer.setYAxisMin(yMin);
          renderer.setYAxisMax(yMax);
          renderer.setAxesColor(axesColor);
          renderer.setLabelsColor(labelsColor);
      }

      private XYMultipleSeriesDataset buildDataset(String[] titles, List<double[]> xValues, List<double[]> yValues) {
          XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
          addXYSeries(dataset, titles, xValues, yValues, 0);
          return dataset;
      }

      private void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles, List<double[]> xValues, List<double[]> yValues, int scale) {
          int length = titles.length;
          for (int i = 0; i < length; i++) {
              XYSeries series = new XYSeries(titles[i], scale);
              double[] xV = xValues.get(i);
              double[] yV = yValues.get(i);
              int seriesLength = xV.length;
              for (int k = 0; k < seriesLength; k++) {
                  series.add(xV[k], yV[k]);
              }
              dataset.addSeries(series);
          }
      }
  }
