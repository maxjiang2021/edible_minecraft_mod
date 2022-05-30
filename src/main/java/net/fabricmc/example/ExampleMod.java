package net.fabricmc.example;

import net.fabricmc.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.item.*;
import net.minecraft.util.registry.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import org.spongepowered.asm.mixin.injection.*;

class MinecraftFoodItem extends Item {
 
    public MinecraftFoodItem(Settings settings) {
        super(settings);
    }
	
	@Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		super.finishUsing(stack,world,user);
        throw new RuntimeException("YOU ATE MINECRAFT!!");
        // return TypedActionResult.success(playerEntity.getStackInHand(hand));
    }
}

public class ExampleMod implements ModInitializer {
 
    // an instance of our new item
    public static final MinecraftFoodItem CUSTOM_ITEM = new MinecraftFoodItem(new Item.Settings().maxCount(127).fireproof().group(ItemGroup.MISC).food(new FoodComponent.Builder().hunger(1).saturationModifier(0.3F).build()));
	
    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, new Identifier("ediblemc", "minecraft_"), CUSTOM_ITEM);
    }
}