package com.aim.coltonjgriswold.sorandom;

import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EventListener implements Listener {

	public final JavaPlugin plugin;
	public final Logger log = Logger.getLogger("Minecraft");
	
	
	public EventListener(final JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (player.isOp()) {
			event.setJoinMessage("Admin " + player.getDisplayName() + " Has Joined The Server!");
		}else if (!player.isOp()){
			event.setJoinMessage("Player " + player.getDisplayName() + "Has Joined The Server!");
		}
	}
	@EventHandler
	public void on(PlayerBucketEmptyEvent event) {
		if (event.getBucket() == Material.LAVA_BUCKET && !event.getPlayer().isOp()) {
			event.setCancelled(true);
			event.setCancelled(true);
		}else {
			event.setCancelled(false);
		}
	}
	@EventHandler
	public void onTntPlaced(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		org.bukkit.block.Block block = event.getBlockPlaced();
		if (!player.isOp() && block.equals(Material.TNT)) {
			event.setCancelled(true);
			player.sendMessage("Only An Operator Can Place TNT!");
		}else{
			event.setCancelled(false);
		}
	}
	@EventHandler
	public void ColorSign(SignChangeEvent event) {
		String linea = event.getLine(0);
		String lineb = event.getLine(1);
		String linec = event.getLine(2);
		String lined = event.getLine(3);
		event.setLine(0, format(linea));
		event.setLine(1, format(lineb));
		event.setLine(2, format(linec));
		event.setLine(3, format(lined));
	}
	@EventHandler
	public void ColorChat(AsyncPlayerChatEvent event) {
		String line = event.getMessage();
		event.setMessage(format(line));
	}
	public String colorize(String s){
		if(s == null) return null;
		return s.replaceAll("&([l-o0-9a-f])", "\u00A7$1");
	}
	public String format(String string) {
		String format = string;
		return colorize(format);
	}
}