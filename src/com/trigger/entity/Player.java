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
    private BufferedImage [] upPlayer;
    private BufferedImage [] downPlayer;
    public int rightDir = 0, leftDir = 1, dir = rightDir;

    public Player(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
        rightPlayer = new BufferedImage[4];
        leftPlayer = new BufferedImage[4];
        upPlayer = new BufferedImage [4];
        downPlayer = new BufferedImage[4];

        for (int i = 0; i < 4; i++) {
            rightPlayer [i] = Game.spriteSheet.getSprite(32 + (i*16), 0, 16, 16);
        }
        for (int i = 0; i < 4; i++) {
            leftPlayer [i] = Game.spriteSheet.getSprite(32 + (i*16), 16, 16, 16);
        }
        for (int i = 0; i < 4; i++) {
            upPlayer [i] = Game.spriteSheet.getSprite(32 + (i*16), 16*2, 16, 16);
        }
        for (int i = 0; i < 4; i++) {
            downPlayer [i] = Game.spriteSheet.getSprite(32 + (i*16), 16*3, 16, 16);
        }

    }

    public void tick () {
        moved = false;
        if (right && World.isFree((int) (x+speed), this.getY())){
            moved = true;
            dir = rightDir;
            x += speed;
        }
        else if (left && World.isFree((int) (x-speed), this.getY())){
            moved = true;
            dir = leftDir;
            x -= speed;
        }

        if (up && World.isFree(this.getX(), (int) (y-speed))) {
            moved = true;
            y -= speed;
        }
        else if (down && World.isFree (this.getX(), (int) (y+speed))){
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

        Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.WIDTH*16 - Game.WIDTH);
        Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, World.HEIGHT*16 - Game.HEIGHT);
    }

    public void render(Graphics g) {

        if (dir == rightDir) {
            g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
        }

        else if (dir == leftDir) {
            g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
        }


    }



}
