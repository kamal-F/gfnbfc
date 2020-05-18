import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class PlayerBot here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PlayerBot extends Actor
{
    int vSpeed = 0;
    boolean right = true;
    int fDelay = 0;
    int health = 10;
    public void act() 
    {
        if((getOneObjectAtOffset(0, getImage().getHeight()/2, Ground.class)==null
        && getOneObjectAtOffset(getImage().getWidth()/2, getImage().getHeight()/2, Ground.class)==null
        && getOneObjectAtOffset(-getImage().getWidth()/2, getImage().getHeight()/2, Ground.class)==null)
        || vSpeed<0){
            setLocation(getX(), getY()+vSpeed);
            vSpeed++;
        }else{
            vSpeed=0;
        }
        
        if(getOneObjectAtOffset(0, getImage().getHeight()/2-1, Ground.class)!=null&&vSpeed==0){
            setLocation(getX(), getY()-1);
        }
       
        if(Greenfoot.isKeyDown("a")&&getOneObjectAtOffset(-getImage().getWidth(), 0, Ground.class)==null){            
            setLocation(getX()-5, getY());
            if(right){
                getImage().mirrorHorizontally();
                right = false;
            }
        }else if(Greenfoot.isKeyDown("d")&&getOneObjectAtOffset(getImage().getWidth(), 0, Ground.class)==null){           
            setLocation(getX()+5, getY());
            if(!right){
                getImage().mirrorHorizontally();
                right = true;
            }
        }
        if(getOneObjectAtOffset(0, getImage().getHeight()/2, Ground.class)!=null
        && vSpeed==0
        && Greenfoot.isKeyDown("space")){
            setLocation(getX(), getY()-1);
            vSpeed = -20;
        }
        if(Greenfoot.isKeyDown("s")&&fDelay==0){
            if(right){
                Projectile p = new Projectile(true);
                getWorld().addObject(p, getX()+getImage().getWidth()/2+5, getY());
            }else{
                Projectile p = new Projectile(true);
                getWorld().addObject(p, getX()-getImage().getWidth()/2-6, getY());
                p.setRotation(180);
            }
            fDelay=40;
        }else if(fDelay>0){
            fDelay--;
        }
        Projectile laser = (Projectile)getOneIntersectingObject(Projectile.class);
        if(laser!=null&&!laser.players){
            health--;
            getWorld().removeObject(laser);
        }
        if(health==0){            
            getWorld().removeObject(this);
        }
    }    
}
