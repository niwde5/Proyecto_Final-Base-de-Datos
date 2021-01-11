
package ui;

import database.Connect;
import database.OperationDatabase;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import ui.mainWindows.Admin;
import ui.mainWindows.Employee;
import ui.popupWindows.Register;

public class MainWindow extends JFrame{
    
    final String titleTopPanel = "Control De Empleados";
    private final Font fontBoxText = new Font("Roboto",0,20);

    Colors colors = new Colors();
    private boolean isEmptyDatabase = false;
    
    private JPanel principalPanel;
    private JPanel mainControlPanel;
    private final Map<String, JPanel> panels = new HashMap<>();
    
    private JTextField boxUser;
    private JPasswordField boxPassword;
    
    private ActionListener eventLogin;
    
    private Admin admin = new Admin();
    private Employee employee = new Employee();
    
    private JButton botonCloseSession;
    private JButton bottomCheck;
    
    public MainWindow(){
        setSize(java.awt.Toolkit.getDefaultToolkit().getScreenSize().width+50,
                java.awt.Toolkit.getDefaultToolkit().getScreenSize().height-50);
        setTitle("Control de empleados");
        
        setExtendedState(MAXIMIZED_BOTH);
        init();
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        setVisible(true);
    }
    public void init(){
        initState();
        listenEvents();
        placePanels();
        placeTitle();
        placeBoxCenter();
        placeBoxBottom();
        createButtonCloseSession();
        
    }
    public void initState(){
        colors.loadColors();
    }
    public void placePanels(){
        principalPanel = new JPanel( new BorderLayout() );
        
        mainControlPanel = new JPanel( new BorderLayout() );

        String inputPanelsToPanels[] = {"TopPanel","CenterPanel","BottomPanel"};
        for(String panel : inputPanelsToPanels){
            panels.put(panel, 
                new JPanel(  
                    panel.equals("CenterPanel") 
                        ? new GridBagLayout() 
                        : new FlowLayout()  
                )
            );
            panels.get( panel ).setBackground( Colors.colors.get( panel ) ); 
        }
        
        this.getContentPane().add( principalPanel );
        
        principalPanel.add( mainControlPanel, BorderLayout.CENTER );
        principalPanel.add(panels.get( "BottomPanel" ), BorderLayout.SOUTH);

        
        mainControlPanel.add( panels.get( "TopPanel" ), BorderLayout.NORTH );
        mainControlPanel.add( panels.get( "CenterPanel" ), BorderLayout.CENTER );
        
        
    }
    public void placeTitle(){
        JLabel title = new JLabel( titleTopPanel );
        title.setFont(new Font("Roboto",0,40));
        panels.get("TopPanel").add(title);
    }
    public void placeBoxCenter(){
        JLabel askUser = new JLabel("Usuario: ");
        JLabel askPassword = new JLabel("Contraseña: ");
        
        bottomCheck = new JButton("Iniciar Sesion");
        bottomCheck.setBackground(Color.WHITE);
        
        boxUser = new JTextField( 15 );
        boxPassword = new JPasswordField( 15 );
        
        boxUser.setFont( fontBoxText );
        boxPassword.setFont( fontBoxText );
        askUser.setFont( fontBoxText );
        askPassword.setFont( fontBoxText );
        bottomCheck.setFont( fontBoxText );
        
        GridBagConstraints constraints = new GridBagConstraints();
        Insets i = new Insets(2, 2, 10, 2);
        constraints.gridx = 0;
        constraints.gridy = 0; 
        constraints.insets = i;
        
        panels.get("CenterPanel").add(askUser, constraints);
        constraints.gridx = 2; 
        constraints.gridy = 0; 
      
        panels.get("CenterPanel").add(boxUser, constraints);
        constraints.gridx = 0; 
        constraints.gridy = 2; 

        panels.get("CenterPanel").add(askPassword, constraints);
        constraints.gridx = 2;
        constraints.gridy = 2; 

        panels.get("CenterPanel").add(boxPassword, constraints);
        constraints.gridx = 0; 
        constraints.gridy = 4;
        constraints.gridwidth = 4; 

        panels.get("CenterPanel").add(bottomCheck, constraints);
        bottomCheck.addActionListener(eventLogin); 
        
        if(OperationDatabase.searchUsers(bottomCheck)){
            boxUser.setEnabled(false);
            boxPassword.setEnabled(false);
            isEmptyDatabase = true;
        }
    }
    public void placeBoxBottom(){
        JLabel textButton = new JLabel("Control y busqueda de empleados, todo en este lugar");
        textButton.setFont(fontBoxText);
        
        panels.get("BottomPanel").add(textButton);
    }
    public void listenEvents(){
        eventLogin = (e) -> {
            if(isEmptyDatabase){
                new Register(null);
                boxUser.setEnabled(true);
                boxPassword.setEnabled(true);
                isEmptyDatabase = false;
                if(OperationDatabase.searchUsers(bottomCheck)){
                    boxUser.setEnabled(false);
                    boxPassword.setEnabled(false);
                    isEmptyDatabase = true;
                }
            }else{
                loginProcess(boxUser.getText(), String.valueOf(boxPassword.getPassword())); 
            }
        };
    }
    public void loginProcess(String user, String password){
        Connect conection = new Connect();
       Connection cn = conection.getConnection();
        
       try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM loginEmployee ;");
            while(rs.next()){
                if(rs.getString("user_employee").equals(user) && rs.getString("password_employee").equals(password)){
                    if(rs.getString("type_User").equals("A")){
                        changeView(admin);
                        changeFooter(true);
                    }else{
                        employee.init();
                        employee.setId(rs.getLong("id_employee")); 
                        changeView(employee);
                        changeFooter(true);
                    }
                } 
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"ERROR AL CONSULTAR EMPLEADO","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        conection.disconnect();
    }
    public void createButtonCloseSession(){
        botonCloseSession = new JButton("Cerrar Sesion");
        
        botonCloseSession.setFont( new Font("Roboto",0,25) );
        botonCloseSession.setBackground(Color.white); 
        
        botonCloseSession.addActionListener((e) -> {
            changeView(mainControlPanel);
            changeFooter(false); 
            boxUser.setText("");
            boxPassword.setText(""); 
        }); 
    }
    public void changeFooter(boolean isSessionActive){
        if(isSessionActive){
            panels.get("BottomPanel").removeAll();
            panels.get("BottomPanel").add(botonCloseSession);
            principalPanel.validate();
            principalPanel.repaint();
        }else{
            panels.get("BottomPanel").removeAll();
            placeBoxBottom();
            principalPanel.validate();
            principalPanel.repaint();
        }
    }
    public void changeView(JPanel newWindow){
        principalPanel.removeAll();
        principalPanel.add(newWindow, BorderLayout.CENTER);        
        principalPanel.add(panels.get("BottomPanel"), BorderLayout.SOUTH);     
        principalPanel.validate();
        principalPanel.repaint();
    }
}
