<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <title>Listar Entidades de Salud</title>
</head>
<body>
    <h1>Lista de Entidades de Salud</h1>

    <c:if test="${not empty mensaje}">
        <p>${mensaje}</p>
    </c:if>

    <table border="1">
        <thead>
            <tr>
                <th>Nombre</th>
                <th>Dirección</th>
                <th>Teléfono</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="entidad" items="${entidades}">
                <tr>
                    <td>${entidad.nombre}</td>
                    <td>${entidad.direccion}</td>
                    <td>${entidad.telefono}</td>
                    <td>
                        <a href="EntidadDeSaludServlet?action=buscar&id=${entidad.identificador}">Ver</a> | 
                        <a href="EntidadDeSaludServlet?action=editar&id=${entidad.identificador}">Editar</a> | 
                        <a href="EntidadDeSaludServlet?action=eliminar&id=${entidad.identificador}">Eliminar</a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <a href="EntidadDeSaludServlet?action=crear">Crear nueva entidad de salud</a>
</body>
</html>
