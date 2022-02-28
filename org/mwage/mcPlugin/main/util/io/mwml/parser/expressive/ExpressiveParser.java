package org.mwage.mcPlugin.main.util.io.mwml.parser.expressive;
import org.mwage.mcPlugin.main.util.io.mwml.parser.Parser;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.ExpressiveValue;
public interface ExpressiveParser<E, A, V extends ExpressiveValue<E, A>> extends Parser<E, A, V> {}