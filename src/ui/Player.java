package ui;

class Player {
    private int row, col;
    private int lives = 3;
    private boolean wearingCloak = false;

    public Player(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getLives() {
		return lives;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getCol() {
        return col;
    }

    public boolean isWearingCloak() {
        return wearingCloak;
    }

    public void wearCloak() {
        this.wearingCloak = true;
    }

    public void loseLife() {
        lives--;
        System.out.println("Life lost! Remaining lives: " + lives);
        if (lives <= 0) {
            System.out.println("Game Over!");
          //  System.exit(0); // gane over ekrani gelecek
        }
    }
}
