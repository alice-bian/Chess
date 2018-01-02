import java.awt.*;
import javax.swing.*;


public class BoardTile extends JPanel {
    public static final int HEIGHT = 50;
    public static final int WIDTH = 50;
    
    private int px; // sets x-location of BoardTile
    private int py; // sets y-location of BoardTile
    
    private int label; // assigns label of 1-64 to BoardTile, 
                       // helps identify which BoardTile is which
    
    boolean status; // true if occupied; false if vacant
    
    private Color color;
    

    // constructor
    public BoardTile(int x, int y, int l, boolean s, Color c) {
        px = x;
        py = y;
        label = l;
        status = s;
        color = c;
    }

    /*** GETTERS **********************************************************************************/
    public int getPx() {
        return px;
    }
    
    public int getPy() {
        return py;
    }
    
    public int getCoord() {
        return (px * 10) + py;
        // ex: if (1, 1), then returns 11
    }
    
    public int getLabel() {
        return label;
    }
    
    public boolean getStatus() {
        return status;
    }
    
    public Color getColor() {
        return color;
    }

    /*** SETTERS **********************************************************************************/
    public void changeStatus() {
        status = !status;
    }

    /*** UPDATES AND OTHER METHODS ****************************************************************/
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(px, py, 50, 50);
    }
    
    public void highlight() {
        color = Color.RED;
    }
    
    public void unhighlight(Color col) {
        color = col;
    }
    
}
