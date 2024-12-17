package ui;

class ArcherMonster extends Monster {
    public ArcherMonster(int row, int col) {
        super(row, col);
    }

    @Override
    public void act(Player player) {
    	
    	System.out.println("it is in aarcher");
        int distance = Math.abs(player.getRow() - row) + Math.abs(player.getCol() - col);
        
        if (distance < 4 && !player.isWearingCloak()) {
            player.loseLife();
        }
    }
}