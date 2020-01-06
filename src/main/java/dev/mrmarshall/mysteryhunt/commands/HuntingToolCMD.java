package dev.mrmarshall.mysteryhunt.commands;

import dev.mrmarshall.mysteryhunt.objects.HuntingTool;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HuntingToolCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			
			if(args.length == 0) {
				if(p.hasPermission("mysteryhunt.huntingtool")) {
					p.getInventory().addItem(HuntingTool.getHuntingTool());
				}
			}
		}
		return false;
	}
}
