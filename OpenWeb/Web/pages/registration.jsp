<%@ page language="java"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ taglib uri="/WEB-INF/lib/core.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/lib/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/lib/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/lib/struts-html.tld" prefix="html"%>
<script type="text/javascript" src="<c:url value="/../../scripts/ConfigurationUI.js"/>"></script>
<link rel="stylesheet" href="<c:url value='/css/table.css'/>" type="text/css"/>
<table class="TableTS" cellpadding="0" cellspacing="0">
	<thead class="TableTS" align="left">
		<th class="TableTS">Patient Registration</th>
	</thead>
	<tr class="TableTS"><td class="TableRG"><div class="DivRG">
	<html:form action="Registration.do" method="post">
		<table class="TableRG" align="left">
			<tr>
				<td align="left" width="250px;"><font size="1" color="red">*</font>Last Name:</td>
				<td align="left"><html:text property="lName" size="18" /></td>
			</tr>
			<tr>
				<td align="left" width="250px;"><font size="1" color="red">*</font>First Name:</td>
				<td align="left"><html:text property="fName" size="18" /></td>
			</tr>
			<tr>
				<td align="left" width="250px;">&nbsp; Street Address:</td>
				<td align="left"><html:text property="address" size="18" /></td>
			</tr>
			<tr>
				<td align="left" width="250px;">&nbsp; City:</td>
				<td align="left"><html:text property="city" size="18" /></td>
			</tr>
			<tr>
				<td align="left" width="250px;">&nbsp; State:</td>
				<td align="left"><html:text property="state" size="18" /></td>
			</tr>
			<tr>
				<td align="left" width="250px;">&nbsp; Zip Code:</td>
				<td align="left"><html:text property="zip" size="18" /></td>
			</tr>
			<tr>
				<td align="left" width="250px;">&nbsp; Country:</td>
				<td align="left"><html:text property="country" size="18" /></td>
			</tr>
			<tr>
				<td align="left" width="250px;">&nbsp; Email Address:</td>
				<td align="left"><html:text property="email" size="18" /></td>
			</tr>
			<tr>
				<td align="left" width="320px;"><font size="1" color="red">*</font>Birth Day(dd/mm/yyyy):</td>
				<td align="left"><html:text property="dob" size="18" /></td>
			</tr>
			<tr>
				<td align="left" width="250px;"><font size="1" color="red">*</font>Sex:</td>
				<td align="left"><html:text property="gender" size="18" /></td>
			</tr>
			<tr>
				<td align="left" width="250px;"><font size="1" color="red">*</font>Patient ID:</td>
				<td align="left"><html:text property="localid" size="18" /></td>
				<td align="left" width="300px;"><font size="1" color="red">*</font>Assigning Authority:</td>
				<td>
				<html:select property="systemid" style="width: 150px;">   
  					<html:optionsCollection property="assigninglist" value="value" label="key" />   
				</html:select>   			
				</td>
			</tr>
			&nbsp;
			&nbsp;
			<tr>
				<td align="right"><html:reset></html:reset></td>
				<td align="center"><html:submit property="action" value="Save"></html:submit></td>
			</tr>
		</table>	
	</html:form>
	</div>
</td></tr>
<tr><td align="center" height="25px">
<%String ip= (String)request.getAttribute("serverport"); 
if(ip != null){
%>
PIX Server IP Address: <%=ip%>
<%}%>
</td></tr>
</table>

