package dev.latvian.kubejs.item;

import dev.latvian.kubejs.util.MapJS;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * @author LatvianModder
 */
public class BoundItemStackJS extends ItemStackJS {
	private final ItemStack stack;

	public BoundItemStackJS(ItemStack is) {
		stack = is;
	}

	@Override
	public Item getItem() {
		return stack.getItem();
	}

	@Override
	public ItemStack getItemStack() {
		return stack;
	}

	@Override
	public ItemStackJS copy() {
		return new BoundItemStackJS(stack.copy()).withChance(getChance());
	}

	@Override
	public void setCount(int c) {
		stack.setCount(c);
	}

	@Override
	public int getCount() {
		return stack.getCount();
	}

	@Override
	public boolean isEmpty() {
		return stack.isEmpty();
	}

	@Override
	public ItemStackJS withCount(int c) {
		if (c <= 0) {
			return EmptyItemStackJS.INSTANCE;
		}

		ItemStack is = stack.copy();
		is.setCount(c);
		return new BoundItemStackJS(is).withChance(getChance());
	}

	@Override
	public MapJS getNbt() {
		MapJS nbt = MapJS.of(stack.getTag());

		if (nbt == null) {
			nbt = new MapJS();
		}

		nbt.changeListener = this;
		return nbt;
	}

	@Override
	public boolean hasNBT() {
		return stack.hasTag();
	}

	@Override
	public String getNbtString() {
		return String.valueOf(stack.getTag());
	}

	@Override
	public ItemStackJS withNBT(Object o) {
		CompoundTag tag = MapJS.nbt(o);

		if (tag != null) {
			ItemStack is = stack.copy();

			return new BoundItemStackJS(is).withChance(getChance());
		}

		return this;
	}

	@Override
	public void setName(@Nullable Component displayName) {
		stack.setHoverName(displayName);
	}

	@Override
	public boolean test(ItemStackJS stack) {
		return stack instanceof BoundItemStackJS ? testVanilla(((BoundItemStackJS) stack).stack) : super.test(stack);
	}

	@Override
	public boolean testVanilla(ItemStack stack2) {
		if (stack.getItem() == stack2.getItem()) {
			CompoundTag nbt = stack.getTag();
			CompoundTag nbt2 = stack2.getTag();
			return Objects.equals(nbt, nbt2);
		}

		return false;
	}

	@Override
	public boolean isNBTEqual(ItemStackJS stack2) {
		if (hasNBT() == stack2.hasNBT()) {
			CompoundTag nbt = stack.getTag();
			CompoundTag nbt2 = MapJS.nbt(stack2.getNbt());
			return Objects.equals(nbt, nbt2);
		}

		return false;
	}

	@Override
	public boolean isNBTEqual(ItemStack stack2) {
		if (hasNBT() == stack2.hasTag()) {
			CompoundTag nbt = stack.getTag();
			CompoundTag nbt2 = stack2.getTag();
			return Objects.equals(nbt, nbt2);
		}

		return false;
	}

	@Override
	public void onChanged(@Nullable MapJS o) {
		stack.setTag(MapJS.nbt(o));
	}
}