package com.beeslmane;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class TextTwistBot
{
    public static final Color gameBorderColor = new Color(194, 255, 0);
    public static boolean stopped = false;

    private static String getValidFile(String initial)
    {
        if (Paths.get(initial).toFile().exists()) return initial;

        System.out.format("Invalid filename '%s'!\nPlease input valid filename: ", initial);
        return TextTwistBot.getValidFile(new Scanner(System.in).nextLine());
    }

    public static List<String> permutations(String word)
    {
        List<Character> icall_list = new ArrayList<>();
        for (char c : word.toCharArray()) icall_list.add(c);
        List<List<Character>> psets = new ArrayList<>();
        Set<String> result = new HashSet<>();
        gpc_icall(psets, icall_list);

        for (List<Character> clist : psets)
        {
            Character []charr = clist.toArray(new Character[clist.size()]);
            char []carr = new char[charr.length];

            for (int i = 0; i < charr.length; i++)
                carr[i] = charr[i];

            Arrays.sort(carr);
            result.add(new String(carr));
        }

        return new ArrayList<>(result);
    }

    private static void gpc_icall(List<List<Character>> rvec, List<Character> chars)
    {
        if (chars.size() <= 2) return;
        rvec.add(chars);

        for (int i = 0; i < chars.size(); i++)
        {
            List<Character> temp = new ArrayList<>(chars);
            temp.remove(i);
            gpc_icall(rvec, temp);
        }
    }

    private static TextTwister.Point findGame(Robot robot)
    {
        GraphicsDevice screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int screenHeight = screen.getDisplayMode().getHeight();
        int screenWidth = screen.getDisplayMode().getWidth();

        BufferedImage screenImage = robot.createScreenCapture(new Rectangle(0, 0, screenWidth, screenHeight));

        for (int y = 0; y < screenHeight; y++)
            for (int x = 0; x < screenWidth; x++)
                if (new Color(screenImage.getRGB(x, y)).equals(gameBorderColor))
                    return new TextTwister.Point(x - 2, y - 2);

        return null;
    }

    public static void main(String ...args)
    {
        Robot robot = Robot.spawn();
        TextTwister.Point gameLoc = TextTwistBot.findGame(robot);

        if (gameLoc == null)
        {
            System.err.println("Could not find game window!");
            System.exit(1);
        }

        new TextTwister(gameLoc, robot, new DictionaryHandler(TextTwistBot.getValidFile("dictionary.txt"))).playGame();
    }
}
