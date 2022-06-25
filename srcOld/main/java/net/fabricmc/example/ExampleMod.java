package net.fabricmc.example;

import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.item.*;
import net.minecraft.util.registry.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import java.util.Random;

import com.google.common.eventbus.Subscribe;

class MinecraftFoodItem extends Item {
 
    public MinecraftFoodItem(Settings settings) {
        super(settings);
        this.rgn = new Random();
    }
	Random rgn;
	@Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		super.finishUsing(stack,world,user);
        if (!world.isClient()){
            for (int i = 0; i < 10; i++) {
                TntEntity customEntity = ((EntityType<TntEntity>) EntityType.get("minecraft:tnt").get())
                        .create(world);
                customEntity.updatePosition(user.getX() + (rgn.nextInt(500) - 250) / 100,
                        user.getY() + (rgn.nextInt(500) - 250) / 100, user.getZ() + (rgn.nextInt(500) - 350) / 100);
                customEntity.setFuse(5);
                world.spawnEntity(customEntity);
            }
        }
        if (world.isClient()) {
            ExampleMod.ticksUntilCrash = 10;
        }
        return stack;
    }
}

public class ExampleMod implements ModInitializer {
    public static int ticksUntilCrash;
    public static int eatTimes;
    // an instance of our new item
    public static final MinecraftFoodItem CUSTOM_ITEM = new MinecraftFoodItem(new Item.Settings().maxCount(64).fireproof().group(ItemGroup.MISC).food(new FoodComponent.Builder().hunger(1).saturationModifier(0.3F).alwaysEdible().build()));
	
    @Override
    public void onInitialize() {
        ticksUntilCrash = -1;
        ClientTickEvents.END_CLIENT_TICK.register(listener -> {
            if (listener.world==null){return;}
            if (listener.world.isClient()) {
                if (ticksUntilCrash>0) {
                    ticksUntilCrash--;
                } else if (ticksUntilCrash==0) {
                    ticksUntilCrash = -1;
                    eatTimes+=1;
                    if (eatTimes>3) {
                        throw new RuntimeException("YOU ATE TOO MUCH MINECRAFT!!!");
                    }
                }
            }
        });
        Registry.register(Registry.ITEM, new Identifier("ediblemc", "minecraft_"), CUSTOM_ITEM);
    }
    
}