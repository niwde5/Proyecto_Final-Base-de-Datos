
package ui.popupWindows;

import database.OperationDatabase;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import ui.Colors;
import validation.ValidationBox;

public class Register extends JDialog{
    private final String titleTopPanel = "Registrar";
    
    private Map<String, JPanel> panels = new HashMap();
    
    private JTextField[] boxRegister = new JTextField[6];
    private JLabel[] askBox = new JLabel[7];
    private JPasswordField passwordUser ;
    
    private ActionListener eventRegister;
    
    private final Font fontBox = new Font("Roboto", 0, 20);
    
    public Register(JFrame window){
        super(window,true);

        
        setSize(400,
                java.awt.Toolkit.getDefaultToolkit().getScreenSize().height-200);
        init();
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public void init(){
        placePanels();
        listenEvent();
        placeTitle();
        placeBox();
        placeButtonBottom();
        addValidations();
    }
    public void placePanels(){
        this.setLayout( new BorderLayout() );
        
        String namePanels[] = {"TopPanel","CenterPanel","BottomPanel"};
        for(String name : namePanels){
            panels.put(name, 
                new JPanel( 
                    (name.equals("CenterPanel")) 
                        ? new GridLayout(7,2,10,10) 
                        : new FlowLayout() 
                )
            );
            panels.get(name).setBackground( Colors.colors.get( name ) );
        }
        
        this.add(panels.get("TopPanel"), BorderLayout.NORTH);
        this.add(panels.get("CenterPanel"), BorderLayout.CENTER);
        this.add(panels.get("BottomPanel"), BorderLayout.SOUTH);
    }
    public void listenEvent(){
        eventRegister = (e) -> {
            String name = boxRegister[0].getText();
            String paternalSurname = boxRegister[1].getText();
            String maternalSurname = boxRegister[2].getText();
            String direction = boxRegister[3].getText();
            String telephone = boxRegister[4].getText();
            String user = boxRegister[5].getText();
            String password = String.valueOf(passwordUser.getPassword()); 
            if((name.trim()).equals("") || (paternalSurname.trim()).equals("") || (maternalSurname.trim()).equals("") ||
                (direction.trim()).equals("") || (telephone.trim()).equals("") || (user.trim()).equals("") ||
                    (password.trim()).equals("")){
                JOptionPane.showMessageDialog(null,"Rellene correctamente los campos","ERROR",JOptionPane.ERROR_MESSAGE);
            }else{
                OperationDatabase.insertAdmin(name, paternalSurname, maternalSurname, direction, telephone, user, password); 
                this.dispose();
            }
        };
    }
    public void placeTitle(){
        JLabel title = new JLabel(titleTopPanel);
        title.setFont( new Font("Roboto", 0, 40 ) );
        panels.get("TopPanel").add(title);
    }
    public void placeBox(){
        passwordUser = new JPasswordField(10);
        passwordUser.setFont( fontBox );
        
        String askData[] = {"Nombre","Apellido paterno","Apellido materno","Direccion","Telefono","Usuario","Contraseña"};
        for(int i = 0 ; i<boxRegister.length ; i++){
            boxRegister[i] = new JTextField(10);
            askBox[i] = new JLabel(askData[i]);
            
            boxRegister[i].setFont( fontBox );
            askBox[i].setFont( fontBox ); 
            
            panels.get("CenterPanel").add(askBox[i]);
            panels.get("CenterPanel").add(boxRegister[i]);
        }
        askBox[6] = new JLabel(askData[6]);
        askBox[6].setFont( fontBox );
        
        panels.get("CenterPanel").add(askBox[6]);
        panels.get("CenterPanel").add(passwordUser);
         
    }
    public void placeButtonBottom(){
        JButton buttonRegister = new JButton("Registrar");
        JButton cancelOperation = new JButton("Cancelar");
        
        buttonRegister.setBackground(Color.white);
        cancelOperation.setBackground(Color.white);  
        
        buttonRegister.setFont( new Font("Roboto", 0, 20) );
        cancelOperation.setFont( new Font("Roboto", 0, 20) );
        
        cancelOperation.addActionListener((ActionEvent e) -> {
            this.dispose();
        }); 
        buttonRegister.addActionListener(eventRegister); 
        
        panels.get("BottomPanel").add(buttonRegister);
        panels.get("BottomPanel").add(cancelOperation);
    }
    public void addValidations(){
        ValidationBox.validarCajasString(boxRegister[0], 30);
        ValidationBox.validarCajasString(boxRegister[1], 30);
        ValidationBox.validarCajasString(boxRegister[2], 30);
        ValidationBox.validarCajasString(boxRegister[3], 100);
        ValidationBox.validarCajasEnteroGrande(boxRegister[4], 10);
        ValidationBox.validarCajasString(boxRegister[5], 30);
        ValidationBox.validarCajasString(passwordUser, 30);
    }
}
