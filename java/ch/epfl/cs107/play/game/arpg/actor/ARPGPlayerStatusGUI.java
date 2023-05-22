package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class ARPGPlayerStatusGUI implements Graphics {

    private final float DEPTH = 2000;

    /**
     * Displays the GUI ont the screen
     *
     * @param canvas   (Canvas): Target, not null
     * @param item    (ARPGItem): Current item the player is holding
     * @param life    (float): Health of the player
     * @param money   (int): Money of the player
     */
    protected void draw(Canvas canvas, ARPGItem item, float life, int money, float MAX_HP) {

        int moneySecondNumber = 0;
        int moneyLastNumber = 0;


        if(money != 0 && money > 99){
            moneySecondNumber = (money % (int) Math.pow(10, (int) Math.log10(money)))/10;
            moneyLastNumber = money%10;
        } else if(money != 0 && money > 9){
            moneySecondNumber = money/10;
            moneyLastNumber = money%10;
        } else if(money != 0){
            moneyLastNumber = money;
        }

        float width = canvas.getScaledWidth();
        float height = canvas.getScaledHeight();

        /**
         * Displays the gearDisplay
         */
        Vector anchor = canvas.getTransform().getOrigin().sub(new Vector(width/2, height/2));
        ImageGraphics gearDisplay = new ImageGraphics(ResourcePath.getSprite("zelda/gearDisplay"), 1.5f, 1.5f, new RegionOfInterest(0, 0, 32, 32), anchor.add(new Vector(0.15f, height - 1.7f)), 1, DEPTH);
        gearDisplay.draw(canvas);

        /**
         * Displays the current Item in the gearDisplay
         */
        if(item.spritename != null) {ImageGraphics gear = new ImageGraphics(ResourcePath.getSprite(item.spritename), 0.7f, 0.7f, new RegionOfInterest(0, 0, 16, 16), anchor.add(new Vector(0.56f, height - 1.32f)), 1, DEPTH);
        gear.draw(canvas);}

        /**
         * Displays the coinsDisplay
         */
        ImageGraphics moneyDisplay = new ImageGraphics(ResourcePath.getSprite("zelda/coinsDisplay"), 4.f, 2f, new RegionOfInterest(0, 0, 64, 32), anchor.add(new Vector(0.3f, height - 12.2f)), 1, DEPTH);
        moneyDisplay.draw(canvas);

        /**
         * Displays an empty row of MAX_HP hearts
         */
        for(int i = 0; i < MAX_HP; ++i){
                ImageGraphics hpDisplay = new ImageGraphics(ResourcePath.getSprite("zelda/heartDisplay"), 1.05f, 1.05f, new RegionOfInterest(0, 0, 16, 16), anchor.add(new Vector(1.7f + i, height - 1.43f)), 1, DEPTH - 1);
                hpDisplay.draw(canvas);
        }

        /**
         * Displays the remaining hearts of the player
         */
        if(life > 0.f) {
            for (int i = 1; i < MAX_HP + 1; ++i) {
                if (i <= life) {
                    ImageGraphics hp = new ImageGraphics(ResourcePath.getSprite("zelda/heartDisplay"), 1.05f, 1.05f, new RegionOfInterest(32, 0, 16, 16), anchor.add(new Vector(0.7f + i, height - 1.43f)), 1, DEPTH);
                    hp.draw(canvas);
                }
            }
            for (int i = 0; i < MAX_HP; ++i) {
                if(life > i && life < i + 1){
                    ImageGraphics hp = new ImageGraphics(ResourcePath.getSprite("zelda/heartDisplay"), 1.05f, 1.05f, new RegionOfInterest(16, 0, 16, 16), anchor.add(new Vector(1.7f + i, height - 1.43f)), 1, DEPTH);
                    hp.draw(canvas);
                }
            }
        }

        /**
         * Displays the money of the player
         */
        if(money > 99){
            if(money/100 > 0 && money/100 < 5){
                ImageGraphics coins = new ImageGraphics(ResourcePath.getSprite("zelda/digits"), 1.f, 1.f, new RegionOfInterest((money/100 - 1) * 16, 0, 16, 16), anchor.add(new Vector(1.7f, height - 11.6f)), 1, DEPTH);
                coins.draw(canvas);
            }
            if(money/100 > 4 && money/100 < 9){
                ImageGraphics coins = new ImageGraphics(ResourcePath.getSprite("zelda/digits"), 1.f, 1.f, new RegionOfInterest((money/100 - 5) * 16, 16, 16, 16), anchor.add(new Vector(1.7f, height - 11.6f)), 1, DEPTH);
                coins.draw(canvas);
            }
            if(money/100 == 9){
                ImageGraphics coins = new ImageGraphics(ResourcePath.getSprite("zelda/digits"), 1.f, 1.f, new RegionOfInterest(0, 32, 16, 16), anchor.add(new Vector(1.7f, height - 11.6f)), 1, DEPTH);
                coins.draw(canvas);
            }
        } else {
            ImageGraphics coins = new ImageGraphics(ResourcePath.getSprite("zelda/digits"), 1.f, 1.f, new RegionOfInterest(16, 32, 16, 16), anchor.add(new Vector(1.7f, height - 11.6f)), 1, DEPTH);
            coins.draw(canvas);
        }

        if(money > 9 && moneySecondNumber != 0){
            if(moneySecondNumber > 0 && moneySecondNumber < 5){
                ImageGraphics coins = new ImageGraphics(ResourcePath.getSprite("zelda/digits"), 1.f, 1.f, new RegionOfInterest((moneySecondNumber - 1) * 16, 0, 16, 16), anchor.add(new Vector(2.5f, height - 11.67f)), 1, DEPTH);
                coins.draw(canvas);
            }
            if(moneySecondNumber > 4 && moneySecondNumber < 9){
                ImageGraphics coins = new ImageGraphics(ResourcePath.getSprite("zelda/digits"), 1.f, 1.f, new RegionOfInterest((moneySecondNumber - 5) * 16, 16, 16, 16), anchor.add(new Vector(2.5f, height - 11.67f)), 1, DEPTH);
                coins.draw(canvas);
            }
            if(moneySecondNumber == 9){
                ImageGraphics coins = new ImageGraphics(ResourcePath.getSprite("zelda/digits"), 1.f, 1.f, new RegionOfInterest(0, 32, 16, 16), anchor.add(new Vector(2.5f, height - 11.67f)), 1, DEPTH);
                coins.draw(canvas);
            }
        } else {
            ImageGraphics coins = new ImageGraphics(ResourcePath.getSprite("zelda/digits"), 1.f, 1.f, new RegionOfInterest(16, 32, 16, 16), anchor.add(new Vector(2.5f, height - 11.67f)), 1, DEPTH);
            coins.draw(canvas);
        }

        if(money > 0 && moneyLastNumber != 0){
            if(moneyLastNumber > 0 && moneyLastNumber < 5){
                ImageGraphics coins = new ImageGraphics(ResourcePath.getSprite("zelda/digits"), 1.f, 1.f, new RegionOfInterest((moneyLastNumber - 1) * 16, 0, 16, 16), anchor.add(new Vector(3.3f, height - 11.65f)), 1, DEPTH);
                coins.draw(canvas);
            }
            if(moneyLastNumber > 4 && moneyLastNumber < 9){
                ImageGraphics coins = new ImageGraphics(ResourcePath.getSprite("zelda/digits"), 1.f, 1.f, new RegionOfInterest((moneyLastNumber - 5) * 16, 16, 16, 16), anchor.add(new Vector(3.3f, height - 11.65f)), 1, DEPTH);
                coins.draw(canvas);
            }
            if(moneyLastNumber == 9){
                ImageGraphics coins = new ImageGraphics(ResourcePath.getSprite("zelda/digits"), 1.f, 1.f, new RegionOfInterest(0, 32, 16, 16), anchor.add(new Vector(3.3f, height - 11.65f)), 1, DEPTH);
                coins.draw(canvas);
            }
        } else {
            ImageGraphics coins = new ImageGraphics(ResourcePath.getSprite("zelda/digits"), 1.f, 1.f, new RegionOfInterest(16, 32, 16, 16), anchor.add(new Vector(3.3f, height - 11.65f)), 1, DEPTH);
            coins.draw(canvas);
        }
    }

    @Override
    public void draw(Canvas canvas) {
    }
}
