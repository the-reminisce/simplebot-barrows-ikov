package me.remie.ikov.barrows.tasks.impl;

import me.remie.ikov.barrows.BarrowsScript;
import me.remie.ikov.barrows.tasks.BarrowsTask;
import simple.api.wrappers.SimpleItem;

/**
 * Created by Reminisce on Feb 12, 2023 at 7:26 PM
 *
 * @author Seth Davis <sethdavis321@gmail.com>
 * @Discord Reminisce#1707 <138751815847116800>
 */
public class RestorePrayerTask extends BarrowsTask {

    private final String[] PRAYER_POTION_PARTIAL_NAMES = new String[]{
            "Prayer potion", "Prayer flask", "Super restore", "Sanfew"
    };

    public RestorePrayerTask(BarrowsScript script) {
        super(script);
    }

    /**
     * Checks if we need to restore prayer points (if we are below the threshold).
     *
     * @return true if we need to restore prayer points
     */
    @Override
    public boolean activate() {
        return ctx.prayers.points() < getSettings().getDrinkPrayer();
    }

    /**
     * Drinks a prayer potion, if we have one in our inventory.
     * If we do not have a prayer potion, we teleport home.
     */
    @Override
    public void execute() {
        final SimpleItem potion = ctx.inventory.populate().filterContains(PRAYER_POTION_PARTIAL_NAMES).next();
        if (potion != null) {
            final int points = ctx.prayers.points();
            potion.interact("Drink");
            ctx.onCondition(() -> ctx.prayers.points() > points, 250, 10);
        } else {
            setStatus("Teleporting, out of prayer pots");
            ctx.magic.castHomeTeleport();
        }
    }

    @Override
    public String status() {
        return "Restoring prayer...";
    }
}
