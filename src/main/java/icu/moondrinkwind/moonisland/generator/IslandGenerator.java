package icu.moondrinkwind.moonisland.generator;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class IslandGenerator extends ChunkGenerator {
    @Override
    public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
        ChunkData chunkData = createChunkData(world);
        chunkData.setRegion(0, 0, 0, 16, 64, 16, Material.WATER);
        return chunkData;
    }
}
