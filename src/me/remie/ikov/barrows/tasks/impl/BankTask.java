package me.remie.ikov.barrows.tasks.impl;

import me.remie.ikov.barrows.BarrowsScript;
import me.remie.ikov.barrows.tasks.BarrowsTask;
import simple.api.wrappers.SimpleSceneObject;

/**
 * Created by Reminisce on Feb 12, 2023 at 7:26 PM
 *
 * @author Seth Davis <sethdavis321@gmail.com>
 * @Discord Reminisce#1707 <138751815847116800>
 */
public class BankTask extends BarrowsTask {

    public BankTask(BarrowsScript script) {
        super(script);
    }

    /**
     * Checks if we are at home and if we have a preset loaded.
     *
     * @return true if we are at home and do not have a preset loaded. False otherwise.
     */
    @Override
    public boolean activate() {
        return getState().atHome() && !getState().isPresetLoaded();
    }

    /**
     * Handles interacting with the bank booth and loading the preset.
     */
    @Override
    public void execute() {
        if (getState().isTeleportingHome()) {
            getState().setTeleportingHome(false);
            getState().incrementTotalTrips();
        }
        if (!ctx.gearPresets.opened()) {
            final SimpleSceneObject bankBooth = ctx.objects.populate().filter("Bank booth").nextNearest();
            if (bankBooth == null) {
                return;
            }
            if (ctx.pathing.distanceTo(bankBooth) > 5) {
                ctx.pathing.step(bankBooth);
                return;
            }
            bankBooth.interact(900);
            if (ctx.onCondition(() -> !ctx.gearPresets.opened(), 250, 10)) {
                return;
            }
        }
        ctx.gearPresets.loadPreset(getSettings().getPresetId());
        ctx.onCondition(() -> getState().isPresetLoaded(), 250, 10);
    }

    @Override
    public String status() {
        return "Loading preset...";
    }

}
