package store.novabook.store.common.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class TypeUtilTest {

	@Test
	void castMap_validMap_returnsCastedMap() {
		// given
		Map<Object, Object> rawMap = new HashMap<>();
		rawMap.put("key1", 1);
		rawMap.put("key2", 2);

		// when
		Map<String, Integer> resultMap = TypeUtil.castMap(rawMap, String.class, Integer.class);

		// then
		assertNotNull(resultMap);
		assertEquals(2, resultMap.size());
		assertEquals(1, resultMap.get("key1"));
		assertEquals(2, resultMap.get("key2"));
	}

	@Test
	void castMap_invalidKeyType_throwsClassCastException() {
		// given
		Map<Object, Object> rawMap = new HashMap<>();
		rawMap.put(1, "value1"); // Invalid key type

		// when, then
		assertThrows(ClassCastException.class, () -> {
			TypeUtil.castMap(rawMap, String.class, String.class);
		});
	}

	@Test
	void castMap_invalidValueType_throwsClassCastException() {
		// given
		Map<Object, Object> rawMap = new HashMap<>();
		rawMap.put("key1", 1); // Invalid value type

		// when, then
		assertThrows(ClassCastException.class, () -> {
			TypeUtil.castMap(rawMap, String.class, String.class);
		});
	}

	@Test
	void castMap_nonMapObject_throwsClassCastException() {
		// given
		Object nonMapObject = new Object();

		// when, then
		assertThrows(ClassCastException.class, () -> {
			TypeUtil.castMap(nonMapObject, String.class, String.class);
		});
	}
}
