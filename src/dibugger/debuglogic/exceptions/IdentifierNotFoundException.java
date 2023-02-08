package dibugger.debuglogic.exceptions;

/**
 * Exception thrown if trying to access a non-existing variable.
 * 
 * @author scheler
 *
 */
public class IdentifierNotFoundException extends DIbuggerLogicException {

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for a VariableNotFoundException.
     * 
     * @param occurrence
     *            the line of occurrence of the exception
     */
    public IdentifierNotFoundException(int occurrence, String id) {
        super(occurrence, "dl_exc_variable_not_found", "Unknown identifier in: "+id);        
    }

}
