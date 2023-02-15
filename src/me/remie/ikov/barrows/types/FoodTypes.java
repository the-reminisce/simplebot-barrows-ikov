package me.remie.ikov.barrows.types;

/**
 * Created by Reminisce on Feb 12, 2023 at 4:41 PM
 *
 * @author Seth Davis <sethdavis321@gmail.com>
 * @Discord Reminisce#1707 <138751815847116800>
 */
public enum FoodTypes {

    NONE("Select food..."),
    SHRIMPS("Shrimps"),
    ANCHOVIES("Anchovies"),
    TROUT("Trout"),
    PIKE("Pike"),
    SALMON("Salmon"),
    TUNA("Tuna"),
    LOBSTER("Lobster"),
    BASS("Bass"),
    SWORDFISH("Swordfish"),
    MONKFISH("Monkfish"),
    SHARK("Shark"),
    CAKE("Cake"),
    CHOCOLATE_CAKE("Chocolate cake"),
    POTATO("Potato"),
    BAKED_POTATO("Baked potato"),
    MUSHROOM_POTATO("Mushroom potato"),
    TUNA_POTATO("Tuna potato"),
    PIZZA("Pizza"),
    ANGLERFISH("Anglerfish"),
    DARK_CRAB("Dark crab"),
    ROCKTAIL("Rocktail");

    private final String name;

    FoodTypes(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the food type.
     * @return the name of the food type.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the food type by the name. This is used for the combobox.
     * @return the food type by the name.
     */
    @Override
    public String toString() {
        return name;
    }

}

