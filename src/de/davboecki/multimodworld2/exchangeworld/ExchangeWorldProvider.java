package de.davboecki.multimodworld2.exchangeworld;

import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;

public class ExchangeWorldProvider extends WorldProvider {
	@Override
	public String getDimensionName() {
		return "MMW ExchangeWorld";
	}

	@Override
	public String getSaveFolder() {
		return "EXCHANGE_DIM" + dimensionId;
	}

	@Override
	public IChunkProvider createChunkGenerator() {
		return new ExchangeWorldChunkProvider(this.worldObj);
	}

	@Override
	public ChunkCoordinates getSpawnPoint() {
        return new ChunkCoordinates(-13, 65, 10);
	}

	@Override
	public ChunkCoordinates getRandomizedSpawnPoint() {
		return getSpawnPoint();
	}
}
