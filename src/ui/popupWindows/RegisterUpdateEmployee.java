
package ui.popupWindows;

import database.OperationDatabase;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;
import ui.Colors;
import validation.ValidationBox;

public class RegisterUpdateEmployee extends JDialog{
    private final Map<String, JPanel> panels = new HashMap<>();
    
    private JPanel panelLeft;
    private JPanel panelRight;
    
    private JTextField[] boxRegister = new JTextField[6];
    private JLabel[] askBox = new JLabel[7];
    private JPasswordField passwordUser ;

    private JFormattedTextField entryTime;
    private JFormattedTextField departureTime;
    
    private final Font fontBox = new Font("Roboto", 0, 20);
    private ActionListener eventRegister;
    
    boolean dayWork[] = {true, true, true, true, true, true, true};
    private JCheckBox[] isWork = new JCheckBox[7];
    
    private long idUser=0;
    private String oldUser = null;
    public RegisterUpdateEmployee(JFrame window){
        super(window,true);

        
        setSize(java.awt.Toolkit.getDefaultToolkit().getScreenSize().width-100,
                java.awt.Toolkit.getDefaultToolkit().getScreenSize().height-100);
        init();
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public RegisterUpdateEmployee(JFrame window,long idUser){
        super(window,true);

        this.idUser = idUser;
        setSize(java.awt.Toolkit.getDefaultToolkit().getScreenSize().width-100,
                java.awt.Toolkit.getDefaultToolkit().getScreenSize().height-100);
        init();
        fillWithCurrentData();
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public void fillWithCurrentData(){
        Map<String, String> data = new HashMap();
        
        data.putAll( OperationDatabase.getDataEmployee(idUser) );
        boxRegister[0].setText(data.get("name_employee")); 
        boxRegister[1].setText(data.get("paternalsurname_employee")); 
        boxRegister[2].setText(data.get("maternalsurname_employee")); 
        boxRegister[3].setText(data.get("direction")); 
        boxRegister[4].setText(data.get("telephone")); 
        data.clear();
        
        data.putAll(OperationDatabase.getDataLoginEmployee(idUser)); 
        boxRegister[5].setText(data.get("user_employee"));
        passwordUser.setText(data.get("password_employee")); 
        oldUser = data.get("user_employee");
        data.clear();
        
        data.putAll(OperationDatabase.getDataScheduleEmployee(idUser));
        isWork[0].setSelected((data.get("sunday").equals("Y"))); 
        dayWork[0] = (data.get("sunday").equals("Y"));
        isWork[1].setSelected((data.get("monday").equals("Y"))); 
        dayWork[1] = (data.get("sunday").equals("Y"));
        isWork[2].setSelected((data.get("tuesday").equals("Y"))); 
        dayWork[2] = (data.get("sunday").equals("Y"));
        isWork[3].setSelected((data.get("wednesday").equals("Y"))); 
        dayWork[3] = (data.get("sunday").equals("Y"));
        isWork[4].setSelected((data.get("thursday").equals("Y")));
        dayWork[4] = (data.get("sunday").equals("Y"));
        isWork[5].setSelected((data.get("friday").equals("Y"))); 
        dayWork[5] = (data.get("sunday").equals("Y"));
        isWork[6].setSelected((data.get("saturday").equals("Y"))); 
        dayWork[6] = (data.get("sunday").equals("Y"));
        entryTime.setText(data.get("entry_time"));
        departureTime.setText(data.get("departure_time"));
        data.clear();
        
    }
    public void init(){
        placePanels();
        listenEvent();
        placeBoxLeft();
        try {
            placeBoxRight();
        } catch (ParseException ex) {
            Logger.getLogger(RegisterUpdateEmployee.class.getName()).log(Level.SEVERE, null, ex);
        }
        placeButtonBottom();
        listenEventsCheckBox();
        addValidations();
    }
    
    public void placePanels(){
        
        String inputPanelsToPanels[] = {"TopPanel","CenterPanel","BottomPanel"};
        for(String panel : inputPanelsToPanels){
            panels.put(panel, 
                new JPanel(  
                    (panel.equals("CenterPanel"))
                        ? new BorderLayout()
                        : new FlowLayout()
                )
            );
            panels.get( panel ).setBackground( Colors.colors.get( panel ) ); 
        }
        panelLeft = new JPanel(new GridLayout(7,2,10,10));
        panelRight = new JPanel(new GridLayout(9,2,10,10));
        
        panelLeft.setBackground(Colors.colors.get("CenterPanel")); 
        panelRight.setBackground(Colors.colors.get("CenterPanel"));
        panelLeft.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED),
                    "Datos Personales", TitledBorder.CENTER,TitledBorder.DEFAULT_POSITION));
        panelRight.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED),
                    "Horario", TitledBorder.CENTER,TitledBorder.DEFAULT_POSITION));
        
        this.add(panels.get("TopPanel"), BorderLayout.NORTH);
        this.add(panels.get("CenterPanel"), BorderLayout.CENTER);
        this.add(panels.get("BottomPanel"), BorderLayout.SOUTH);
        
        panels.get("CenterPanel").add(panelRight, BorderLayout.EAST);
        panels.get("CenterPanel").add(panelLeft, BorderLayout.CENTER);
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
            String dataEntryTime = entryTime.getText();
            String datadDepartureTime = departureTime.getText();
            
            if(idUser != 0){
                if(isCorrectData()){
                    if(!OperationDatabase.searchThisUser(user) || user.equals(oldUser)){
                        OperationDatabase.updateEmployee(idUser, name, paternalSurname, maternalSurname, direction, telephone);
                        OperationDatabase.updateLogin(idUser, user, password);
                        OperationDatabase.updateSchedule(idUser, dayWork, dataEntryTime, datadDepartureTime); 
                        this.dispose();
                    }else{
                        JOptionPane.showMessageDialog(null,"Este usuario existe, digite otro nombre de usuario","ERROR",JOptionPane.PLAIN_MESSAGE);
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"Rellene correctamente los campos","ERROR",JOptionPane.ERROR_MESSAGE);
                }
            }else{
                if(isCorrectData()){
                    if(!OperationDatabase.searchThisUser(user)){
                        OperationDatabase.insertEmployee(name, paternalSurname, maternalSurname, direction, telephone,
                            true, user, password, dayWork, dataEntryTime, datadDepartureTime);
                        this.dispose();
                    }else{
                        JOptionPane.showMessageDialog(null,"Este usuario existe, digite otro nombre de usuario","ERROR",JOptionPane.PLAIN_MESSAGE);
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"Rellene correctamente los campos","ERROR",JOptionPane.ERROR_MESSAGE);
                }
            }
            
        };
    }
    public boolean isCorrectData(){
        boolean isCorrect = true;
        for (JTextField boxRegister1 : boxRegister) {
            if ((boxRegister1.getText().trim()).equals("")) {
                isCorrect = false;
                break;
            }
        }
        if((String.valueOf(passwordUser.getPassword()).trim()).equals("") || (entryTime.getText().trim()).equals("")
                || (departureTime.getText().trim()).equals("")){ 
            isCorrect = false;
        }
        return isCorrect;
    }
    public void placeBoxLeft(){
         passwordUser = new JPasswordField(10);
        passwordUser.setFont( fontBox );
        
        String askData[] = {"Nombre","Apellido paterno","Apellido materno","Direccion","Telefono","Usuario","Contraseña"};
        for(int i = 0 ; i<boxRegister.length ; i++){
            boxRegister[i] = new JTextField(10);
            askBox[i] = new JLabel(askData[i]);
            
            boxRegister[i].setFont( fontBox );
            askBox[i].setFont( fontBox ); 
            
            panelLeft.add(askBox[i]);
            panelLeft.add(boxRegister[i]);
        }
        askBox[6] = new JLabel(askData[6]);
        askBox[6].setFont( fontBox );
        
        panelLeft.add(askBox[6]);
        panelLeft.add(passwordUser);
    }
    public void placeBoxRight() throws ParseException{
        String days[] = {"Domingo","Lunes","Martes","Miercoles","Jueves","Viernes","Sabado"};
        for(int i=0 ; i<dayWork.length ; i++){
            JLabel askDay = new JLabel( days[i] );
            isWork[i] = new JCheckBox("",true);
            askDay.setFont( fontBox ); 
            
            isWork[i].setBackground(Colors.colors.get("CenterPanel"));
            
            panelRight.add(askDay);
            panelRight.add(isWork[i]);
            
        }
        JLabel labelEntry = new JLabel("Hora de entrada");
        JLabel labelDeparture = new JLabel("Hora de salida");
        
        MaskFormatter mask = new MaskFormatter("##:##:##");
        DateFormat format = new SimpleDateFormat("HH:mm:ss"); 
        entryTime = new JFormattedTextField(format);
        
        
        mask.install(entryTime); 
        departureTime =new JFormattedTextField(new SimpleDateFormat("HH:mm:ss"));
        mask.install(departureTime); 
        
        
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        entryTime.setValue(calendar.getTime());
        
        calendar.set(Calendar.SECOND, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.HOUR_OF_DAY, 16);
        departureTime.setValue(calendar.getTime());
        
        entryTime.setFont(fontBox);
        departureTime.setFont(fontBox);
        labelEntry.setFont(fontBox);
        labelDeparture.setFont(fontBox);
        
        panelRight.add(labelEntry);
        panelRight.add(entryTime);
        panelRight.add(labelDeparture);
        panelRight.add(departureTime);
        
    }
    public void placeButtonBottom(){
        JButton buttonRegister = new JButton((idUser == 0) ? "Registrar": "Actualizar");
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
    public void listenEventsCheckBox(){
        ActionListener eventsChec = (e) -> {
            for(int i=0 ; i<isWork.length ; i++){
                if(isWork[i] == e.getSource()){
                    dayWork[i] = (isWork[i].isSelected());
                    break;
                }
            }
        };
        for(JCheckBox checks: isWork){
            checks.addActionListener(eventsChec); 
        }
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
