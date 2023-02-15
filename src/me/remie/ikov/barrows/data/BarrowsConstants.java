package me.remie.ikov.barrows.data;

import simple.api.coords.WorldArea;
import simple.api.coords.WorldPoint;

/**
 * Created by Reminisce on Feb 12, 2023 at 7:26 PM
 *
 * @author Seth Davis <sethdavis321@gmail.com>
 * @Discord Reminisce#1707 <138751815847116800>
 */
public class BarrowsConstants {

    public static final WorldArea HOME_AREA = new WorldArea(
            new WorldPoint(3072, 3521, 0), new WorldPoint(3072, 3464, 0),
            new WorldPoint(3137, 3474, 0), new WorldPoint(3137, 3521, 0));

    public static final WorldArea BARROWS_AREA = new WorldArea(
            new WorldPoint(3545, 3320, 0),
            new WorldPoint(3585, 3267, 0));

    public static final WorldArea BARROWS_CHEST_ROOM = new WorldArea(3545, 9689, 13, 13, 0);

}
