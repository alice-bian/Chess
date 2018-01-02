import java.awt.*;
import java.awt.Graphics;
import java.util.*;


public abstract class ChessPiece {
    
    protected Color color;
    
    protected int location;
    
    protected int XPos;
    protected int YPos;

    /**
     * Constructor
     */
    public ChessPiece(Color c, int x, int y, int l) {
        color = c; // either black or white
        XPos = x;
        YPos = y;
        location = l;
    }
    

    /*** GETTERS **********************************************************************************/
    public Color getColor() {
        return color;
    }
    
    public int getLocation() {
        return location;
    }
    
    public int getXPos() {
        return XPos;
    }
    
    public int getYPos() {
        return YPos;
    }
    
    public abstract String getType();
    

    /*** SETTERS **********************************************************************************/
    public void setLocation(int loc) {
        location = loc;
    }
    
    public void setXPos(int x) {
        XPos = x;
    }
    
    public void setYPos(int y) {
        YPos = y;
    }
    
    
    /*** UPDATES AND OTHER METHODS ****************************************************************/

    public abstract void draw(Graphics g);
    
    public abstract Set<Integer> moveOptions();
}