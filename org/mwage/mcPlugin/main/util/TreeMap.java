package org.mwage.mcPlugin.main.util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.mwage.mcPlugin.main.Main_GeneralMethods;
import org.mwage.mcPlugin.main.api.MWAPIInfo_Main;
import org.mwage.mcPlugin.main.standard.api.MWAPIInfo;
/**
 * 树形节点数据结构。
 * 
 * @author GHYNG
 * @param <K>
 *            数据结构中键的类型。
 * @param <V>
 *            数据结构中值的类型。
 */
public class TreeMap<K, V> implements Main_GeneralMethods {
	/**
	 * 本级节点的值。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	protected V value;
	/**
	 * 下级节点的映射表。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	protected final Map<K, TreeMap<K, V>> subTrees = new HashMap<K, TreeMap<K, V>>();
	/**
	 * 获取该树本级以及全部直接和间接下级的非null节点值的数量，
	 * 称为该树的大小。
	 * 
	 * @return 该树的大小。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public int size() {
		int size = 0;
		if(value != null) {
			size++;
		}
		Set<K> keys = subTrees.keySet();
		for(K key : keys) {
			TreeMap<K, V> subMap = subTrees.get(key);
			size += subMap.size();
		}
		return size;
	}
	/**
	 * 将所有不存在直接或间接下级非null节点的直接或间接下级树删除。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public void clearEmpty() {
		Set<K> keys = subTrees.keySet();
		for(K key : keys) {
			TreeMap<K, V> subMap = subTrees.get(key);
			int subSize = subMap.size();
			if(subSize == 0) {
				subTrees.put(key, null);
			}
			else {
				subMap.clearEmpty();
			}
		}
	}
	/**
	 * 给本级节点赋值。
	 * 
	 * @param value
	 *            新的值。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public void put(V value) {
		this.value = value;
	}
	/**
	 * 根据指定的下级节点地址，
	 * 给下级节点赋值。
	 * 如果指定的下级节点尚未有成型的树，
	 * 则在赋值前产生子树。
	 * 
	 * @param value
	 *            新的值。
	 * @param keyChain
	 *            下级节点的地址。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	@SuppressWarnings("unchecked")
	public void put(V value, K... keyChain) {
		List<K> list = arrayToList(keyChain);
		putWithKeyChain(list, value);
	}
	/**
	 * 根据指定的下级节点地址，
	 * 给下级节点赋值。
	 * 如果指定的下级节点尚未有诚心的树，
	 * 则在赋值前产生子树。
	 * 
	 * @param keyChain
	 *            下级节点的地址。
	 * @param value
	 *            新的值。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public void putWithKeyChain(List<K> keyChain, V value) {
		int length = keyChain.size();
		if(length == 0) {
			put(value);
			return;
		}
		K key = keyChain.get(0);
		TreeMap<K, V> subMap = subTrees.get(key);
		if(subMap == null) {
			subMap = new TreeMap<K, V>();
			subTrees.put(key, subMap);
		}
		List<K> subKeyChain = new ArrayList<K>();
		for(int i = 1; i < length; i++) {
			subKeyChain.add(keyChain.get(i));
		}
		subMap.putWithKeyChain(subKeyChain, value);
	}
	/**
	 * 根据指定的下级节点地址，
	 * 将另一棵树接在本树下，
	 * 作为子树。
	 * 
	 * @param subTree
	 *            子树。
	 * @param keyChain
	 *            下级节点地址。
	 * @throws DeadLoopTreeException
	 *             不允许目标树作为本树的子树。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	@SuppressWarnings("unchecked")
	public void putSubTree(TreeMap<K, V> subTree, K... keyChain) throws DeadLoopTreeException {
		List<K> list = arrayToList(keyChain);
		putSubTreeWithKeyChain(subTree, list);
	}
	/**
	 * 根据指定的下级节点地址，
	 * 将另一棵树接在本树下，
	 * 作为子树。
	 * 
	 * @param subTree
	 *            子树。
	 * @param keyChain
	 *            下级节点地址。
	 * @throws DeadLoopTreeException
	 *             不允许目标树作为本树的子树。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public void putSubTreeWithKeyChain(TreeMap<K, V> subTree, List<K> keyChain) throws DeadLoopTreeException {
		if(keyChain == null) {
			throw new NullPointerException("Paramater keyChain is null.");
		}
		int length = keyChain.size();
		if(length == 0) {
			throw new DeadLoopTreeException("Cannot put sub tree in same level. At least one sub level needed.");
		}
		if(subTree.containsSubTree(this)) {
			throw new DeadLoopTreeException("Cannot put parent tree in child tree.");
		}
		if(this.containsSubTree(subTree)) {
			throw new DeadLoopTreeException("Cannot put same child in parent tree more then once.");
		}
		if(length == 1) {
			K key = keyChain.get(0);
			subTrees.put(key, subTree);
			return;
		}
		List<K> subKeyChain = new ArrayList<K>();
		for(int i = 1; i < length; i++) {
			subKeyChain.add(keyChain.get(i));
		}
		K key0 = keyChain.get(0);
		TreeMap<K, V> subTree0 = subTrees.get(key0);
		if(subTree0 == null) {
			subTree0 = new TreeMap<K, V>();
			subTrees.put(key0, subTree0);
		}
		subTree0.putSubTreeWithKeyChain(subTree, subKeyChain);
	}
	/**
	 * 获取本级节点的值。
	 * 
	 * @return 本级节点的值。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public V get() {
		return value;
	}
	/**
	 * 根据指定的下级节点地址，
	 * 获取下级节点的值。
	 * 
	 * @param keyChain
	 *            下级节点地址。
	 * @return 值。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	@SuppressWarnings("unchecked")
	/**
	 * 根据指定的下级节点地址，
	 * 获取下级节点的值。
	 * 
	 * @param keyChain
	 *            下级节点地址。
	 * @return 值。
	 */
	public V get(K... keyChain) {
		List<K> list = arrayToList(keyChain);
		return getWithKeyChain(list);
	}
	/**
	 * 根据指定的下级节点地址，
	 * 获取下级节点的值。
	 * 
	 * @param keyChain
	 *            下级节点地址。
	 * @return 值。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public V getWithKeyChain(List<K> keyChain) {
		int length = keyChain.size();
		if(length == 0) {
			return get();
		}
		K key = keyChain.get(0);
		TreeMap<K, V> subMap = subTrees.get(key);
		if(subMap == null) {
			return null;
		}
		List<K> subKeyChain = new ArrayList<K>();
		for(int i = 1; i < length; i++) {
			subKeyChain.add(keyChain.get(i));
		}
		return subMap.getWithKeyChain(subKeyChain);
	}
	/**
	 * 根据指定的下级节点地址，
	 * 获取子树。
	 * 
	 * @param keyChain
	 *            下级节点地址。
	 * @return 子树。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	@SuppressWarnings("unchecked")
	public TreeMap<K, V> getSubTree(K... keyChain) {
		List<K> list = arrayToList(keyChain);
		return getSubTreeWithKeyChain(list);
	}
	/**
	 * 根据指定的下级节点地址，
	 * 获取子树。
	 * 
	 * @param keyChain
	 *            下级节点地址。
	 * @return 子树。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public TreeMap<K, V> getSubTreeWithKeyChain(List<K> keyChain) {
		int length = keyChain.size();
		if(length == 0) {
			return this;
		}
		K key = keyChain.get(0);
		TreeMap<K, V> subMap = subTrees.get(key);
		if(subMap == null) {
			return subMap;
		}
		List<K> subKeyChain = new ArrayList<K>();
		for(int i = 1; i < length; i++) {
			subKeyChain.add(keyChain.get(i));
		}
		return subMap.getSubTreeWithKeyChain(subKeyChain);
	}
	/**
	 * 确认该树是否含有某值。
	 * 
	 * @param value
	 *            指定的值。
	 * @return 真，如果该树的某一个节点含有该值；假，否则。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public boolean contains(V value) {
		if(value == null) {
			return true;
		}
		if(value.equals(this.value)) {
			return true;
		}
		Set<K> keys = subTrees.keySet();
		for(K key : keys) {
			TreeMap<K, V> subMap = subTrees.get(key);
			if(subMap.contains(value)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 确认该树是否含有子树。
	 * 
	 * @param another
	 *            指定的子树。
	 * @return 真，如果含有；假，否则。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	public boolean containsSubTree(TreeMap<K, V> another) {
		if(another == null) {
			return true;
		}
		if(this == another) {
			return true;
		}
		Set<K> keys = subTrees.keySet();
		for(K key : keys) {
			TreeMap<K, V> subMap = subTrees.get(key);
			if(subMap.containsSubTree(another)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 判断该树是否和另一颗树相等。
	 * <p>
	 * 如果是同一个实例，相等。
	 * 如果两棵树拥有相同的节点结构和值，相等。
	 */
	@MWAPIInfo_Main(api = @MWAPIInfo(startsAt = 1))
	@Override
	public boolean equals(Object anotherObject) {
		if(this == anotherObject) {
			return true;
		}
		if(anotherObject instanceof TreeMap<?, ?> another) {
			Object anotherValue = another.value;
			if(value == null) {
				if(anotherValue != null) {
					return false;
				}
			}
			else {
				if(anotherValue == null) {
					return false;
				}
				if(!value.equals(anotherValue)) {
					return false;
				}
			}
			return subTrees.equals(another.subTrees);
		}
		return false;
	}
}