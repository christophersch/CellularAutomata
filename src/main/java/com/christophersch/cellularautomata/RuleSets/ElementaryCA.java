package com.christophersch.cellularautomata.RuleSets;

import com.christophersch.cellularautomata.Grid;
import javafx.scene.paint.Color;

public abstract class ElementaryCA implements RuleSet {
    @Override
    public int getMaxCellID() {
        return 1;
    }

    @Override
    public String getName(int cell_id) {
        return (cell_id == 1 ? "ON" : "OFF");
    }

    public Color getColor(int cell_id) {
        return (cell_id == 1 ? Color.WHITE : Color.BLACK);
    }

    int elementary_y = 0;

    public void initializeGrid() {
        Grid.setCell(50,0,1);
    }


    public void updateRules(int cell_id, int x, int y) {

    }

    boolean[] getThree(int x, int y) {
        boolean[] three = new boolean[3];
        for(int i = -1; i < 2; i++) {
            if (Grid.getCell(x+i,y) == 1)
                three[i+1] = true;
        }
        return three;
    }

    public void updateCA() {
        if (Grid.generations > 96) {
            for (int x = 0; x < Grid.grid_height; x++) {
                System.arraycopy(Grid.grid[x], 1, Grid.grid[x], 0, Grid.grid_height - 1);
            }
        } else {
            elementary_y++;
        }
    }
}
