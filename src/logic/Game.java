
/**
 * GAME CLASS
 * Handles game loop.
 */

package logic;

import automata.World;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.image.BufferStrategy;

import javax.swing.JFrame;


public class Game extends Canvas implements Runnable {
    private int windowX  = 722;
    private int windowY  = 482;
    private int cellSize = 13;
    private int margin   = 2;

    private int worldSmoothing = 30;
    private int ticks = 20;

    private boolean running = false;
    private World world;
    private JFrame mainFrame;

    final int FPS = 20;
    final int SKIP_TICKS = 1000 / FPS;
    final int MAX_FRAMESKIP = 5;


    public Game() {
        this.world = new World(windowX, windowY, cellSize, margin);

        Dimension size = new Dimension(windowX, windowY);
        setPreferredSize(size);
        this.mainFrame = new JFrame();

        this.mainFrame.setResizable(false);
        this.mainFrame.setTitle("Cave Creator");
        this.mainFrame.add(this);
        this.mainFrame.pack();
        this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.mainFrame.setLocationRelativeTo(null);
        this.mainFrame.setVisible(true);
    }

    public synchronized void start() {
        this.world.build();

        this.running = true;
        Thread thread = new Thread(this, "Display");
        thread.start(); // call run()
    }

    public synchronized void stop() throws Exception {
        this.running = false;
    }

    public void run() {
        double nextTick = System.currentTimeMillis();
        int loops;

        // World building loop
        while (this.running && this.worldSmoothing > 0) {
            loops = 0;
            while (System.currentTimeMillis() > nextTick && loops < MAX_FRAMESKIP) {
                this.world.update();
                render();

                nextTick += SKIP_TICKS;
                loops++;
                this.worldSmoothing--;
            }
        }

        // Explorer loop
        while (this.running && this.ticks > 0) {
            loops = 0;
            while (System.currentTimeMillis() > nextTick && loops < MAX_FRAMESKIP) {
                update();
                render();

                nextTick += SKIP_TICKS;
                loops++;
                this.ticks--;
            }
        }
    }

    public void update() {
        
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        Graphics g = (Graphics2D) bs.getDrawGraphics();
        
        g.setColor(new Color(0x2c3e50));
        g.fillRect(0, 0, getWidth(), getHeight());
        
        this.world.render(g);

        g.dispose();
        bs.show();
    }
}