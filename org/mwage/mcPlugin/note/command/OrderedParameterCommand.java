package org.mwage.mcPlugin.note.command;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
public abstract class OrderedParameterCommand {
	private final Map<String, OrderedParameterCommand> subCommands = new HashMap<String, OrderedParameterCommand>();
	// private final Map<CommandSender, OrderedParameterCommand> subCommandTabCompleteSuggestionMode = new HashMap<CommandSender, OrderedParameterCommand>();
	public final OrderedParameterCommand parent;
	/*
	 * The default command.
	 * May not be the same as key from parent command tree.
	 */
	public final String command; // this should ALWAYS match key from parent map
	public OrderedParameterCommand(OrderedParameterCommand parent, String command) {
		this.parent = parent;
		this.command = command;
	}
	public Map<String, OrderedParameterCommand> getSubCommands() {
		return subCommands;
	}
	public void activateSubCommand(OrderedParameterCommand subCommand) {
		if(subCommand.parent != this) {
			throw new IllegalArgumentException("Sub command must be this command's child.");
		}
		subCommands.put(subCommand.command, subCommand);
	}
	public void deactivateSubCommand(OrderedParameterCommand subCommand) {
		if(subCommand.parent != this) {
			throw new IllegalArgumentException("Sub command must be this command's child.");
		}
		subCommands.remove(subCommand.command);
	}
	public abstract boolean isCommandLegal(CommandSender sender, String command);
	public abstract boolean isComplete();
	/*
	 * ################
	 * # Tab Complete #
	 * ################
	 * This section is used for command parameter completion for different command branches.
	 */
	/*
	 * defaults null.
	 * Only returns a list when actual required parameters are not the same as the key from parent command tree.
	 * Should never return key from parent.
	 */
	public List<String> getLocalTabComplete(CommandSender sender, String command) {
		return null;
	}
	public final List<String> getTabComplete(CommandSender sender, String command, String[] args) {
		int length = args.length;
		List<String> results = new ArrayList<String>();
		if(length == 0) {
			// subCommandTabCompleteSuggestionMode.remove(sender);
			// if(parent != null && this.command.equals(command)) {
			// parent.subCommandTabCompleteSuggestionMode.put(sender, this);
			// }
			if(this.command.equals(command) || command.equals(this.command + ".") || command.equals(this.command + "`")) {
				if(isCommandLegal(sender, this.command)) {
					results.add(this.command);
					return results;
				}
				else {
					List<String> localResults = getLocalTabComplete(sender, command);
					if(localResults != null) {
						localResults.forEach(l -> {
							results.add(l);
						});
					}
					return results;
				}
			}
			else {
				if(startsWith(this.command, command)) {
					results.add(this.command);
				}
				List<String> localResults = getLocalTabComplete(sender, command);
				if(localResults != null) {
					for(String localResult : localResults) {
						if(startsWith(localResult, command) && !command.equals("")) {
							results.add(localResult);
						}
					}
				}
				return results;
			}
		}
		if(!isCommandLegal(sender, command)) {
			return results;
		}
		String[] subargs = getSubArgs(args);
		Set<String> keys = subCommands.keySet();
		if(length == 1) {
			// OrderedParameterCommand subMode = subCommandTabCompleteSuggestionMode.get(sender);
			List<String> possibleResults = new ArrayList<String>();
			for(String key : keys) {
				OrderedParameterCommand subCommand = subCommands.get(key);
				if(subCommand != null) {
					// if(subCommand == subMode || subMode == null) {
					// possibleResults.addAll(subCommand.getTabComplete(sender, args[0], subargs));
					// }
					possibleResults.addAll(subCommand.getTabComplete(sender, args[0], subargs));
				}
			}
			results.addAll(possibleResults);
			return results;
		}
		else {
			for(String key : keys) {
				OrderedParameterCommand subCommandTree = subCommands.get(key);
				if(subCommandTree == null) {
					continue;
				}
				if(subCommandTree.isCommandLegal(sender, args[0])) {
					return subCommandTree.getTabComplete(sender, args[0], subargs);
				}
			}
		}
		return results;
	}
	/*
	 * #############
	 * # Echo Help #
	 * #############
	 * Echo Help is used when player inputs wrong parameters,
	 * the system will send them the message about the correct way to input commands.
	 */
	public abstract String getLocalLocalEchoHelp(CommandSender sender, String command);
	public String getLocalEchoHelp(CommandSender sender, String command) {
		String lineSep = "\n"; // System.getProperty("line.separator");
		String echo = parent == null ? "/" + this.command : this.command;
		OrderedParameterCommand parent = this.parent;
		while(parent != null) {
			String prefix = parent.parent == null ? "/" + parent.command : parent.command;
			echo = prefix + " " + echo;
			parent = parent.parent;
		}
		echo = ChatColor.GRAY + echo;
		String localLocalEcho = getLocalLocalEchoHelp(sender, command);
		String[] localLocalLines = localLocalEcho.split(lineSep);
		for(String line : localLocalLines) {
			echo += ChatColor.DARK_GRAY + lineSep + "  " + line;
		}
		for(String key : subCommands.keySet()) {
			OrderedParameterCommand subCommand = subCommands.get(key);
			if(subCommand != null) {
				String subEcho = subCommand.getLocalEchoHelp(sender, command);
				String[] subEchoLines = subEcho.split(lineSep);
				for(String line : subEchoLines) {
					echo += lineSep + "  " + line;
				}
			}
		}
		return echo;
	}
	public final String getEchoHelp(CommandSender sender, String command, String[] args) {
		int length = args.length;
		if(length == 0) {
			return getLocalEchoHelp(sender, command);
		}
		else {
			if(!isCommandLegal(sender, command)) {
				return getLocalEchoHelp(sender, command);
			}
			String[] subargs = getSubArgs(args);
			OrderedParameterCommand subCommand = subCommands.get(args[0]);
			if(subCommand != null) {
				return subCommand.getEchoHelp(sender, args[0], subargs);
			}
			Set<String> keys = subCommands.keySet();
			for(String key : keys) {
				subCommand = subCommands.get(key);
				if(subCommand != null && subCommand.isCommandLegal(sender, args[0])) {
					return subCommand.getEchoHelp(sender, args[0], subargs);
				}
			}
			return getLocalEchoHelp(sender, command);
		}
	}
	/*
	 * #########################
	 * # Command Running Logic #
	 * #########################
	 * The logic when a given command (with all parameters inputed) is firing.
	 */
	public abstract boolean localPreRun(CommandSender sender, String command);
	public abstract boolean localPostRun(CommandSender sender, String command);
	public final boolean run(CommandSender sender, String command, String[] args) {
		int length = args.length;
		if(length == 0) {
			if(isComplete() && isCommandLegal(sender, command)) {
				boolean runSuccessfully = localPreRun(sender, command);
				if(!runSuccessfully) {
					return false;
				}
				return localPostRun(sender, command);
			}
			return false;
		}
		else {
			if(!isCommandLegal(sender, command)) {
				return false;
			}
			boolean runSuccessfully = false;
			runSuccessfully = localPreRun(sender, command);
			if(!runSuccessfully) {
				return false;
			}
			String[] subargs = getSubArgs(args);
			OrderedParameterCommand subCommand = subCommands.get(args[0]);
			if(subCommand != null && subCommand.isCommandLegal(sender, args[0])) {
				runSuccessfully = subCommand.run(sender, args[0], subargs);
			}
			else {
				Set<String> keys = subCommands.keySet();
				for(String key : keys) {
					subCommand = subCommands.get(key);
					if(subCommand != null && subCommand.isCommandLegal(sender, args[0])) {
						runSuccessfully = subCommand.run(sender, args[0], subargs);
						break;
					}
				}
			}
			if(!runSuccessfully) {
				return false;
			}
			return localPostRun(sender, command);
		}
	}
	/*
	 * ##########
	 * # Update #
	 * ##########
	 * This section is used to update information and data stored in the command tree.
	 * This logic is called every time when the command is attempted to run or tab complete.
	 */
	public abstract void localUpdate(CommandSender sender, String command);
	public final void update(CommandSender sender, String command, String[] args) {
		int length = args.length;
		if(length == 0) {
			if(isCommandLegal(sender, command)) {
				localUpdate(sender, command);
			}
		}
		else {
			if(!isCommandLegal(sender, command)) {
				return;
			}
			localUpdate(sender, command);
			String[] subargs = getSubArgs(args);
			Set<String> keys = subCommands.keySet();
			for(String key : keys) {
				OrderedParameterCommand subCommand = subCommands.get(key);
				if(subCommand == null) {
					continue;
				}
				subCommand.update(sender, args[0], subargs);
			}
		}
	}
	/*
	 * Below are util methods.
	 */
	private static String[] getSubArgs(String[] args) {
		int length = args.length;
		if(length == 0) {
			return null;
		}
		String[] subargs = new String[length - 1];
		for(int i = 1; i < length; i++) {
			subargs[i - 1] = args[i];
		}
		return subargs;
	}
	private static boolean startsWith(String parent, String child) {
		return parent.toLowerCase().startsWith(child.toLowerCase());
	}
}