package net.zestyblaze.lootr.network;

import net.minecraft.network.FriendlyByteBuf;

public class CloseCart {
    public int entityId;

    public CloseCart(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
    }

    public CloseCart(int entityId) {
        this.entityId = entityId;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
    }

    /*
    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> handle(this, context));
        context.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handle(CloseCart message, Supplier<NetworkEvent.Context> context) {
        ClientHandlers.handleCloseCart(message, context);
    }
     */
}