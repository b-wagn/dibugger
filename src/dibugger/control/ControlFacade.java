package dibugger.control;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import dibugger.debuglogic.debugger.DebugLogicFacade;
import dibugger.debuglogic.exceptions.DIbuggerLogicException;
import dibugger.debuglogic.exceptions.SyntaxException;
import dibugger.debuglogic.interpreter.ScopeTuple;
import dibugger.filehandler.exceptions.FileHandlerException;
import dibugger.filehandler.exceptions.LanguageNotFoundException;
import dibugger.filehandler.facade.LanguageFile;
import dibugger.userinterface.GUIFacade;

/**
 * Provides an interface for this package's functionality.
 */
public class ControlFacade {
    private boolean isInDebugMode;

    private DebugLogicController debugLogicController;
    private ExceptionHandler exceptionHandler;
    private FileHandlerInteractor fileHandlerInteractor;

    /**
     * Creates a new ControlFacade object. Change in presentation of DIbugger's
     * model component will be triggered by given GUIFacade.
     * 
     * @param guiFacade
     *            a UI-facade of DIbugger
     */
    public ControlFacade(GUIFacade guiFacade) {
        disableDebugMode();
        Objects.requireNonNull(guiFacade);
        debugLogicController = new DebugLogicController();
        debugLogicController.attachToModel(guiFacade);
        try {
            fileHandlerInteractor = new FileHandlerInteractor(debugLogicController, guiFacade);
        } catch (FileHandlerException exception) {
        	exception.printStackTrace();
            System.exit(0);
        }
        exceptionHandler = new ExceptionHandler(fileHandlerInteractor, guiFacade);
    }

    public boolean isInDebugMode() {
        return isInDebugMode;
    }

    private void enableDebugMode() {
        isInDebugMode = true;
    }

    private void disableDebugMode() {
        isInDebugMode = false;
    }

    private void ensureInDebugMode() {
        if (!isInDebugMode()) {
        	//TODO: German translation
            throw new IllegalStateException("This action is not allowed in edit mode.");
        }
    }

    private void ensureNotInDebugMode() {
        if (isInDebugMode()) {
        	//TODO: German translation
            throw new IllegalStateException("This action is not allowed in debug mode.");
        }
    }

    /**
     * Sets the a program's stepsize.
     * 
     * @param programId
     *            the id of program to change the stepsize of
     * @param stepSize
     *            the new stepsize to use while debugging
     * @see DebugLogicController#setStepSize(String, String)
     */
    public void setStepSize(String programId, String stepSize) {
        try {
            debugLogicController.setStepSize(programId, stepSize);
        } catch (SyntaxException exception) {
            exceptionHandler.handle(exception);
        }
    }

    /**
     * Executes a step defined by a given step type.
     * 
     * @param type
     *            the type of the step
     * @see DebugLogicController#step(int)
     */
    public void step(int type) {
        try {
        	ensureInDebugMode();
            debugLogicController.step(type);
        } catch (DIbuggerLogicException exception) {
            exceptionHandler.handle(exception);
        } catch (IllegalStateException e){
        	exceptionHandler.handle(e);
        	//e.printStackTrace();
        }
    }

    /**
     * Continues Debugging / Stepping in all programs until a Breakpoint or
     * Conditional Breakpoint is reached.
     * 
     * @see DebugLogicController#continueDebug()
     */
    public void continueDebug() {
        try {
            ensureInDebugMode();
            debugLogicController.continueDebug();
        } catch (DIbuggerLogicException exception) {
            exceptionHandler.handle(exception);
        } catch (IllegalStateException e){
        	exceptionHandler.handle(e);
        	//e.printStackTrace();
        }
    }

    /**
     * Executes a normal step of size 1 in a given program
     * 
     * @param programNameId
     *            the id of program to do a step in
     * @see DebugLogicController#singleStep(String)
     */
    public void singleStep(String programNameId) {
        try{
        	ensureInDebugMode();
            debugLogicController.singleStep(programNameId);
        } catch (IllegalStateException e){
        	exceptionHandler.handle(e);
        	//e.printStackTrace();
        }
    }

    /**
     * Creates a new watch expression.
     * 
     * @param watchExpressionId
     *            the id of the watch expression
     * @param expression
     *            the expression of the watch expression
     * @see DebugLogicController#createWatchExpression(int, String)
     */
    public void createWatchExpression(int watchExpressionId, String expression) {
        try {
            debugLogicController.createWatchExpression(watchExpressionId, expression);
        } catch (DIbuggerLogicException e) {
            exceptionHandler.handle(e);
        }
    }

