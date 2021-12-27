package org.mwage.mcPlugin.vote;
import java.util.List;
import java.util.Map;
import org.bukkit.command.CommandSender;
public interface Vote_GeneralMethods {
	default void sendMessage(CommandSender receiver, List<String> message) {
		for(String line : message) {
			receiver.sendMessage(line);
		}
	}
	default String convertMessage(String originalContent, Map<String, String> replacer) {
		String processedContent = originalContent;
		for(String key : replacer.keySet()) {
			String newStr = replacer.get(key);
			if(newStr == null) {
				continue;
			}
			processedContent = originalContent.replaceAll(key, newStr);
		}
		return processedContent;
	}
	default boolean goodIdentifier(String identifier) {
		if(identifier == null || identifier.length() < 1) {
			return false;
		}
		int index = 0;
		if(Character.isJavaIdentifierStart(identifier.charAt(index))) {
			while(++index < identifier.length()) {
				if(!Character.isJavaIdentifierPart(identifier.charAt(index))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
}
