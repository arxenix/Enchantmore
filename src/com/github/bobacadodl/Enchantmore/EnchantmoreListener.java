package com.github.bobacadodl.Enchantmore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;


import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import com.github.bobacadodl.Enchantmore.Enchantmore.CheckType;


public class EnchantmoreListener implements Listener{
	BlockBreakEvent fakeEvent = null;
    private Enchantmore main;
	public EnchantmoreListener(Enchantmore instance){main = instance;}
	public List<Enchantment> disabledEnchantments = new ArrayList<Enchantment>();
	public void trade(InventoryClickEvent event){
		if(event.getInventory().getType().equals(InventoryType.MERCHANT) && event.getSlot()==3){
		}
	}

	@EventHandler(priority=EventPriority.HIGH)
	public void onBlockBreak(BlockBreakEvent event){
		if(event.isCancelled()) return;
		if(event.equals(fakeEvent)){
			return;
		}
		Player p = event.getPlayer();
		ItemStack tool = event.getPlayer().getItemInHand();
		ToolType type = toolCheck(tool);
		Block b = event.getBlock();
		Material bm = b.getType();
		switch(type){
			case PICKAXE:
				if(hasEnch(tool,Enchantment.ARROW_DAMAGE,p)){
					//break entire ore vein
					boolean shouldsmelt = hasEnch(tool,Enchantment.ARROW_FIRE,p);
					if(isOre(bm)){
						List<Block> connecting = getConnecting(b,main.getConfig().getInt("Enchants.pickaxe-power.blocks-per-level")*tool.getEnchantmentLevel(Enchantment.ARROW_DAMAGE),0,new ArrayList<Block>());
						main.disableChecks(p, CheckType.FAKEBLOCKBREAKING);
						for(Block b1:connecting){
							fakeEvent = new BlockBreakEvent(b1,p);
							Bukkit.getServer().getPluginManager().callEvent(fakeEvent);
							if(!fakeEvent.isCancelled()){
								tool.setDurability((short) (tool.getDurability()-connecting.size()));
								if(!shouldsmelt){
									b1.breakNaturally(tool);
								}
								else{
									event.setCancelled(true);
									smeltAndDrop(b1,tool);
								}
							}
						}
						main.enableChecks(p, CheckType.FAKEBLOCKBREAKING);
					
					}
					if(shouldsmelt){
						if(smeltAndDrop(b,tool)){
							event.setCancelled(true);
						}
					}
					return;
				}
				if(hasEnch(tool,Enchantment.ARROW_FIRE,p)){
					//smelt drops
					event.setCancelled(true);
					smeltAndDrop(b,tool);
				}
				

			case AXE:
				if(hasEnch(tool,Enchantment.ARROW_DAMAGE,p)){
					//Chop entire tree
					boolean shouldsmelt = hasEnch(tool,Enchantment.ARROW_FIRE,p);
					if(bm.equals(Material.LOG)){
						List<Block> connecting = getConnecting(b,main.getConfig().getInt("Enchants.axe-power.blocks-per-level")*tool.getEnchantmentLevel(Enchantment.ARROW_DAMAGE),0,new ArrayList<Block>());
						main.disableChecks(p, CheckType.FAKEBLOCKBREAKING);
						for(Block b1:connecting){
							fakeEvent = new BlockBreakEvent(b1,p);
							main.getServer().getPluginManager().callEvent(fakeEvent);
							if(!fakeEvent.isCancelled()){
								tool.setDurability((short) (tool.getDurability()-connecting.size()));
								if(!shouldsmelt){
									b1.breakNaturally(tool);
								}
								else{
									if(smeltAndDrop(b1,tool)){
										event.setCancelled(true);
									}
								}
							}
						}
						main.enableChecks(p, CheckType.FAKEBLOCKBREAKING);
					}
					if(shouldsmelt){
						if(smeltAndDrop(b,tool)){
							event.setCancelled(true);
						}
					}
				}
				if(hasEnch(tool,Enchantment.ARROW_FIRE,p)){
					//smelt drops
					if(smeltAndDrop(b,tool)){
						event.setCancelled(true);
					}
				}
				
				
				
				
				
				
				
				
				
			case SHOVEL:
				
				if(hasEnch(tool,Enchantment.ARROW_DAMAGE,p)){
					//Excavate Large Area
					int r = tool.getEnchantmentLevel(Enchantment.ARROW_DAMAGE)*main.getConfig().getInt("Enchants.shovel-power.size-per-level");
					Location loc = b.getLocation();
                    int x0 = loc.getBlockX();
                    int y0 = loc.getBlockY();
                    int z0 = loc.getBlockZ();
                    World w = loc.getWorld();
                    // cube
                    Location pl = p.getLocation();
                    int pitch = Math.round(pl.getPitch());
                    int yaw = Math.round(pl.getYaw());
                    if(pitch <= -60){
                    	//break upwards
                    	if(isExcavatable(b.getTypeId())){
                    		main.disableChecks(p, CheckType.FAKEBLOCKBREAKING);
                        	for(int dx=x0-r;dx<=x0+r;dx++){
                        		for(int dz=z0-r;dz<=z0+r;dz++){
                        			for(int dy=y0;dy<=y0+r;dy++){
                        				Block b1 = b.getWorld().getBlockAt(dx, dy, dz);
                        				if(isExcavatable(b1.getTypeId())){
                        					safeSetBlock(p,b1,Material.AIR);
                        					
                        				}
                        			}
                        		}
                        	}
                        	main.enableChecks(p, CheckType.FAKEBLOCKBREAKING);
                    	}
                    }
                    
                    if(pitch >= 60){
                    	//break downwards
                    	if(isExcavatable(b.getTypeId())){
                    		main.disableChecks(p, CheckType.FAKEBLOCKBREAKING);
                        	for(int dx=x0-r;dx<=x0+r;dx++){
                        		for(int dz=z0-r;dz<=z0+r;dz++){
                        			for(int dy=y0-r;dy<=y0;dy++){
                        				Block b1 = b.getWorld().getBlockAt(dx, dy, dz);
                        				if(isExcavatable(b1.getTypeId())){
                        					
                        					safeSetBlock(p,b1,Material.AIR);
                        					
                        				}
                        			}
                        		}
                        	}
                        	main.enableChecks(p, CheckType.FAKEBLOCKBREAKING);
                    	}
                    }
                    
                    if(pitch<60 && pitch>-60){
                    	if(yaw>=315 || yaw<45){
                        	if(isExcavatable(b.getTypeId())){
                        		main.disableChecks(p, CheckType.FAKEBLOCKBREAKING);
                            	for(int dx=-r;dx<=r;dx++){
                            		for(int dy=-r;dy<=r;dy++){
                            			for(int dz=0;dz<=-2*r;dz--){
                            				Block b1 = b.getRelative(dx, dy, dz);
                            				if(isExcavatable(b1.getTypeId())){
                            					safeSetBlock(p,b1,Material.AIR);
                            				}
                            			}
                            		}
                            	}
                            	main.enableChecks(p, CheckType.FAKEBLOCKBREAKING);
                        	}
                    	}
                    	
                    	if(yaw>=45 && yaw<135){
                    		//break -x
                        	if(isExcavatable(b.getTypeId())){
                        		main.disableChecks(p, CheckType.FAKEBLOCKBREAKING);
                            	for(int dz=-r;dz<=r;dz++){
                            		for(int dy=-r;dy<=r;dy++){
                            			for(int dx=0;dx<=2*r;dx++){
                            				Block b1 = b.getRelative(dx, dy, dz);
                            				if(isExcavatable(b1.getTypeId())){
                            					safeSetBlock(p,b1,Material.AIR);
                            				}
                            			}
                            		}
                            	}
                            	main.enableChecks(p, CheckType.FAKEBLOCKBREAKING);
                        	}
                    	}
                    	if(yaw>=135 && yaw <225){
                        	if(isExcavatable(b.getTypeId())){
                        		p.sendMessage("yaw = 180");
                        		main.disableChecks(p, CheckType.FAKEBLOCKBREAKING);
                            	for(int dx=-r;dx<=r;dx++){
                            		for(int dy=-r;dy<=r;dy++){
                            			for(int dz=0;dz<=2*r;dz++){
                            				p.sendMessage("break!");
                            				Block b1 = b.getRelative(dx, dy, dz);
                            				if(isExcavatable(b1.getTypeId())){
                            					safeSetBlock(p,b1,Material.AIR);
                            				}
                            			}
                            		}
                            	}
                            	main.enableChecks(p, CheckType.FAKEBLOCKBREAKING);
                        	}
                    	}
                    	if(yaw>=225 && yaw <315){
                        	if(isExcavatable(b.getTypeId())){
                        		main.disableChecks(p, CheckType.FAKEBLOCKBREAKING);
                            	for(int dz=-r;dz<=r;dz++){
                            		for(int dy=-r;dy<=r;dy++){
                            			for(int dx=0;dx<=-2*r;dx--){
                            				Block b1 = b.getRelative(dx, dy, dz);
                            				if(isExcavatable(b1.getTypeId())){
                            					safeSetBlock(p,b1,Material.AIR);
                            				}
                            			}
                            		}
                            	}
                            	main.enableChecks(p, CheckType.FAKEBLOCKBREAKING);
                        	}
                    	}
                    }
                    
                    
                    
				}
				
				if(hasEnch(tool,Enchantment.ARROW_FIRE,p)){
					//Smelt drops
					if(smeltAndDrop(b,tool)){
						event.setCancelled(true);
					}
				}
			default:
				return;
		}
		
	}
	
	
	
