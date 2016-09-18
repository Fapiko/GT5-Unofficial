package gregtech.loaders.misc;

import com.mojang.authlib.GameProfile;
import forestry.api.arboriculture.*;
import forestry.api.core.EnumHumidity;
import forestry.api.core.EnumTemperature;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;
import forestry.api.world.ITreeGenData;
import forestry.arboriculture.blocks.BlockForestryLeaves;
import forestry.arboriculture.genetics.ITreeDefinition;
import forestry.arboriculture.genetics.Tree;
import forestry.arboriculture.genetics.TreeBranchDefinition;
import forestry.arboriculture.genetics.TreeDefinition;
import forestry.arboriculture.genetics.alleles.AlleleFruit;
import forestry.arboriculture.genetics.alleles.AlleleGrowth;
import forestry.arboriculture.render.IconProviderGermling;
import forestry.arboriculture.render.IconProviderGermlingVanilla;
import forestry.arboriculture.tiles.TileLeaves;
import forestry.arboriculture.worldgen.*;
import forestry.core.config.Constants;
import forestry.core.genetics.alleles.AlleleBoolean;
import forestry.core.genetics.alleles.AlleleHelper;
import forestry.core.genetics.alleles.AllelePlantType;
import forestry.core.genetics.alleles.EnumAllele;
import forestry.plugins.PluginArboriculture;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.util.ForgeDirection;

import java.awt.*;
import java.util.Arrays;

/**
 * Created by danie_000 on 18.09.2016.
 */
public enum GT_TreeDefinition implements ITreeDefinition, ITreeGenerator {
    Poplarerer(GT_TreeBranchDefinition.POPLARERER, "whitePoplarerer", "albaerer", true, EnumLeafType.DECIDUOUS, new Color(0x00ffff), new Color(0x00ffff), EnumWoodType.POPLAR) {
        @Override
        public WorldGenerator getWorldGenerator(ITreeGenData tree) {
            return new WorldGenPoplar(tree);
        }

        @Override
        protected void setSpeciesProperties(IAlleleTreeSpeciesCustom treeSpecies) {
            treeSpecies.addFruitFamily(EnumFruitFamily.PRUNES)
                    .addFruitFamily(EnumFruitFamily.POMES);
        }

        @Override
        protected void setAlleles(IAllele[] alleles) {
            AlleleHelper.instance.set(alleles, EnumTreeChromosome.HEIGHT, EnumAllele.Height.SMALL);
            AlleleHelper.instance.set(alleles, EnumTreeChromosome.SAPPINESS, EnumAllele.Sappiness.LOW);
            AlleleHelper.instance.set(alleles, EnumTreeChromosome.MATURATION, EnumAllele.Maturation.SLOWER);
        }

        @Override
        protected void registerMutations() {
            registerMutation(getSpecies("Lime"),getSpecies("Mahoe"),10);
        }
    },
    Poplarer(GT_TreeBranchDefinition.POPLARER, "whitePoplarer", "albaer", true, EnumLeafType.DECIDUOUS, new Color(0x0000ff), new Color(0x0000ff), EnumWoodType.POPLAR) {
        @Override
        public WorldGenerator getWorldGenerator(ITreeGenData tree) {
            return new WorldGenPoplar(tree);
        }

        @Override
        protected void setSpeciesProperties(IAlleleTreeSpeciesCustom treeSpecies) {
            treeSpecies.addFruitFamily(EnumFruitFamily.PRUNES)
                    .addFruitFamily(EnumFruitFamily.POMES);
        }

        @Override
        protected void setAlleles(IAllele[] alleles) {
            AlleleHelper.instance.set(alleles, EnumTreeChromosome.HEIGHT, EnumAllele.Height.SMALL);
            AlleleHelper.instance.set(alleles, EnumTreeChromosome.SAPPINESS, EnumAllele.Sappiness.LOW);
            AlleleHelper.instance.set(alleles, EnumTreeChromosome.MATURATION, EnumAllele.Maturation.SLOWER);
        }

        @Override
        protected void registerMutations() {
            ITreeMutationCustom tMutation= registerMutation(getSpecies("Mahoe"),Poplarerer.species,10);
        }
    };

    private final GT_TreeBranchDefinition branch;
    private final IAlleleTreeSpeciesCustom species;

    private final EnumWoodType woodType; // for forestry trees
    private final ItemStack vanillaWood; // for vanilla trees

    private IAllele[] template;
    private ITreeGenome genome;

    // vanilla tree constructor
    GT_TreeDefinition(GT_TreeBranchDefinition branch, String speciesName, String binomial, boolean dominant, EnumLeafType leafType, Color primary, Color secondary, int vanillaMeta, ItemStack vanillaWood) {
        String uid = "forestry.tree" + this;
        String unlocalizedDescription = "for.description.tree" + this;
        String unlocalizedName = "for.trees.species." + speciesName;

        this.branch = branch;

        ILeafIconProvider leafIconProvider = TreeManager.treeFactory.getLeafIconProvider(leafType, primary, secondary);
        IGermlingIconProvider germlingIconProvider = new IconProviderGermlingVanilla(vanillaMeta);

        this.species = TreeManager.treeFactory.createSpecies(uid, unlocalizedName, "GREGTECH", unlocalizedDescription, dominant, branch.getBranch(), binomial, leafIconProvider, germlingIconProvider, this);
        this.woodType = null;
        this.vanillaWood = vanillaWood;
    }

