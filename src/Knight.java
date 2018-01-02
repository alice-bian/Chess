import java.awt.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.util.*;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Knight extends ChessPiece {
    
    // create image of Knight
    private BufferedImage img;

    public Knight(Color c, int x, int y, int l) {
        super(c, x, y, l);
        
        try {
            if (img == null) {
                if (Color.BLACK.getRGB() == c.getRGB()) {
                    img = ImageIO.read(new File("files/black_knight.png"));
                } else {
                    img = ImageIO.read(new File("files/white_knight.png"));
                }
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }
    
    public String getType() {
        return "KNIGHT";
    }
    
    public void draw(Graphics g) {
        g.drawImage(img, this.getXPos() + 10, this.getYPos() + 10, 30, 30, null); 
    }
    
    @Override
    public Set<Integer> moveOptions() {
        Set<Integer> tempMoves = new TreeSet<Integer>();
        Set<Integer> moves = new TreeSet<Integer>();
        
        tempMoves.add(this.getLocation() - 6);
        tempMoves.add(this.getLocation() - 10);
        tempMoves.add(this.getLocation() - 15);
        tempMoves.add(this.getLocation() - 17);
        tempMoves.add(this.getLocation() + 6);
        tempMoves.add(this.getLocation() + 10);
        tempMoves.add(this.getLocation() + 15);
        tempMoves.add(this.getLocation() + 17);
        
        Iterator<Integer> itr = tempMoves.iterator();
        while (itr.hasNext()) {
            int temp = itr.next();
            if (temp >= 0 && temp < 64) {
                moves.add(temp);
            }
        }
        
        return moves;
    }
}







