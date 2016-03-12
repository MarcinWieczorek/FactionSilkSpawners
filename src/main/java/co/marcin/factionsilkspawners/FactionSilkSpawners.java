package co.marcin.factionsilkspawners;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class FactionSilkSpawners extends JavaPlugin implements Listener {
	@Override
	public void onEnable() {
		Plugin factionsPlugin = getServer().getPluginManager().getPlugin("Factions");
		if(factionsPlugin == null || !(factionsPlugin instanceof Factions)) {
			info("Factions plugin is invalid or not found!");
			setEnabled(false);
			return;
		}

		getServer().getPluginManager().registerEvents(this, this);
		info("v" + getDescription().getVersion() + " Enabled");
	}

	@Override
	public void onDisable() {
		info("v" + getDescription().getVersion() + " Disabled");
	}

	public static void info(String string) {
		Bukkit.getLogger().info("[FactionSilkSpawners] " + string);
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();

		if(event.getBlock().getType() != Material.MOB_SPAWNER) {
			return;
		}

		if(!player.getItemInHand().getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
			return;
		}

		Faction faction = BoardColl.get().getFactionAt(PS.valueOf(event.getBlock().getLocation()));
		MPlayer mPlayer = MPlayer.get(player);
		if(faction == null || mPlayer == null) {
			return;
		}

		if(faction.equals(mPlayer.getFaction()) && !faction.equals(FactionColl.get().getSafezone()) && !faction.equals(FactionColl.get().getWarzone()) && !faction.equals(FactionColl.get().getNone())) {
			return;
		}

		event.setCancelled(false);
	}
}
