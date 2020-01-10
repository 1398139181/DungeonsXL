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
package de.erethon.dungeonsxl.api.world;

import de.erethon.dungeonsxl.api.player.InstancePlayer;
import de.erethon.dungeonsxl.api.sign.DungeonSign;
import de.erethon.dungeonsxl.world.WorldConfig;
import java.io.File;
import java.util.Collection;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * <p>
 * An instance world is not equal to a {@link de.erethon.dungeonsxl.api.dungeon.Dungeon}.
 *
 * @author Daniel Saukel
 */
public interface InstanceWorld {

    String getName();

    WorldConfig getConfig();

    ResourceWorld getResource();

    File getFolder();

    World getWorld();

    boolean exists();

    int getId();

    Collection<DungeonSign> getDungeonSigns();

    void addDungeonSign(DungeonSign sign);

    void removeDungeonSign(DungeonSign sign);

    Location getLobbyLocation();

    void setLobbyLocation(Location location);

    Collection<InstancePlayer> getPlayers();

    /**
     * Sends a message to all players in the instance.
     *
     * @param message the message to send
     */
    void sendMessage(String message);

    /**
     * Makes all players leave the world. Attempts to let them leave properly if they are correct DInstancePlayers; teleports them to the spawn if they are not.
     */
    void kickAllPlayers();

    /**
     * Deletes this instance.
     */
    void delete();

}
