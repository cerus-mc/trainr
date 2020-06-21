package xyz.trainr.trainr.worldgen;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class TrainrChunkGenerator extends ChunkGenerator {

    @Override
    public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
        ChunkData chunkData = createChunkData(world);
        for (int X = 0; X < 16; X++) {
            for (int Z = 0; Z < 16; Z++) {
                for (int Y = 0; Y < world.getMaxHeight(); Y++) {
                    chunkData.setBlock(X, Y, Z, Material.AIR);
                }
            }
        }
        return chunkData;
    }

}
