package noobanidus.mods.lootr.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import noobanidus.mods.lootr.Lootr;
import noobanidus.mods.lootr.blocks.entities.*;

@Mod.EventBusSubscriber(modid=Lootr.MODID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class ModBlockEntities {
  public static BlockEntityType<LootrChestBlockEntity> SPECIAL_LOOT_CHEST = BlockEntityType.Builder.of(LootrChestBlockEntity::new, ModBlocks.CHEST).build(null);
  public static BlockEntityType<LootrTrappedChestBlockEntity> SPECIAL_TRAPPED_LOOT_CHEST = BlockEntityType.Builder.of(LootrTrappedChestBlockEntity::new, ModBlocks.TRAPPED_CHEST).build(null);
  public static BlockEntityType<LootrBarrelBlockEntity> SPECIAL_LOOT_BARREL = BlockEntityType.Builder.of(LootrBarrelBlockEntity::new, ModBlocks.BARREL).build(null);
  public static BlockEntityType<LootrInventoryBlockEntity> SPECIAL_LOOT_INVENTORY = BlockEntityType.Builder.of(LootrInventoryBlockEntity::new, ModBlocks.INVENTORY).build(null);
  public static BlockEntityType<LootrShulkerBlockEntity> SPECIAL_LOOT_SHULKER = BlockEntityType.Builder.of(LootrShulkerBlockEntity::new, ModBlocks.SHULKER).build(null);

  @SubscribeEvent
  public static void registerTileEntityType(RegistryEvent.Register<BlockEntityType<?>> event) {
    SPECIAL_LOOT_CHEST.setRegistryName(Lootr.MODID, "special_loot_chest");
    event.getRegistry().register(SPECIAL_LOOT_CHEST);
    SPECIAL_TRAPPED_LOOT_CHEST.setRegistryName(Lootr.MODID, "special_trapped_loot_chest");
    event.getRegistry().register(SPECIAL_TRAPPED_LOOT_CHEST);
    SPECIAL_LOOT_BARREL.setRegistryName(Lootr.MODID, "special_loot_barrel");
    event.getRegistry().register(SPECIAL_LOOT_BARREL);
    SPECIAL_LOOT_INVENTORY.setRegistryName(Lootr.MODID, "special_loot_inventory");
    event.getRegistry().register(SPECIAL_LOOT_INVENTORY);
    SPECIAL_LOOT_SHULKER.setRegistryName(Lootr.MODID, "special_loot_shulker");
    event.getRegistry().register(SPECIAL_LOOT_SHULKER);
  }
}
