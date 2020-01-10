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

import de.erethon.caliburn.item.VanillaItem;
import de.erethon.commons.chat.MessageUtil;
import de.erethon.dungeonsxl.DungeonsXL;
import de.erethon.dungeonsxl.api.world.GameWorld;
import de.erethon.dungeonsxl.config.DMessage;
import de.erethon.dungeonsxl.event.dsign.DSignRegistrationEvent;
import de.erethon.dungeonsxl.game.Game;
import de.erethon.dungeonsxl.sign.DSignTypeDefault;
import de.erethon.dungeonsxl.trigger.Trigger;
import de.erethon.dungeonsxl.world.DGameWorld;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;

/**
 * Extend this to create a custom dungeon sign.
 *
 * @author Frank Baumann, Milan Albrecht, Daniel Saukel
 */
public abstract class AbstractDSign implements DungeonSign {

    protected DungeonsXL plugin;

    public static final String ERROR_0 = ChatColor.DARK_RED + "## ERROR ##";
    public static final String ERROR_1 = ChatColor.WHITE + "Please";
    public static final String ERROR_2 = ChatColor.WHITE + "contact an";
    public static final String ERROR_3 = ChatColor.WHITE + "admin!";

    private Sign sign;
    protected String[] lines;
    private GameWorld gameWorld;

    // List of Triggers
    private Set<Trigger> triggers = new HashSet<>();

    protected boolean initialized, erroneous;

    protected AbstractDSign(DungeonsXL plugin, Sign sign, String[] lines, GameWorld gameWorld) {
        this.plugin = plugin;

        this.sign = sign;
        this.lines = lines;
        this.gameWorld = gameWorld;

        // Check Trigger
        if (gameWorld == null) {
            return;
        }

        String line3 = lines[3].replaceAll("\\s", "");
        String[] triggerTypes = line3.split(",");

        for (String triggerString : triggerTypes) {
            if (triggerString.isEmpty()) {
                continue;
            }

            String type = triggerString.substring(0, 1);
            String value = null;
            if (triggerString.length() > 1) {
                value = triggerString.substring(1);
            }

            Trigger trigger = Trigger.getOrCreate(plugin, type, value, this);
            if (trigger != null) {
                trigger.addListener(this);
                addTrigger(trigger);
            }
        }
    }

    /* Getters and setters */
    @Override
    public Sign getSign() {
        return sign;
    }

    /**
     * @param sign the sign to set
     */
    public void setSign(Sign sign) {
        this.sign = sign;
    }

    @Override
    public String[] getLines() {
        return lines;
    }

    /**
     * @param lines the sign lines to set
     */
    public void setLines(String[] lines) {
        this.lines = lines;
    }

    @Override
    public GameWorld getGameWorld() {
        return gameWorld;
    }

    /**
     * @return the game
     */
    public Game getGame() {
        return Game.getByGameWorld(gameWorld);
    }

    @Override
    public Set<Trigger> getTriggers() {
        return triggers;
    }

    //@Override
    public void addTrigger(Trigger trigger) {
        triggers.add(trigger);
    }

    //@Override
    public void removeTrigger(Trigger trigger) {
        triggers.remove(trigger);
    }

    /* Actions */
    @Override
    public void initialize() {
        initialized = true;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    public void remove() {
        for (Trigger trigger : triggers) {
            trigger.removeListener(this);
        }
        gameWorld.getDSigns().remove(this);
    }

    @Override
    public boolean setToAir() {
        if (!erroneous && getType().isSetToAir()) {
            sign.getBlock().setType(VanillaItem.AIR.getMaterial());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isErroneous() {
        return erroneous;
    }

    @Override
    public void markAsErroneous(String reason) {
        erroneous = true;
        sign.setLine(0, ERROR_0);
        sign.setLine(1, ERROR_1);
        sign.setLine(2, ERROR_2);
        sign.setLine(3, ERROR_3);
        sign.update();

        MessageUtil.log(plugin, DMessage.LOG_ERROR_SIGN_SETUP.getMessage(sign.getX() + ", " + sign.getY() + ", " + sign.getZ()));
        MessageUtil.log(plugin, getType().getName() + ": " + reason);
    }

    /* Statics */
    public static AbstractDSign create(DungeonsXL plugin, Sign sign, DGameWorld gameWorld) {
        return create(plugin, sign, sign.getLines(), gameWorld);
    }

    public static AbstractDSign create(DungeonsXL plugin, Sign sign, String[] lines, DGameWorld gameWorld) {
        AbstractDSign dSign = null;

        for (DungeonSignType type : plugin.getSignTypeRegistry()) {
            if (!lines[0].equalsIgnoreCase("[" + type.getName() + "]")) {
                continue;
            }

            try {
                Constructor<? extends DungeonSign> constructor = type.getHandler().getConstructor(DungeonsXL.class, Sign.class, String[].class, DGameWorld.class);
                dSign = (AbstractDSign) constructor.newInstance(plugin, sign, lines, gameWorld);

            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
                MessageUtil.log("An error occurred while accessing the handler class of the sign " + type.getName() + ": " + exception.getClass().getSimpleName());
                if (!(type instanceof DSignTypeDefault)) {
                    MessageUtil.log("Please note that this sign is an unsupported feature added by an addon!");
                }
                exception.printStackTrace();
            }
        }

        if (gameWorld != null) {
            DSignRegistrationEvent event = new DSignRegistrationEvent(sign, gameWorld, dSign);
            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return null;
            }
        }

        if (!(dSign != null && gameWorld != null)) {
            return dSign;
        }

        if (dSign.getType().isOnDungeonInit()) {
            dSign.initialize();
            dSign.setToAir();
        }

        return dSign;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{sign=" + sign + "; gameWorld=" + gameWorld + "}";
    }

}
