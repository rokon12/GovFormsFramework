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
<title><fmt:message key="title.ministry" /></title>

</head>

<body>
	<c:url var="url" value="/ministry/${formAction}.htm" />
	<form:form modelAttribute="ministry" method="POST"
		action="../ministry/create.htm" id="form1">
		<div>
			<form:errors cssClass="error" />
		</div>

		<div class="label">
			<form:label path="ministryName">
				<fmt:message key="label.ministry.name" />
			</form:label>
			<span class="required">*</span>
		</div>
		<div class="field">
			<form:input path="ministryName" cssClass="required" />
			<form:errors path="ministryName" cssClass="error" />
		</div>
		<div class="clear"></div>

		<div class="label">
			<form:label path="ministryShortName">
				<fmt:message key="label.ministry.shortname" />
			</form:label>
			<span class="required">*</span>
		</div>
		<div class="field">
			<form:input path="ministryShortName" cssClass="required" />
			<form:errors path="ministryShortName" cssClass="error" />
		</div>
		<div class="clear"></div>

		<div class="buttonDivLeft">
			<a href="<c:url value="/ministry/index.htm"/>"><fmt:message
					key="button.minsitry" /></a>
		</div>
		<div class="buttonDiv">
			<input type="submit"
				value="<fmt:message key="button.ministry.create"/>" />
		</div>
	</form:form>

</body>
</html>