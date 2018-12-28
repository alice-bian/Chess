import java.awt.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.util.*; 

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Bishop extends ChessPiece {
    
    // create image of Bishop
    private BufferedImage img;

    public Bishop(Color c, int x, int y, int l) {
        super(c, x, y, l);
        
        try {
            if (img == null) {
                if (Color.BLACK.getRGB() == c.getRGB()) {
                    img = ImageIO.read(new File("files/black_bishop.png"));
                } else {
                    img = ImageIO.read(new File("files/white_bishop.png"));
                }
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }
    
    public String getType() {
        return "BISHOP";
    }
    
    public void draw(Graphics g) {
        g.drawImage(img, this.getXPos() + 10, this.getYPos() + 10, 30, 30, null); 
    }
    
    /**
     * Note that the possible moves for a Bishop is sorted in GameCourt.java.
     */
    @Override
    public Set<Integer> moveOptions() {
        Set<Integer> moves = new TreeSet<Integer>();
        
        return moves;
    }
}