	public List<Block> getConnecting(Block b,int amount,int amountchecked,List<Block> connecting){
		if(amountchecked==0 && amount >=1){
			connecting.add(b);
			amountchecked = 1;
		}
		if(amountchecked==amount) return connecting;
		if(b.getLocation().add(1,0,0).getBlock().getType().equals(b.getType())){
			if(!connecting.contains(b.getLocation().add(1,0,0).getBlock())){
				connecting.add(b.getLocation().add(1,0,0).getBlock());
				amountchecked=amountchecked+1;
				connecting.addAll(getConnecting(b.getLocation().add(1,0,0).getBlock(),amount,amountchecked,connecting));
			}
		}
		if(amountchecked==amount) return connecting;
		if(b.getLocation().add(0,1,0).getBlock().getType().equals(b.getType())){
			if(!connecting.contains(b.getLocation().add(0,1,0).getBlock())){
				connecting.add(b.getLocation().add(0,1,0).getBlock());
				amountchecked=amountchecked+1;
				connecting.addAll(getConnecting(b.getLocation().add(0,1,0).getBlock(),amount,amountchecked,connecting));
			}
		}
		if(amountchecked==amount) return connecting;
		if(b.getLocation().add(0,0,1).getBlock().getType().equals(b.getType())){
			if(!connecting.contains(b.getLocation().add(0,0,1).getBlock())){
				connecting.add(b.getLocation().add(0,0,1).getBlock());
				amountchecked=amountchecked+1;
				connecting.addAll(getConnecting(b.getLocation().add(0,0,1).getBlock(),amount,amountchecked,connecting));
			}
		}
		if(amountchecked==amount) return connecting;
		if(b.getLocation().subtract(1,0,0).getBlock().getType().equals(b.getType())){
			if(!connecting.contains(b.getLocation().subtract(1,0,0).getBlock())){
				connecting.add(b.getLocation().subtract(1,0,0).getBlock());
			amountchecked=amountchecked+1;
			connecting.addAll(getConnecting(b.getLocation().subtract(1,0,0).getBlock(),amount,amountchecked,connecting));
			}
		}
		if(amountchecked==amount) return connecting;
		if(b.getLocation().subtract(0,1,0).getBlock().getType().equals(b.getType())){
			if(!connecting.contains(b.getLocation().subtract(0,1,0).getBlock())){
				connecting.add(b.getLocation().subtract(0,1,0).getBlock());
			amountchecked=amountchecked+1;
			connecting.addAll(getConnecting(b.getLocation().subtract(0,1,0).getBlock(),amount,amountchecked,connecting));
			}
		}
		if(amountchecked==amount) return connecting;
		if(b.getLocation().subtract(0,0,1).getBlock().getType().equals(b.getType())){
			if(!connecting.contains(b.getLocation().subtract(0,0,1).getBlock())){
				connecting.add(b.getLocation().subtract(0,0,1).getBlock());
				amountchecked=amountchecked+1;
				connecting.addAll(getConnecting(b.getLocation().subtract(0,0,1).getBlock(),amount,amountchecked,connecting));
			}
		}
		if(amountchecked==amount) return connecting;
		return connecting;	
	}
	public ToolType toolCheck(ItemStack i){
		//if axe
		if(i.getType().equals(Material.WOOD_AXE) || i.getType().equals(Material.STONE_AXE) || i.getType().equals(Material.IRON_AXE) || i.getType().equals(Material.GOLD_AXE) || i.getType().equals(Material.DIAMOND_AXE)){
			return ToolType.AXE;
		}
		
		//if pickaxe
		if(i.getType().equals(Material.WOOD_PICKAXE) || i.getType().equals(Material.STONE_PICKAXE) || i.getType().equals(Material.IRON_PICKAXE) || i.getType().equals(Material.GOLD_PICKAXE) || i.getType().equals(Material.DIAMOND_PICKAXE)){
			return ToolType.PICKAXE;
		}
		
		
		//if shovel
		if(i.getType().equals(Material.WOOD_SPADE) || i.getType().equals(Material.STONE_SPADE) || i.getType().equals(Material.IRON_SPADE) || i.getType().equals(Material.GOLD_SPADE) || i.getType().equals(Material.DIAMOND_SPADE)){
			return ToolType.SHOVEL;
		}
		
		//if sword
		if(i.getType().equals(Material.WOOD_SWORD) || i.getType().equals(Material.STONE_SWORD) || i.getType().equals(Material.IRON_SWORD) || i.getType().equals(Material.GOLD_SWORD) || i.getType().equals(Material.DIAMOND_SWORD)){
			return ToolType.SWORD;
		}
		
		if(i.getType().equals(Material.WOOD_HOE) || i.getType().equals(Material.STONE_HOE) || i.getType().equals(Material.IRON_HOE) || i.getType().equals(Material.GOLD_HOE)  || i.getType().equals(Material.DIAMOND_HOE)){
			return ToolType.HOE;
		}
		if(i.getType().equals(Material.FLINT_AND_STEEL)){
			return ToolType.FLINTANDSTEEL;
		}
		if(i.getType().equals(Material.SHEARS)){
			return ToolType.SHEARS;
		}
		return ToolType.OTHER;
	}

