package com.seventh_root.harmonicmoon.fight;

import com.seventh_root.harmonicmoon.HarmonicMoon;
import com.seventh_root.harmonicmoon.character.Character;
import com.seventh_root.harmonicmoon.enemy.Enemy;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class FightPanel extends JPanel {

    private HarmonicMoon harmonicMoon;
    private BufferedImage background;
    private FightOptionBox optionBox;

    private Fight fight;

    private boolean active;
    private boolean prepared;

    public FightPanel(HarmonicMoon harmonicMoon) {
        this.harmonicMoon = harmonicMoon;
        //harmonicMoon.getEventManager().registerListener(new FightCharacterMoveListener(harmonicMoon));
        optionBox = new FightOptionBox(harmonicMoon, this);
    }

    public void prepareFight(Fight fight) {
        if (!prepared) {
            this.fight = fight;
            this.background = fight.getArea().getBackground();
            int x = 16;
            for (Character.Fight character : fight.getCharacterParty().getMembers()) {
                character.setLocation(new FightLocation(x, 224));
                x += 144;
            }
            optionBox.setCharacter(fight.getCharacterParty().getMembers().get(0));
            x = 16;
            for (Enemy enemy : fight.getEnemyParty().getMembers()) {
                enemy.setLocation(new FightLocation(x, 96));
                x += 144;
            }
            prepared = true;
        }
    }

    public void startFight() {
        if (prepared && !active) {
            optionBox.resetOptions();
            active = true;
            harmonicMoon.getMusicPlayer().loop("/music/battle_theme.ogg");
            repaint();
        }
    }

    public void endFight() {
        prepared = false;
        active = false;
        harmonicMoon.getMusicPlayer().stopAll();
        harmonicMoon.setPanel("world");
    }

    public Fight getFight() {
        return fight;
    }

    public FightOptionBox getOptionBox() {
        return optionBox;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        if (prepared) {
            render(graphics);
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void onTick() {
        if (active) {
            fight.onTick();
            repaint();
        }
    }

    public void render(Graphics graphics) {
        graphics.drawImage(background, 0, 0, null);
        for (Character.Fight character : fight.getCharacterParty().getMembers()) {
            character.render(graphics);
        }
        for (Enemy enemy : fight.getEnemyParty().getMembers()) {
            enemy.render(graphics);
        }
        harmonicMoon.getParticleManager().render(graphics);
        optionBox.render(graphics);
    }

}