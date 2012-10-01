package net.serubin.warp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.logging.Logger;

import net.escapecraft.escapePlug.EscapePlug;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.tulonsae.mc.util.Log;

public class FlatFile {
	private EscapePlug plugin;

	// File
	private String fileName = "warps.csv";
	private FileWriter fStream = null;
	private BufferedWriter out = null;

	/**
	 * Initiates FlatFile.
	 * 
	 * @param plugin
	 *            EscapePlug
	 */
	public FlatFile(EscapePlug plugin, Logger log) {
		this.plugin = plugin;
		File warpsFile = new File(plugin.getDataFolder(), "warps.yml");
		if (!warpsFile.exists()) {
			log.info("Creating 'warps.yml'...");
			try {
				warpsFile.createNewFile();
			} catch (IOException e) {
				log.severe("'warps.yml' could not be created!");
				e.printStackTrace();
			}
		}
		loadData();
	}

	private void loadData() {
		try {
			fStream = new FileWriter(fileName);
			out = new BufferedWriter(fStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean addWarp(String name, Location loc, Player user) {
		String format = name + ", " + loc.getWorld() + ", " + loc.getBlockX()
				+ ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ", "
				+ user.getName();
		return false;
	}

	public static Timestamp getDateTime() {
		java.sql.Timestamp date;
		java.util.Date today = new java.util.Date();
		date = new java.sql.Timestamp(today.getTime());
		return date;
	}
}
