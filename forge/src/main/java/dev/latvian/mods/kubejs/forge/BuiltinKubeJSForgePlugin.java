package dev.latvian.mods.kubejs.forge;

import dev.latvian.mods.kubejs.BuiltinKubeJSPlugin;
import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.entity.forge.LivingEntityDropsEventJS;
import dev.latvian.mods.kubejs.item.forge.ItemDestroyedEventJS;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ClassFilter;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;

public class BuiltinKubeJSForgePlugin extends BuiltinKubeJSPlugin {
	@Override
	public void registerEvents() {
		super.registerEvents();
		ItemDestroyedEventJS.EVENT.register();
		LivingEntityDropsEventJS.EVENT.register();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void registerClientEvents() {
		super.registerClientEvents();
	}

	@Override
	public void registerClasses(ScriptType type, ClassFilter filter) {
		super.registerClasses(type, filter);

		filter.allow("net.minecraftforge"); // Forge
		filter.deny("net.minecraftforge.fml");
		filter.deny("net.minecraftforge.accesstransformer");
		filter.deny("net.minecraftforge.coremod");

		filter.deny("cpw.mods.modlauncher"); // FML
		filter.deny("cpw.mods.gross");
	}

	@Override
	public void registerBindings(BindingsEvent event) {
		super.registerBindings(event);

		if (event.type == ScriptType.STARTUP) {
			event.addFunction("onForgeEvent", args -> onPlatformEvent(event, args), null, KubeJSForgeEventHandlerWrapper.class);
		}

	}

	@Override
	public void registerTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
		super.registerTypeWrappers(type, typeWrappers);
	}

	public static Object onPlatformEvent(BindingsEvent event, Object[] args) {
		if (args.length < 2 || !(args[0] instanceof CharSequence)) {
			throw new RuntimeException("Invalid syntax! onPlatformEvent(string, function) required event class and handler");
		} else if (!KubeJS.startupScriptManager.firstLoad) {
			ConsoleJS.STARTUP.warn("onPlatformEvent() can't be reloaded! You will have to restart the game for changes to take effect.");
			return null;
		}

		try {
			Class type = Class.forName(args[0].toString());
			var handler = (KubeJSForgeEventHandlerWrapper) args[1];
			MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, type, handler);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

		return null;
	}
}
