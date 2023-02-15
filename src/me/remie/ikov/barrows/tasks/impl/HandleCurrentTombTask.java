package me.remie.ikov.barrows.tasks.impl;

import me.remie.ikov.barrows.BarrowsScript;
import me.remie.ikov.barrows.tasks.BarrowsTask;
import me.remie.ikov.barrows.types.BarrowsHill;
import simple.api.SimpleGame;
import simple.api.wrappers.SimpleSceneObject;

/**
 * Created by Reminisce on Feb 14, 2023 at 12:31 AM
 *
 * @author Seth Davis <sethdavis321@gmail.com>
 * @Discord Reminisce#1707 <138751815847116800>
 */
public class HandleCurrentTombTask extends BarrowsTask {

    /**
     * Handles current crypt that we are in
     * @param script
     */
    public HandleCurrentTombTask(final BarrowsScript script) {
        super(script);
    }

    /**
     * Checks if we are in a tomb and if we are, we handle it accordingly (climb stairs, kill brother, etc)
     * @return true if we are in a tomb
     */
    @Override
    public boolean activate() {
        return getState().getCurrentTomb() != null;
    }

    /**
     * Handles the current tomb we are in (climb stairs, kill brother, etc).
     * If we are in the boss tomb, we check if we have killed all 5 brothers, if we have, we click the option to open the tomb.
     * If we are in a brother tomb, we check if we have killed the brother, if we have, we climb the stairs.
     * If we are in a brother tomb, we check if we have not killed the brother, if we have not, we interact with the tomb.
     */
    @Override
    public void execute() {
        final BarrowsHill currentTomb = getState().getCurrentTomb();
        final BarrowsHill bossBrother = getState().getBossBrother();
        if (bossBrother == currentTomb) {
            if (getState().getBrothersKilled() != 5) {
                climbStairs();
                return;
            } else {
                if (ctx.widgets.getBackDialogId() == 2459) {
                    ctx.dialogue.clickDialogueOption(1);
                    ctx.onCondition(() -> getState().inChestRoom(), 250, 10);
                    return;
                }
            }
        }

        if (getState().isBrotherKilled(currentTomb)) {
            climbStairs();
            return;
        }

        setStatus("Interacting with tomb");
        final SimpleSceneObject tomb = ctx.objects.populate().filter(currentTomb.getObjectId())
                .filterWithin(currentTomb.getCoffinTile(), 5).nextNearest();
        if (tomb != null) {
            tomb.interact(502);
            if (ctx.onCondition(() -> ctx.widgets.getBackDialogId() == 2459 ||
                    ctx.game.hintType() == SimpleGame.HintType.NPC, 250, 10)) {
                if (ctx.widgets.getBackDialogId() == 2459) {
                    getState().setBossBrother(currentTomb);
                }
            }
        }
    }

    /**
     * Climbs the stairs to go back to the surface
     */
    private void climbStairs() {
        setStatus("Climbing stairs");
        final SimpleSceneObject stairs = ctx.objects.populate().filter("Staircase").nextNearest();
        if (stairs != null) {
            stairs.interact(502);
            ctx.onCondition(() -> !getState().isInsideTomb(), 250, 10);
        }
    }

    @Override
    public String status() {
        return "Handling current tomb...";
    }

}
