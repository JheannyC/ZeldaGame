package com.trigger.entity;

import com.trigger.main.Game;
import com.trigger.world.Camera;
import com.trigger.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {

    public boolean right, up, left, down;
    public double speed  = 0.8;
    private int frames = 0, maxFrames = 4, index = 0, maxIndex = 3;
    private boolean moved = false;
    private BufferedImage []rightPlayer;
    private BufferedImage []leftPlayer;

    public int rightDir = 0, leftDir = 1, dir = rightDir;

    public static double life = 100, maxLife = 100;

    public int ammo = 0;

    public Player(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
        rightPlayer = new BufferedImage[4];
        leftPlayer = new BufferedImage[4];
        initPlayer();

    }

    public void tick () {
	    playerMoviment();
        collisionLifePack();
        collisionAmmo();
        camera();
    }

    public void render(Graphics g) {
        moviment(g);
    }

    public void initPlayer () {
        for (int i = 0; i < 4; i++) {
            rightPlayer [i] = Game.spriteSheet.getSprite(32 + (i*16), 0, 16, 16);
        }
        for (int i = 0; i < 4; i++) {
            leftPlayer [i] = Game.spriteSheet.getSprite(32 + (i*16), 16, 16, 16);
        }
    }

    public void playerMoviment() {
        moved = false;
        if (right && World.isFree((int) (x + speed), this.getY())){
            moved = true;
            dir = rightDir;
            x += speed;
        }
        else if (left && World.isFree((int) (x - speed), this.getY())){
            moved = true;
            dir = leftDir;
            x -= speed;
        }

        if (up && World.isFree(this.getX(), (int) (y - speed))) {
            moved = true;
            y -= speed;
        }
        else if (down && World.isFree (this.getX(), (int) (y + speed))){
            moved = true;
            y += speed;
        }

        if (moved) {
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

    public void moviment(Graphics g) {
        if (dir == rightDir) {
            g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
        }

        else if (dir == leftDir) {
            g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
        }
    }

    public void collisionLifePack () {
        for (int i = 0; i < Game.entities.size(); i++) {
            Entity entity = Game.entities.get(i);
            if (entity instanceof  LifePack) {
                if(Entity.isColliding(this, entity)) {
                    life += 8;
                    if (life >= 100)
                        life = 100;
                    Game.entities.remove(i);
                    return;
                }
            }
        }
    }
    public void collisionAmmo () {
        for (int i = 0; i < Game.entities.size(); i++) {
            Entity bullet = Game.entities.get(i);
            if (bullet instanceof  Bullet) {
                if(Entity.isColliding(this, bullet)) {
                    ammo += 10;
                    Game.entities.remove(bullet);
                }
            }
        }
    }


    public void camera () {
        Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.WIDTH*16 - Game.WIDTH);
        Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, World.HEIGHT*16 - Game.HEIGHT);
    }

}
