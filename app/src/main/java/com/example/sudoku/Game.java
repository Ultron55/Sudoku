package com.example.sudoku;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;


public class Game extends AppCompatActivity{

    public int[][] mas = new int[9][9];
    public int[][] mrand = new int[9][9];
    public int level = 30;
    public GridLayout table1;
    public LinearLayout Numbers;
    public LinearLayout Checkboxs;
    public int idbtn = 100;
    public Object[] tagx = new Object[5];
    public ProgressBar prbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        prbar = (ProgressBar) findViewById(R.id.progressBar);
        table1  = (GridLayout) findViewById(R.id.table1);
        Button newbtn = (Button) findViewById(R.id.newbtn);
        final Button choicebtn = (Button) findViewById(R.id.choicebtn);
        Numbers = (LinearLayout) findViewById(R.id.Numbers);
        Checkboxs = (LinearLayout) findViewById(R.id.Checkboxs);
        tagx[0] = table1.getChildAt(0).getTag();
        tagx[1] = table1.getChildAt(7).getTag();
        tagx[2] = table1.getChildAt(30).getTag();
        tagx[3] = table1.getChildAt(54).getTag();
        tagx[4] = table1.getChildAt(80).getTag();
        creatematrix();
        choiceleveldialog();
        choicebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choiceleveldialog();
            }
        });

    }

    public void onnewbtnClick(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("NEW GAME")
                .setMessage("YOU SURE?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new ShowDialogAsyncTask().execute();
                    }
                })
                .setNeutralButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                })
                .setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void choiceleveldialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Choice level")
                .setMessage("LEVEL")
                .setNeutralButton("Low", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        level = 25;
                        new ShowDialogAsyncTask().execute();
                    }
                })
                .setNegativeButton("Middle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        level = 35;
                        new ShowDialogAsyncTask().execute();
                    }
                })
                .setPositiveButton("High", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        level = 45;
                        new ShowDialogAsyncTask().execute();
                    }
                })
                .setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onMyButtonClick(View view)
    {
        if (view.getParent().equals(table1))
        {
            int color = 0;
            Resources res = getResources();
            if (idbtn != 100)
            {
                Button btnN = (Button) table1.getChildAt(idbtn);
                ColorDrawable bcolor = (ColorDrawable) btnN.getBackground();
                if (bcolor.getColor() == res.getColor(R.color.errornumb))
                {
                    color = bcolor.getColor();
                }
                else
                {

                    if (    btnN.getTag().equals(tagx[0]) ||
                            btnN.getTag().equals(tagx[1]) ||
                            btnN.getTag().equals(tagx[2]) ||
                            btnN.getTag().equals(tagx[3]) ||
                            btnN.getTag().equals(tagx[4]))
                    {
                        color = getResources().getColor(R.color.xcolor);
                    }
                    else
                    {
                        color = getResources().getColor(R.color.pluscolor);
                    }
                }
                btnN.setBackgroundColor(color);
            }
            idbtn = ((GridLayout) view.getParent()).indexOfChild(view);
            view.setBackgroundColor(getResources().getColor(R.color.select));
        }
        if (view.getParent().equals(Numbers))
        {
            if (idbtn != 100)
            {
                Button btnN = (Button) table1.getChildAt(idbtn);
                if (btnN.getCurrentTextColor() != getResources().getColor(R.color.kor))
                {
                    if (view.getTag().equals(Numbers.getChildAt(Numbers.getChildCount() - 1).getTag()))
                    {
                        btnN.setText(" ");
                    }
                    else
                    {
                        btnN.setText(view.getTag().toString());
                    }
                    win();
                }
            }
        }
    }

    public void ClickCheckbox(View view)
    {
        CheckBox ch = (CheckBox) view;
        int n = Checkboxs.indexOfChild(view);
        int v = 0;
        if (ch.isChecked())
        {
            v = 4;
        }
        else
        {
            v = 0;
        }
        ((Button) Numbers.getChildAt(n)).setVisibility(v);
    }

    public void win()
    {
        boolean nowin = false;
        boolean b = true;
        int cred = getResources().getColor(R.color.errornumb);
        int x = 0;
        int color = 0;
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                x = (i * 9) + j;
                Button btnN = (Button) table1.getChildAt(x);
                b = true;
                if (btnN.getText().toString() == " ")
                {
                    nowin = true;
                    b = false;
                }
                if (checkcell(i, j) && b)
                {
                    btnN.setBackgroundColor(cred);
                    nowin = true;
                }
                else
                {
                    if (    btnN.getTag().equals(tagx[0]) ||
                            btnN.getTag().equals(tagx[1]) ||
                            btnN.getTag().equals(tagx[2]) ||
                            btnN.getTag().equals(tagx[3]) ||
                            btnN.getTag().equals(tagx[4]))
                    {
                        color = getResources().getColor(R.color.xcolor);
                    }
                    else
                    {
                        color = getResources().getColor(R.color.pluscolor);
                    }
                    if(x == idbtn)
                    {
                        color = getResources().getColor(R.color.select);
                    }
                    btnN.setBackgroundColor(color);
                }
            }
        }
        if (!nowin)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setTitle("YOU WIN!")
                    .setMessage("VICTORY")
                    .setNegativeButton("Choice level", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            choiceleveldialog();
                        }
                    })
                    .setPositiveButton("New game", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new ShowDialogAsyncTask().execute();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }


    public void creatematrix()
    {
        int k = 1;
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                if (k > 9)
                {
                    k = k - 9;
                }
                if ((i == 3 || i == 6) && j == 0)
                {
                    k++;
                }
                mas[i][j] = k;
                k++;
            }
            k = mas[i][0] + 3;
        }
    }

    int[][] mascop(int[][] m2)
    {
        int[][] m1 = new int[m2.length][];
        for (int i = 0; i < m1.length; i++) {
            m1[i] = new int[m2[i].length];
            System.arraycopy(m2[i], 0, m1[i], 0, m1[i].length);
        }
        return m1;
    }

    void textcolor(int color, boolean all)
    {
        for (int i = 0; i < 81; i++)
        {
            Button btnN = (Button) table1.getChildAt(i);
            if (!all)
            {
                if (btnN.getText().equals(" "))
                    {
                        continue;
                    }
            }
            btnN.setTextColor(color);
        }
    }


    private class ShowDialogAsyncTask extends AsyncTask<Void, Integer, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prbar.setVisibility(ProgressBar.VISIBLE);
            for (int i = 0; i < 9; i++)
            {
                ((Button) Numbers.getChildAt(i)).setVisibility(View.VISIBLE);
                ((CheckBox) Checkboxs.getChildAt(i)).setChecked(false);
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            new_game();
            return (null);
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            prbar.setVisibility(ProgressBar.INVISIBLE);
        }
    }

    public void new_game()
    {
        idbtn = 100;
        int color = getResources().getColor(R.color.gold);
        textcolor(color, true);
        ArrayList<int[][]> sa = new ArrayList<int[][] >();
        mrand = mascop(mas);
        Random random = new Random();
        sa.add(swap_line(mrand, true));
        sa.add(swap_line(mrand, false));
        sa.add(swap_area(mrand, true));
        sa.add(swap_area(mrand, false));
        int r = 0;
        for (int i = 0; i < 40; i++)
        {
            r = random.nextInt(4);
            mrand = sa.get(r);
        }
        clearcells();
        color = getResources().getColor(R.color.kor);
        textcolor(color, false);
        for (int i = 0; i < 81; i++)
        {
            Button btnN = (Button) table1.getChildAt(i);
            if (    btnN.getTag().equals(tagx[0]) ||
                    btnN.getTag().equals(tagx[1]) ||
                    btnN.getTag().equals(tagx[2]) ||
                    btnN.getTag().equals(tagx[3]) ||
                    btnN.getTag().equals(tagx[4]))
            {
                color = getResources().getColor(R.color.xcolor);
            }
            else
            {
                color = getResources().getColor(R.color.pluscolor);
            }
            btnN.setBackgroundColor(color);
        }

    }

    int[][] swap_line(int[][] mrand, boolean row)
    {
        Random random = new Random();
        int area = random.nextInt(3);
        int line1 = random.nextInt(3);
        int line2;
        for (; ;)
        {
            line2 = random.nextInt(3);
            if (line2 != line1)
            {
                break;
            }
        }
        line1 = area * 3 + line1;
        line2 = area * 3 + line2;
        int[] line = new int[9];
        for (int i = 0; i < 9; i++)
        {
            if (row)
            {
                line[i] = mrand[line1][i];
                mrand[line1][i] = mrand[line2][i];
                mrand[line2][i] = line[i];
            }
            else
            {
                line[i] = mrand[i][line1];
                mrand[i][line1] = mrand[i][line2];
                mrand[i][line2] = line[i];
            }

        }
        return mrand;
    }

    int[][] swap_area(int[][] mrand, boolean row)
    {
        Random random = new Random();
        int area1 = random.nextInt(3);
        int area2 = random.nextInt(3);
        while (area1 == area2)
        {
            area2 = random.nextInt(3);
        }
        int[] area = new int[27];
        area1 = area1 * 3;
        area2 = area2 * 3;
        int j = 0;
        int k = 0;
        for (int i = 0; i < 27; i ++)
        {
            if (row)
            {
                area[i] = mrand[area1 + k][j];
                mrand[area1 + k][j] = mrand[area2 + k][j];
                mrand[area2 + k][j] = area[i];
            }
            else
            {
                area[i] = mrand[j][area1 + k];
                mrand[j][area1 + k] = mrand[j][area2 + k];
                mrand[j][area2 + k] = area[i];
            }
            j++;
            if (j == 9)
            {
                j = 0;
                k++;
            }
        }
        return mrand;
    }

    void clearcells()
    {
        ArrayList<int[]> null_cells = new ArrayList<int[]>();
        int[] cell = new int[3];
        int[][] mc = mascop(mrand);
        Random rand = new Random();
        int countsol = 0;
        while (null_cells.size() < level)
        {
            cell[0] = rand.nextInt(9);
            cell[1] = rand.nextInt(9);
            if (mc[cell[0]][cell[1]] == 0)
            {
                continue;
            }
            cell[2] = mc[cell[0]][cell[1]];
            mc[cell[0]][cell[1]] = 0;
            null_cells.add(new int[] { cell[0], cell[1], 1 });
            int n = 0;
            countsol = nullcheck(n, null_cells);
        }
        mc = mascop(mrand);
        for (int m = 0; m < null_cells.size(); m++)
        {
            mc[null_cells.get(m)[0]][null_cells.get(m)[1]] = 0;
        }
        tablevalue(mc);
    }

    int nullcheck(int n, ArrayList<int[]> null_cells)
    {
        int[][] mc = new int[9][9];
        int k = 0;
        for (int x = 0; x < 9; x++)
        {
            if (n < null_cells.size() - 1)
            {
                k =+ nullcheck(n + 1, null_cells);
                if (k > 1)
                {
                    null_cells.get(n)[2] = 1;
                    return k;
                }
            }
            mc = mascop(mrand);
            for (int m = 0; m < null_cells.size(); m++)
            {
                mc[null_cells.get(m)[0]][null_cells.get(m)[1]] = null_cells.get(m)[2];
            }
            tablevalue(mc);
            if (checkall())
            {
                k++;
            }
            null_cells.get(n)[2]++;
        }
        null_cells.get(n)[2] = 1;
        return k;
    }

    boolean checkall ()
    {
        int k = 0;
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                if (!checkcell(i, j))
                {
                    return true;
                }
            }
        }
        return false;
    }


    public boolean checkcell (int r, int c)
    {
            for (int i = 0; i < 9; i++)
            {
                for (int j = 0; j < 9; j++)
                {
                    Button btnN = (Button) table1.getChildAt((i * 9) + j);
                    Button btnN2 = (Button) table1.getChildAt((r * 9) + c);
                    if ((i == r && j == c) || btnN.getText().toString() == " ")
                    {
                        continue;
                    }
                    if ((i == r || j == c) && btnN.getText().equals(btnN2.getText()))
                    {
                        return true;
                    }
                    if ((btnN.getTag().equals(btnN2.getTag()))
                            && btnN.getText().equals(btnN2.getText()))
                    {
                        return true;
                    }
                }
            }
        return false;
    }


    public void tablevalue(int[][] m)
    {
        String s = " ";
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                Button btnN = (Button) table1.getChildAt((i * 9) + j);
                if (m[i][j] == 0)
                    s = " ";
                else
                {
                    s = String.valueOf(m[i][j]);
                }
                btnN.setText(s);
            }
        }
    }

}