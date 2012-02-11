package de.hydrox.bukkit.timezone;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import net.escapecraft.component.AbstractComponent;
import net.escapecraft.component.BukkitCommand;
import net.escapecraft.component.ComponentDescriptor;
import net.escapecraft.escapePlug.EscapePlug;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.tulonsae.mc.util.Log;

@ComponentDescriptor(name="timezone",slug="timezone",version="1.00")
@BukkitCommand(command="timezone")
public class TimezoneComponent extends AbstractComponent implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		Date date = new Date();

		DateFormat serverFormat = new SimpleDateFormat();
		DateFormat firstFormat = new SimpleDateFormat();

		sender.sendMessage("Servertime: " + serverFormat.format(date));
		TimeZone zone = null;


		if (args.length == 0) {
			zone = TimeZone.getTimeZone("GMT");
			firstFormat.setTimeZone(zone);
			sender.sendMessage("GMT: " + firstFormat.format(date));

			zone = TimeZone.getTimeZone("AET");
			firstFormat.setTimeZone(zone);
			sender.sendMessage("Sydney: " + firstFormat.format(date));

			return true;
		}
		if (args.length >= 1) {
			if (args[0].equalsIgnoreCase("list")) {
				listTimezones(sender);
				return true;
			}
			if (args[0].equalsIgnoreCase("help")) {
				displayHelp(sender);
				return true;
			}
			if (args[0].equalsIgnoreCase("convert")) {
				String parsedString = null;
				try {
					parsedString = parseDate(args);
			        DateFormat format =
			            DateFormat.getTimeInstance(DateFormat.SHORT);
					Date parsedDate = format.parse(parsedString);
					sender.sendMessage("Original string: " + parsedString);
					String[] tmp = serverFormat.format(parsedDate).split(" ");
					sender.sendMessage("Server: " + tmp[1] + tmp[2]);

					zone = TimeZone.getTimeZone("GMT");
					firstFormat.setTimeZone(zone);
					tmp = firstFormat.format(parsedDate).split(" ");
					sender.sendMessage("GMT: " + tmp[1] + tmp[2]);

					zone = TimeZone.getTimeZone("AET");
					firstFormat.setTimeZone(zone);
					tmp = firstFormat.format(parsedDate).split(" ");
					sender.sendMessage("Sydney: " + tmp[1] + tmp[2]);

				}
				catch(ParseException pe) {
				}
				return true;
			}

			zone = TimeZone.getTimeZone(args[0]);

			for (String string : args) {
				zone = TimeZone.getTimeZone(string);
				firstFormat.setTimeZone(zone);
				sender.sendMessage("-->"+string+": " + firstFormat.format(date));
			}
			return true;
		}
		return false;
	}

	private void listTimezones(CommandSender sender) {
		sender.sendMessage("Here is a list of valid Timezones (not complete):");
		sender.sendMessage("GMT, GMT+1, GMT-5, PST, AET, Australia/Sydney, CST, EST, CET");
	}

	private void displayHelp(CommandSender sender) {
		sender.sendMessage("Type '/tz' or '/tz help' to display this help");
		sender.sendMessage("Type '/tz list' for a (non complete) list of valid timezones");
		sender.sendMessage("Type '/tz timezone1 [timezone2]' to show the servertime and the times in the given zones");
		sender.sendMessage("You can display multiple timezones at once");
	}

	private String parseDate(String [] args) {
		return  args[1] + " " + args[2];
	}

    @Override
    public boolean enable(Log log, EscapePlug plugin) {
        plugin.getComponentManager().registerCommands(this);
        return true;
    }

    @Override
    public void disable() {
        
    }
}
