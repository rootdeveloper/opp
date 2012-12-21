<%@ page language="java"%>
<%@ page import="java.util.List" %>
<%@ taglib uri="/WEB-INF/lib/core.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/lib/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/lib/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/lib/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/lib/jmesa.tld" prefix="jmesa"%>
<link rel="stylesheet" href="<c:url value='/css/jmesa.css'/>" type="text/css"/>
<link rel="stylesheet" href="<c:url value='/css/tabs.css'/>" type="text/css"/>
<link rel="stylesheet" href="<c:url value='/css/table.css'/>" type="text/css"/>
<link rel="stylesheet" href="<c:url value='/css/domTT.css'/>" type="text/css" />
<script type="text/javascript" src="<c:url value='/scripts/jmesa.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/grid.js'/>"></script>
<script type="text/javascript" language="javascript" src="<c:url value="/scripts/domLib.js"/>"></script>
<script type="text/javascript" language="javascript" src="<c:url value="/scripts/domTT.js"/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/jquery-1.2.2.pack.js'/>"></script>

<table class="TableTS" cellpadding="0" cellspacing="0">
	<thead class="TableTS" align="left">
		<th class="TableTS">Patient Demographic Query - SearchCriteria:</th>
	</thead>
	<tr><td><div class="Table">
	<html:form action="Pixquery.do" method="post">
		<table class="TablePIX">

			<tr>
				<td align="right">Patient ID:</td>
				<td align="left"><html:text property="localid" size="18"
					maxlength="80" /></td>
				<td align="right">Assigning Authority:</td>
				<td>
				<html:select property="systemid" style="width: 150px;">   
  					<html:optionsCollection property="assigninglist" value="value" label="key" />   
				</html:select>   			
				</td>
				<td align="right"><html:submit property="action" value="Submit Query"></html:submit></td>
			</tr>

		</table>
		<table class = "TableJMESA" cellpadding="0" cellspacing="0">
		<tr><td>
		<div id="tabletag">
	<jmesa:tableFacade id="pixtag" items="${beanList}" maxRows="17" editable="false"
		stateAttr="restore" var="bean" rowFilter="">
		<jmesa:htmlTable width="100%">
			<jmesa:htmlRow>
				<jmesa:htmlColumn title="Name" property="nameString" width="10%" cellRenderer="org.openhealthexchange.messagestore.grid.PatientIDCell"  />
				<jmesa:htmlColumn title="BirthDay" property="dob" width="10%" cellRenderer="org.openhealthexchange.messagestore.grid.OverflowCell"/>
				<jmesa:htmlColumn title="Sex" property="gender" width="10%" cellRenderer="org.openhealthexchange.messagestore.grid.OverflowCell"/>
				<jmesa:htmlColumn title="Email Address" property="email" width="10%" cellRenderer="org.openhealthexchange.messagestore.grid.OverflowCell"/>
				<jmesa:htmlColumn title="Address" property="fullAddress" width="10%" cellRenderer="org.openhealthexchange.messagestore.grid.OverflowCell"/>
			</jmesa:htmlRow>
		</jmesa:htmlTable>
	</jmesa:tableFacade>
	</div>
	</td></tr>
	</table>
	</html:form></div>
	</td></tr>
</table>
