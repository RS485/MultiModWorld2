package de.davboecki.multimodworld2.commands;

import java.util.Arrays;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.server.FMLServerHandler;

public class MMWCommandHandler implements ICommand {

	@Override
	public int compareTo(Object arg0) {
		if(arg0 instanceof ICommand) {
			return 0;
		}
		return 0;
	}

	@Override
	public String getCommandName() {
		return "MultiModWorld";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return null;
	}

	@Override
	public List getCommandAliases() {
		return Arrays.asList(new String[]{"mmw", "mmw2", "MultiModWorld2"});
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		if(icommandsender instanceof EntityPlayerMP) {
			if(astring.length > 0 && "pos".equals(astring[0])) {
				icommandsender.sendChatToPlayer(ChatMessageComponent.func_111066_d("X: " + ((EntityPlayerMP)icommandsender).posX));
				icommandsender.sendChatToPlayer(ChatMessageComponent.func_111066_d("Y: " + ((EntityPlayerMP)icommandsender).posY));
				icommandsender.sendChatToPlayer(ChatMessageComponent.func_111066_d("Z: " + ((EntityPlayerMP)icommandsender).posZ));
			} else if(((EntityPlayerMP)icommandsender).dimension != -10) {
				MinecraftServer.getServerConfigurationManager(FMLServerHandler.instance().getServer()).transferPlayerToDimension((EntityPlayerMP) icommandsender, -10);
			} else {
				MinecraftServer.getServerConfigurationManager(FMLServerHandler.instance().getServer()).transferPlayerToDimension((EntityPlayerMP) icommandsender, 0);
			}
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender icommandsender) {
		return icommandsender instanceof EntityPlayerMP;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender icommandsender, String[] astring) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] astring, int i) {
		return false;
	}

}
