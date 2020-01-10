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

import de.erethon.dungeonsxl.api.game.Game;
import org.bukkit.Location;

/**
 * <p>
 * A game world is not equal to a {@link de.erethon.dungeonsxl.api.dungeon.Dungeon}.
 *
 * @author Daniel Saukel
 */
public interface GameWorld extends InstanceWorld {

    enum Type {
        START_FLOOR,
        END_FLOOR,
        DEFAULT
    }

    /**
     * Returns the {@link Type} of this GameWorld.
     *
     * @return the {@link Type} of this GameWorld
     */
    Type getType();

    void setType(Type type);

    Game getGame();

    Dungeon getDungeon();

    boolean isPlaying();

    void setPlaying(boolean playing);

    Location getStartLocation();

}
