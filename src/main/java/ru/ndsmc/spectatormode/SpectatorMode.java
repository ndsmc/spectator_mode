/*
 * MIT License
 *
 * Copyright (c) 2021 carelesshippo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN
 */

package ru.ndsmc.spectatormode;

import co.aikar.commands.PaperCommandManager;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.jetbrains.annotations.NotNull;
import ru.ndsmc.spectatormode.commands.SpectatorCommand;
import ru.ndsmc.spectatormode.context.SpectatorContextCalculator;
import ru.ndsmc.spectatormode.listener.OnCommandPreprocessListener;
import ru.ndsmc.spectatormode.listener.OnGameModeChangeListener;
import ru.ndsmc.spectatormode.listener.OnLogOnListener;
import ru.ndsmc.spectatormode.listener.OnMoveListener;
import ru.ndsmc.spectatormode.util.ConfigManager;
import ru.ndsmc.spectatormode.util.Logger;
import ru.ndsmc.spectatormode.util.Messenger;

import java.io.File;
import java.util.Map;

public class SpectatorMode extends JavaPlugin {

    private final boolean unitTest;
    private SpectatorManager spectatorManager;
    private ConfigManager config;
    private Logger pluginLogger;

    public SpectatorMode() {
        super();
        unitTest = false;
    }

    protected SpectatorMode(
            JavaPluginLoader loader,
            PluginDescriptionFile description,
            File dataFolder,
            File file) {
        super(loader, description, dataFolder, file);
        unitTest = true;
    }

    public SpectatorManager getSpectatorManager() {
        return spectatorManager;
    }

    public boolean isUnitTest() {
        return unitTest;
    }

    @Override
    public void onEnable() {
        config = new ConfigManager(this, this.getConfig());
        pluginLogger = new Logger(this);
        spectatorManager = new SpectatorManager(this);
        registerCommands();
        if (!unitTest) {
            addMetrics();
            initializeLuckPermsContext();
        }

        Messenger.init(this);
        registerListeners();
    }

    private void initializeLuckPermsContext() {
        try {
            Class.forName("net.luckperms.api.LuckPerms");
        } catch (ClassNotFoundException ignored) {
            pluginLogger.debugLog("LuckPerms class not found");
            return;
        }
        if (!getServer().getPluginManager().isPluginEnabled("LuckPerms")) {
            pluginLogger.debugLog("LuckPerms not enabled");
            return;
        }
        SpectatorContextCalculator.initializeSpectatorContext(this);
    }

    private void addMetrics() {
        Metrics metrics = new Metrics(this, 21272);
        for (Map.Entry<String, String> entry : config.getAllBooleansAndNumbers().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            metrics.addCustomChart(new SimplePie(key + "_chart_id", () -> value));
        }
    }

    public void registerCommands() {
        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new SpectatorCommand(this));
    }

    public void registerListeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new OnMoveListener(this), this);
        pm.registerEvents(new OnLogOnListener(this), this);
        pm.registerEvents(new OnCommandPreprocessListener(this), this);
        pm.registerEvents(new OnGameModeChangeListener(this), this);
    }

    @NotNull
    public ConfigManager getConfigManager() {
        return config;
    }

    public void setConfigManagerConfigFile(FileConfiguration fileConfiguration) {
        config = new ConfigManager(this, fileConfiguration);
    }

    public void reloadConfigManager() {
        this.reloadConfig();
        config = new ConfigManager(this, this.getConfig());
    }

    public Logger getPluginLogger() {
        return pluginLogger;
    }
}
