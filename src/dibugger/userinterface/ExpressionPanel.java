package dibugger.userinterface;

import javax.swing.JPanel;
import java.util.Observable;

/**
 * Abstract class.
 */
public abstract class ExpressionPanel extends JPanel {
    private static final long serialVersionUID = -6801243723500555354L;

    String panelType = "ExpressionPanel";

    /**
     * update-Method for observer pattern.
     */
    public abstract void update(Observable o);

}
