<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" isELIgnored="false"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Ministry of Bangladesh</title>

<style type="text/css">
tr.odd {
	background-color: #D3E5DA;
}

td {
	border: 1px solid #D3E5DA;
	padding: 5px;
	font-size: 10pt;
	font-weight: normal;
}

td a {
	font-size: 10pt;
	font-weight: normal;
	color: #ff0000;
}
</style>
</head>
<body>
	<div>
		<table>
			<tr>
				<th>Ministry Name</th>
				<th>Ministry Short Name</th>
			</tr>

			<c:forEach var="f" items="${ministryList}">
				<tr>
					<td>${f.ministryName}</td>
					<td>${f.ministryShortName }</td>
				</tr>
			</c:forEach>

		</table>
	</div>
</body>
</html>