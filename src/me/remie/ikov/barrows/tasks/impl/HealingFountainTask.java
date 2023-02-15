package me.remie.ikov.barrows.tasks.impl;

import me.remie.ikov.barrows.BarrowsScript;
import me.remie.ikov.barrows.tasks.BarrowsTask;
import simple.api.wrappers.SimpleSceneObject;

/**
 * Created by Reminisce on Feb 13, 2023 at 10:29 PM
 *
 * @author Seth Davis <sethdavis321@gmail.com>
 * @Discord Reminisce#1707 <138751815847116800>
 */
public class HealingFountainTask extends BarrowsTask {

    public HealingFountainTask(final BarrowsScript script) {
        super(script);
    }

    /**
     * If we are at home, and our health or prayer is not 100%, then we should drink from the fountain.
     *
     * @return true if we should drink from the fountain.
     */
    @Override
    public boolean activate() {
        return getState().atHome() && (ctx.combat.healthPercent() != 100 || ctx.prayers.prayerPercent() != 100);
    }

    /**
     * This will disable all prayers, and then drink from the fountain.
     */
    @Override
    public void execute() {
        ctx.prayers.disableAll(true);
        final SimpleSceneObject object = ctx.objects.populate().filter("Healing font").nextNearest();
        if (object != null) {
            object.interact(502);
            ctx.onCondition(() -> ctx.combat.healthPercent() == 100 && ctx.prayers.prayerPercent() == 100, 250, 10);
        }
    }

    @Override
    public String status() {
        return "Drinking from the fountain...";
    }

}
