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
package de.erethon.dungeonsxl;

import de.erethon.caliburn.CaliburnAPI;
import de.erethon.caliburn.loottable.LootTable;
import de.erethon.commons.compatibility.Internals;
import de.erethon.commons.compatibility.Version;
import de.erethon.commons.javaplugin.DREPlugin;
import de.erethon.commons.javaplugin.DREPluginSettings;
import de.erethon.commons.misc.FileUtil;
import de.erethon.commons.misc.Registry;
import de.erethon.commons.spiget.comparator.VersionComparator;
import de.erethon.dungeonsxl.adapter.block.BlockAdapter;
import de.erethon.dungeonsxl.adapter.block.BlockAdapterBlockData;
import de.erethon.dungeonsxl.adapter.block.BlockAdapterMagicValues;
import de.erethon.dungeonsxl.announcer.AnnouncerCache;
import de.erethon.dungeonsxl.api.DungeonsAPI;
import de.erethon.dungeonsxl.api.Requirement;
import de.erethon.dungeonsxl.api.Reward;
import de.erethon.dungeonsxl.api.Trigger;
import de.erethon.dungeonsxl.api.dungeon.Dungeon;
import de.erethon.dungeonsxl.api.dungeon.GameRule;
import de.erethon.dungeonsxl.api.mob.ExternalMobProvider;
import de.erethon.dungeonsxl.api.player.GlobalPlayer;
import de.erethon.dungeonsxl.api.player.PlayerClass;
import de.erethon.dungeonsxl.api.sign.DungeonSign;
import de.erethon.dungeonsxl.api.world.InstanceWorld;
import de.erethon.dungeonsxl.api.world.ResourceWorld;
import de.erethon.dungeonsxl.command.DCommandCache;
import de.erethon.dungeonsxl.config.MainConfig;
import de.erethon.dungeonsxl.dungeon.DungeonCache;
import de.erethon.dungeonsxl.game.GameTypeCache;
import de.erethon.dungeonsxl.global.GlobalData;
import de.erethon.dungeonsxl.global.GlobalProtectionCache;
import de.erethon.dungeonsxl.global.GlobalProtectionListener;
import de.erethon.dungeonsxl.mob.DMobListener;
import de.erethon.dungeonsxl.mob.DMobType;
import de.erethon.dungeonsxl.mob.ExternalMobProviderCache;
import de.erethon.dungeonsxl.player.DClassCache;
import de.erethon.dungeonsxl.player.DPermission;
import de.erethon.dungeonsxl.player.DPlayerCache;
import de.erethon.dungeonsxl.requirement.*;
import de.erethon.dungeonsxl.reward.*;
import de.erethon.dungeonsxl.sign.DSignTypeCache;
import de.erethon.dungeonsxl.sign.SignScriptCache;
import de.erethon.dungeonsxl.trigger.TriggerListener;
import de.erethon.dungeonsxl.trigger.TriggerTypeCache;
import de.erethon.dungeonsxl.util.PlaceholderUtil;
import de.erethon.dungeonsxl.world.DWorldCache;
import de.erethon.vignette.api.VignetteAPI;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;

/**
 * @author Frank Baumann, Tobias Schmitz, Daniel Saukel
 */
public class DungeonsXL extends DREPlugin implements DungeonsAPI {

    private static DungeonsXL instance;
    private CaliburnAPI caliburn;

    public static final BlockAdapter BLOCK_ADAPTER = Version.isAtLeast(Version.MC1_13) ? new BlockAdapterBlockData() : new BlockAdapterMagicValues();

    public static final String[] EXCLUDED_FILES = {"config.yml", "uid.dat", "DXLData.data", "data"};
    public static final File ANNOUNCERS = new File(SCRIPTS, "announcers");
    public static final File LOOT_TABLES = new File(SCRIPTS, "loottables");
    public static final File MOBS = new File(SCRIPTS, "mobs");
    public static final File SIGNS = new File(SCRIPTS, "signs");

