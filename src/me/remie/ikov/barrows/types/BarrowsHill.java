package me.remie.ikov.barrows.types;

import simple.api.coords.WorldArea;
import simple.api.coords.WorldPoint;
import simple.api.filters.SimplePrayers;

/**
 * Created by Reminisce on Feb 14, 2023 at 12:03 AM
 *
 * @author Seth Davis <sethdavis321@gmail.com>
 * @Discord Reminisce#1707 <138751815847116800>
 */
public enum BarrowsHill {


    AHRIM_HILL(20770, "Ahrim the Blighted", SimplePrayers.Prayers.PROTECT_FROM_MAGIC,
            new WorldPoint(3564, 3289, 0), new WorldPoint(3558, 9703, 0),
            new WorldPoint(3555, 9698, 0), new WorldArea(new WorldPoint(3562, 3286, 0), new WorldPoint(3568, 3292, 0)),
            new WorldArea(new WorldPoint(3562, 9704, 3), new WorldPoint(3549, 9693, 3))),

    DHAROK_HILL(20720, "Dharok the Wretched", SimplePrayers.Prayers.PROTECT_FROM_MELEE,
            new WorldPoint(3574, 3299, 0), new WorldPoint(3557, 9718, 0),
            new WorldPoint(3554, 9714, 0), new WorldArea(new WorldPoint(3574, 3300, 0), new WorldPoint(3577, 3296, 0)),
            new WorldArea(new WorldPoint(3561, 9719, 3), new WorldPoint(3548, 9709, 3))),

    GUTHAN_HILL(20722, "Guthan the Infested", SimplePrayers.Prayers.PROTECT_FROM_MELEE,
            new WorldPoint(3577, 3281, 0), new WorldPoint(3534, 9705, 0),
            new WorldPoint(3538, 9703, 0), new WorldArea(new WorldPoint(3576, 3283, 0), new WorldPoint(3579, 3280, 0)),
            new WorldArea(new WorldPoint(3533, 9709, 3), new WorldPoint(3546, 9698, 3))),

    KARIL_HILL(20771, "Karil the Tainted", SimplePrayers.Prayers.PROTECT_FROM_MISSILES,
            new WorldPoint(3565, 3275, 0), new WorldPoint(3546, 9685, 0),
            new WorldPoint(3550, 9682, 0), new WorldArea(new WorldPoint(3567, 3277, 0), new WorldPoint(3564, 3274, 0)),
            new WorldArea(new WorldPoint(3545, 9689, 3), new WorldPoint(3558, 9677, 3))),

    TORAG_HILL(20721, "Torag the Corrupted", SimplePrayers.Prayers.PROTECT_FROM_MELEE,
            new WorldPoint(3555, 3282, 0), new WorldPoint(3565, 9683, 0),
            new WorldPoint(3569, 9685, 0), new WorldArea(new WorldPoint(3555, 3281, 0), new WorldPoint(3552, 3284, 0)),
            new WorldArea(new WorldPoint(3563, 9682, 3), new WorldPoint(3576, 9693, 3))),

    VERAC_HILL(20772, "Verac the Defiled", SimplePrayers.Prayers.PROTECT_FROM_MELEE,
            new WorldPoint(3558, 3298, 0), new WorldPoint(3578, 9703, 0),
            new WorldPoint(3573, 9705, 0), new WorldArea(new WorldPoint(3558, 3296, 0), new WorldPoint(3555, 3300, 0)),
            new WorldArea(new WorldPoint(3579, 9703, 3), new WorldPoint(3567, 9711, 3)));

    private final int objectId;

    private final String npcName;

    private final SimplePrayers.Prayers prayer;

    private final WorldPoint stepTile;

    private final WorldPoint stairTile;

    private final WorldPoint coffinTile;

    private final WorldArea hillArea;

    private final WorldArea tombArea;

    BarrowsHill(int objectId, String npcName, SimplePrayers.Prayers prayer, WorldPoint stepTile, WorldPoint stairTile, WorldPoint coffinTile, WorldArea hillArea, WorldArea tombArea) {
        this.npcName = npcName;
        this.objectId = objectId;
        this.prayer = prayer;
        this.stepTile = stepTile;
        this.stairTile = stairTile;
        this.coffinTile = coffinTile;
        this.hillArea = hillArea;
        this.tombArea = tombArea;
    }

    /**
     * Gets the hill by npc name.
     *
     * @param name the npc name.
     * @return the hill.
     */
    public static BarrowsHill getHillByNpcName(String name) {
        for (BarrowsHill hill : values()) {
            if (hill.getNpcName().equals(name))
                return hill;
        }
        return null;
    }

    /**
     * Gets the object id of the coffin.
     *
     * @return the object id of the coffin.
     */
    public int getObjectId() {
        return objectId;
    }

    /**
     * Gets the npc name.
     *
     * @return the npc name.
     */
    public String getNpcName() {
        return npcName;
    }

    /**
     * Gets the step tile.
     *
     * @return the step tile.
     */
    public WorldPoint getStepTile() {
        return stepTile;
    }

    /**
     * Gets the stair tile.
     *
     * @return the stair tile.
     */
    public WorldPoint getStairTile() {
        return stairTile;
    }

    /**
     * Gets the coffin tile.
     *
     * @return the coffin tile.
     */
    public WorldPoint getCoffinTile() {
        return coffinTile;
    }

    /**
     * Gets the prayer to protect from.
     *
     * @return the prayer.
     */
    public SimplePrayers.Prayers getPrayer() {
        return prayer;
    }

    /**
     * Gets the hill area.
     *
     * @return the hill area.
     */
    public WorldArea getHillArea() {
        return hillArea;
    }

    /**
     * Gets the tomb area.
     *
     * @return the tomb area.
     */
    public WorldArea getTombArea() {
        return tombArea;
    }

    /**
     * Gets the brother index.
     *
     * @return the brother index.
     */
    public int getBrotherIndex() {
        return ordinal();
    }

    /**
     * Determines if the brother is a melee brother.
     *
     * @return true if melee, false otherwise.
     */
    public boolean isMelee() {
        return prayer == SimplePrayers.Prayers.PROTECT_FROM_MELEE;
    }

}
