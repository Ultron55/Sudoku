package com.example.sudoku;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class BlenderNumbers implements Serializable
{
    File file;
    ArrayList<int[]> blenArrL;

    public BlenderNumbers(File filedir)
    {
        file = new File(filedir, "blendernumbers.bin");
        if (!file.exists())
        {
            blenArrL = new ArrayList<>();
            int[] indexesOfnull_cells;
            String s;
            int last = 0;
            int size = 0;
            int factorial;
            for (int i = 0; i < 200; i++)
            {
                isblenderEnd = false;
                indexesOfnull_cells = new int[i + 1];
                for (int j = 0; j < indexesOfnull_cells.length; j++)
                {
                    indexesOfnull_cells[j] = j + 1;
                }
                indexesOfnull_cells[i] = i + 1;
                blenArrL.add(indexesOfnull_cells);
                factorial = 1;
                for (int x = 1; x <= i + 1; x++)
                {
                    factorial = factorial * x;
                }
                for (int x = 0; x < factorial - 1; x++)
                {
                    blenArrL.add(new int[i + 1]);
                }
                last = (i - 1) * i - 1;
                size = blenArrL.size() - factorial - 1;
                for (int x = 0; x < last; x++)
                {
                    indexesOfnull_cells = new int[i + 1];
                    for (int z = 0; z < i; z++)
                    {
                        indexesOfnull_cells[z] = blenArrL.get(size - x)[z];
                    }
                    indexesOfnull_cells[i] = i + 1;
                    blenArrL.add(indexesOfnull_cells);
                }
                int index = 0;
                while (!isblenderEnd)
                {
                    indexesOfnull_cells = blender(indexesOfnull_cells);
                    blenArrL.set(index, indexesOfnull_cells);
//                    s = "arr: ";
//                    for (int k = 0; k < indexesOfnull_cells.length; k++)
//                    {
//                        s += indexesOfnull_cells[k] + " ";
//                    }
//                    Log.v("blender", s);
                }
                try {
                    if (i > 4)
                    {
                        Thread.currentThread().sleep(10000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            (new JobForFileObj()).saveArrL(blenArrL);
        }
    }
    
    public ArrayList OpenFileBlendArr(int AreaSize)
    {
        JobForFileObj jobForFileObj = new JobForFileObj();
        blenArrL = new ArrayList<>();
        jobForFileObj.openArrL();
        blenArrL = jobForFileObj.arrayList;
        return blenArrL;
    }
        
        
    boolean isblenderEnd;
    
    class JobForFileObj implements Serializable
    {
        public ArrayList<int[]> arrayList;

        public void saveArrL(ArrayList arrayList)
        {
            try
            {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
                this.arrayList = arrayList;
                oos.writeObject(this);
            }
            catch (IOException e) {
                return;
            }
        }
        
        public void openArrL()
        {
            try
            {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                JobForFileObj o = (JobForFileObj) ois.readObject();
            }
            catch(Exception ex){}
        }
    }
    

    int[] blender(int[] indexesOfnull_cells)
    {
        int z = 0;
        int null_cellsSize = indexesOfnull_cells.length;
        int j = null_cellsSize - 2;
        try
        {
            while (j >= 0 && indexesOfnull_cells[j] >= indexesOfnull_cells[j + 1])
            {
                j--;
            }
        }
        catch (Exception e)
        {
            Log.v("coerror", e.getLocalizedMessage() + " jjj");
        }
        if (j < 0)
        {
            isblenderEnd = true;
            return indexesOfnull_cells;
        }
        int k = null_cellsSize - 1;
        try {
            while (indexesOfnull_cells[j] >= indexesOfnull_cells[k])
            {
                k--;
            }
        }
        catch (Exception e)
        {
            Log.v("coerror", "j: " + j + " k: " + k);
            return indexesOfnull_cells;
        }
        z = indexesOfnull_cells[j];
        indexesOfnull_cells[j] = indexesOfnull_cells[k];
        indexesOfnull_cells[k] = z;
        int l = j + 1;
        int r = null_cellsSize - 1;
        while (l < r)
        {
            l++;
            r--;
            z = indexesOfnull_cells[l];
            indexesOfnull_cells[l] = indexesOfnull_cells[r];
            indexesOfnull_cells[r] = z;
        }
        return  indexesOfnull_cells;
    }
}