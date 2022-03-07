package com.pdev.drduels.api.player;

import java.util.UUID;

import com.pdev.drduels.utils.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class DuelPlayer {
    private UUID uuid;
    private Location lastLocation;
    private ItemStack[] contents;
    private ItemStack[] armor;
    private GameMode gameMode;

    public DuelPlayer(Player player) {
        this.uuid = player.getUniqueId();
        this.lastLocation = player.getLocation();
        this.armor = player.getInventory().getArmorContents();
        this.contents = player.getInventory().getContents();
        this.gameMode = player.getGameMode();
    }

    public UUID getUuid() {
        return uuid;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public ItemStack[] getArmor() {
        return armor;
    }

    public ItemStack[] getContents() {
        return contents;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public boolean isLoser(UUID uuid) {
        if (this.uuid.equals(uuid)) {
            return true;
        }

        return false;
    }

    public void resetAfterDuel(boolean lost) {
        Player player = Bukkit.getPlayer(uuid);

        if (player == null) {
            return;
        }

        // Teleport & Inventory
        player.teleportAsync(lastLocation, TeleportCause.PLUGIN);
        player.getInventory().clear();
        player.getInventory().setContents(contents);
        player.getInventory().setArmorContents(armor);
        player.setGameMode(gameMode);

        // Messaging
        if (lost) {
            player.sendMessage(StringUtils.colorize("&cYou have lost the duel!"));
        } else {
            player.sendMessage(StringUtils.colorize("&aYou have won the duel!"));
        }
    }

    public void sendWaitingMessage() {
        Player player = Bukkit.getPlayer(uuid);

        if (player != null) {
            player.sendMessage(StringUtils.colorize(
                    "&7You are currently in the queue to duel, once it is your turn you will be teleported!"));
        }
    }

    // Suppressing deprecation here so that I can use .spigot() to send action bars.
    // I apologize in advance that I did not do this with NMS
    @SuppressWarnings("deprecation")
    public void sendTimeMessage(int seconds) {
        Player player = Bukkit.getPlayer(uuid);

        if (player != null) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    new TextComponent(StringUtils.colorize("&fTime Elapsed: &e" + seconds + " seconds")));
        }
    }
}
