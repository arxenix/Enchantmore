package com.github.bobacadodl.Enchantmore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.bobacadodl.Enchantmore.EnchantmoreListener.ToolType;

public class Enchantmore extends JavaPlugin{
	boolean NoCheatPlusHook = false;
	boolean AntiCheatHook = false;
	private EnchantmoreCommandExecutor myExecutor;
	public HashMap<ToolType,List<Enchantment>> enabledenchants = new HashMap<ToolType,List<Enchantment>>();
	public void onEnable(){
		loadConfiguration();
		myExecutor = new EnchantmoreCommandExecutor(this,this);
		getCommand("enchantmore").setExecutor(myExecutor);
		getEnabledEnchantments();
		getServer().getPluginManager().registerEvents(new EnchantmoreListener(this), this);
		if(getServer().getPluginManager().isPluginEnabled("NoCheatPlus")) NoCheatPlusHook=true;
		if(getServer().getPluginManager().isPluginEnabled("AntiCheat")) AntiCheatHook=true;
	}
	
	public void onDisable(){
		
	}
	
	public void loadConfiguration(){
		if(getConfig().getInt("Config-Version")!=1){
		    getConfig().set("Permissions-Enabled", false);
		    getConfig().set("Enchants.pickaxe-flame.enabled", true);
		    getConfig().set("Enchants.pickaxe-power.blocks-per-level", 3);
		    getConfig().set("Enchants.pickaxe-power.enabled", true);
		    getConfig().set("Enchants.shovel-flame.enabled", true);
		    getConfig().set("Enchants.shovel-power.enabled", true);
		    getConfig().set("Enchants.shovel-power.size-per-level", 3);
		    getConfig().set("Enchants.hoe-flame.enabled", true);
		    getConfig().set("Enchants.shears-flame.enabled", true);
		    getConfig().set("Enchants.flintandsteel-flame.enabled", true);
		    getConfig().set("Enchants.sword-flame.enabled", true);
		    getConfig().set("Enchants.axe-power.enabled", true);
		    getConfig().set("Enchants.axe-flame.enabled", true);
		    getConfig().set("Enchants.axe-power.blocks-per-level", 6);
			getConfig().set("Config-Version", 1);
		}
		getConfig().options().copyDefaults(true);
	    saveConfig();
	}
	public void disableChecks(Player p, CheckType type){
		if(AntiCheatHook==true){
			if(type.equals(CheckType.FAKEBLOCKBREAKING)){
				if(!net.h31ix.anticheat.api.AnticheatAPI.isExempt(p, net.h31ix.anticheat.manage.CheckType.LONG_REACH)){
					net.h31ix.anticheat.api.AnticheatAPI.exemptPlayer(p, net.h31ix.anticheat.manage.CheckType.LONG_REACH);
				}
				if(!net.h31ix.anticheat.api.AnticheatAPI.isExempt(p, net.h31ix.anticheat.manage.CheckType.FAST_BREAK)){
					net.h31ix.anticheat.api.AnticheatAPI.exemptPlayer(p, net.h31ix.anticheat.manage.CheckType.FAST_BREAK);
				}
				if(!net.h31ix.anticheat.api.AnticheatAPI.isExempt(p, net.h31ix.anticheat.manage.CheckType.NO_SWING)){
					net.h31ix.anticheat.api.AnticheatAPI.exemptPlayer(p, net.h31ix.anticheat.manage.CheckType.NO_SWING);
				}
			}

		}
		if(NoCheatPlusHook==true){
			if(type.equals(CheckType.FAKEBLOCKBREAKING)){
				if(!fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.isExempted(p,fr.neatmonster.nocheatplus.checks.CheckType.BLOCKBREAK)){
					fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.exemptPermanently(p, fr.neatmonster.nocheatplus.checks.CheckType.BLOCKBREAK);
				}
				if(!fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.isExempted(p,fr.neatmonster.nocheatplus.checks.CheckType.BLOCKBREAK_DIRECTION)){
					fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.exemptPermanently(p, fr.neatmonster.nocheatplus.checks.CheckType.BLOCKBREAK_DIRECTION);
				}
				if(!fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.isExempted(p,fr.neatmonster.nocheatplus.checks.CheckType.BLOCKBREAK_FASTBREAK)){
					fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.exemptPermanently(p, fr.neatmonster.nocheatplus.checks.CheckType.BLOCKBREAK_FASTBREAK);
				}
				if(!fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.isExempted(p,fr.neatmonster.nocheatplus.checks.CheckType.BLOCKBREAK_FREQUENCY)){
					fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.exemptPermanently(p, fr.neatmonster.nocheatplus.checks.CheckType.BLOCKBREAK_FREQUENCY);
				}
				if(!fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.isExempted(p,fr.neatmonster.nocheatplus.checks.CheckType.BLOCKBREAK_NOSWING)){
					fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.exemptPermanently(p, fr.neatmonster.nocheatplus.checks.CheckType.BLOCKBREAK_NOSWING);
				}
				if(!fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.isExempted(p,fr.neatmonster.nocheatplus.checks.CheckType.BLOCKBREAK_REACH)){
					fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.exemptPermanently(p, fr.neatmonster.nocheatplus.checks.CheckType.BLOCKBREAK_REACH);
				}
				if(!fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.isExempted(p,fr.neatmonster.nocheatplus.checks.CheckType.BLOCKBREAK_WRONGBLOCK)){
					fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.exemptPermanently(p, fr.neatmonster.nocheatplus.checks.CheckType.BLOCKBREAK_WRONGBLOCK);
				}
				if(!fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.isExempted(p,fr.neatmonster.nocheatplus.checks.CheckType.BLOCKINTERACT)){
					fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.exemptPermanently(p, fr.neatmonster.nocheatplus.checks.CheckType.BLOCKINTERACT);
				}
				if(!fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.isExempted(p,fr.neatmonster.nocheatplus.checks.CheckType.BLOCKINTERACT_DIRECTION)){
					fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.exemptPermanently(p, fr.neatmonster.nocheatplus.checks.CheckType.BLOCKINTERACT_DIRECTION);
				}
				if(!fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.isExempted(p,fr.neatmonster.nocheatplus.checks.CheckType.BLOCKINTERACT_REACH)){
					fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.exemptPermanently(p, fr.neatmonster.nocheatplus.checks.CheckType.BLOCKINTERACT_REACH);
				}
			}
		}
	}
	
