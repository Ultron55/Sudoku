package com.example.sudoku;

import java.util.ArrayList;
import java.util.Random;

public class NewSudokuCreate {

    int AreaSize = 9;
    int SqrtAreaSize;
    int level = 2;
    int SolutionCount = 0;
    int[][] Matrix_base;
    int[][] Matrix_random = new int[AreaSize][AreaSize];
    int[][] mnullcheck = new int[AreaSize][AreaSize];
    int[][] Matrix_done;
    int[][] SectorsIndexes;
    int[][] SectorStartCoordinates;
    ArrayList<int[]> sel = new ArrayList<int[]>();

    public NewSudokuCreate(int AreaSize, int level, int[][] Matrix_base, int[][] SectorsIndexes, int[][] SectorStartCoordinates)
    {
        this.AreaSize = AreaSize;
        this.level = level;
        this.Matrix_base = Matrix_base;
        this.SectorsIndexes = SectorsIndexes;
        this.SectorStartCoordinates = SectorStartCoordinates;
        SqrtAreaSize = (int) Math.round(Math.sqrt(AreaSize));
        new_game();
    }

    public int[][] getMatrix_random()
    {
        return Matrix_random;
    }

    public int[][] getMatrix_done()
    {
        return Matrix_done;
    }


    void new_game()
    {
        Matrix_random = MatrixCopy(Matrix_base);
        Random random = new Random();
        boolean horizontalswap;
        for (int i = 0; i < 40; i++)
        {
            if (random.nextInt(2) == 0)
            {
                horizontalswap = false;
            }
            else
            {
                horizontalswap = true;
            }
            if (random.nextInt(2) == 0)
            {
                swap_line(Matrix_random, horizontalswap);
            }
            else
            {
                swap_area(Matrix_random, horizontalswap);
            }
        }
        clearcells();
    }

    int[][] swap_line(int[][] Matrix, boolean horizontalswap)
    {
        Random random = new Random();
        int area = random.nextInt(SqrtAreaSize);
        int line1 = random.nextInt(SqrtAreaSize);
        int line2;
        for (; ;)
        {
            line2 = random.nextInt(SqrtAreaSize);
            if (line2 != line1)
            {
                break;
            }
        }
        line1 = area * SqrtAreaSize + line1;
        line2 = area * SqrtAreaSize + line2;
        int[] line = new int[AreaSize];
        for (int i = 0; i < AreaSize; i++)
        {
            if (horizontalswap)
            {
                line[i] = Matrix[line1][i];
                Matrix[line1][i] = Matrix[line2][i];
                Matrix[line2][i] = line[i];
            }
            else
            {
                line[i] = Matrix[i][line1];
                Matrix[i][line1] = Matrix[i][line2];
                Matrix[i][line2] = line[i];
            }

        }
        return Matrix;
    }

    int[][] swap_area(int[][] Matrix, boolean horizontalswap)
    {
        Random random = new Random();
        int area1 = random.nextInt(SqrtAreaSize);
        int area2 = random.nextInt(SqrtAreaSize);
        while (area1 == area2)
        {
            area2 = random.nextInt(SqrtAreaSize);
        }
        int[] area = new int[AreaSize * SqrtAreaSize];
        area1 = area1 * SqrtAreaSize;
        area2 = area2 * SqrtAreaSize;
        int j = 0;
        int k = 0;
        for (int i = 0; i < area.length; i ++)
        {
            if (horizontalswap)
            {
                area[i] = Matrix[area1 + k][j];
                Matrix[area1 + k][j] = Matrix[area2 + k][j];
                Matrix[area2 + k][j] = area[i];
            }
            else
            {
                area[i] = Matrix[j][area1 + k];
                Matrix[j][area1 + k] = Matrix[j][area2 + k];
                Matrix[j][area2 + k] = area[i];
            }
            j++;
            if (j == AreaSize)
            {
                j = 0;
                k++;
            }
        }
        return Matrix;
    }

    int selrand()
    {
        Random rand = new Random();
        for(int i = 0; i < sel.size(); i++)
        {
            if(rand.nextInt(100) == 50)
            {
                return i;
            }
            if(rand.nextInt(100) == 50)
            {
                return sel.size() - 1 - i;
            }
        }
        return 1000;
    }

    void clearcells()
    {
        mnullcheck = MatrixCopy(Matrix_random);
        ArrayList<int[]> null_cells = new ArrayList<int[]>();
        ArrayList<int[]> seldel = new ArrayList<int[]>();
        int lvl = level;
        int n = 0;
        int[] nn = new int[4];
        int sn = 1000;
        int ll = AreaSize * AreaSize - lvl;
        sel.clear();
        for (int i = 0; i < AreaSize; i++)
        {
            for(int j = 0; j < AreaSize; j++)
            {
                sel.add(new int[] {i, j});
            }
        }
        while (null_cells.size() < lvl)
        {
            if (sel.size() == 0)
            {
                for(int i = seldel.size() - 1; i > ll - 2; i--)
                {
                    sel.add(seldel.get(i));
                }
            }
            for(;;)
            {
                if ((sn = selrand()) != 1000)
                {
                    break;
                }
            }
            null_cells.add(new int[] { sel.get(sn)[0], sel.get(sn)[1], 1 });
            n = null_cells.size() - 1;
            SolutionCount = 0;
            nn[0] = null_cells.get(n)[0];
            nn[1] = null_cells.get(n)[1];
            nn[2] = 1;
            if (null_cells.size() < 4)
            {
                nn = nullcheck(nn);
                null_cells.get(n)[2] = nn[3];
            }
            else
            {
                coursel(null_cells);
            }
            if(SolutionCount > 1)
            {
                null_cells.remove(null_cells.size() - 1);
            }
            seldel.add(sel.get(sn));
            sel.remove(sn);
        }
        Matrix_done = MatrixCopy(Matrix_random);
        for (int m = 0; m < null_cells.size(); m++)
        {
            if (Matrix_done[null_cells.get(m)[0]][null_cells.get(m)[1]] == 0)
            {
                Matrix_done[null_cells.get(m)[0]][null_cells.get(m)[1]] = 0;
            }
            Matrix_done[null_cells.get(m)[0]][null_cells.get(m)[1]] = 0;
        }
    }