    // forestry tree constructor
    GT_TreeDefinition(GT_TreeBranchDefinition branch, String speciesName, String binomial, boolean dominant, EnumLeafType leafType, Color primary, Color secondary, EnumWoodType woodType) {
        String uid = "forestry.tree" + this;
        String unlocalizedDescription = "for.description.tree" + this;
        String unlocalizedName = "for.trees.species." + speciesName;

        this.branch = branch;

        ILeafIconProvider leafIconProvider = TreeManager.treeFactory.getLeafIconProvider(leafType, primary, secondary);
        IGermlingIconProvider germlingIconProvider = new IconProviderGermling(uid);

        this.species = TreeManager.treeFactory.createSpecies(uid, unlocalizedName, "GREGTECH", unlocalizedDescription, dominant, branch.getBranch(), binomial, leafIconProvider, germlingIconProvider, this);
        this.woodType = woodType;
        this.vanillaWood = null;
    }

    protected abstract void setSpeciesProperties(IAlleleTreeSpeciesCustom treeSpecies);

    protected abstract void setAlleles(IAllele[] alleles);

    protected abstract void registerMutations();

    @Override
    public void setLogBlock(ITreeGenome genome, World world, int x, int y, int z, ForgeDirection facing) {
        if (woodType == null) {
            Block vanillaWoodBlock = Block.getBlockFromItem(vanillaWood.getItem());
            int vanillaWoodMeta = vanillaWood.getItemDamage();
            switch (facing) {
                case NORTH:
                case SOUTH:
                    vanillaWoodMeta |= 2 << 2;
                    break;
                case EAST:
                case WEST:
                    vanillaWoodMeta |= 1 << 2;
                    break;
            }

            world.setBlock(x, y, z, vanillaWoodBlock, vanillaWoodMeta, Constants.FLAG_BLOCK_SYNCH);
        } else {
            AlleleBoolean fireproofAllele = (AlleleBoolean) genome.getActiveAllele(EnumTreeChromosome.FIREPROOF);
            boolean fireproof = fireproofAllele.getValue();
            ItemStack log = TreeManager.woodItemAccess.getLog(woodType, fireproof);

            BlockTypeLog logBlock = new BlockTypeLog(log);
            logBlock.setDirection(facing);
            logBlock.setBlock(world, x, y, z);
        }
    }

    @Override
    public void setLogBlock(World world, int x, int y, int z, ForgeDirection facing) {
        setLogBlock(genome, world, x, y, z, facing);
    }

    @Override
    public void setLeaves(ITreeGenome genome, World world, GameProfile owner, int x, int y, int z, boolean decorative) {
        boolean placed = world.setBlock(x, y, z, PluginArboriculture.blocks.leaves, 0, Constants.FLAG_BLOCK_SYNCH_AND_UPDATE);
        if (!placed) {
            return;
        }

        Block block = world.getBlock(x, y, z);
        if (PluginArboriculture.blocks.leaves != block) {
            world.setBlockToAir(x, y, z);
            return;
        }

        TileLeaves tileLeaves = BlockForestryLeaves.getLeafTile(world, x, y, z);
        if (tileLeaves == null) {
            world.setBlockToAir(x, y, z);
            return;
        }

        tileLeaves.setOwner(owner);
        if (decorative) {
            tileLeaves.setDecorative();
        }
        tileLeaves.setTree(new Tree(genome));

        world.markBlockForUpdate(x, y, z);
    }

    @Override
    public void setLeaves(World world, GameProfile owner, int x, int y, int z, boolean decorative) {
        setLeaves(genome, world, owner, x, y, z, decorative);
    }

    public static void initTrees() {
        for (GT_TreeDefinition tree : values()) {
            tree.init();
        }
        for (GT_TreeDefinition tree : values()) {
            tree.registerMutations();
        }
    }

    private static IAlleleTreeSpecies getSpecies(String name) {
        return (IAlleleTreeSpecies) AlleleManager.alleleRegistry.getAllele((new StringBuilder()).append("forestry.tree").append(name).toString());
    }

    private void init() {
        setSpeciesProperties(species);

        template = branch.getTemplate();
        AlleleHelper.instance.set(template, EnumTreeChromosome.SPECIES, species);
        setAlleles(template);

        genome = TreeManager.treeRoot.templateAsGenome(template);

        TreeManager.treeRoot.registerTemplate(template);
    }

    protected final ITreeMutationCustom registerMutation(IAlleleTreeSpecies parent1, IAlleleTreeSpecies parent2, int chance) {
        return TreeManager.treeMutationFactory.createMutation(parent1, parent2, getTemplate(), chance);
    }

    @Override
    public final IAllele[] getTemplate() {
        return Arrays.copyOf(template, template.length);
    }

    public final String getUID() {
        return species.getUID();
    }

    @Override
    public final ITreeGenome getGenome() {
        return genome;
    }

    @Override
    public final ITree getIndividual() {
        return new Tree(genome);
    }

    @Override
    public final ItemStack getMemberStack(EnumGermlingType treeType) {
        ITree tree = getIndividual();
        return TreeManager.treeRoot.getMemberStack(tree, treeType.ordinal());
    }
}