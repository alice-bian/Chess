import java.awt.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.util.*;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class King extends ChessPiece {
    
    // create image of King
    private BufferedImage img;

    public King(Color c, int x, int y, int l) {
        super(c, x, y, l);
        
        try {
            if (img == null) {
                if (Color.BLACK.getRGB() == c.getRGB()) {
                    img = ImageIO.read(new File("files/black_king.png"));
                } else {
                    img = ImageIO.read(new File("files/white_king.png"));
                }
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }
    
    public String getType() {
        return "KING";
    } 
    
    public void draw(Graphics g) {
        g.drawImage(img, this.getXPos() + 10, this.getYPos() + 10, 30, 30, null); 
    }
    
    @Override
    public Set<Integer> moveOptions() {
        Set<Integer> moves = new TreeSet<Integer>();
        
        // if King is in a corner location:
        if (this.getLocation() == 0) {
            moves.add(1);
            moves.add(8);
            moves.add(9);
        } else if (this.getLocation() == 7) {
            moves.add(6);
            moves.add(14);
            moves.add(15);
        } else if (this.getLocation() == 56) {
            moves.add(48);
            moves.add(49);
            moves.add(57);
        } else if (this.getLocation() == 63) {
            moves.add(54);
            moves.add(55);
            moves.add(62);
        }
        
        // if King is in side location
        else if (this.getLocation() > 0 && getLocation() < 7) {
            moves.add(this.getLocation() - 1);
            moves.add(this.getLocation() + 1);
            moves.add(this.getLocation() + 7);
            moves.add(this.getLocation() + 8);
            moves.add(this.getLocation() + 9);
        } else if (this.getLocation() > 56 && getLocation() < 63) {
            moves.add(this.getLocation() - 1);
            moves.add(this.getLocation() + 1);
            moves.add(this.getLocation() - 7);
            moves.add(this.getLocation() - 8);
            moves.add(this.getLocation() - 9);
        } else if (this.getLocation() % 8 == 0) {
            moves.add(this.getLocation() - 8);
            moves.add(this.getLocation() + 8);
            moves.add(this.getLocation() - 7);
            moves.add(this.getLocation() + 1);
            moves.add(this.getLocation() + 9);
        } else if (this.getLocation() % 8 == 7) {
            moves.add(this.getLocation() - 8);
            moves.add(this.getLocation() + 8);
            moves.add(this.getLocation() + 7);
            moves.add(this.getLocation() - 1);
            moves.add(this.getLocation() - 9);
        }
        
        // if King is not along outer edges of board
        else {
            moves.add(getLocation() - 9);
            moves.add(getLocation() - 8);
            moves.add(getLocation() - 7);
            moves.add(getLocation() - 1);
            moves.add(getLocation() + 1);
            moves.add(getLocation() + 7);
            moves.add(getLocation() + 8);
            moves.add(getLocation() + 9);
        }
        
        return moves;
    }
}
