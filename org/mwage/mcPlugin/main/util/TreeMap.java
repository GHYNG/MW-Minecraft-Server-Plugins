package org.mwage.mcPlugin.main.util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.mwage.mcPlugin.main.Main_GeneralMethods;
/**
 * 树形节点数据结构。
 * 
 * @author GHYNG
 * @param <K>
 *            数据结构中键的类型。
 * @param <V>
 *            数据结构中值的类型。
 * @deprecated 未完工。
 */
@Deprecated
public class TreeMap<K, V> implements Main_GeneralMethods {
	/**
	 * 本级节点的值。
	 */
	protected V value;
	/**
	 * 下级节点的映射表。
	 */
	protected final Map<K, TreeMap<K, V>> subMaps = new HashMap<K, TreeMap<K, V>>();
	/**
	 * 获取该树本级以及全部直接和间接下级的非null节点值的数量，
	 * 称为该树的大小。
	 * 
	 * @return 该树的大小。
	 */
	public int size() {
		int size = 0;
		if(value != null) {
			size++;
		}
		Set<K> keys = subMaps.keySet();
		for(K key : keys) {
			TreeMap<K, V> subMap = subMaps.get(key);
			size += subMap.size();
		}
		return size;
	}
	/**
	 * 将所有不存在直接或间接下级非null节点的直接或间接下级树删除。
	 */
	public void clearEmpty() {
		Set<K> keys = subMaps.keySet();
		for(K key : keys) {
			TreeMap<K, V> subMap = subMaps.get(key);
			int subSize = subMap.size();
			if(subSize == 0) {
				subMaps.put(key, null);
			}
			else {
				subMap.clearEmpty();
			}
		}
	}
	public void put(V value) {
		this.value = value;
	}
	@SuppressWarnings("unchecked")
	public void put(V value, K... keyChain) {
		List<K> list = arrayToList(keyChain);
		putWithKeyChain(list, value);
	}
	public void putWithKeyChain(List<K> keyChain, V value) {
		int length = keyChain.size();
		if(length == 0) {
			put(value);
			return;
		}
		K key = keyChain.get(0);
		TreeMap<K, V> subMap = subMaps.get(key);
		if(subMap == null) {
			subMap = new TreeMap<K, V>();
			subMaps.put(key, subMap);
		}
		List<K> subKeyChain = new ArrayList<K>();
		for(int i = 1; i < length; i++) {
			subKeyChain.add(keyChain.get(i));
		}
		subMap.putWithKeyChain(subKeyChain, value);
	}
	public V get() {
		return value;
	}
	@SuppressWarnings("unchecked")
	public V get(K... keyChain) {
		int length = keyChain.length;
		if(length == 0) {
			return get();
		}
		return null;
	}
	public V getWithKeyChain(List<K> keyChain) {
		int length = keyChain.size();
		if(length == 0) {
			return get();
		}
		K key = keyChain.get(0);
		TreeMap<K, V> subMap = subMaps.get(key);
		if(subMap == null) {
			return null;
		}
		List<K> subKeyChain = new ArrayList<K>();
		for(int i = 1; i < length; i++) {
			subKeyChain.add(keyChain.get(i));
		}
		return subMap.getWithKeyChain(subKeyChain);
	}
	@SuppressWarnings("unchecked")
	public TreeMap<K, V> getSubMap(K... keyChain) {
		return null;
	}
	public TreeMap<K, V> getSubMapWithKeyChain(List<K> keyChain) {
		int length = keyChain.size();
		if(length == 0) {
			return this;
		}
		K key = keyChain.get(0);
		TreeMap<K, V> subMap = subMaps.get(key);
		if(subMap == null) {
			return subMap;
		}
		List<K> subKeyChain = new ArrayList<K>();
		for(int i = 1; i < length; i++) {
			subKeyChain.add(keyChain.get(i));
		}
		return subMap.getSubMapWithKeyChain(subKeyChain);
	}
}