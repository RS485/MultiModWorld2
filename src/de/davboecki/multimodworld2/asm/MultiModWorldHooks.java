package de.davboecki.multimodworld2.asm;

import java.util.Map;

import de.davboecki.multimodworld2.asm.interfaces.ISetVanillaPacket;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;

public class MultiModWorldHooks {
	
	public static boolean handlePacket(Packet packet) {
		if(packet instanceof Packet250CustomPayload) {
        	if(!((Packet250CustomPayload)packet).channel.startsWith("MC|")) return false;
    	}
    	if(packet instanceof Packet1Login) {
    		((ISetVanillaPacket)packet).setVanillaPacket();
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
}
