package org.mwage.mcPlugin.vote.normal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
public class VoteNormal {
	public class Selection {
		public final String name;
		protected Map<UUID, Integer> voteCount = new HashMap<UUID, Integer>();
		public Selection(String name) {
			this.name = name;
		}
		public boolean castVote(Player player, int newVotes) {
			UUID uuid = player.getUniqueId();
			{
				// init hash table
				Integer i = voteCount.get(uuid);
				if(i == null) {
					voteCount.put(uuid, 0);
				}
			}
			int playerTotalVoted = getTotalCastsFromPlayer(player);
			int playerSelectionVoted = voteCount.get(uuid);
			if(playerTotalVoted + newVotes > vpp || playerSelectionVoted > vps) {
				return false;
			}
			if(playerSelectionVoted + newVotes < 0) {
				setCastsFromPlayer(player, 0);
				return true;
			}
			setCastsFromPlayer(player, playerSelectionVoted + newVotes);
			return true;
		}
		public void setCastsFromPlayer(Player player, int amount) {
			voteCount.put(player.getUniqueId(), amount);
		}
		public int getCastsFromPlayer(Player player) {
			Integer count = voteCount.get(player.getUniqueId());
			if(count == null) {
				count = 0;
				voteCount.put(player.getUniqueId(), count);
			}
			return count;
		}
		public int getTotalCasts() {
			int total = 0;
			for(UUID uuid : voteCount.keySet()) {
				Integer i = voteCount.get(uuid);
				if(i == null) {
					continue;
				}
				total += i;
			}
			return total;
		}
	}
	public final String name;
	/**
	 * the max amount of votes one person can cast in total
	 */
	protected int vpp = 0;
	/**
	 * the max amount of votes one person can cast to a selection.
	 * -1 if no limit
	 */
	protected int vps = 0;
	/**
	 * if the vote is secret
	 */
	protected boolean anon = true;
	protected Map<String, Selection> selections = new HashMap<String, Selection>();
	public VoteNormal(String name) {
		this.name = name;
	}
	public boolean createSelection(String name) {
		if(selections.containsKey(name)) {
			return false;
		}
		Selection selection = new Selection(name);
		selections.put(name, selection);
		return true;
	}
	public boolean removeSelection(String name) {
		if(selections.containsKey(name)) {
			return false;
		}
		selections.remove(name);
		return true;
	}
	public int getTotalCastsFromPlayer(Player player) {
		int votes = 0;
		UUID uuid = player.getUniqueId();
		for(String selectionName : selections.keySet()) {
			Selection selection = selections.get(selectionName);
			if(selection == null) {
				continue;
			}
			Integer v = selection.voteCount.get(uuid);
			if(v == null) {
				continue;
			}
			votes += v;
		}
		return votes;
	}
	public int getTotalCasts() {
		int total = 0;
		for(Player player : getPlayers()) {
			total += getTotalCastsFromPlayer(player);
		}
		return total;
	}
	public void clearPlayerCasts(Player player) {
		for(String selectionName : selections.keySet()) {
			Selection selection = selections.get(selectionName);
			selection.setCastsFromPlayer(player, 0);
		}
	}
	public Set<? extends UUID> getPlayerUUIDs() {
		Set<UUID> uuids = new HashSet<UUID>();
		for(String selectionName : selections.keySet()) {
			Selection selection = selections.get(selectionName);
			if(selection == null) {
				continue;
			}
			for(UUID uuid : selection.voteCount.keySet()) {
				uuids.add(uuid);
			}
		}
		return uuids;
	}
	public Set<? extends Player> getPlayers() {
		Set<Player> players = new HashSet<Player>();
		Set<? extends UUID> uuids = getPlayerUUIDs();
		for(UUID uuid : uuids) {
			Player player = Bukkit.getPlayer(uuid);
			players.add(player);
		}
		return players;
	}
}