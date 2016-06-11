package net.terrocidepvp.treetwerk;

import net.terrocidepvp.treetwerk.configurations.ConfigManager;
import net.terrocidepvp.treetwerk.hooks.VaultHook;
import net.terrocidepvp.treetwerk.listeners.PlayerToggleSneakListener;
import org.bukkit.plugin.java.JavaPlugin;

public class TreeTwerk extends JavaPlugin {

    private static TreeTwerk instance;
    public static TreeTwerk getInstance() {
        return instance;
    }

    private ConfigManager configManager;
    private VaultHook vaultHook;
    private boolean usingVault;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        reloadConfig();

        if (!getConfig().isSet("config-version")) {
            getLogger().severe("The config.yml file is broken!");
            getLogger().severe("The plugin failed to detect a 'config-version'.");
            getLogger().severe("The plugin will not load until you generate a new, working config OR if you fix the config.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        int configVersion = 4;
        /*
         Updated for: Made config easier as an object
         Release: v1.0.0 for Spigot release
        */
        if (getConfig().getInt("config-version") != configVersion) {
            getLogger().severe("Your config is outdated!");
            getLogger().severe("The plugin will not load unless you change the config version to " + configVersion + ".");
            getLogger().severe("This means that you will need to reset your config, as there may have been major changes to the plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        configManager = ConfigManager.createConfigManager(this);

        if (getServer().getPluginManager().isPluginEnabled("Vault")) {
            getLogger().info("Vault is enabled - attempting to hook...");
            vaultHook = new VaultHook(this);
            if (!vaultHook.setupEconomy()) {
                getLogger().severe("You have Vault but no economy plugin! Money as an economy mode will not work.");
                usingVault = false;
            }
            usingVault = true;
        } else {
            getLogger().severe("Vault was not found! Money as an economy mode will not work.");
            usingVault = false;
        }

        new PlayerToggleSneakListener(this, getConfigManager());
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public boolean isUsingVault() {
        return usingVault;
    }

    public VaultHook getVaultHook() {
        return vaultHook;
    }
}
