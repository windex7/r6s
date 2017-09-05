package plugin.R6S.api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.math.BigInteger;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import net.minecraft.server.v1_9_R2.NBTCompressedStreamTools;
import net.minecraft.server.v1_9_R2.NBTTagCompound;
import net.minecraft.server.v1_9_R2.NBTTagList;

public class Base64Item {
	public static String itemToString(ItemStack item) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutput = new DataOutputStream(outputStream);
		NBTTagList nbtTagListItems = new NBTTagList();
		NBTTagCompound nbtTagCompoundItem = new NBTTagCompound();
		net.minecraft.server.v1_9_R2.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		nmsItem.save(nbtTagCompoundItem);
		nbtTagListItems.add(nbtTagCompoundItem);
		try {
			NBTCompressedStreamTools.a(nbtTagCompoundItem, (DataOutput) dataOutput);
			return new BigInteger(1, outputStream.toByteArray()).toString(32);
		} catch (Exception e) {
			throw new IllegalStateException("Unable to save item stack.", e);
		}

	}

	public static ItemStack itemFromString(String data) {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(new BigInteger(data, 32).toByteArray());
		NBTTagCompound nbtTagCompoundRoot;
		try {
			nbtTagCompoundRoot = NBTCompressedStreamTools.a(new DataInputStream(inputStream));
			net.minecraft.server.v1_9_R2.ItemStack nmsItem = net.minecraft.server.v1_9_R2.ItemStack
					.createStack(nbtTagCompoundRoot);
			ItemStack item = (ItemStack) CraftItemStack.asBukkitCopy(nmsItem);
			return item;
		} catch (Exception e) {
			throw new IllegalStateException("Unable to load item stack.", e);
		}

	}

	public static String itemToStringList(ItemStack[] items) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		BukkitObjectOutputStream dataOutput;
		try {
			dataOutput = new BukkitObjectOutputStream(outputStream);
			dataOutput.writeInt(items.length);
			int index = 0;
			for (ItemStack is : items) {
				if (is != null && is.getType() != Material.AIR) {
					dataOutput.writeObject(itemToString(is));
				} else {
					dataOutput.writeObject(null);
				}
				dataOutput.writeInt(index);
				index++;
			}
			dataOutput.close();
			return Base64Coder.encodeLines(outputStream.toByteArray());
		} catch (Exception e) {
			throw new IllegalStateException("Unable to save item stacks.", e);
		}
	}

	public static ItemStack[] itemFromStringList(String items) {
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(items));
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
			int size = dataInput.readInt();
			ItemStack[] list = new ItemStack[size];
			for (int i = 0; i < size; i++) {
				Object utf = dataInput.readObject();
				int slot = dataInput.readInt();
				if (utf != null) {
					list[slot] = itemFromString((String) utf);
				}
			}
			dataInput.close();
			return list;
		} catch (Exception e) {
			throw new IllegalStateException("Unable to load item stacks.", e);
		}
	}
}
