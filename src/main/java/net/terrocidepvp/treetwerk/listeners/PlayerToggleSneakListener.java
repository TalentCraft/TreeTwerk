package net.terrocidepvp.treetwerk.listeners;

import net.terrocidepvp.treetwerk.TreeTwerk;
import net.terrocidepvp.treetwerk.configurations.ConfigManager;
import net.terrocidepvp.treetwerk.utils.ExpUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.Optional;
import java.util.Random;

public class PlayerToggleSneakListener implements Listener {

    private TreeTwerk plugin;
    private ConfigManager configManager;
    private Random random;

    public PlayerToggleSneakListener(TreeTwerk plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        random = new Random();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onToggleSneak(PlayerToggleSneakEvent event) {
        if (event.isCancelled() || !event.isSneaking()) return;

        Player player = event.getPlayer();

        if (!player.hasPermission("treetwerk.use"))
            return;

        World world = player.getWorld();
        Location playerLocation = player.getLocation();
        int ox = playerLocation.getBlockX();
        int oy = playerLocation.getBlockY();
        int oz = playerLocation.getBlockZ();

        Optional<Block> sapling = Optional.empty();
        for (int cx = -configManager.getBase().getRadiusX(); cx <= configManager.getBase().getRadiusX(); cx++) {
            if (sapling.isPresent()) break;

            for (int cy = -configManager.getBase().getRadiusY(); cy <= configManager.getBase().getRadiusY(); cy++) {
                if (sapling.isPresent()) break;

                for (int cz = -configManager.getBase().getRadiusZ(); cz <= configManager.getBase().getRadiusZ(); cz++) {
                    Optional<Block> block = Optional.ofNullable(world.getBlockAt(ox + cx, oy + cy, oz + cz));
                    if (!block.isPresent()) continue;

                    if (block.get().getType() == Material.SAPLING) {
                        sapling = block;
                        break;
                    }
                }
            }
        }
        if (!sapling.isPresent()) return;

        int playerRandom = random.nextInt(configManager.getBase().getOutOf());

        Location saplingLocation = sapling.get().getLocation();

        if (configManager.getBase().isUseParticleEffect()) {
            world.spigot().playEffect(saplingLocation.add(0.5, 0.5, 0.5), Effect.HAPPY_VILLAGER,
                    // Particle info
                    0, 0, 0.25F, 0.25F, 0.25F, 1, 16, 16);
        }

        if (configManager.getEconomy().isEnabled()) {
            switch (configManager.getEconomy().getMode()) {
                case "exp":
                    if (ExpUtil.getTotalExperience(player) - configManager.getEconomy().getAmount() < 0) {
                        configManager.getMessages().getNotEnough().forEach(player::sendMessage);
                        return;
                    }
                    break;
                case "level":
                    if (player.getLevel() - configManager.getEconomy().getAmount() < 0) {
                        configManager.getMessages().getNotEnough().forEach(player::sendMessage);
                        return;
                    }
                    break;
                case "money":
                    if (!plugin.isUsingVault()) {
                        plugin.getLogger().severe("You can't charge money if Vault is not enabled!");
                        break;
                    }
                    if (plugin.getVaultHook().getEconomy().getBalance(player) - configManager.getEconomy().getAmount() < 0) {
                        configManager.getMessages().getNotEnough().forEach(player::sendMessage);
                        return;
                    }
                    break;
            }
        }

        if (playerRandom != 1)
            return;

        int originalCurrency = 0;
        if (configManager.getEconomy().isEnabled()) {
            switch (configManager.getEconomy().getMode()) {
                case "exp":
                    originalCurrency = ExpUtil.getTotalExperience(player);
                    int finalExp = originalCurrency - configManager.getEconomy().getAmount();
                    ExpUtil.setTotalExperience(player, finalExp);
                    break;
                case "level":
                    originalCurrency = player.getLevel();
                    int finalLevel = originalCurrency - configManager.getEconomy().getAmount();
                    player.setLevel(finalLevel);
                    break;
                case "money":
                    if (!plugin.isUsingVault()) {
                        break;
                    }
                    plugin.getVaultHook().getEconomy().withdrawPlayer(player, configManager.getEconomy().getAmount());
                    break;
            }
        }

        byte saplingData = sapling.get().getData();
        Material saplingType = sapling.get().getType();
        TreeType saplingTreeType;
        switch (saplingData) {
            case 0:
                saplingTreeType = TreeType.TREE;
                break;
            case 1:
                saplingTreeType = TreeType.REDWOOD;
                break;
            case 2:
                saplingTreeType = TreeType.BIRCH;
                break;
            case 3:
                saplingTreeType = TreeType.JUNGLE;
                break;
            case 4:
                saplingTreeType = TreeType.ACACIA;
                break;
            case 5:
                saplingTreeType = TreeType.DARK_OAK;
                break;
            case 8:
                saplingTreeType = TreeType.TREE;
                break;
            default:
                plugin.getLogger().severe("There is a new tree type which the plugin can't recognise! " +
                        "Please inform the developer so it can be added in.");
                return;
        }

        sapling.get().setType(Material.AIR);
        if (!world.generateTree(saplingLocation, saplingTreeType)) {
            world.getBlockAt(saplingLocation).setTypeIdAndData(saplingType.getId(), saplingData, false);
            if (configManager.getEconomy().isEnabled()) {
                switch (configManager.getEconomy().getMode()) {
                    case "exp":
                        ExpUtil.setTotalExperience(player, originalCurrency);
                        break;
                    case "level":
                        player.setLevel(originalCurrency);
                        break;
                    case "money":
                        if (!plugin.isUsingVault()) {
                            break;
                        }
                        plugin.getVaultHook().getEconomy().depositPlayer(player, configManager.getEconomy().getAmount());
                        break;
                }
            }
        } else configManager.getMessages().getSuccess().forEach(player::sendMessage);
    }

}
