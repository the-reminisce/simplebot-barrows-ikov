package me.remie.ikov.barrows.tasks.impl;

import me.remie.ikov.barrows.BarrowsScript;
import me.remie.ikov.barrows.tasks.BarrowsTask;
import me.remie.ikov.barrows.types.BarrowsHill;
import me.remie.ikov.barrows.types.OffensivePrayer;
import simple.api.filters.SimplePrayers;
import simple.api.wrappers.SimpleNpc;

/**
 * Created by Reminisce on Feb 14, 2023 at 1:54 AM
 *
 * @author Seth Davis <sethdavis321@gmail.com>
 * @Discord Reminisce#1707 <138751815847116800>
 */
public class FightBrotherTask extends BarrowsTask {

    private BarrowsHill hill;
    private SimpleNpc brother;

    public FightBrotherTask(final BarrowsScript script) {
        super(script);
    }

    /**
     * This method activates the task if we have a hint arrow and the brother is not dead.
     * It will also set the hill and brother variables.
     * If the brother is dead, we will clean up the state and return false.
     *
     * @return
     */
    @Override
    public boolean activate() {
        brother = ctx.game.getHintArrowNpc();
        if (brother == null) {
            return false;
        }
        hill = BarrowsHill.getHillByNpcName(brother.getName());
        if (hill == null) {
            return false;
        }
        if (brother.isDead()) {
            cleanupDeadBrother();
            return false;
        }
        return true;
    }


    /**
     * This method will attack the brother if we are not already attacking it.
     * It will also handle the protection prayer for the brother we are fighting.
     * If we have the curses prayer book enabled, we will use the deflect prayer
     * If the brother is dead, we will clean up the state.
     * If the brother is not dead, we will set the offensive prayer.
     */
    @Override
    public void execute() {
        if (brother.isDead()) {
            cleanupDeadBrother();
            return;
        }
        handleProtectionPrayer();
        if (ctx.players.getLocal().getInteracting() == null || !ctx.players.getLocal().getInteracting().equals(brother)) {
            brother.interact("Attack");
        }
        final OffensivePrayer prayer = hill.isMelee() ? getSettings().getPrimaryOffensivePrayer() : getSettings().getSecondaryOffensivePrayer();
        if (prayer != OffensivePrayer.NONE) {
            ctx.prayers.prayer(prayer.getPrayer(), true, true);
        }
    }

    /**
     * This method handles the protection prayer for the brother we are fighting.
     * If we have the curses prayer book enabled, we will use the deflect prayer
     */
    private void handleProtectionPrayer() {
        SimplePrayers.Prayers prayer = hill.getPrayer();
        if (getState().isCursesPrayerEnabled()) {
            switch (prayer) {
                case PROTECT_FROM_MELEE:
                    prayer = SimplePrayers.Prayers.DEFLECT_MELEE;
                    break;
                case PROTECT_FROM_MISSILES:
                    prayer = SimplePrayers.Prayers.DEFLECT_MISSILES;
                    break;
                case PROTECT_FROM_MAGIC:
                    prayer = SimplePrayers.Prayers.DEFLECT_MAGIC;
                    break;
            }
        }
        ctx.prayers.prayer(prayer, true, true);
    }

    /**
     * This method cleans up the state of the brother we just killed.
     * It will disable all prayers and set the brother as dead.
     */
    private void cleanupDeadBrother() {
        ctx.prayers.disableAll();
        getState().setBrotherDead(brother.getName());
    }

    @Override
    public String status() {
        return "Fighting " + brother.getName();
    }

}
