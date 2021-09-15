package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ThemeData themeData;
    int AreaSize = 9;
    int ScreenWidth;
    boolean isResumeGame = false;
    //GameData - класса для сохраненного сеанса игры
    GameData gameData;
    boolean isResumeActivity = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        themeData = new ThemeData(getFilesDir());
        ThemeChanged();
        gameData = new GameData(getFilesDir());
        ResumeGame();
        isResumeActivity = true;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if(!isResumeActivity)
        {
            return;
        }
        themeData.openChoiceFile();
        ThemeChanged();
        isResumeGame = false;
        ResumeGame();
    }

    void ResumeGame()
    {
        // проверка на наличие сохраненного сеанса игры
        gameData.openDataFile();
        if(gameData.FileExists() && gameData.isSavedGameData)
        {
            // отбражение "кнопки" для продолжения игры (LinerLayout с кнопками)
            (findViewById(R.id.ResumeGameLL)).setVisibility(View.VISIBLE);
            Button ResumeGameInfo = (Button) findViewById(R.id.ResumeGameInfo);
            AreaSize = gameData.AreaSize;
            String lvlchoice = "";
            // вывод информации о сохраненном сеансе игры
            switch (gameData.lvlchoice)
            {
                case 0:
                {
                    lvlchoice = "Low";
                    break;
                }
                case 1:
                {
                    lvlchoice = "Middle";
                    break;
                }
                case 2:
                {
                    lvlchoice = "High";
                    break;
                }
            }
            String InfoText = gameData.timeText + " " + AreaSize + "x" + AreaSize + " " + lvlchoice;
            ResumeGameInfo.setText(InfoText);
        }
        else
        {
            (findViewById(R.id.ResumeGameLL)).setVisibility(View.INVISIBLE);
        }
        // определение размерности игрового поля
        String s = "9x9";
        if (AreaSize == 16)
        {
            s = "16x16";
        }
        Button ChoiceSizebtn = (Button) findViewById(R.id.ChoiceSizebtn);
        // изменение размерности игрового поля если оно иное
        if (!ChoiceSizebtn.getText().toString().equals(s))
        {
            ChSizeOnClick(ChoiceSizebtn);
        }
    }




    public void ResumeGameClick(View view)
    {
        isResumeGame = true;
        NewGame(view);
    }

    public void NewGame(View view)
    {
        ScreenWidth = (findViewById(R.id.MainCL)).getWidth();
        Intent intent = new Intent(this, GameBase.class);
        intent.putExtra("AreaSize", AreaSize);
        intent.putExtra("ScreenWidth", ScreenWidth);
        intent.putExtra("isResumeGame", isResumeGame);
        startActivity(intent);
    }

    public void StylebtnClick(View view)
    {
        ScreenWidth = (findViewById(R.id.MainCL)).getWidth();
        new StyleDialog(this, themeData, new ThemeData(getFilesDir()));
    }

    public void ThemeChanged()
    {
        // применяет выбранную тему из объекта themeData
        themeData.openChoiceFile();
        findViewById(R.id.MainCL).setBackgroundColor(themeData.mainback);
        ((TextView) findViewById(R.id.Recordsbtn)).setTextColor(themeData.textcolor1);
        ((TextView) findViewById(R.id.Stylebtn)).setTextColor(themeData.textcolor1);
        ((TextView) findViewById(R.id.MainTitle)).setTextColor(themeData.textcolor1);
        ((TextView) findViewById(R.id.SizeTV)).setTextColor(themeData.textcolor1);
        ((Button) findViewById(R.id.ResumeGameTitle)).setTextColor(themeData.textcolor1);
        ((Button) findViewById(R.id.ResumeGameInfo)).setTextColor(themeData.textcolor2);
        ((Button) findViewById(R.id.StartNewGamebtn)).setTextColor(themeData.textcolor1);
        ((Button) findViewById(R.id.ChoiceSizebtn)).setTextColor(themeData.textcolor1);
        ((RadioButton) findViewById(R.id.RB9)).setTextColor(themeData.textcolor1);
        ((RadioButton) findViewById(R.id.RB16)).setTextColor(themeData.textcolor1);
        (findViewById(R.id.Recordsbtn)).setBackgroundColor(themeData.btnseconary);
        (findViewById(R.id.Stylebtn)).setBackgroundColor(themeData.btnseconary);
        (findViewById(R.id.Stylebtn)).setBackgroundColor(themeData.btnseconary);
        (findViewById(R.id.ResumeGameLL)).setBackgroundColor(themeData.btnseconary);
        (findViewById(R.id.ResumeGameTitle)).setBackgroundColor(themeData.btnseconary);
        (findViewById(R.id.ResumeGameInfo)).setBackgroundColor(themeData.btnseconary);
        (findViewById(R.id.StartNewGamebtn)).setBackgroundColor(themeData.btnseconary);
        (findViewById(R.id.ChoiceSizebtn)).setBackgroundColor(themeData.btnseconary);
        (findViewById(R.id.ArrowDownbtn)).setBackgroundColor(themeData.btnseconary);
        ((ImageButton) findViewById(R.id.ArrowDownbtn)).setColorFilter(themeData.textcolor1);

    }

    public void ArrowDownOnClick(View view)
    {
        ScrollView RadioSV = (ScrollView) findViewById(R.id.RadioSV);
        if (RadioSV.getVisibility() == View.VISIBLE)
        {
            RadioSV.setVisibility(ProgressBar.INVISIBLE);
        }
        else
        {
            RadioSV.setVisibility(ProgressBar.VISIBLE);
        }
    }

    public void ChSizeOnClick(View view)
    {
        Button ChoiceSizebtn = (Button) findViewById(R.id.ChoiceSizebtn);
        RadioButton RB9 = (RadioButton) findViewById(R.id.RB9);
        RadioButton RB16 = (RadioButton) findViewById(R.id.RB16);
        switch (AreaSize)
        {
            case 9:
                AreaSize = 16;
                ChoiceSizebtn.setText("16x16");
                RB9.setChecked(false);
                RB16.setChecked(true);
                break;
            case 16:
                AreaSize = 9;
                ChoiceSizebtn.setText("9x9");
                RB9.setChecked(true);
                RB16.setChecked(false);
                break;
        }
    }

    public void RadioOnClick(View view)
    {
        Button ChoiceSizebtn = (Button) findViewById(R.id.ChoiceSizebtn);
        switch (view.getId())
        {
            case R.id.RB9:
                AreaSize = 9;
                ChoiceSizebtn.setText("9x9");
                break;
            case R.id.RB16:
                AreaSize = 16;
                ChoiceSizebtn.setText("16x16");
                break;
        }
    }

    public void RecordsbtnClick(View view)
    {
        Intent intent = new Intent(this, TimeRecordsActivity.class);
        startActivity(intent);
    }



}