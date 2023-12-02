package com.mx.dongalleto.core;

import com.mx.dongalleto.db.ConexionMySQL;
import com.mx.dongalleto.modelo.Venta;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControladorVenta {

    public void insert(List<Venta> ventas) throws Exception {
        //Definimos la consulta SQL que invoca al Stored Procedure:
        String sql = "{call insertarVenta(?,?,?,?,?,?,?)}";    // 4

        //Con este objeto nos vamos a conectar a la Base de Datos:
        ConexionMySQL connMySQL = new ConexionMySQL();

        //Abrimos la conexión con la Base de Datos:
        Connection conn = connMySQL.open();

        //Con este objeto invocaremos al StoredProcedure:
        CallableStatement cstmt = conn.prepareCall(sql);

        //Establecemos los parámetros de los datos personales en el orden
        //en que los pide el procedimiento almacenado, comenzando en 1:
        ventas.forEach(venta -> {
            try {
                int idVentaGenerada = -1;

                cstmt.setObject(1, venta.getGalleta().getIdGalleta());
                cstmt.setObject(2, venta.getGalleta().getPrecio());
                cstmt.setObject(3, venta.getGalleta().getNombre());
                cstmt.setInt(4, venta.getCantidad());
                cstmt.setInt(5, venta.getTipo());
                cstmt.setString(6, venta.getFechaVenta());

                cstmt.registerOutParameter(7, Types.INTEGER);

                cstmt.executeUpdate();

                //Recuperamos los ID's generados:
                idVentaGenerada = cstmt.getInt(7);

                venta.setIdVenta(idVentaGenerada);
            } catch (SQLException ex) {
                Logger.getLogger(ControladorVenta.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        cstmt.close();
        conn.close();
        connMySQL.close();
    }

}
