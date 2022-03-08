package org.mwage.mcPlugin.main.util.io.mwml;
import java.util.ArrayList;
import java.util.List;
public class ValueAddress {
	public final MWMLModule locationModule;
	private final List<String> path = new ArrayList<String>();
	public ValueAddress(MWMLModule locationModule, String... address) {
		this.locationModule = locationModule;
	}
}