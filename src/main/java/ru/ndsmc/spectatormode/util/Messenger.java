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

package ru.ndsmc.spectatormode.util;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.ndsmc.spectatormode.SpectatorMode;

import java.util.Objects;

public class Messenger {

    private static SpectatorMode plugin;

    private Messenger() {
    }

    public static void init(@NotNull SpectatorMode plugin) {
        Messenger.plugin = plugin;
    }

    public static void send(@NotNull CommandSender sender, @NotNull String msgkey) {
        send(sender, msgkey, sender, "");
    }

    public static void send(
            @NotNull CommandSender sender, @NotNull String msgkey, @NotNull CommandSender target) {
        send(sender, msgkey, target, "");
    }

    public static void send(
            @NotNull CommandSender sender, @NotNull String msgkey, @NotNull String extra) {
        send(sender, msgkey, sender, extra);
    }

    public static void send(
            @NotNull CommandSender sender,
            @NotNull String msgkey,
            @NotNull CommandSender target,
            @NotNull String extra) {
        String cfgmsg =
                Objects.requireNonNull(plugin.getConfigManager(), "Messenger not initialized")
                        .getColorizedString(msgkey)
                        .replace("/target/", target.getName());
        ChatMessageType type =
                cfgmsg.startsWith("/actionbar/")
                        ? ChatMessageType.ACTION_BAR
                        : ChatMessageType.CHAT;
        cfgmsg = cfgmsg.replace("/actionbar/", "");
        cfgmsg += extra;
        if (sender instanceof Player) {
            ((Player) sender).spigot().sendMessage(type, new TextComponent(cfgmsg));
        } else {
            sender.sendMessage(cfgmsg);
        }
    }
}
