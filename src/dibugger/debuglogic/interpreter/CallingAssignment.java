package dibugger.debuglogic.interpreter;

import java.util.ArrayList;
import java.util.List;

import dibugger.debuglogic.exceptions.DIbuggerLogicException;
import dibugger.debuglogic.exceptions.IdentifierNotFoundException;
import dibugger.debuglogic.exceptions.WrongTypeAssignmentException;

/**
 * A command to assign the result of a function to a already declared variable.
 * 
 * @author wagner
 *
 */
public class CallingAssignment extends Command {
    private Command funcCall;
    private String identifier;

    public CallingAssignment(GenerationController controller, int linenumber, String identifier, Command funcCall) {
        super(controller, linenumber);
        this.identifier = identifier;
        this.funcCall = funcCall;
    }

    @Override
    public List<TraceState> run() throws DIbuggerLogicException {
        Scope scope = this.controller.getCurrentScope();
        Type type = scope.getTypeOf(identifier);
        // check if variable exists
        if (type == null)
            throw new IdentifierNotFoundException(this.linenumber, identifier);

        // run the function
        List<TraceState> traceStateList = new ArrayList<TraceState>();
        traceStateList.addAll(this.funcCall.run());
        // get the return value
        TermValue value = scope.getReturnValue();
        // check type
        if (type != value.getType()) {
            throw new WrongTypeAssignmentException(this.linenumber);
        }
        // set value
        scope.setValueOf(this.identifier, value);
        traceStateList.remove(traceStateList.size()-1); //remove the state added by the function call and replace it with the state with the assignment done
        traceStateList.add(new TraceState(TraceStatePosition.AFTERRETURN, this.linenumber, scope));
        return traceStateList;
    }

}