	public void enableChecks(Player p , CheckType type){
		if(AntiCheatHook==true){
			if(type.equals(CheckType.FAKEBLOCKBREAKING)){
				if(net.h31ix.anticheat.api.AnticheatAPI.isExempt(p, net.h31ix.anticheat.manage.CheckType.LONG_REACH)){
					net.h31ix.anticheat.api.AnticheatAPI.unexemptPlayer(p, net.h31ix.anticheat.manage.CheckType.LONG_REACH);
				}
				if(net.h31ix.anticheat.api.AnticheatAPI.isExempt(p, net.h31ix.anticheat.manage.CheckType.FAST_BREAK)){
					net.h31ix.anticheat.api.AnticheatAPI.unexemptPlayer(p, net.h31ix.anticheat.manage.CheckType.FAST_BREAK);
				}
				if(net.h31ix.anticheat.api.AnticheatAPI.isExempt(p, net.h31ix.anticheat.manage.CheckType.NO_SWING)){
					net.h31ix.anticheat.api.AnticheatAPI.unexemptPlayer(p, net.h31ix.anticheat.manage.CheckType.NO_SWING);
				}
			}

		}
		
		if(NoCheatPlusHook==true){
			if(type.equals(CheckType.FAKEBLOCKBREAKING)){
				if(fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.isExempted(p,fr.neatmonster.nocheatplus.checks.CheckType.BLOCKBREAK)){
					fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.unexempt(p, fr.neatmonster.nocheatplus.checks.CheckType.BLOCKBREAK);
				}
				if(fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.isExempted(p,fr.neatmonster.nocheatplus.checks.CheckType.BLOCKBREAK_DIRECTION)){
					fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.unexempt(p, fr.neatmonster.nocheatplus.checks.CheckType.BLOCKBREAK_DIRECTION);
				}
				if(fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.isExempted(p,fr.neatmonster.nocheatplus.checks.CheckType.BLOCKBREAK_FASTBREAK)){
					fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.unexempt(p, fr.neatmonster.nocheatplus.checks.CheckType.BLOCKBREAK_FASTBREAK);
				}
				if(fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.isExempted(p,fr.neatmonster.nocheatplus.checks.CheckType.BLOCKBREAK_FREQUENCY)){
					fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.unexempt(p, fr.neatmonster.nocheatplus.checks.CheckType.BLOCKBREAK_FREQUENCY);
				}
				if(fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.isExempted(p,fr.neatmonster.nocheatplus.checks.CheckType.BLOCKBREAK_NOSWING)){
					fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.unexempt(p, fr.neatmonster.nocheatplus.checks.CheckType.BLOCKBREAK_NOSWING);
				}
				if(fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.isExempted(p,fr.neatmonster.nocheatplus.checks.CheckType.BLOCKBREAK_REACH)){
					fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.unexempt(p, fr.neatmonster.nocheatplus.checks.CheckType.BLOCKBREAK_REACH);
				}
				if(fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.isExempted(p,fr.neatmonster.nocheatplus.checks.CheckType.BLOCKBREAK_WRONGBLOCK)){
					fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.unexempt(p, fr.neatmonster.nocheatplus.checks.CheckType.BLOCKBREAK_WRONGBLOCK);
				}
				if(fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.isExempted(p,fr.neatmonster.nocheatplus.checks.CheckType.BLOCKINTERACT)){
					fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.unexempt(p, fr.neatmonster.nocheatplus.checks.CheckType.BLOCKINTERACT);
				}
				if(fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.isExempted(p,fr.neatmonster.nocheatplus.checks.CheckType.BLOCKINTERACT_DIRECTION)){
					fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.unexempt(p, fr.neatmonster.nocheatplus.checks.CheckType.BLOCKINTERACT_DIRECTION);
				}
				if(fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.isExempted(p,fr.neatmonster.nocheatplus.checks.CheckType.BLOCKINTERACT_REACH)){
					fr.neatmonster.nocheatplus.hooks.NCPExemptionManager.unexempt(p, fr.neatmonster.nocheatplus.checks.CheckType.BLOCKINTERACT_REACH);
				}
			}
		}
	}
	
