package test.debuglogic.interpreter;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import dibugger.debuglogic.exceptions.DIbuggerLogicException;
import dibugger.debuglogic.interpreter.DoubleValue;
import dibugger.debuglogic.interpreter.Scope;
import dibugger.debuglogic.interpreter.ScopeTuple;
import dibugger.debuglogic.interpreter.TraceState;
import dibugger.debuglogic.interpreter.TraceStatePosition;
import dibugger.debuglogic.interpreter.Type;
import dibugger.debuglogic.interpreter.WatchExpression;

public class WatchExpressionTest {
    WatchExpression we;

    @Test
    public void testEvaluate_constant() throws DIbuggerLogicException {
        ///
        List<TraceState> states = new ArrayList<TraceState>();
        states.add(new TraceState(TraceStatePosition.NOTSPECIAL, 1, new Scope()));
        List<ScopeTuple> scopes = new ArrayList<ScopeTuple>();
        scopes.add(new ScopeTuple(0, 100));
        we = new WatchExpression("3-2", scopes);
        assertTrue(we.evaluate(states).equals("1"));
        we.change("(4%2)+3", scopes);
        assertTrue(we.evaluate(states).equals("3"));
        assertTrue(we.evaluate(states).equals("3"));
    }

    @Test
    public void testEvaluate_validAddition() throws DIbuggerLogicException {
        List<TraceState> states = new ArrayList<TraceState>();
        Scope s = new Scope();
        s.setTypeOf("a", Type.DOUBLE);
        s.setValueOf("a", new DoubleValue(5.3));
        TraceState state = new TraceState(TraceStatePosition.NOTSPECIAL, 1, s);
        state.setProgramId("A");
        states.add(state);
        we = new WatchExpression("3+A.a");
        assertTrue(we.evaluate(states).equals("8.3"));
    }

    @Test
    public void testEvaluate_validBoolean() throws DIbuggerLogicException {
        List<TraceState> states = new ArrayList<TraceState>();
        states.add(new TraceState(TraceStatePosition.NOTSPECIAL, 1, new Scope()));
        List<ScopeTuple> scopes = new ArrayList<ScopeTuple>();
        scopes.add(new ScopeTuple(0, 100));
        we = new WatchExpression("a", scopes);
        we.change("true||false", scopes);
        assertTrue(we.evaluate(states).equals("true"));
        we.change("!!true", scopes);
        assertTrue(we.evaluate(states).equals("true"));
        we.change("(5+3)*2 == 5+3*2", scopes);
        assertTrue(we.evaluate(states).equals("false"));
        we.change("(5+3)*2!=16", scopes);
        assertTrue(we.evaluate(states).equals("false"));
        we.change("3<1+3", scopes);
        assertTrue(we.evaluate(states).equals("true"));
    }
}
