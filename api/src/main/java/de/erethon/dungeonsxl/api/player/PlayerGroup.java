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
package de.erethon.dungeonsxl.api.player;

import de.erethon.caliburn.item.ExItem;
import de.erethon.caliburn.item.VanillaItem;
import de.erethon.commons.compatibility.Version;
import de.erethon.commons.player.PlayerCollection;
import de.erethon.dungeonsxl.api.dungeon.Dungeon;
import de.erethon.dungeonsxl.api.world.GameWorld;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;

/**
 * Represents a group of players provided by DungeonsXL.
 *
 * @author Daniel Saukel
 */
public interface PlayerGroup {

    /**
     * Links different color types together.
     */
    public enum Color {

        BLACK(ChatColor.BLACK, DyeColor.BLACK, VanillaItem.BLACK_WOOL),
        DARK_GRAY(ChatColor.DARK_GRAY, DyeColor.GRAY, VanillaItem.GRAY_WOOL),
        LIGHT_GRAY(ChatColor.GRAY, DyeColor.valueOf(Version.isAtLeast(Version.MC1_13) ? "LIGHT_GRAY" : "SILVER"), VanillaItem.LIGHT_GRAY_WOOL),
        WHITE(ChatColor.WHITE, DyeColor.WHITE, VanillaItem.WHITE_WOOL),
        DARK_GREEN(ChatColor.DARK_GREEN, DyeColor.GREEN, VanillaItem.GREEN_WOOL),
        LIGHT_GREEN(ChatColor.GREEN, DyeColor.LIME, VanillaItem.LIME_WOOL),
        CYAN(ChatColor.DARK_AQUA, DyeColor.CYAN, VanillaItem.CYAN_WOOL),
        DARK_BLUE(ChatColor.DARK_BLUE, DyeColor.BLUE, VanillaItem.BLUE_WOOL),
        LIGHT_BLUE(ChatColor.AQUA, DyeColor.LIGHT_BLUE, VanillaItem.LIGHT_BLUE_WOOL),
        PURPLE(ChatColor.DARK_PURPLE, DyeColor.PURPLE, VanillaItem.PURPLE_WOOL),
        MAGENTA(ChatColor.LIGHT_PURPLE, DyeColor.MAGENTA, VanillaItem.MAGENTA_WOOL),
        DARK_RED(ChatColor.DARK_RED, DyeColor.BROWN, VanillaItem.BROWN_WOOL),
        LIGHT_RED(ChatColor.RED, DyeColor.RED, VanillaItem.RED_WOOL),
        ORANGE(ChatColor.GOLD, DyeColor.ORANGE, VanillaItem.ORANGE_WOOL),
        YELLOW(ChatColor.YELLOW, DyeColor.YELLOW, VanillaItem.YELLOW_WOOL),
        PINK(ChatColor.BLUE, DyeColor.PINK, VanillaItem.PINK_WOOL);

        private ChatColor chat;
        private DyeColor dye;
        private VanillaItem woolMaterial;

        Color(ChatColor chat, DyeColor dye, VanillaItem woolMaterial) {
            this.chat = chat;
            this.dye = dye;
            this.woolMaterial = woolMaterial;
        }

        /**
         * Returns the ChatColor.
         *
         * @return the ChatColor
         */
        public ChatColor getChatColor() {
            return chat;
        }

        /**
         * Returns the DyeColor.
         *
         * @return the DyeColor
         */
        public DyeColor getDyeColor() {
            return dye;
        }

        /**
         * Returns the RGB value.
         *
         * @return the RGB value
         */
        public int getRGBColor() {
            return dye.getColor().asRGB();
        }

        /**
         * Returns the wool material.
         *
         * @return the wool material
         */
        public VanillaItem getWoolMaterial() {
            return woolMaterial;
        }

        /**
         * Returns the GroupColor matching the ChatColor or null if none exists.
         *
         * @param color the ChatColor to check
         * @return the GroupColor matching the ChatColor or null if none exists
         */
        public static Color getByChatColor(ChatColor color) {
            for (Color groupColor : values()) {
                if (groupColor.chat == color) {
                    return groupColor;
                }
            }
            return null;
        }

        /**
         * Returns the GroupColor matching the DyeColor or null if none exists.
         *
         * @param color the DyeColor to check
         * @return the GroupColor matching the DyeColor or null if none exists.
         */
        public static Color getByDyeColor(DyeColor color) {
            for (Color groupColor : values()) {
                if (groupColor.dye == color) {
                    return groupColor;
                }
            }
            return null;
        }

        /**
         * Returns the GroupColor matching the wool material or null if none exists.
         *
         * @param wool the wool material to check
         * @return the GroupColor matching the wool material or null if none exists
         */
        public static Color getByWoolType(ExItem wool) {
            for (Color groupColor : values()) {
                if (groupColor.woolMaterial == wool) {
                    return groupColor;
                }
            }
            return null;
        }

    }

    int getId();

    String getName();

    String getRawName();

    void setName(String name);

    default void setName(Color color) {
        setName(color.toString() + "#" + getId());
    }

    Player getCaptain();

    void setCaptain(Player player);

    PlayerCollection getPlayers();

    default void addPlayer(Player player) {
        addPlayer(player, true);
    }

    void addPlayer(Player player, boolean message);

    default void removePlayer(Player player) {
        addPlayer(player, true);
    }

    void removePlayer(Player player, boolean message);

    PlayerCollection getInvitedPlayers();

    void addInvitedPlayer(Player player, boolean message);

    void removeInvitedPlayer(Player player, boolean message);

    /**
     * Removes all invitations for players who are not online.
     */
    void clearOfflineInvitedPlayers();

    GameWorld getGameWorld();

    void setGameWorld(GameWorld gameWorld);

    Dungeon getDungeon();

    boolean isPlaying();

    int getLives();

    void setLives(int lives);

}
