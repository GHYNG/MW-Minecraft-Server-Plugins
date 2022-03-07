package org.mwage.mcPlugin.main.util.io.mwml.value.expressive.primary;
import org.mwage.mcPlugin.main.util.io.mwml.parser.expressive.primary.ExpressiveByteParser;
public interface ExpressiveByteValue<V extends ExpressiveByteValue<V, P, A>, P extends ExpressiveByteParser<P, V, A>, A> extends ExpressiveNumberValue<V, P, Byte, A> {}