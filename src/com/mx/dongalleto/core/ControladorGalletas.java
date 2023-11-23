package com.mx.dongalleto.core;

import com.mx.dongalleto.db.ConexionMySQL;
import com.mx.dongalleto.modelo.Galleta;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ControladorGalletas {

    public List<Galleta> getAll(String filtro) throws Exception {
        //La consulta SQL a ejecutar:
        String sql = "SELECT * FROM inventario_galleta";

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto ejecutaremos la consulta:
        PreparedStatement pstmt = conn.prepareStatement(sql);

        //Aquí guardaremos los resultados de la consulta:
        ResultSet rs = pstmt.executeQuery();

        List<Galleta> galletas = new ArrayList<>();

        while (rs.next()) {
            galletas.add(fill(rs));
        }

        rs.close();
        pstmt.close();
        connMySQL.close();

        return galletas;
    }

    private Galleta fill(ResultSet rs) throws Exception {
        Galleta galleta = new Galleta();

        if (rs.getInt("id") == -10) {

            return null;
        } else {

            galleta.setIdGalleta(rs.getInt("id"));
            galleta.setNombre(rs.getString("nombre"));
            galleta.setCantidad(rs.getInt("cantidad"));
            galleta.setDescripcion(rs.getString("descripcion"));
            galleta.setFotografia(rs.getString("fotografia"));

            return galleta;
        }
    }

    public void insert(Galleta g) throws Exception {
        //Definimos la consulta SQL que invoca al Stored Procedure:
        String sql = "{call InsertarGalleta(?,?,?,?,?)}";    // 4

        int idGalletaGenerada = -1;

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto invocaremos al StoredProcedure:
        CallableStatement cstmt = conn.prepareCall(sql);

        //Establecemos los parámetros de los datos personales en el orden
        //en que los pide el procedimiento almacenado, comenzando en 1:
        cstmt.setInt(1, g.getCantidad());
        cstmt.setString(2, g.getNombre());
        cstmt.setString(3, g.getDescripcion());
        cstmt.setString(4, g.getFotografia());

        cstmt.registerOutParameter(5, Types.INTEGER);

        cstmt.executeUpdate();

        //Recuperamos los ID's generados:
        idGalletaGenerada = cstmt.getInt(5);

        g.setIdGalleta(idGalletaGenerada);

        cstmt.close();
        conn.close();
        connMySQL.close();
    }

    public void update(Galleta g) throws Exception {
        //Definimos la consulta SQL que invoca al Stored Procedure:
        String sql = "{call ActualizarGalleta(?,?,?,?,?)}";// 32 IDs

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto invocaremos al StoredProcedure:
        CallableStatement cstmt = conn.prepareCall(sql);

        //Establecemos los parámetros de los datos personales en el orden
        //en que los pide el procedimiento almacenado, comenzando en 1:
        cstmt.setInt(1, g.getIdGalleta());
        cstmt.setInt(2, g.getCantidad());
        cstmt.setString(3, g.getNombre());
        cstmt.setString(4, g.getDescripcion());
        cstmt.setString(5, g.getFotografia());

        //Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();
        cstmt.close();
        conn.close();
        connMySQL.close();
    }

    public void delete(int id) throws Exception {

        String sql = "{call EliminarGalleta(?)}";

        ConexionMySQL connMySQL = new ConexionMySQL();

        Connection conn = connMySQL.open();

        CallableStatement cstmt = conn.prepareCall(sql);

        cstmt.setInt(1, id);

        cstmt.executeUpdate();
        conn.close();
        connMySQL.close();
    }

}
