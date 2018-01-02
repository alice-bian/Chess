import java.awt.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.util.*;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Pawn extends ChessPiece {
    
    // create image of Pawn
    private BufferedImage img;

    public Pawn(Color c, int x, int y, int l) {
        super(c, x, y, l);
        
        try {
            if (img == null) {
                if (Color.BLACK.getRGB() == c.getRGB()) {
                    img = ImageIO.read(new File("files/black_pawn.png"));
                } else {
                    img = ImageIO.read(new File("files/white_pawn.png"));
                }
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }
    
    @Override
    public String getType() {
        return "PAWN";
    }
    
    public void draw(Graphics g) {
        g.drawImage(img, this.getXPos() + 10, this.getYPos() + 10, 30, 30, null); 
    }
    
    @Override
    public Set<Integer> moveOptions() {
        Set<Integer> moves = new TreeSet<Integer>();
        
        if (this.getColor().getRGB() == Color.WHITE.getRGB()) {
            moves.add(this.getLocation() - 8);
        } else {
            moves.add(this.getLocation() + 8);
        }
        
        return moves;
    }
}