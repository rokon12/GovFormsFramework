<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Document Manager</title>

</head>
<body>

	<h3>Document List</h3>
	<c:if test="${!empty documentList}">
		<table class="data">
			<tr>
				<th>Name</th>
				<th>Description</th>
				<th>&nbsp;</th>
			</tr>
			<c:forEach items="${documentList}" var="document">
				<tr>
					<td width="100px">${document.name}</td>
					<td width="250px">${document.description}</td>
					<td width="20px"><a
						href="${pageContext.request.contextPath}/formBuilder/download/${document.id}.htm">
							Download</a> <a
						href="${pageContext.request.contextPath}/formBuilder/remove/${document.id}.htm">
							Delete </a></td>
				</tr>
			</c:forEach>
		</table>
	</c:if>

</body>
</html>
