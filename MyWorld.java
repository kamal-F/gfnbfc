import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import greenfoot.Color.*;
/**
 * Write a description of class Worldy here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MyWorld extends World
{
    /**
     * Constructor for objects of class Worldy.
     * 
     */
    public MyWorld()
    {    
        super(900, 600, 1);
        
        getBackground().setColor(new Color(100, 100, 100));
        getBackground().fill();
        
        addObject(new Ground(900, 60), 450, 600);
        addObject(new Ground(200, 60), 300, 420);
        addObject(new Ground(400, 60), 700, 300);
        
        addObject( new PlayerBot(), 450, 523);
        
        addObject(new Musuh(), 200, 507);       
    }    
}
