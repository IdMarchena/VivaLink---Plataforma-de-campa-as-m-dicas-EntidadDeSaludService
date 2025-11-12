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

@WebServlet(name = "EntidadDeSaludServlet", urlPatterns = {"/EntidadDeSaludServlet"})
public class EntidadDeSaludServlet extends HttpServlet {

    private final EntidadDeSaludService entidadDeSaludService;

    // Constructor
    public EntidadDeSaludServlet() throws SQLException {
        this.entidadDeSaludService = new EntidadDeSaludServiceImpl("TipoDb"); // Reemplaza "TipoDb" con el tipo de base de datos que usas
    }

    // Método para procesar las peticiones
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        System.out.println("Acción recibida: " + action);

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
            // Otras acciones o por defecto
        }
    }

    // Acción para crear una entidad de salud
    private void crearEntidadDeSalud(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String direccion = request.getParameter("direccion");
        String telefono = request.getParameter("telefono");
        String tipo = request.getParameter("tipo");
        String idUsuario = request.getParameter("idUsuario");

        try {
            UsuarioDto usuarioDto = obtenerUsuarioPorId(Integer.parseInt(idUsuario));
            EntidadDeSaludDto entidadDto = new EntidadDeSaludDto(tipo, nombre, direccion, telefono, idUsuario, usuarioDto);
            entidadDeSaludService.crearEntidadDeSalud(entidadDto);
            request.setAttribute("mensaje", "Entidad de Salud creada con éxito.");
            listarEntidades(request, response);
        } catch (Exception e) {
            request.setAttribute("mensaje", "Hubo un error al crear la entidad de salud.");
            request.getRequestDispatcher("/jsp/crearEntidadDeSalud.jsp").forward(request, response);
        }
    }

    // Acción para listar las entidades de salud
    private void listarEntidades(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<EntidadDeSaludDto> entidades = entidadDeSaludService.listarEntidadeDeSalud();
        request.setAttribute("entidades", entidades);
        request.getRequestDispatcher("/jsp/listarEntidadesDeSalud.jsp").forward(request, response);
    }
        // Acción para listar las entidades de salud
    private void listarEntidadDeSaludPorTipo(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String tipo = request.getParameter("tipo");
        List<EntidadDeSaludDto> entidades = entidadDeSaludService.listarEntidadDeSaludPorTipo(tipo);
        request.setAttribute("entidades", entidades);
        request.getRequestDispatcher("/jsp/listarEntidadesDeSalud.jsp").forward(request, response);
    }
            // Acción para listar las entidades de salud
    private void listarEntidadDeSaludPorDireccion(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String direccion = request.getParameter("direccion");
        List<EntidadDeSaludDto> entidades = entidadDeSaludService.listarEntidadDeSaludPorDireccion(direccion);
        request.setAttribute("entidades", entidades);
        request.getRequestDispatcher("/jsp/listarEntidadesDeSalud.jsp").forward(request, response);
    }

    // Acción para buscar una entidad de salud por ID
    private void buscarEntidadDeSalud(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idd = request.getParameter("id");
        int id = Integer.parseInt(idd);
        try {
            EntidadDeSaludDto entidad = entidadDeSaludService.buscarEntidadDeSalud(id);
            if (entidad != null) {
                request.setAttribute("entidad", entidad);
                request.getRequestDispatcher("/jsp/verEntidadDeSalud.jsp").forward(request, response);
            } else {
                response.sendRedirect("/jsp/entidadNoEncontrada.jsp");
            }
        } catch (Exception e) {
            response.sendRedirect("/jsp/entidadNoEncontrada.jsp");
        }
    }

    // Acción para actualizar una entidad de salud
    private void actualizarEntidadDeSalud(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idd = request.getParameter("id");
        int id = Integer.parseInt(idd);
        String nombre = request.getParameter("nombre");
        String direccion = request.getParameter("direccion");
        String telefono = request.getParameter("telefono");
        String tipo = request.getParameter("tipo");
        String idUsuario = request.getParameter("idUsuario");

        try {
            UsuarioDto usuarioDto = obtenerUsuarioPorId(Integer.parseInt(idUsuario));
            EntidadDeSaludDto entidadDto = new EntidadDeSaludDto(tipo, nombre, direccion, telefono, idUsuario, usuarioDto);
            entidadDeSaludService.actualizarEntidadDeSalud(id, entidadDto);
            request.setAttribute("mensaje", "Entidad de Salud actualizada con éxito.");
            listarEntidades(request, response);
        } catch (Exception e) {
            request.setAttribute("mensaje", "Hubo un error al actualizar la entidad de salud.");
            request.getRequestDispatcher("/jsp/editarEntidadDeSalud.jsp").forward(request, response);
        }
    }

    // Acción para eliminar una entidad de salud
    private void eliminarEntidadDeSalud(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String idd = request.getParameter("id");
        int id = Integer.parseInt(idd);

        try {
            entidadDeSaludService.eliminarEntidadDeSalud(id);
            request.setAttribute("mensaje", "Entidad de Salud eliminada con éxito.");
            listarEntidades(request, response);
        } catch (Exception e) {
            request.setAttribute("mensaje", "Hubo un error al eliminar la entidad de salud.");
            request.getRequestDispatcher("/jsp/listarEntidadesDeSalud.jsp").forward(request, response);
        }
    }

    // Acción para buscar entidades de salud por gerente
    private void buscarEntidadDeSaludPorGerente(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idUsuarioGerente = request.getParameter("idUsuarioGerente");
        List<EntidadDeSaludDto> entidades = entidadDeSaludService.buscarEntidadDeSaludPorGerente(Integer.parseInt(idUsuarioGerente));
        request.setAttribute("entidades", entidades);
        request.getRequestDispatcher("/jsp/listarEntidadesDeSaludPorGerente.jsp").forward(request, response);
    }

    // Acción para verificar si existe una entidad de salud con un nombre específico
    private void existeEntidadConNombre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        boolean existe = entidadDeSaludService.existeEntidadConNombre(nombre);
        request.setAttribute("existe", existe);
        request.getRequestDispatcher("/jsp/resultadoExistencia.jsp").forward(request, response);
    }

    // Acción para contar las entidades de salud por tipo
    private void contarEntidadesPorTipo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String tipo = request.getParameter("tipo");
        int cantidad = entidadDeSaludService.contarEntidadesPorTipo(tipo);
        request.setAttribute("cantidad", cantidad);
        request.getRequestDispatcher("/jsp/resultadoConteo.jsp").forward(request, response);
    }

    // Acción para registrar una entidad de salud con un usuario
    private void registrarEntidadConUsuario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String direccion = request.getParameter("direccion");
        String telefono = request.getParameter("telefono");
        String tipo = request.getParameter("tipo");
        String idUsuario = request.getParameter("idUsuario");

        try {
            UsuarioDto usuarioDto = obtenerUsuarioPorId(Integer.parseInt(idUsuario));
            EntidadDeSaludDto entidadDto = new EntidadDeSaludDto(tipo, nombre, direccion, telefono, idUsuario, usuarioDto);
            entidadDeSaludService.registrarEntidadConUsuario(entidadDto);
            request.setAttribute("mensaje", "Entidad de Salud registrada con éxito.");
            listarEntidades(request, response);
        } catch (Exception e) {
            request.setAttribute("mensaje", "Hubo un error al registrar la entidad de salud.");
            request.getRequestDispatcher("/jsp/registrarEntidadConUsuario.jsp").forward(request, response);
        }
    }

    // Método para obtener el UsuarioDto por ID (esto debería hacerse en tu servicio o DAO)
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
