/*
 * Copyright (C) 2012-2019 Frank Baumann
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
package de.erethon.dungeonsxl.sign;

import de.erethon.dungeonsxl.api.sign.AbstractDSign;
import de.erethon.dungeonsxl.api.sign.DungeonSign;
import de.erethon.dungeonsxl.api.sign.DungeonSignType;
import de.erethon.dungeonsxl.player.DPermission;
import de.erethon.dungeonsxl.sign.button.ActionBarSign;
import de.erethon.dungeonsxl.sign.button.BossShopSign;
import de.erethon.dungeonsxl.sign.button.CheckpointSign;
import de.erethon.dungeonsxl.sign.button.ClassesSign;
import de.erethon.dungeonsxl.sign.button.CommandSign;
import de.erethon.dungeonsxl.sign.button.DungeonChestSign;
import de.erethon.dungeonsxl.sign.button.EndSign;
import de.erethon.dungeonsxl.sign.button.FlagSign;
import de.erethon.dungeonsxl.sign.button.HologramSign;
import de.erethon.dungeonsxl.sign.button.InteractSign;
import de.erethon.dungeonsxl.sign.button.LeaveSign;
import de.erethon.dungeonsxl.sign.button.LivesModifierSign;
import de.erethon.dungeonsxl.sign.button.MessageSign;
import de.erethon.dungeonsxl.sign.button.NoteSign;
import de.erethon.dungeonsxl.sign.button.PlaceSign;
import de.erethon.dungeonsxl.sign.button.ProtectionSign;
import de.erethon.dungeonsxl.sign.button.ReadySign;
import de.erethon.dungeonsxl.sign.button.ResourcePackSign;
import de.erethon.dungeonsxl.sign.button.RewardChestSign;
import de.erethon.dungeonsxl.sign.button.SoundMessageSign;
import de.erethon.dungeonsxl.sign.button.TitleSign;
import de.erethon.dungeonsxl.sign.passive.BedSign;
import de.erethon.dungeonsxl.sign.passive.LobbySign;
import de.erethon.dungeonsxl.sign.passive.StartSign;
import de.erethon.dungeonsxl.sign.rocker.BlockSign;
import de.erethon.dungeonsxl.sign.rocker.DoorSign;
import de.erethon.dungeonsxl.sign.script.ScriptSign;
import de.erethon.dungeonsxl.sign.todo.TeleportSign;
import de.erethon.dungeonsxl.sign.todo.TriggerSign;
import de.erethon.dungeonsxl.sign.windup.DropSign;
import de.erethon.dungeonsxl.sign.windup.MobSign;
import de.erethon.dungeonsxl.sign.windup.RedstoneSign;

/**
 * Default implementation of DungeonSignType.
 *
 * @author Daniel Saukel
 */
public enum DSignTypeDefault implements DungeonSignType {

    ACTION_BAR("ActionBar", "actionbar", ActionBarSign.class),
    BED("Bed", "bed", BedSign.class),
    BLOCK("Block", "block", BlockSign.class),
    BOSS_SHOP("BossShop", "bossshop", BossShopSign.class),
    CHECKPOINT("Checkpoint", "checkpoint", CheckpointSign.class),
    @Deprecated
    CHEST("Chest", "chest", RewardChestSign.class),
    CLASSES("Classes", "classes", ClassesSign.class),
    COMMAND("CMD", "cmd", CommandSign.class),
    DOOR("Door", "door", DoorSign.class),
    DROP("Drop", "drop", DropSign.class),
    DUNGEON_CHEST("DungeonChest", "dungeonchest", DungeonChestSign.class),
    END("End", "end", EndSign.class),
    @Deprecated
    EXTERNAL_MOB("ExternalMob", "mob", MobSign.class),
    FLAG("Flag", "flag", FlagSign.class),
    @Deprecated
    FLOOR("Floor", "end", EndSign.class),
    HOLOGRAM("Hologram", "hologram", HologramSign.class),
    INTERACT("Interact", "interact", InteractSign.class),
    LEAVE("Leave", "leave", LeaveSign.class),
    LIVES_MODIFIER("Lives", "lives", LivesModifierSign.class),
    LOBBY("Lobby", "lobby", LobbySign.class),
    MOB("Mob", "mob", MobSign.class),
    MESSAGE("MSG", "msg", MessageSign.class),
    NOTE("Note", "note", NoteSign.class),
    PLACE("Place", "place", PlaceSign.class),
    PROTECTION("Protection", "protection", ProtectionSign.class),
    READY("Ready", "ready", ReadySign.class),
    REDSTONE("Redstone", "redstone", RedstoneSign.class),
    RESOURCE_PACK("ResourcePack", "resourcepack", ResourcePackSign.class),
    REWARD_CHEST("RewardChest", "rewardchest", RewardChestSign.class),
    SCRIPT("Script", "script", ScriptSign.class),
    SOUND_MESSAGE("SoundMSG", "soundmsg", SoundMessageSign.class),
    START("Start", "start", StartSign.class),
    TELEPORT("Teleport", "teleport", TeleportSign.class),
    TITLE("Title", "title", TitleSign.class),
    TRIGGER("Trigger", "trigger", TriggerSign.class);

    private String name;
    private String buildPermission;
    private Class<? extends AbstractDSign> handler;

    DSignTypeDefault(String name, String buildPermission, Class<? extends AbstractDSign> handler) {
        this.name = name;
        this.buildPermission = buildPermission;
        this.handler = handler;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getBuildPermission() {
        return DPermission.SIGN.getNode() + "." + buildPermission;
    }

    @Override
    public boolean isOnDungeonInit() {
        switch (this) {
            case ACTION_BAR:
            case CLASSES:
            case DUNGEON_CHEST:
            case HOLOGRAM:
            case INTERACT:
            case LEAVE:
            case LOBBY:
            case READY:
            case RESOURCE_PACK:
            case START:
            case TITLE:
            case TRIGGER:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isProtected() {
        switch (this) {
            case BLOCK:
            case BOSS_SHOP:
            case CLASSES:
            case END:
            case FLOOR:
            case INTERACT:
            case LEAVE:
            case READY:
            case RESOURCE_PACK:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isSetToAir() {
        switch (this) {
            case BLOCK:
            case BOSS_SHOP:
            case DUNGEON_CHEST:
            case END:
            case FL/*TODO */AG:
            case INTERACT:
            case LEAVE:
            case RESOURCE_PACK:
            case REWARD_CHEST:
            case SCRIPT:
            case WAVE:
            case CLASSES:
            case READY:
                return false;
            default:
                return true;
        }
    }

    public boolean isLegacy() {
        switch (this) {
            case CHEST:
            case EXTERNAL_MOB:
            case FLOOR:
                return true;
            default:
                return false;
        }
    }

    @Override
    public Class<? extends DungeonSign> getHandler() {
        return handler;
    }

}
