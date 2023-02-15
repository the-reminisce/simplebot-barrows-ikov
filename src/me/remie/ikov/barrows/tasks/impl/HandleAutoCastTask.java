package me.remie.ikov.barrows.tasks.impl;

import me.remie.ikov.barrows.BarrowsScript;
import me.remie.ikov.barrows.tasks.BarrowsTask;
import me.remie.ikov.barrows.types.BarrowsHill;
import me.remie.ikov.barrows.types.MagicSpell;

/**
 * Created by Reminisce on Feb 14, 2023 at 2:19 AM
 *
 * @author Seth Davis <sethdavis321@gmail.com>
 * @Discord Reminisce#1707 <138751815847116800>
 */
public class HandleAutoCastTask extends BarrowsTask {

    public HandleAutoCastTask(final BarrowsScript script) {
        super(script);
    }

    /**
     * We only want to auto-cast if we have a spell selected, and we're on a melee hill.
     * @return true if we should auto-cast, false otherwise.
     */
    @Override
    public boolean activate() {
        if (getSettings().getMagicSpell() == MagicSpell.NONE) {
            return false;
        }
        BarrowsHill nextHill = getState().getNextHill();
        return nextHill != null && nextHill.isMelee() && !ctx.magic.isAutoCasting();
    }

    /**
     * Auto-cast the spell we want to use.
     */
    @Override
    public void execute() {
        ctx.menuActions.sendAction(104, 0, 0, getSettings().getMagicSpell().getSpellId());
        ctx.onCondition(() -> ctx.magic.isAutoCasting(), 250, 10);
    }

    @Override
    public String status() {
        return "Auto-casting " + getSettings().getMagicSpell().getSpellName();
    }
}
