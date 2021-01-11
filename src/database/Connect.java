
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
    private static Connection conn;
    private final String driver="com.mysql.cj.jdbc.Driver";
    private final String user = "root";
    private final String password = "Mysql1234";  ///La contraseña de mysql 
    
    //puerto 3306, si tienes uno diferente cambialo
    //employees es el nombre de la base de datos
    private final String url = "jdbc:mysql://localhost:3306/employees?serverTimezone=UTC";   
    public Connect(){
        conn = null;
        try{
            Class.forName(driver);
            conn = (Connection) DriverManager.getConnection(url,user,password);
        }catch (ClassNotFoundException | SQLException e){}
    }
    public Connection getConnection(){
        return conn;
        
    }
    public void disconnect(){
        conn = null;
    }
}