    /**
     * Changes the specified watch expression.
     * 
     * @param watchExpressionId
     *            the id of the watch expression to change
     * @param expression
     *            the new expression
     * @param scopes
     *            a list of scopes for the new watch expression
     * @see DebugLogicController#changeWatchExpression(int, String, List)
     */
    public void changeWatchExpression(int watchExpressionId, String expression, List<ScopeTuple> scopes) {
        try {
            debugLogicController.changeWatchExpression(watchExpressionId, expression, scopes);
        } catch (DIbuggerLogicException e) {
            exceptionHandler.handle(e);
        }
    }

    /**
     * Deletes the specified watch expression.
     * 
     * @param watchExpressionId
     *            the id of the watch expression
     * @see DebugLogicController#deleteWatchExpression(int)
     */
    public void deleteWatchExpression(int watchExpressionId) {
        debugLogicController.deleteWatchExpression(watchExpressionId);
    }

    /**
     * Creates a new conditional breakpoint.
     * 
     * @param breakPointId
     *            the id of the breakpoint
     * @param condition
     *            the condition of the breakpoint
     * @see DebugLogicController#createConditionalBreakpoint(int, String)
     */
    public void createConditionalBreakpoint(int breakPointId, String condition) {
        try {
            debugLogicController.createConditionalBreakpoint(breakPointId, condition);
        } catch (DIbuggerLogicException e) {
            exceptionHandler.handle(e);
        }
    }

    /**
     * Changes the specified conditional breakpoint.
     * 
     * @param breakPointId
     *            the id of the breakpoint to change
     * @param condition
     *            the condition of the breakpoint
     * @param scopes
     *            a list of all scopes
     * @see DebugLogicController#changeConditionalBreakpoint(int, String, List)
     */
    public void changeConditionalBreakpoint(int breakPointId, String condition, List<ScopeTuple> scopes) {
        try {
            debugLogicController.changeConditionalBreakpoint(breakPointId, condition, scopes);
        } catch (DIbuggerLogicException e) {
            exceptionHandler.handle(e);
        }
    }

    /**
     * Deletes specified breakpoint.
     * 
     * @param conditionalBreakpointId
     *            the id of the breakpoint
     * @see DebugLogicController#deleteConditionalBreakpoint(int)
     */
    public void deleteConditionalBreakpoint(int conditionalBreakpointId) {
        debugLogicController.deleteConditionalBreakpoint(conditionalBreakpointId);
    }

    // /**
    // * Creates a breakpoint at given line in all programs known to DIbugger.
    // *
    // * @param line the line to create a breakpoint at
    // * @see DebugLogicController#createSynchronousBreakpoint(int)
    // */
    // public void createSynchronousBreakpoint(int line) {
    // debugLogicController.createSynchronousBreakpoint(line);
    // }

    /**
     * Creates a new breakpoint at given line of specified program.
     * 
     * @param programNameId
     *            the program's id
     * @param line
     *            the line to create a breakpoint at
     * @see DebugLogicController#createBreakpoint(String, int)
     */
    public void createBreakpoint(String programNameId, int line) {
        debugLogicController.createBreakpoint(programNameId, line);
    }

    /**
     * Deletes a breakpoint at given line of specified program.
     * 
     * @param programNameId
     *            the id of program to delete a breakpoint of
     * @param line
     *            the line number referring to the breakpoint
     * @see DebugLogicController#deleteBreakpoint(String, int)
     */
    public void deleteBreakpoint(String programNameId, int line) {
        debugLogicController.deleteBreakpoint(programNameId, line);
    }

    /**
     * Deletes all breakpoints in all programs.
     * 
     * @see DebugLogicController#deleteAllBreakpoints()
     */
    public void deleteAllBreakpoints() {
        debugLogicController.deleteAllBreakpoints();
    }

    /**
     * Stores given lists of multiple program's assigments of input variables,
     * text and identifier. For each program, its assignments of input variables
     * is expected to a {@code String} of this kind: "a = 10;b = "bridge"",
     * where "a" and "b" are input variables.
     * 
     * @param inputTexts
     *            a list containing each program's input variable-assignments
     * @param programTexts
     *            a list containing each program's text
     * @param programIdentifiers
     *            a list of containing each program's identifier
     * @see DebugLogicController#saveText(List, List, List)
     */
    public void saveText(List<String> inputTexts, List<String> programTexts, List<String> programIdentifiers) {
        /*
         * Notiz: Pr??fen ob Beispiel im Kommentar oben stimmt.
         */
        debugLogicController.saveText(inputTexts, programTexts, programIdentifiers);
    }

