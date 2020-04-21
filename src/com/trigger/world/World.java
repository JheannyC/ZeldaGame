package com.trigger.world;

import com.trigger.entity.*;
import com.trigger.graficos.SpriteSheet;
import com.trigger.main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import static com.trigger.main.Game.spriteSheet;

public class World {

    public static Tile [] tiles;
    public static int WIDTH, HEIGHT;
    public static final int TILE_SIZE = 16;

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
                        tiles[xx + (yy * WIDTH)] = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL);
                    }
                    else if (currentPixel == 0xFFFF006E ) { //PLAYER
                        Game.player.setX(xx*16);
                        Game.player.setY(yy*16);
                    }
                    else if (currentPixel == 0xFFFFD800 ) { //ENEMY
                        Enemy enemy = new Enemy(xx * 16, yy * 16, 16, 16);
                        Game.entities.add(enemy);
                        Game.enemies.add(enemy);
                    }
                    else if (currentPixel == 0xFF0026FF ) { //WEAPON
                        Weapon weapon = new Weapon(xx * 16, yy * 16, 16, 16, Entity.WEAPON_EN);
                        Game.entities.add(weapon);
                    }
                    else if (currentPixel == 0xFFFF6A00 ) { //BULLET
                        Bullet bullet = new Bullet(xx * 16, yy * 16, 16, 16, Entity.BULLET_EN);
                        Game.entities.add(bullet);
                    }
                    else if (currentPixel == 0xFF7F0000 ) {
                        LifePack lifePack = new LifePack(xx * 16, yy * 16, 16, 16, Entity.LIFEPACK_EN);
                        Game.entities.add(lifePack);

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

    public static boolean isFree (int xNext, int yNext) {
        int x1 = xNext / TILE_SIZE;
        int y1 = yNext / TILE_SIZE;

        int x2 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
        int y2 = yNext / TILE_SIZE;

        int x3 = xNext / TILE_SIZE;
        int y3 = (yNext + TILE_SIZE - 1) / TILE_SIZE;

        int x4 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
        int y4 = (yNext + TILE_SIZE - 1) / TILE_SIZE;

        return !((tiles [x1 + (y1 * World.WIDTH)] instanceof WallTile) ||
                (tiles [x2 + (y2 * World.WIDTH)] instanceof WallTile) ||
                (tiles [x3 + (y3 * World.WIDTH)] instanceof WallTile) ||
                (tiles [x4 + (y4 * World.WIDTH)] instanceof WallTile));

    }
    public static void restartGame(String level) {
        Game.entities = new ArrayList<>();
        Game.enemies = new ArrayList<>();
        spriteSheet = new SpriteSheet("/spritesheet.png");
        Game.player = new Player(0, 0, 16, 16, Game.spriteSheet.getSprite(32, 0, 16, 16));
        Game.entities.add(Game.player);
        Game.world = new World("/"+ level);
    }
}
