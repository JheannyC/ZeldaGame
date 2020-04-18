package com.trigger.world;

import com.trigger.entity.*;
import com.trigger.main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class World {

    private Tile [] tiles;
    public static int WIDTH, HEIGHT;


    public World(String path) {
        try {
            BufferedImage map = ImageIO.read(getClass().getResource(path));
            int [] pixels = new int[map.getWidth() * map.getHeight()];
            WIDTH = map.getWidth();
            HEIGHT = map.getHeight();
            tiles = new Tile [map.getWidth() * map.getHeight()];
            map.getRGB(0,0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());

            for (int xx = 0; xx < map.getWidth(); xx++) {
                for (int yy = 0; yy < map.getHeight(); yy++) {
                    int currentPixel = pixels [xx + (yy * map.getWidth())];

                    tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);

                    if (currentPixel == 0xFF000000 ) { //CHAO
                        tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
                    }
                    else if (currentPixel == 0xFFFFFFFF ) { //PAREDE
                        tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_WALL);
                    }
                    else if (currentPixel == 0xFFFF006E ) { //PLAYER
                        Game.player.setX(xx*16);
                        Game.player.setY(yy*16);
                    }
                    else if (currentPixel == 0xFFFFD800 ) { //ENEMY
                        Game.entities.add(new Enemy(xx * 16, yy * 16, 16, 16, Entity.ENEMY_EN));
                    }
                    else if (currentPixel == 0x0026FF ) { //WEAPON
                        Game.entities.add(new Weapon(xx * 16, yy * 16, 16, 16, Entity.WEAPON_EN));
                    }
                    else if (currentPixel == 0xFFFF6A00 ) { //BULLET
                        Game.entities.add(new Bullet(xx * 16, yy * 16, 16, 16, Entity.BULLET_EN));
                    }

                    else if (currentPixel == 0xFF7F0000 ) { //KIT
                        Game.entities.add(new LifePack(xx * 16, yy * 16, 16, 16, Entity.LIFEPACK_EN));

                    }

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void render (Graphics g) {
        int xStart = Camera.x >> 4;
        int yStart = Camera.y >> 4;

        int xFinal = xStart + (Game.WIDTH >> 4) + 1;
        int yFinal = yStart + (Game.HEIGHT >> 4) + 1;

        for (int xx = xStart; xx <= xFinal; xx++) {
            for (int yy = yStart; yy <= yFinal; yy++) {
                if (xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) {
                    continue;
                }
                Tile tile = tiles [xx + (yy*WIDTH)];
                tile.render(g);
            }

        }

    }
}
