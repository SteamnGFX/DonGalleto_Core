package com.mx.dongalleto.core;

import com.mx.dongalleto.db.ConexionMySQL;
import com.mx.dongalleto.modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author angelmartinez
 */
public class ControladorUsuario {
    //Este método insertará un empleado
    //Recibe un objeto de tipo Empleado

    public List<Usuario> getAll(String filtro) throws Exception {
        //La consulta SQL a ejecutar:
        String sql = "SELECT * Usuario";

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto ejecutaremos la consulta:
        PreparedStatement pstmt = conn.prepareStatement(sql);

        //Aquí guardaremos los resultados de la consulta:
        ResultSet rs = pstmt.executeQuery();

        List<Usuario> user = new ArrayList<>();

        while (rs.next()) {
            user.add(fill(rs));
        }

        rs.close();
        pstmt.close();
        connMySQL.close();

        return user;
    }

    private Usuario fill(ResultSet rs) throws Exception {
        Usuario user = new Usuario();

        if (rs.getInt("id") == -10) {
            
            return null;
        } else {

            user.setIdUsuario(rs.getInt("id"));
            user.setNombre_usuario(rs.getString("nombre_usuario"));
            user.setContrasenia(rs.getString("contrasenia"));

            return user;
        }
    }

    public Usuario login(String usuario, String contrasenia) throws Exception {
        //La consulta SQL a ejecutar:
        String sql = "call obtener_usuario(?, ?)";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement ps = conn.prepareCall(sql);
        Usuario user = null;

        ps.setString(1, usuario);
        ps.setString(2, contrasenia);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            user = fill(rs);
        }

        rs.close();
        ps.close();
        connMySQL.close();

        return user;
    }
}
