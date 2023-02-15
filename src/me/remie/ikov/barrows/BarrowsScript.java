package me.remie.ikov.barrows;

import me.remie.ikov.barrows.data.BarrowsSettings;
import me.remie.ikov.barrows.data.BarrowsState;
import me.remie.ikov.barrows.helpers.PaintHelper;
import me.remie.ikov.barrows.tasks.impl.*;
import me.remie.ikov.barrows.types.BarrowsHill;
import simple.api.events.ChatMessageEvent;
import simple.api.listeners.SimpleMessageListener;
import simple.api.script.Category;
import simple.api.script.LoopingScript;
import simple.api.script.ScriptManifest;
import simple.api.script.interfaces.SimplePaintable;
import simple.api.script.task.Task;
import simple.api.script.task.TaskScript;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Reminisce on Feb 12, 2023 at 4:10 PM
 *
 * @author Seth Davis <sethdavis321@gmail.com>
 * @Discord Reminisce#1707 <138751815847116800>
 */
@ScriptManifest(author = "Reminisce", name = "RBarrows - Ikov",
        category = Category.MINIGAMES, version = "0.0.2",
        description = "Completes the barrows minigme with ease.", discord = "Reminisce#1707", servers = {"Ikov"}, vip = true)
public class BarrowsScript extends TaskScript implements LoopingScript, SimplePaintable, SimpleMessageListener, MouseListener {

    private final List<Task> tasks = new ArrayList<>();
    private long startTime;
    private boolean started = false;
    private BarrowsFrame frame;
    private BarrowsState state;
    private BarrowsSettings settings;

    private PaintHelper paintHelper;

    /**
     * This method is called when the script is started.
     *
     * @return True if the script should continue to start its process, false if it should not and re-run the onExecute method.
     */
    @Override
    public boolean onExecute() {
        this.state = new BarrowsState(ctx);
        this.startTime = System.currentTimeMillis();
        setScriptStatus("Waiting to start...");
        this.frame = new BarrowsFrame(this);
        setupPaint();

        this.tasks.addAll(Arrays.asList(
                new HealingFountainTask(this),
                new BankTask(this),
                new TeleportHomeTask(this),
                new TeleportBarrowsTask(this),
                new EatFoodTask(this),
                new RestorePrayerTask(this),
                new HandleEquipmentTask(this),
                new HandleAutoCastTask(this),
                new HandleChestRoomTask(this),
                new FightBrotherTask(this),
                new HandleCurrentTombTask(this),
                new TraverseNextHillTask(this)
        ));
        return true;
    }

    /**
     * This method is required to be implemented by the script to return the tasks that the script will process.
     *
     * @return The list of tasks that the script will process.
     */
    @Override
    public List<Task> tasks() {
        return this.tasks;
    }

    /**
     * This method determines if the script should prioritize tasks.
     * If it returns true, it will process the tasks in the order they are added to the list.
     * It creates a more linear flow to the script, such that if a task is able to be activated, it will execute and stop the script from processing other tasks
     * that are in the list after it.
     *
     * @return True if the script should prioritize tasks, false if it should not.
     */
    @Override
    public boolean prioritizeTasks() {
        return true;
    }

    /**
     * This method is called every time the script is processed.
     * It will check if the script has started and if it has, it will call the super method.
     * The super method will call the tasks and process them.
     */
    @Override
    public void onProcess() {
        if (!this.started) {
            return;
        }
        super.onProcess();
    }

    /**
     * This method is called when the script is terminated.
     * It will close the GUI and set the frame to null.
     */
    @Override
    public void onTerminate() {
        if (this.frame != null) {
            this.frame.setVisible(false);
            this.frame = null;
        }
    }

