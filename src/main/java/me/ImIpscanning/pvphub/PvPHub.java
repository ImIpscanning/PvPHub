package me.ImIpscanning.pvphub;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PvPHub extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(this, this);

        // Aplicar modo al iniciar el servidor
        applyModeToAll();

        getLogger().info("PvPHub activado");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("pvphub")) {

            // Solo OP
            if (!sender.isOp()) {
                sender.sendMessage("§cNo tienes permiso");
                return true;
            }

            if (args.length == 0) {
                sender.sendMessage("§aUsa: /pvphub <trigger|semispam|spam>");
                return true;
            }

            String mode = args[0].toLowerCase();

            if (!getConfig().contains("modes." + mode)) {
                sender.sendMessage("§cModo inválido");
                return true;
            }

            // Guardar modo
            getConfig().set("current-mode", mode);
            saveConfig();

            // Aplicar a todos
            applyModeToAll();

            Bukkit.broadcastMessage("§7Modo PvP cambiado a: §e" + mode);
            return true;
        }

        return false;
    }

    // Aplicar modo a todos los jugadores online
    private void applyModeToAll() {
        String mode = getConfig().getString("current-mode", "semispam");
        double speed = getConfig().getDouble("modes." + mode + ".attack-speed");

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getAttribute(Attribute.GENERIC_ATTACK_SPEED) != null) {
                p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(speed);
            }
        }
    }

    // Aplicar modo al entrar
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        String mode = getConfig().getString("current-mode", "semispam");
        double speed = getConfig().getDouble("modes." + mode + ".attack-speed");

        Player p = e.getPlayer();

        if (p.getAttribute(Attribute.GENERIC_ATTACK_SPEED) != null) {
            p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(speed);
        }
    }
}
