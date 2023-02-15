package me.remie.ikov.barrows.types;

/**
 * Created by Reminisce on Feb 13, 2023 at 7:24 PM
 *
 * @author Seth Davis <sethdavis321@gmail.com>
 * @Discord Reminisce#1707 <138751815847116800>
 */
public enum MagicSpell {

    NONE("Select spell...", -1, -1, new int[][]{}),

    FIRE_SURGE("Fire surge", 95, 1181, new int[][]{
            {554, 10},//fire
            {556, 7},//air
            {556, 1},//wrath
    }),
    FIRE_WAVE("Fire wave", 75, 1189, new int[][]{
            {554, 7},//fire
            {556, 5},//air
            {565, 1}//blood
    }),
    FIRE_BLAST("Fire blast", 59, 21481, new int[][]{
            {554, 5},//fire
            {556, 4},//air
            {560, 1}//death
    });

    private final String spellName;

    private final int level;

    private final int spellId;

    private final int[][] runes;

    MagicSpell(String spellName, int level, int spellId, int[][] runes) {
        this.spellName = spellName;
        this.level = level;
        this.spellId = spellId;
        this.runes = runes;
    }

    /**
     * Gets the spell by the spell id.
     *
     * @param spellId the spell id
     * @return the spell
     */
    public static MagicSpell getSpell(int spellId) {
        for (MagicSpell spell : values()) {
            if (spell.getSpellId() == spellId)
                return spell;
        }
        return null;
    }

    /**
     * Gets the spell name.
     *
     * @return the spell name
     */
    public String getSpellName() {
        return spellName;
    }

    /**
     * Gets the level required for the spell.
     *
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets the spell id.
     *
     * @return the spell id
     */
    public int getSpellId() {
        return spellId;
    }

    /**
     * Gets the runes required for the spell.
     *
     * @return the runes
     */
    public int[][] getRunes() {
        return runes;
    }

    /**
     * Gets the spell name and level. This is used for the combobox.
     *
     * @return the spell name and level
     */
    @Override
    public String toString() {
        return getSpellName() + (this == NONE ? "" : " (Level: " + getLevel() + ")");
    }


}
