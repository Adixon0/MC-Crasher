package cb.sektory.mccrasher;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMCCrash implements CommandExecutor {
    public CommandMCCrash(MC_Crasher mcCrasher) {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage("Niewystarczające uprawnienia!");
            return false;
        }

        if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                sender.sendMessage("Wskazany przez ciebie gracz jest offline!");
                return false;
            }

            String method = args[1];

            if (method.equalsIgnoreCase("wszystko")) {
                for (CrusherType crashType : CrusherType.values()) {
                    try {
                        CrusherUtils.crashPlayer(sender, target, crashType);
                    } catch (Exception e) {
                        sender.sendMessage("Wystąpił błąd podczas crashowania gracza!");
                        e.printStackTrace();
                        return false;
                    }
                }
                return true;
            }

            CrusherType type = CrusherType.getFromString(method.toUpperCase());

            if (type != null) {
                try {
                    CrusherUtils.crashPlayer(sender, target, type);
                } catch (Exception e) {
                    sender.sendMessage("Wystąpił błąd podczas crashowania gracza!");
                    e.printStackTrace();
                    return false;
                }
                return true;
            } else {
                sender.sendMessage("Metoda " + method + " nie istnieje!");
                return false;
            }


        } else {
            sender.sendMessage("Użycie: /crash <gracz> <eksplozja/pozycja/wszystko>!");
        }

        return true;
    }
}
