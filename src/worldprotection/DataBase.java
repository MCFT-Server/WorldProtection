package worldprotection;

import cn.nukkit.utils.Config;

public class DataBase {
	public Config protectlist;
	private Main plugin;
	
	public DataBase(Main plugin) {
		this.plugin = plugin;
		initDB();
	}
	
	private void initDB() {
		plugin.getDataFolder().mkdirs();
		protectlist = new Config(plugin.getDataFolder().getPath() + "/protectlist.json", Config.JSON);
	}
	
	public void save() {
		protectlist.save();
	}
}
