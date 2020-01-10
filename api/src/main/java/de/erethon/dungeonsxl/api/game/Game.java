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
package de.erethon.dungeonsxl.api.game;

import de.erethon.dungeonsxl.api.player.PlayerGroup;
import de.erethon.dungeonsxl.api.world.GameWorld;
import de.erethon.dungeonsxl.api.world.ResourceWorld;
import java.util.List;

/**
 * @author Daniel Saukel
 */
public interface Game {

    /**
     * Returns if this is a tutorial game.
     *
     * @return if this is a tutorial game
     */
    boolean isTutorial();

    /**
     * Sets if this is a tutorial game
     *
     * @param tutorial if this is a tutorial game
     */
    void setTutorial(boolean tutorial);

    /**
     * Returns a read-only List of the groups that are playing this game.
     *
     * @return a read-only List of the groups that are playing this game
     */
    List<PlayerGroup> getGroups();

    /**
     * Adds the given group to this game.
     *
     * @param group the group
     */
    void addGroup(PlayerGroup group);

    /**
     * Removes the given group from this game.
     *
     * @param group the group
     */
    void removeGroup(PlayerGroup group);

    /**
     * Returns if the group has started (=if the ready sign has been triggered).
     *
     * @return if the group has started
     */
    boolean hasStarted();

    /**
     * Sets the status of the game to have started / not yet started.
     *
     * @param started if the game has started
     */
    void setStarted(boolean started);

    /**
     * Returns the game instance in which this game takes place.
     *
     * @return the game instance in which this game takes place
     */
    GameWorld getWorld();

    /**
     * Sets the game instance in which this game takes place.
     *
     * @param gameWorld the game instance in which this game takes place
     */
    void setWorld(GameWorld gameWorld);

    /**
     * Returns a read-only List of the remaining floors to play.
     *
     * @return a read-only List of the remaining floors to play
     */
    List<ResourceWorld> getUnplayedFloors();

    /**
     * Returns the amount of played floors in this game.
     *
     * @return the amount of played floors in this game
     */
    int getFloorCount();

}