    int[] nullcheck(int[] nn)
    {
        for (int x = 0; x < AreaSize; x++)
        {
            mnullcheck[nn[0]][nn[1]] = nn[2];
            if(!CheckCell(nn[0], nn[1]))
            {
                SolutionCount++;
                if(SolutionCount > 1)
                {
                    return nn;
                }
                nn[3] = nn[2];
            }
            nn[2]++;
        }
        return nn;
    }

    void coursel (ArrayList<int[]> null_cells)
    {
        int last = null_cells.size() - 1;
        int[] nn = new int[4];
        for (int i = 0; i < last; i++)
        {
            null_cells.get(last)[2] = null_cells.get(i)[2];
            for (int j = 1; j < AreaSize + 1; j++)
            {
                null_cells.get(i)[2] = j;
                if(!CheckSelectionedCells(null_cells))
                {
                    SolutionCount++;
                    if(SolutionCount > 1)
                    {
                        return;
                    }
                }
            }
            null_cells.get(i)[2] = null_cells.get(last)[2];
        }
        nn[0] = null_cells.get(last)[0];
        nn[1] = null_cells.get(last)[1];
        nn[2] = 1;
        nn = nullcheck(nn);
        null_cells.get(last)[2] = nn[3];
    }


    boolean CheckSelectionedCells(ArrayList<int[]> null_cells)
    {
        int value;
        int[] sectorstarcoor = new int[3];
        int sectorRow;
        int sectorColumn;
        int sectorColIndex = 0;
        int a;
        int b;
        for(int i = 0; i < null_cells.size(); i++)
        {
            a = null_cells.get(i)[0];
            b = null_cells.get(i)[1];
            value = null_cells.get(i)[2];
            mnullcheck[a][b] = value;
            sectorRow = 0;
            sectorColumn = 0;
            if(SectorsIndexes[a][b] != sectorstarcoor[2])
            {
                sectorstarcoor[2] = SectorsIndexes[a][b];
                sectorstarcoor[0] = SectorStartCoordinates[sectorstarcoor[2] - 1][0];
                sectorstarcoor[1] = SectorStartCoordinates[sectorstarcoor[2] - 1][1];
                sectorRow = sectorstarcoor[0];
                sectorColumn = sectorstarcoor[1];
            }
            for(int x = 0; x < AreaSize; x++)
            {
                if((mnullcheck[a][x] != 0 && mnullcheck[a][x] == value && b != x) ||
                        (mnullcheck[x][b] != 0 && mnullcheck[x][b] == value && a != x))
                {
                    return true;
                }
                if(mnullcheck[sectorRow][sectorColumn] == mnullcheck[a][b] && (sectorRow != a || sectorColumn != b))
                {
                    return true;
                }
                sectorColumn++;
                sectorColIndex++;
                if(sectorColIndex == SqrtAreaSize)
                {
                    sectorColIndex = 0;
                    sectorColumn = sectorstarcoor[1];
                    sectorRow++;
                }
            }
        }
        return false;
    }

    boolean CheckCell(int row, int column)
    {
        int value;
        int[] sectorstarcoor = new int[2];
        int sectorRow;
        int sectorColumn;
        int sectorColIndex = 0;
        value = mnullcheck[row][column];
        sectorstarcoor[1] = SectorsIndexes[row][column];
        sectorRow = SectorStartCoordinates[sectorstarcoor[1] - 1][0];
        sectorstarcoor[0] = SectorStartCoordinates[sectorstarcoor[1] - 1][1];
        sectorColumn = sectorstarcoor[0];
        for(int x = 0; x < AreaSize; x++)
        {
            if((mnullcheck[row][x] != 0 && mnullcheck[row][x] == value && column != x) ||
                    (mnullcheck[x][column] != 0 && mnullcheck[x][column] == value && row != x))
            {
                return true;
            }
            if(mnullcheck[sectorRow][sectorColumn] == mnullcheck[row][column] && (sectorRow != row || sectorColumn != column))
            {
                return true;
            }
            sectorColumn++;
            sectorColIndex++;
            if(sectorColIndex == SqrtAreaSize)
            {
                sectorColIndex = 0;
                sectorColumn = sectorstarcoor[0];
                sectorRow++;
            }
        }
        return false;
    }

    int[][] MatrixCopy(int[][] Matrix)
    {
        int[][] MatrixCopies = new int[Matrix.length][];
        for (int i = 0; i < MatrixCopies.length; i++) {
            MatrixCopies[i] = new int[Matrix[i].length];
            System.arraycopy(Matrix[i], 0, MatrixCopies[i], 0, MatrixCopies[i].length);
        }
        return MatrixCopies;
    }

}
