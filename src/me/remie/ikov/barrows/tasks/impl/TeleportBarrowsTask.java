package me.remie.ikov.barrows.tasks.impl;

import me.remie.ikov.barrows.BarrowsScript;
import me.remie.ikov.barrows.tasks.BarrowsTask;

/**
 * Created by Reminisce on Feb 14, 2023 at 12:03 AM
 *
 * @author Seth Davis <sethdavis321@gmail.com>
 * @Discord Reminisce#1707 <138751815847116800>
 */
public class TeleportBarrowsTask extends BarrowsTask {

    public TeleportBarrowsTask(BarrowsScript script) {
        super(script);
    }

    /**
     * Checks if we are at home and if we have loaded a preset
     * @return true if we are at home and have loaded a preset
     */
    @Override
    public boolean activate() {
        return getState().atHome() && getState().isPresetLoaded();
    }

    /**
     * Teleports to barrows and sets the preset loaded to false
     */
    @Override
    public void execute() {
        if (ctx.teleporter.open()) {
            ctx.teleporter.teleportStringPath("Minigames", "Barrows");
            if (ctx.onCondition(() -> getState().inBarrows(), 350, 10)) {
                getState().setPresetLoaded(false);
            }
        }
    }

    @Override
    public String status() {
        return "Teleporting to barrows...";
    }

}
