package org.mwage.mcPlugin.main.util.io.config2.parser;

import org.mwage.mcPlugin.main.util.io.config2.value.ExpressiveNumberValue;

public interface ExpressiveNumberParser<E extends Number, A, V extends ExpressiveNumberValue<E, A>> extends ExpressivePrimaryParser<E, A, V> {}