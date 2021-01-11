
package ui.popupWindows;

import database.OperationDatabase;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import ui.Colors;

public class DeleteEmployee extends JDialog{
    private long idUser;
    
    private Colors color = new Colors();
    private Font font = new Font("Roboto",0,20);
    
    private Map<String, JPanel> panels = new HashMap();
    public DeleteEmployee(JFrame window, long idUser){
        super(window,true);
        this.idUser = idUser;
        
        setSize(400,130);
        init();
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public void init(){
        color.loadColors();
        placePanels();
        placeMessage();
        placeButtons();
    }
    public void placePanels(){
        String namePanels[] = {"TopPanel","CenterPanel"};
        for(String name : namePanels){
            panels.put(name, 
                new JPanel(new FlowLayout() )
            );
            panels.get(name).setBackground( Colors.colors.get( name ) );
        }
        panels.get("CenterPanel").setBackground(Colors.colors.get("TopPanel")); 
        
        this.getContentPane().setLayout( new BorderLayout() ); 
        this.getContentPane().setBackground(Colors.colors.get( "TopPanel" ));
        this.getContentPane().add( panels.get("TopPanel"), BorderLayout.NORTH ); 
        this.getContentPane().add( panels.get("CenterPanel"), BorderLayout.SOUTH ); 
    }
    public void placeMessage(){
        JLabel text = new JLabel("¿Esta seguro de eliminar este empleado?");
        text.setFont(font);
        
        panels.get("TopPanel").add(text);
    }
    public void placeButtons(){
        JButton yes = new JButton("Si");
        JButton no = new JButton("No");
        
        yes.setFont(font);
        no.setFont(font); 
        yes.setBackground(Color.white);
        no.setBackground(Color.white);
        
        no.addActionListener((e) -> {
            this.dispose();
        });
        yes.addActionListener((e) -> {
            OperationDatabase.deleteDataEmployee(idUser); 
            this.dispose();
        }); 
        
        panels.get("CenterPanel").add(yes);
        panels.get("CenterPanel").add(no);
    }
}