    /**
     * Switches DIbugger's mode to debug-mode. Input of user stored in this
     * Controlfacade via {@link #saveText(List, List, List)} will be sent to
     * package {@code dibugger.debuglogic.debugger}.
     * 
     * @see DebugLogicController#startDebug()
     */
    public void startDebug() {
        enableDebugMode();
        try {
            debugLogicController.startDebug();
        } catch (DIbuggerLogicException exception) {
            exceptionHandler.handle(exception);
        }
    }

    /**
     * Switches DIbugger's mode to edit-mode.
     * 
     * @see DebugLogicController#stopDebug()
     */
    public void stopDebug() {
        disableDebugMode();
        debugLogicController.stopDebug();
    }

    /**
     * Partially resets state of DIbugger's model component.
     * 
     * @see DebugLogicController#reset()
     */
    public void reset() {
        debugLogicController.reset();
    }

    /**
     * Creates a {@code ConfigurationFile} using given {@code File} and attempts
     * to restore a debugging-session by altering DIbugger's model- and
     * presentation-component.
     * 
     * @param configurationFile
     *            the {@code File} to load
     * @see FileHandlerInteractor#loadConfigurationFile(File)
     */
    public void loadConfiguration(File configurationFile) {
        

        try {
        	ensureNotInDebugMode();
            fileHandlerInteractor.loadConfigurationFile(configurationFile);
        } catch (FileHandlerException exception) {
            exceptionHandler.handle(exception);
        } catch (DIbuggerLogicException e) {
            exceptionHandler.handle(e);
        } catch (Exception e) {
        	exceptionHandler.handle(e);
        }
    }

    /**
     * Saves some of Dibugger's model- and presentation-component's state to a
     * specified {@code File}.
     * 
     * @param configurationFile
     *            a {@code File} to save DIbugger's state to
     * @see FileHandlerInteractor#saveConfiguration(File)
     */
    public void saveConfiguration(File configurationFile) {
        fileHandlerInteractor.saveConfiguration(configurationFile);
    }

    /**
     * Prompts this' GUIFacade to display a specified program's text.
     * 
     * @param file
     *            the file containing the text
     * @return the text contained in file
     * @see FileHandlerInteractor#loadProgramText(File)
     */
    public String loadProgramText(File file) {
    	try {
    		ensureNotInDebugMode();
    		return fileHandlerInteractor.loadProgramText(file);
    	}
    	catch(Exception e) {
    		exceptionHandler.handle(e);
    		return "";
    	}
        
    }

    /**
     * Returns a list containing all languages available for use by this'
     * GUIFacade.
     * 
     * @return a list containing all languages available
     * @see FileHandlerInteractor#getAvailableLanguages()
     */
    public List<String> getAvailableLanuages() {
        return fileHandlerInteractor.getAvailableLanguages();
    }

    /**
     * Sets the maximum-iteration-count (example: while loop).
     * 
     * @param maximum
     *            the new maximum
     * @see DebugLogicController#setMaximumIterations(int)
     */
    public void setMaximumIterations(int maximum) {
        debugLogicController.setMaximumIterations(maximum);
    }

    /**
     * Sets the upper limit of function calls allowed when executing a program.
     * 
     * @param maximum
     *            the new maximum
     * @see DebugLogicController#setMaximumFunctionCalls(int)
     */
    public void setMaximumFunctionCalls(int maximum) {
        debugLogicController.setMaximumFunctionCalls(maximum);
    }

    /**
     * Suggest a stepsize for all programs.
     * 
     * @see DebugLogicController#suggestStepSize()
     */
    public void suggestStepSize() {
        debugLogicController.suggestStepSize();
    }

    /**
     * Suggests a watch expression.
     * 
     * @return String representing the expression
     * @see DebugLogicController#suggestWatchExpression()
     */
    public String suggestWatchExpression() {
        return debugLogicController.suggestWatchExpression();
    }

    /**
     * Suggests a conditional breakpoint.
     * 
     * @return String representing the condition
     * @see DebugLogicController#suggestConditionalBreakpoint()
     */
    public String suggestConditionalBreakpoint() {
        return debugLogicController.suggestConditionalBreakpoint();
    }

