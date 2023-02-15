package me.remie.ikov.barrows.tasks.impl;

import me.remie.ikov.barrows.BarrowsScript;
import me.remie.ikov.barrows.tasks.BarrowsTask;
import me.remie.ikov.barrows.types.BarrowsHill;
import simple.api.wrappers.SimpleItem;

/**
 * Created by Reminisce on Feb 14, 2023 at 2:11 AM
 *
 * @author Seth Davis <sethdavis321@gmail.com>
 * @Discord Reminisce#1707 <138751815847116800>
 */
public class HandleEquipmentTask extends BarrowsTask {

    public HandleEquipmentTask(final BarrowsScript script) {
        super(script);
    }

    /**
     * Checks if we have the equipment we need to kill the next brother in our inventory.
     * @return true if we have the equipment we need to kill the next brother in our inventory. false otherwise.
     */
    @Override
    public boolean activate() {
        BarrowsHill nextHill = getState().getNextHill();
        return nextHill != null && !ctx.inventory.populate().filter(nextHill.isMelee() ?
                getSettings().getPrimaryEquipment() : getSettings().getSecondaryEquipment()).isEmpty();
    }

    /**
     * Equips the equipment we need to kill the next brother
     */
    @Override
    public void execute() {
        for (SimpleItem item : ctx.inventory) {
            item.interact(454);
            ctx.sleep(50);
        }
    }

    @Override
    public String status() {
        return "Equipping equipment";
    }
}
