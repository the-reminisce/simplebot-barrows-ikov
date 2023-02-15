package me.remie.ikov.barrows.tasks.impl;

import me.remie.ikov.barrows.BarrowsScript;
import me.remie.ikov.barrows.tasks.BarrowsTask;

public class TeleportHomeTask extends BarrowsTask {

    public TeleportHomeTask(BarrowsScript script) {
        super(script);
    }

    /**
     * If we're teleporting home, and we're not at home, then we should teleport home.
     *
     * @return true if we should teleport home, false otherwise.
     */
    @Override
    public boolean activate() {
        return getState().isTeleportingHome() && !getState().atHome();
    }

    /**
     * Cast the home teleport spell. This will teleport us to the home location.
     */
    @Override
    public void execute() {
        ctx.magic.castHomeTeleport();
    }

    @Override
    public String status() {
        return "Teleporting home...";
    }
}