    /**
     * Suggests an InputValue for a given variable in a given range.
     * 
     * @param inputVariableId
     *            the variable's name
     * @param range
     *            the range containing the value to suggest
     * @param type
     *            the type of the variable
     * @return String representing the suggestion value
     * @see DebugLogicController#suggestInputValue(String, String, int)
     */
    public String suggestInputValue(String inputVariableId, String range, int type) {
        try{
        	return debugLogicController.suggestInputValue(inputVariableId, range, type);
        } catch (DIbuggerLogicException e){
        	exceptionHandler.handle(e);
        }
		return "";
    }

    /**
     * Select a strategy to be used to suggest step sizes.
     * 
     * @param stepSizeStrategyId
     *            the strategy id to select
     * @see DebugLogicController#selectStepSizeStrategy(int)
     */
    public void selectStepSizeStrategy(String stepSizeStrategyId) {
        debugLogicController.selectStepSizeStrategy(stepSizeStrategyId);
    }

    /**
     * Select a strategy to be used to suggest realtional expressions
     * 
     * @param expressionStrategyId
     *            the strategy id to select
     * @see DebugLogicController#selectRelationalExpressionStrategy(int)
     */
    public void selectRelationalExpressionStrategy(String expressionStrategyId) {
        debugLogicController.selectRelationalExpressionStrategy(expressionStrategyId);
    }

    /**
     * Select a strategy to be used to suggest input values
     * 
     * @param inputValueStrategyId
     *            the strategy id to select
     * @see DebugLogicController#selectInputValueStrategy(int)
     */
    public void selectInputValueStrategy(String inputValueStrategyId) {
        debugLogicController.selectInputValueStrategy(inputValueStrategyId);
    }

    /**
     * Changes the language in which information is shown by this' GUIFacade.
     * 
     * @param languageId
     *            the id specifying the language
     * @see FileHandlerInteractor#changeLanguage(String)
     */
    public void changeLanguage(String languageId) {
        try {
            fileHandlerInteractor.changeLanguage(languageId);
            exceptionHandler.setLanguageFile(fileHandlerInteractor.getLanguageFile());
        } catch (LanguageNotFoundException exception) {
            exceptionHandler.handle(exception);
        }
    }

    /**
     * Returns the DebugLogicFacade known to this ControlFacade.
     * 
     * @return the DebugLogicFacade known to this
     */
    public DebugLogicFacade getDebugLogicFacade() {
        return debugLogicController.getDebugLogicFacade();
    }

    /**
     * Returns all available strategies for RelationalExpressionSuggestions.
     *
     * @return all available strategies for RelationalExpressionSuggestions
     */
    public List<String> getRelationalExpressionSuggestionStrategies() {
        return debugLogicController.getRelationalExpressionSuggestionStrategies();
    }

    /**
     * Returns all available strategies for StepSizeSuggestions.
     *
     * @return all available strategies for StepSizeSuggestions
     */
    public List<String> getStepSizeSuggestionStrategies() {
        return debugLogicController.getStepSizeSuggestionStrategies();
    }

    /**
     * Returns all available strategies for InputValueSuggestions.
     *
     * @return all available strategies for InputValueSuggestions
     */
    public List<String> getInputValueSuggestionStrategies() {
        return debugLogicController.getInputValueSuggestionStrategies();
    }

    /**
     * Returns the language file of the currently selected language.
     *
     * @return current language file.
     */
    public LanguageFile getLanguageFile() {
        return fileHandlerInteractor.getLanguageFile();
    }

    public Map<String, Integer> getWatchExpressionScopeBeginnnings(int expressionId) {
        return debugLogicController.getWatchExpressionScopeBeginnnings(expressionId);
    }

    public Map<String, Integer> getWatchExpressionScopeEnds(int expressionId) {
        return debugLogicController.getWatchExpressionScopeEnds(expressionId);
    }

    public Map<String, Integer> getConditionalBreakpointScopeBeginnings(int expressionId) {
        return debugLogicController.getConditionalBreakpointScopeBeginnings(expressionId);
    }

    public Map<String, Integer> getConditionalBreakpointScopeEnds(int expressionId) {
        return debugLogicController.getConditionalBreakpointScopeEnds(expressionId);
    }
    
    public void saveProperties(){
        fileHandlerInteractor.getPropertiesFile().setMaxFunctionCalls(debugLogicController.getDebugLogicFacade().getMaxFunctionCalls());
        fileHandlerInteractor.getPropertiesFile().setMaxWhileIterations(debugLogicController.getDebugLogicFacade().getMaxIterations());
        fileHandlerInteractor.getPropertiesFile().setSelectedLanguage(fileHandlerInteractor.getLanguageFile().getLangID());
        fileHandlerInteractor.savePropertiesFile();
    }
}
