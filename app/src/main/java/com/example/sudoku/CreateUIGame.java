package com.example.sudoku;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.ArrayList;


public class CreateUIGame
{
    Activity activity;
    GridLayout SudokuArea;
    GridLayout ValuesArea;
    GridLayout CheckboxsArea;
    ProgressBar prbar;
    Chronometer chronometer;
    int AreaSize;
    int SqrtAreaSize;
    float densityVal;
    float scaledDensityVal;
    int textcolor1;
    int btnprimary;
    int btnseconary;
    int mainback;
    int[][] SectorsIndexes;
    int[] SectorsBtnPrimaryColor;
    Drawable chbox_off;
    public int textsizeNormal = 14;
    public int textsizePencil = 8;
    public int textsizeValuesArea = 28;
    boolean isCreatechboxAndValuesAreabool = false;
    int SudokuAreaWidth;
    int LValuesAreawidth;
    int wbtnOfSudokuArea;
    int hbtnOfSudokuArea;
    int wbtnOfValuesArea;
    int hbtnOfValuesArea;
    int wchbox;
    int hchbox;
    int Margin5dp;
    int Margin1dp;
    int btnTopMargin;
    int btnRightMargin;
    int px (float d) { return Math.round(d * densityVal); }
    int dip (float px) { return Math.round(px / scaledDensityVal); }
    boolean isThreadCreateUIGame = false;
    int index;

    CreateUIGame(Activity activity, GridLayout SudokuArea, GridLayout ValuesArea,
                 GridLayout CheckboxsArea, ProgressBar prbar, Chronometer chronometer,
                 int AreaSize, int SqrtAreaSize, float densityVal, float scaledDensityVal,
                 int textcolor1, int btnprimary, int btnseconary, int mainback,
                 int[][] SectorsIndexes, int[] SectorsBtnPrimaryColor, int SudokuAreaWidth,
                 Drawable chbox_off)
    {
        this.activity = activity;
        this.SudokuArea = SudokuArea;
        this.ValuesArea = ValuesArea;
        this.CheckboxsArea = CheckboxsArea;
        this.prbar = prbar;
        this.chronometer = chronometer;
        this.AreaSize = AreaSize;
        this.SqrtAreaSize = SqrtAreaSize;
        this.densityVal = densityVal;
        this.scaledDensityVal = scaledDensityVal;
        this.textcolor1 = textcolor1;
        this.btnprimary = btnprimary;
        this.btnseconary = btnseconary;
        this.mainback = mainback;
        this.SectorsIndexes = SectorsIndexes;
        this.SectorsBtnPrimaryColor = SectorsBtnPrimaryColor;
        this.SudokuAreaWidth = SudokuAreaWidth;
        this.chbox_off = chbox_off;
        CreateUIGame();
    }
    
    Handler handler = new Handler();