	public Boolean hasEnch( ItemStack item, Enchantment ench,Player p){
		if(!isEnabledAndPlayerHasPerms(item,ench,p)) return false;
		if(item.getEnchantments().containsKey(ench)) return true;
		return false;
	}
	
	public Boolean isEnabledAndPlayerHasPerms(ItemStack item, Enchantment ench,Player p){
		boolean perms = main.getConfig().getBoolean("Permissions-Enabled");
		ToolType type = toolCheck(item);
		switch(type){
		case PICKAXE:
			if(main.enabledenchants.get(ToolType.PICKAXE).contains(ench)){
				if(perms==true && !p.hasPermission("enchantmore.pickaxe"+enchantmentToString(ench))){
					return false;
				}
				return true;
			}
		case SHOVEL:
			if(main.enabledenchants.get(ToolType.SHOVEL).contains(ench)){
				if(perms==true && !p.hasPermission("enchantmore.shovel"+enchantmentToString(ench))){
					return false;
				}
				return true;
			}
		case HOE:
			if(main.enabledenchants.get(ToolType.HOE).contains(ench)){
				if(perms==true && !p.hasPermission("enchantmore.hoe"+enchantmentToString(ench))){
					return false;
				}
				return true;
			}
		case AXE:
			if(main.enabledenchants.get(ToolType.AXE).contains(ench)){
				if(perms==true && !p.hasPermission("enchantmore.axe"+enchantmentToString(ench))){
					return false;
				}
				return true;
			}
		case SWORD:
			if(main.enabledenchants.get(ToolType.SWORD).contains(ench)){
				if(perms==true && !p.hasPermission("enchantmore.sword"+enchantmentToString(ench))){
					return false;
				}
				return true;
			}
		case SHEARS:
			if(main.enabledenchants.get(ToolType.SHEARS).contains(ench)){
				if(perms==true && !p.hasPermission("enchantmore.shears"+enchantmentToString(ench))){
					return false;
				}
				return true;
			}
		case FLINTANDSTEEL:
			if(main.enabledenchants.get(ToolType.FLINTANDSTEEL).contains(ench)){
				if(perms==true && !p.hasPermission("enchantmore.flintandsteel"+enchantmentToString(ench))){
					return false;
				}
				return true;
			}
		case OTHER:
			return false;
		}
		return false;
	}
	//PLUGIN FUNCTION RELATATED METHODS
    public ItemStack smelt(ItemStack raw) {
        // 'getResult' in 1.1-R8, was 'a' obfuscated in 1.1-R4
        net.minecraft.server.ItemStack smeltNMS = net.minecraft.server.RecipesFurnace.getInstance().getResult(raw.getTypeId());
        ItemStack smelted = (ItemStack)(new CraftItemStack(smeltNMS));
    
        return smelted;
    }
    
