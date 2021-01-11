
package ui.mainWindows;

import database.OperationDatabase;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import ui.Colors;

public class Employee extends JPanel{
   
    private JLabel titleWindow; 
    private Font font = new Font("Roboto", 0, 20);
    
    private JPanel panelLeft;
    private JPanel panelRight;
    
    private Map<String, JPanel> panels = new HashMap();
    private Colors colors = new Colors();
    
    private long idUser;
    
    private JLabel dataPersonalUser[] = new JLabel[7];
    private JLabel dataPersonalUserToFill[] = new JLabel[7];
    
    
    private Map<String, String> dataUser;
    
    JCheckBox[] isWork = new JCheckBox[7];
    private JLabel dateEntry;
    private JLabel dateDeparture;
    
    String statusEmployee;
    public Employee(){
        colors.loadColors();
        init();
    }
    public void setId(long idUser){
        this.idUser = idUser;
        
        statusEmployee = null;
        
        Map<String, String> data = new HashMap();
        
        data.putAll( OperationDatabase.getDataEmployee(idUser) );
        titleWindow.setText("Bienvenido "+data.get("name_employee"));
        dataPersonalUserToFill[0].setText(data.get("name_employee")); 
        dataPersonalUserToFill[1].setText(data.get("paternalsurname_employee")); 
        dataPersonalUserToFill[2].setText(data.get("maternalsurname_employee")); 
        dataPersonalUserToFill[3].setText(data.get("direction")); 
        dataPersonalUserToFill[4].setText(data.get("telephone")); 
        statusEmployee = data.get("isActive");
        data.clear();
        
        data.putAll(OperationDatabase.getDataLoginEmployee(idUser)); 
        dataPersonalUserToFill[5].setText(data.get("user_employee"));
        dataPersonalUserToFill[6].setText(data.get("password_employee")); 
        data.clear();
        
        data.putAll(OperationDatabase.getDataScheduleEmployee(idUser));
        isWork[0].setSelected((data.get("sunday").equals("Y"))); 
        isWork[1].setSelected((data.get("monday").equals("Y"))); 
        isWork[2].setSelected((data.get("tuesday").equals("Y"))); 
        isWork[3].setSelected((data.get("wednesday").equals("Y"))); 
        isWork[4].setSelected((data.get("thursday").equals("Y")));
        isWork[5].setSelected((data.get("friday").equals("Y"))); 
        isWork[6].setSelected((data.get("saturday").equals("Y"))); 
        dateEntry.setText(data.get("entry_time"));
        dateDeparture.setText(data.get("departure_time"));
        data.clear();
        
        if(statusEmployee.equals("N")){
            setStateDisable();
        }
    }
    public void setStateDisable(){
        this.removeAll();
        this.setBackground(Colors.colors.get("TopPanel")) ;
        
        JLabel textDisable = new JLabel("Su cuenta ha sido bloqueada ;(");
        textDisable.setFont( new Font("Roboto",0,50) );
        
        this.setLayout(new FlowLayout()); 
        this.add(textDisable);
        this.validate();
        this.repaint();
        
    }
    public void init(){
        this.removeAll();
        this.setLayout( new BorderLayout() );
        
        placePanels();
        placeTitle();
        placeBoxLeft();
        placeBoxRight();
        
        this.validate();
        this.repaint();
    }
    public void placePanels(){
        String dataPanels[] = {"TopPanel","CenterPanel","BottomPanel"};
        for(String name : dataPanels){
            panels.put(name, 
                new JPanel( (name.equals("CenterPanel") 
                    ? new BorderLayout()
                    : new FlowLayout()   )
                )
            );
            panels.get(name).setBackground( Colors.colors.get(name)  );
        }
        
        this.add(panels.get("TopPanel"), BorderLayout.NORTH);
        this.add(panels.get("CenterPanel"), BorderLayout.CENTER);
        this.add(panels.get("BottomPanel"), BorderLayout.SOUTH);
        
        panelLeft = new JPanel(new GridLayout(7,2,10,10));
        panelRight = new JPanel(new GridLayout(9,2,10,10));
        
        panelLeft.setBackground(Colors.colors.get("CenterPanel")); 
        panelRight.setBackground(Colors.colors.get("CenterPanel"));
        panelLeft.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED),
                    "Datos Personales", TitledBorder.CENTER,TitledBorder.DEFAULT_POSITION));
        panelRight.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED),
                    "Horario", TitledBorder.CENTER,TitledBorder.DEFAULT_POSITION));
       
        
        panels.get("CenterPanel").add(panelRight, BorderLayout.EAST);
        panels.get("CenterPanel").add(panelLeft, BorderLayout.CENTER);
    }
    public void placeTitle(){
        titleWindow = new JLabel();
        titleWindow.setFont( new Font("Roboto",0,40) );
        
        panels.get("TopPanel").add(titleWindow);
    }
    public void placeBoxLeft(){
        
        String askData[] = {"Nombre","Apellido paterno","Apellido materno","Direccion","Telefono","Usuario","Contraseña"};
        
        for(int i=0 ; i<askData.length ; i++){
            dataPersonalUser[i] = new JLabel(askData[i]);
            dataPersonalUserToFill[i] = new JLabel();
            
            dataPersonalUser[i].setFont( font ); 
            dataPersonalUserToFill[i].setFont( font ); 
            
            panelLeft.add(dataPersonalUser[i]);
            panelLeft.add(dataPersonalUserToFill[i]);
        }

    }
    public void placeBoxRight(){
        String days[] = {"Domingo","Lunes","Martes","Miercoles","Jueves","Viernes","Sabado"};
        for(int i=0 ; i<days.length ; i++){
            JLabel askDay = new JLabel( days[i] );
            isWork[i] = new JCheckBox("",true);
            askDay.setFont( font ); 
            
            isWork[i].setBackground(Colors.colors.get("CenterPanel"));
            isWork[i].setEnabled(false);
            
            panelRight.add(askDay);
            panelRight.add(isWork[i]);
            
        }
        JLabel labelEntry = new JLabel("Hora de entrada");
        JLabel labelDeparture = new JLabel("Hora de salida");
        dateEntry = new JLabel();
        dateDeparture = new JLabel();
        
        dateEntry.setFont(font);
        dateDeparture.setFont(font);
        labelEntry.setFont(font);
        labelDeparture.setFont(font);
        
        panelRight.add(labelEntry);
        panelRight.add(dateEntry);
        panelRight.add(labelDeparture);
        panelRight.add(dateDeparture);
    }
}
