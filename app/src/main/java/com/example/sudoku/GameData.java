package com.example.sudoku;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class GameData implements Serializable
{
    public int AreaSize = 9;
    public int level = 2;
    public int lvlchoice = 5;
    public int[][] Matrix_random = new int[9][9];
    public int[][] Matrix_done = new int[9][9];
    public int[][] Matrix_changed = new int[9][9];
    public boolean[] Pencilboolcell = new boolean[81];
    public String[] PencilStringcell = new String[81];
    public boolean[] ischboxChecked = new boolean[9];
    public boolean isSavedGameData = false;
    public String timeText = "00:00";
    public long timeNumb = 0;
    File file;;

    public GameData(File filedir)
    {
        file = new File(filedir, "gamedata.bin");
    }

    void openDataFile()
    {
        if (!FileExists())
        {
            return;
        }
        try
        {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            GameData o = (GameData) ois.readObject();
            this.AreaSize = o.AreaSize;
            this.level = o.level;
            this.lvlchoice = o.lvlchoice;
            this.Matrix_random = o.Matrix_random;
            this.Matrix_done = o.Matrix_done;
            this.Matrix_changed = o.Matrix_changed;
            this.Pencilboolcell = o.Pencilboolcell;
            this.PencilStringcell = o.PencilStringcell;
            this.ischboxChecked = o.ischboxChecked;
            this.isSavedGameData = o.isSavedGameData;
            this.timeText = o.timeText;
            this.timeNumb = o.timeNumb;
        }
        catch(Exception ex){}
    }

    void saveDataFile()
    {
        try
        {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            isSavedGameData = true;
            oos.writeObject(this);
        }
        catch (IOException e) {
            isSavedGameData = false;
            return;
        }
    }


    public boolean FileExists()
    {
        return file.exists();
    }

    public void Reset(int areaSize)
    {
        AreaSize = areaSize;
        level = 2;
        lvlchoice = 5;
        Matrix_random = new int[AreaSize][AreaSize];
        Matrix_done = new int[AreaSize][AreaSize];
        Matrix_changed = new int[AreaSize][AreaSize];
        Pencilboolcell = new boolean[AreaSize * AreaSize];
        PencilStringcell = new String[AreaSize * AreaSize];
        ischboxChecked = new boolean[AreaSize];
        isSavedGameData = false;
        timeText = "00:00";
        timeNumb = 0;
    }
}
