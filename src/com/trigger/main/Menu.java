package com.trigger.main;

import java.awt.*;

public class Menu {

    public String[] options = {"newGame", "loadGame", "exit"};
    public int currentOption = 0;
    public int maxOption = options.length - 1;
    public boolean up, down, enter, pause = false;


    public void tick( ) {
        if (up) {
            up = false;
            currentOption--;
            if (currentOption < 0) {
                currentOption = maxOption;
            }
        }
        if (down){
            down = false;
            currentOption ++;
            if (currentOption > maxOption) {
                currentOption = 0;
            }
        }
        if (enter) {
            Sound.music.loop();
            enter = false;
            if (options[currentOption].equals("newGame") || options[currentOption].equals("continue")) {
                Game.gameState = "NORMAL";
                pause = false;
            }else if(options[currentOption].equals("exit")) {
                System.exit(0);
            }
        }
    }
    public void render(Graphics g) {
        g.setFont(new Font("Impact", Font.PLAIN, 40));
        g.setColor(new Color(0, 0, 0, 100));
        g.fillRect(0, 0, ((Game.WIDTH*Game.SCALE)), ((Game.HEIGHT*Game.SCALE)));

        g.setColor(Color.WHITE);

        g.setFont(new Font("Impact", Font.PLAIN, 40));
        g.drawString(">> MEU JOGUINHO <<", ((Game.WIDTH*Game.SCALE)/2) - 160 , ((Game.HEIGHT*Game.SCALE)/2) - 80 );

        g.setColor(Color.WHITE);
        g.setFont(new Font("Impact", Font.PLAIN, 20));
        if (!pause) {
            g.drawString("Novo jogo", ((Game.WIDTH * Game.SCALE) / 2) - 60, ((Game.HEIGHT * Game.SCALE) / 2) + 30);
        }else {
            g.drawString("Continuar", ((Game.WIDTH * Game.SCALE) / 2) - 60, ((Game.HEIGHT * Game.SCALE) / 2) + 30);
        }

        g.drawString("Carregar jogo", ((Game.WIDTH*Game.SCALE)/2) - 80 , ((Game.HEIGHT*Game.SCALE)/2) + 80 );
        g.drawString("Sair", ((Game.WIDTH*Game.SCALE)/2) - 40 , ((Game.HEIGHT*Game.SCALE)/2) + 130 );

        if(options[currentOption].equals("newGame")) {
            g.setColor(Color.green);
            g.drawString(">", ((Game.WIDTH*Game.SCALE)/2) - 80 , ((Game.HEIGHT*Game.SCALE)/2) + 30 );
        }else if( options[currentOption].equals("loadGame")) {

            g.setColor(Color.green);
            g.drawString(">", ((Game.WIDTH*Game.SCALE)/2) - 110 , ((Game.HEIGHT*Game.SCALE)/2) + 80 );
        }else  {
            g.setColor(Color.green);
            g.drawString(">", ((Game.WIDTH*Game.SCALE)/2) - 80 , ((Game.HEIGHT*Game.SCALE)/2) + 130 );
        }

    }
}
