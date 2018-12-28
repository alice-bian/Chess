import java.awt.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.util.*;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Queen extends ChessPiece {
    
    // create image of Queen 
    private BufferedImage img;

    public Queen(Color c, int x, int y, int l) {
        super(c, x, y, l);
        
        try {
            if (img == null) {
                if (Color.BLACK.getRGB() == c.getRGB()) {
                    img = ImageIO.read(new File("files/black_queen.png"));
                } else {
                    img = ImageIO.read(new File("files/white_queen.png"));
                }
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }
    
    public String getType() {
        return "QUEEN";
    }
    
    public void draw(Graphics g) {
        g.drawImage(img, this.getXPos() + 10, this.getYPos() + 10, 30, 30, null); 
    }
    
    /**
     * Note that the possible moves for a Queen is sorted in GameCourt.java.
     */
    @Override
    public Set<Integer> moveOptions() {
        Set<Integer> moves = new TreeSet<Integer>();
        
        return moves;
    }
}





