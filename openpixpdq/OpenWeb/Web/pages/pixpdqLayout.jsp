<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/lib/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/lib/core.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/lib/jmesa.tld" prefix="jmesa"%>
<html>
<head>
<link rel="stylesheet" href="<c:url value='/css/tabs.css'/>" type="text/css" />
<link rel="stylesheet" href="<c:url value='/css/table.css'/>" type="text/css" />
<link rel="stylesheet" href="<c:url value='/css/jmesa.css'/>" type="text/css"/>

<script type="text/javascript" src="<c:url value="/scripts/grid.js"/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/jmesa.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/jquery-1.2.2.pack.js'/>"></script>
<script type="text/javascript" language="javascript" src="<c:url value="/scripts/ConfigurationUI.js"/>"></script>
</head>
<body marginheight="0" marginwidth="0">
<table class="wholePage" border="0" width="100%" style="height: 100%;" cellpadding="0" cellspacing="0"> 
	<tr class="wholePage">
		<td><tiles:insert name="header" /></td>
	</tr>
	<tr class="wholePageTR" height="100%" width="100%">
		<td><tiles:insert name="body" /></td>
	</tr>
</table>
</body>
</html>
