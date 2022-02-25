package net.zestyblaze.lootr.entity;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecartContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.zestyblaze.lootr.api.LootrAPI;
import net.zestyblaze.lootr.api.entity.ILootCart;
import net.zestyblaze.lootr.config.LootrModConfig;
import net.zestyblaze.lootr.network.OpenCart;
import net.zestyblaze.lootr.registry.LootrBlockInit;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class LootrChestMinecartEntity extends AbstractMinecartContainer implements ILootCart {
    private Set<UUID> openers = new HashSet<>();
    private boolean opened = false;

    public LootrChestMinecartEntity(EntityType<LootrChestMinecartEntity> type, Level world) {
        super(type, world);
    }

    /*
    public LootrChestMinecartEntity(Level worldIn, double x, double y, double z) {
        super(ModEntities.LOOTR_MINECART_ENTITY, x, y, z, worldIn);
    }
     */

    @Override
    public Set<UUID> getOpeners() {
        return openers;
    }

    public void addOpener(Player player) {
        openers.add(player.getUUID());
        setChanged();
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened() {
        this.opened = true;
    }

    public void setClosed () {
        this.opened = false;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if (this.isInvulnerable() && source != DamageSource.OUT_OF_WORLD && !source.isCreativePlayer()) {
            return true;
        }

        if (source.getEntity() instanceof Player) {
            if (source.getEntity().isShiftKeyDown()) {
                return false;
            } else {
                source.getEntity().sendMessage(new TranslatableComponent("lootr.message.cart_should_sneak").setStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.AQUA))), Util.NIL_UUID);
                source.getEntity().sendMessage(new TranslatableComponent("lootr.message.should_sneak2", new TranslatableComponent("lootr.message.cart_should_sneak3").setStyle(Style.EMPTY.withBold(true))).setStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.AQUA))), Util.NIL_UUID);
            }
        } else {
            return true;
        }

        return true;
    }

    @Override
    public void destroy(DamageSource source) {
        this.remove(Entity.RemovalReason.KILLED);
        if (this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            ItemStack itemstack = new ItemStack(Items.MINECART);
            ItemStack itemstack2 = new ItemStack(Items.CHEST);
            if (this.hasCustomName()) {
                itemstack.setHoverName(this.getCustomName());
                itemstack2.setHoverName(this.getCustomName());
            }

            this.spawnAtLocation(itemstack);
            this.spawnAtLocation(itemstack2);
        }
    }

    @Override
    public int getContainerSize() {
        return 27;
    }

    @Override
    public Type getMinecartType() {
        return Type.CHEST;
    }

    private static BlockState cartNormal = LootrBlockInit.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.NORTH);

    @Override
    public BlockState getDefaultDisplayBlockState() {
        return cartNormal;
    }

    @Override
    public int getDefaultDisplayOffset() {
        return 8;
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory playerInventory) {
        return ChestMenu.threeRows(id, playerInventory, this);
    }

    @Override
    public void remove(RemovalReason reason) {
        this.setRemoved(reason);
        if (reason == Entity.RemovalReason.KILLED) {
            this.gameEvent(GameEvent.ENTITY_KILLED);
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        if (this.lootTable != null) {
            compound.putString("LootTable", this.lootTable.toString());
        }
        compound.putLong("LootTableSeed", this.lootTableSeed);
        ListTag list = new ListTag();
        for (UUID opener : this.openers) {
            list.add(NbtUtils.createUUID(opener));
        }
        compound.put("LootrOpeners", list);
        super.addAdditionalSaveData(compound);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.lootTable = new ResourceLocation(compound.getString("LootTable"));
        this.lootTableSeed = compound.getLong("LootTableSeed");
        if (compound.contains("LootrOpeners", Tag.TAG_LIST)) {
            ListTag openers = compound.getList("LootrOpeners", Tag.TAG_INT_ARRAY);
            this.openers.clear();
            for (Tag item : openers) {
                this.openers.add(NbtUtils.loadUUID(item));
            }
        }
        super.readAdditionalSaveData(compound);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        InteractionResult ret = InteractionResult.PASS;
        if (ret.consumesAction()) return ret;
        if (player.isShiftKeyDown()) {
            ChestUtil.handleLootCartSneak(player.level, this, player);
            if (!player.level.isClientSide) {
                return InteractionResult.CONSUME;
            } else {
                return InteractionResult.SUCCESS;
            }
        } else {
            ChestUtil.handleLootCart(player.level, this, player);
            if (!player.level.isClientSide) {
                PiglinAi.angerNearbyPiglins(player, true);
                return InteractionResult.CONSUME;
            } else {
                return InteractionResult.SUCCESS;
            }
        }
    }

    public void addLoot(@Nullable Player player, Container inventory, @Nullable ResourceLocation overrideTable, long seed) {
        if (this.lootTable != null && this.level.getServer() != null) {
            LootTable loottable = this.level.getServer().getLootTables().get(overrideTable != null ? overrideTable : this.lootTable);
            if (loottable == LootTable.EMPTY) {
                LootrAPI.LOG.error("Unable to fill loot in " + level.dimension() + " at " + position() + " as the loot table '" + (overrideTable != null ? overrideTable : this.lootTable) + "' couldn't be resolved! Please search the loot table in `latest.log` to see if there are errors in loading.");
                if (LootrModConfig.get().debug.report_unresolved_tables && player != null) {
                    player.sendMessage(new TranslatableComponent("lootr.message.invalid_table", (overrideTable != null ? overrideTable : this.lootTable).toString()).setStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.DARK_RED)).withBold(true)), Util.NIL_UUID);
                }
            }
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.GENERATE_LOOT.trigger((ServerPlayer) player, overrideTable != null ? overrideTable : this.lootTable);
            }
            LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerLevel) this.level)).withParameter(LootContextParams.ORIGIN, this.position()).withOptionalRandomSeed(ConfigManager.RANDOMISE_SEED.get() ? ThreadLocalRandom.current().nextLong() : seed == Long.MIN_VALUE ? this.lootTableSeed : seed);
            lootcontext$builder.withParameter(LootContextParams.KILLER_ENTITY, this);
            if (player != null) {
                lootcontext$builder.withLuck(player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player);
            }

            loottable.fill(inventory, lootcontext$builder.create(LootContextParamSets.CHEST));
        }
    }

    @Override
    public void startOpen(Player player) {
        if (!player.isSpectator()) {
            OpenCart cart = new OpenCart(this.getId());
            //PacketHandler.sendToInternal(cart, (ServerPlayer) player);
        }
    }

    @Override
    public void stopOpen(Player player) {
        if (!player.isSpectator()) {
            addOpener(player);
        }
    }
}