package com.beeslmane;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;
import java.awt.*;

public class TextTwister
{
    private Point console = new Point(1125, 825);
    private double wait_time = 0.5F;
    private int ssid = 0;

    private Point nextButtonTestLoc;
    private Point nextButton;
    private Point gameLoc;

    private DictionaryHandler dictionary;
    private String currentPuzzle;
    private Robot robot;

    // Setup ssid for this object
    {
        String []files = Paths.get("/", "Users", "developer", "home", "puzzles_jview").toFile().list();
        int maxval = 0;

        for (String name : files)
        {
            if (name.startsWith("screen") && name.endsWith(".png"))
            {
                int number = Integer.parseInt(name.substring(6, name.length() - 4));
                if (number > maxval) maxval = number;
            }
        }

        this.ssid = maxval + 1;
    }

    public TextTwister(Point gameLoc, Robot robot, DictionaryHandler dictionary)
    {
        this.dictionary = dictionary;
        this.gameLoc = gameLoc;
        this.robot = robot;

        this.nextButton = new Point(this.gameLoc.x + 265, this.gameLoc.y + 212);
        this.nextButtonTestLoc = new Point(this.gameLoc.x + 144, this.gameLoc.y + 144);
    }

    public void playGame()
    {
        this.startGame();

        do {
            this.readPuzzle();
            this.robot.clickAt(this.gameLoc.x, this.gameLoc.y);
            this.robot.wait(wait_time);
            this.solve();

            do {
                this.robot.wait(wait_time);
            } while (!this.robot.getPixelColor(this.nextButtonTestLoc.x, this.nextButtonTestLoc.y).equals(Color.WHITE));

            this.robot.clickAt(this.nextButton.x, this.nextButton.y);
            this.robot.wait(wait_time);
        } while (!TextTwistBot.stopped);
    }

    private void startGame()
    {
        BufferedImage gameImage = this.takeGameScreenShot();

        if (new Color(gameImage.getRGB(144, 118)).equals(TextTwistBot.gameBorderColor))
        {
            int x = this.gameLoc.x + 250;
            int y = this.gameLoc.y + 225;

            this.robot.clickAt(x, y);
            this.robot.wait(wait_time);
            this.robot.clickAt(x, y);
            this.robot.wait(wait_time);
        }
    }

    private void readPuzzle()
    {
        this.robot.clickAt(console.x, console.y);
        System.out.print("Puzzle String: ");
        currentPuzzle = stdio.nextLine().toLowerCase();

        switch (this.currentPuzzle)
        {
            case "c": {
                this.robot.clickAt(this.nextButton.x, this.nextButton.y);
                this.robot.wait(wait_time);
                this.readPuzzle();
            } break;
            case "d":
            case "b": {
                this.robot.clickAt(this.gameLoc.x, this.gameLoc.y);
                this.robot.wait(wait_time);
                this.robot.typeString("\b");
                this.robot.wait(wait_time);
                this.readPuzzle();
            } break;
            case "s": {
                this.saveGameScreen();
                this.robot.wait(wait_time);
                this.readPuzzle();
            } break;
            case "q": {
                System.out.println("Done.");
                System.exit(0);
            } break;
        }
    }

    private void readPuzzleOnScreen()
    {
        // Circles are 84x84
        // Offset by x starting at (x, y)
        // There are 6 circles
    }

    private void solve()
    {
        List<String> combinations = TextTwistBot.permutations(this.currentPuzzle);
        Set<String> solutions = new HashSet<>();

        for (String combo : combinations)
        {
            List<String> possible = this.dictionary.search(combo);
            if (possible == null) continue;
            solutions.addAll(possible);
        }

        System.out.format("Got %d solutions for puzzle string '%s'. ", solutions.size(), this.currentPuzzle);
        System.out.println("Solutions: " + solutions);
        this.robot.wait(wait_time);
        System.out.flush();

        for (String solution : solutions)
            this.robot.typeString(solution + "\n");
    }

    private BufferedImage takeGameScreenShot()
    {
        return robot.createScreenCapture(new Rectangle(this.gameLoc.x, this.gameLoc.y, 506, 380));
    }

    private void saveGameScreen()
    {
        try {
            String imageName = String.format("screen%d.png", this.ssid++);
            ImageIO.write(this.takeGameScreenShot(), "png", Paths.get("/", "Users", "developer", "home", "puzzles_jview", imageName).toFile());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    // Static Things
    private static final Scanner stdio = new Scanner(System.in);

    public static class Point
    {
        public int x, y;

        public Point(int x, int y)
        { this.x = x; this.y = y; }
    }
}
