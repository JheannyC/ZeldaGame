package com.trigger.entity;

import com.trigger.main.Game;
import com.trigger.world.Camera;
import com.trigger.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy extends Entity {

    private double speed = 0.3;
    private int maskX = 8, maskY = 8, maskW = 10, maskH = 10;
    private int frames = 0, maxFrames = 20, index = 0, maxIndex = 1;
    private BufferedImage []sprites;

    public Enemy(int x, int y, int width, int height) {
        super(x, y, width, height, null);
        sprites = new BufferedImage[2];
        sprites [0] = Game.spriteSheet.getSprite(112, 16, 16, 16);
        sprites [1] = Game.spriteSheet.getSprite(112 + 16, 16, 16, 16);

    }

    public void tick() {

        if (isCollidingWithPlayer()) {
            if (Game.rand.nextInt(100) < 10) {
                Player.life--;
                System.out.println("Vida: " + Player.life);
                if (Player.life <= 0) {
                    //System.exit(1);

                }
            }
        } else {
            if ((int) x < Game.player.getX() &&
                    World.isFree((int) (x + speed), this.getY()) &&
                    !(isColliding((int) (x + speed), this.getY()))) {
                x += speed;
            } else if ((int) x > Game.player.getX() &&
                    World.isFree((int) (x - speed), this.getY()) &&
                    !(isColliding((int) (x - speed), this.getY()))) {
                x -= speed;
            }
            if ((int) y < Game.player.getY() &&
                    World.isFree(this.getX(), (int) (y + speed)) &&
                    !(isColliding(this.getX(), (int) (y + speed)))) {
                y += speed;
            } else if ((int) y > Game.player.getY() &&
                    World.isFree(this.getX(), (int) (y - speed)) &&
                    !(isColliding(this.getX(), (int) (y - speed)))) {
                y -= speed;
            }

            frames++;
            if (frames == maxFrames) {
                frames = 0;
                index++;
                if (index > maxIndex) {
                    index = 0;
                }
            }
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

    public boolean isCollidingWithPlayer() {
        Rectangle currentEnemy = new Rectangle(this.getX() + maskX, this.getY() + maskY, maskW, maskH);
        Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);
        return currentEnemy.intersects(player);
    }
    public void render (Graphics g) {
            g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
        //g.setColor(Color.white.);
        //g.fillRect(this.getX() + maskX - Camera.x, this.getY() + maskY - Camera.y, maskH, maskH);
    }
}

