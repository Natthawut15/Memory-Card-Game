package src.Card;

import javax.swing.ImageIcon;

//import GUI.*;
public class Card  {
    private String imagePath;
    private boolean matched;

    public Card(String imagePath) {

        this.imagePath = imagePath;
        this.matched = false;
    }

    public String getImagePath() {

        return imagePath;
    }

    public boolean isMatched() {

        return matched;
    }

    public void setMatched(boolean matched) {

        this.matched = matched;
    }

    public ImageIcon getImage() {

        return new ImageIcon(imagePath);
    }
    
}
