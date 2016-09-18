package gregtech.loaders.misc;

import cpw.mods.fml.common.Loader;

/**
 * Created by danie_000 on 18.09.2016.
 */
public class GT_Trees {
    public GT_Trees() {
        if (Loader.isModLoaded("Forestry")) {
            GT_TreeDefinition.initTrees();
        }
    }
}
