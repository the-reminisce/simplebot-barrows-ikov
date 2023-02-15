package me.remie.ikov.barrows.tasks;

import me.remie.ikov.barrows.BarrowsScript;
import me.remie.ikov.barrows.data.BarrowsSettings;
import me.remie.ikov.barrows.data.BarrowsState;
import simple.api.script.task.Task;

/**
 * Created by Reminisce on Feb 14, 2023 at 12:03 AM
 *
 * @author Seth Davis <sethdavis321@gmail.com>
 * @Discord Reminisce#1707 <138751815847116800>
 */
public abstract class BarrowsTask extends Task {

    public final BarrowsScript script;

    public BarrowsTask(BarrowsScript script) {
        super(script.ctx);
        this.script = script;
    }

    /**
     * Gets the script instance.
     *
     * @return the script instance.
     */
    public BarrowsScript getScript() {
        return script;
    }

    /**
     * Gets the settings instance. This holds all the settings for the script.
     *
     * @return the settings instance.
     */
    public BarrowsSettings getSettings() {
        return script.getSettings();
    }

    /**
     * Gets the state instance. This holds all the state specific variables for the script.
     *
     * @return the state instance.
     */
    public BarrowsState getState() {
        return script.getState();
    }

    /**
     * Sets the status of the script. This is a helper since it's not exposed in the task class.
     *
     * @param status the status to set.
     */
    public void setStatus(String status) {
        script.setScriptStatus(status);
    }

}
