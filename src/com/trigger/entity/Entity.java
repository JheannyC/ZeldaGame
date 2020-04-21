package com.trigger.entity;

import com.trigger.main.Game;
import com.trigger.world.Camera;
import org.w3c.dom.css.Rect;

import java.awt.*;
import java.awt.image.BufferedImage;

@SuppressWarnings("ALL")
public class Entity {

    protected double x, y;
    protected int width, height;
    private int maskX, maskY, maskW, maskH;

    private BufferedImage sprite;
    public static BufferedImage LIFEPACK_EN = Game.spriteSheet.getSprite(16*6, 0 ,16, 16);
    public static BufferedImage WEAPON_EN = Game.spriteSheet.getSprite(16*7, 0 ,16, 16);
    public static BufferedImage BULLET_EN = Game.spriteSheet.getSprite(16*6, 16 ,16, 16);
    public static BufferedImage ENEMY_EN = Game.spriteSheet.getSprite(16*7, 16 ,16, 16);
    public static BufferedImage GUN_LEFT = Game.spriteSheet.getSprite(16*9, 0 ,16, 16);
    public static BufferedImage GUN_RIGHT = Game.spriteSheet.getSprite(16*8, 0 ,16, 16);
    public static BufferedImage ENEMY_FEEDBACK = Game.spriteSheet.getSprite(16*9, 16 ,16, 16);


    public Entity(int x, int y, int width, int height, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;

        this.maskX = 0;
        this.maskY = 0;
        this.maskW = width;
        this.maskH = height;
    }
    public void  setMask (int maskX, int maskY, int maskW, int maskH) {
            this.maskX = maskX;
            this.maskY = maskY;
            this.maskW = maskW;
            this.maskH = maskH;

    }
    public int getX() {
        return (int) x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return (int) y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return (int) width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return (int) height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void render (Graphics g) {
        g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
        //g.setColor(Color.red);
        //g.fillRect(this.getX() + maskX - Camera.x, this.getY() + maskY - Camera.y, maskW, maskH);
    }

    public void tick() {

    }

    public static boolean isColliding (Entity e1, Entity e2) {
        Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskX, e1.getY() + e1.maskY, e1.maskW, e1.maskH);
        Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskX, e2.getY() + e2.maskY, e2.maskW, e2.maskH);
        return  e1Mask.intersects(e2Mask);
    }
}
