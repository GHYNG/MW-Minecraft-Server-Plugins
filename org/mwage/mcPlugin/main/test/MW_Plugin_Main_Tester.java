package org.mwage.mcPlugin.main.test;
import org.mwage.mcPlugin.main.util.io.config1.value.CollectionValue;
import org.mwage.mcPlugin.main.util.io.config1.value.IntValue;
import org.mwage.mcPlugin.main.util.io.config1.value.IntValueProcessor;
import org.mwage.mcPlugin.main.util.io.config1.value.Value;
import org.mwage.mcPlugin.main.util.methods.ClassUtil;
public class MW_Plugin_Main_Tester {
	public static void main(String[] args) {
		IntValueProcessor parser = new IntValueProcessor();
		IntValue iv = parser.generate(null, 250);
		System.out.println(iv.getGenericTypesInfo());
	}
}