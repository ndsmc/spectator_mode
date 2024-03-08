package ru.ndsmc.spectatormode.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.ndsmc.spectatormode.SpectatorManager;
import ru.ndsmc.spectatormode.SpectatorMode;
import ru.ndsmc.spectatormode.util.Messenger;

@CommandAlias("s|sm")
public class SpectatorCommand extends BaseCommand {
    private final SpectatorManager spectatorManager;
    private final SpectatorMode plugin;

    public SpectatorCommand(SpectatorMode plugin) {
        this.plugin = plugin;
        this.spectatorManager = plugin.getSpectatorManager();
    }

    @Default
    @Description("Toggle spectator mode")
    @CommandPermission("spectatormode.use")
    public void defaultCommand(Player player) {
        spectatorManager.togglePlayer(player);
    }

    @Subcommand("enable")
    @Description("Enables the /s command")
    @CommandPermission("spectatormode.enable")
    public void enableCommand(CommandSender sender) {
        spectatorManager.setSpectatorEnabled(true);
        Messenger.send(sender, "enable-message");
    }

    @Subcommand("disable")
    @Description("Disables the /s command")
    @CommandPermission("spectatormode.enable")
    public void disableCommand(CommandSender sender) {
        spectatorManager.setSpectatorEnabled(false);
        Messenger.send(sender, "disable-message");
    }

    @Subcommand("reload")
    @Description("Reloads the config and data file")
    @CommandPermission("spectatormode.reload")
    public void reloadCommand(CommandSender sender) {
        plugin.reloadConfigManager();
        spectatorManager.getStateHolder().load();
        Messenger.send(sender, "reload-message");
    }

    @Subcommand("effect")
    @Description("Toggles your spectator effects if enabled")
    @CommandPermission("spectatormode.toggle")
    public void toggleEffectsCommand(Player player) {
        spectatorManager.togglePlayerEffects(player);
    }

    @Subcommand("force")
    @Description("Forces a player into spectator mode")
    @CommandPermission("spectatormode.force")
    @CommandCompletion("@players")
    public void forcePlayerCommand(CommandSender sender, String targetName) {
        Player target = plugin.getServer().getPlayer(targetName);
        if (target != null) {
            spectatorManager.togglePlayer(target, true);
            Messenger.send(sender, "force-success", target);
        } else {
            Messenger.send(sender, "invalid-player-message");
        }
    }
}
