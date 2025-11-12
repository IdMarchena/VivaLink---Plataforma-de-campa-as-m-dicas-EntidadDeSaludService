<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <title>Crear Entidad de Salud</title>
</head>
<body>
    <h1>Crear nueva Entidad de Salud</h1>

    <form action="EntidadDeSaludServlet" method="POST">
        <input type="hidden" name="action" value="crear" />
        
        <label for="nombre">Nombre: </label>
        <input type="text" name="nombre" required /><br>

        <label for="direccion">Dirección: </label>
        <input type="text" name="direccion" required /><br>

        <label for="telefono">Teléfono: </label>
        <input type="text" name="telefono" required /><br>

        <label for="tipo">Tipo: </label>
        <input type="text" name="tipo" required /><br>

        <label for="idUsuario">ID del Usuario: </label>
        <input type="text" name="idUsuario" required /><br>

        <button type="submit">Crear</button>
    </form>

    <c:if test="${not empty mensaje}">
        <p>${mensaje}</p>
    </c:if>

    <a href="EntidadDeSaludServlet?action=listar">Volver a la lista</a>
</body>
</html>
