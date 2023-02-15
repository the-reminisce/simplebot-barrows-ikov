package me.remie.ikov.barrows.data;

import me.remie.ikov.barrows.types.BarrowsHill;
import simple.api.ClientAccessor;
import simple.api.ClientContext;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Reminisce on Feb 12, 2023 at 7:26 PM
 *
 * @author Seth Davis <sethdavis321@gmail.com>
 * @Discord Reminisce#1707 <138751815847116800>
 */
public class BarrowsState extends ClientAccessor<ClientContext> {

    private boolean cursesPrayerEnabled = false;
    private boolean presetLoaded = false;
    private boolean teleportingHome = false;

    private int totalTrips = 0;
    private int barrowsChestsOpened = 0;
    private int bossBrotherIndex = -1;

    private final boolean[] killedBrothers = new boolean[6];
    private final BarrowsHill[] RANDOMIZED_BROTHER_ORDER = new BarrowsHill[6];

    private final Random random = new Random();

    public BarrowsState(ClientContext ctx) {
        super(ctx);
        createRandomHillOrder();
    }

    /**
     * This method creates a random order of the barrows brothers for the bot to search in.
     * This is done to prevent the bot from always searching the same order.
     * This is used in {@link me.remie.ikov.barrows.data.BarrowsState#getNextHill()}.
     * This can cause the bot to become less efficient, but it is a necessary to prevent detection.
     */
    private void createRandomHillOrder() {
        final boolean[] used = new boolean[6];
        for (int i = 0; i < RANDOMIZED_BROTHER_ORDER.length; i++) {
            int index = random.nextInt(6);
            while (used[index]) {
                index = random.nextInt(6);
            }
            used[index] = true;
            RANDOMIZED_BROTHER_ORDER[i] = BarrowsHill.values()[index];
        }
        System.out.println("Randomized brother order: " + Arrays.toString(RANDOMIZED_BROTHER_ORDER));
    }

    /**
     * Checks if the player is at the home area.
     * @return if the player is at the home area.
     */
    public boolean atHome() {
        return BarrowsConstants.HOME_AREA.within();
    }

    /**
     * Checks if the player is at the barrows area.
     * @return if the player is at the barrows area.
     */
    public boolean inBarrows() {
        return BarrowsConstants.BARROWS_AREA.within();
    }

    /**
     * Checks if the player has loaded their preset.
     * @return if the player has loaded their preset.
     */
    public boolean isPresetLoaded() {
        return presetLoaded;
    }

    /**
     * Sets the state of the preset loaded.
     * @param presetLoaded the state to set.
     */
    public void setPresetLoaded(boolean presetLoaded) {
        this.presetLoaded = presetLoaded;
    }

    /**
     * Checks if the brother is killed.
     *
     * @param index the index of the brother.
     * @return if the brother is killed.
     */
    public boolean isBrotherKilled(int index) {
        return killedBrothers[index];
    }

