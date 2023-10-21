package com.company;


import javax.swing.*;
        import java.awt.*;
        import java.awt.event.ActionEvent;
        import java.awt.event.ActionListener;
        import java.awt.event.KeyEvent;
        import java.awt.event.KeyListener;
        import java.util.LinkedList;
        import java.util.Random;

public class SnakeGame extends JFrame implements ActionListener, KeyListener {

    private static final int TILE_SIZE = 20;
    private static final int GRID_SIZE = 20;
    private static final int GAME_SPEED = 150;

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private LinkedList<Point> snake;
    private Point food;
    private Direction direction;
    private Timer timer;
    private boolean paused;
    private boolean gameOver;

    public SnakeGame() {
        setTitle("Snake Game");
        setSize(TILE_SIZE * GRID_SIZE, TILE_SIZE * GRID_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        snake = new LinkedList<>();
        direction = Direction.RIGHT;
        paused = false;
        gameOver = false;

        // Initial snake position
        snake.add(new Point(5, 5));

        // Initial food position
        generateFood();

        timer = new Timer(GAME_SPEED, this);
        timer.start();

        addKeyListener(this);
        setFocusable(true);
    }

    private void generateFood() {
        Random random = new Random();
        int x = random.nextInt(GRID_SIZE);
        int y = random.nextInt(GRID_SIZE);

        food = new Point(x, y);

        // Make sure food is not generated on the snake
        while (snake.contains(food)) {
            x = random.nextInt(GRID_SIZE);
            y = random.nextInt(GRID_SIZE);
            food = new Point(x, y);
        }
    }

    private void move() {
        if (!paused && !gameOver) {
            Point head = snake.getFirst();

            switch (direction) {
                case UP:
                    head = new Point(head.x, (head.y - 1 + GRID_SIZE) % GRID_SIZE);
                    break;
                case DOWN:
                    head = new Point(head.x, (head.y + 1) % GRID_SIZE);
                    break;
                case LEFT:
                    head = new Point((head.x - 1 + GRID_SIZE) % GRID_SIZE, head.y);
                    break;
                case RIGHT:
                    head = new Point((head.x + 1) % GRID_SIZE, head.y);
                    break;
            }

            if (snake.contains(head) || head.equals(food)) {
                if (head.equals(food)) {
                    snake.addFirst(food);
                    generateFood();
                } else {
                    gameOver = true;
                    timer.stop();
                    JOptionPane.showMessageDialog(this, "Game Over! Your score: " + (snake.size() - 1),
                            "Game Over", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                snake.addFirst(head);
                snake.removeLast();
            }
        }

        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (!gameOver) {
            // Draw snake
            g.setColor(Color.GREEN);
            for (Point point : snake) {
                g.fillRect(point.x * TILE_SIZE, point.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }

            // Draw food
            g.setColor(Color.RED);
            g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        } else {
            // Game over message
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Game Over! Your score: " + (snake.size() - 1), 100, getHeight() / 2 - 10);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            paused = !paused;
        } else if (e.getKeyCode() == KeyEvent.VK_UP && direction != Direction.DOWN) {
            direction = Direction.UP;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && direction != Direction.UP) {
            direction = Direction.DOWN;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && direction != Direction.RIGHT) {
            direction = Direction.LEFT;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && direction != Direction.LEFT) {
            direction = Direction.RIGHT;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not used
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SnakeGame().setVisible(true));
    }
}
