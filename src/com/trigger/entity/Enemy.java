package com.trigger.entity;

import com.trigger.main.Game;
import com.trigger.world.Camera;
import com.trigger.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy extends Entity {

    private double speed = 0.4;
    private int maskX = 8, maskY = 8, maskW = 10, maskH = 10;

    public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
    }

    public void tick() {
        //if (Game.rand.nextInt(100) < 50) {

        if ((int) x < Game.player.getX() &&
                World.isFree((int) (x + speed), this.getY()) &&
                !(isColliding((int) (x + speed), this.getY()))) {
            x += speed;
        }
        else if ((int)x > Game.player.getX() &&
                World.isFree((int) (x - speed), this.getY()) &&
                !(isColliding((int) (x - speed), this.getY()))){
            x -= speed;
        }
        if ((int) y < Game.player.getY() &&
                World.isFree(this.getX(), (int) (y + speed)) &&
                !(isColliding(this.getX(), (int) (y + speed)))) {
            y += speed;
        }
        else if ((int)y > Game.player.getY() &&
                World.isFree(this.getX(), (int) (y - speed)) &&
                !(isColliding(this.getX(), (int) (y - speed)))){
            y -= speed;
        }


    }

    public boolean isColliding (int xNext, int yNext) {
        Rectangle currentEnemy = new Rectangle(xNext + maskX, yNext + maskY, maskW, maskH);
        for (int i = 0; i < Game.enemies.size(); i++) {
            Enemy e = Game.enemies.get(i);
            if (e == this) continue;
            Rectangle targetEnemy = new Rectangle(e.getX() + maskX, e.getY() + maskY, maskW, maskH);
            if (currentEnemy.intersects(targetEnemy)) {
                return true;
            }
        }
        return false;
    }
    /*public void render (Graphics g) {
        super.render(g);
        g.setColor(Color.white.);
        g.fillRect(this.getX() + maskX - Camera.x, this.getY() + maskY - Camera.y, maskH, maskH);
    }*/
}
