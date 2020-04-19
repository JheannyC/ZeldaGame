package com.trigger.entity;

import com.trigger.main.Game;
import com.trigger.world.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BulletShoot extends Entity{

    private double dx, dy;
    private double speed = 2;

    private int life = 40, curLife = 0;

    public BulletShoot(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
        super(x, y, width, height, sprite);
        this.dx = dx;
        this.dy = dy;

    }

    public void tick () {
        x += dx * speed;
        y += dy * speed;
        curLife ++;
        if (curLife == life) {
            Game.bullets.remove(this);
        }
    }
    public void render (Graphics g) {
        g.setColor(Color.yellow);
        g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, width, height);

    }

}
