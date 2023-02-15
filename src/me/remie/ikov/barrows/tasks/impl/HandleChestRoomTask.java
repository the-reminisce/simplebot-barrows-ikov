package me.remie.ikov.barrows.tasks.impl;

import me.remie.ikov.barrows.BarrowsScript;
import me.remie.ikov.barrows.tasks.BarrowsTask;
import simple.api.SimpleGame;
import simple.api.wrappers.SimpleSceneObject;

/**
 * Created by Reminisce on Feb 14, 2023 at 2:44 AM
 *
 * @author Seth Davis <sethdavis321@gmail.com>
 * @Discord Reminisce#1707 <138751815847116800>
 */
public class HandleChestRoomTask extends BarrowsTask {

    public HandleChestRoomTask(final BarrowsScript script) {
        super(script);
    }

    /**
     * Checks if we are in the chest room and if we have killed all the brothers.
     * If we have killed all the brothers, we will return true.
     * If we have not killed all the brothers, we will check if we have a hint arrow.
     * If we have a hint arrow, we will return false.
     * @return true if we are in the chest room and have killed all the brothers or if we are in the chest room and have no hint arrow. False otherwise.
     */
    @Override
    public boolean activate() {
        if (!getState().inChestRoom()) {
            return false;
        }
        return getState().killedAllBrothers() ||
                (!getState().killedAllBrothers() && ctx.game.hintType() == SimpleGame.HintType.NONE);
    }

    /**
     * Handles interacting with the chest in the chest room.
     */
    @Override
    public void execute() {
        final SimpleSceneObject chest = ctx.objects.populate().filter(20973).nextNearest();
        if (chest != null) {
            chest.interact(502);
            ctx.onCondition(() -> ctx.game.hintType() == SimpleGame.HintType.NPC ||
                    ctx.widgets.getOpenInterfaceId() == 46745, 250, 10);
        }
    }

    @Override
    public String status() {
        return "Handling chest room";
    }

}
