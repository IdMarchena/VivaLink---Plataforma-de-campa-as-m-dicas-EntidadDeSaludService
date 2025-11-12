<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <title>Ver Entidad de Salud</title>
</head>
<body>
    <h1>Detalles de la Entidad de Salud</h1>

    <c:if test="${not empty entidad}">
        <p><strong>Nombre:</strong> ${entidad.nombre}</p>
        <p><strong>Dirección:</strong> ${entidad.direccion}</p>
        <p><strong>Teléfono:</strong> ${entidad.telefono}</p>
        <p><strong>Tipo:</strong> ${entidad.tipo}</p>
        <p><strong>Usuario Responsable:</strong> ${entidad.usuarioDto.nombre}</p>
    </c:if>

    <a href="EntidadDeSaludServlet?action=listar">Volver a la lista</a>
</body>
</html>
