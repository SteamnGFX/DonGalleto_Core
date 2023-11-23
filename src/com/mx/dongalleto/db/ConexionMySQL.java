package com.mx.dongalleto.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionMySQL {

    Connection conn;

    public Connection open() {
        //Usuario de base de datos
        String user = "root";
        //Contraseña de base de datos
        String password = "12345678"; //CAMBIAR CONTRASEÑA Y NO HACER PUSH
        //URL de BaseDeDatos
        String url = "jdbc:mysql://127.0.0.1:3306/DonGalleto?allowPublicKeyRetrieval=true&useSSL=false&useUnicode=true&characterEncoding=utf-8";
        //String url = "jdbc:mysql://10.100.48.222:3306/almacen?allowPublicKeyRetrieval=true&useSSL=false&useUnicode=true&characterEncoding=utf-8";
        //https://node133976-almacen.jelastic.saveincloud.net/

        //Realizamos un TRY para la conexión de BD
        try {
            //Registramos el manejador del controlador
            Class.forName("com.mysql.cj.jdbc.Driver");
            //conexión del driver, url, usuario y contraseña
            conn = DriverManager.getConnection(url, user, password);
            //devolvemos la conexión
            return conn;
        } //Realizamos una excepción
        catch (Exception e) {
            //Devolvemos la exepción
            throw new RuntimeException(e);
        }
    }

    public void close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Exception controlada.");
            }
        }

    }
}
