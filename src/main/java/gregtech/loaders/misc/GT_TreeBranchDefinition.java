package gregtech.loaders.misc;

import forestry.api.arboriculture.EnumTreeChromosome;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IAlleleRegistry;
import forestry.api.genetics.IClassification;
import forestry.arboriculture.genetics.BranchTrees;
import forestry.arboriculture.genetics.alleles.AlleleFruit;
import forestry.arboriculture.genetics.alleles.AlleleGrowth;
import forestry.arboriculture.genetics.alleles.AlleleLeafEffect;
import forestry.core.genetics.IBranchDefinition;
import forestry.core.genetics.alleles.AlleleHelper;
import forestry.core.genetics.alleles.AllelePlantType;
import forestry.core.genetics.alleles.EnumAllele;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Arrays;
import java.util.Locale;

/**
 * Created by danie_000 on 18.09.2016.
 */
public enum GT_TreeBranchDefinition implements IBranchDefinition {
    POPLARERER,
    POPLARER;

    private final IClassification branch;

    GT_TreeBranchDefinition() {
        String name = this.name().toLowerCase(Locale.ENGLISH);
        String scientific = WordUtils.capitalize(name);
        branch = new BranchTrees(name, scientific);
    }

    GT_TreeBranchDefinition(String scientific) {
        String name = this.name().toLowerCase(Locale.ENGLISH);
        branch = new BranchTrees(name, scientific);
    }

    private static IAllele[] defaultTemplate;

    @Override
    public IAllele[] getTemplate() {
        if (defaultTemplate == null) {
            defaultTemplate = new IAllele[EnumTreeChromosome.values().length];

            AlleleHelper.instance.set(defaultTemplate, EnumTreeChromosome.FRUITS, AlleleFruit.fruitNone);
            AlleleHelper.instance.set(defaultTemplate, EnumTreeChromosome.GROWTH, AlleleGrowth.growthLightLevel);
            AlleleHelper.instance.set(defaultTemplate, EnumTreeChromosome.HEIGHT, EnumAllele.Height.SMALL);
            AlleleHelper.instance.set(defaultTemplate, EnumTreeChromosome.FERTILITY, EnumAllele.Saplings.LOWER);
            AlleleHelper.instance.set(defaultTemplate, EnumTreeChromosome.YIELD, EnumAllele.Yield.LOWEST);
            AlleleHelper.instance.set(defaultTemplate, EnumTreeChromosome.PLANT, AllelePlantType.plantTypeNone);
            AlleleHelper.instance.set(defaultTemplate, EnumTreeChromosome.SAPPINESS, EnumAllele.Sappiness.LOWEST);
            AlleleHelper.instance.set(defaultTemplate, EnumTreeChromosome.TERRITORY, EnumAllele.Territory.AVERAGE);
            AlleleHelper.instance.set(defaultTemplate, EnumTreeChromosome.EFFECT, AlleleLeafEffect.leavesNone);
            AlleleHelper.instance.set(defaultTemplate, EnumTreeChromosome.MATURATION, EnumAllele.Maturation.AVERAGE);
            AlleleHelper.instance.set(defaultTemplate, EnumTreeChromosome.GIRTH, 1);
            AlleleHelper.instance.set(defaultTemplate, EnumTreeChromosome.FIREPROOF, EnumAllele.Fireproof.FALSE);
        }
        return Arrays.copyOf(defaultTemplate, defaultTemplate.length);
    }

    public final IClassification getBranch() {
        return branch;
    }

    public static void createAlleles() {

        IAlleleRegistry alleleRegistry = AlleleManager.alleleRegistry;

        IClassification plantae = alleleRegistry.getClassification("kingdom.plantae");

        plantae.addMemberGroup(
                alleleRegistry.createAndRegisterClassification(IClassification.EnumClassLevel.DIVISION, "testera", "Testera",
                        alleleRegistry.createAndRegisterClassification(IClassification.EnumClassLevel.CLASS, "testium", "Testium",
                                alleleRegistry.createAndRegisterClassification(IClassification.EnumClassLevel.ORDER, "testes", "Testes",
                                        alleleRegistry.createAndRegisterClassification(IClassification.EnumClassLevel.FAMILY, "testae", "Testae",
                                                POPLARER.getBranch()
                                        ),
                                        alleleRegistry.createAndRegisterClassification(IClassification.EnumClassLevel.FAMILY, "testonae", "Testonae",
                                                POPLARERER.getBranch()
                                        )
                                )
                        )
                )
        );
    }
}
