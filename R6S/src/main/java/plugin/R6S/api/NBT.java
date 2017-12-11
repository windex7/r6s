package plugin.R6S.api;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_9_R2.EntityArrow;
import net.minecraft.server.v1_9_R2.EntityItem;
import net.minecraft.server.v1_9_R2.NBTTagCompound;

public class NBT {
	public static Object readItemTag(ItemStack item, String key, String datatype) {
		if (!(item != null) || item.getType() == Material.AIR) return null;
		net.minecraft.server.v1_9_R2.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		NBTTagCompound nbttag = nmsItem.getTag();
		if (nbttag != null) {
			if (nbttag.hasKey(key)) {
				switch (datatype.toLowerCase()) {
				case "string":
					String value = nbttag.getString(key);
					if (value == null || value.isEmpty()) return null;
					else return value;
				case "int":
					return nbttag.getInt(key);
				case "float":
					return nbttag.getFloat(key);
				case "long":
					return nbttag.getLong(key);
				case "double":
					return nbttag.getDouble(key);
				case "boolean":
					return nbttag.getBoolean(key);
				default:
					return null;
				}
			}
			return null;
		}
		return null;
	}

	public static ItemStack writeItemTag(ItemStack item, String key, Object value, String datatype) {
		if (!(item != null) || item.getType() == Material.AIR) return item;
		net.minecraft.server.v1_9_R2.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		NBTTagCompound nbttag = nmsItem.getTag();
		String valuestr = value.toString();
		if (value != null && !(valuestr.equals("null"))) {
			if (nbttag != null) {
				nmsItem.setTag(setNBT(nbttag, key, value, datatype));
			}
			else {
				NBTTagCompound newnbttag = new NBTTagCompound();
				nmsItem.setTag(setNBT(newnbttag, key, value, datatype));
			}
			ItemStack nbtitem = CraftItemStack.asBukkitCopy(nmsItem);
			return nbtitem;
		}
		else {
			return removeItemTag(item, key);
		}
	}

	private static NBTTagCompound setNBT(NBTTagCompound nbttag, String key, Object value, String datatype) {
		String valuestr = value.toString();
		//switch (value.getClass().getSimpleName().toLowerCase()) {
		switch (datatype) {
		case "string":
			nbttag.setString(key, valuestr);
			break;
		case "int":
			nbttag.setInt(key, new Integer(valuestr));
			break;
		case "float":
			nbttag.setFloat(key, new Float(valuestr));
			break;
		case "long":
			nbttag.setLong(key, new Long(valuestr));
		case "double":
			nbttag.setDouble(key, new Double(valuestr));
			break;
		case "boolean":
			nbttag.setBoolean(key, new Boolean(valuestr));
			break;
		default:
		}
		return nbttag;
	}

	public static ItemStack removeItemTag(ItemStack item, String key) {
		if (!(item != null) || item.getType() == Material.AIR) return item;
		net.minecraft.server.v1_9_R2.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		NBTTagCompound nbttag = nmsItem.getTag();
		if (nbttag != null) {
			nbttag.remove(key);
			nmsItem.setTag(nbttag);
		}
		ItemStack nbtitem = CraftItemStack.asBukkitCopy(nmsItem);
		return nbtitem;
	}

	public static void writeEntityTag(Entity entity, String entitytype, String key, Object value, String datatype) {
		net.minecraft.server.v1_9_R2.Entity nmsEntity = ((CraftEntity) entity).getHandle();
		NBTTagCompound nbttag = new NBTTagCompound();
		nmsEntity.c(nbttag);
		NBTTagCompound setnbttag = setNBT(nbttag, key, value, datatype);
		// if (!(entitytype != null && !(entitytype.equals(null)))) return;
		switch (entitytype.toLowerCase()) {
		case "arrow":
			EntityArrow nbtentityarrow = (EntityArrow) nmsEntity;
			nbtentityarrow.a(setnbttag);
			return;
		case "item":
			EntityItem nbtentityitem = (EntityItem) nmsEntity;
			nbtentityitem.a(setnbttag);
			return;
		default:
			return;
		}
	}
}
