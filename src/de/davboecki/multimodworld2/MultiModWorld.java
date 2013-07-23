package de.davboecki.multimodworld2;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.ForgeSubscribe;
import cpw.mods.fml.common.IPlayerTracker;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.ServerStarted;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.davboecki.multimodworld2.asm.MultiModWorldHooks;
import de.davboecki.multimodworld2.asm.interfaces.IMovingListener;
import de.davboecki.multimodworld2.commands.MMWCommandHandler;
import de.davboecki.multimodworld2.exchangeworld.ExchangeWorldProvider;

@Mod(modid = "MMW",
			name = "MultiModWorld 2",
			version = "0.0.1a")
@SideOnly(Side.SERVER)
public class MultiModWorld {
	
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		DimensionManager.registerProviderType(-10, ExchangeWorldProvider.class, true);
		DimensionManager.registerDimension(-10, -10);
		MultiModWorldHooks.movingListener.add(new IMovingListener() {
			@Override
			public boolean playerTryesToMoveTo(EntityPlayerMP player, double xPosition, double yPosition, double zPosition) {
				if(yPosition < 50) return true;
				if(player.posX < -0.3 && xPosition > -0.3) {
						
					return false;
				}
				if(player.posX > 1.3 && xPosition < 1.3) {
					
					return false;
				}
				return true;
			}
		});
	}
	
	@EventHandler
	public void startServer(FMLServerStartingEvent event) {
		event.registerServerCommand(new MMWCommandHandler());
	}
}
