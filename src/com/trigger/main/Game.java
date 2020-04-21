package com.trigger.main;

import com.trigger.entity.BulletShoot;
import com.trigger.entity.Enemy;
import com.trigger.entity.Entity;
import com.trigger.entity.Player;
import com.trigger.graficos.SpriteSheet;
import com.trigger.graficos.UI;
import com.trigger.world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener {

    public static JFrame frame;
    public static final int WIDTH = 240;
    public static final int HEIGHT = 160;
    public static final int SCALE = 3;

    private Thread thread;
    private boolean isRunning;

    private final BufferedImage image;

    public static List <Entity> entities;
    public static List <Enemy> enemies;
    public static List <BulletShoot> bullets;
    public static SpriteSheet spriteSheet;

    public static Player player;
    public static World world;
    public static Random rand;
    public static Menu menu;
    public static int CUR_LEVEL = 1, MAX_LEVEL = 4;

    public UI ui;
    public static String gameState = "MENU";
    private int framesGameOver = 0;
    private boolean showMessageGameOver = true, restartGame = false;

    public Game() {

        rand = new Random();
        addKeyListener(this);
        addMouseListener(this);

        setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
        initFrame();

        //Iniciando objetos
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        ui = new UI();
        initObjects();

    }
    public void  initObjects () {
        entities = new ArrayList<>();
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        spriteSheet = new SpriteSheet("/spritesheet.png");
        player = new Player(0, 0, 16, 16, spriteSheet.getSprite(32, 0, 16, 16));
        entities.add(player);
        world = new World("/level1.png");
        menu = new Menu();
    }
    public void initFrame() {
        frame = new JFrame("Meu jogo");
        frame.add(this);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
    public synchronized void start() {
        thread = new Thread(this);
        isRunning = true;
        thread.start();

    }
    public synchronized void stop() {
        isRunning =false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void tick(){
        if(gameState.equals("NORMAL")) {
            this.restartGame = false;
            addEnemies();
            addBullets();
            nextLevel();
        } else if (gameState.equals("GAME_OVER")) {
            gameState = "GAME_OVER";
            this.framesGameOver++;
            if(this.framesGameOver == 15) {
                this.framesGameOver = 0;
                this.showMessageGameOver = !this.showMessageGameOver;
            }
            if (restartGame) {
                this.restartGame = false;
                gameState = "NORMAL";
                String newWorld = "level"+CUR_LEVEL+".png";
                World.restartGame(newWorld);
            }
        }else if (gameState.equals("MENU")) {
            menu.tick();
        }

    }


    private void addEnemies () {
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            e.tick();
        }
    }
    private void addBullets () {
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).tick();
        }
    }

    private void nextLevel () {
        if (enemies.size() == 0) {
            CUR_LEVEL++;
            if(CUR_LEVEL > MAX_LEVEL) {
                CUR_LEVEL = 1;
            }
            String newWorld = "level" + CUR_LEVEL + ".png";
            World.restartGame(newWorld);
        }
    }
    public  void  render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = image.getGraphics();
        g.setColor(new Color(0, 0, 0));
        g.fillRect(0,0, WIDTH, HEIGHT);

        world.render(g); renderEntities(g); renderBullet(g);
        ui.render(g);
        g.dispose(); //melhorar performance
        g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
        renderUi(g);
        gameOver(g);

        bs.show();
    }
    public void gameOver (Graphics g) {
        if (gameState.equals("GAME_OVER")) {
            Graphics2D g2 = (Graphics2D) g;
            g.setColor(new Color(0, 0, 0, 200));
            g.fillRect(0,0, WIDTH*SCALE, HEIGHT*SCALE);

            g.setFont(new Font("Impact", Font.PLAIN, 60));
            g.setColor(Color.BLACK);
            g.drawString("GAME OVER", ((WIDTH*SCALE)/2) - 118, ((HEIGHT*SCALE)/2) - 5);

            g.setFont(new Font("Impact", Font.PLAIN, 60));
            g.setColor(new Color(255, 255, 255 ));
            g.drawString("GAME OVER", ((WIDTH*SCALE)/2) - 120, ((HEIGHT*SCALE)/2) - 10);

            if (showMessageGameOver) {
                g.setFont(new Font("Impact", Font.PLAIN, 40));
                g.setColor(new Color(255, 255, 255 ));
                g.drawString("> PRESSIONE 'ENTER' PARA REINICIAR", ((WIDTH*SCALE)/2) -260, ((HEIGHT*SCALE)/2) + 100);
            }

        }
        else if( gameState.equals("MENU")) {
            menu.render(g);
        }
    }
    public void renderEntities(Graphics g) {
        for (Entity e : entities) {
            e.render(g);
        }
    }

    public void renderBullet(Graphics g) {
        for (BulletShoot bullet : bullets) {
            bullet.render(g);
        }
    }
    public void renderUi (Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("Impact", Font.PLAIN, 20));
        g.drawString("Munição: " + player.ammo, 600, 30);
        g.setColor(Color.white);
        g.setFont(new Font("Impact", Font.PLAIN, 20));
        g.drawString("Vida: " + (int)Game.player.life + " / " + (int)Game.player.maxLife, 30, 30);
        g.setColor(Color.white);
        g.setFont(new Font("Impact", Font.PLAIN, 20));
        g.drawString("Level: " + CUR_LEVEL, 350, 30);
    }

    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        int frames = 0;
        double timer = System.currentTimeMillis();
        requestFocus();
        while (isRunning) {

            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            if (delta >=1) {

                tick();
                render();

                frames++;
                delta--;
            }

            if (System.currentTimeMillis() - timer >= 1000) {
                //System.out.println("FPS: " + frames);
                frames = 0;
                timer+=1000;
            }
        }
        stop();

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            player.right = true;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A){
            player.left = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
            player.up = true;
            if (gameState.equals("MENU")){
                menu.up = true;
            }
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
            player.down = true;
            if (gameState.equals("MENU")){
                menu.down = true;
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            player.shoot = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            restartGame = true;

            if(gameState.equals("MENU")){
                menu.enter = true;
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            gameState = "MENU";
            menu.pause = true;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            player.right = false;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A){
            player.left = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
            player.up = false;

        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
            player.down = false;

        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        player.mouseShoot = true;
        player.mx = (e.getX() / 3);
        player.my = (e.getY() / 3);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        player.mouseShoot = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
