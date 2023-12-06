package com.mx.dongalleto.core;

import com.mx.dongalleto.db.ConexionMySQL;
import com.mx.dongalleto.modelo.Materia;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ControladorMateria {

    public List<Materia> getAll(String filtro) throws Exception {
        //La consulta SQL a ejecutar:
        String sql = "SELECT * FROM materiaPrima";

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto ejecutaremos la consulta:
        PreparedStatement pstmt = conn.prepareStatement(sql);

        //Aquí guardaremos los resultados de la consulta:
        ResultSet rs = pstmt.executeQuery();

        List<Materia> materias = new ArrayList<>();

        while (rs.next()) {
            materias.add(fill(rs));
        }

        rs.close();
        pstmt.close();
        connMySQL.close();

        return materias;
    }

    private Materia fill(ResultSet rs) throws Exception {
        Materia materia = new Materia();

        if (rs.getInt("id") == -10) {

            return null;
        } else {

            materia.setId(rs.getInt("id"));
            materia.setNombre(rs.getString("nombre"));
            materia.setCantidad(rs.getDouble("cantidad"));
            materia.setUnidadMedida(rs.getString("unidad_medida"));
            materia.setProveedor(rs.getString("proveedor"));
            materia.setFechaCompra(rs.getString("fecha_compra"));
            materia.setPrecioUnitario(rs.getDouble("precio_unitario"));
            materia.setFotografia(rs.getString("fotografia"));

            return materia;
        }
    }


    public void insert(Materia g) throws Exception {
        //Definimos la consulta SQL que invoca al Stored Procedure:
        String sql = "{call insertarMateria(?,?,?,?,?,?,?,?)}";    // 4

        int idMateriaGenerada = -1;

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto invocaremos al StoredProcedure:
        CallableStatement cstmt = conn.prepareCall(sql);

        //Establecemos los parámetros de los datos personales en el orden
        //en que los pide el procedimiento almacenado, comenzando en 1:
        cstmt.setDouble(1, g.getCantidad());
        cstmt.setString(2, g.getNombre());
        cstmt.setString(3, g.getUnidadMedida());
        cstmt.setString(4, g.getProveedor());
        cstmt.setDouble(5, g.getPrecioUnitario());
        cstmt.setString(6, g.getFechaCompra());
        cstmt.setString(7, g.getFotografia());


        cstmt.registerOutParameter(8, Types.INTEGER);

        cstmt.executeUpdate();

        //Recuperamos los ID's generados:
        idMateriaGenerada = cstmt.getInt(8);

        g.setId(idMateriaGenerada);

        cstmt.close();
        conn.close();
        connMySQL.close();
    }

    public void update(Materia g) throws Exception {
        //Definimos la consulta SQL que invoca al Stored Procedure:
        String sql = "{call modificarMateria(?,?,?,?,?,?,?,?)}";
        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto invocaremos al StoredProcedure:
        CallableStatement cstmt = conn.prepareCall(sql);

        //Establecemos los parámetros de los datos personales en el orden
        //en que los pide el procedimiento almacenado, comenzando en 1:
        cstmt.setInt(1, g.getId());
        cstmt.setDouble(2, g.getCantidad());
        cstmt.setString(3, g.getNombre());
        cstmt.setString(4, g.getUnidadMedida());
        cstmt.setString(5, g.getProveedor());
        cstmt.setDouble(6, g.getPrecioUnitario());
        cstmt.setString(7, g.getFechaCompra());
        cstmt.setString(8, g.getFotografia());

        //Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();
        cstmt.close();
        conn.close();
        connMySQL.close();
    }

    public void delete(int id) throws Exception {

        String sql = "{call eliminarMateria(?)}";

        ConexionMySQL connMySQL = new ConexionMySQL();

        Connection conn = connMySQL.open();

        CallableStatement cstmt = conn.prepareCall(sql);

        cstmt.setInt(1, id);

        cstmt.executeUpdate();
        conn.close();
        connMySQL.close();
    }

}
