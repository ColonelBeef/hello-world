package com.beef.pong.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.beef.pong.Entities.Ball;
import com.beef.pong.Entities.Bot;
import com.beef.pong.Entities.Player;

public class Game extends JPanel implements Runnable {
	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 300;
	public static final int HEIGHT = WIDTH / 16 * 9;
	public static final int SCALE = 3;

	private static final String NAME = "PONG";

	private JFrame frame;
	public static InputHandler input;
	public static Player player;
	public static Ball ball;
	public static Bot bot;

	boolean isRunning = false;

	public Game() {
		setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

		frame = new JFrame(NAME);
		frame.add(this);

		init();
		addKeyListener(input);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void init() {
		input = new InputHandler();
		player = new Player();
		ball = new Ball(4);
		bot = new Bot();
	}

	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		double ns = 1000000000D / 60;
		double delta = 0;

		int ticks = 0;
		int frames = 0;

		requestFocus();

		while (isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;

			while (delta >= 1) {
				tick();
				ticks++;
				delta--;
			}
			repaint();
			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				frame.setTitle(NAME + "     |     " + ticks + " ticks, " + frames + " fps");
				ticks = 0;
				frames = 0;
			}
		}
	}

	public void tick() {
		input.tick();
		player.tick();
		bot.tick();
		ball.tick();
	}

	public void paint(Graphics g) {
		super.paint(g);

		g.setColor(new Color(0xD3E053));
		g.fillRect(0, 0, getWidth(), getHeight());

		g.setColor(new Color(0x5681D1));
		g.fillRect(player.x, player.y, player.width, player.height);
		g.fillRect(bot.x, bot.y, bot.width, bot.height);
		
		g.setColor(new Color(0x50CC64));
		g.fillRect(ball.x, ball.y, 20, 20);

		g.dispose();
	}

	public synchronized void start() {
		new Thread(this, "Game").start();
		isRunning = true;
	}

	public synchronized void stop() {
		isRunning = false;
	}

	public static void main(String[] args) {
		new Game().start();
	}
}
