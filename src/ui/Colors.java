
package ui;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class Colors {
    public static Map<String, Color> colors = new HashMap();

    public void loadColors(){
        colors.put("TopPanel", new Color( 190,215,170 ));
        colors.put("CenterPanel", new Color( 213,225,204 ));
        colors.put("BottomPanel", new Color( 200,215,190 ));
        colors.put("LeftPanel", new Color( 213,225,190 ));
        colors.put("RightPanel", new Color( 213,225,190 ));
        colors.put("TitlePanel", new Color( 190,215,170 ));
        colors.put("SearcherPanel", new Color( 213,225,204 ));
        
        
        
    }

}
