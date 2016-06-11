package net.terrocidepvp.treetwerk.hooks;

import net.milkbowl.vault.economy.Economy;
import net.terrocidepvp.treetwerk.TreeTwerk;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook {

    private TreeTwerk plugin;
    private Economy economy = null;

    public VaultHook(TreeTwerk plugin) {
        this.plugin = plugin;
    }

    public boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }

    public Economy getEconomy() {
        return economy;
    }

}
