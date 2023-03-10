package dibugger.userinterface;

import dibugger.debuglogic.debugger.DebugLogicFacade;
import dibugger.debuglogic.exceptions.DIbuggerLogicException;
import dibugger.debuglogic.interpreter.ScopeTuple;
import dibugger.filehandler.facade.LanguageFile;
import dibugger.userinterface.dibuggerpopups.ExpressionChangePopUp;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

/**
 * class that represents a ConditionalBreakpointPanel where the user can see,
 * edit and delete his conditional breakpoints.
 */
public class ConditionalBreakpointPanel extends ExpressionPanel {
    private static final long serialVersionUID = -5451353323378495075L;

    private static ConditionalBreakpointPanel singleton = null;
    private MainInterface mainInterface;
    private java.util.List<String[]> dataEntries;
    private HashMap<Integer, Integer> idMap = new HashMap<>();
    private int currentHighestId = 0;
    private HashMap<Integer, ArrayList<ScopeTuple>> scopes = new HashMap<>();
    private JTable table;
    private DefaultTableModel tableModel;

    private static String CB_TOOLTIP = "<html>Erstes Feld für Optionen zu diesem bedingten "
            + "Breakpoint, <br> mittleres Feld um den CB zu ändern. <br> Um einen neuen CB "
            + "hinzuzufügen, mittleres Feld der letzten Zeile anklicken.</html>";

    private ConditionalBreakpointPanel(MainInterface mainInterface) {
        this.mainInterface = mainInterface;
        initComponents();
        this.setVisible(true);
    }

    /**
     * method that implements the singleton aspect of this class.
     *
     * @param mainInterface
     *            MainInterface on which the ConditionalBreakpointPanel is used
     * @return the ConditionalBreakpointPanel (if it does not exists it creates
     *         a new one, if it exists you get the existing one)
     */
    public static ConditionalBreakpointPanel getConditionalBreakpointPanel(MainInterface mainInterface) {
        if (singleton == null) {
            singleton = new ConditionalBreakpointPanel(mainInterface);
        }
        return singleton;
    }

    /**
     * update Method that implements the observer pattern is called by the model
     * part of the mvc pattern updates the important values that are calculated
     * by the model part.
     */
    public void update(Observable o) {
        DebugLogicFacade debugLogicFacade = (DebugLogicFacade) o;
        Map<Integer, String> map = debugLogicFacade.getConditionalBreakpointMap();
//        for (int i = 0; i <= currentHighestId; i++) {
//            try {
//            	String val = map.get(i);
//                if (dataEntries[i] != null && val!=null) {
//                    dataEntries[i][1] = val;
//                    dataEntries[i][2] = debugLogicFacade.getCBValue(i);
//                    table.getModel().setValueAt(dataEntries[i][1], i, 1);
//                    table.getModel().setValueAt(dataEntries[i][2], i, 2);
//                }
//            } catch (DIbuggerLogicException e) {
//                // TODO do nothing?
//            }
//        }
        for(Integer i : map.keySet()){
        	if(!idMap.containsValue(i)){
        		addRow(i);
        	}
        }
        Object[][] oa = new Object[dataEntries.size()][3];
        for(int i=0;i<dataEntries.size();++i){
        	try {
            	String val = map.get(idMap.get(i));
            	String[] data = dataEntries.get(i);
                if (data != null && val!=null) {
                	data[1] = val;
                	data[2] = ""+debugLogicFacade.getCBValue(idMap.get(i));
                	for(int j=0;j<3;++j){
                		oa[i][j] = data[j];
                		table.getModel().setValueAt(data[j], i, j);
                	}
                }
            } catch (DIbuggerLogicException e) {
                // TODO: Muss von der LogicFacade gefangen werden
            }
        }
        table.updateUI();
        // TODO: check
    }

