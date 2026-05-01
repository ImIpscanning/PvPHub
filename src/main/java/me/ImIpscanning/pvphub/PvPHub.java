package me.ImIpscanning.pvphub;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PvPHub extends JavaPlugin implements Listener {

    // UUID fijo para el modificador, así podemos añadirlo y quitarlo sin duplicados
    private static final UUID SPEED_MODIFIER_UUID = UUID.fromString("a1b2c3d4-e5f6-7890-abcd-ef1234567890");
    private static final String SPEED_MODIFIER_NAME = "pvphub_attack_speed";

    // Bloques que activan la velocidad de ataque especial
    private static final Set<Material> SPECIAL_BLOCKS = new HashSet<>(Arrays.asList(
            Material.DIAMOND_BLOCK,
            Material.EMERALD_BLOCK,
            Material.IRON_BLOCK,
            Material.GOLD_BLOCK,
            Material.NETHERITE_BLOCK,
            Material.PACKED_ICE,
            Material.BEACON,
            Material.COPPER_BLOCK,
            Material.REDSTONE_BLOCK,
            Material.AMETHYST_BLOCK
    ));

    @Override
    public void onEnable() {
        saveDefaultConfig();

        if (!getConfig().contains("current-mode")) {
            getConfig().set("current-mode", "semispam");
            saveConfig();
        }

        Bukkit.getPluginManager().registerEvents(this, this);

        // Solo limpiar modificadores viejos que hayan quedado de recargas anteriores
        for (Player p : Bukkit.getOnlinePlayers()) {
            removeSpeedModifier(p);
            // Si ya tiene un bloque especial en mano al recargar, aplicarlo
            ItemStack hand = p.getInventory().getItemInMainHand();
            if (hand != null && SPECIAL_BLOCKS.contains(hand.getType())) {
                applySpeedForItem(p, hand);
            }
        }

        getLogger().info("PvPHub activado correctamente");
    }

    @Override
    public void onDisable() {
        // Al desactivar, restaurar velocidad normal a todos
        for (Player p : Bukkit.getOnlinePlayers()) {
            removeSpeedModifier(p);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!cmd.getName().equalsIgnoreCase("pvphub")) return false;

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

        getConfig().set("current-mode", mode);
        saveConfig();

        // Re-aplicar a todos según su item en mano
        for (Player p : Bukkit.getOnlinePlayers()) {
            applySpeedForItem(p, p.getInventory().getItemInMainHand());
        }

        Bukkit.broadcastMessage("§7Modo PvP cambiado a: §e" + mode);
        return true;
    }

    // Cuando el jugador cambia de slot / item en mano
    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent e) {
        Player p = e.getPlayer();
        ItemStack newItem = p.getInventory().getItem(e.getNewSlot());
        applySpeedForItem(p, newItem);
    }

    // Cuando un jugador entra al servidor
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        applySpeedForItem(p, p.getInventory().getItemInMainHand());
    }

    // Cuando un jugador sale, limpiar el modificador
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        removeSpeedModifier(e.getPlayer());
    }

    // ─── Lógica principal ───────────────────────────────────────────────────────

    private void applySpeedForItem(Player p, ItemStack item) {
        AttributeInstance attr = p.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
        if (attr == null) return;

        // Siempre limpiar modificador anterior
        removeSpeedModifier(p);

        boolean isSpecialBlock = item != null && SPECIAL_BLOCKS.contains(item.getType());

        if (isSpecialBlock) {
            // Velocidad del modo activo (ej: 6.5)
            String mode = getConfig().getString("current-mode", "semispam");
            if (!getConfig().contains("modes." + mode)) mode = "semispam";
            double targetSpeed = getConfig().getDouble("modes." + mode + ".attack-speed", 6.5);

            // Usamos ADD_NUMBER sobre la base vanilla (4.0) para llegar a targetSpeed
            double delta = targetSpeed - 4.0;

            AttributeModifier modifier = new AttributeModifier(
                    SPEED_MODIFIER_UUID,
                    SPEED_MODIFIER_NAME,
                    delta,
                    AttributeModifier.Operation.ADD_NUMBER
            );
            attr.addModifier(modifier);
        } else {
            // Restaurar base vanilla exacta para que hacha, espada, etc. usen su cooldown normal
            attr.setBaseValue(4.0);
        }
    }

    private void removeSpeedModifier(Player p) {
        AttributeInstance attr = p.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
        if (attr == null) return;

        attr.getModifiers().stream()
                .filter(mod -> mod.getUniqueId().equals(SPEED_MODIFIER_UUID))
                .findFirst()
                .ifPresent(attr::removeModifier);
    }
}
