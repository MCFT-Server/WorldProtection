package worldprotection;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.TranslationContainer;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.level.Level;
import cn.nukkit.utils.TextFormat;

public class EventListener implements Listener {
	private Main plugin;
	
	public EventListener(Main plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < 1) {
			return false;
		}
		switch (args[0].toLowerCase()) {
		case "add" :
			if (args.length < 2) {
				sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.usage", "/" + command.getName() + " add <worldname>"));
				return true;
			}
			Level level = Server.getInstance().getLevelByName(args[1]);
			if (level == null) {
				sender.sendMessage(TextFormat.RED + "The world does not exist.");
				return true;
			}
			addProtectWorld(level);
			sender.sendMessage(TextFormat.DARK_AQUA + "level " + level.getFolderName() + "is now protection");
			return true;
		case "delete" :
			if (args.length < 2) {
				sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.usage", "/" + command.getName() + " delete <worldname>"));
				return true;
			}
			if (!isProtectWorld(args[1])) {
				sender.sendMessage(TextFormat.RED + "The world doesn't protected");
				return true;
			}
			deleteProtectWorld(args[1]);
			sender.sendMessage(TextFormat.DARK_AQUA + "The world's protection cancelled");
			return true;
		case "list" :
			sender.sendMessage(TextFormat.DARK_AQUA + "Protection list: ");
			String list = "";
			for (Object levelname : getProtectList()) {
				list = list + levelname + ", ";
			}
			sender.sendMessage(TextFormat.DARK_AQUA + list);
			return true;
		default :
			return false;
		}
	}
	
	private void addProtectWorld(Level level) {
		plugin.getDB().protectlist.set(level.getFolderName(), level.getFolderName());
	}
	@SuppressWarnings("unused")
	private void deleteProtectWorld(Level level) {
		deleteProtectWorld(level.getFolderName());
	}
	private void deleteProtectWorld(String level) {
		plugin.getDB().protectlist.remove(level);
	}
	private boolean isProtectWorld(Level level) {
		return isProtectWorld(level.getFolderName());
	}
	private boolean isProtectWorld(String level) {
		return plugin.getDB().protectlist.exists(level);
	}
	private Object[] getProtectList() {
		return plugin.getDB().protectlist.getAll().values().toArray();
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Level level = event.getBlock().getLevel();
		if (isProtectWorld(level) && !player.hasPermission("whiteworld.fixable.place")) {
			event.setCancelled();
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Level level = event.getBlock().getLevel();
		if (isProtectWorld(level) && !player.hasPermission("whiteworld.fixable.break")) {
			event.setCancelled();
		}
	}
	
	@EventHandler
	public void onTouch(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Level level = event.getBlock().getLevel();
		if (isProtectWorld(level) && !player.hasPermission("whiteworld.fixable.touch")) {
			event.setCancelled();
		}
	}
}
