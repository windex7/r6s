package plugin.R6S.api;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import plugin.R6S.R6SPlugin;

public class Metadata {
	public static void setMetaData(Entity entity, String key, Object value) {
		if (!(entity != null) || StringUtils.isEmpty(key) || value.equals(null)) return;
		Plugin r6s = R6SPlugin.getInstance();
		MetadataValue metadata = new FixedMetadataValue(r6s, value);
		if (entity.hasMetadata(key)) {
			entity.removeMetadata(key, r6s);
		}
		entity.setMetadata(key, metadata);
	}

	// generally I use metadata in the way 1 key - 1 value
	public static Object getMetaData(Entity entity, String key) {
		if (!(entity != null) || StringUtils.isEmpty(key) ) return null;
		for (MetadataValue metadata : entity.getMetadata(key)) {
			return metadata.value();
		}
		return null;
	}
}
