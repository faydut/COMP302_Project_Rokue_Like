package monster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.Timer;

import ui.Player;

abstract class Monster {
    protected int row, col;
    Random random= new Random();

    public Monster(int row, int col) {
    	
        this.row = row;
        this.col = col;
    }

    public abstract void act(Player player);
}




