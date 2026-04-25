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

        // 🔥 asegurar que exista current-mode
        if (!getConfig().contains("current-mode")) {
            getConfig().set("current-mode", "semispam");
            saveConfig();
        }

        Bukkit.getPluginManager().registerEvents(this, this);

        // 🔥 aplicar modo al iniciar
        applyModeToAll();

        getLogger().info("PvPHub activado correctamente");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!cmd.getName().equalsIgnoreCase("pvphub")) return false;

        // 🔒 solo OP
        if (!sender.isOp()) {
            sender.sendMessage("§cNo tienes permiso");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("§aUsa: /pvphub <trigger|semispam|spam>");
            return true;
        }

        String mode = args[0].toLowerCase();

        // 🔥 validar modo
        if (!getConfig().contains("modes." + mode)) {
            sender.sendMessage("§cModo inválido");
            return true;
        }

        // 💾 guardar modo
        getConfig().set("current-mode", mode);
        saveConfig();

        // 🔥 aplicar a todos
        applyModeToAll();

        Bukkit.broadcastMessage("§7Modo PvP cambiado a: §e" + mode);
        return true;
    }

    // 🌎 aplicar modo global
    private void applyModeToAll() {
        String mode = getConfig().getString("current-mode");

        if (mode == null || !getConfig().contains("modes." + mode)) {
            getLogger().warning("Modo inválido, usando semispam");
            mode = "semispam";
        }

        double speed = getConfig().getDouble("modes." + mode + ".attack-speed", 6.5);

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getAttribute(Attribute.GENERIC_ATTACK_SPEED) != null) {
                p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(speed);
            }
        }
    }

    // 👤 cuando entra un jugador
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        String mode = getConfig().getString("current-mode");

        if (mode == null || !getConfig().contains("modes." + mode)) {
            mode = "semispam";
        }

        double speed = getConfig().getDouble("modes." + mode + ".attack-speed", 6.5);

        Player p = e.getPlayer();

        if (p.getAttribute(Attribute.GENERIC_ATTACK_SPEED) != null) {
            p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(speed);
        }
    }
}
