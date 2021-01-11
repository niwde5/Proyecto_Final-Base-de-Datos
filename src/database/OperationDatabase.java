
package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JOptionPane;


public class OperationDatabase {
    public static boolean searchUsers(JButton buttonLogin){
        boolean isEmpty = false;
        Connect conectar = new Connect();
        Connection conexion = conectar.getConnection();
        try{
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM loginEmployee");
            if(!rs.next()){
                buttonLogin.setText("Registrar Admin");
                isEmpty = true;
            }else
                buttonLogin.setText("Iniciar Sesion"); 
        }catch(SQLException exepcionSQL){
            JOptionPane.showMessageDialog(null,"ERROR AL VERIFICAR BD LOGIN","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        conectar.disconnect();
        return isEmpty;
    }
    public static boolean searchThisUser(String user){
        boolean existThisUser = false;
        Connect conectar = new Connect();
        Connection conexion = conectar.getConnection();
        try{
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM loginEmployee WHERE user_employee='"+user+"' ;");
            if(rs.next()){
                existThisUser = true;
            }
        }catch(SQLException exepcionSQL){
            JOptionPane.showMessageDialog(null,"ERROR AL VERIFICAR BD LOGIN","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        conectar.disconnect();
        return existThisUser;
    }
    public static void insertAdmin(String name, String paternarSurname, String maternarSurname, String direction, 
            String telephone, String user, String password){
        Connect conection = new Connect();
        Connection cn = conection.getConnection();
        try {
          PreparedStatement pps = 
            cn.prepareStatement("INSERT INTO employees(id_employee,name_employee,paternalsurname_employee"+
                  ",maternalsurname_employee,direction,telephone,isActive) VALUES("+
                  null+",'"+name+"','"+paternarSurname+"','"+maternarSurname+"','"+direction+"','"+telephone+"','Y');");
          pps.executeUpdate();   
        } catch (SQLException ex) {
          JOptionPane.showMessageDialog(null,"ERROR AL REGISTRAR","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        conection.disconnect();
        
        queryLastId(user,password);
    }
    public static void queryLastId( String user, String password){
       Connect conection = new Connect();
       Connection cn = conection.getConnection();
        
       try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM employees ORDER BY id_employee DESC LIMIT 2;");
            if(rs.next()){
                PreparedStatement pps = cn.prepareStatement("INSERT INTO loginEmployee(id_employee,"+
                "user_employee,password_employee,type_User) VALUES("+rs.getLong("id_employee")+",'"+user+"','"
                    +password+"','"+('A')+"');");
                pps.executeUpdate(); 
                
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"ERROR AL AGREGAR EMPLEADO","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        conection.disconnect();
    }
    public static void insertEmployee(String name, String paternarSurname, String maternarSurname, String direction, 
            String telephone, boolean isActive,String user, String password, boolean []daysWork, 
            String dateEntry, String dateDeparture){
        Connect conection = new Connect();
        Connection cn = conection.getConnection();
        try {
          PreparedStatement pps = 
            cn.prepareStatement("INSERT INTO employees(id_employee,name_employee,paternalsurname_employee"+
                  ",maternalsurname_employee,direction,telephone,isActive) VALUES("+
                  null+",'"+name+"','"+paternarSurname+"','"+maternarSurname+"','"+direction+"','"+telephone+"','"+
                  ((isActive) ? 'Y' : 'N')+"');");
          pps.executeUpdate();   
        } catch (SQLException ex) {
          JOptionPane.showMessageDialog(null,"ERROR AL REGISTRAR","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        conection.disconnect();
        
        queryLastId(user, password,daysWork, dateEntry, dateDeparture);
    }
    public static void queryLastId(String user,String password,boolean []daysWork, String dateEntry, String dateDeparture){
        Connect conection = new Connect();
        Connection cn = conection.getConnection();
        
       try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM employees ORDER BY id_employee DESC LIMIT 2;");
            if(rs.next()){
                
                registerUserLogin(cn, rs.getLong("id_employee"), user, password);
                registerSchedule(cn, rs.getLong("id_employee"), daysWork,dateEntry,dateDeparture);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"ERROR AL AGREGAR EMPLEADO","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        conection.disconnect();
    }
    public static void registerUserLogin(Connection cn,long idUser, String user, String password) throws SQLException{
        PreparedStatement pps = cn.prepareStatement("INSERT INTO loginEmployee(id_employee,"+
                "user_employee,password_employee,type_User) VALUES("+idUser+",'"+user+"','"+password+"','"+('E')+"');");
            pps.executeUpdate(); 
    }
    public static void registerSchedule(Connection cn, long idUser, boolean[] daysWork, String entryTime,
            String departureTime) throws SQLException{
        PreparedStatement pps = cn.prepareStatement("INSERT INTO schedule(id_employee,sunday,monday,"+
             "tuesday,wednesday,thursday,friday,saturday,entry_time,departure_time) VALUES("+idUser+","+
              "'"+((daysWork[0]) ? "Y" : "N")+"','"+((daysWork[1]) ? "Y" : "N")+"','"+((daysWork[2]) ? "Y" : "N")+"'"+
              ",'"+((daysWork[3]) ? "Y" : "N")+"','"+((daysWork[4]) ? "Y" : "N")+"','"+((daysWork[5]) ? "Y" : "N")+"'"+
              ",'"+((daysWork[6]) ? "Y" : "N")+"','"+(entryTime)+"','"+(departureTime)+"');");
            pps.executeUpdate(); 
    }
    public static void updateEmployee(Long id,String name, String paternalSurname, String maternalSurname, String direction,
            String telephone){
        Connect ven = new Connect();
        Connection cn = ven.getConnection();
        try {
            PreparedStatement pps = cn.prepareStatement("UPDATE employees SET name_employee='"+name+"',"+
                "paternalsurname_employee='"+paternalSurname+"',maternalsurname_employee='"+maternalSurname+"',"+
                "direction='"+direction+"',telephone='"+telephone+"' WHERE id_employee="+id+";");
            pps.executeUpdate();   
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"ERROR AL ACTUALIZAR EMPLEADO","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        ven.disconnect();
    }
    public static void updateLogin(Long id,String user, String password){
        Connect ven = new Connect();
        Connection cn = ven.getConnection();
        try {
            PreparedStatement pps = cn.prepareStatement("UPDATE loginEmployee SET user_employee='"+user+"',"+
                "password_employee='"+password+"' WHERE id_employee="+id+";");
            pps.executeUpdate();   
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"ERROR AL ACTUALIZAR LOGIN","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        ven.disconnect();
    }
    public static void updateSchedule(Long id,boolean[] daysWork,String entryTime, String departureTime){
        Connect ven = new Connect();
        Connection cn = ven.getConnection();
        try {
            PreparedStatement pps = cn.prepareStatement("UPDATE schedule SET sunday='"+((daysWork[0]) ? "Y" : "N")+"',monday='"+((daysWork[1]) ? "Y" : "N")+
              "',tuesday='"+((daysWork[2]) ? "Y" : "N")+"',wednesday='"+((daysWork[3]) ? "Y" : "N")
              +"',thursday='"+((daysWork[4]) ? "Y" : "N")+"',friday='"+
              ((daysWork[5]) ? "Y" : "N")+"',saturday='"+((daysWork[6]) ? "Y" : "N")+"',entry_time='"+entryTime+
              "',departure_time='"+departureTime+"' WHERE id_employee="+id+";");
            pps.executeUpdate();   
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"ERROR AL ACTUALIZAR HORARIO","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        ven.disconnect();
    }
    public static boolean isActiveEmployee(long id){
        boolean isActive = true;
        
        Connect conection = new Connect();
        Connection cn = conection.getConnection();
        
       try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM employees WHERE id_employee="+id+";");
            if(rs.next()){
                isActive = (rs.getString("isActive").equals("Y"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"ERROR AL AGREGAR EMPLEADO","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        conection.disconnect();
        
        return isActive;
    }
    public static Map<String, String> getDataEmployee(long id){
        Map<String, String> dataEmployee = new HashMap();
    
        Connect conection = new Connect();
        Connection cn = conection.getConnection();
        
       try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM employees WHERE id_employee="+id+";");
            if(rs.next()){
                dataEmployee.put("id_employee", rs.getString("id_employee"));
                dataEmployee.put("name_employee", rs.getString("name_employee"));
                dataEmployee.put("paternalsurname_employee", rs.getString("paternalsurname_employee"));
                dataEmployee.put("maternalsurname_employee", rs.getString("maternalsurname_employee"));
                dataEmployee.put("direction", rs.getString("direction"));
                dataEmployee.put("telephone", rs.getString("telephone"));
                dataEmployee.put("isActive", rs.getString("isActive"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"ERROR AL CONSULTAR EMPLEADO","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        conection.disconnect();
        
        return dataEmployee;
    }
    public static Map<String, String> getDataLoginEmployee(long id){
        Map<String, String> dataEmployee = new HashMap();
    
        Connect conection = new Connect();
        Connection cn = conection.getConnection();
        
       try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM loginEmployee WHERE id_employee="+id+";");
            if(rs.next()){
                dataEmployee.put("id_employee", rs.getString("id_employee"));
                dataEmployee.put("user_employee", rs.getString("user_employee"));
                dataEmployee.put("password_employee", rs.getString("password_employee"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"ERROR AL CONSULTAR EMPLEADO","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        conection.disconnect();
        
        return dataEmployee;
    }
    public static Map<String, String> getDataScheduleEmployee(long id){
        Map<String, String> dataEmployee = new HashMap();
    
        Connect conection = new Connect();
        Connection cn = conection.getConnection();
        
       try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM schedule WHERE id_employee="+id+";");
            if(rs.next()){
                dataEmployee.put("id_employee", rs.getString("id_employee"));
                dataEmployee.put("sunday", rs.getString("sunday"));
                dataEmployee.put("monday", rs.getString("monday"));
                dataEmployee.put("tuesday", rs.getString("tuesday"));
                dataEmployee.put("wednesday", rs.getString("wednesday"));
                dataEmployee.put("thursday", rs.getString("thursday"));
                dataEmployee.put("friday", rs.getString("friday"));
                dataEmployee.put("saturday", rs.getString("saturday"));
                dataEmployee.put("entry_time", rs.getString("entry_time"));
                dataEmployee.put("departure_time", rs.getString("departure_time"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"ERROR AL CONSULTAR EMPLEADO","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        conection.disconnect();
        
        return dataEmployee;
    }
    public static void lockUnlockEmpleado(Long id, boolean lock){
        Connect ven = new Connect();
        Connection cn = ven.getConnection();
        try {
            PreparedStatement pps = cn.prepareStatement("UPDATE employees SET isActive='"+(lock ? 'N' : "Y")+"' WHERE id_employee="+id+";");
            pps.executeUpdate();   
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"ERROR AL BLOQUEAR EMPLEADO","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        ven.disconnect();
    }
    public static void deleteDataEmployee(long idUser){
        Connect conection = new Connect();
        Connection cn = conection.getConnection();
        try {
            deleteSchedule(cn, idUser);
            deleteLoginEmployee(cn, idUser);
            deleteEmployee(cn, idUser);
        } catch (SQLException ex) {
          JOptionPane.showMessageDialog(null,"ERROR RESTABLECER TEMPORAL","ERROR",JOptionPane.ERROR_MESSAGE);
        }
        conection.disconnect();
    }
    public static void deleteSchedule(Connection cn, long idUser) throws SQLException{
        PreparedStatement pps = cn.prepareStatement("DELETE FROM schedule WHERE id_employee="+idUser+";");
        pps.executeUpdate(); 
    }
    public static void deleteLoginEmployee(Connection cn, long idUser) throws SQLException{
        PreparedStatement pps = cn.prepareStatement("DELETE FROM loginEmployee WHERE id_employee="+idUser+";");
        pps.executeUpdate(); 
    }
    public static void deleteEmployee(Connection cn, long idUser) throws SQLException{
        PreparedStatement pps = cn.prepareStatement("DELETE FROM employees WHERE id_employee="+idUser+";");
        pps.executeUpdate(); 
    }
}
