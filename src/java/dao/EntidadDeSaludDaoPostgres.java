package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.EntidadDeSalud;
import modelo.Usuario;
import dao.conexion.DatabaseConnection;
import factory.EntidadDeSaludFactory;
import factory.DatabaseConnectionFactory;

public class EntidadDeSaludDaoPostgres implements EntidadDeSaludDao {

    private final Connection conn;

    // Constructor que recibe el tipo de base de datos y utiliza la fábrica para obtener la conexión.
    public EntidadDeSaludDaoPostgres(String tipoDb) throws SQLException {
        DatabaseConnection dbConnection = DatabaseConnectionFactory.connection(tipoDb);
        this.conn = dbConnection.getConection();
    }

    @Override
    public EntidadDeSalud buscarEntidadDeSaludPorId(int id) {
        String sql = """
                SELECT e.id, e.nombre, e.direccion, e.telefono, e.usuario_gerente_id,
                       ep.nit_empresa, epub.codigo_ministerio,
                       u.id AS usuario_id, u.telefono AS usuario_telefono, u.salario_base AS usuario_salario_base,
                       u.fecha_nacimiento AS usuario_fecha_nacimiento, u.sexo AS usuario_sexo, u.direccion AS usuario_direccion
                FROM entidades_de_salud e
                LEFT JOIN entidades_de_salud_privada ep ON e.id = ep.id
                LEFT JOIN entidades_de_salud_publica epub ON e.id = epub.id
                LEFT JOIN usuarios u ON u.id = e.usuario_gerente_id
                WHERE e.id=?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Crear el UsuarioGerente
                Usuario usuarioGerente = new Usuario(
                        rs.getInt("usuario_id"),
                        rs.getString("usuario_telefono"),
                        rs.getInt("usuario_salario_base"),
                        rs.getDate("usuario_fecha_nacimiento").toLocalDate(),
                        rs.getString("usuario_sexo"),
                        rs.getString("usuario_direccion")
                );

                // Crear la entidad de salud usando la fábrica
                EntidadDeSalud entidadDeSalud = EntidadDeSaludFactory.crearEntidad(
                        rs.getString("nit_empresa") != null ? "privada" : "publica",
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        usuarioGerente,
                        rs.getString("nit_empresa") != null ? rs.getString("nit_empresa") : rs.getString("codigo_ministerio")
                );

                return entidadDeSalud;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar la entidad de salud: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public boolean verificarSiEntidadDeSaludExiste(int id) {
        EntidadDeSalud entidadDeSalud=null;
        boolean bandera=false;
        entidadDeSalud=buscarEntidadDeSaludPorId(id);
        if(entidadDeSalud!=null){
            bandera=true;
        }
        return bandera;
    }

    @Override
    public void guardar(EntidadDeSalud entidadDeSalud, String identificador) {
        String sqlEntiddad = """
            INSERT INTO entidades_de_salud (nombre, direccion, telefono, usuario_gerente_id)
            VALUES (?, ?, ?, ?)
            """;
        String sqlEntidadPrivada = """
                    INSERT INTO entidades_de_salud_privada(nit_empresa)
                    VALUES (?);      
                      """;
        String sqlEntidadPublica = """
                    INSERT INTO entidades_de_salud_publica(codigoMinisterio)
                    VALUES (?);
                    """;
         if (verificarSiEntidadDeSaludExiste(entidadDeSalud.getId())) {
            throw new RuntimeException("La entidad de salud ya existe.");
        }else {
            Usuario user= entidadDeSalud.getUsuarioGerente();
            if(identificador.equals("publica")){
                try(PreparedStatement ps1 =conn.prepareStatement(sqlEntiddad);
                    PreparedStatement ps2 =conn.prepareStatement(sqlEntidadPublica)){
                    ps1.setString(1, entidadDeSalud.getNombre());
                    ps1.setString(2, entidadDeSalud.getDireccion());
                    ps1.setString(3, entidadDeSalud.getTelefono());
                    ps1.setInt(4, user.getId());
                    ps1.executeQuery();
                    ps2.setString(1, identificador);
                    ps2.executeQuery();
                    
                } catch (SQLException e){
                    throw new RuntimeException("Erorr al crear la entidad de salud "+e.getMessage()+e);
                } 
            } else if(identificador.equals("privada")){
                try(PreparedStatement ps1 =conn.prepareStatement(sqlEntiddad);
                   PreparedStatement ps3 =conn.prepareStatement(sqlEntidadPrivada)){
                    ps1.setString(1, entidadDeSalud.getNombre());
                    ps1.setString(2, entidadDeSalud.getDireccion());
                    ps1.setString(3, entidadDeSalud.getTelefono());
                    ps1.setInt(4, user.getId());
                    ps1.executeQuery();
                    ps3.setString(1, identificador);
                    ps3.executeQuery();
                } catch (SQLException e){
                    throw new RuntimeException("Erorr al crear la entidad de salud "+e.getMessage()+e);
                } 
            
            }
        }
        

    }

    @Override
    public List<EntidadDeSalud> listarTodasLasEntidadDeSalud() {
        List<EntidadDeSalud> entidades = new ArrayList<>();
        String sql = """
                SELECT e.id, e.nombre, e.direccion, e.telefono, e.usuario_gerente_id,
                       ep.nit_empresa, epub.codigo_ministerio,
                       u.id AS usuario_id, u.telefono AS usuario_telefono, u.salario_base AS usuario_salario_base,
                       u.fecha_nacimiento AS usuario_fecha_nacimiento, u.sexo AS usuario_sexo, u.direccion AS usuario_direccion
                FROM entidades_de_salud e
                LEFT JOIN entidades_de_salud_privada ep ON e.id = ep.id
                LEFT JOIN entidades_de_salud_publica epub ON e.id = epub.id
                LEFT JOIN usuarios u ON u.id = e.usuario_gerente_id
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Crear el UsuarioGerente
                Usuario usuarioGerente = new Usuario(
                        rs.getInt("usuario_id"),
                        rs.getString("usuario_telefono"),
                        rs.getInt("usuario_salario_base"),
                        rs.getDate("usuario_fecha_nacimiento").toLocalDate(),
                        rs.getString("usuario_sexo"),
                        rs.getString("usuario_direccion")
                );

                // Crear la entidad de salud usando la fábrica
                EntidadDeSalud entidadDeSalud = EntidadDeSaludFactory.crearEntidad(
                        rs.getString("nit_empresa") != null ? "privada" : "publica",
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        usuarioGerente,
                        rs.getString("nit_empresa") != null ? rs.getString("nit_empresa") : rs.getString("codigo_ministerio")
                );

                entidades.add(entidadDeSalud);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar las entidades de salud: " + e.getMessage(), e);
        }
        return entidades;
    }

    @Override
    public void actualizarEntidadDeSalud(int id, EntidadDeSalud entidadDeSalud, String tipo) {
        if (entidadDeSalud == null || id <= 0) {
            throw new IllegalArgumentException("El usuario no debe tener id inválido ni nulo");
        }
        if (!verificarSiEntidadDeSaludExiste(id)) {
            throw new RuntimeException("El usuario con id " + id + " no existe.");
        }
        String sql = "UPDATE entidades_de_salud SET nombre = ?, direccion = ?, telefono = ?, idUsuarioGerente = ? WHERE id = ?"; 
        Usuario user = entidadDeSalud.getUsuarioGerente();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,entidadDeSalud.getNombre());
            ps.setString(2,entidadDeSalud.getDireccion());
            ps.setString(3,entidadDeSalud.getTelefono());
            ps.setInt(4,user.getId());
            int filasActualizadas=ps.executeUpdate();
            if(filasActualizadas<1){
                throw new RuntimeException("La entidad de salud con ese id no se pudo actualizar porque no se encontro una fila");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar la entidad de salud: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminarEntidadDeSalud(int id) {
        String sql = "DELETE FROM entidades_de_salud WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar la entidad de salud: " + e.getMessage(), e);
        }
    }

    @Override
    public List<EntidadDeSalud> buscarEntidadDeSaludPorNombre(String nombre){
    List<EntidadDeSalud> entidades = new ArrayList<>();
        String sql = """
                SELECT e.id, e.nombre, e.direccion, e.telefono, e.usuario_gerente_id,
                       ep.nit_empresa, epub.codigo_ministerio,
                       u.id AS usuario_id, u.telefono AS usuario_telefono, u.salario_base AS usuario_salario_base,
                       u.fecha_nacimiento AS usuario_fecha_nacimiento, u.sexo AS usuario_sexo, u.direccion AS usuario_direccion
                FROM entidades_de_salud e
                LEFT JOIN entidades_de_salud_privada ep ON e.id = ep.id
                LEFT JOIN entidades_de_salud_publica epub ON e.id = epub.id
                LEFT JOIN usuarios u ON u.id = e.usuario_gerente_id
                WHERE LOWER(e.nombre)=LOWER(?)
                """;
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
    ResultSet rs = ps.executeQuery();
        while (rs.next()) {
        // Crear el UsuarioGerente
        Usuario usuarioGerente = new Usuario(
                rs.getInt("usuario_id"),
                rs.getString("usuario_telefono"),
                rs.getInt("usuario_salario_base"),
                rs.getDate("usuario_fecha_nacimiento").toLocalDate(),
                rs.getString("usuario_sexo"),
                rs.getString("usuario_direccion")
        );

        // Comprobamos si la entidad es privada o pública
        String tipoEntidad = rs.getString("nit_empresa") != null ? "privada" : "publica";
        
        // Crear la entidad de salud usando la fábrica
        EntidadDeSalud entidadDeSalud = EntidadDeSaludFactory.crearEntidad(
                tipoEntidad,
                rs.getString("nombre"),
                rs.getString("direccion"),
                rs.getString("telefono"),
                usuarioGerente,
                tipoEntidad.equals("privada") ? rs.getString("nit_empresa") : rs.getString("codigo_ministerio")
        );

        // Añadir la entidad de salud a la lista
        entidades.add(entidadDeSalud);
    }
    } catch (SQLException e) {
        throw new RuntimeException("Error al listar las entidades de salud: " + e.getMessage(), e);
    }

    return entidades;
    }

    @Override
    public List<EntidadDeSalud> buscarEntidadDeSaludPorDireccion(String direccion) {
        List<EntidadDeSalud> entidades = new ArrayList<>();
        String sql = """
                SELECT e.id, e.nombre, e.direccion, e.telefono, e.usuario_gerente_id,
                       ep.nit_empresa, epub.codigo_ministerio,
                       u.id AS usuario_id, u.telefono AS usuario_telefono, u.salario_base AS usuario_salario_base,
                       u.fecha_nacimiento AS usuario_fecha_nacimiento, u.sexo AS usuario_sexo, u.direccion AS usuario_direccion
                FROM entidades_de_salud e
                LEFT JOIN entidades_de_salud_privada ep ON e.id = ep.id
                LEFT JOIN entidades_de_salud_publica epub ON e.id = epub.id
                LEFT JOIN usuarios u ON u.id = e.usuario_gerente_id
                WHERE LOWER(e.direccion)=LOWER(?)
                """;

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
    ResultSet rs = ps.executeQuery();
        while (rs.next()) {
        // Crear el UsuarioGerente
        Usuario usuarioGerente = new Usuario(
                rs.getInt("usuario_id"),
                rs.getString("usuario_telefono"),
                rs.getInt("usuario_salario_base"),
                rs.getDate("usuario_fecha_nacimiento").toLocalDate(),
                rs.getString("usuario_sexo"),
                rs.getString("usuario_direccion")
        );

        // Comprobamos si la entidad es privada o pública
        String tipoEntidad = rs.getString("nit_empresa") != null ? "privada" : "publica";
        
        // Crear la entidad de salud usando la fábrica
        EntidadDeSalud entidadDeSalud = EntidadDeSaludFactory.crearEntidad(
                tipoEntidad,
                rs.getString("nombre"),
                rs.getString("direccion"),
                rs.getString("telefono"),
                usuarioGerente,
                tipoEntidad.equals("privada") ? rs.getString("nit_empresa") : rs.getString("codigo_ministerio")
        );

        // Añadir la entidad de salud a la lista
        entidades.add(entidadDeSalud);
    }
    } catch (SQLException e) {
        throw new RuntimeException("Error al listar las entidades de salud: " + e.getMessage(), e);
    }

    return entidades;
    }

    @Override
    public List<EntidadDeSalud> buscarEntidadDeSaludPorGerente(int id) {
            List<EntidadDeSalud> entidades = new ArrayList<>();
            String sql = """
                SELECT e.id, e.nombre, e.direccion, e.telefono, e.usuario_gerente_id,
                       ep.nit_empresa, epub.codigo_ministerio,
                       u.id AS usuario_id, u.telefono AS usuario_telefono, u.salario_base AS usuario_salario_base,
                       u.fecha_nacimiento AS usuario_fecha_nacimiento, u.sexo AS usuario_sexo, u.direccion AS usuario_direccion
                FROM entidades_de_salud e
                LEFT JOIN entidades_de_salud_privada ep ON e.id = ep.id
                LEFT JOIN entidades_de_salud_publica epub ON e.id = epub.id
                LEFT JOIN usuarios u ON u.id = e.usuario_gerente_id
                WHERE e.usuario_gerente_id=?
                """;

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
    ResultSet rs = ps.executeQuery();
        while (rs.next()) {
        // Crear el UsuarioGerente
        Usuario usuarioGerente = new Usuario(
                rs.getInt("usuario_id"),
                rs.getString("usuario_telefono"),
                rs.getInt("usuario_salario_base"),
                rs.getDate("usuario_fecha_nacimiento").toLocalDate(),
                rs.getString("usuario_sexo"),
                rs.getString("usuario_direccion")
        );

        // Comprobamos si la entidad es privada o pública
        String tipoEntidad = rs.getString("nit_empresa") != null ? "privada" : "publica";
        
        // Crear la entidad de salud usando la fábrica
        EntidadDeSalud entidadDeSalud = EntidadDeSaludFactory.crearEntidad(
                tipoEntidad,
                rs.getString("nombre"),
                rs.getString("direccion"),
                rs.getString("telefono"),
                usuarioGerente,
                tipoEntidad.equals("privada") ? rs.getString("nit_empresa") : rs.getString("codigo_ministerio")
        );

        // Añadir la entidad de salud a la lista
        entidades.add(entidadDeSalud);
    }
    } catch (SQLException e) {
        throw new RuntimeException("Error al listar las entidades de salud: " + e.getMessage(), e);
    }

    return entidades;
    }

}
