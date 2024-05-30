package cb.sektory.mccrasher;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommandPingPlayer implements CommandExecutor {
    private final JavaPlugin plugin;
    private final ProtocolManager protocolManager;
    private final Map<UUID, Integer> playerPings = new HashMap<>();

    public CommandPingPlayer(JavaPlugin plugin) {
        this.plugin = plugin;
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        if (protocolManager == null) {
            plugin.getLogger().severe("ProtocolManager is null! Make sure ProtocolLib is installed and properly configured.");
            return;
        }
        registerPacketListener();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage("Niewystarczające uprawnienia!");
            return false;
        }

        if (args.length != 2) {
            sender.sendMessage("Użycie: /pingplayer <nick gracza> <wartość pingu>");
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("Gracz o podanym nicku jest offline!");
            return false;
        }

        int ping;
        try {
            ping = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage("Podaj poprawną wartość pingu!");
            return false;
        }

        playerPings.put(target.getUniqueId(), ping);
        sender.sendMessage("Ustawiono symulowany ping " + ping + "ms dla gracza " + target.getName());
        return true;
    }

    private void registerPacketListener() {
        PacketListener listener = new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.KEEP_ALIVE) {
            @Override
            public void onPacketSending(PacketEvent event) {
                Player player = event.getPlayer();
                if (player != null && playerPings.containsKey(player.getUniqueId())) {
                    int delay = playerPings.get(player.getUniqueId());
                    try {
                        Thread.sleep(delay); // Symuluje opóźnienie
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        protocolManager.addPacketListener(listener);
    }
}
