package com.trigger.world;

import com.trigger.main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile {

    public static BufferedImage TILE_FLOOR = Game.spriteSheet.getSprite(0,0,16,16);
    public static BufferedImage TILE_WALL = Game.spriteSheet.getSprite(16,0,16,16);

    BufferedImage sprite;
    private int x, y;

    public Tile( int x, int y, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }

    public void render (Graphics g) {
        g.drawImage(sprite, x, y, null);

    }

}
