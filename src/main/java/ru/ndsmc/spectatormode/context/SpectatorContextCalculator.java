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

package ru.ndsmc.spectatormode.context;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.context.ContextCalculator;
import net.luckperms.api.context.ContextConsumer;
import net.luckperms.api.context.ContextSet;
import net.luckperms.api.context.ImmutableContextSet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;
import ru.ndsmc.spectatormode.SpectatorMode;

public class SpectatorContextCalculator implements ContextCalculator<Player> {

    private static final String CONTEXT_NAME = "SpectatorMode";
    private final SpectatorMode plugin;

    public SpectatorContextCalculator(SpectatorMode plugin) {
        this.plugin = plugin;
    }

    public static void initializeSpectatorContext(SpectatorMode plugin) {
        RegisteredServiceProvider<LuckPerms> provider =
                Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            LuckPerms api = provider.getProvider();
            api.getContextManager().registerCalculator(new SpectatorContextCalculator(plugin));
        }
    }

    @Override
    public void calculate(@NotNull Player target, ContextConsumer contextConsumer) {
        contextConsumer.accept(
                CONTEXT_NAME,
                String.valueOf(plugin.getSpectatorManager().getStateHolder().hasPlayer(target)));
    }

    @Override
    public @NotNull ContextSet estimatePotentialContexts() {
        ImmutableContextSet.Builder builder = ImmutableContextSet.builder();
        builder.add(CONTEXT_NAME, String.valueOf(true));
        builder.add(CONTEXT_NAME, String.valueOf(false));
        return builder.build();
    }
}