    /**
     * This method is called on script execution.
     * It creates a new instance of the paint helper and adds the lines to it.
     * The lines are added in the order we want them to be displayed.
     * The paint helper will then handle the painting of the lines.
     * We only want to call this method once, so we check if the paint helper is null.
     * If it is not null, we return and do not call the method again.
     * This is to prevent the lines from being added multiple times.
     * We don't add default lines here, we add them in the paint helper class such as the runtime and script status.
     */
    private void setupPaint() {
        if (this.paintHelper != null) {
            return;
        }
        this.paintHelper = new PaintHelper(this);
        paintHelper.addLine(() -> "Trips: " + getState().getTotalTrips() + " (" + ctx.paint.valuePerHour(getState().getTotalTrips(), this.startTime) + ")");
        paintHelper.addLine(() -> "Looted chests: " + getState().getBarrowsChestsOpened() + " (" + ctx.paint.valuePerHour(getState().getBarrowsChestsOpened(), this.startTime) + ")");
        paintHelper.addLine(() -> "Brothers killed: " + getState().getBrothersKilled());
    }

    /**
     * This method is called when the user clicks the start button on the GUI. It will set the settings and start the script.
     *
     * @param settings The settings that the user has selected in the GUI.
     */
    public void startScript(final BarrowsSettings settings) {
        this.settings = settings;
        this.started = true;
        this.startTime = System.currentTimeMillis();
        setScriptStatus("Starting script...");
        this.frame.setVisible(false);
    }

    /**
     * This method is the listener for all chat messages we receive in the game.
     *
     * @param event The event that is fired when a chat message is received.
     */
    @Override
    public void onChatMessage(final ChatMessageEvent event) {
        if (event.getMessageType() != 0) {
            return;
        }
        if (event.getSender() != null && !event.getSender().isEmpty()) {
            return;
        }
        final String message = event.getMessage().toLowerCase();
        if (message.equals("your inventory items were sent to your bank.")) {
            getState().setPresetLoaded(true);
        } else if (message.equals("the sarcophagus appears to be empty.")) {
            final BarrowsHill tomb = getState().getCurrentTomb();
            if (tomb != null) {
                getState().setBrotherKilled(tomb, true);
            }
        } else if (message.contains("@or3@you've looted a total of")) {
            getState().incrementBarrowsChestsOpened();
            getState().reset();
        } else if (message.equals("it doesn't seem to open.") || message.equals("the chest appears to be empty.")) {
            getState().setTeleportingHome(true);
        }
    }

    /**
     * This method is called when the script is painted.
     * It will call the paint helper to draw the paint.
     * We want to check if the paint helper is null, because if it is, we don't want to draw the paint.
     * This is to prevent a null pointer exception.
     *
     * @param g The graphics object that is used to draw the paint.
     */
    @Override
    public void onPaint(Graphics2D g) {
        if (paintHelper == null) {
            return;
        }
        paintHelper.drawPaint(g);
    }

    /**
     * Invoked when the mouse button has been clicked (pressed)
     * This is used to toggle the paint on and off. We check if the mouse click was within the bounds of the paint.
     * If it was, we invert the drawingPaint boolean.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(final MouseEvent e) {
        if (paintHelper == null) {
            return;
        }
        paintHelper.handleMouseClick(e);
    }

    @Override
    public void mousePressed(final MouseEvent e) {

    }

    @Override
    public void mouseReleased(final MouseEvent e) {

    }

    @Override
    public void mouseEntered(final MouseEvent e) {

    }

    @Override
    public void mouseExited(final MouseEvent e) {

    }

    /**
     * The loop duration is the amount of time in milliseconds that the script will wait before executing the next loop.
     *
     * @return the loop duration in milliseconds
     */
    @Override
    public int loopDuration() {
        return 150;
    }

    /**
     * Gets the start time of the script.
     *
     * @return the start time
     */
    public long getStartTime() {
        return this.startTime;
    }

    /**
     * Gets our script settings object that was set when the script was started from the GUI.
     *
     * @return the script settings
     */
    public BarrowsSettings getSettings() {
        return this.settings;
    }

    /**
     * Gets the script state object that contains all the information about the current state of the script.
     *
     * @return the script state
     */
    public BarrowsState getState() {
        return this.state;
    }

}
