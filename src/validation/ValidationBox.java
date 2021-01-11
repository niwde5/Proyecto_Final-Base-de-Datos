
package validation;

import java.awt.HeadlessException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class ValidationBox {
    public static KeyListener eventoCajasEnteroGrande;
    public static KeyListener eventoCajasEnteroChico;
    public static KeyListener eventoCajasFlotante;
    public static KeyListener eventoCajasString;
    
    public static void validarCajasString(JTextField caja,int tamanio){
        eventoCajasString = new KeyAdapter(){
            @Override
            public void keyReleased(KeyEvent ke) {
                if(caja.getText().length() >tamanio){
                    caja.setText(String.valueOf(caja.getText().subSequence(0, tamanio)));
                }
            }
        };
        caja.addKeyListener(eventoCajasString); 
    }
    public static void validarCajasEnteroGrande(JTextField caja,int tamanio){
        eventoCajasEnteroGrande = new KeyAdapter(){
            @Override
            public void keyReleased(KeyEvent ke) {
                if(!caja.getText().equals("")){
                    try{
                        if(Long.parseLong(caja.getText())<=0){
                            JOptionPane.showMessageDialog(null,"Dato no valido 2","ERROR",JOptionPane.ERROR_MESSAGE);
                            caja.setText("");
                        }    
                        if(caja.getText().length() >tamanio)
                            caja.setText(String.valueOf(caja.getText().subSequence(0, tamanio)));
                    }catch(HeadlessException | NumberFormatException e){
                        JOptionPane.showMessageDialog(null,"Dato no valido","ERROR",JOptionPane.ERROR_MESSAGE);
                        caja.setText("");
                    }
                }
            }
        };
        caja.addKeyListener(eventoCajasEnteroGrande);  
    } 
}
