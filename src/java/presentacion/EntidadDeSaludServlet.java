package presentacion;

import servicio.service.EntidadDeSaludService;
import servicio.serviceImpl.EntidadDeSaludServiceImpl;
import dto.EntidadDeSaludDto;
import dto.UsuarioDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import com.google.gson.Gson;

@WebServlet(name = "EntidadDeSaludServlet", urlPatterns = {"/EntidadDeSaludServlet"})
public class EntidadDeSaludServlet extends HttpServlet {

    private final EntidadDeSaludService entidadDeSaludService;
    private final Gson gson;

    // Constructor
    public EntidadDeSaludServlet() throws SQLException {
        this.entidadDeSaludService = new EntidadDeSaludServiceImpl("TipoDb");
        this.gson = new Gson();
    }

    // Método para procesar las peticiones
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        System.out.println("Acción recibida: " + action);

        // Configurar respuesta como JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Filtra las acciones según el parámetro "action"
            if ("crear".equals(action)) {
                crearEntidadDeSalud(request, response);
            } else if ("listar".equals(action)) {
                listarEntidades(request, response);
            } else if ("buscar".equals(action)) {
                buscarEntidadDeSalud(request, response);
            } else if ("actualizar".equals(action)) {
                actualizarEntidadDeSalud(request, response);
            } else if ("eliminar".equals(action)) {
                eliminarEntidadDeSalud(request, response);
            } else if ("listarPorTipo".equals(action)) {
                listarEntidadDeSaludPorTipo(request, response);
            } else if ("listarPorDireccion".equals(action)) {
                listarEntidadDeSaludPorDireccion(request, response);
            } else if ("buscarPorGerente".equals(action)) {
                buscarEntidadDeSaludPorGerente(request, response);
            } else if ("existeEntidadConNombre".equals(action)) {
                existeEntidadConNombre(request, response);
            } else if ("contarEntidadesPorTipo".equals(action)) {
                contarEntidadesPorTipo(request, response);
            } else if ("registrarEntidadConUsuario".equals(action)) {
                registrarEntidadConUsuario(request, response);
            } else {
                // Acción no reconocida
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson("Acción no reconocida: " + action));
            }
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error interno del servidor: " + e.getMessage()));
        }
    }

    // Acción para crear una entidad de salud
    private void crearEntidadDeSalud(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String nombre = request.getParameter("nombre");
        String direccion = request.getParameter("direccion");
        String telefono = request.getParameter("telefono");
        String tipo = request.getParameter("tipo");
        String idUsuario = request.getParameter("idUsuario");

        try {
            // Validar parámetros requeridos
            if (nombre == null || nombre.isEmpty() || direccion == null || direccion.isEmpty() ||
                telefono == null || telefono.isEmpty() || tipo == null || tipo.isEmpty() ||
                idUsuario == null || idUsuario.isEmpty()) {
                
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson("Todos los campos son requeridos"));
                return;
            }

            UsuarioDto usuarioDto = obtenerUsuarioPorId(Integer.parseInt(idUsuario));
            EntidadDeSaludDto entidadDto = new EntidadDeSaludDto(tipo, nombre, direccion, telefono, idUsuario, usuarioDto);
            entidadDeSaludService.crearEntidadDeSalud(entidadDto);
            
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write(gson.toJson("Entidad de Salud creada con éxito"));
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al crear entidad de salud: " + e.getMessage()));
        }
    }

    // Acción para listar las entidades de salud
    private void listarEntidades(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            List<EntidadDeSaludDto> entidades = entidadDeSaludService.listarEntidadeDeSalud();
            response.getWriter().write(gson.toJson(entidades));
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al listar entidades: " + e.getMessage()));
        }
    }

    // Acción para listar entidades de salud por tipo
    private void listarEntidadDeSaludPorTipo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String tipo = request.getParameter("tipo");
        
        if (tipo == null || tipo.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("El parámetro tipo es requerido"));
            return;
        }
        
        try {
            List<EntidadDeSaludDto> entidades = entidadDeSaludService.listarEntidadDeSaludPorTipo(tipo);
            response.getWriter().write(gson.toJson(entidades));
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al listar entidades por tipo: " + e.getMessage()));
        }
    }

    // Acción para listar entidades de salud por dirección
    private void listarEntidadDeSaludPorDireccion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String direccion = request.getParameter("direccion");
        
        if (direccion == null || direccion.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("El parámetro direccion es requerido"));
            return;
        }
        
        try {
            List<EntidadDeSaludDto> entidades = entidadDeSaludService.listarEntidadDeSaludPorDireccion(direccion);
            response.getWriter().write(gson.toJson(entidades));
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al listar entidades por dirección: " + e.getMessage()));
        }
    }

    // Acción para buscar una entidad de salud por ID
    private void buscarEntidadDeSalud(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("El parámetro id es requerido"));
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            EntidadDeSaludDto entidad = entidadDeSaludService.buscarEntidadDeSalud(id);
            
            if (entidad != null) {
                response.getWriter().write(gson.toJson(entidad));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(gson.toJson("Entidad de salud no encontrada"));
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("ID de entidad de salud inválido"));
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al buscar entidad de salud: " + e.getMessage()));
        }
    }

    // Acción para actualizar una entidad de salud
    private void actualizarEntidadDeSalud(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idStr = request.getParameter("id");
        String nombre = request.getParameter("nombre");
        String direccion = request.getParameter("direccion");
        String telefono = request.getParameter("telefono");
        String tipo = request.getParameter("tipo");
        String idUsuario = request.getParameter("idUsuario");

        try {
            int id = Integer.parseInt(idStr);
            
            // Validar parámetros requeridos
            if (nombre == null || nombre.isEmpty() || direccion == null || direccion.isEmpty() ||
                telefono == null || telefono.isEmpty() || tipo == null || tipo.isEmpty() ||
                idUsuario == null || idUsuario.isEmpty()) {
                
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson("Todos los campos son requeridos"));
                return;
            }

            UsuarioDto usuarioDto = obtenerUsuarioPorId(Integer.parseInt(idUsuario));
            EntidadDeSaludDto entidadDto = new EntidadDeSaludDto(tipo, nombre, direccion, telefono, idUsuario, usuarioDto);
            entidadDeSaludService.actualizarEntidadDeSalud(id, entidadDto);
            
            response.getWriter().write(gson.toJson("Entidad de Salud actualizada con éxito"));
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("ID de entidad de salud inválido"));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al actualizar entidad de salud: " + e.getMessage()));
        }
    }

    // Acción para eliminar una entidad de salud
    private void eliminarEntidadDeSalud(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idStr = request.getParameter("id");

        try {
            int id = Integer.parseInt(idStr);
            entidadDeSaludService.eliminarEntidadDeSalud(id);
            
            response.getWriter().write(gson.toJson("Entidad de Salud eliminada con éxito"));
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("ID de entidad de salud inválido"));
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al eliminar entidad de salud: " + e.getMessage()));
        }
    }

    // Acción para buscar entidades de salud por gerente
    private void buscarEntidadDeSaludPorGerente(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idUsuarioGerente = request.getParameter("idUsuarioGerente");
        
        if (idUsuarioGerente == null || idUsuarioGerente.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("El parámetro idUsuarioGerente es requerido"));
            return;
        }
        
        try {
            List<EntidadDeSaludDto> entidades = entidadDeSaludService.buscarEntidadDeSaludPorGerente(Integer.parseInt(idUsuarioGerente));
            response.getWriter().write(gson.toJson(entidades));
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("ID de usuario gerente inválido"));
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al buscar entidades por gerente: " + e.getMessage()));
        }
    }

    // Acción para verificar si existe una entidad de salud con un nombre específico
    private void existeEntidadConNombre(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String nombre = request.getParameter("nombre");
        
        if (nombre == null || nombre.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("El parámetro nombre es requerido"));
            return;
        }
        
        try {
            boolean existe = entidadDeSaludService.existeEntidadConNombre(nombre);
            response.getWriter().write(gson.toJson(existe));
            
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al verificar existencia: " + e.getMessage()));
        }
    }

    // Acción para contar las entidades de salud por tipo
    private void contarEntidadesPorTipo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String tipo = request.getParameter("tipo");
        
        if (tipo == null || tipo.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("El parámetro tipo es requerido"));
            return;
        }
        
        try {
            int cantidad = entidadDeSaludService.contarEntidadesPorTipo(tipo);
            response.getWriter().write(gson.toJson(cantidad));
            
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al contar entidades: " + e.getMessage()));
        }
    }

    // Acción para registrar una entidad de salud con un usuario
    private void registrarEntidadConUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String nombre = request.getParameter("nombre");
        String direccion = request.getParameter("direccion");
        String telefono = request.getParameter("telefono");
        String tipo = request.getParameter("tipo");
        String idUsuario = request.getParameter("idUsuario");

        try {
            // Validar parámetros requeridos
            if (nombre == null || nombre.isEmpty() || direccion == null || direccion.isEmpty() ||
                telefono == null || telefono.isEmpty() || tipo == null || tipo.isEmpty() ||
                idUsuario == null || idUsuario.isEmpty()) {
                
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson("Todos los campos son requeridos"));
                return;
            }

            UsuarioDto usuarioDto = obtenerUsuarioPorId(Integer.parseInt(idUsuario));
            EntidadDeSaludDto entidadDto = new EntidadDeSaludDto(tipo, nombre, direccion, telefono, idUsuario, usuarioDto);
            entidadDeSaludService.registrarEntidadConUsuario(entidadDto);
            
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write(gson.toJson("Entidad de Salud registrada con éxito"));
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson("ID de usuario inválido"));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Error al registrar entidad: " + e.getMessage()));
        }
    }

    // Método para obtener el UsuarioDto por ID
    private UsuarioDto obtenerUsuarioPorId(int idUsuario) throws Exception {
        return new UsuarioDto(idUsuario, "telefonoEjemplo", 5000, java.time.LocalDate.now(), "Masculino", "Direccion Ejemplo");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}