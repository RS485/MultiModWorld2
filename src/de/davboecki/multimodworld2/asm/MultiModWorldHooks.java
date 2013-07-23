package de.davboecki.multimodworld2.asm;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet10Flying;
import net.minecraft.network.packet.Packet13PlayerLookMove;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.network.packet.Packet9Respawn;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;
import de.davboecki.multimodworld2.asm.interfaces.IMovingListener;
import de.davboecki.multimodworld2.asm.interfaces.ISetVanillaPacket;

public class MultiModWorldHooks {
	
	public static boolean handlePacket(Packet packet) {
		if(packet instanceof Packet250CustomPayload) {
        	if(!((Packet250CustomPayload)packet).channel.startsWith("MC|")) return false;
    	}
    	if(packet instanceof Packet1Login) {
    		((ISetVanillaPacket)packet).setVanillaPacket();
    		if(((Packet1Login)packet).dimension == -10)  {
    			((Packet1Login)packet).dimension = 0;
    		}
    	}
    	if(packet instanceof Packet9Respawn) {
    		if(((Packet9Respawn)packet).respawnDimension == -10)  {
    			((Packet9Respawn)packet).respawnDimension = 0;
    		}
    	}
    	return true;
	}
	
	//Copy From FMLNetworkHandler
	// List of states for connections from clients to server
    static final int LOGIN_RECEIVED = 1;
    static final int CONNECTION_VALID = 2;
    static final int FML_OUT_OF_DATE = -1;
    static final int MISSING_MODS_OR_VERSIONS = -2;
    
	
	public static void handleConnection(Map<NetLoginHandler, Integer> loginStates, NetLoginHandler netLoginHandler, String userName) {
		System.out.println("Login");
		if(loginStates.containsKey(netLoginHandler)) {
			//TODO FML Player
			return;
		} else {
			//TODO Vanilla Player
			loginStates.put(netLoginHandler, LOGIN_RECEIVED);
		}
	}


	public static boolean sendModRequest(Map<NetLoginHandler, Integer> loginStates, NetLoginHandler netLoginHandler, String userName) {
		System.out.println("Send ModRequest");
        netLoginHandler.completeConnection(null);
        loginStates.remove(netLoginHandler);
		return false;
	}
	
	public static boolean setBlock(int x, int y, int z, int id, int meta, World world, WorldInfo worldInfo) {
		int dimension = world.provider.dimensionId;
		String worldName = worldInfo.getWorldName();
		return true;
	}


	public static boolean setBlockTileEntity(int x, int y, int z, TileEntity tile, World world, WorldInfo worldInfo) {
		return true;
	}


	public static boolean spawnEntityInWorld(Entity entity, World world, WorldInfo worldInfo) {
		if(entity instanceof EntityPlayer) return true;
		return true;
	}

	public static List<IMovingListener> movingListener = new LinkedList<IMovingListener>();

	public static boolean handlePlayerMove(EntityPlayerMP playerEntity, Packet10Flying par1Packet10Flying, boolean hasMoved) {
		if(!hasMoved) return false;
		if(playerEntity.dimension != -10) return false;
		if (par1Packet10Flying.moving && !(par1Packet10Flying.moving && par1Packet10Flying.yPosition == -999.0D && par1Packet10Flying.stance == -999.0D)) {
			if(playerEntity.posX != par1Packet10Flying.xPosition || playerEntity.posY != par1Packet10Flying.yPosition || playerEntity.posZ != par1Packet10Flying.zPosition) {
				boolean cancel = false;
				for(IMovingListener listener:movingListener) {
					cancel |= !listener.playerTryesToMoveTo(playerEntity, par1Packet10Flying.xPosition, par1Packet10Flying.yPosition, par1Packet10Flying.zPosition);
				}
				if(cancel) {
					playerEntity.playerNetServerHandler.setPlayerLocation(playerEntity.posX, playerEntity.posY, playerEntity.posZ, playerEntity.rotationYaw, playerEntity.rotationPitch);
					return true;
				}
			}
		}
		return false;
	}
}
