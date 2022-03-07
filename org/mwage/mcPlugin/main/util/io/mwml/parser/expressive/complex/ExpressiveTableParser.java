package org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.complex;
import java.util.Map;
import org.mwage.mcPlugin.main.util.io.mwml.parser.Parser;
import org.mwage.mcPlugin.main.util.io.mwml.value.Value;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.complex.ExpressiveTableValue;
// @formatter:off
public interface ExpressiveTableParser <
	P extends ExpressiveTableParser <
		P,
		V, 
		E, 
			E_V, 
			E_P, 
				E_VP_E, 
				E_VP_A, 
		A
	>,
	V extends ExpressiveTableValue <
		V, 
		P, 
		E, 
			E_V, 
			E_P, 
				E_VP_E, 
				E_VP_A, 
		A
	>,
	E extends Map <
		String, 
		E_V
	>,
		E_V extends Value <
			E_V, 
			E_P, 
				E_VP_E, 
				E_VP_A
		>,
		E_P extends Parser <
			E_P, 
			E_V, 
				E_VP_E, 
				E_VP_A
		>,
			E_VP_E,
			E_VP_A,
	A
>
extends ExpressiveComplexParser <
	P, 
	V, 
	E, 
	A
>
// @formatter:on
{}