    private void initComponents() {

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel panel_ar = new JPanel();
        panel_ar.setLayout(new BoxLayout(panel_ar, BoxLayout.X_AXIS));
        this.add(panel_ar);        
        
        JButton addButton = new JButton("+");
        panel_ar.add(addButton);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRow();
                mainInterface.getControlFacade().createConditionalBreakpoint(currentHighestId, "A.i <= B.k");
                getConditionalBreakpointPanel(mainInterface).updateUI();
                saveCBs();
            }
        });
        
        JButton removeButton = new JButton("-");
        panel_ar.add(removeButton);
        removeButton.addActionListener(e -> {
        	int row = table.getSelectedRow();
        	if(row!=-1){
            	mainInterface.getControlFacade().deleteConditionalBreakpoint(idMap.get(row));
        		deleteEntry(row);
        	}
        });
        

        idMap.put(0, 0);

        panelType = "Conditional Breakpoints:";
        String[] columnTitles;
        columnTitles = new String[] { "Opt", panelType, "=" };
        dataEntries = new ArrayList<String[]>();
        String[] s = new String[3];
        s[0] = " ";
        s[1] = "false";
        s[2] = "false";
        dataEntries.add(s);
        mainInterface.getControlFacade().createConditionalBreakpoint(0, "false");
        tableModel = new DefaultTableModel(createDataFromList(), columnTitles) {
            private static final long serialVersionUID = 8335695487059890207L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        };
        table = new JTable(tableModel);
        table.setToolTipText(CB_TOOLTIP);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(130);
        table.getColumnModel().getColumn(2).setPreferredWidth(5);
        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
            	if(mouseEvent.getButton()==MouseEvent.BUTTON1){
	                Point p = mouseEvent.getPoint();
	                if (table.columnAtPoint(p) == 0) {
	                    int row = table.rowAtPoint(p);
	                    int id = idMap.get(row);
	                    new ExpressionChangePopUp(mainInterface, "ConditionalBreakpoint", row, table,
	                            ConditionalBreakpointPanel.this, id);
	                }
            	}
	            saveCBs();
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                saveCBs();
            }
        });
        table.setSize(50,50);
        table.getColumnModel().getColumn(0).setCellRenderer(new ButtonCellRenderer());
        
        JScrollPane tableContainer = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tableContainer.setViewportView(table);
        tableContainer.createVerticalScrollBar();
        tableContainer.setPreferredSize(new Dimension(290, 250));

        this.add(tableContainer);

    }

    /**
     * method to save the scopes of a ConditionalBreakpoint.
     *
     * @param id
     *            ID of the ConditionalBreakpoint
     * @param scopeTupels
     *            Tuple with (start, end)
     */
    public void saveScopes(int id, ArrayList<ScopeTuple> scopeTupels) {
        scopes.put(id, scopeTupels);
    }

    /**
     * method to reset the ConditionalBreakpointPanel.
     */
    public void reset() {
        singleton = new ConditionalBreakpointPanel(mainInterface);
        this.updateUI();
    }

    /**
     * method to delete an entry of the ConditionalBreakpointPanel.
     *
     * @param rowToDelete
     *            row that should be deleted
     */
    public void deleteEntry(int rowToDelete) {
//        ArrayList<Object[]> dataEntriesAsList = new ArrayList<>(Arrays.asList(dataEntries));
    	if (dataEntries.size() > 0) {
            tableModel.removeRow(rowToDelete);
            dataEntries.remove(rowToDelete);
//            for (int row : idMap.keySet()) {
//                if (row > rowToDelete) {
//                    idMap.put(row - 1, idMap.get(row));
//                }
//            }
            for(int i=rowToDelete;i<dataEntries.size()-1;++i){
            	idMap.put(i, idMap.get(i+1));
            }
        }
//        dataEntriesAsList.toArray(dataEntries);
    }

    private void addRow() {
        int row = this.table.getRowCount();
        idMap.put(row, currentHighestId + 1);
        currentHighestId++;
        String[] newRow = { "Opt", "false", "" };
        tableModel.addRow(newRow);
//        ArrayList<Object[]> dataAsList = new ArrayList<>(dataEntries.length);
//        dataAsList.addAll(Arrays.asList(dataEntries));
//        dataAsList.add(newRow);
//        dataEntries = new Object[dataAsList.size()][];
//        for (int j = 0; j < dataAsList.size(); j++) {
//            dataEntries[j] = dataAsList.get(j);
//        }
        dataEntries.add(newRow);
        this.updateUI();
    }

    private void addRow(int id) {
        int row = this.table.getRowCount();
        idMap.put(row, id);
        currentHighestId = Math.max(currentHighestId, id);
        String[] newRow = { "Opt", "false", " " };
        tableModel.addRow(newRow);
        dataEntries.add(newRow);
        this.updateUI();
    }
    
    protected void saveCBs() {
        for (int j = 0; j < table.getRowCount(); j++) {
            mainInterface.getControlFacade().changeConditionalBreakpoint(idMap.get(j),
                    table.getModel().getValueAt(j, 1).toString(), scopes.get(j));
        }
    }

    void changeLanguage() {
        LanguageFile languageFile = mainInterface.getControlFacade().getLanguageFile();
        if (languageFile != null) {
            table.setToolTipText(languageFile.getTranslation("ui_cb_tooltip"));
        }
    }
    
    private Object[][] createDataFromList(){
    	Object[][] o = new Object[dataEntries.size()][3];
    	for(int i=0;i<dataEntries.size();++i){
    		String[] s = dataEntries.get(i);
    		for(int j=0;j<3;++j){
    			o[i][j] = s[j];
    		}
    	}
    	return o;
    }
    
    //Option Cell Renderer
    private class ButtonCellRenderer extends DefaultTableCellRenderer{
		private static final long serialVersionUID = 1L;
		private final JButton btn = new JButton("Opt");

		@Override
    	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
    			int row, int column) {
    		return btn;
    	}
    	
    }
}
