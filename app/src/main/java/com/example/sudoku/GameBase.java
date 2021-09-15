package com.example.sudoku;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class GameBase extends AppCompatActivity {

    // все глобальные переменные в начале для удоства их поиска
    Handler handler = new Handler();
    public int AreaSize; // размерность игрового поля AreaSize x AreaSize
    public int SqrtAreaSize;
    public int[][] Matrix_base = new int[AreaSize][AreaSize]; // исходная матрица значений
    public int[][] Matrix_random = new int[AreaSize][AreaSize]; // перемешанная матрица значений
    int[][] Matrix_done; // матрица выводимая на экран
    int[][] Matrix_changed; // матрица измененная игроком
    public int level = 33;
    public int lvlchoice = 5; // выбранный уровень сложности 0, 1 или 2, а 5 как невыбранное
    public GridLayout SudokuArea;
    public GridLayout ValuesArea;
    public GridLayout CheckboxsArea;
    public int indexOfbtnSArea = 1000; // индекс выбранной ячейки (кнопки) на игровом поле
    public ProgressBar prbar;
    public Button NewGamebtn;
    public Button ChoiceLevelbtn;
    public boolean Pencilbool = false;
    public ImageButton Pencilbtn;
    public boolean[] Pencilboolcell; // для определения ячеек заполененых через Pencil
    public String[] PencilStringcell; // строки ячеек заполененых через Pencil
    public boolean[] ischboxChecked; // вкл/выкл чекбоксы
    public ImageButton Eraserbtn;
    public boolean selcellbool = false; // разрешение выделения ячеек
    public boolean Xbool = false; // вкл/выкл X функция
    public int[] XSelectedbtn = new int [3]; // для методов X функции
    public String[] ValuesAreaText = new String[]
            {"1", "2", "3", "4", "5", "6", "7", "8", "9",
                    "A", "B", "C", "D", "E", "F", "G"};
    public int[][] SectorsIndexes; // SectorsIndexes[row][column] = sector
    public int[] SectorsBtnPrimaryColor;
    public int[][] SectorStartCoordinates;
    public Context context;
    public int textsizeNormal = 14;
    public int textsizePencil = 8;
    public int textsizeValuesArea = 28;
    public Chronometer chronometer;
    public TimeRecordsData timerecord; // класс для сохранения рекордов времени
    public ThemeData themeData; // класс темы оформления
    public int[] arraydataofThisTheme; //для определения изменения темы при возвращении в активность
    public GameData gameData; // класс для сохранения сеанса игры
    public AlertDialog dialog;
    boolean isCancelableChoiceleveldialog = false; // можно ли закрывать диалог уровней
    static int SudokuAreaWidth;
    boolean isResumeGame;
    Drawable chbox_off;
    Drawable chbox_on;
    float densityVal;
    CreateUIGame createUIGame;
    boolean isThreadCreateUIGame = false;


    // сброс и заполенение глобальных переменных
    void ValFill()
    {
        lvlchoice = 5;
        SqrtAreaSize = (int) Math.round(Math.sqrt(AreaSize));
        Matrix_base = new int[AreaSize][AreaSize];
        Matrix_random = new int[AreaSize][AreaSize];
        Pencilboolcell = new boolean[AreaSize * AreaSize];
        PencilStringcell = new String[AreaSize * AreaSize];
        ischboxChecked = new boolean[AreaSize];
        switch (AreaSize)
        {
            case 9:
            {
                textsizeNormal = 28;
                textsizePencil = 8;
                textsizeValuesArea = 28;
                SectorsIndexes = new int[][] {
                        {1, 1, 1, 2, 2, 2, 3, 3, 3},
                        {1, 1, 1, 2, 2, 2, 3, 3, 3},
                        {1, 1, 1, 2, 2, 2, 3, 3, 3},
                        {4, 4, 4, 5, 5, 5, 6, 6, 6},
                        {4, 4, 4, 5, 5, 5, 6, 6, 6},
                        {4, 4, 4, 5, 5, 5, 6, 6, 6},
                        {7, 7, 7, 8, 8, 8, 9, 9, 9},
                        {7, 7, 7, 8, 8, 8, 9, 9, 9},
                        {7, 7, 7, 8, 8, 8, 9, 9, 9}
                };
                SectorsBtnPrimaryColor = new int[] {1, 3, 5, 7, 9};
                SectorStartCoordinates = new int[][] {
                        {0, 0}, {0, 3}, {0, 6},
                        {3, 0}, {3, 3}, {3, 6},
                        {6, 0}, {6, 3}, {6, 6}
                };
                break;
            }
            case 16:
            {
                textsizeNormal = 14;
                textsizePencil = 6;
                textsizeValuesArea = 28;
                SectorsIndexes = new int[][] {
                        {1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4},
                        {1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4},
                        {1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4},
                        {1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4},
                        {5, 5, 5, 5, 6, 6, 6, 6, 7, 7, 7, 7, 8, 8, 8, 8},
                        {5, 5, 5, 5, 6, 6, 6, 6, 7, 7, 7, 7, 8, 8, 8, 8},
                        {5, 5, 5, 5, 6, 6, 6, 6, 7, 7, 7, 7, 8, 8, 8, 8},
                        {5, 5, 5, 5, 6, 6, 6, 6, 7, 7, 7, 7, 8, 8, 8, 8},
                        {9, 9, 9, 9, 10, 10, 10, 10, 11, 11, 11, 11, 12, 12, 12, 12},
                        {9, 9, 9, 9, 10, 10, 10, 10, 11, 11, 11, 11, 12, 12, 12, 12},
                        {9, 9, 9, 9, 10, 10, 10, 10, 11, 11, 11, 11, 12, 12, 12, 12},
                        {9, 9, 9, 9, 10, 10, 10, 10, 11, 11, 11, 11, 12, 12, 12, 12},
                        {13, 13, 13, 13, 14, 14, 14, 14, 15, 15, 15, 15, 16, 16, 16, 16},
                        {13, 13, 13, 13, 14, 14, 14, 14, 15, 15, 15, 15, 16, 16, 16, 16},
                        {13, 13, 13, 13, 14, 14, 14, 14, 15, 15, 15, 15, 16, 16, 16, 16},
                        {13, 13, 13, 13, 14, 14, 14, 14, 15, 15, 15, 15, 16, 16, 16, 16}
                };
                SectorsBtnPrimaryColor = new int[] {1, 3, 6, 8, 9, 11, 14, 16};
                SectorStartCoordinates = new int[][] {
                        {0, 0}, {0, 4}, {0, 8}, {0, 12},
                        {4, 0}, {4, 4}, {4, 8}, {4, 12},
                        {8, 0}, {8, 4}, {8, 8}, {8, 12},
                        {12, 0}, {12, 4}, {12, 8}, {12, 12}
                };
            }
        }
        CreateBaseMatrix();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_base);
        Bundle args = getIntent().getExtras();
        densityVal = getResources().getDisplayMetrics().density;
        AreaSize = args.getInt("AreaSize");
        // ширина игрового поля = ширина экрана - отступы
        SudokuAreaWidth = args.getInt("ScreenWidth") -
                Math.round(24 * densityVal);
        isResumeGame = args.getBoolean("isResumeGame");
        chbox_on = getResources().getDrawable(R.drawable.chbox_on);
        chbox_off = getResources().getDrawable(R.drawable.chbox_off);
        gameData = new GameData(getFilesDir());
        if (isResumeGame)
        {
            // если запушено продолжение игры
            gameData.openDataFile();
            AreaSize = gameData.AreaSize;
        }
        ValFill();
        timerecord = new TimeRecordsData(getFilesDir());
        themeData = new ThemeData(getFilesDir());
        arraydataofThisTheme = themeData.ArrayDataOfThisTheme();
        ((Button) findViewById(R.id.SizeSwitchbtn)).setText("Size: " + String.valueOf(AreaSize));
        Pencilbtn = (ImageButton) findViewById(R.id.Pencilbtn);
        prbar = (ProgressBar) findViewById(R.id.progressBar);
        chronometer = (Chronometer) findViewById(R.id.chronometr);
        SudokuArea  = (GridLayout) findViewById(R.id.SudokuArea);
        ChoiceLevelbtn = (Button) findViewById(R.id.ChoiceLevelbtn);
        NewGamebtn = (Button) findViewById(R.id.NewGamebtn);
        ValuesArea = (GridLayout) findViewById(R.id.ValuesArea);
        CheckboxsArea = (GridLayout) findViewById(R.id.CheckboxsArea);
        Eraserbtn = (ImageButton) findViewById(R.id.Eraserbtn);
        Eraserbtn.setEnabled(false);
        context = this;
        ThemeChanged();
        CreateUI(); // для создания новой игры
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        themeData.openChoiceFile();
        int[] arr = themeData.ArrayDataOfThisTheme();
        for (int i = 0; i < arraydataofThisTheme.length; i++)
        {
            if(arr[i] != arraydataofThisTheme[i])
            {
                ThemeChanged();
                arraydataofThisTheme = arr;
                return;
            }
        }
    }

    void CreateUI()
    {
        float scaledDensityVal = getResources().getDisplayMetrics().scaledDensity;
        createUIGame = new CreateUIGame(this, SudokuArea, ValuesArea, CheckboxsArea, prbar,
                chronometer, AreaSize, SqrtAreaSize, densityVal, scaledDensityVal,
                themeData.textcolor1, themeData.btnprimary, themeData.btnseconary,
                themeData.mainback, SectorsIndexes, SectorsBtnPrimaryColor, SudokuAreaWidth,
                chbox_off);
        (new Thread(new Runnable() {
            @Override
            public void run() {
                while (createUIGame.isThreadCreateUIGame)
                {
                    try
                    {
                        Thread.currentThread().sleep(1);
                    }
                    catch (InterruptedException e) {e.printStackTrace();}
                }
                isThreadCreateUIGame = false;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        setonClickMethodsOfbtns();
                        if (isResumeGame)
                        {
                            ResumeGame();
                        }
                        else
                        {
                            gameData.Reset(AreaSize);
                            gameData.file.delete();
                            ChoiceLevelDialog();
                        }
                    }
                });

            }
        })).start();
    }

    void ResumeGame()
    {
        // при запуске продолжения игры
        gameData.openDataFile();
        textcolor(true);
        Matrix_random = gameData.Matrix_random;
        Matrix_done = gameData.Matrix_done;
        Matrix_changed = gameData.Matrix_changed;
        level = gameData.level;
        lvlchoice = gameData.lvlchoice;
        Pencilboolcell = gameData.Pencilboolcell;
        PencilStringcell = gameData.PencilStringcell;
        ischboxChecked = gameData.ischboxChecked;
        SudokuAreaFillValues(Matrix_done);
        textcolor(false);
        SudokuAreaFillValues(gameData.Matrix_changed);
        Button btnOfSudokuArea;
        for (int i = 0; i < AreaSize * AreaSize; i++)
        {
            if (Pencilboolcell[i])
            {
                btnOfSudokuArea = (Button) SudokuArea.getChildAt(i);
                PencilchangeButton(btnOfSudokuArea);
                btnOfSudokuArea.setText(PencilStringcell[i]);
            }
        }
        for (int i = 0; i < AreaSize; i++)
        {
            if ( ischboxChecked[i])
            {
                ImageButton chbox = (ImageButton) CheckboxsArea.getChildAt(i);
                chbox.setImageDrawable(chbox_on);
                (ValuesArea.getChildAt(i)).setVisibility(View.INVISIBLE);
            }
        }
        Eraserbtn.setEnabled(true);
        selcellbool = true;
        NewGamebtn.setEnabled(true);
        chronometer.setText(gameData.timeText);
        (findViewById(R.id.ChoiceLevelbtn)).setEnabled(true);
        (findViewById(R.id.ResumebtnFL)).setVisibility(View.VISIBLE);
        (findViewById(R.id.ResumeOpacityArea)).setVisibility(View.VISIBLE);
        isWin();
        isResumeGame = false;
    }

    public void ResumeClick(View view)
    {
        view.setVisibility(View.INVISIBLE);
        (findViewById(R.id.ResumebtnFL)).setVisibility(View.INVISIBLE);
        chronometer.setBase(SystemClock.elapsedRealtime() - gameData.timeNumb);
        chronometer.start();
    }

    void ResumeUIInvisibileted()
    {
        (findViewById(R.id.ResumebtnFL)).setVisibility(View.INVISIBLE);
        (findViewById(R.id.ResumeOpacityArea)).setVisibility(View.INVISIBLE);
    }

    void textcolor(boolean all)
    {
        // задает цвет текста кнопка в SudokuArea (ячейкам)
        int color;
        if (all)
        {
            color = themeData.textcolor1;
        }
        else
        {
            color = themeData.textcolor2;
        }
        for (int i = 0; i < AreaSize * AreaSize; i++)
        {
            Button btnOfSudokuArea = (Button) SudokuArea.getChildAt(i);
            if (!all)
            {
                if (btnOfSudokuArea.getText().equals(" "))
                {
                    continue;
                }
            }
            btnOfSudokuArea.setTextColor(color);
        }
    }

    void SudokuAreaFillValues(int[][] matrix)
    {
        String s = " ";
        for (int i = 0; i < AreaSize; i++)
        {
            for (int j = 0; j < AreaSize; j++)
            {
                Button btnOfSudokuArea = (Button) SudokuArea.getChildAt((i * AreaSize) + j);
                if (matrix[i][j] == 0)
                {
                    s = " ";
                }
                else
                {
                    s = ValuesAreaText[matrix[i][j] - 1];
                }
                btnOfSudokuArea.setText(s);
            }
        }
    }

    void chronostart()
    {
        long time = SystemClock.elapsedRealtime();
        chronometer.setBase(time);
        gameData.timeNumb = SystemClock.elapsedRealtime() - chronometer.getBase();
        gameData.timeText = "00:00";
        chronometer.start();
    }


    public void ThemeChanged()
    {
        // применяет выбранную тему из объекта themeData
        themeData.openChoiceFile();
        (findViewById(R.id.gameCL)).setBackgroundColor(themeData.mainback);
        (findViewById(R.id.SettingbtnON)).setBackgroundColor(themeData.btnprimary);
        ((ImageButton) findViewById(R.id.SettingbtnON)).setColorFilter(themeData.textcolor1);
        (findViewById(R.id.SettingbtnOFF)).setBackgroundColor(themeData.btnprimary);
        ((ImageButton) findViewById(R.id.SettingbtnOFF)).setColorFilter(themeData.textcolor1);
        (findViewById(R.id.NewGamebtn)).setBackgroundColor(themeData.btnprimary);
        ((Button) findViewById(R.id.NewGamebtn)).setTextColor(themeData.textcolor1);
        (findViewById(R.id.ChoiceLevelbtn)).setBackgroundColor(themeData.btnprimary);
        ((Button) findViewById(R.id.ChoiceLevelbtn)).setTextColor(themeData.textcolor1);
        (findViewById(R.id.SizeSwitchbtn)).setBackgroundColor(themeData.btnprimary);
        ((Button) findViewById(R.id.SizeSwitchbtn)).setTextColor(themeData.textcolor1);
        (findViewById(R.id.Stylebtn)).setBackgroundColor(themeData.btnprimary);
        ((Button) findViewById(R.id.Stylebtn)).setTextColor(themeData.textcolor1);
        (findViewById(R.id.menuL)).setBackgroundColor(themeData.mainback);
        (findViewById(R.id.chronometr)).setBackgroundColor(themeData.btnprimary);
        ((Chronometer) findViewById(R.id.chronometr)).setTextColor(themeData.textcolor1);
        (findViewById(R.id.Searchbtn)).setBackgroundColor(themeData.btnprimary);
        ((ImageButton) findViewById(R.id.Searchbtn)).setColorFilter(themeData.textcolor1);
        prbar.setBackgroundColor(themeData.SudokuAreaback);
        prbar.getIndeterminateDrawable().setColorFilter(themeData.mainback, android.graphics.PorterDuff.Mode.MULTIPLY);
        findViewById(R.id.ResumeText).setBackgroundColor(themeData.mainback);
        ((TextView) findViewById(R.id.ResumeText)).setTextColor(themeData.textcolor2);
        SudokuArea.setBackgroundColor(themeData.SudokuAreaback);
        if (SudokuArea.getChildCount() != 0)
        {
            Button btnOfSudokuArea;
            int BackgroundOld;
            for (int i = 0; i < AreaSize; i++)
            {
                for (int j = 0; j < AreaSize; j++)
                {
                    btnOfSudokuArea = (Button) SudokuArea.getChildAt(i * AreaSize + j);
                    BackgroundOld = ((ColorDrawable) btnOfSudokuArea.getBackground()).getColor();
                    if (isBtnPrimaryColor(i * AreaSize + j))
                    {
                        btnOfSudokuArea.setBackgroundColor(themeData.btnprimary);
                    }
                    else
                    {
                        btnOfSudokuArea.setBackgroundColor(themeData.btnseconary);
                    }
                    if (themeData.errorvalue == BackgroundOld)
                    {
                        btnOfSudokuArea.setBackgroundColor(themeData.errorvalue);
                    }
                    if (themeData.selecterrorvalue == BackgroundOld)
                    {
                        btnOfSudokuArea.setBackgroundColor(themeData.selecterrorvalue);
                    }
                    if (themeData.select == BackgroundOld)
                    {
                        btnOfSudokuArea.setBackgroundColor(themeData.select);
                    }
                    if (themeData.Xcolor == BackgroundOld)
                    {
                        btnOfSudokuArea.setBackgroundColor(themeData.Xcolor);
                    }
                    if (Matrix_done[i][j] == 0)
                    {
                        btnOfSudokuArea.setTextColor(themeData.textcolor1);
                    }
                    else
                    {
                        btnOfSudokuArea.setTextColor(themeData.textcolor2);
                    }
                }
            }
        }
        (findViewById(R.id.Eraserbtn)).setBackgroundColor(themeData.btnprimary);
        ((ImageButton) findViewById(R.id.Eraserbtn)).setColorFilter(themeData.textcolor1);
        (findViewById(R.id.Pencilbtn)).setBackgroundColor(themeData.btnprimary);
        if (Pencilbool)
        {
            ((ImageButton) findViewById(R.id.Pencilbtn)).setColorFilter(themeData.errorvalue);
        }
        else
        {
            ((ImageButton) findViewById(R.id.Pencilbtn)).setColorFilter(themeData.textcolor1);
        }
        (findViewById(R.id.Xbtn)).setBackgroundColor(themeData.btnprimary);
        Button Xbtn = (Button) findViewById(R.id.Xbtn);
        if (Xbtn.getCurrentTextColor() != themeData.Xcolor)
        {
            Xbtn.setTextColor(themeData.textcolor1);
        }
        for (int i = 0; i < ValuesArea.getChildCount(); i++)
        {
            ValuesArea.getChildAt(i).setBackgroundColor(themeData.btnprimary);
            ((Button) ValuesArea.getChildAt(i)).setTextColor(themeData.textcolor1);
            (CheckboxsArea.getChildAt(i)).setBackgroundColor(themeData.mainback);
            ((ImageButton) CheckboxsArea.getChildAt(i)).setColorFilter(themeData.textcolor1);
        }
    }

    void SaveTimeIngameDataFile()
    {
        gameData.timeText = chronometer.getText().toString();
        gameData.timeNumb = SystemClock.elapsedRealtime() - chronometer.getBase();
        gameData.saveDataFile();
    }

    void FirstSavegameData()
    {
        if (!gameData.isSavedGameData)
        {
            gameData.AreaSize = AreaSize;
            gameData.level = level;
            gameData.lvlchoice = lvlchoice;
            gameData.Matrix_random = Matrix_random;
            gameData.Matrix_done = Matrix_done;
            gameData.Matrix_changed = Matrix_changed;
            SaveTimeIngameDataFile();
        }
    }

    void CreateBaseMatrix()
    {
        int k = 1;
        boolean b;
        for (int i = 0; i < AreaSize; i++)
        {
            for (int j = 0; j < AreaSize; j++)
            {
                if (k > AreaSize)
                {
                    k = k - AreaSize;
                }
                b = false;
                for (int x = 1; x < SqrtAreaSize; x++)
                {
                    if (j != 0)
                    {
                        b = false;
                        break;
                    }
                    if (i == SqrtAreaSize * x)
                    {
                        b = true;
                        break;
                    }
                }
                if (b)
                {
                    k++;
                }
                Matrix_base[i][j] = k;
                k++;
            }
            k = Matrix_base[i][0] + SqrtAreaSize;
        }
    }

    //dialogs start
    View dialogstyled(View dialog_layout)
    {
        (dialog_layout.findViewById(R.id.dialogCL)).setBackgroundColor(themeData.mainback);
        ((TextView) dialog_layout.findViewById(R.id.TitleTV)).setTextColor(themeData.textcolor2);
        ((TextView) dialog_layout.findViewById(R.id.MessageTV)).setTextColor(themeData.textcolor1);
        Button Leftbtn = (Button) dialog_layout.findViewById(R.id.Leftbtn);
        Leftbtn.setTextColor(themeData.textcolor1);
        Leftbtn.setBackgroundColor(themeData.btnprimary);
        Button Centerbtn = (Button) dialog_layout.findViewById(R.id.Centerbtn);
        Centerbtn.setBackgroundColor(themeData.btnprimary);
        Centerbtn.setTextColor(themeData.textcolor1);
        Button Rightbtn = (Button) dialog_layout.findViewById(R.id.Rightbtn);
        Rightbtn.setTextColor(themeData.textcolor1);
        Rightbtn.setBackgroundColor(themeData.btnprimary);
        return dialog_layout;
    }

    public void onSizeSwitchbtnClick(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialog_layout = getLayoutInflater().inflate(R.layout.dialog, null);
        dialog_layout = dialogstyled(dialog_layout);
        TextView TitleTV = (TextView) dialog_layout.findViewById(R.id.TitleTV);
        TitleTV.setText("SWITCH SIZE");
        TextView MessageTV = (TextView) dialog_layout.findViewById(R.id.MessageTV);
        MessageTV.setText("YOU SURE");
        dialog_layout.findViewById(R.id.Centerbtn).setVisibility(View.INVISIBLE);
        Button Leftbtn = (Button) dialog_layout.findViewById(R.id.Leftbtn);
        Leftbtn.setText("NO");
        Button Rightbtn = (Button) dialog_layout.findViewById(R.id.Rightbtn);
        Rightbtn.setText("YES");
        Leftbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                ResumeTime();
            }
        });
        Rightbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCancelableChoiceleveldialog = false;
                chronometer.stop();
                SwitchSize();
                dialog.cancel();
            }
        });
        builder.setView(dialog_layout).setCancelable(true);
        dialog = builder.create();
        dialog.show();
        SettingOFF(false);
    }

    void SwitchSize()
    {
        ResumeUIInvisibileted();
        SudokuArea.removeAllViews();
        ValuesArea.removeAllViews();
        CheckboxsArea.removeAllViews();
        if (AreaSize == 9)
        {
            AreaSize = 16;
        }
        else
        {
            AreaSize = 9;
        }
        ((Button) findViewById(R.id.SizeSwitchbtn)).setText("Size: " + String.valueOf(AreaSize));
        ValFill();
        CreateUI();
    }


    public void onNewGamebtnClick(final View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialog_layout = getLayoutInflater().inflate(R.layout.dialog, null);
        dialog_layout = dialogstyled(dialog_layout);
        TextView TitleTV = (TextView) dialog_layout.findViewById(R.id.TitleTV);
        TitleTV.setText("NEW GAME");
        TextView MessageTV = (TextView) dialog_layout.findViewById(R.id.MessageTV);
        MessageTV.setText("YOU SURE");
        dialog_layout.findViewById(R.id.Centerbtn).setVisibility(View.INVISIBLE);
        Button Leftbtn = (Button) dialog_layout.findViewById(R.id.Leftbtn);
        Leftbtn.setText("NO");
        Button Rightbtn = (Button) dialog_layout.findViewById(R.id.Rightbtn);
        Rightbtn.setText("YES");
        Leftbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                ResumeTime();
            }
        });
        Rightbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                NewSudoku();
            }
        });
        builder.setView(dialog_layout).setCancelable(true);
        dialog = builder.create();
        dialog.show();
        SettingOFF(false);
    }

    boolean isThreadNewSudoku = false;
    boolean isStopThreads = false;
    boolean isStartThreads = false;
    boolean isGenerateRunDone;

    void NewSudoku()
    {
        ResumeUIInvisibileted();
        isResumeGame = false;
        isThreadNewSudoku = true;
        gameData.Reset(AreaSize); // сбрасывает данные в соответствии с AreaSize
        prbar.setVisibility(ProgressBar.VISIBLE);
        for (int i = 0; i < AreaSize; i++)
        {
            (ValuesArea.getChildAt(i)).setVisibility(View.VISIBLE);
            ImageButton chbox = (ImageButton) CheckboxsArea.getChildAt(i);
            chbox.setImageDrawable(chbox_off);
        }
        Eraserbtn.setEnabled(false);
        selcellbool = false;
        NewGamebtn.setEnabled(false);
        (findViewById(R.id.ChoiceLevelbtn)).setEnabled(false);
        isCancelableChoiceleveldialog = true;
        indexOfbtnSArea = 1000;
        Runnable rb = new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        prbar.setVisibility(ProgressBar.VISIBLE);
                        for (int i = 0; i < AreaSize; i++)
                        {
                            (ValuesArea.getChildAt(i)).setVisibility(View.VISIBLE);
                            ImageButton chbox = (ImageButton) CheckboxsArea.getChildAt(i);
                            chbox.setImageDrawable(chbox_off);
                        }
                        textcolor(true);
                        isStartThreads = false;
                        isStopThreads = false;
                        Runnable GenerateRun = new Runnable() {
                            @Override
                            public void run() {
                                // NewSudokuCreate - класс создания новой головоломки
                                NewSudokuCreate NSC = new NewSudokuCreate(AreaSize, level, Matrix_base,
                                        SectorsIndexes, SectorStartCoordinates);
                                isStartThreads = true;
                                NSC.new_game();
                                if (!isStopThreads)
                                {
                                    isStopThreads = true;
                                    Matrix_random = NSC.Matrix_random;
                                    Matrix_done = NSC.Matrix_done;
                                    Matrix_changed = MatrixCopy(Matrix_done);
                                    isGenerateRunDone = true;
                                }
                            }
                        };
                        isGenerateRunDone = false;
                        new Thread(GenerateRun).start();
                        while (!isGenerateRunDone)
                        {
                            try
                            {
                                Thread.currentThread().sleep(1);
                            }
                            catch (InterruptedException e) {e.printStackTrace();}
                        }
                        SudokuAreaFillValues(Matrix_done);
                        textcolor(false);
                        Button btnOfSudokuArea;
                        int color;
                        for (int i = 0; i < AreaSize * AreaSize; i++)
                        {
                            btnOfSudokuArea = (Button) SudokuArea.getChildAt(i);
                            if (isBtnPrimaryColor(i))
                            {
                                color = themeData.btnprimary;
                            }
                            else
                            {
                                color = themeData.btnseconary;
                            }
                            btnOfSudokuArea.setBackgroundColor(color);
                            if (Pencilboolcell[i])
                            {
                                btnOfSudokuArea.setTextSize(textsizeNormal);
                                btnOfSudokuArea.setPadding(0,0, 0, 0);
                                Pencilboolcell[i] = false;
                            }
                        }
                        for (int i = 0; i < AreaSize; i++)
                        {
                            ischboxChecked[i] = false;
                        }
                        Pencilbool = true;
                        PencilSwitch();
                        Eraserbtn.setEnabled(true);
                        selcellbool = true;
                        NewGamebtn.setEnabled(true);
                        (findViewById(R.id.ChoiceLevelbtn)).setEnabled(true);
                        prbar.setVisibility(ProgressBar.INVISIBLE);
                        chronostart();
                        FirstSavegameData();
                        isThreadNewSudoku = false;
                    }
                });
            }
        };
        Thread thread = new Thread(rb);
        thread.start();
    }

    public void onChoiceLevelbtnClick(View view){SettingOFF(false);ChoiceLevelDialog();}

    void ChoiceLevelDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialog_layout = getLayoutInflater().inflate(R.layout.dialog, null);
        dialog_layout = dialogstyled(dialog_layout);
        TextView TitleTV = (TextView) dialog_layout.findViewById(R.id.TitleTV);
        TitleTV.setText("CHOICE LEVEL");
        TextView MessageTV = (TextView) dialog_layout.findViewById(R.id.MessageTV);
        MessageTV.setText("");
        Button Leftbtn = (Button) dialog_layout.findViewById(R.id.Leftbtn);
        Leftbtn.setText("LOW");
        Button Centerbtn = (Button) dialog_layout.findViewById(R.id.Centerbtn);
        Centerbtn.setVisibility(View.VISIBLE);
        Button Rightbtn = (Button) dialog_layout.findViewById(R.id.Rightbtn);
        Rightbtn.setText("HIGH");
        final int lvlmiddle = AreaSize * AreaSize / 2;
        Leftbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                level = lvlmiddle - AreaSize;
                lvlchoice = 0;
                NewSudoku();
            }
        });
        Centerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                level = lvlmiddle + AreaSize;
                lvlchoice = 1;
                NewSudoku();
            }
        });
        Rightbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                level = lvlmiddle + AreaSize * 2;
                lvlchoice = 2;
                NewSudoku();
            }
        });
        if (isCancelableChoiceleveldialog)
        {
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    ResumeTime();
                }
            });
        }
        builder.setView(dialog_layout).setCancelable(isCancelableChoiceleveldialog);
        dialog = builder.create();
        dialog.show();
    }

    public void WinDialog()
    {
        chronometer.stop();
        String stime = chronometer.getText().toString();
        if (lvlchoice != 5)
        {
            timerecord.addrecord(AreaSize, lvlchoice, stime);
        }
        isResumeGame = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialog_layout = getLayoutInflater().inflate(R.layout.dialog, null);
        dialog_layout = dialogstyled(dialog_layout);
        TextView TitleTV = (TextView) dialog_layout.findViewById(R.id.TitleTV);
        TitleTV.setText("YOU WIN!");
        TextView MessageTV = (TextView) dialog_layout.findViewById(R.id.MessageTV);
        MessageTV.setText("YOUR TIME:" + stime);
        Button Leftbtn = (Button) dialog_layout.findViewById(R.id.Leftbtn);
        Leftbtn.setText("Choice level");
        Button Centerbtn = (Button) dialog_layout.findViewById(R.id.Centerbtn);
        Centerbtn.setVisibility(View.VISIBLE);
        Centerbtn.setText("Switch size");
        Button Rightbtn = (Button) dialog_layout.findViewById(R.id.Rightbtn);
        Rightbtn.setText("New game");
        Leftbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                ChoiceLevelDialog();
            }
        });
        Centerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                SwitchSize();
            }
        });
        Rightbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                NewSudoku();
            }
        });
        builder.setView(dialog_layout).setCancelable(true);
        dialog = builder.create();
        dialog.show();
    }

    public void onStylebtnClick(View view)
    {
        new StyleDialog(this, themeData, new ThemeData(getFilesDir()));
    }
    //dialogs end


    int[][] MatrixCopy(int[][] Matrix)
    {
        int[][] MatrixCopies = new int[Matrix.length][];
        for (int i = 0; i < MatrixCopies.length; i++) {
            MatrixCopies[i] = new int[Matrix[i].length];
            System.arraycopy(Matrix[i], 0, MatrixCopies[i], 0, MatrixCopies[i].length);
        }
        return MatrixCopies;
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


    public void onMyButtonClick(final View view)
    {
        int column;
        int row;
        if (!selcellbool) // шоб не работало во время асинхронных процессов
        {
            return;
        }
        // если нажата кнопка в SudokuArea (ячейка)
        if (view.getParent().equals(SudokuArea))
        {
            int color = 0;
            int index_old = 0;
            boolean isOldX = false;
            Button btnOfSudokuArea;
            ColorDrawable BackgroundOld;
            if (indexOfbtnSArea != 1000)
            {
                // если уже есть выбранная ячейка
                btnOfSudokuArea = (Button) SudokuArea.getChildAt(indexOfbtnSArea);
                BackgroundOld = (ColorDrawable) btnOfSudokuArea.getBackground();
                if (BackgroundOld.getColor() == themeData.selecterrorvalue)
                {
                    color = themeData.errorvalue;
                }
                else
                {
                    if (isBtnPrimaryColor(indexOfbtnSArea))
                    {
                        color = themeData.btnprimary;
                    }
                    else
                    {
                        color = themeData.btnseconary;
                    }
                }
                index_old = indexOfbtnSArea;
                isOldX = true;
                btnOfSudokuArea.setBackgroundColor(color);
            }
            indexOfbtnSArea = ((GridLayout) view.getParent()).indexOfChild(view);
            if (Xbool) // если активна функция X
            {
                XbaseAndOrXgraphcolor(index_old, isOldX);
            }
            BackgroundOld = (ColorDrawable) view.getBackground();
            if (BackgroundOld.getColor() ==themeData.errorvalue)
            {
                color = themeData.selecterrorvalue;
            }
            else
            {
                color = themeData.select;
            }
            view.setBackgroundColor(color);
        }
        if (view == Eraserbtn && indexOfbtnSArea != 1000)
        {
            // если нажат ластик и есть выбранная ячейка
            Button btnOfSudokuArea = (Button) SudokuArea.getChildAt(indexOfbtnSArea);
            // textcolor2 - это изначально заполенные ячейки, неизменяемые
            if (btnOfSudokuArea.getCurrentTextColor() != themeData.textcolor2)
            {
                (btnOfSudokuArea).setText(" ");
                if (Xbool)
                {
                    onXbtnClick(view);
                    isWin();
                    onXbtnClick(view);
                }
                else
                {
                    isWin();
                }

            }
            column = indexOfbtnSArea % AreaSize;
            row = Math.round((indexOfbtnSArea - column) / AreaSize);
            Matrix_changed[row][column] = 0;
            gameData.Matrix_changed[row][column] = 0;
            SaveTimeIngameDataFile();
        }
        if (view.getParent().equals(ValuesArea)) // нажата кнопка ввода
        {
            if (indexOfbtnSArea != 1000)
            {
                String s1 = "";
                Button btnOfSudokuArea = (Button) SudokuArea.getChildAt(indexOfbtnSArea);
                if (btnOfSudokuArea.getCurrentTextColor() != themeData.textcolor2)
                {
                    if (Pencilbool) // если активна функция Pencil (карандаш)
                    {
                        btnOfSudokuArea.setTextSize(textsizePencil);
                        btnOfSudokuArea.setPadding(1,1, 1, 1);
                        if (!Pencilboolcell[indexOfbtnSArea]) {
                            // если имеющееся значение введено не через Pencil
                            btnOfSudokuArea.setText("");
                        }
                        s1 = PencilText(btnOfSudokuArea.getText().toString(),
                                ValuesArea.indexOfChild(view));
                        btnOfSudokuArea.setText(s1);
                        if (s1 != "")
                        {
                            Pencilboolcell[indexOfbtnSArea] = true;
                        }
                        else
                        {
                            Pencilboolcell[indexOfbtnSArea] = false;
                        }
                        onXbtnClick(view);
                        isWin();
                        onXbtnClick(view);
                    }
                    else
                    {
                        Pencilboolcell[indexOfbtnSArea] = false;
                        btnOfSudokuArea.setTextSize(textsizeNormal);
                        btnOfSudokuArea.setPadding(0,0, 0, 0);
                        btnOfSudokuArea.setText(String.valueOf(ValuesAreaText[ValuesArea.indexOfChild(view)]));
                        s1 = String.valueOf(btnOfSudokuArea.getText());
                        String s2 = "";
                        int BackgroundOld = 0;
                        int cred = themeData.errorvalue;
                        int cselred = themeData.selecterrorvalue;
                        int csel = themeData.select;
                        for (int i = 0; i < AreaSize * AreaSize; i++)
                        {
                            // проверка на рчевидную ошибку в решении
                            btnOfSudokuArea = (Button) SudokuArea.getChildAt(i);
                            s2 = String.valueOf(btnOfSudokuArea.getText());
                            if (s1 == s2)
                            {
                                BackgroundOld =((ColorDrawable) btnOfSudokuArea.getBackground()).getColor();
                                if (BackgroundOld != cred && BackgroundOld != cselred)
                                {
                                    btnOfSudokuArea.setBackgroundColor(csel);
                                }
                            }
                        }
                        onXbtnClick(view);
                        isWin();
                        onXbtnClick(view);
                    }
                }
                if (isWin())
                {
                    gameData.Reset(AreaSize);
                    gameData.file.delete();
                    WinDialog();
                }
                else
                {
                    column = indexOfbtnSArea % AreaSize;
                    row = Math.round((indexOfbtnSArea - column) / AreaSize);
                    if (Pencilboolcell[indexOfbtnSArea])
                    {
                        gameData.Pencilboolcell[indexOfbtnSArea] = true;
                        PencilStringcell[indexOfbtnSArea] = s1;
                        gameData.PencilStringcell[indexOfbtnSArea] = s1;
                        Matrix_changed[row][column] = 0;
                        gameData.Matrix_changed[row][column] = 0;
                    }
                    else
                    {
                        gameData.Pencilboolcell[indexOfbtnSArea] = false;
                        PencilStringcell[indexOfbtnSArea] = "";
                        gameData.PencilStringcell[indexOfbtnSArea] = "";
                        for (int i = 0; i < AreaSize; i++)
                        {
                            if(s1 == ValuesAreaText[i])
                            {
                                Matrix_changed[row][column] = i + 1;
                                gameData.Matrix_changed[row][column] = i + 1;
                            }
                        }
                    }
                    SaveTimeIngameDataFile();
                }
            }
        }
    }

    //methods X start
    public void onXbtnClick(View view)
    {
        Xbool = !Xbool;
        int color;
        int cred = themeData.errorvalue;
        int cselred = themeData.selecterrorvalue;
        int BackgroundOld;
        Button btnOfSudokuArea;
        if (Xbool)
        {
            color = themeData.Xcolor;
            if(indexOfbtnSArea != 1000)
            {
                Xgraphcolor();
                btnOfSudokuArea = (Button) SudokuArea.getChildAt(indexOfbtnSArea);
                BackgroundOld = ((ColorDrawable) btnOfSudokuArea.getBackground()).getColor();
                if (BackgroundOld == cred || BackgroundOld == cselred)
                {
                    btnOfSudokuArea.setBackgroundColor(cselred);
                }
                else
                {
                    btnOfSudokuArea.setBackgroundColor(themeData.select);
                }
            }
        }
        else
        {
            color = themeData.textcolor1;
            if (indexOfbtnSArea != 1000)
            {
                Xbase(indexOfbtnSArea);
                btnOfSudokuArea = (Button) SudokuArea.getChildAt(indexOfbtnSArea);
                BackgroundOld = ((ColorDrawable) btnOfSudokuArea.getBackground()).getColor();
                if (BackgroundOld == cred || BackgroundOld == cselred)
                {
                    btnOfSudokuArea.setBackgroundColor(cselred);
                }
                else
                {
                    btnOfSudokuArea.setBackgroundColor(themeData.select);
                }
            }
        }
        ((Button) findViewById(R.id.Xbtn)).setTextColor(color);
    }

    void XbaseAndOrXgraphcolor(int index_old, boolean isOldX)
    {
        if(isOldX)
        {
            Xbase(index_old);
        }
        Xgraphcolor();
    }

    void Xgraphcolor()
    {
        /* функция X - выделяет все ячейки связанные с выбранной
            (по стоблцу, строке, сектору, значению) */
        int color = themeData.Xcolor;
        int cred = themeData.errorvalue;
        int cselred = themeData.selecterrorvalue;
        int csel = themeData.select;
        int BackgroundOld;
        int column = indexOfbtnSArea % AreaSize;
        int row = Math.round((indexOfbtnSArea - column) / AreaSize);
        int[] sectorstarcoor = new int[2];
        XSelectedbtn[0] = row;
        XSelectedbtn[1] = column;
        XSelectedbtn[2] = SectorsIndexes[row][column];
        sectorstarcoor[0] = SectorStartCoordinates[XSelectedbtn[2] - 1][0];
        sectorstarcoor[1] = SectorStartCoordinates[XSelectedbtn[2] - 1][1];
        int sectorRow = sectorstarcoor[0];
        int sectorColumn = sectorstarcoor[1];
        int sectorColIndex = 0;
        String s1 = String.valueOf(((Button) SudokuArea.getChildAt(indexOfbtnSArea)).getText());
        String s2 = "";
        Button btnOfSudokuArea;
        for (int i = 0; i < AreaSize * AreaSize; i++)
        {
            if (s1 == " ")
            {
                break;
            }
            btnOfSudokuArea = (Button) SudokuArea.getChildAt(i);
            s2 = String.valueOf(((Button) SudokuArea.getChildAt(i)).getText());
            if (s1 == s2)
            {
                BackgroundOld =((ColorDrawable) btnOfSudokuArea.getBackground()).getColor();
                if (BackgroundOld != cred && BackgroundOld != cselred)
                {
                    btnOfSudokuArea.setBackgroundColor(csel);
                }
            }

        }
        for (int i = 0; i < AreaSize; i++)
        {
            btnOfSudokuArea = (Button) SudokuArea.getChildAt(row * AreaSize + i);
            BackgroundOld =((ColorDrawable) btnOfSudokuArea.getBackground()).getColor();
            if (BackgroundOld != cred && BackgroundOld != cselred)
            {
                btnOfSudokuArea.setBackgroundColor(color);
            }
            btnOfSudokuArea = (Button) SudokuArea.getChildAt(i * AreaSize + column);
            BackgroundOld =((ColorDrawable) btnOfSudokuArea.getBackground()).getColor();
            if (BackgroundOld != cred && BackgroundOld != cselred)
            {
                btnOfSudokuArea.setBackgroundColor(color);
            }
            btnOfSudokuArea = (Button) SudokuArea.getChildAt(sectorRow * AreaSize + sectorColumn);
            BackgroundOld =((ColorDrawable) btnOfSudokuArea.getBackground()).getColor();
            if (BackgroundOld != cred && BackgroundOld != cselred)
            {
                btnOfSudokuArea.setBackgroundColor(color);
            }
            sectorColumn++;
            sectorColIndex++;
            if (sectorColIndex == SqrtAreaSize)
            {
                sectorColIndex = 0;
                sectorColumn = sectorstarcoor[1];
                sectorRow++;
            }
        }
    }

    void Xbase(int index_old)
    {
        if (XSelectedbtn[2] == 0)
        {
            return;
        }
        int column = index_old % AreaSize;
        int row = Math.round((index_old - column) / AreaSize);
        int[] sectorstarcoor = new int[2];
        sectorstarcoor[0] = SectorStartCoordinates[XSelectedbtn[2] - 1][0];
        sectorstarcoor[1] = SectorStartCoordinates[XSelectedbtn[2] - 1][1];
        int sectorRow = sectorstarcoor[0];
        int sectorColumn = sectorstarcoor[1];
        int sectorColIndex = 0;
        int[] sectorcolors = new int[AreaSize];
        switch (AreaSize)
        {
            case 9:
            {
                sectorcolors = new int[] {
                        0, 1, 0,
                        1, 0, 1,
                        0, 1, 0
                };
                break;
            }
            case 16:
            {
                sectorcolors = new int[] {
                        0, 1, 0, 1,
                        1, 0, 1, 0,
                        0, 1, 0, 1,
                        1, 0, 1, 0
                };
                break;
            }
        }
        int[] color = new int[2];
        int cred = themeData.errorvalue;
        int cselred = themeData.selecterrorvalue;
        int BackgroundOld;
        color[0] = themeData.btnprimary;
        color[1] = themeData.btnseconary;
        int tcolor = color[sectorcolors[SectorsIndexes[row][column] - 1]];
        String s1 = String.valueOf(((Button) SudokuArea.getChildAt(index_old)).getText());
        String s2 = "";
        Button btnOfSudokuArea;
        for (int i = 0; i < AreaSize * AreaSize; i++)
        {
            if (s1 == " ")
            {
                break;
            }
            btnOfSudokuArea = (Button) SudokuArea.getChildAt(i);
            s2 = String.valueOf(((Button) SudokuArea.getChildAt(i)).getText());
            if (s1 == s2)
            {
                BackgroundOld = ((ColorDrawable) btnOfSudokuArea.getBackground()).getColor();
                if (BackgroundOld != cred && BackgroundOld != cselred)
                {
                    if (isBtnPrimaryColor(i))
                    {
                        btnOfSudokuArea.setBackgroundColor(color[0]);
                    }
                    else
                    {
                        btnOfSudokuArea.setBackgroundColor(color[1]);
                    }
                }
            }
        }
        for (int i = 0; i < AreaSize; i++)
        {
            btnOfSudokuArea = (Button) SudokuArea.getChildAt(row * AreaSize + i);
            BackgroundOld = ((ColorDrawable) btnOfSudokuArea.getBackground()).getColor();
            if (BackgroundOld != cred && BackgroundOld != cselred)
            {
                btnOfSudokuArea.setBackgroundColor(color[sectorcolors[SectorsIndexes[row][i] - 1]]);
            }
            btnOfSudokuArea = (Button) SudokuArea.getChildAt(i * AreaSize + column);
            BackgroundOld = ((ColorDrawable) btnOfSudokuArea.getBackground()).getColor();
            if (BackgroundOld != cred && BackgroundOld != cselred)
            {
                btnOfSudokuArea.setBackgroundColor(color[sectorcolors[SectorsIndexes[i][column] - 1]]);
            }
            btnOfSudokuArea = (Button) SudokuArea.getChildAt(sectorRow * AreaSize + sectorColumn);
            BackgroundOld = ((ColorDrawable) btnOfSudokuArea.getBackground()).getColor();
            if (BackgroundOld != cred && BackgroundOld != cselred)
            {
                btnOfSudokuArea.setBackgroundColor(tcolor);
            }
            sectorColumn++;
            sectorColIndex++;
            if (sectorColIndex == SqrtAreaSize)
            {
                sectorColIndex = 0;
                sectorColumn = sectorstarcoor[1];
                sectorRow++;
            }
        }
    }
    // methods X end

    // methods Pencil start
    public void onPencilbtnClick(View view) {PencilSwitch();}

    void PencilSwitch()
    {
        Pencilbool = !Pencilbool;
        int color;
        if (Pencilbool)
        {
            color = themeData.errorvalue;
        }
        else
        {
            color = themeData.textcolor1;
        }
        Pencilbtn.setColorFilter(color);
    }

    String PencilText(String btntext, int valindex)
    {
        String newbtntext = "";
        String s = "";
        int sn = 0;
        boolean b = true;
        for (int i = 0; i < btntext.length(); i++)
        {
            if (btntext.charAt(i) == ' ')
            {
                continue;
            }
            else
            {
                s = btntext.substring(i, i + 1);
                for(int j = 0; j < AreaSize; j++)
                {
                    if (s.equals(ValuesAreaText[j]))
                    {
                        sn = j;
                        break;
                    }
                }
                if (valindex != sn)
                {
                    newbtntext = newbtntext + s + " ";
                }
                else
                {
                    b = false;
                }
            }
        }
        if(b)
        {
            s = ValuesAreaText[valindex];
            newbtntext = newbtntext + s;
        }
        return newbtntext;
    }

    void PencilchangeButton(Button btn)
    {
        btn.setTextSize(textsizePencil);
        btn.setPadding(1,1, 1, 1);
    }
    // methods Pencil end

    public void onClickCheckbox(View view)
    {
        ImageButton chbox = (ImageButton) view;
        int indexOfValuebtn = CheckboxsArea.indexOfChild(view);
        int visibilityParam = 0;
        if (!ischboxChecked[indexOfValuebtn])
        {
            visibilityParam = 4;//INVISIBLE
            chbox.setImageDrawable(chbox_on);
            ischboxChecked[indexOfValuebtn] = true;
        }
        else
        {
            visibilityParam = 0;//VISIBLE
            chbox.setImageDrawable(chbox_off);
            ischboxChecked[indexOfValuebtn] = false;
        }
        ((Button) ValuesArea.getChildAt(indexOfValuebtn)).setVisibility(visibilityParam);
        gameData.ischboxChecked = ischboxChecked;
        SaveTimeIngameDataFile();
    }

    public void onClickSearch(View view)
    {
        if (Matrix_random[0][0] == 0)
        {
            return;
        }
        Button btnOfSudokuArea;
        int cred = themeData.errorvalue;
        int cselred = themeData.selecterrorvalue;
        int indexbtn;
        String btntext = "";
        for (int i = 0; i < AreaSize; i++)
        {
            for (int j = 0; j < AreaSize; j++)
            {
                indexbtn = i * AreaSize + j;
                btnOfSudokuArea = (Button) SudokuArea.getChildAt(indexbtn);
                if (Pencilboolcell[indexbtn])
                {
                    continue;
                }
                btntext = String.valueOf(btnOfSudokuArea.getText());
                if (btntext != " " && btntext != ValuesAreaText[Matrix_random[i][j] - 1])
                {
                    if (indexbtn == indexOfbtnSArea)
                    {
                        btnOfSudokuArea.setBackgroundColor(cselred);
                    }
                    else
                    {
                        btnOfSudokuArea.setBackgroundColor(cred);
                    }
                }
            }
        }
    }

    public boolean isWin()
    {
        boolean win = true;
        boolean isCheckedCell = true;
        int cred = themeData.errorvalue;
        int cselred = themeData.selecterrorvalue;
        int BackgroundOld;
        int indexbtn = 0;
        int color = 0;
        for (int i = 0; i < AreaSize; i++)
        {
            for (int j = 0; j < AreaSize; j++)
            {
                indexbtn = (i * AreaSize) + j;
                Button btnOfSudokuArea = (Button) SudokuArea.getChildAt(indexbtn);
                BackgroundOld = ((ColorDrawable) btnOfSudokuArea.getBackground()).getColor();
                isCheckedCell = true;
                if (btnOfSudokuArea.getText().toString() == " " || Pencilboolcell[indexbtn])
                {
                    win = false;
                    isCheckedCell = false;
                }
                if (isErrorCellTable(i, j) && isCheckedCell)
                {
                    if (indexbtn == indexOfbtnSArea)
                    {
                        btnOfSudokuArea.setBackgroundColor(cselred);
                    }
                    else
                    {
                        btnOfSudokuArea.setBackgroundColor(cred);
                    }
                    win = false;
                }
                else
                {
                    if (Xbool && (i == XSelectedbtn[0] || j == XSelectedbtn[1] ||
                            SectorsIndexes[i][j] == XSelectedbtn[2]))
                    {
                        color = themeData.Xcolor;
                    }
                    else if (isBtnPrimaryColor(i * AreaSize + j))
                    {
                        color = themeData.btnprimary;
                    }
                    else
                    {
                        color = themeData.btnseconary;
                    }
                    if (Xbool && BackgroundOld == themeData.select)
                    {
                        color = BackgroundOld;
                    }
                    if (indexbtn == indexOfbtnSArea)
                    {
                        color = themeData.select;
                    }
                    btnOfSudokuArea.setBackgroundColor(color);
                }
            }
        }
        return win;
    }

    boolean isErrorCellTable (int row, int column)
    {
        for (int i = 0; i < AreaSize; i++)
        {
            for (int j = 0; j < AreaSize; j++)
            {
                Button btnOfSudokuArea = (Button) SudokuArea.getChildAt((i * AreaSize) + j);
                Button btnOfSudokuArea2 = (Button) SudokuArea.getChildAt((row * AreaSize) + column);
                if ((i == row && j == column) || btnOfSudokuArea.getText().toString() == " " || Pencilboolcell[(i * AreaSize) + j])
                {
                    continue;
                }
                if ((i == row || j == column) && btnOfSudokuArea.getText().equals(btnOfSudokuArea2.getText()))
                {
                    return true;
                }
                if ((SectorsIndexes[row][column] == SectorsIndexes[i][j])
                        && btnOfSudokuArea.getText().equals(btnOfSudokuArea2.getText()))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public void onSettingbtnONClick(View view)
    {
        chronometer.stop();
        gameData.timeNumb = SystemClock.elapsedRealtime() - chronometer.getBase();
        findViewById(R.id.SettingFL).setVisibility(View.VISIBLE);
    }

    public void onSettingbtnOFFClick(View view) {SettingOFF(true);}
    void SettingOFF(boolean isResumeTime)
    {
        findViewById(R.id.SettingFL).setVisibility(View.INVISIBLE);
        if (isResumeTime)
        {
            ResumeTime();
        }
    }

    void ResumeTime()
    {
        chronometer.setBase(SystemClock.elapsedRealtime() - gameData.timeNumb);
        chronometer.start();
    }

    public void setonClickMethodsOfbtns()
    {
        for (int i = 0; i < SudokuArea.getChildCount(); i++)
        {
            (SudokuArea.getChildAt(i)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onMyButtonClick(v);
                }
            });
        }
        for (int i = 0; i < AreaSize; i++)
        {
            (ValuesArea.getChildAt(i)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onMyButtonClick(v);
                }
            });
            (CheckboxsArea.getChildAt(i)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickCheckbox(v);
                }
            });
        }
    }

    @Override
    public void onBackPressed()
    {
        if (!isThreadCreateUIGame && !isThreadNewSudoku)
        {
            super.onBackPressed();
        }
    }

    
}