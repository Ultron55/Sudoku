package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TimeRecordsActivity extends AppCompatActivity {

    public LinearLayout[] lvl = new LinearLayout[2];
    public ViewGroup.LayoutParams[] layoutParams = new LinearLayout.LayoutParams[2];
    public boolean[] b = new boolean[2];
    TimeRecordsData timerecordsdata;
    ThemeData themeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_records);
        lvl[0] = (LinearLayout) findViewById(R.id.lvl9);
        lvl[1] = (LinearLayout) findViewById(R.id.lvl16);
        for (int i = 0; i < lvl.length; i++)
        {
            layoutParams[i] = lvl[i].getLayoutParams();
            b[i] = true;
        }
        timerecordsdata = new TimeRecordsData(getFilesDir());
        themeData = new ThemeData(getFilesDir());
        findViewById(R.id.recordmainCL).setBackgroundColor(themeData.btnseconary);
        findViewById(R.id.backlist).setBackgroundColor(themeData.textcolor1);
        findViewById(R.id.up9).setBackgroundColor(themeData.mainback);
        ((TextView) findViewById(R.id.t9)).setTextColor(themeData.textcolor1);
        ((TextView) findViewById(R.id.plus9)).setTextColor(themeData.textcolor1);
        findViewById(R.id.up16).setBackgroundColor(themeData.mainback);
        ((TextView) findViewById(R.id.t16)).setTextColor(themeData.textcolor1);
        ((TextView) findViewById(R.id.plus16)).setTextColor(themeData.textcolor1);
        LinearLayout lvlll;
        for (int k = 0; k < 2; k++)
        {
            for (int i = 0; i < 3; i++) {
                lvlll = (LinearLayout) lvl[k].getChildAt(i);
                TextView tv = (TextView) lvlll.getChildAt(0);
                tv.setBackgroundColor(themeData.btnprimary);
                tv.setTextColor(themeData.textcolor2);
            }
        }
        createlist();
    }

    void createlist()
    {
        int timetextsize = 18;
        TextView tv;
        LinearLayout.LayoutParams lp2;
        LinearLayout lvlll;
        for (int k = 0; k < 2; k++)
        {
            for (int i = 0; i < 3; i++)
            {
                lvlll = (LinearLayout) lvl[k].getChildAt(i);
                for (int j = 0; j < timerecordsdata.datalist[k][i].size(); j++)
                {
                    tv = new Button(this);
                    tv.setText(timerecordsdata.datalist[k][i].get(j));
                    tv.setTextColor(themeData.textcolor1);
                    tv.setTextSize(timetextsize);
                    tv.setBackgroundColor(themeData.btnseconary);
                    lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    lp2.setMargins(0,0, 0, 7);
                    lvlll.addView(tv, lp2);
                }
            }
        }
    }

    public void plus9Clicl(View view)
    {
        listaction(0);
    }

    public void plus16Clicl(View view)
    {
        listaction(1);
    }

    void listaction(int i)
    {
        if (b[i])
        {
            lvl[i].setLayoutParams(new LinearLayout.LayoutParams(0, 0));
            b[i] = false;
        }
        else
        {
            lvl[i].setLayoutParams(layoutParams[i]);
            b[i] = true;
        }
    }

}