    /**
     * Gets whether all brothers are killed.
     *
     * @return if all brothers are killed.
     */
    public boolean killedAllBrothers() {
        for (boolean killed : killedBrothers) {
            if (!killed) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the amount of brothers killed.
     *
     * @return the amount of brothers killed.
     */
    public int getBrothersKilled() {
        int killed = 0;
        for (boolean b : killedBrothers) {
            if (b) {
                killed++;
            }
        }
        return killed;
    }

    /**
     * Gets the killed state of the brother.
     *
     * @param hill the hill to check if the brother is killed.
     * @return the killed state of the brother.
     */
    public boolean isBrotherKilled(BarrowsHill hill) {
        return killedBrothers[hill.ordinal()];
    }

    /**
     * Sets the killed state of the brother.
     *
     * @param hill   the hill to check if the brother is killed.
     * @param killed the killed state to set.
     */
    public void setBrotherKilled(BarrowsHill hill, boolean killed) {
        killedBrothers[hill.ordinal()] = killed;
    }

    /**
     * Gets the boss brother index.
     *
     * @return the boss brother index or -1 if the boss brother is not set.
     */
    public int getBossBrotherIndex() {
        return bossBrotherIndex;
    }

    /**
     * Gets the boss brother's hill or null if the boss brother is not set.
     *
     * @return the boss brother's hill or null if the boss brother is not set.
     */
    public BarrowsHill getBossBrother() {
        if (bossBrotherIndex == -1) {
            return null;
        }
        return BarrowsHill.values()[bossBrotherIndex];
    }

    /**
     * Sets the boss brother index.
     *
     * @param hill the hill to set the boss brother index to.
     */
    public void setBossBrother(BarrowsHill hill) {
        this.bossBrotherIndex = hill.getBrotherIndex();
    }

    /**
     * Resets the state of the barrows state.
     * This is called when the player leaves the barrows area.
     */
    public void reset() {
        setPresetLoaded(false);
        setTeleportingHome(true);
        createRandomHillOrder();
        Arrays.fill(killedBrothers, false);
    }

    /**
     * Checks if the player is in a tomb.
     *
     * @return if the player is in a tomb.
     */
    public boolean isInsideTomb() {
        if (inChestRoom()) {
            return true;
        }
        for (BarrowsHill hill : BarrowsHill.values()) {
            if (hill.getTombArea().within()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the player is in the chest room.
     *
     * @return if the player is in the chest room.
     */
    public boolean inChestRoom() {
        return BarrowsConstants.BARROWS_CHEST_ROOM.within();
    }

    /**
     * Gets the next hill to go to, it will return the boss hill if all other brothers are killed.
     *
     * @return the next hill to go to.
     */
    public BarrowsHill getNextHill() {
        for (BarrowsHill hill : RANDOMIZED_BROTHER_ORDER) {
            if (getBossBrotherIndex() == hill.getBrotherIndex()) {
                continue;
            }
            if (!isBrotherKilled(hill.getBrotherIndex())) {
                return hill;
            }
        }
        return getBrothersKilled() == 5 ? getBossBrother() : null;
    }

    /**
     * Gets the current tomb the player is in or null if the player is not in a tomb.
     *
     * @return the current tomb the player is in or null if the player is not in a tomb.
     */
    public BarrowsHill getCurrentTomb() {
        for (BarrowsHill hill : BarrowsHill.values()) {
            if (hill.getTombArea().within()) {
                return hill;
            }
        }
        return null;
    }

    /**
     * Sets the brother dead by the npc name.
     *
     * @param npcName the npc name to set the brother dead.
     */
    public void setBrotherDead(String npcName) {
        for (BarrowsHill hill : BarrowsHill.values()) {
            if (hill.getNpcName().equalsIgnoreCase(npcName)) {
                setBrotherKilled(hill, true);
                return;
            }
        }
    }

    /**
     * Checks if the player should teleport home.
     *
     * @return if the player should teleport home.
     */
    public boolean isTeleportingHome() {
        return teleportingHome;
    }

    /**
     * Sets the teleporting home state.
     *
     * @param teleportingHome the teleporting home state to set.
     */
    public void setTeleportingHome(boolean teleportingHome) {
        this.teleportingHome = teleportingHome;
    }

    /**
     * Gets the total trips.
     *
     * @return the total trips.
     */
    public int getTotalTrips() {
        return totalTrips;
    }

    /**
     * Increments the total trips.
     */
    public void incrementTotalTrips() {
        totalTrips++;
    }

    /**
     * Gets the total amount of chests opened.
     *
     * @return the total amount of chests opened.
     */
    public int getBarrowsChestsOpened() {
        return barrowsChestsOpened;
    }

    /**
     * Increments the total amount of chests opened.
     */
    public void incrementBarrowsChestsOpened() {
        barrowsChestsOpened++;
    }

    /**
     * Gets whether we are using curses prayers.
     *
     * @return
     */
    public boolean isCursesPrayerEnabled() {
        return cursesPrayerEnabled;
    }

    /**
     * Sets whether we are using curses prayers.
     *
     * @param cursesPrayerEnabled the cursesPrayerEnabled to set
     */
    public void setCursesPrayerEnabled(boolean cursesPrayerEnabled) {
        this.cursesPrayerEnabled = cursesPrayerEnabled;
    }

}
