package com.example.sudoku;

import android.content.res.XmlResourceParser;
import android.graphics.Color;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ThemeData implements Cloneable
{
    public int select = Color.argb(255, 1, 22, 99);;
    public int errorvalue = Color.argb(255, 166, 16, 45);
    public int selecterrorvalue = Color.argb(255, 99, 14, 87);
    public int Xcolor = Color.argb(255, 3, 87, 160);
    public int textcolor1;
    public int textcolor2;
    public int SudokuAreaback;
    public int btnprimary;
    public int btnseconary;
    public int mainback;
    public File fileList;
    public File filechoice;
    int choicethme = 0;

    public ThemeData()
    {
        Basetheme();
    }

    public ThemeData(File filedir)
    {
        fileList = new File(filedir, "theme_list.bin");
        filechoice = new File(filedir, "choicetheme.bin");
        openChoiceFile();
    }

    public boolean FileExists()
    {
        return fileList.exists();
    }

    public int[] ArrayDataOfThisTheme()
    {
        return new int[] {select, errorvalue, selecterrorvalue, Xcolor, textcolor1, textcolor2,
        SudokuAreaback, btnprimary, btnseconary, mainback, choicethme};
    }

    public void openChoiceFile()
    {
        try
        {
            DataInputStream dis = new DataInputStream(new FileInputStream(filechoice));
            choicethme = dis.readInt();
            switch (choicethme)
            {
                case 0:
                {
                    Basetheme();
                    break;
                }
                case 1:
                {
                    Graytheme();
                    break;
                }
                case 2:
                {
                    openDataFile();
                    break;
                }
            }
        }
        catch (IOException e)
        {
            Basetheme();
        }
    }

    public void openDataFile()
    {
        ThemeData o = null;
        try
        {
            DataInputStream dis = new DataInputStream(new FileInputStream(fileList));
            textcolor1 = dis.readInt();
            textcolor2 = dis.readInt();
            SudokuAreaback = dis.readInt();
            btnprimary = dis.readInt();
            btnseconary = dis.readInt();
            mainback = dis.readInt();
        }
        catch (IOException e) {}
    }

    public void saveDataFile()
    {
        try
        {
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(filechoice));
            dos.writeInt(choicethme);
            if (choicethme == 2)
            {
                dos = new DataOutputStream(new FileOutputStream(fileList));
                dos.writeInt(textcolor1);
                dos.writeInt(textcolor2);
                dos.writeInt(SudokuAreaback);
                dos.writeInt(btnprimary);
                dos.writeInt(btnseconary);
                dos.writeInt(mainback);
            }
        }
        catch (IOException e) {}
    }

    public void Basetheme()
    {
        textcolor1 = Color.argb(255, 255, 232, 165);
        textcolor2 = Color.argb(255, 239, 127, 8);
        SudokuAreaback = Color.argb(255, 79, 125, 145);
        btnprimary = Color.argb(255, 21, 70, 76);
        btnseconary = Color.argb(255, 10, 96, 104);
        mainback = Color.argb(255, 6, 31, 60);
    }

    public void Graytheme()
    {
        textcolor1 = Color.argb(255, 230, 230, 230);
        textcolor2 = Color.argb(255, 161, 161, 161);
        SudokuAreaback = Color.argb(255, 174, 174, 174);
        btnprimary = Color.argb(255, 32, 32, 32);
        btnseconary = Color.argb(255, 67, 67, 67);
        mainback = Color.argb(255, 19, 19, 19);
    }

    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

}
