package de.davboecki.multimodworld2.asm;
import java.util.Map;

import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.network.packet.Packet250CustomPayload;


public class MultiModWorldHooks {
	
	public static boolean handlePacket(Packet packet) {
		if(packet instanceof Packet250CustomPayload) {
        	if(!((Packet250CustomPayload)packet).channel.startsWith("MC|")) return false;
    	}
    	if(packet instanceof Packet1Login) {
    		((Packet1Login)packet).vanillaCompatible = true;
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
}
