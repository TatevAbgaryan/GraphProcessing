package com.example.tatevabgaryan.graphprocessing.helper;

import com.example.tatevabgaryan.graphprocessing.model.Island;
import com.example.tatevabgaryan.graphprocessing.model.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tatev.Abgaryan on 2/11/2018.
 */

public class IslandHelper {

    //No of rows and columns
    private int rowSize, columnSize;

    // A function to check if a given cell (row, col) can
    // be included in DFS
    private boolean isSafe(int M[][], int row, int col,
                           boolean visited[][]) {
        // row number is in range, column number is in range
        // and value is 1 and not yet visited
        return (row >= 0) && (row < rowSize) &&
                (col >= 0) && (col < columnSize) &&
                (M[row][col] == 1 && !visited[row][col]);
    }

    // A utility function to do DFS for a 2D boolean matrix.
    // It only considers the 8 neighbors as adjacent vertices
    private void DFS(int M[][], int row, int col, boolean visited[][], Island island) {
        // These arrays are used to get row and column numbers
        // of 8 neighbors of a given cell
        int rowNbr[] = new int[]{-1, -1, -1, 0, 0, 1, 1, 1};
        int colNbr[] = new int[]{-1, 0, 1, -1, 1, -1, 0, 1};

        // Mark this cell as visited
        visited[row][col] = true;
        island.getPoints().add(new Point(row, col));

        // Recur for all connected neighbours
        for (int k = 0; k < 8; ++k)
            if (isSafe(M, row + rowNbr[k], col + colNbr[k], visited))
                DFS(M, row + rowNbr[k], col + colNbr[k], visited, island);
    }

    // The main function that returns count of islands in a given
    //  boolean 2D matrix
    public List<Island> countIslands(int M[][], int rowSize, int columnSize) {
        this.rowSize = rowSize;
        this.columnSize = columnSize;
        List<Island> islands = new ArrayList<>();
        // Make a bool array to mark visited cells.
        // Initially all cells are unvisited
        boolean visited[][] = new boolean[rowSize][columnSize];


        // Initialize count as 0 and travese through the all cells
        // of given matrix
        for (int i = 0; i < rowSize; ++i)
            for (int j = 0; j < columnSize; ++j)
                if (M[i][j] == 1 && !visited[i][j]) // If a cell with
                {                                 // value 1 is not
                    // visited yet, then new island found, Visit all
                    // cells in this island and increment island count
                    Island island = new Island();
                    DFS(M, i, j, visited, island);
                    if (island.getPoints().size() > 20)
                        islands.add(island);
                }

        return islands;
    }

}
