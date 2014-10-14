/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

/**
 *
 * @author Stephen
 */
public final class Colors2 {
    /**
     * Removes all previously applied color and formatting attributes.
     */
    public static final String NORMAL = "\u000f";
    /**
     * Bold text.
     */
    public static final String BOLD = "\u0002";
    /**
     * Underlined text.
     */
    public static final String UNDERLINE = "\u001f";
    /**
     * Reversed text (may be rendered as italic text in some clients).
     */
    public static final String REVERSE = "\u0016";
    /**
     * White coloured text.
     */
    public static final String WHITE = "00";
    /**
     * Black coloured text.
     */
    public static final String BLACK = "01";
    /**
     * Dark blue coloured text.
     */
    public static final String DARK_BLUE = "02";
    /**
     * Dark green coloured text.
     */
    public static final String DARK_GREEN = "03";
    /**
     * Red coloured text.
     */
    public static final String RED = "04";
    /**
     * Brown coloured text.
     */
    public static final String BROWN = "05";
    /**
     * Purple coloured text.
     */
    public static final String PURPLE = "06";
    /**
     * Olive coloured text.
     */
    public static final String OLIVE = "07";
    /**
     * Yellow coloured text.
     */
    public static final String YELLOW = "08";
    /**
     * Green coloured text.
     */
    public static final String GREEN = "09";
    /**
     * Teal coloured text.
     */
    public static final String TEAL = "10";
    /**
     * Cyan coloured text.
     */
    public static final String CYAN = "11";
    /**
     * Blue coloured text.
     */
    public static final String BLUE = "12";
    /**
     * Magenta coloured text.
     */
    public static final String MAGENTA = "13";
    /**
     * Dark gray coloured text.
     */
    public static final String DARK_GRAY = "14";
    /**
     * Light gray coloured text.
     */
    public static final String LIGHT_GRAY = "15";
    
    public static final String BASE = "\u0003";
    private Colors2(){
        
    }
    
    public static final String getColors(String color) {
        return(BASE+color);
    }
    public static final String getColors(String text, String background){
        return(BASE+text+","+background);
    }
    public static final String getRandomColor(){
        int color = (int) (Math.random()*16);
        return (BASE+Integer.toString(color));
    }
    public static final String getRandomColorAndBackground(){
        int color = (int) (Math.random()*16);
        int color2 = (int) (Math.random()*16);
        return (BASE+Integer.toString(color)+","+color2);
    }
}