    void CreateUIGame()
    {
        isThreadCreateUIGame = true;
        index = 1000;
        isCreatechboxAndValuesAreabool = false;
        Margin5dp = px(5f);
        Margin1dp = px(1f);
        btnTopMargin = px(1f);
        btnRightMargin = px(1.2f);
        activity.findViewById(R.id.SettingbtnON).setEnabled(false);
        Runnable CreateUIGameRunnble = new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        index = 1000;
                        chronometer.setText("00:00");
                        Button Xbtn = (Button) activity.findViewById(R.id.Xbtn);
                        Xbtn.setTextSize(dip(Xbtn.getHeight() * 0.6f));
                        ViewGroup.LayoutParams tlp = SudokuArea.getLayoutParams();
                        tlp.height = SudokuAreaWidth;
                        SudokuArea.setLayoutParams(tlp);
                        tlp.width = SudokuAreaWidth;
                        tlp = prbar.getLayoutParams();
                        tlp.width = SudokuAreaWidth;
                        tlp.height = SudokuAreaWidth;
                        prbar.setLayoutParams(tlp);
                        LValuesAreawidth = activity.findViewById(R.id.LValuesArea).getWidth();
                        int SudokuAreaminus = Math.round(SudokuAreaWidth - (px(4) * 2) - Margin1dp * (AreaSize - SqrtAreaSize + 1)
                                - Margin5dp * (SqrtAreaSize - 1));
                        wbtnOfSudokuArea =  Math.round((SudokuAreaminus - btnRightMargin * AreaSize) / AreaSize);
                        hbtnOfSudokuArea =  Math.round((SudokuAreaminus - btnTopMargin * AreaSize) / AreaSize);
                        wbtnOfValuesArea = Math.round(LValuesAreawidth / 12);
                        hbtnOfValuesArea = wbtnOfValuesArea;
                        wchbox = wbtnOfValuesArea;
                        hchbox = wchbox;
                        textsizeNormal = dip(wbtnOfSudokuArea * 0.7f );
                        textsizeValuesArea = dip(wbtnOfValuesArea * 0.7f);
                        textsizePencil = Math.round(textsizeNormal / 2f);
                        index = 0;
                    }
                });
                while (index == 1000) {
                    try
                    {
                        Thread.currentThread().sleep(1);
                    }
                    catch (InterruptedException e) {e.printStackTrace();}
                }
                SudokuAreabtnCreate();
            }
        };
        Thread CreateUIGameThread = new Thread(CreateUIGameRunnble);
        CreateUIGameThread.start();
    }


    boolean sb = false;
    void SudokuAreabtnCreate()
    {
        final ArrayList<GridLayout.LayoutParams> layoutParamsbtnOfSudokuArea = new ArrayList<GridLayout.LayoutParams>();
        final Button[] btnOfSudokuArea = new Button[AreaSize * AreaSize];
        int row;
        int column;
        int leftMargin;
        int bottomMargin;
        int color;
        boolean isMargin5dp = false;
        for (int i = 0; i < AreaSize * AreaSize; i++)
        {
            btnOfSudokuArea[i] = new Button(activity);
            btnOfSudokuArea[i].setTextColor(textcolor1);
            btnOfSudokuArea[i].setTextSize(textsizeNormal);
            layoutParamsbtnOfSudokuArea.add(new GridLayout.LayoutParams());
            column = i % AreaSize;
            row = Math.round((i - column) / AreaSize);
            layoutParamsbtnOfSudokuArea.get(i).columnSpec = GridLayout.spec(column);
            layoutParamsbtnOfSudokuArea.get(i).rowSpec = GridLayout.spec(row);
            layoutParamsbtnOfSudokuArea.get(i).width = wbtnOfSudokuArea;
            layoutParamsbtnOfSudokuArea.get(i).height = hbtnOfSudokuArea;
            layoutParamsbtnOfSudokuArea.get(i).setGravity(Gravity.CENTER);
            btnOfSudokuArea[i].setPadding(0,0, 0, 0);
            isMargin5dp = false;
            for (int j = 1; j < SqrtAreaSize; j++)
            {
                if (column == SqrtAreaSize * j)
                {
                    isMargin5dp = true;
                    break;
                }
            }
            if (isMargin5dp)
            {
                leftMargin = Margin5dp;
            }
            else
            {
                leftMargin = Margin1dp;
            }
            isMargin5dp = false;
            for (int j = 1; j < SqrtAreaSize; j++)
            {
                if (row == SqrtAreaSize * j - 1)
                {
                    isMargin5dp = true;
                }
            }
            if (isMargin5dp)
            {
                bottomMargin = Margin5dp;
            }
            else
            {
                bottomMargin = Margin1dp;
            }
            layoutParamsbtnOfSudokuArea.get(i).setMargins(leftMargin, btnTopMargin, btnRightMargin, bottomMargin);
            if (isBtnPrimaryColor(i))
            {
                color = btnprimary;
            }
            else
            {
                color = btnseconary;
            }
            btnOfSudokuArea[i].setBackgroundColor(color);
            sb = false;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (!isCreatechboxAndValuesAreabool)
                    {
                        SudokuArea.addView(btnOfSudokuArea[index], layoutParamsbtnOfSudokuArea.get(index));
                        index++;
                        if (index == AreaSize * AreaSize)
                        {
                            index = 0;
                            isCreatechboxAndValuesAreabool = true;
                        }
                    }
                }
            });
        }
        while (!isCreatechboxAndValuesAreabool) {
            try
            {
                Thread.currentThread().sleep(1);
            }
            catch (InterruptedException e) {e.printStackTrace();}
        }
        chboxAndValuesAreabtnCreate();
    }

    boolean isBtnPrimaryColor(int index)
    {
        int c = index % AreaSize;
        int numb = SectorsIndexes[Math.round((index - c) / AreaSize)][c];
        for(int i = 0; i < SectorsBtnPrimaryColor.length; i++)
        {
            if (numb == SectorsBtnPrimaryColor[i])
            {
                return true;
            }
        }
        return false;
    }

    void chboxAndValuesAreabtnCreate()
    {
        final ArrayList<GridLayout.LayoutParams> layoutParamsbtnOfValuesArea = new ArrayList<GridLayout.LayoutParams>();
        final Button[] btnOfValuesArea = new Button[AreaSize];
        final ImageButton[] chbox = new ImageButton[AreaSize];
        final String[] ValuesAreaText = new String[]
                {"1", "2", "3", "4", "5", "6", "7", "8", "9",
                        "A", "B", "C", "D", "E", "F", "G"};
        int column;
        int row;
        index = 0;
        for (int i = 0; i < AreaSize; i++)
        {
            btnOfValuesArea[i] = new Button(activity);
            btnOfValuesArea[i].setTextColor(textcolor1);
            btnOfValuesArea[i].setTextSize(textsizeValuesArea);
            btnOfValuesArea[i].setPadding(0,0,0,0);
            btnOfValuesArea[i].setBackgroundColor(btnprimary);
            btnOfValuesArea[i].setText(ValuesAreaText[i]);
            chbox[i] = new ImageButton(activity);
            chbox[i].setPadding(0, 0, 0, 0);
            chbox[i].setImageDrawable(chbox_off);
            chbox[i].setScaleType(ImageView.ScaleType.FIT_CENTER);
            chbox[i].setMaxWidth(px(40));
            chbox[i].setMaxHeight(px(40));
            chbox[i].setColorFilter(textcolor1);
            chbox[i].setBackgroundColor(mainback);
            column = i % 9;
            row = Math.round((i - column) / 9);
            layoutParamsbtnOfValuesArea.add(new GridLayout.LayoutParams());
            layoutParamsbtnOfValuesArea.get(i).columnSpec = GridLayout.spec(column);
            layoutParamsbtnOfValuesArea.get(i).rowSpec = GridLayout.spec(row);
            layoutParamsbtnOfValuesArea.get(i).setMargins(px(2f), px(1.5f), px(1.5f), px(1.5f));
            layoutParamsbtnOfValuesArea.get(i).width = wbtnOfValuesArea;
            layoutParamsbtnOfValuesArea.get(i).height = hbtnOfValuesArea;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (index < AreaSize)
                    {
                        ValuesArea.addView(btnOfValuesArea[index], layoutParamsbtnOfValuesArea.get(index));
                        CheckboxsArea.addView(chbox[index], layoutParamsbtnOfValuesArea.get(index));
                        index++;
                        if (index == AreaSize)
                        {
                            ViewGroup.LayoutParams layoutParams = activity.findViewById(R.id.Eraserbtn).getLayoutParams();
                            layoutParams.width = wbtnOfValuesArea;
                            layoutParams.height = hbtnOfValuesArea;
                            activity.findViewById(R.id.Eraserbtn).setLayoutParams(layoutParams);
                            layoutParams = activity.findViewById(R.id.Pencilbtn).getLayoutParams();
                            layoutParams.width = wbtnOfValuesArea;
                            layoutParams.height = hbtnOfValuesArea;
                            activity.findViewById(R.id.Pencilbtn).setLayoutParams(layoutParams);
                            layoutParams = activity.findViewById(R.id.Xbtn).getLayoutParams();
                            layoutParams.width = wbtnOfValuesArea;
                            layoutParams.height = hbtnOfValuesArea;
                            activity.findViewById(R.id.Xbtn).setLayoutParams(layoutParams);
                            activity.findViewById(R.id.SettingbtnON).setEnabled(true);
                            isThreadCreateUIGame = false;
                        }
                    }
                }
            });
        }
    }
}
