package com.aim.coltonjgriswold.sorandom;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.MetricsLite;

public class SoRandom extends JavaPlugin implements Listener {

	public static JavaPlugin plugin;
	public final Logger log = Logger.getLogger("Minecraft");
	public ChatColor a = ChatColor.AQUA;
	public ChatColor b = ChatColor.DARK_PURPLE;
	public ChatColor c = ChatColor.BOLD;
	public ChatColor d = ChatColor.GOLD;
	public ChatColor e = ChatColor.RESET;
	public ChatColor ff = ChatColor.DARK_RED;
	private FileConfiguration customConfig = null;
	private File customConfigFile = null;
	public String name;
	public String comm;
	
	public void onDisable(){
		log.info("[SoRandom] Version " + getDescription().getVersion() + " by coltonj96 " + "Has Been Disabled!");
	}
	public void onEnable(){
		Date d = new Date();
		SimpleDateFormat form = new SimpleDateFormat("MM-dd-yyyy");
		String date = form.format(d);
		log.info("[SoRandom] Version " + getDescription().getVersion() + " By coltonj96 " + "Has Been Enabled On " + date);
		try {
		    MetricsLite metrics = new MetricsLite(this);
		    metrics.start();
		} catch (IOException e) {
		    // Failed to submit the stats :-(
		}
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new EventListener(this), this);
		pm.registerEvents(this, this);
		try {
			getConfig().options().copyDefaults(true);
			getConfig().options().header("#SoRandom-Version " + getDescription().getVersion() + " Configuration");
			getConfig().options().copyHeader(true);
			saveConfig();
		} catch (Exception ex) {
			getLogger().log(Level.SEVERE, "[SoRandom] Error, Cannot Load config.yml!!", ex);
		}
		File A = new File(getDataFolder(), "data.yml");
		if (!A.exists()) {//if the file does not exist...
			saveResource("data.yml", true);//save the file as your default configuration.
			System.out.println("[SoRandom] Creating data.yml...");
		}
	}
	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String string,String[] args) {
		if (this.getConfig().getBoolean("settings.commands")) {
			if (cs instanceof Player) {
				Player player = (Player) cs;
					if (cmd.getName().equalsIgnoreCase("implode")) {
						Player tar = Bukkit.getServer().getPlayer(args[0]);
						if (args.length == 1) {
							if (tar != null){
								if (tar.isOnline()){
									Location loc = tar.getLocation();
									double y = loc.getBlockY();
									double x = loc.getBlockX();
									double z = loc.getBlockZ();
									World world = tar.getPlayer().getWorld();
									Location a = new Location(world, x, y + 2, z);
									Entity tnt = tar.getWorld().spawnEntity(a, EntityType.PRIMED_TNT);
									List<Entity> implode = tnt.getNearbyEntities(10, 10, 10);
									for (Entity e : implode) {
										e.teleport(tnt);
									}
									return true;
								}
							}
						}else{
							player.sendMessage("ERROR, To Many Paremeters Used!");
							return true;
						}
					}
					if (cmd.getName().equalsIgnoreCase("stick")) {
						Player tar = Bukkit.getServer().getPlayer(args[0]);
						if (args.length == 1) {
							if (tar != null){
								if (tar.isOnline()){
									Location loc = tar.getLocation();
									double y = loc.getBlockY();
									double x = loc.getBlockX();
									double z = loc.getBlockZ();
									World world = tar.getPlayer().getWorld();
									Location a = new Location(world, x, y, z);
									Entity tnt = tar.getWorld().spawnEntity(a, EntityType.PRIMED_TNT);
									tar.setPassenger(tnt);
									return true;
								}
							}
						}else{
							player.sendMessage("ERROR, To Many Paremeters Used!");
							return true;
						}
					}
					if (cmd.getName().equalsIgnoreCase("get")) {
						World world = player.getWorld();
						if (args[0].equalsIgnoreCase("time")) {
							long time = world.getTime();
							player.sendMessage("The Time Of This World Is: " + time);
							return true;
						}else if (args[0].equalsIgnoreCase("fulltime")) {
							long time = world.getFullTime();
							player.sendMessage("The Full Time Of This World Is: " + time);
							return true;
						}else if (args.length > 1) {
							player.sendMessage("ERROR, To Many Paremeters Used!");
							return true;
						}
					}
				}else{
					cs.sendMessage("That command can only be used ingame.");
			}
		}
		return true;
	}
	public void reloadData() {
	    if (customConfigFile == null) {
	    customConfigFile = new File(getDataFolder(), "data.yml");
	    }
	    customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
	 
	    // Look for defaults in the jar
	    InputStream defConfigStream = getResource("data.yml");
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        customConfig.setDefaults(defConfig);
	    }
	}
	public FileConfiguration getData() {
	    if (customConfig == null) {
	    	reloadData();
	    }
	    return customConfig;
	}
	public void saveData() {
	    if (customConfig == null || customConfigFile == null) {
	    return;
	    }
	    try {
	        getData().save(customConfigFile);
	    } catch (IOException ex) {
	        getLogger().log(Level.SEVERE, "Could not save " + customConfigFile, ex);
	    }
	}
	@EventHandler
	public void onSignPlace(SignChangeEvent event) {
		Block sign = event.getBlock();
		Location loc = sign.getLocation();
		name = event.getLine(2);
		comm = event.getLine(3);
		Player player = event.getPlayer();
		Map<String, String> map = new HashMap<String, String>();
		map.put("owner", player.getDisplayName());
		map.put("command", event.getLine(3));
		map.put("location", loc.toString());
		map.put("world", sign.getWorld()+"");
		map.put("x", loc.getBlockX()+"");
		map.put("y", loc.getBlockY()+"");
		map.put("z", loc.getBlockZ()+"");
		if (event.getLine(0).equals("[so]")) {
			if (event.getLine(1).equals("[cmd]")) {
				event.setLine(0, ChatColor.AQUA + "[SoRandom]");
				event.setLine(1, ChatColor.AQUA + "[Command]");
				if (!getData().getKeys(false).contains(name)) {
					getData().createSection(name, map);
					saveData();
				}else{
					event.getPlayer().sendMessage("There Is Already A Sign Named " + name);
					event.setCancelled(true);
				}
			}else{
				event.setLine(0, ChatColor.DARK_RED + "[SoRandom]");
				event.setLine(1, ChatColor.DARK_RED + "[ERROR]");
				event.setLine(2, ChatColor.DARK_RED + "[Incorrect]");
				event.setLine(3, ChatColor.DARK_RED + "[Tag Usage]");
			}
		}else{
			return;
		}
	}
	@EventHandler
	public void onSignClick(PlayerInteractEvent event) {
		Block block = event.getClickedBlock();
		Location loc = event.getClickedBlock().getLocation();
		Player player = event.getPlayer();
		if (loc.toString() == getData().getString(name+".location") && block.getType() == Material.SIGN) {
			if (player.isOp()) {
				player.performCommand(comm);
			}else{
				player.sendMessage("You Cannot Use SoRandom Command Signs!");
			}
		}else{
			return;
		}
	}
	public void onBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		int x1 = block.getLocation().getBlockX();
		int y1 = block.getLocation().getBlockY();
		int z1 = block.getLocation().getBlockZ();
		Location loc = new Location(block.getWorld(), x1, y1, z1);
		String na = name;
		String dname = player.getDisplayName();
		String own = getData().getString(na+".owner");
		boolean blo = block.getType().equals(Sign.class);
		int x = getData().getInt(na+".x");
		int y = getData().getInt(na+".y");
		int z = getData().getInt(na+".z");
		World w = (World) getData().get(na+".world");
		Location loc2 = new Location(w, x, y, z);
		if (loc != loc2 && !blo && dname != own) {
			player.sendMessage("You Do Not Own This Sign!");
			event.setCancelled(true);
			return;
		}else if (loc == loc2 && !blo && dname != own) {
			player.sendMessage("You Do Not Own This Sign!");
			event.setCancelled(true);
			return;
		}else if (loc != loc2 && blo && dname != own) {
			player.sendMessage("You Do Not Own This Sign!");
			event.setCancelled(true);
			return;
		}else if (loc != loc2 && !blo && dname == own) {
			player.sendMessage("You Do Not Own This Sign!");
			event.setCancelled(true);
			return;
		}else if (loc == loc2 && blo && dname != own) {
			player.sendMessage("You Do Not Own This Sign!");
			event.setCancelled(true);
		}else if (loc == loc2 && blo && dname == own) {
			player.sendMessage("Successfully Removed Sign!");
			return;
		}else{
			player.sendMessage("An ERROR Has Occured");
			return;
		}
	}
}

