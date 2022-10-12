package dev.latvian.mods.kubejs.forge;

import dev.latvian.mods.kubejs.bindings.event.EntityEvents;
import dev.latvian.mods.kubejs.bindings.event.ItemEvents;
import dev.latvian.mods.kubejs.entity.forge.LivingEntityDropsEventJS;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.item.forge.ItemDestroyedEventJS;

public interface ForgeKubeJSEvents {
	EventHandler ITEM_DESTROYED = ItemEvents.GROUP.server("destroyed", () -> ItemDestroyedEventJS.class).supportsNamespacedExtraId();

	EventHandler ENTITY_DROPS = EntityEvents.GROUP.server("drops", () -> LivingEntityDropsEventJS.class).supportsNamespacedExtraId().cancelable();

	static void register() {
	}
}
