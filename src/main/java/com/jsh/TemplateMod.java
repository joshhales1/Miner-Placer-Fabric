package com.jsh;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemplateMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("template-mod");
	public static final Block MINER_BLOCK = new MinerBlock(FabricBlockSettings.create().strength(4.0f));
	public static final Block PLACER_BLOCK = new PlacerBlock(FabricBlockSettings.create().strength(4.0f));

	@Override
	public void onInitialize() {
		Registry.register(Registries.BLOCK, new Identifier("template-mod", "miner"), MINER_BLOCK);
		Registry.register(Registries.ITEM, new Identifier("template-mod", "miner"), new BlockItem(MINER_BLOCK, new FabricItemSettings()));

		Registry.register(Registries.BLOCK, new Identifier("template-mod", "placer"), PLACER_BLOCK);
		Registry.register(Registries.ITEM, new Identifier("template-mod", "placer"), new BlockItem(PLACER_BLOCK, new FabricItemSettings()));

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(content -> {
			content.add(MINER_BLOCK);
			content.add(PLACER_BLOCK);
		});

		LOGGER.info("Hello Fabric world!");
	}


}