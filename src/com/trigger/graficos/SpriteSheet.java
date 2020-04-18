package com.trigger.graficos;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SpriteSheet {

    private BufferedImage spritesheet;

    public SpriteSheet (String path) {
        try {
            spritesheet = ImageIO.read(getClass().getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getSprite (int x, int y, int widht, int height) {
        return spritesheet.getSubimage(x, y, widht, height);
    }

}
