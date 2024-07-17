package store.novabook.store.common.util;

import java.util.HashMap;
import java.util.Map;

public class TypeUtil {
	private TypeUtil() {
	}
	public static <K, V> Map<K, V> castMap(Object obj, Class<K> keyClass, Class<V> valueClass) {
		if (obj instanceof Map<?, ?> rawMap) {
			Map<K, V> resultMap = new HashMap<>();
			for (Map.Entry<?, ?> entry : rawMap.entrySet()) {
				Object rawKey = entry.getKey();
				Object rawValue = entry.getValue();
				if (keyClass.isInstance(rawKey) && valueClass.isInstance(rawValue)) {
					resultMap.put(keyClass.cast(rawKey), valueClass.cast(rawValue));
				} else {
					throw new ClassCastException("Cannot cast map entries to the specified types.");
				}
			}
			return resultMap;
		} else {
			throw new ClassCastException("Object is not a map.");
		}
	}
}
