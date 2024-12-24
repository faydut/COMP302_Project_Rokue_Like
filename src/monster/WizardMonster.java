package monster;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.Timer;

import ui.Player;

class WizardMonster extends Monster {
	 private  JLabel[] []grid;
	 private ArrayList<JLabel> objectList;
   public WizardMonster(int row, int col, JLabel[] []grid, ArrayList<JLabel> objectList) {
       super(row, col);
       this.grid= grid;
       this.objectList= objectList;
   }

   @Override
   public void act(Player player ) {
   	//System.out.println("it is in wizarrd");
       
		// Randomly teleport the rune every 5 seconds
   	Timer spawnTimer = new Timer(5000, e -> teleport(objectList));
   	spawnTimer.start();
   	
       
   }
   private void teleport(ArrayList<JLabel> objectList) {
   //	System.out.println("it is in teleport");
   	for(JLabel obj: objectList) {
   		if(obj.getName().equals("rune")) {
   			//System.out.println("yes is hide:"+ obj.getText());
   			obj.setName("nonempty");
   		}
   	}
   	int num= random.nextInt(objectList.size());
   	objectList.get(num).setName("rune");
   	//System.out.println("rune object:"+objectList.get(num).getText());
   	
   }
}
