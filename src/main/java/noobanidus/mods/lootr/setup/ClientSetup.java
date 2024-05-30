package noobanidus.mods.lootr.setup;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import noobanidus.mods.lootr.api.LootrAPI;
import noobanidus.mods.lootr.client.block.BarrelModel;
import noobanidus.mods.lootr.client.block.LootrChestBlockRenderer;
import noobanidus.mods.lootr.client.entity.LootrChestCartRenderer;
import noobanidus.mods.lootr.client.block.LootrShulkerBlockRenderer;
import noobanidus.mods.lootr.init.ModBlockEntities;
import noobanidus.mods.lootr.init.ModEntities;

@Mod.EventBusSubscriber(modid= LootrAPI.MODID, value= Dist.CLIENT, bus= Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {
  @SubscribeEvent
  public static void stitch(TextureStitchEvent.Pre event) {
    ResourceLocation atlas = event.getAtlas().location();
    if (atlas.equals(Sheets.CHEST_SHEET)) {
      event.addSprite(LootrChestBlockRenderer.MATERIAL.texture());
      event.addSprite(LootrChestBlockRenderer.MATERIAL2.texture());
      event.addSprite(LootrChestBlockRenderer.MATERIAL3.texture());
      event.addSprite(LootrChestBlockRenderer.MATERIAL4.texture());
      event.addSprite(LootrChestBlockRenderer.OLD_MATERIAL.texture());
      event.addSprite(LootrChestBlockRenderer.OLD_MATERIAL2.texture());
    } else if (atlas.equals(Sheets.SHULKER_SHEET)) {
      event.addSprite(LootrShulkerBlockRenderer.MATERIAL.texture());
      event.addSprite(LootrShulkerBlockRenderer.MATERIAL2.texture());
      event.addSprite(LootrShulkerBlockRenderer.MATERIAL3.texture());
      event.addSprite(LootrShulkerBlockRenderer.MATERIAL4.texture());
    }
  }

  @SubscribeEvent
  public static void modelRegister(ModelEvent.RegisterGeometryLoaders event) {
    event.register("barrel", BarrelModel.Loader.INSTANCE);
  }

  @SubscribeEvent
  public static void registerRenderers (EntityRenderersEvent.RegisterRenderers event) {
    event.registerBlockEntityRenderer(ModBlockEntities.LOOTR_CHEST.get(), LootrChestBlockRenderer::new);
    event.registerBlockEntityRenderer(ModBlockEntities.LOOTR_TRAPPED_CHEST.get(), LootrChestBlockRenderer::new);
    event.registerBlockEntityRenderer(ModBlockEntities.LOOTR_INVENTORY.get(), LootrChestBlockRenderer::new);
    event.registerBlockEntityRenderer(ModBlockEntities.LOOTR_SHULKER.get(), LootrShulkerBlockRenderer::new);
    event.registerEntityRenderer(ModEntities.LOOTR_MINECART_ENTITY.get(), (e) -> new LootrChestCartRenderer<>(e, ModelLayers.CHEST_MINECART));
  }
}
