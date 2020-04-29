package me.qther.dupebull;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.AbstractHorseInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class DupeBull extends JavaPlugin implements Listener {

    List<Vector> offsets = new ArrayList<>(Arrays.asList(
            new Vector(1, 0, -1),
            new Vector(1, 0, 0),
            new Vector(1, 0, 1),
            new Vector(0, 0, -1),
            new Vector(0, 0, 0),
            new Vector(0, 0, 1),
            new Vector(-1, 0, -1),
            new Vector(-1, 0, 0),
            new Vector(-1, 0, -1)
    ));

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("DupeBull gives you dupes!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("DupeBull has drank some End Bull and flew away ;(");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage("DupeBull gives you dupes!\n Do /db or /dupebull riding a donkey or llama at half-heart \nnear a nether portal (3x3 centered on portal blocks)\nIt will kill your donkey or llama but its inventory will be dropped twice.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("dupebull") ||
                command.getName().equalsIgnoreCase("db")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.isInsideVehicle()) {
                    if (Objects.requireNonNull(player.getVehicle()) instanceof ChestedHorse) {
                        ChestedHorse chestedHorse = (ChestedHorse) player.getVehicle();

                        if (chestedHorse.getHealth() != 1) {
                            player.sendMessage("Get the chested horse to half a heart!");
                            return false;
                        }

                        if (!chestedHorse.isCarryingChest()) {
                            player.sendMessage("Donkey doesn't even have a chest!");
                            return false;
                        }

                        boolean nearPortal = false;
                        for (Vector v : offsets) {
                            nearPortal = nearPortal || chestedHorse.getWorld().getBlockAt(chestedHorse.getLocation().add(v)).getType() == Material.NETHER_PORTAL;
                        }

                        Inventory inventory = chestedHorse.getInventory();
                        if (inventory.getStorageContents().length == 0) {
                            player.sendMessage("Donkey doesn't even have items in it!");
                            return false;
                        }

                        for (ItemStack i : chestedHorse.getInventory().getStorageContents()) {
                            chestedHorse.getWorld().dropItem(chestedHorse.getLocation(), i);
                        }

                        chestedHorse.setHealth(0);
                        return true;
                    }
                }

                /*
                if (player.getActiveItem() != null && player.getActiveItem().getType() != Material.AIR) {
                    player.getWorld().dropItem(player.getLocation(), Objects.requireNonNull(player.getActiveItem()));
                    player.kickPlayer("DupeBull gives you dupes!");
                    return true;
                } else {
                    player.sendMessage("You trying to dupe air?");
                    return true;
                }
                */
            }
        }

        return false;
    }
}
