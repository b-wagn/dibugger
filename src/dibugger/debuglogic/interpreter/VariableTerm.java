package dibugger.debuglogic.interpreter;

import java.util.List;

import dibugger.debuglogic.exceptions.DIbuggerLogicException;
import dibugger.debuglogic.exceptions.IdentifierNotFoundException;

/**
 * Represents a variableaccess based on the values of a single program.
 * 
 * @author wagner
 *
 */
public final class VariableTerm extends Term {
    private String identifier;

    public VariableTerm(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public TermValue evaluate(List<TraceState> states) throws IdentifierNotFoundException {
    	throw new IdentifierNotFoundException(-1, identifier);
    }

    @Override
    public TermValue evaluate(Scope currentScope) throws DIbuggerLogicException {
        TermValue value = currentScope.getValueOf(identifier);
        if (value == null)
            throw new IdentifierNotFoundException(-1, identifier);
        return value;
    }

}