    private Registry<UUID, GlobalPlayer> playerCache = new Registry<>();
    private Registry<String, PlayerClass> classRegistry = new Registry<>();
    private Registry<String, Class<? extends DungeonSign>> signRegistry = new Registry<>();
    private Registry<String, Class<? extends Trigger>> triggerRegistry = new Registry<>();
    private Registry<String, Class<? extends Requirement>> requirementRegistry = new Registry<>();
    private Registry<String, Class<? extends Reward>> rewardRegistry = new Registry<>();
    private Registry<String, Dungeon> dungeonRegistry = new Registry<>();
    private Registry<String, ResourceWorld> mapRegistry = new Registry<>();
    private Registry<Integer, InstanceWorld> instanceRegistry = new Registry<>();
    private Registry<String, GameRule> gameRuleRegistry = new Registry<>();
    private Registry<String, ExternalMobProvider> externalMobProviderRegistry = new Registry<>();

    private boolean loadingWorld;

    private GlobalData globalData;
    private MainConfig mainConfig;

    private DCommandCache dCommands;
    private GameTypeCache gameTypes;
    private TriggerTypeCache triggers;
    private GlobalProtectionCache protections;
    private AnnouncerCache announcers;
    private SignScriptCache signScripts;

    public DungeonsXL() {
        settings = DREPluginSettings.builder()
                .internals(Internals.andHigher(Internals.v1_8_R1))
                .economy(true)
                .permissions(true)
                .metrics(true)
                .spigotMCResourceId(9488)
                .versionComparator(VersionComparator.EQUAL)
                .build();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;
        initFolders();
        loadCaliburnAPI();
        DPermission.register();
        loadConfig();
        createCaches();
        initCaches();
        loadData();
        if (manager.isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderUtil(this, "dxl").register();
        }
        VignetteAPI.init(this);
    }

    @Override
    public void onDisable() {
        saveData();
        getGroupCache().clear();
        deleteAllInstances();
        HandlerList.unregisterAll(this);
        getServer().getScheduler().cancelTasks(this);
    }

