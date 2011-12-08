package en.tehbeard.worldDiff;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

public class WorldDiff {

	public WorldDiff(File file){
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		for(World w:Bukkit.getWorlds()){
			w.setDifficulty(
					Difficulty.getByValue(config.getInt("worlddiff."+w.getName(),1))
					);
			
		}
	}
}
