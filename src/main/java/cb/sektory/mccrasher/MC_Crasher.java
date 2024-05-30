package cb.sektory.mccrasher;

import org.bukkit.plugin.java.JavaPlugin;

public final class MC_Crasher extends JavaPlugin {

    @Override
    public void onEnable() {
        if (getServer().getPluginManager().getPlugin("ProtocolLib") == null) {
            getLogger().severe("ProtocolLib is not installed or not enabled! Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.getCommand("mccrash").setExecutor(new CommandMCCrash(this));
        this.getCommand("pingplayer").setExecutor(new CommandPingPlayer(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
