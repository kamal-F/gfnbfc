import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

import weka.core.*;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.classifiers.Classifier;
//import weka.classifiers.bayes.*;

/**
 * Write a description of class Musuh here.
 * 
 * @author kamal 
 * @version 1 20200507
 */
public class Musuh extends Actor
{
    int health = 10;
    private int vSpeed = 0;
    private int wSpeed = 6;
    
    private boolean right = true;
    private int fDelay = 0;
    
    private boolean a = false;
    private boolean d = false;
    private boolean s = false;
    private boolean space = false;
    
    private FastVector attributes = new FastVector();
    private Instances dataset;
    private Classifier classifier;

    private Instance inst;
    private int predAction = 0;
    private int playerBotX;
    private int playerBotY;

    private int meX;
    private int meY;

    private Attribute x;
    private Attribute y;
    private Attribute target_x;
    private Attribute target_y;

    public Musuh(){
        init();
    }

    /**
     * Act - do whatever the Musuh wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {

        World world = getWorld();
        List<PlayerBot> actor = world.getObjects(PlayerBot.class);
        if(actor!=null){
            for(PlayerBot a:actor){                
                playerBotX = a.getX();
                playerBotY = a.getY();
            }
        }

        playerBotX = playerBotX /100 +1;
        playerBotY = playerBotY / 100 +1;
        meX = getX() / 100 +1;
        meY = getY() /100 +1;

        predAction = classify(meX,meY,playerBotX,playerBotY);
        if(predAction == 0)a= true;
        if(predAction == 1)d= true;
        if(predAction == 2)space= true;

        dodge();
        shoot(PlayerBot.class);
        ai();

        Projectile laser = (Projectile)getOneIntersectingObject(Projectile.class);
        if(laser!=null&&laser.players){
            health--;
            getWorld().removeObject(laser);
        }
        if(health==0){
            getWorld().removeObject(this);
        }

        //showInfo();

        reset();
    }

    public void ai(){
        //cek sentuh tanah/ di atas tanah, memastikan seluruh gambar tercover -getimage + getimage
        if((getOneObjectAtOffset(0, getImage().getHeight()/2+16, Ground.class)==null
            && getOneObjectAtOffset(getImage().getWidth()/2, getImage().getHeight()/2+16, Ground.class)==null
            && getOneObjectAtOffset(-getImage().getWidth()/2, getImage().getHeight()/2+16, Ground.class)==null)
        || vSpeed<0){
            setLocation(getX(), getY()+vSpeed);
            vSpeed++;
        }else{
            vSpeed=0;
        }

        //cek sentuh tanah
        if(getOneObjectAtOffset(0, getImage().getHeight()/2+15, Ground.class)!=null && vSpeed==0){
            setLocation(getX(), getY()-1);
        }

        //cek mentok ground posisi x
        if(a && getOneObjectAtOffset(-getImage().getWidth(), 0, Ground.class)==null){
            setLocation(getX()-wSpeed, getY());
            if(right){
                getImage().mirrorHorizontally();
                right = false;
            }            
        }

        //cek mentok ground posisi x
        if(d && getOneObjectAtOffset(getImage().getWidth(), 0, Ground.class)==null){
            setLocation(getX()+wSpeed, getY());
            if(!right){
                getImage().mirrorHorizontally();
                right = true;
            }
        }

        //loncat true jika di atas tanah, vspeed =0 dan space true
        if(getOneObjectAtOffset(0, getImage().getHeight()/2+16, Ground.class)!=null && vSpeed==0 && space){
            setLocation(getX(), getY()-1);
            vSpeed = -20;
        }

        //tembak true
        if(s && fDelay==0){
            if(right){
                Projectile p = new Projectile(false);
                getWorld().addObject(p, getX()+getImage().getWidth()/2+5, getY()-31);
            }else{
                Projectile p = new Projectile(false);
                getWorld().addObject(p, getX()-getImage().getWidth()/2-6, getY()-31);
                p.setRotation(180);
            }
            fDelay=40;
        }else if(fDelay>0){
            fDelay--;
        }
    }

    private boolean atWorldEdge()
    {
        if(getX() < 20 || getX() > getWorld().getWidth() - 20)
            return true;
        if(getY() < 20 || getY() > getWorld().getHeight() - 20)
            return true;
        else
            return false;
    }
    
    //forward chaining rules: 1
    public void shoot(Class clss)
    {
        World world = getWorld();
        List<Actor> actor = world.getObjects(clss);
        if(actor!=null){
            for(Actor a:actor){
                if(a.getY()<=getY()+20 && a.getY()>=getY()-50)
                {
                    s = true;
                }
            }
        }else{
            s = false;
        }
    }

    //forward chaining rules: 2
    public void dodge()
    {
        World world = getWorld();
        List<Projectile> actor = getObjectsInRange(150, Projectile.class);
        if(actor!=null){
            for(Projectile a:actor){
                if(a.players){
                    space = true;
                }
            }
        }
    }

    public  void init() {
        List my_nominal_values = new ArrayList(9);
        my_nominal_values.add("1");
        my_nominal_values.add("2");
        my_nominal_values.add("3");
        my_nominal_values.add("4");
        my_nominal_values.add("5");
        my_nominal_values.add("6");
        my_nominal_values.add("7");
        my_nominal_values.add("8");
        my_nominal_values.add("9");

        List my_nominal_values2 = new ArrayList(6);
        my_nominal_values2.add("1");
        my_nominal_values2.add("2");
        my_nominal_values2.add("3");
        my_nominal_values2.add("4");
        my_nominal_values2.add("5");
        my_nominal_values2.add("6");

        x = new Attribute("x",my_nominal_values);
        y = new Attribute("y",my_nominal_values2);
        target_x = new Attribute("target_x",my_nominal_values);
        target_y = new Attribute("target_y",my_nominal_values2);

        FastVector labels = new FastVector();
        labels.addElement("a");
        labels.addElement("d");
        labels.addElement("space");

        Attribute cls = new Attribute("class", labels);

        attributes.addElement(x);
        attributes.addElement(y);
        attributes.addElement(target_x);
        attributes.addElement(target_y);
        attributes.addElement(cls);

        dataset = new Instances("TestInstances", attributes, 0);
        dataset.setClassIndex(dataset.numAttributes() - 1);
        try {
            classifier = (Classifier) SerializationHelper.read("./model/model_nb_map.model");
        } catch (Exception e) {
            e.printStackTrace();
        }

        inst = new DenseInstance(1, new double[dataset.numAttributes()]);
    }

    public  int classify(int x, int y, int target_x, int target_y) {
        try {
            inst.setValue(dataset.attribute("x"), String.valueOf(x) );
            inst.setValue(dataset.attribute("y"), String.valueOf(y) );
            inst.setValue(dataset.attribute("target_x"), String.valueOf(target_x) );
            inst.setValue(dataset.attribute("target_y"), String.valueOf(target_y) );

            inst.setDataset(dataset);
            try {
                double predictedClass = classifier.classifyInstance(inst);
                return (int)predictedClass;                
            } catch (java.lang.Exception ex) {
                ex.printStackTrace();
            }
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
        }

        return 0;
    }

    private void reset(){
        a = false;
        s = false;
        d = false;
        space = false;
    }

    private void showInfo(){
        getWorld().showText("predictedClass= " + predAction, 80, 50);
        getWorld().showText("a=" + a, 80, 100);
        getWorld().showText("s=" + s, 80, 120);
        getWorld().showText("d=" + d, 80, 140);
        getWorld().showText("space=" + space, 80, 160);
        getWorld().showText("playerBotX=" + playerBotX, 80, 180);
        getWorld().showText("playerBotY=" + playerBotY, 80, 200);
        getWorld().showText("meX=" + meX, 80, 220);
        getWorld().showText("meY=" + meY, 80, 240);        
        getWorld().showText("health= " + health, 80, 260);
    }
}
