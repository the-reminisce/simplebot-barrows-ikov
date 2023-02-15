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
public class EatFoodTask extends BarrowsTask {

    public EatFoodTask(BarrowsScript script) {
        super(script);
    }

    /**
     * Checks if we need to eat food (if we are below the threshold).
     *
     * @return true if we need to eat food
     */
    @Override
    public boolean activate() {
        return ctx.combat.health() < getSettings().getEatHealth();
    }

    /**
     * Eats food, if we have some in our inventory.
     * If we do not have food, we teleport home.
     */
    @Override
    public void execute() {
        final SimpleItem food = ctx.inventory.populate().filter(getSettings().getFoodType().getName()).next();
        if (food != null) {
            final int health = ctx.combat.health();
            food.interact("Eat");
            ctx.onCondition(() -> ctx.combat.health() > health, 250, 10);
        } else {
            setStatus("Teleporting, out of food");
            ctx.magic.castHomeTeleport();
        }
    }

    @Override
    public String status() {
        return "Eating " + getSettings().getFoodType().getName() + "...";
    }

}
