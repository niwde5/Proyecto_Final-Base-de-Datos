
package ui.mainWindows;

import database.Connect;
import database.OperationDatabase;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import ui.Colors;
import ui.popupWindows.DeleteEmployee;
import ui.popupWindows.RegisterUpdateEmployee;

public class Admin extends JPanel{
    
    private final String titleTopWindow = "Lista de empleados";
    
    private Map<String, JPanel> panels = new HashMap();
    private Map<String, JPanel> panelsTop = new HashMap();
    private JPanel leftPanel;
    
    private Map<String, JButton> buttomsLeft = new HashMap();
    
    private JTable tableEmployees;
    private DefaultTableModel dtm ;
    private DefaultTableCellRenderer modelCenter ;
    
    final Color colorTableTop = new Color(198,198,198);
    final Color backgroundTable = new Color(243,243,239);
    final Color colorTextSelectedTable = new Color(0,112,186);
    final Color colorSelectedCellTable = new Color(232,220,254);
    
    private final Font fontBox = new Font("Roboto",0,20);
    
    Colors color = new Colors(); 
    
    private JTextField searcher ;
    public Admin(){
        color.loadColors();
        setLayout(new BorderLayout());
        placePanels();
        placeComponentsPanelTop();
        placePanelLeft();
        placeTableCenter();
        listenEvents();
        mDTabla("");
    }
    public void placePanels(){
        String dataPanels[] = {"TopPanel","CenterPanel","LeftPanel"};
        String dataPanelsTop[] = {"TitlePanel","SearcherPanel"};
        for(String name : dataPanels){
            panels.put(name, 
                new JPanel(new BorderLayout())
            );
            panels.get(name).setBackground( Colors.colors.get(name)  );
        }
        for(String nameNorth : dataPanelsTop){
            panelsTop.put(nameNorth, new JPanel( new FlowLayout() ));
            panelsTop.get(nameNorth).setBackground( Colors.colors.get(nameNorth)  );
        }
        
        this.add(panels.get("TopPanel"), BorderLayout.NORTH);
        this.add(panels.get("CenterPanel"), BorderLayout.CENTER);
        this.add(panels.get("LeftPanel"), BorderLayout.WEST);
        
        panels.get("TopPanel").add(panelsTop.get("TitlePanel"), BorderLayout.NORTH);
        panels.get("TopPanel").add(panelsTop.get("SearcherPanel"), BorderLayout.CENTER);
        
        leftPanel = new JPanel(new GridLayout(5,1,10,10));
        leftPanel.setBackground(Colors.colors.get("LeftColor"));
        panels.get("LeftPanel").add(leftPanel, BorderLayout.NORTH);
    }
    public void placeComponentsPanelTop(){
        JLabel title = new JLabel(titleTopWindow);
        title.setFont( new Font("Roboto",0,35) );
        
        JLabel askSearch = new JLabel("Buscar: ");
        searcher = new JTextField(30);
        askSearch.setFont( fontBox ); 
        searcher.setFont( fontBox ); 
        
        panelsTop.get("TitlePanel").add(title);
        panelsTop.get("SearcherPanel").add(askSearch);
        panelsTop.get("SearcherPanel").add(searcher);
        
    }
    public void placePanelLeft(){
        String nameButtons[] = {"Agregar","Modificar","Bloquear","Desbloquear","Eliminar"};
        for(String name : nameButtons){
            buttomsLeft.put(name, new JButton(name));
            buttomsLeft.get(name).setFont( fontBox );
            buttomsLeft.get(name).setBackground(Color.white); 
            leftPanel.add( buttomsLeft.get(name) );
        }
        
    }
    public void placeTableCenter(){
        
        tableEmployees = new JTable();
        Box cuadro = Box.createHorizontalBox();
        modelCenter = new DefaultTableCellRenderer();
        modelCenter.setHorizontalAlignment(SwingConstants.CENTER);
        dtm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        dtm.setColumnIdentifiers( new String[]{"Id","Nombre","Apellido paterno","Apellido materno",
            "Direccion","Telefono","Estado"} );
        tableEmployees.setModel( dtm );

        tableEmployees.setBackground(backgroundTable);
        tableEmployees.setSelectionBackground(colorTextSelectedTable);
        tableEmployees.setSelectionForeground(colorSelectedCellTable);
        tableEmployees.setFont(new Font("Roboto",0,20)); 
        tableEmployees.getTableHeader().setFont(new Font("Roboto",0,20));
        tableEmployees.setRowHeight(45); 
        
        for(int i=0 ; i<tableEmployees.getColumnCount() ; i++)
            tableEmployees.getColumnModel().getColumn(i).setCellRenderer(modelCenter); 
        
        tableEmployees.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
        tableEmployees.getColumnModel().getColumn(0).setMaxWidth(100);
        tableEmployees.getColumnModel().getColumn(1).setMaxWidth(300);
        tableEmployees.getColumnModel().getColumn(2).setMaxWidth(200);
        tableEmployees.getColumnModel().getColumn(3).setMaxWidth(200);
        tableEmployees.getColumnModel().getColumn(4).setMaxWidth(500);
        tableEmployees.getColumnModel().getColumn(5).setMaxWidth(300);
        tableEmployees.getColumnModel().getColumn(6).setMaxWidth(200);
        
        tableEmployees.getTableHeader().
            setBackground(colorTableTop); 
        
        cuadro.add(new JScrollPane( tableEmployees ));
        
        panels.get("CenterPanel").add(cuadro, BorderLayout.CENTER);
    }
    private void mDTabla(String valor){
        buttomsLeft.get("Modificar").setEnabled(false);
        buttomsLeft.get("Bloquear").setEnabled(false);
        buttomsLeft.get("Desbloquear").setEnabled(false);
        buttomsLeft.get("Eliminar").setEnabled(false);
        int total = tableEmployees.getRowCount();
        for(int i=total-1;i>=0;i--){
          dtm.removeRow(i);
        }
        Connect cc = new Connect();
        Connection cn = cc.getConnection();
        String sql = "SELECT * FROM employees WHERE "+
                "CONCAT(name_employee,paternalsurname_employee)"+
                " LIKE '%"+valor+"%' AND id_employee>1 ORDER BY id_employee DESC;";
        String [] registros = new String[7];
        try {
          Statement st = cn.createStatement();
          ResultSet rs = st.executeQuery(sql);
          while(rs.next()){
              for(int i=0 ; i<registros.length-1;i++)
                registros[i] = rs.getString(i+1);
              registros[6] = (rs.getString("isActive").equals("Y") ? "Desbloqueado" : "Bloqueado");
              dtm.addRow(registros);
          }
        } catch (SQLException ex) {
          System.err.println("error");
        }
        cc.disconnect();
    }
    public void listenEvents(){
        buttomsLeft.get("Agregar").addActionListener((e) -> {
            new RegisterUpdateEmployee(null);
            searcher.setText(""); 
            mDTabla(""); 
        });
        buttomsLeft.get("Modificar").addActionListener((e) -> {
            String id = tableEmployees.getValueAt(tableEmployees.getSelectedRow(), 0).toString();
            new RegisterUpdateEmployee(null, Long.parseLong(id)); 
            searcher.setText("");
            mDTabla(""); 
        });
        buttomsLeft.get("Bloquear").addActionListener((e) -> {
            String id = tableEmployees.getValueAt(tableEmployees.getSelectedRow(), 0).toString();
            OperationDatabase.lockUnlockEmpleado(Long.parseLong(id),true); 
            searcher.setText("");
            mDTabla("");
        });
        buttomsLeft.get("Desbloquear").addActionListener((e) -> {
            String id = tableEmployees.getValueAt(tableEmployees.getSelectedRow(), 0).toString();
            OperationDatabase.lockUnlockEmpleado(Long.parseLong(id),false); 
            searcher.setText("");
            mDTabla("");
        });
        buttomsLeft.get("Eliminar").addActionListener((e) -> {
            String id = tableEmployees.getValueAt(tableEmployees.getSelectedRow(), 0).toString();
            new DeleteEmployee(null, Long.parseLong(id));
            //OperationDatabase.deleteDataEmployee(Long.parseLong(id)); 
            searcher.setText("");
            mDTabla("");
        });
        searcher.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                mDTabla(searcher.getText()); 
            }
            
        });
        tableEmployees.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                isActiveEmployee();
                buttomsLeft.get("Eliminar").setEnabled(true);
            }
        });
        tableEmployees.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                isActiveEmployee();
                buttomsLeft.get("Eliminar").setEnabled(true);
            }
            
        });
    }
    public void isActiveEmployee(){
        String id = tableEmployees.getValueAt(tableEmployees.getSelectedRow(), 0).toString();
        buttomsLeft.get("Modificar").setEnabled(true); 
        if(OperationDatabase.isActiveEmployee(Long.parseLong(id))){
            buttomsLeft.get("Bloquear").setEnabled(true); 
            buttomsLeft.get("Desbloquear").setEnabled(false); 
        }else{
            buttomsLeft.get("Bloquear").setEnabled(false); 
            buttomsLeft.get("Desbloquear").setEnabled(true); 
        }
    }
}
