package net.terrocidepvp.treetwerk.configurations;

import net.terrocidepvp.treetwerk.TreeTwerk;
import net.terrocidepvp.treetwerk.utils.ColorCodeUtil;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private Base base;
    private Messages messages;
    private Economy economy;

    private ConfigManager(TreeTwerk plugin) {
        FileConfiguration config = plugin.getConfig();

        base = new Base(
                config.getInt("out-of"),
                config.getBoolean("use-particle-effect"),
                config.getInt("radius.x"),
                config.getInt("radius.y"),
                config.getInt("radius.z")
        );

        messages = new Messages(
                ColorCodeUtil.translate(config.getStringList("plugin-messages.success")),
                ColorCodeUtil.translate(config.getStringList("plugin-messages.not-enough"))
        );

        String economyMode = config.getString("economy.amount.mode");
        switch (economyMode) {
            case "exp":
                economy = new Economy(true,
                        "exp",
                        config.getInt("economy.amount.exp"));
                break;
            case "level":
                economy = new Economy(true,
                        "level",
                        config.getInt("economy.amount.level"));
                break;
            case "money":
                economy = new Economy(true,
                        "money",
                        config.getInt("economy.amount.money"));
                break;
            default:
                plugin.getLogger().severe("Invalid config option for 'economy.amount.mode'! " +
                        "You can only choose 'exp', 'level' or 'money'. " +
                        "Skipping usage of economy.");
                economy = new Economy(false, "", 0);
                break;
        }
    }

    public static ConfigManager createConfigManager(TreeTwerk plugin) {
        return new ConfigManager(plugin);
    }

    public Base getBase() {
        return base;
    }

    public Messages getMessages() {
        return messages;
    }

    public Economy getEconomy() {
        return economy;
    }

}
