package com.example.sudoku;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import java.lang.reflect.Method;

public class StyleDialog extends AlertDialog
{

    AlertDialog dialog;
    Method themechanged;

    public StyleDialog(@NonNull final Context context, final ThemeData themeData, ThemeData TD)
    {
        super(context);
        try
        {
            themechanged = context.getClass().getMethod("ThemeChanged", null);
        } catch (NoSuchMethodException e) {}
        dialog2show = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View dialog_layout = getLayoutInflater().inflate(R.layout.style_choice_dialog, null);
        ((ConstraintLayout) dialog_layout.findViewById(R.id.dialogCL)).setBackgroundColor(themeData.mainback);
        ((TextView) dialog_layout.findViewById(R.id.TitleTV)).setTextColor(themeData.textcolor2);
        final TextView[] tvs = new TextView[3];
        tvs[0] = dialog_layout.findViewById(R.id.BaseStTV);
        tvs[1] = dialog_layout.findViewById(R.id.DarkStTV);
        tvs[2] = dialog_layout.findViewById(R.id.CustomStTV);
        leftbtn = (Button) dialog_layout.findViewById(R.id.leftbtn);
        leftbtn.setBackgroundColor(themeData.btnprimary);
        leftbtn.setTextColor(themeData.textcolor1);
        rightbtn = (Button) dialog_layout.findViewById(R.id.rightbtn);
        rightbtn.setBackgroundColor(themeData.btnprimary);
        rightbtn.setTextColor(themeData.textcolor1);
        final ViewGroup[] grids = new ViewGroup[3];
        grids[0] = (ViewGroup) dialog_layout.findViewById(R.id.BaseStyle);
        grids[1] = (ViewGroup) dialog_layout.findViewById(R.id.DarkStyle);
        grids[2] = (ViewGroup) dialog_layout.findViewById(R.id.CustomStyle);
        final ImageButton[] imgs = new ImageButton[3];
        imgs[0] = (ImageButton) dialog_layout.findViewById(R.id.BaseChoice);
        imgs[1] = (ImageButton) dialog_layout.findViewById(R.id.DarkChoice);
        imgs[2] = (ImageButton) dialog_layout.findViewById(R.id.CustomChoice);
        imgs[themeData.choicethme].setVisibility(View.VISIBLE);
        final Method[] themevoids = new Method[3];
        try {
            themevoids[0] = TD.getClass().getMethod("Basetheme", null);
            themevoids[1] = TD.getClass().getMethod("Graytheme", null);
            themevoids[2] = TD.getClass().getMethod("openDataFile", null);
        } catch (Exception e) {}
        for (int i = 0; i < 3; i++)
        {
            try {
                themevoids[i].invoke(TD, null);
            } catch (Exception e) {}
            tvs[i].setTextColor(themeData.textcolor1);
            grids[i].setBackgroundColor(TD.SudokuAreaback);
            imgs[i].setColorFilter(TD.textcolor1);
            grids[i].getChildAt(0).setBackgroundColor(TD.btnprimary);
            ((Button) grids[i].getChildAt(0)).setTextColor(TD.textcolor1);
            grids[i].getChildAt(1).setBackgroundColor(TD.btnseconary);
            ((Button) grids[i].getChildAt(1)).setTextColor(TD.textcolor2);
            grids[i].getChildAt(2).setBackgroundColor(TD.select);
            ((Button) grids[i].getChildAt(2)).setTextColor(TD.textcolor1);

            grids[i].getChildAt(3).setBackgroundColor(TD.btnseconary);
            ((Button) grids[i].getChildAt(3)).setTextColor(TD.textcolor1);
            grids[i].getChildAt(4).setBackgroundColor(TD.btnprimary);
            ((Button) grids[i].getChildAt(4)).setTextColor(TD.textcolor2);
            grids[i].getChildAt(5).setBackgroundColor(TD.Xcolor);
            ((Button) grids[i].getChildAt(5)).setTextColor(TD.textcolor1);

            grids[i].getChildAt(6).setBackgroundColor(TD.Xcolor);
            ((Button) grids[i].getChildAt(6)).setTextColor(TD.textcolor2);
            grids[i].getChildAt(7).setBackgroundColor(TD.errorvalue);
            ((Button) grids[i].getChildAt(7)).setTextColor(TD.textcolor2);
            grids[i].getChildAt(8).setBackgroundColor(TD.selecterrorvalue);
            ((Button) grids[i].getChildAt(8)).setTextColor(TD.textcolor1);
        }

        dialog_layout.findViewById(R.id.f1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Imagechoice(0, imgs);
            }
        });
        imgs[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Imagechoice(0, imgs);
            }
        });
        dialog_layout.findViewById(R.id.f2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Imagechoice(1, imgs);
            }
        });
        imgs[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Imagechoice(1, imgs);
            }
        });
        dialog_layout.findViewById(R.id.f3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Imagechoice(2, imgs);
            }
        });
        imgs[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Imagechoice(2, imgs);
            }
        });

        leftbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog2show)
                {
                    dialog2show = false;
                    themeData.choicethme = 2;
                    themeData.openDataFile();
                    themeData.saveDataFile();
                    try
                    {
                        themechanged.invoke(context);
                    }
                    catch (Exception e) {}
                    dialog.cancel();
                }
                else
                {

                    dialog.cancel();
                }
            }
        });
        rightbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!dialog2show)
                {
                    int choice = 0;
                    for (int i = 0; i < 3; i++)
                    {
                        if (imgs[i].getVisibility() == View.VISIBLE)
                        {
                            choice = i;
                            break;
                        }
                    }
                    if (choice == 2)
                    {
                        if (!themeData.FileExists())
                        {
                            Intent intent = new Intent(context, CustomThemeSetting.class);
                            context.startActivity(intent);
                            dialog.cancel();
                        }
                        else
                        {
                            for (int i = 0; i < 3; i++)
                            {
                                ((ViewGroup) grids[i].getParent()).setVisibility(View.INVISIBLE);
                                ((TextView) tvs[i]).setVisibility(View.INVISIBLE);
                                ((TextView) dialog_layout.findViewById(R.id.TitleTV)).setText("OPEN PALLETE?");
                                leftbtn.setText("NO");
                                rightbtn.setText("YES");

                            }
                            dialog2show = true;
                        }
                    }
                    if (!dialog2show)
                    {
                        themeData.choicethme = choice;
                        themeData.saveDataFile();
                        try
                        {
                            themechanged.invoke(context);
                        }
                        catch (Exception e) {}
                        dialog.cancel();
                    }
                }
                else
                {
                    Intent intent = new Intent(context, CustomThemeSetting.class);
                    context.startActivity(intent);
                    dialog.cancel();
                }
            }
        });
        builder.setView(dialog_layout).setCancelable(true);
        dialog = builder.create();
        dialog.show();
    }

    public void Imagechoice(int i, ImageButton[] imgs)
    {
        imgs[i].setVisibility(View.VISIBLE);
        for (int j = 0; j < 3; j++)
        {
            if (j == i)
            {
                continue;
            }
            imgs[j].setVisibility(View.INVISIBLE);
        }
    }

    boolean dialog2show = false;
    Button leftbtn;
    Button rightbtn;

}
