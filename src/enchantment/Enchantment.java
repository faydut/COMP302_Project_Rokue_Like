package enchantment;


import java.util.ArrayList;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

public abstract class Enchantment {
	
	    private  String type;
		public Enchantment(String type) {
	        this.type = type;
	        
	    }

	    public String getType() {
	        return type;
	    }
	    
	    
       public  abstract void useEnchantment() ;
	    	
	    	
	   
	    
	


}
