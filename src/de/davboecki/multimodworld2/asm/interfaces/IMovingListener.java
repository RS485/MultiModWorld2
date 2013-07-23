package de.davboecki.multimodworld2.asm.interfaces;

import net.minecraft.entity.player.EntityPlayerMP;

public interface IMovingListener {
	public boolean playerTryesToMoveTo(EntityPlayerMP player, double xPosition, double yPosition, double zPosition);
}