    // Init.
    public void initFolders() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        BACKUPS.mkdir();
        MAPS.mkdir();
        PLAYERS.mkdir();
        SCRIPTS.mkdir();
        ANNOUNCERS.mkdir();
        CLASSES.mkdir();
        DUNGEONS.mkdir();
        LOOT_TABLES.mkdir();
        MOBS.mkdir();
        SIGNS.mkdir();
    }

    public void loadConfig() {
        mainConfig = new MainConfig(this, new File(getDataFolder(), "config.yml"));
    }

    public void createCaches() {
        gameTypes = new GameTypeCache();

        requirementRegistry.add("feeLevel", FeeLevelRequirement.class);
        requirementRegistry.add("feeMoney", FeeMoneyRequirement.class);
        requirementRegistry.add("forbiddenItems", ForbiddenItemsRequirement.class);
        requirementRegistry.add("groupSize", GroupSizeRequirement.class);
        requirementRegistry.add("keyItems", KeyItemsRequirement.class);
        requirementRegistry.add("permission", PermissionRequirement.class);

        rewardRegistry.add("item", ItemReward.class);
        rewardRegistry.add("money", MoneyReward.class);
        rewardRegistry.add("level", LevelReward.class);

        triggers = new TriggerTypeCache();
        dSigns = new DSignTypeCache(this);
        dWorlds = new DWorldCache(this);
        dungeons = new DungeonCache(this);
        protections = new GlobalProtectionCache(this);
        dMobProviders = new ExternalMobProviderCache(this);
        dPlayers = new DPlayerCache(this);
        announcers = new AnnouncerCache(this);
        dClasses = new DClassCache(this);
        signScripts = new SignScriptCache();
        dCommands = new DCommandCache(this);
    }

    public void initCaches() {
        // Game types
        // Requirements
        Bukkit.getPluginManager().registerEvents(new RewardListener(this), this);
        Bukkit.getPluginManager().registerEvents(new TriggerListener(this), this);
        dSigns.init();
        dWorlds.init(MAPS);
        dungeons.init(DUNGEONS);
        Bukkit.getPluginManager().registerEvents(new GlobalProtectionListener(this), this);
        globalData = new GlobalData(this, new File(getDataFolder(), "data.yml"));
        globalData.load();
        dMobProviders.init();
        dPlayers.init();
        announcers.init(ANNOUNCERS);
        dClasses.init(CLASSES);
        Bukkit.getPluginManager().registerEvents(new DMobListener(), this);
        signScripts.init(SIGNS);
        dCommands.register(this);
    }

    // Save and load
    public void saveData() {
        protections.saveAll();
        dWorlds.saveAll();
    }

    public void loadData() {
        dPlayers.loadAll();
        dWorlds.check();
    }

    /* Getters and loaders */
    /**
     * @return the plugin instance
     */
    public static DungeonsXL getInstance() {
        return instance;
    }

    /**
     * @return the loaded instance of CaliburnAPI
     */
    public CaliburnAPI getCaliburn() {
        return caliburn;
    }

    private void loadCaliburnAPI() {
        caliburn = CaliburnAPI.getInstance() == null ? new CaliburnAPI(this) : CaliburnAPI.getInstance();
        if (LOOT_TABLES.isDirectory()) {
            FileUtil.getFilesForFolder(LOOT_TABLES).forEach(s -> new LootTable(caliburn, s));
        }
        if (MOBS.isDirectory()) {
            FileUtil.getFilesForFolder(MOBS).forEach(s -> caliburn.getExMobs().add(new DMobType(this, s)));
        }
    }

    @Override
    public Registry<UUID, GlobalPlayer> getPlayerCache() {
        return playerCache;
    }

    @Override
    public Registry<String, PlayerClass> getClassRegistry() {
        return classRegistry;
    }

    @Override
    public Registry<String, Class<? extends DungeonSign>> getSignRegistry() {
        return signRegistry;
    }

    @Override
    public Registry<String, Class<? extends Trigger>> getTriggerRegistry() {
        return triggerRegistry;
    }

    @Override
    public Registry<String, Class<? extends Requirement>> getRequirementRegistry() {
        return requirementRegistry;
    }

    @Override
    public Registry<String, Class<? extends Reward>> getRewardRegistry() {
        return rewardRegistry;
    }

    @Override
    public Registry<String, Dungeon> getDungeonRegistry() {
        return dungeonRegistry;
    }

    @Override
    public Registry<String, ResourceWorld> getMapRegistry() {
        return mapRegistry;
    }

    @Override
    public Registry<Integer, InstanceWorld> getInstanceRegistry() {
        return instanceRegistry;
    }

    @Override
    public Registry<String, GameRule> getGameRuleRegistry() {
        return gameRuleRegistry;
    }

    @Override
    public Registry<String, ExternalMobProvider> getExternalMobProviderRegistry() {
        return externalMobProviderRegistry;
    }

    /**
     * Returns true if the plugin is currently loading a world, false if not.
     * <p>
     * If the plugin is loading a world, it is locked in order to prevent loading two at once.
     *
     * @return true if the plugin is currently loading a world, false if not
     */
    public boolean isLoadingWorld() {
        return loadingWorld;
    }

    /**
     * Notifies the plugin that a world is being loaded.
     * <p>
     * If the plugin is loading a world, it is locked in order to prevent loading two at once.
     *
     * @param loadingWorld if a world is being loaded
     */
    public void setLoadingWorld(boolean loadingWorld) {
        this.loadingWorld = loadingWorld;
    }

    /**
     * @return the loaded instance of GlobalData
     */
    public GlobalData getGlobalData() {
        return globalData;
    }

    @Override
    public DCommandCache getCommandCache() {
        return dCommands;
    }

    /**
     * @return the loaded instance of MainConfig
     */
    public MainConfig getMainConfig() {
        return mainConfig;
    }

    /**
     * @return the game types
     */
    public GameTypeCache getGameTypeCache() {
        return gameTypes;
    }

    /**
     * @return the triggers
     */
    public TriggerTypeCache getTriggerCache() {
        return triggers;
    }

    /**
     * @return the loaded instance of GlobalProtectionCache
     */
    public GlobalProtectionCache getGlobalProtectionCache() {
        return protections;
    }

    /**
     * @return the loaded instance of AnnouncerCache
     */
    public AnnouncerCache getAnnouncerCache() {
        return announcers;
    }

    /**
     * @return the loaded instance of SignScriptCache
     */
    public SignScriptCache getSignScriptCache() {
        return signScripts;
    }

    @Deprecated
    private Set<Inventory> guis = new HashSet<>();

    @Deprecated
    public Set<Inventory> getGUIs() {
        return guis;
    }

}
