package me.ImIpscanning.pvphub;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PvPHub extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("PvPHub activado");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("pvphub")) {

            if (args.length == 0) {
                p.sendMessage("§aModos: trigger | semispam | spam");
                return true;
            }

            switch (args[0].toLowerCase()) {

                case "trigger":
                    setAttackSpeed(p, 4.0);
                    p.sendMessage("§aModo Trigger (~4 CPS)");
                    break;

                case "semispam":
                    setAttackSpeed(p, 6.0);
                    p.sendMessage("§eModo SemiSpam (~6 CPS)");
                    break;

                case "spam":
                    setAttackSpeed(p, 20.0);
                    p.sendMessage("§cModo Spam (sin cooldown)");
                    break;

                default:
                    p.sendMessage("§cModo inválido");
                    break;
            }
        }

        return true;
    }

    private void setAttackSpeed(Player p, double speed) {
        p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(speed);
    }
}
