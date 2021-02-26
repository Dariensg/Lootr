package noobanidus.mods.lootr.advancement;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.ServerPlayerEntity;

import javax.annotation.Nullable;

public class CartPredicate implements IGenericPredicate<Void> {
  @Override
  public boolean test(ServerPlayerEntity player, Void condition) {
    return true;
  }

  @Override
  public IGenericPredicate<Void> deserialize(@Nullable JsonObject element) {
    return new CartPredicate();
  }
}
