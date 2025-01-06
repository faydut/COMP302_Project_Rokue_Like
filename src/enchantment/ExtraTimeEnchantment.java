package enchantment;

import java.util.LinkedHashMap;

import ui.GameManager;

public class ExtraTimeEnchantment extends Enchantment{
	GameManager gameManager;
	public ExtraTimeEnchantment(String type, GameManager gameManager) {
		super(type);
		this.gameManager= gameManager;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void useEnchantment() {
		gameManager.addExtraTime(5);
		
	}

	
	
	
	

}
