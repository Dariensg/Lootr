package noobanidus.mods.lootr.gen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import noobanidus.mods.lootr.LootrTags;
import noobanidus.mods.lootr.init.ModBlocks;
import org.jetbrains.annotations.Nullable;

public class LootrBlockTagProvider extends BlockTagsProvider {
  public LootrBlockTagProvider(DataGenerator p_126511_, String modId, @Nullable ExistingFileHelper existingFileHelper) {
    super(p_126511_, modId, existingFileHelper);
  }

  @Override
  protected void addTags() {
    tag(BlockTags.SHULKER_BOXES).add(ModBlocks.SHULKER);
    tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.TROPHY);
    tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.CHEST, ModBlocks.TRAPPED_CHEST, ModBlocks.BARREL);
    tag(BlockTags.GUARDED_BY_PIGLINS).add(ModBlocks.CHEST, ModBlocks.TRAPPED_CHEST, ModBlocks.BARREL);
    tag(Tags.Blocks.CHESTS_WOODEN).add(ModBlocks.CHEST);
    tag(Tags.Blocks.CHESTS_TRAPPED).add(ModBlocks.TRAPPED_CHEST);
    tag(Tags.Blocks.BARRELS).add(ModBlocks.BARREL);

    tag(LootrTags.Blocks.BARRELS).add(ModBlocks.BARREL);
    tag(LootrTags.Blocks.CHESTS).add(ModBlocks.CHEST, ModBlocks.INVENTORY);
    tag(LootrTags.Blocks.TRAPPED_CHESTS).add(ModBlocks.TRAPPED_CHEST);
    tag(LootrTags.Blocks.SHULKERS).add(ModBlocks.SHULKER);
    //noinspection unchecked
    tag(LootrTags.Blocks.CONTAINERS).addTags(LootrTags.Blocks.BARRELS, LootrTags.Blocks.CHESTS, LootrTags.Blocks.TRAPPED_CHESTS, LootrTags.Blocks.SHULKERS);

    tag(LootrTags.Blocks.CONVERT_BARRELS).add(Blocks.BARREL);
    tag(LootrTags.Blocks.CONVERT_CHESTS).add(Blocks.CHEST);
    tag(LootrTags.Blocks.CONVERT_TRAPPED_CHESTS).add(Blocks.TRAPPED_CHEST);
    tag(LootrTags.Blocks.CONVERT_SHULKERS).add(Blocks.SHULKER_BOX);
    tag(LootrTags.Blocks.CONVERT_BLOCK).addTags(LootrTags.Blocks.CONVERT_BARRELS, LootrTags.Blocks.CONVERT_CHESTS, LootrTags.Blocks.CONVERT_TRAPPED_CHESTS, LootrTags.Blocks.CONVERT_SHULKERS);
  }

  @Override
  public String getName() {
    return "Lootr Block Tags";
  }
}
