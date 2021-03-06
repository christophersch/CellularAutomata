package com.christophersch.cellularautomata;

import com.christophersch.cellularautomata.RuleSets.RuleSet;
import javafx.scene.input.MouseButton;

import java.util.ArrayList;
import java.util.Random;

public class Grid {
    // How many states have been processed so far?
    public static int generations = 0;

    public static boolean paused = true;

    public static boolean unaltered = true;

    static int mouse_grid_x = 0;
    static int mouse_grid_y = 0;
    static boolean mouse_down = false;
    static MouseButton mouse_button = MouseButton.PRIMARY;
    static int mouse_cell_selection = 1;

    static RuleSet rule_set;

    static int grid_width = 100;
    public static int grid_height = 100;

    // stores the actual 2D grid of cells
    public static int[][] grid = new int[grid_width][grid_height];
    public static int[][] next_grid = new int[grid_width][grid_height];

    public static int active_cell_count = 0;
    static ArrayList<Integer> active_cells = new ArrayList<>();

    public static void setRuleSet(RuleSet rules) {
        rule_set = rules;
        rule_set.initializeGrid();
    }

    public static void resetGrid() {
        grid = new int[grid_width][grid_height];
        next_grid = new int[grid_width][grid_height];
        generations = 0;
    }

    public static void update() {
        // Initialize the next iteration of the grid used by createCell();
        next_grid = new int[grid_width][grid_height];


        if (mouse_down && inBounds(mouse_grid_x,mouse_grid_y)) {
            if (mouse_button == MouseButton.PRIMARY)
                //fillRegion(mouse_grid_x-1,mouse_grid_y-1,2,2,1);
                setCell(mouse_grid_x,mouse_grid_y,mouse_cell_selection);
            else if (mouse_button == MouseButton.SECONDARY)
                setCell(mouse_grid_x,mouse_grid_y,0);
            unaltered = false;
        }

        // Loop over the grid, update each cell accordingly

        if (!paused) {
            active_cell_count = 0;
            unaltered = false;
            for (int x = 0; x < grid_width; x++) {
                for (int y = 0; y < grid_height; y++) {
                    rule_set.updateRules(grid[x][y], x, y);
                }
            }

            for (int x = 0; x < grid_width; x++) {
                if (grid_height >= 0) System.arraycopy(next_grid[x], 0, grid[x], 0, grid_height);
            }

            rule_set.updateCA();

            active_cells.add(active_cell_count);

            generations++;
        }


    }

    // Fills the grid with a specific cell type
    public static void fillGrid(int cell) {
        for(int x = 0; x < grid_width; x++) {
            for(int y = 0; y < grid_height; y++) {
                setCell(x,y,cell);
            }
        }
    }

    // Creates a new cell in the next generation
    public static void createCell(int x, int y, int cell_id) {
        if (inBounds(x,y)) {
            if (cell_id != 0)
                active_cell_count++;
            next_grid[x][y] = cell_id;
        }
    }

    // Creates a cell in the current generation
    public static int getCell(int x, int y) {
        if (inBounds(x,y))
            return grid[x][y];
        else
            return -1;
    }

    public static Boolean inBounds(int x, int y) {
        return (x >= 0 && y >= 0 && x < grid_width && y < grid_height);
    }

    public static void setCell(int x, int y, int cell_id) {
        if (inBounds(x,y))
            grid[x][y] = cell_id;
    }

    public static void fillRegion(int x, int y, int w, int h, int cells) {
        for(int xx = 0; xx < w; xx++) {
            for(int yy = 0; yy < h; yy++) {
                setCell(x+xx,y+yy,cells);
            }
        }
    }

    public static void fillRegionRandom(int x, int y, int w, int h, int cells) {
        for(int xx = 0; xx < w; xx++) {
            for(int yy = 0; yy < h; yy++) {
                Random rand = new Random(); //instance of random class
                int int_random = rand.nextInt(2);
                setCell(x+xx,y+yy,int_random);
            }
        }
    }

    public static int getGenerations() {
        return generations;
    }

    public static int getNeighborCount(int x, int y, int cell_type) {
        int count = 0;

        for(int xx = -1; xx < 2; xx++) {
            for(int yy = -1; yy < 2; yy++) {
                if (!(xx == 0 & yy == 0) && x + xx >= 0 && y + yy >= 0 && x + xx < grid_width && y + yy < grid_height)
                    if (grid[x + xx][y + yy] == cell_type)
                        count++;

            }
        }

        return count;
    }
}
