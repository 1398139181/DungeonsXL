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

/**
 * Implement this to create custom sign types.
 *
 * @author Daniel Saukel
 */
public interface DungeonSignType {

    /**
     * Returns the name to identify the sign.
     *
     * @return the name
     */
    String getName();

    /**
     * Returns the permission node that is required to build a sign of this type.
     *
     * @return the buildPermission
     */
    String getBuildPermission();

    /**
     * Returns if the sign gets initialized when the dungeon is loaded instead of when the game starts.
     *
     * @return if the sign gets initialized when the dungeon is loaded instead of when the game starts
     */
    boolean isOnDungeonInit();

    /**
     * Returns if the sign block is breakable after the initialization.
     *
     * @return if the sign block is breakable after the initialization
     */
    boolean isProtected();

    /**
     * Returns if the block type of the sign is set to air after the initialization.
     *
     * @return if the block type of the sign is set to air after the initialization
     */
    boolean isSetToAir();

    /**
     * Returns the class that handles the sign.
     *
     * @return the class that handles the sign
     */
    Class<? extends DungeonSign> getHandler();

}
