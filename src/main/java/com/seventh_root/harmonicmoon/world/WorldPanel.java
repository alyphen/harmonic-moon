package com.seventh_root.harmonicmoon.world;

import com.seventh_root.harmonicmoon.HarmonicMoon;
import com.seventh_root.harmonicmoon.player.Camera;
import com.seventh_root.harmonicmoon.player.Player;
import com.seventh_root.harmonicmoon.player.PlayerController;

import javax.swing.*;
import java.awt.*;

public class WorldPanel extends JPanel {

    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    private HarmonicMoon harmonicMoon;

    private boolean active;

    private World world;

    public WorldPanel(HarmonicMoon harmonicMoon, String map) {
        this.harmonicMoon = harmonicMoon;
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setDoubleBuffered(true);
        try {
            world = World.load(harmonicMoon, map);
        } catch (MalformedWorldSaveException exception) {
            exception.printStackTrace();
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public World getWorld() {
        return world;
    }

    public PlayerController getPlayerController() {
        return harmonicMoon.getPlayerController();
    }

    public Player getPlayer() {
        return harmonicMoon.getPlayer();
    }

    public Camera getCamera() {
        return harmonicMoon.getCamera();
    }

    public void reset() {
    }

    public void onTick() {
        if (active) {
            world.onTick();
            getPlayerController().onTick();
            harmonicMoon.getMessageBox().onTick();
            if (getPlayer().getCharacter() != null) getCamera().onTick();
        }
    }

    public void render(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.translate(-getCamera().getLocation().getX(), -getCamera().getLocation().getY());
        world.render(graphics);
        harmonicMoon.getParticleManager().render(graphics);
        graphics2D.translate(getCamera().getLocation().getX(), getCamera().getLocation().getY());
        harmonicMoon.getMessageBox().render(graphics);
        harmonicMoon.getParticleManager().renderHUD(graphics);
    }

    @Override
    public void paintComponent(Graphics graphics) {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, getWidth(), getHeight());
        render(graphics);
    }

    public HarmonicMoon getHarmonicMoon() {
        return harmonicMoon;
    }
}