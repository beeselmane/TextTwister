package com.beeslmane;

import java.awt.AWTException;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class Robot extends java.awt.Robot
{
    public void pushKey(int keycode)
    { this.keyPress(keycode); }

    public void releaseKey(int keycode)
    { this.keyRelease(keycode); }

    public void pressKey(int keycode)
    {
        this.pushKey(keycode);
        this.releaseKey(keycode);
    }

    public void typeString(String text)
    {
        for (char character : text.toCharArray())
        {
            if (Character.isAlphabetic(character)) {
                boolean uppercase = (character & 0x20) != 0x20;
                if (uppercase) this.pushKey(KeyEvent.VK_SHIFT);
                this.pressKey(character & ~0x20);
                if (uppercase) this.releaseKey(KeyEvent.VK_SHIFT);
            } else if (character == '@') {
                this.pushKey(KeyEvent.VK_SHIFT);
                this.pressKey(KeyEvent.VK_2);
                this.releaseKey(KeyEvent.VK_SHIFT);
            } else if (character == ':') {
                this.pushKey(KeyEvent.VK_SHIFT);
                this.pressKey(KeyEvent.VK_SEMICOLON);
                this.releaseKey(KeyEvent.VK_SHIFT);
            } else if (character == '^') {
                this.pushKey(KeyEvent.VK_SHIFT);
                this.pressKey(KeyEvent.VK_6);
                this.releaseKey(KeyEvent.VK_SHIFT);
            } else if (character == '$') {
                this.pushKey(KeyEvent.VK_SHIFT);
                this.pressKey(KeyEvent.VK_4);
                this.releaseKey(KeyEvent.VK_SHIFT);
            } else {
                this.pressKey(character);
            }
        }
    }

    public void wait(double seconds)
    {
        this.delay((int)(seconds * 1000.0F));
    }

    public void clickAt(int x, int y)
    {
        this.mouseMove(x, y);
        this.mousePress(InputEvent.BUTTON1_MASK);
        this.mouseRelease(InputEvent.BUTTON1_MASK);
    }

    // java.awt.Robot constructor throws AWTException
    // the factory method spawn() allows for creation
    // of this Robot class extension without worrying
    // about the exception thrown

    public static Robot spawn()
    {
        try {
            return new Robot();
        } catch (AWTException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Robot() throws AWTException
    { super(); }
}
