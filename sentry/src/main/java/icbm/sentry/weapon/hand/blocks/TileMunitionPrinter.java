package icbm.sentry.weapon.hand.blocks;

import icbm.core.prefab.TileICBM;
import icbm.sentry.ICBMSentry;
import icbm.sentry.weapon.hand.items.IItemAmmunition;
import icbm.sentry.weapon.hand.items.ItemWeapon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import universalelectricity.api.energy.EnergyStorageHandler;
import calclavia.lib.prefab.tile.IRotatable;

public class TileMunitionPrinter extends TileICBM implements IRotatable, IInventory {

	ItemStack[] inventory;

	private Item chosenMunition = ICBMSentry.itemMagazine;

	public TileMunitionPrinter() {
		setEnergyHandler(new EnergyStorageHandler(50000, 15000));
		inventory = new ItemStack[2];
		inventory[0] = new ItemStack(chosenMunition);
	}

	public boolean canPrint() {
		if (inventory[0] != null) { return false; }
		if (inventory[1] != null) { return false; }
		if (getEnergy(null) < 500) { return false; }
		return true;
	}

	public void updateEntity() {
		if (!worldObj.isRemote) print();
	}

	public void print() {
		if (canPrint()) {
			ItemStack newStack = new ItemStack(chosenMunition);

			if (chosenMunition instanceof IItemAmmunition) {
				inventory[0] = newStack.copy();
			} else if (chosenMunition instanceof ItemWeapon) {
				inventory[1] = newStack.copy();
			}
		}
		setEnergy(null, getEnergy(null) - 500);
	}

	@Override
	public int getSizeInventory() {
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return this.inventory[i];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (slot >= 0 && slot < inventory.length) {
			ItemStack itemstack = inventory[slot];
			inventory[slot] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		if (this.inventory[i] != null) {
			ItemStack itemstack = this.inventory[i];
			this.inventory[i] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		this.inventory[i] = itemstack;

		if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
			itemstack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public String getInvName() {
		return "Munition Printer";
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	}

	@Override
	public void openChest() {

	}

	@Override
	public void closeChest() {

	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return false;
	}

	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items");
		this.inventory = new ItemStack[this.getSizeInventory()];

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(i);
			byte b0 = nbttagcompound1.getByte("Slot");

			if (b0 >= 0 && b0 < this.inventory.length) {
				this.inventory[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	/**
	 * Writes a tile entity to NBT.
	 */
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < this.inventory.length; ++i) {
			if (this.inventory[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				this.inventory[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		par1NBTTagCompound.setTag("Items", nbttaglist);
	}

}
