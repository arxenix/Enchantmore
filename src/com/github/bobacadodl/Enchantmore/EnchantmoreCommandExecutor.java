package com.github.bobacadodl.Enchantmore;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

public class EnchantmoreCommandExecutor implements CommandExecutor{
	private Enchantmore main;
	public EnchantmoreCommandExecutor(Enchantmore plugin, Enchantmore instance) {
		this.main = plugin;
		{main = instance;}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// TODO Auto-generated method stub
		if(args.length>=1){
			if(args[0].equalsIgnoreCase("yaw")){
				if(sender instanceof Player){
					Player p = (Player) sender;
					p.sendMessage(ChatColor.GOLD+"Yaw="+Float.toString(p.getLocation().getYaw()));
				}
			}
			if(args[0].equalsIgnoreCase("enchant")){
				if(args.length!=3){
					sender.sendMessage(ChatColor.RED+"Invalid Args!");
					return true;
				}
				Enchantment e = main.stringToEnchantment(args[1]);
				if(e==null){
					sender.sendMessage(ChatColor.RED+"Invalid Enchantment!");
					return true;
				}
				if(!(sender instanceof Player)) return true;
				Player p = (Player) sender;
				int level = 1;
				try{
					level = Integer.parseInt(args[2]);
				}
				catch(NumberFormatException ex){
					p.sendMessage(ChatColor.RED+"Level must be a number!");
					return true;
				}
				p.getItemInHand().addUnsafeEnchantment(e, level);
				p.sendMessage(ChatColor.GREEN+"Enchantment successful!");
				return true;
			}
		}
		if(args.length==0){
			sender.sendMessage(ChatColor.GREEN+"/em enchant [enchantmentname] [level]");
			return true;
		}
		return false;
	}

}
