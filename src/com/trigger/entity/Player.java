package com.trigger.entity;

import com.trigger.graficos.SpriteSheet;
import com.trigger.main.Game;
import com.trigger.world.Camera;
import com.trigger.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static com.trigger.main.Game.*;

public class Player extends Entity {

    public boolean right, up, left, down;
    public double speed  = 0.8;
    private int frames = 0, maxFrames = 4, index = 0, maxIndex = 3;
    private boolean moved = false;
    private BufferedImage []rightPlayer;
    private BufferedImage []leftPlayer;
    private BufferedImage playerDamage;

    public int rightDir = 0, leftDir = 1, dir = rightDir;

    public double life = 100, maxLife = 100;

    public int ammo = 0;
    private boolean hasGun = false;

    public boolean isDamaged = false;
    private int damageFrames = 0;
    public boolean shoot = false, mouseShoot = false;
    public int mx, my;

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
        collisionGun();
        camera();
    }

    public void render(Graphics g) {
        moviment(g);
    }

    public void initPlayer () {
        playerDamage = spriteSheet.getSprite(0, 16, 16, 16);

        for (int i = 0; i < 4; i++) {
            rightPlayer [i] = spriteSheet.getSprite(32 + (i*16), 0, 16, 16);
        }
        for (int i = 0; i < 4; i++) {
            leftPlayer [i] = spriteSheet.getSprite(32 + (i*16), 16, 16, 16);
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
        if (isDamaged) {
            this.damageFrames++;
            if (this.damageFrames == 5) {
                this.damageFrames = 0;
                isDamaged = false;
            }
        }
        if (life <= 0) {
            newGame();
        }

        if (shoot) {
            shoot = false;
            if (hasGun && ammo > 0){
                ammo--;
                 int dx = 0;
                if (dir == rightDir){
                    dx = 1;
                } else {
                    dx = -1;
                }

                BulletShoot bullet = new BulletShoot(this.getX(), this.getY() + 8, 3, 3, null, dx , 0);
                bullets.add(bullet);
            }
        }

        if (mouseShoot) {
            mouseShoot = false;
            if (hasGun && ammo > 0){
                ammo--;
                int px = 0, py = 8;
                double angle = 0;

                if (dir == rightDir){
                    px = 18;
                    angle = (Math.atan2(my - (this.getY() + py - Camera.y), mx - (this.getX() + px - Camera.x)));
                }
                else{
                    px = -8;
                    angle = (Math.atan2(my - (this.getY() + py - Camera.y), mx - (this.getX() + px - Camera.x)));
                }

                double dx = Math.cos(angle);
                double dy = Math.sin(angle);

                BulletShoot bullet = new BulletShoot(this.getX() + px, this.getY() + py, 3, 3, null, dx , dy);
                bullets.add(bullet);
            }
        }

    }
    public void newGame () {
        Game.entities = new ArrayList<>();
        Game.enemies = new ArrayList<>();
        spriteSheet = new SpriteSheet("/spritesheet.png");
        Game.player = new Player(0, 0, 16, 16, Game.spriteSheet.getSprite(32, 0, 16, 16));
        Game.entities.add(Game.player);
        Game.world = new World("/map.png");
    }

    public void moviment(Graphics g) {
        if (!isDamaged) {
        if (dir == rightDir) {
            g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
            if (hasGun) {
                g.drawImage(Entity.GUN_RIGHT_EN, this.getX() + 8 - Camera.x, this.getY() - Camera.y, null);

            }
        }

        else if (dir == leftDir) {
            g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
            if (hasGun) {
                g.drawImage(Entity.GUN_LEFT_EN, this.getX() - 8 - Camera.x, this.getY() - Camera.y, null);
            }
        }
        }else {
            g.drawImage(playerDamage, this.getX() - Camera.x, this.getY() - Camera.y, null);
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
                    ammo += 15;
                    Game.entities.remove(bullet);
                }
            }
        }
    }
    public void collisionGun () {
        for (int i = 0; i < Game.entities.size(); i++) {
            Entity gun = Game.entities.get(i);
            if (gun instanceof  Weapon) {
                if(Entity.isColliding(this, gun)) {
                    hasGun = true;
                    Game.entities.remove(gun);
                }
            }
        }
    }



    public void camera () {
        Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.WIDTH*16 - Game.WIDTH);
        Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, World.HEIGHT*16 - Game.HEIGHT);
    }

}
