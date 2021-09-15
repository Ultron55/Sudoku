package com.example.sudoku;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import yuku.ambilwarna.AmbilWarnaDialog;

public class CustomThemeSetting extends AppCompatActivity {

    ThemeData themeData;
    int textcolor1;
    int textcolor2;
    int SudokuAreaback;
    int btnprimary;
    int btnseconary;
    int mainback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_theme_setting);
        themeData = new ThemeData(getFilesDir());
        if (themeData.FileExists())
        {
            themeData.openDataFile();
        }
        textcolor1 = themeData.textcolor1;
        textcolor2 = themeData.textcolor2;
        SudokuAreaback = themeData.SudokuAreaback;
        btnprimary = themeData.btnprimary;
        btnseconary = themeData.btnseconary;
        mainback = themeData.mainback;
        ThemeChanged();
    }

    void ThemeChanged()
    {
        findViewById(R.id.mCL).setBackgroundColor(mainback);
        findViewById(R.id.ColorListTV).setBackgroundColor(btnprimary);
        ((TextView) findViewById(R.id.ColorListTV)).setTextColor(textcolor1);
        findViewById(R.id.mainbackbtn).setBackgroundColor(mainback);
        ((Button) findViewById(R.id.mainbackbtn)).setTextColor(textcolor1);
        findViewById(R.id.btnprimbtn).setBackgroundColor(btnprimary);
        ((Button) findViewById(R.id.btnprimbtn)).setTextColor(textcolor1);
        ((Button) findViewById(R.id.btnprimt)).setTextColor(textcolor1);
        ((Button) findViewById(R.id.btnprimt3)).setTextColor(textcolor1);
        ((Button) findViewById(R.id.btnsect)).setTextColor(textcolor1);
        ((Button) findViewById(R.id.btnsect3)).setTextColor(textcolor2);
        ((Button) findViewById(R.id.btnprimt4)).setTextColor(textcolor2);
        findViewById(R.id.btnsecbtn).setBackgroundColor(btnseconary);
        ((Button) findViewById(R.id.btnsecbtn)).setTextColor(textcolor1);
        findViewById(R.id.SudokuAreabackbtn).setBackgroundColor(SudokuAreaback);
        ((Button) findViewById(R.id.SudokuAreabackbtn)).setTextColor(textcolor1);
        findViewById(R.id.text1btn).setBackgroundColor(textcolor1);
        ((Button) findViewById(R.id.text1btn)).setTextColor(textcolor2);
        findViewById(R.id.text2btn).setBackgroundColor(textcolor2);
        ((Button) findViewById(R.id.text2btn)).setTextColor(textcolor1);
        findViewById(R.id.BaseStyle).setBackgroundColor(SudokuAreaback);
        findViewById(R.id.btnprimt1).setBackgroundColor(btnprimary);
        ((Button) findViewById(R.id.btnprimt1)).setTextColor(textcolor1);
        findViewById(R.id.btnsect1).setBackgroundColor(btnseconary);
        ((Button) findViewById(R.id.btnsect1)).setTextColor(textcolor1);
        findViewById(R.id.btnsect2).setBackgroundColor(btnseconary);
        ((Button) findViewById(R.id.btnsect2)).setTextColor(textcolor2);
        findViewById(R.id.btnprimt2).setBackgroundColor(btnprimary);
        ((Button) findViewById(R.id.btnprimt2)).setTextColor(textcolor2);
        findViewById(R.id.Cancelbtn).setBackgroundColor(btnprimary);
        ((Button) findViewById(R.id.Cancelbtn)).setTextColor(textcolor1);
        findViewById(R.id.Savebtn).setBackgroundColor(btnprimary);
        ((Button) findViewById(R.id.Savebtn)).setTextColor(textcolor1);
    }



    public void onbtnClick(final View view)
    {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, ((ColorDrawable) view.getBackground()).getColor(), true, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            public void onOk(AmbilWarnaDialog dialog, int color) {
                boolean error = false;
                switch (((Button) view).getText().toString())
                {
                    case "Main background":
                    {
                        if (color == btnseconary)
                        {
                            error = true;
                        }
                        else
                        {
                            mainback = color;
                        }
                        break;
                    }
                    case "Game backgroud":
                    {
                        if (color == btnseconary)
                        {
                            error = true;
                        }
                        else
                        {
                            SudokuAreaback = color;
                        }
                        break;
                    }
                    case "First cell color":
                    {
                        if (color == btnseconary)
                        {
                            error = true;
                        }
                        else
                        {
                            btnprimary = color;
                        }
                        break;
                    }
                    case "Second cell color":
                    {if (color == btnseconary)
                    {
                        error = true;
                    }
                    else
                    {
                        btnseconary = color;
                    }
                        break;
                    }
                    case "First text color ":
                    {
                        if (color == btnseconary)
                        {
                            error = true;
                        }
                        else
                        {
                            textcolor1 = color;
                        }
                        break;
                    }
                    case "Second text color":
                    {
                        if (color == btnseconary)
                        {
                            error = true;
                        }
                        else
                        {
                            textcolor2 = color;
                        }
                        break;
                    }
                }
                if (error)
                {
                    Toast toast = Toast.makeText(CustomThemeSetting.this, "Colors should not be repeated", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, Gravity.CENTER, Gravity.CENTER);
                    toast.show();
                }
                else
                {
                    ThemeChanged();
                }
            }

            public void onCancel(AmbilWarnaDialog dialog) {
                return;
            }
        });
        dialog.show();
        Button bb = (Button) dialog.getDialog().getButton(AlertDialog.BUTTON_POSITIVE);
        ((Button) dialog.getDialog().getButton(AlertDialog.BUTTON_POSITIVE)).setTextColor(textcolor1);
        ((Button) dialog.getDialog().getButton(AlertDialog.BUTTON_NEGATIVE)).setTextColor(textcolor1);
        bb.setTextColor(textcolor1);
        dialog.getDialog().findViewById(R.id.ambilwarna_dialogView).setBackgroundColor(btnprimary);
        ((ViewGroup) bb.getParent()).setBackgroundColor(btnprimary);
    }

    public void onSavebtnClick(View view)
    {
        themeData.choicethme = 2;
        themeData.textcolor1 = textcolor1;
        themeData.textcolor2 = textcolor2;
        themeData.SudokuAreaback = SudokuAreaback;
        themeData.btnprimary = btnprimary;
        themeData.btnseconary = btnseconary;
        themeData.mainback = mainback;
        themeData.saveDataFile();
        this.finish();
    }


    public void onCancelbtnClick(View view)
    {
        this.finish();
    }



}