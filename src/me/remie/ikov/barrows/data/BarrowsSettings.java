package me.remie.ikov.barrows.data;

import me.remie.ikov.barrows.types.FoodTypes;
import me.remie.ikov.barrows.types.MagicSpell;
import me.remie.ikov.barrows.types.OffensivePrayer;

/**
 * Created by Reminisce on Feb 12, 2023 at 7:26 PM
 *
 * @author Seth Davis <sethdavis321@gmail.com>
 * @Discord Reminisce#1707 <138751815847116800>
 */
public class BarrowsSettings {

    private final FoodTypes foodType;
    private final int eatHealth;
    private final int drinkPrayer;
    private final MagicSpell magicSpell;
    private final OffensivePrayer primaryOffensivePrayer;
    private final OffensivePrayer secondaryOffensivePrayer;
    private final int presetId;
    private final String[] primaryEquipment;
    private final String[] secondaryEquipment;

    public BarrowsSettings(FoodTypes foodType, int eatHealth, int drinkPrayer, MagicSpell magicSpell,
                           OffensivePrayer primaryOffensivePrayer, OffensivePrayer secondaryOffensivePrayer, int presetId,
                           String[] primaryEquipment, String[] secondaryEquipment) {
        this.foodType = foodType;
        this.eatHealth = eatHealth;
        this.drinkPrayer = drinkPrayer;
        this.magicSpell = magicSpell;
        this.primaryOffensivePrayer = primaryOffensivePrayer;
        this.secondaryOffensivePrayer = secondaryOffensivePrayer;
        this.presetId = presetId;
        this.primaryEquipment = primaryEquipment;
        this.secondaryEquipment = secondaryEquipment;
    }

    /**
     * Gets the food type.
     *
     * @return the food type.
     */
    public FoodTypes getFoodType() {
        return foodType;
    }

    /**
     * Gets the health to eat at.
     *
     * @return the health to eat at.
     */
    public int getEatHealth() {
        return eatHealth;
    }

    /**
     * Gets the prayer points to drink at.
     *
     * @return the drink prayer.
     */
    public int getDrinkPrayer() {
        return drinkPrayer;
    }

    /**
     * Gets the magic spell.
     *
     * @return the magic spell.
     */
    public MagicSpell getMagicSpell() {
        return magicSpell;
    }

    /**
     * Gets the primary offensive prayer.
     *
     * @return the primary offensive prayer.
     */
    public OffensivePrayer getPrimaryOffensivePrayer() {
        return primaryOffensivePrayer;
    }

    /**
     * Gets the secondary offensive prayer.
     *
     * @return the secondary offensive prayer.
     */
    public OffensivePrayer getSecondaryOffensivePrayer() {
        return secondaryOffensivePrayer;
    }

    /**
     * Gets the preset id.
     *
     * @return the preset id.
     */
    public int getPresetId() {
        return presetId;
    }

    /**
     * Gets the names of the primary equipment.
     *
     * @return the names of the primary equipment.
     */
    public String[] getPrimaryEquipment() {
        return primaryEquipment;
    }

    /**
     * Gets the names of the secondary equipment.
     *
     * @return the names of the secondary equipment.
     */
    public String[] getSecondaryEquipment() {
        return secondaryEquipment;
    }

}

