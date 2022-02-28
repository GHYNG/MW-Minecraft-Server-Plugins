package org.mwage.mcPlugin.main.util.io.mwml.parser.expressive;

import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.ExpressiveNumberValue;

public interface ExpressiveNumberParser<E extends Number, A, V extends ExpressiveNumberValue<E, A>> extends ExpressivePrimaryParser<E, A, V> {}