	public enum CheckType{
		FAKEBLOCKBREAKING,
		FLYING,
		SPEED;
	}
	
	public List<Enchantment> getEnabledEnchantments(){
		List<Enchantment> enabled = new ArrayList<Enchantment>();
		for(String enchantmentcombo:getConfig().getConfigurationSection("Enchants").getKeys(false)){
			if(getConfig().getBoolean("Enchants."+enchantmentcombo+".enabled")){
				String toolstring = enchantmentcombo.split("-")[0];
				ToolType type = ToolType.OTHER;
				if(toolstring.equalsIgnoreCase("pickaxe")) type = ToolType.PICKAXE;
				if(toolstring.equalsIgnoreCase("axe")) type = ToolType.AXE;
				if(toolstring.equalsIgnoreCase("shovel")) type = ToolType.SHOVEL;
				if(toolstring.equalsIgnoreCase("sword")) type = ToolType.SWORD;
				if(toolstring.equalsIgnoreCase("hoe")) type = ToolType.HOE;
				if(toolstring.equalsIgnoreCase("shears")) type = ToolType.SHEARS;
				if(toolstring.equalsIgnoreCase("flintandsteel")) type = ToolType.FLINTANDSTEEL;
				String enchantmentstring = enchantmentcombo.split("-")[1];
				Enchantment e = stringToEnchantment(enchantmentstring);
				
				getServer().getLogger().log(Level.INFO, "Enabling EnchantmentCombo "+enchantmentcombo);
				if(enabledenchants.containsKey(type)){
					List<Enchantment> current = enabledenchants.get(type);
					current.add(e);
					enabledenchants.put(type, current);
				}
				else{
					List<Enchantment> current = new ArrayList<Enchantment>();
					current.add(e);
					enabledenchants.put(type, current);
				}
			}
		}
		return enabled;
	}
	
	public Enchantment stringToEnchantment(String enchantmentstring){
		Enchantment e = null;
		if(enchantmentstring.equalsIgnoreCase("flame")) e=Enchantment.ARROW_FIRE;
		if(enchantmentstring.equalsIgnoreCase("sharpness")) e=Enchantment.DAMAGE_ALL;
		if(enchantmentstring.equalsIgnoreCase("fireaspect")) e=Enchantment.FIRE_ASPECT;
		if(enchantmentstring.equalsIgnoreCase("baneofarthropods")) e=Enchantment.DAMAGE_ARTHROPODS;
		if(enchantmentstring.equalsIgnoreCase("smite")) e=Enchantment.DAMAGE_UNDEAD;
		if(enchantmentstring.equalsIgnoreCase("efficiency")) e=Enchantment.DIG_SPEED;
		if(enchantmentstring.equalsIgnoreCase("unbreaking")) e=Enchantment.DURABILITY;
		if(enchantmentstring.equalsIgnoreCase("punch")) e=Enchantment.ARROW_KNOCKBACK;
		if(enchantmentstring.equalsIgnoreCase("power")) e=Enchantment.ARROW_DAMAGE;
		if(enchantmentstring.equalsIgnoreCase("infinity")) e=Enchantment.ARROW_INFINITE;
		if(enchantmentstring.equalsIgnoreCase("knockback")) e=Enchantment.KNOCKBACK;
		if(enchantmentstring.equalsIgnoreCase("fortune")) e=Enchantment.LOOT_BONUS_BLOCKS;
		if(enchantmentstring.equalsIgnoreCase("looting")) e=Enchantment.LOOT_BONUS_MOBS;
		if(enchantmentstring.equalsIgnoreCase("respiration")) e=Enchantment.OXYGEN;
		if(enchantmentstring.equalsIgnoreCase("protection")) e=Enchantment.PROTECTION_ENVIRONMENTAL;
		if(enchantmentstring.equalsIgnoreCase("featherfalling")) e=Enchantment.PROTECTION_FALL;
		if(enchantmentstring.equalsIgnoreCase("blastprotection")) e=Enchantment.PROTECTION_EXPLOSIONS;
		if(enchantmentstring.equalsIgnoreCase("fireprotection")) e=Enchantment.PROTECTION_FIRE;
		if(enchantmentstring.equalsIgnoreCase("projectileprotection")) e=Enchantment.PROTECTION_PROJECTILE;
		if(enchantmentstring.equalsIgnoreCase("aquaaffinity")) e=Enchantment.WATER_WORKER;
		if(enchantmentstring.equalsIgnoreCase("silktouch")) e=Enchantment.SILK_TOUCH;
		return e;
	}

}
