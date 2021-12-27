package org.mwage.mcPlugin.vote.normal;
import java.util.HashMap;
import java.util.Map;
import org.mwage.mcPlugin.vote.Main;
public class VoteNormalSystem {
	public final Main plugin;
	protected Map<String, VoteNormal> ongoingVotes = new HashMap<String, VoteNormal>();
	public VoteNormalSystem(Main plugin) {
		this.plugin = plugin;
	}
}