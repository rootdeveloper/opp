<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.sql.*" errorPage="" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.openhealthexchange.messagestore.vo.ConfigBean" %>
<%@ page import="org.openhealthexchange.openpixpdq.ihe.configuration.*" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html>

<link rel="stylesheet" href="<%= request.getContextPath() %>/css/table.css" type="text/css" />
<body>
<table class="TableTS" cellpadding="0" cellspacing="0">
<tr><td><div class="Table">
<form name="ConfigBean" method="post" action="Config.do">

  <%
  ConfigBean cb = (ConfigBean) request.getAttribute("ConfiBean");
    %>
    <div class="Table">
  <table class = "TableCON" cellpadding="0" cellspacing="0">
  <thead class = "TableTS">
  <th colspan="5" class = "TableTS"> 
		IHE Configuration
  </th>
  </thead>
   <tr class = "TableTS">
   <td class = "TableTS">	
	  Load configuration file:
	</td>
	<td class="TableTS">
	<input type=file id= "browse" name= "browse" style="display: none;">
	</td>
		<td>
		<%
		if (cb.getConfigFile() != null) {
		%><input type="text" size="80" id="configFile" name="configFile" value="<%=cb.getConfigFile()%>">
		<%
		} else {
		%><input type="text" size="80" name="configFile" value="">
		<%
		}
		%>
		</td>
		<td class="TableTS">
  	<input type=button onClick="browse.disabled=false;browse.click();configFile.value=browse.value;browse.disabled=true;" value="Browse"> 
	</td>
     <td class = "TableTS">		
    <input type="submit" name="action" value="Load">
    </td>
   </tr> 
   </table>
  </div>
  <%
    	List l = new LinkedList();
    	if (cb.getActors() != null) {

    		for (int x = 0; x < cb.getActors().length; x++) {
    			l.add(cb.getActors()[x]);
    		}
    	}
    	List lActors = (List) request.getAttribute("ActorList");
    	if (lActors != null && lActors.size() > 0) {
    		String sType = ((IheActorDescription) lActors.get(0)).getType();
    %>
    <div class="Table">
	 <table class = "TableCON">
  		<thead class = "TableTH">
  			<th colspan="3" class = "TableTH"> 
				<%=sType%>
		  	</th>
		</thead>
	<%
			System.out.println(lActors);
			for (int x = 0; x < lActors.size(); x++) {
				IheActorDescription iad = (IheActorDescription) lActors.get(x);
				if (!sType.equals(iad.getType())) {
			sType = iad.getType();
	%>
			<table class = "TableCON">
  				<thead class = "TableTH">
  					<th colspan="3" class = "TableTH"> 
						<%=sType%>
		  			</th>
				</thead>
			 
			<%
			 			}
			 			%>
		<tr class = "TableTS">
		<td class = "TableTS">
		<input type="checkbox" name="actors" value="<%=iad.getId()%>" 
		<%if (l.contains(iad.getId())){ %> checked="checked"<%}%> %>
			<%=iad.getDescription()%>
		</td>
		</tr>
		
		<%
				}
				%>
	</table>
	 <table class = "TableCON"> 
	 	
		<tr class = "TableTS">
		<td class = "TableTS">
			Log file:</td>
			<td class="TableTS">
	<input type=file name=logbrowse style="display: none;">
	</td>
	<td><%
	if (cb.getLogfile() != null) {
	%><input type="text" size="80" name="logfile" value="<%=cb.getLogfile()%>">
	<%
	} else {
	%><input type="text" size="80" name="logfile" value=""><%
	}
	%>
		</td>
		<td class="TableTS">
  	<input type=button onClick="logbrowse.disabled=false;logbrowse.click();logfile.value=logbrowse.value;logbrowse.disabled=true;" value="Browse" align="left"> 
	</td>
		</tr>
		<tr class = "TableTS">
		<td class = "TableTS" align="left" colspan="2">
			<input type="submit" name="action" value="Save">
		</td>
		<td class = "TableTS" align="center" colspan="2">
			<input type="submit" name="action" value="Stop All">
		</td>
		</tr>	
  	<%
	  	}
	  	%>
  	</table>
</table></div></form></div></td></tr></table>
<p>&nbsp;</p>
</body>
</html>
