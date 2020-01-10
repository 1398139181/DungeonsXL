/*
 * Copyright (C) 2012-2020 Frank Baumann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.erethon.dungeonsxl.api.sign;

import de.erethon.dungeonsxl.api.world.GameWorld;
import java.util.Set;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

/**
 * Interface for all dungeon signs.
 *
 * @author Frank Baumann, Milan Albrecht, Daniel Saukel
 */
public interface DungeonSign {

    /**
     * Returns the type of the sign.
     *
     * @return the type of the sign
     */
    DungeonSignType getType();

    Set/*<Trigger>*/ getTriggers();

    Sign getSign();

    String[] getLines();

    GameWorld getGameWorld();

    //void addTrigger(Trigger trigger);

    //void removeTrigger(Trigger trigger);

    void initialize();

    boolean isInitialized();

    /**
     * Triggers the sign. The effects are defined by the implementation.
     * @param player the player that triggered the sign or null
     */
    void trigger(Player player);

    void update();

    default boolean hasTriggers() {
        return !getTriggers().isEmpty();
    }

    /**
     * Sets the sign to air if it is not erroneous and if its type requires this.
     *
     * @return if the sign type was set to air
     */
    boolean setToAir();

    /**
     * Returns if the sign is valid.
     *
     * @return if the sign is valid
     */
    default boolean validate() {
        return true;
    }

    /**
     * Returns if the sign is erroneous.
     *
     * @return if the sign is erroneous
     */
    boolean isErroneous();

    /**
     * Set a placeholder to show that the sign is setup incorrectly.
     *
     * @param reason the reason why the sign is marked as erroneous
     */
    void markAsErroneous(String reason);

}
