<%@ page language="java" %>
<html>
<head>
<%@ taglib uri="/WEB-INF/lib/core.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/lib/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/lib/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/lib/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/lib/jmesa.tld" prefix="jmesa" %>
<link rel="stylesheet" href="<c:url value='/css/jmesa.css'/>" type="text/css"/>
<link rel="stylesheet" href="<c:url value='/css/tabs.css'/>" type="text/css"/>
<link rel="stylesheet" href="<c:url value='/css/table.css'/>" type="text/css"/>
<link rel="stylesheet" href="<c:url value='/css/domTT.css'/>" type="text/css" />
<script type="text/javascript" src="<c:url value='/scripts/jmesa.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/grid.js'/>"></script>
<script type="text/javascript" language="javascript" src="<c:url value="/scripts/domLib.js"/>"></script>
<script type="text/javascript" language="javascript" src="<c:url value="/scripts/domTT.js"/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/jquery-1.2.2.pack.js'/>"></script>

</head>
<body>
<table class="TableTS" cellpadding="0" cellspacing="0">
<thead class="TableTS" align="left">
		<th class="TableTS">Patient Demographic Query - SearchCriteria:</th>
	</thead>
	<tr><td><div class="Table">
<html:form action="Pdquery.do" method="post">
<table class="TablePDQ">			
		<tr>
			<td align="left" width="250px;">LastName:</td>
			<td align="left"><html:text property="lName" size="15" /></td>
			<td align="left" width="250px;">FirstName:</td>
			<td align="left"><html:text property="fName" size="15" /></td>
		</tr>
		<tr>
			<td align="left" width="250px;">StreetAddress:</td>
			<td align="left"><html:text property="address"  size="15"/></td>
			<td align="left" width="250px;">City:</td>
			<td align="left"><html:text property="city" size="15"/></td>
			<td align="left" width="250px;">State:</td>
			<td align="left"><html:text property="state" size="15"/></td>
			<td align="left" width="250px;">ZipCode:</td>
			<td align="left"><html:text property="zip" size="15"/></td>
			<td align="left" width="250px;">Country:</td>
			<td align="left"><html:text property="country" size="15"/></td>
			
		</tr>		
		<tr>
			<td align="left"width="250px;">Email Address:</td>
			<td align="left"><html:text property="email"  size="15" /></td>
			<td align="left"width="250px;">BirthDate(dd/mm/yyyy):</td>
			<td align="left"><html:text property="dob" size="15"/></td>
			<td align="left"width="250px;">Sex:</td>
			<td align="left"><html:text property="gender" size="15"/></td>
			<td></td><td></td><td></td>
			<td align="right"><html:submit property="action" value="Submit Query"/>
			</td>			
		</tr>				
</table>
<table class = "TableJMESA" cellpadding="0" cellspacing="0">
<tr><td>
	<div id="tabletag">
	<jmesa:tableFacade id="pdtag" items="${beanList}" maxRows="17" editable="false"
		stateAttr="restore" var="bean" rowFilter="" >
		<jmesa:htmlTable width="100%">
			<jmesa:htmlRow>
				<jmesa:htmlColumn title="Name" property="nameString" width="10%" cellRenderer="org.openhealthexchange.messagestore.grid.PatientIDCell"/>
				<jmesa:htmlColumn title="BirthDay" property="dob" width="10%" cellRenderer="org.openhealthexchange.messagestore.grid.OverflowCell"/>
				<jmesa:htmlColumn title="Sex" property="gender" width="10%"  cellRenderer="org.openhealthexchange.messagestore.grid.OverflowCell"/>
				<jmesa:htmlColumn title="Email Address" property="email" width="10%"  cellRenderer="org.openhealthexchange.messagestore.grid.OverflowCell"/>
				<jmesa:htmlColumn title="Address" property="fullAddress" width="10%"  cellRenderer="org.openhealthexchange.messagestore.grid.OverflowCell"/>
			</jmesa:htmlRow>
		</jmesa:htmlTable>
	</jmesa:tableFacade>
	</div>
	</td></tr>
</table>	
</html:form></div>
</td></tr>
</table>
</body>
</html>