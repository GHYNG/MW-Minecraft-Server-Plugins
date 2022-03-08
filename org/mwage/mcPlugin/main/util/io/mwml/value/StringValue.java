package org.mwage.mcPlugin.main.util.io.mwml.value;
import org.mwage.mcPlugin.main.util.CircularRegistrationException;
import org.mwage.mcPlugin.main.util.io.mwml.MWMLModule;
import org.mwage.mcPlugin.main.util.io.mwml.NameSpacedConcept.Signature;
import org.mwage.mcPlugin.main.util.io.mwml.parser.StringParser;
import org.mwage.mcPlugin.main.util.io.mwml.value.actual.primary.ActualStringValue;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.complex.ExpressiveComplexValue;
import org.mwage.mcPlugin.main.util.io.mwml.value.expressive.primary.ExpressiveStringValue;
public interface StringValue<V extends StringValue<V, P>, P extends StringParser<P, V>> extends ActualStringValue<V, P, String>, ExpressiveStringValue<V, P, String> {}
interface PureStringValue extends StringValue<PureStringValue, PureStringParser> {}
interface PureStringParser extends StringParser<PureStringParser, PureStringValue> {}
class PureStringValueImplementer implements PureStringValue {
	@Override
	public PureStringValueImplementer clone() {
		return null;
	}
	@Override
	public MWMLModule getInvokerModule() {
		// TODO 自动生成的方法存根
		return null;
	}
	@Override
	public PureStringParser getParser() {
		// TODO 自动生成的方法存根
		return null;
	}
	@Override
	public String getExpressiveInstance() {
		// TODO 自动生成的方法存根
		return null;
	}
	@Override
	public String getActualInstance() {
		// TODO 自动生成的方法存根
		return null;
	}
	@Override
	public Signature getUsingValueSignature() {
		// TODO 自动生成的方法存根
		return null;
	}
	@Override
	public Signature getUsingExpressionSignature() {
		// TODO 自动生成的方法存根
		return null;
	}
	@Override
	public ExpressiveComplexValue<?, ?, ?, ?> getOuterValue() {
		// TODO 自动生成的方法存根
		return null;
	}
	@Override
	public boolean appendToOuterValue(ExpressiveComplexValue<?, ?, ?, ?> outerValue) throws CircularRegistrationException {
		// TODO 自动生成的方法存根
		return false;
	}
}