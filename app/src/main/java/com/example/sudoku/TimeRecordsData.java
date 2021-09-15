package com.example.sudoku;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class TimeRecordsData implements Serializable
{
    //size2>level3>time*
    public ArrayList<String>[][] datalist = new ArrayList[2][3];
    File file;
    String[] levelstr = new String[] {"Low", "Middle", "High"};

    public TimeRecordsData(File filedir)
    {
        file = new File(filedir, "timelist.bin");
        for (int j = 0; j < 3; j++)
        {
            datalist[0][j] = new ArrayList<String>();
            datalist[1][j] = new ArrayList<String>();
        }
        if (FileExists())
        {
            openDataFile();
        }
    }

    public boolean FileExists()
    {
        return file.exists();
    }

    public void addrecord(int size, int lvlchoice, String stime)
    {
        switch (size)
        {
            case 9:
            {
                datalist[0][lvlchoice].add(stime);
                break;
            }
            case 16:
            {
                datalist[1][lvlchoice].add(stime);
                break;
            }
        }
        saveDataFile();
    }

    void openDataFile()
    {
        try
        {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            TimeRecordsData o = (TimeRecordsData) ois.readObject();
            datalist = o.datalist;
        }
        catch(Exception ex){}
    }

    void saveDataFile()
    {
        try
        {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(this);
        }
        catch (IOException e) {}
    }

}