    public boolean smeltAndDrop(Block b,ItemStack tool){
		Collection<ItemStack> rawDrops = b.getDrops(tool);
		for (ItemStack rawDrop: rawDrops) {
			// note: original smelted idea from Firelord tools http://dev.bukkit.org/server-mods/firelord/
			// also see Superheat plugin? either way, coded this myself..
			ItemStack smeltedDrop = smelt(rawDrop);
			
			if(smeltedDrop==null || smeltedDrop.getType().equals(Material.AIR)){
				b.breakNaturally(tool);
				return true;
			}
			
			if (smeltedDrop != null && smeltedDrop.getType() != Material.AIR) {
				b.getWorld().dropItemNaturally(b.getLocation(), smeltedDrop);
				b.setType(Material.AIR);
				b.getWorld().playEffect(b.getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
				b.getWorld().playEffect(b.getLocation(), Effect.EXTINGUISH, 0);
				return true;
			}
		}
		return false;
    }
    
    public boolean isOre(Material m){
    	if(m.equals(Material.DIAMOND_ORE)||m.equals(Material.GOLD_ORE)||m.equals(Material.REDSTONE_ORE)||m.equals(Material.IRON_ORE)||m.equals(Material.COAL_ORE)||m.equals(Material.EMERALD_ORE)||m.equals(Material.LAPIS_ORE))
    		return true;
    	return false;
    }
    
    public boolean isExcavatable(int typeid){
    	Material m = Material.getMaterial(typeid);
    	if(m.equals(Material.SAND) || m.equals(Material.SOUL_SAND) || m.equals(Material.CLAY) || m.equals(Material.GRASS) || m.equals(Material.DIRT) || m.equals(Material.SNOW_BLOCK) || m.equals(Material.GRAVEL))
    		return true;
    	
    	return false;
    }
    
    public void safeSetBlock(Player p, Block b, Material m){
    	Location pl = p.getLocation();
    	Location bl = b.getLocation();
    	if(bl.getBlockX()==pl.getBlockX() && bl.getBlockZ()==pl.getBlockZ()){
    		if(bl.getBlockY()<=pl.getBlockY())
    			return;
    	}
		fakeEvent = new BlockBreakEvent(b,p);
		main.getServer().getPluginManager().callEvent(fakeEvent);
		if(!fakeEvent.isCancelled()){
	    	b.setType(m);
		}
    }
    
    public String enchantmentToString(Enchantment e){
    	String estring = "";
    	if(e.equals(Enchantment.ARROW_DAMAGE)) return "power";
    	if(e.equals(Enchantment.ARROW_FIRE)) return "flame";
    	if(e.equals(Enchantment.ARROW_INFINITE)) return "infinity";
    	if(e.equals(Enchantment.ARROW_KNOCKBACK)) return "punch";
    	if(e.equals(Enchantment.DAMAGE_ALL)) return "sharpness";
    	if(e.equals(Enchantment.DAMAGE_ARTHROPODS)) return "baneofarthropods";
    	if(e.equals(Enchantment.DAMAGE_UNDEAD)) return "smite";
    	if(e.equals(Enchantment.DIG_SPEED)) return "efficiency";
    	if(e.equals(Enchantment.DURABILITY)) return "unbreaking";
    	if(e.equals(Enchantment.FIRE_ASPECT)) return "fireaspect";
    	if(e.equals(Enchantment.KNOCKBACK)) return "knockback";
    	if(e.equals(Enchantment.LOOT_BONUS_BLOCKS)) return "fortune";
    	if(e.equals(Enchantment.LOOT_BONUS_MOBS)) return "looting";
    	if(e.equals(Enchantment.OXYGEN)) return "respiration";
    	if(e.equals(Enchantment.PROTECTION_ENVIRONMENTAL)) return "protection";
    	if(e.equals(Enchantment.PROTECTION_EXPLOSIONS)) return "blastprotection";
    	if(e.equals(Enchantment.PROTECTION_FALL)) return "featherfalling";
    	if(e.equals(Enchantment.PROTECTION_FIRE)) return "fireprotection";
    	if(e.equals(Enchantment.PROTECTION_PROJECTILE)) return "projectileprotection";
    	if(e.equals(Enchantment.SILK_TOUCH)) return "silktouch";
    	if(e.equals(Enchantment.WATER_WORKER)) return "aquaaffinity";
    	return estring;
    }
    
    
    public enum ToolType{
    	AXE,
    	PICKAXE,
    	SHOVEL,
    	SWORD,
    	FLINTANDSTEEL,
    	SHEARS,
    	HOE,
    	OTHER;
    }
    
    
}
