package de.davboecki.multimodworld2.exchangeworld;

import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.CAVE;
import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.MINESHAFT;
import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.RAVINE;
import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.SCATTERED_FEATURE;
import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.STRONGHOLD;
import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.VILLAGE;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.DUNGEON;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ICE;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAKE;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAVA;

import java.util.List;
import java.util.Random;

import de.davboecki.multimodworld2.util.Material;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.MapGenCaves;
import net.minecraft.world.gen.MapGenRavine;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.feature.MapGenScatteredFeature;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.terraingen.ChunkProviderEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

public class ExchangeWorldChunkProvider implements IChunkProvider {
	/** Reference to the World object. */
	private World worldObj;

	public ExchangeWorldChunkProvider(World par1World) {
		this.worldObj = par1World;
	}

	/**
	 * loads or generates the chunk at the chunk location specified
	 */
	public Chunk loadChunk(int par1, int par2) {
		return this.provideChunk(par1, par2);
	}

	private void addBlock(byte[] result, int x, int y, int z, byte value) {
		result[(x * 16 + z) * 128 + y] = value;
	}

	/**
	 * Will return back a chunk, if it doesn't exist and its not a MP client it
	 * will generates all the blocks for the specified chunk from the map seed
	 * and chunk seed
	 */
	public Chunk provideChunk(int cx, int cz) {
		byte[] result = new byte[32768];

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				addBlock(result, x, 0, z, (byte) Material.BEDROCK.getId());
				for (int y = 1; y < 64; y++) {
					addBlock(result, x, y, z, (byte) Material.DIRT.getId());
				}
				addBlock(result, x, 10, z, (byte) Material.BEDROCK.getId());
				addBlock(result, x, 30, z, (byte) Material.BEDROCK.getId());
				addBlock(result, x, 54, z, (byte) Material.BEDROCK.getId());
				addBlock(result, x, 64, z, (byte) Material.GRASS.getId());
			}
		}

		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 16; j++) {
				addBlock(result, 0, i + 10, j, (byte) Material.BEDROCK.getId());
				addBlock(result, 15, i + 10, j, (byte) Material.BEDROCK.getId());
				addBlock(result, j, i + 10, 0, (byte) Material.BEDROCK.getId());
				addBlock(result, j, i + 10, 15, (byte) Material.BEDROCK.getId());
			}
		}
		for (int x = 3; x < 12; x++) {
			for (int z = 3; z < 12; z++) {
				for (int y = 11; y < 24; y++) {
					addBlock(result, x, y, z, (byte) 0);
				}
				addBlock(result, x, 11, z, (byte) Material.STONE.getId());
				addBlock(result, x, 16, z, (byte) Material.STONE.getId());
				addBlock(result, x, 17, z, (byte) Material.STONE.getId());
				addBlock(result, x, 22, z, (byte) Material.STONE.getId());
				addBlock(result, x, 23, z, (byte) Material.STONE.getId());
				addBlock(result, x, 24, z, (byte) Material.BEDROCK.getId());
			}
		}

		for (int y = 11; y < 24; y++) {
			for (int i = 2; i < 12; i++) {
				addBlock(result, 2, y, i, (byte) Material.BEDROCK.getId());
				addBlock(result, 12, y, i, (byte) Material.BEDROCK.getId());
				addBlock(result, i, y, 2, (byte) Material.BEDROCK.getId());
				addBlock(result, i, y, 12, (byte) Material.BEDROCK.getId());
			}
			for (int i = 3; i < 11; i++) {
				addBlock(result, 3, y, i, (byte) Material.STONE.getId());
				addBlock(result, 11, y, i, (byte) Material.STONE.getId());
				addBlock(result, i, y, 3, (byte) Material.STONE.getId());
				addBlock(result, i, y, 11, (byte) Material.STONE.getId());
			}
		}

		for (int x = 6; x < 9; x++) {
			for (int z = 6; z < 9; z++) {
				addBlock(result, x, 16, z, (byte) Material.GLOWSTONE.getId());
				addBlock(result, x, 22, z, (byte) Material.GLOWSTONE.getId());
			}
		}
		addBlock(result, 7, 16, 7, (byte) Material.REDSTONEBLOCK.getId());
		addBlock(result, 7, 22, 7, (byte) Material.REDSTONEBLOCK.getId());

		addBlock(result, 7, 16, 4, (byte) 0);
		addBlock(result, 7, 17, 4, (byte) 0);
		addBlock(result, 7, 16, 10, (byte) 0);
		addBlock(result, 7, 17, 10, (byte) 0);

		addBlock(result, 4, 12, 6, (byte) Material.STONE.getId());
		addBlock(result, 4, 13, 6, (byte) Material.STONE.getId());
		addBlock(result, 4, 14, 6, (byte) Material.STONE.getId());
		addBlock(result, 4, 14, 7, (byte) Material.STONE.getId());
		addBlock(result, 4, 14, 8, (byte) Material.STONE.getId());
		addBlock(result, 4, 13, 8, (byte) Material.STONE.getId());
		addBlock(result, 4, 12, 8, (byte) Material.STONE.getId());

		addBlock(result, 10, 12, 6, (byte) Material.STONE.getId());
		addBlock(result, 10, 13, 6, (byte) Material.STONE.getId());
		addBlock(result, 10, 14, 6, (byte) Material.STONE.getId());
		addBlock(result, 10, 14, 7, (byte) Material.STONE.getId());
		addBlock(result, 10, 14, 8, (byte) Material.STONE.getId());
		addBlock(result, 10, 13, 8, (byte) Material.STONE.getId());
		addBlock(result, 10, 12, 8, (byte) Material.STONE.getId());

		if (cx == 0) {
			for (int z = 0; z < 16; z++) {
				addBlock(result, 0, 65, z, (byte) Material.LAVA.getId());
				addBlock(result, 1, 65, z, (byte) Material.GRASS.getId());
				for (int y = 1; y < 64; y++) {
					addBlock(result, 1, y, z, (byte) Material.BEDROCK.getId());
				}
			}

			if (cz == 0) {
				for (int z = 0; z < 16; z++) {
					for (int y = 65; y < 71; y++) {
						addBlock(result, 0, y, z, (byte) Material.LAVA.getId());
						addBlock(result, 1, y, z, (byte) Material.GLASS.getId());
					}
				}

				for (int x = 2; x < 16; x++) {
					addBlock(result, x, 65, 0, (byte) Material.GRASS.getId());
				}
				for (int z = 0; z < 16; z++) {
					addBlock(result, 15, 65, z, (byte) Material.GRASS.getId());
				}

				addBlock(result, 2, 65, 9, (byte) Material.STONE.getId());
				addBlock(result, 2, 66, 9, (byte) Material.STONE.getId());
				addBlock(result, 2, 67, 9, (byte) Material.STONE.getId());
				addBlock(result, 2, 67, 10, (byte) Material.STONE.getId());
				addBlock(result, 2, 67, 11, (byte) Material.STONE.getId());
				addBlock(result, 2, 66, 11, (byte) Material.STONE.getId());
				addBlock(result, 2, 65, 11, (byte) Material.STONE.getId());
			} else if (cz == 1) {
				for (int z = 0; z < 6; z++) {
					for (int y = 65; y < 71; y++) {
						addBlock(result, 0, y, z, (byte) Material.LAVA.getId());
						addBlock(result, 1, y, z, (byte) Material.GLASS.getId());
					}
				}

				for (int x = 2; x < 16; x++) {
					addBlock(result, x, 65, 5, (byte) Material.GRASS.getId());
				}
				for (int z = 0; z < 6; z++) {
					addBlock(result, 15, 65, z, (byte) Material.GRASS.getId());
				}

			}
		} else if (cx == -1) {
			for (int z = 0; z < 16; z++) {
				addBlock(result, 15, 65, z, (byte) Material.GRASS.getId());
				for (int y = 1; y < 64; y++) {
					addBlock(result, 15, y, z, (byte) Material.BEDROCK.getId());
				}
			}

			if (cz == 0) {
				for (int z = 0; z < 16; z++) {
					for (int y = 65; y < 71; y++) {
						addBlock(result, 15, y, z,
								(byte) Material.GLASS.getId());
					}
				}

				for (int x = 1; x < 15; x++) {
					addBlock(result, x, 65, 0, (byte) Material.GRASS.getId());
				}
				for (int z = 0; z < 16; z++) {
					addBlock(result, 0, 65, z, (byte) Material.GRASS.getId());
				}

				addBlock(result, 14, 65, 9, (byte) Material.STONE.getId());
				addBlock(result, 14, 66, 9, (byte) Material.STONE.getId());
				addBlock(result, 14, 67, 9, (byte) Material.STONE.getId());
				addBlock(result, 14, 67, 10, (byte) Material.STONE.getId());
				addBlock(result, 14, 67, 11, (byte) Material.STONE.getId());
				addBlock(result, 14, 66, 11, (byte) Material.STONE.getId());
				addBlock(result, 14, 65, 11, (byte) Material.STONE.getId());
			} else if (cz == 1) {
				for (int z = 0; z < 6; z++) {
					for (int y = 65; y < 71; y++) {
						addBlock(result, 15, y, z,
								(byte) Material.GLASS.getId());
					}
				}

				for (int x = 1; x < 15; x++) {
					addBlock(result, x, 65, 5, (byte) Material.GRASS.getId());
				}
				for (int z = 0; z < 6; z++) {
					addBlock(result, 0, 65, z, (byte) Material.GRASS.getId());
				}

			}
		}

		Chunk chunk = new Chunk(this.worldObj, result, cx, cz);
		chunk.generateSkylightMap();
		return chunk;
	}

	/**
	 * Populates chunk with ores etc etc
	 */
	public void populate(IChunkProvider provider, int cX, int cZ) {
		int baseX = cX << 4;
		int baseZ = cZ << 4;
		worldObj.setBlock(baseX + 5, 14, baseZ + 4, Material.TORCH.getId(), 3, 2);
		worldObj.setBlock(baseX + 5, 14, baseZ + 6, Material.TORCH.getId(), 1, 2);
		worldObj.setBlock(baseX + 5, 14, baseZ + 8, Material.TORCH.getId(), 1, 2);
		worldObj.setBlock(baseX + 5, 14, baseZ + 10, Material.TORCH.getId(), 4, 2);

		worldObj.setBlock(baseX + 9, 14, baseZ + 4, Material.TORCH.getId(), 3, 2);
		worldObj.setBlock(baseX + 9, 14, baseZ + 6, Material.TORCH.getId(), 2, 2);
		worldObj.setBlock(baseX + 9, 14, baseZ + 8, Material.TORCH.getId(), 2, 2);
		worldObj.setBlock(baseX + 9, 14, baseZ + 10, Material.TORCH.getId(), 4, 2);
		
		worldObj.setBlock(baseX + 5, 12, baseZ + 4, Material.CARPET.getId(), 11, 2);
		worldObj.setBlock(baseX + 5, 12, baseZ + 10, Material.CARPET.getId(), 11, 2);
		worldObj.setBlock(baseX + 9, 12, baseZ + 4, Material.CARPET.getId(), 11, 2);
		worldObj.setBlock(baseX + 9, 12, baseZ + 10, Material.CARPET.getId(), 11, 2);
		for(int x = 5;x < 10;x++) {
			for(int z = 5;z < 10;z++) {
				worldObj.setBlock(baseX + x, 12, baseZ + z, Material.CARPET.getId(), 11, 2);
			}
			worldObj.setBlock(baseX + x, 12, baseZ + 7, Material.CARPET.getId(), 14, 2);
		}
		for(int x = 4;x < 11;x++) {
			for(int z = 4;z < 11;z++) {
				worldObj.setBlock(baseX + x, 18, baseZ + z, Material.CARPET.getId(), 11, 2);
			}
		}
		for(int z = 5;z < 10;z++) {
			worldObj.setBlock(baseX + 7, 18, baseZ + z, Material.CARPET.getId(), 15, 2);
			if(z == 7) continue;
			worldObj.setBlock(baseX + 7, 12, baseZ + z, Material.CARPET.getId(), 15, 2);
		}
		worldObj.setBlock(baseX + 4, 12, baseZ + 7, Material.CARPET.getId(), 14, 2);
		worldObj.setBlock(baseX + 10, 12, baseZ + 7, Material.CARPET.getId(), 14, 2);

		for(int i = 12;i < 20;i++) {
			worldObj.setBlock(baseX + 7, i, baseZ + 4, Material.LADDER.getId(), 3, 2);
			worldObj.setBlock(baseX + 7, i, baseZ + 10, Material.LADDER.getId(), 2, 2);
		}
		
		for(int y = 12;y < 21; y++) {
			if(y > 14 && y < 18) continue;
			worldObj.setBlock(baseX + 4, y, baseZ + 4, Material.CHEST.getId(), 1, 2);
			worldObj.setBlock(baseX + 4, y, baseZ + 5, Material.CHEST.getId(), 1, 2);
			worldObj.setBlock(baseX + 4, y, baseZ + 9, Material.CHEST.getId(), 1, 2);
			worldObj.setBlock(baseX + 4, y, baseZ + 10, Material.CHEST.getId(), 1, 2);

			worldObj.setBlock(baseX + 10, y, baseZ + 4, Material.CHEST.getId(), 2, 2);
			worldObj.setBlock(baseX + 10, y, baseZ + 5, Material.CHEST.getId(), 2, 2);
			worldObj.setBlock(baseX + 10, y, baseZ + 9, Material.CHEST.getId(), 2, 2);
			worldObj.setBlock(baseX + 10, y, baseZ + 10, Material.CHEST.getId(), 2, 2);

			worldObj.setBlock(baseX + 6, y, baseZ + 10, Material.CHEST.getId(), 3, 2);
			worldObj.setBlock(baseX + 8, y, baseZ + 10, Material.CHEST.getId(), 3, 2);
			
			worldObj.setBlock(baseX + 6, y, baseZ + 4, Material.CHEST.getId(), 4, 2);
			worldObj.setBlock(baseX + 8, y, baseZ + 4, Material.CHEST.getId(), 4, 2);
			if(y > 17) {
				worldObj.setBlock(baseX + 4, y, baseZ + 7, Material.CHEST.getId(), 4, 2);
				worldObj.setBlock(baseX + 10, y, baseZ + 7, Material.CHEST.getId(), 4, 2);
			}
		}

		worldObj.setBlock(baseX + 5, 20, baseZ + 4, Material.TORCH.getId(), 3, 2);
		worldObj.setBlock(baseX + 4, 20, baseZ + 6, Material.TORCH.getId(), 1, 2);
		worldObj.setBlock(baseX + 4, 20, baseZ + 8, Material.TORCH.getId(), 1, 2);
		worldObj.setBlock(baseX + 5, 20, baseZ + 10, Material.TORCH.getId(), 4, 2);

		worldObj.setBlock(baseX + 9, 20, baseZ + 4, Material.TORCH.getId(), 3, 2);
		worldObj.setBlock(baseX + 10, 20, baseZ + 6, Material.TORCH.getId(), 2, 2);
		worldObj.setBlock(baseX + 10, 20, baseZ + 8, Material.TORCH.getId(), 2, 2);
		worldObj.setBlock(baseX + 9, 20, baseZ + 10, Material.TORCH.getId(), 4, 2);

		worldObj.setBlock(baseX + 7, 20, baseZ + 4, Material.TORCH.getId(), 3, 2);
		worldObj.setBlock(baseX + 7, 20, baseZ + 10, Material.TORCH.getId(), 4, 2);
	}

	/**
	 * Checks to see if a chunk exists at x, y
	 */
	public boolean chunkExists(int par1, int par2) {
		return true;
	}

	/**
	 * Two modes of operation: if passed true, save all Chunks in one go. If
	 * passed false, save up to two chunks. Return true if all chunks have been
	 * saved.
	 */
	public boolean saveChunks(boolean par1, IProgressUpdate par2IProgressUpdate) {
		return true;
	}

	public void func_104112_b() {
	}

	/**
	 * Unloads chunks that are marked to be unloaded. This is not guaranteed to
	 * unload every such chunk.
	 */
	public boolean unloadQueuedChunks() {
		return false;
	}

	/**
	 * Returns if the IChunkProvider supports saving.
	 */
	public boolean canSave() {
		return true;
	}

	/**
	 * Converts the instance data to a readable string.
	 */
	public String makeString() {
		return "RandomLevelSource";
	}

	/**
	 * Returns a list of creatures of the specified type that can spawn at the
	 * given location.
	 */
	public List getPossibleCreatures(EnumCreatureType par1EnumCreatureType,
			int par2, int par3, int par4) {
		return null;
	}

	/**
	 * Returns the location of the closest structure of the specified type. If
	 * not found returns null.
	 */
	public ChunkPosition findClosestStructure(World par1World, String par2Str,
			int par3, int par4, int par5) {
		return null;
	}

	public int getLoadedChunkCount() {
		return 0;
	}

	public void recreateStructures(int par1, int par2) {
	}
}
