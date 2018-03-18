package com.example.tatevabgaryan.graphprocessing.helper;

import com.example.tatevabgaryan.graphprocessing.context.BitmapContext;
import com.example.tatevabgaryan.graphprocessing.model.Contour;
import com.example.tatevabgaryan.graphprocessing.model.Graph;
import com.example.tatevabgaryan.graphprocessing.model.Island;
import com.example.tatevabgaryan.graphprocessing.model.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tatev.Abgaryan on 2/11/2018.
 */

public class IslandHelper {

    private int rowSize, columnSize;

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

    public List<Island> findIslands(Graph graph) {
        Contour contour = graph.getContour();
        int M[][] = contour.getMatrix();
        int rowSize = contour.getRowSize();
        int columnSize = contour.getColumnSize();
        this.rowSize = rowSize;
        this.columnSize = columnSize;
        List<Island> islands = new ArrayList<>();
        // Make a bool array to mark visited cells.
        // Initially all cells are unvisited
        boolean visited[][] = new boolean[rowSize][columnSize];


        // Initialize count as 0 and travese through the all cells
        // of given matrix
        int maxSize = 0;
        Island graphIsland = null;
        for (int i = 0; i < rowSize; ++i)
            for (int j = 0; j < columnSize; ++j)
                if (M[i][j] == 1 && !visited[i][j]) // If a cell with
                {                                 // value 1 is not
                    // visited yet, then new island found, Visit all
                    // cells in this island and increment island count
                    Island island = new Island();
                    DFS(M, i, j, visited, island);
                    if (island.getPoints().size() > 20 && !isCornerIsland(island)) {
                        islands.add(island);
                        if(island.getPoints().size() > maxSize){
                            maxSize = island.getPoints().size();
                            graphIsland = island;
                        }
                    }
                }

        graphIsland.setGraph(true);
        graph.setGraphIsland(graphIsland);
        islands.remove(graphIsland);
        return islands;
    }

    private boolean isCornerIsland(Island island){
        if (island.getPoints().contains(new Point(0, BitmapContext.getHeight() - 1)))
            return true;
        else if (island.getPoints().contains(new Point(BitmapContext.getWidth() - 1, BitmapContext.getHeight() - 1)))
            return true;
        else if (island.getPoints().contains(new Point(1, 1)))
            return true;
        else if (island.getPoints().contains(new Point(BitmapContext.getWidth() - 1, 0)))
            return true;
        return false;
    }
}
