import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import greenfoot.Color.*;
/**
 * Write a description of class Projectile here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Projectile extends Actor
{
    boolean players;
    public Projectile(boolean Players) 
    {
        players = Players;
        GreenfootImage img = new GreenfootImage(10, 5);
        if(players){
            img.setColor(new Color(0, 255, 0));
            img.fill();
            setImage(img);
        }else{
            img.setColor(new Color(255, 0, 0));
            img.fill();
            setImage(img);
        }
    }
    
    public boolean atWorldEdge()
    {
        if(getX() < 2 || getX() > getWorld().getWidth() - 2)
            return true;
        if(getY() < 2 || getY() > getWorld().getHeight() - 2)
            return true;
        else
            return false;
    }
    
    public void act()
    {
        move(8);
        if(atWorldEdge()){
            getWorld().removeObject(this);
        }
    }
}
