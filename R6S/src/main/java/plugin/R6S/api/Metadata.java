package plugin.R6S.api;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import plugin.R6S.R6SPlugin;

public class Metadata {
	static Plugin r6s = R6SPlugin.getInstance();

	public static void setMetadata(Entity entity, String key, Object value) {
		if (!(entity != null) || StringUtils.isEmpty(key) || value.equals(null))
			return;
		MetadataValue metadata = new FixedMetadataValue(r6s, value);
		if (entity.hasMetadata(key)) {
			entity.removeMetadata(key, r6s);
		}
		entity.setMetadata(key, metadata);
	}

	// generally I use metadata in the way 1 key - 1 value
	public static Object getMetadata(Entity entity, String key) {
		if (!(entity != null) || StringUtils.isEmpty(key)) {
			return null;
		} else if (entity.hasMetadata(key)) {
			for (MetadataValue metadata : entity.getMetadata(key)) {
				return metadata.value();
			}
			return null;
		}
		return null;
	}

	public static void removeMetadata(Entity entity, String key) {
		if (entity != null) {
			if (StringUtils.isEmpty(key)) {
				return;
			} else if (entity.hasMetadata(key)) {
				entity.removeMetadata(key, r6s);
				return;
			}
		}
	}

	public static void clearMetadata(Entity entity, String[] keylist) {
		if (entity != null) {
			for (String key : keylist) {
				removeMetadata(entity, key);
			}
		}
	}
}
