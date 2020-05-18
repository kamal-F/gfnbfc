import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import greenfoot.Color.*;
/**
 * Write a description of class Ground here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Ground extends Actor
{
    public Ground(int width, int height)
    {
        GreenfootImage img = new GreenfootImage(width, height);
        img.setColor(new Color(0, 0, 0));
        img.fill();
        setImage(img);
    }
}
