package me.remie.ikov.barrows.tasks.impl;

import me.remie.ikov.barrows.BarrowsScript;
import me.remie.ikov.barrows.tasks.BarrowsTask;
import me.remie.ikov.barrows.types.BarrowsHill;
import simple.api.wrappers.SimpleItem;

/**
 * Created by Reminisce on Feb 14, 2023 at 12:12 AM
 *
 * @author Seth Davis <sethdavis321@gmail.com>
 * @Discord Reminisce#1707 <138751815847116800>
 */
public class TraverseNextHillTask extends BarrowsTask {

    public TraverseNextHillTask(final BarrowsScript script) {
        super(script);
    }

    /**
     * If we're not in a tomb, and we have a next hill to traverse, and we're not in the chest room, then we should traverse the next hill.
     *
     * @return true if we should traverse the next hill, false otherwise.
     */
    @Override
    public boolean activate() {
        if (getState().isInsideTomb()) {
            return false;
        }
        return getState().getNextHill() != null && !getState().inChestRoom();
    }

    /**
     * Traverse the next hill. This will step to the hill, and then dig on the hill.
     * If we're not at the hill, we'll step to the hill.
     * If we're at the hill, we'll dig on the hill.
     */
    @Override
    public void execute() {
        final BarrowsHill hill = getState().getNextHill();
        if (ctx.pathing.distanceTo(hill.getStepTile()) > 10) {
            ctx.pathing.step(hill.getStepTile());
            return;
        }

        if (!hill.getHillArea().within()) {
            ctx.pathing.step(hill.getHillArea().randomTile());
            ctx.onCondition(() -> hill.getHillArea().within(), 250, 10);
            return;
        }

        final SimpleItem spade = ctx.inventory.populate().filter("Spade").next();
        if (spade != null) {
            spade.interact(74);
            ctx.onCondition(() -> hill.getTombArea().within(), 250, 10);
        }
    }

    @Override
    public String status() {
        return "Traversing next hill...";
    }

}
