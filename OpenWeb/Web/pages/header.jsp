<%@ page language="java" %>


<link rel="stylesheet" href="<c:url value='/css/tabs.css'/>" type="text/css"/>
<html>
<head></head>
<body onload="javascript:loadApplication(0)">
<div class="header">
<table width="100%" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td class="name">OpenPIXPDQ</td>
		<td valign="bottom">
		<div class="tabs">
		<ul>
			<li id="headertab0"><a href="javascript:loadApplication(0)">Registration</a></li>
			<li id="headertab1"><a href="javascript:loadApplication(1)">Demographics Query</a></li>
			<li id="headertab2"><a href="javascript:loadApplication(2)">Pix Query</a></li>
			<li id="headertab3"><a href="javascript:loadApplication(3)">Configuration</a></li>
			<li id="headertab4"><a href="javascript:loadApplication(4)">Message Log</a></li>
		</ul>
		</div>

		</td>

		<td class="logo" align="right"><img src="images/logo_misys.gif" /></td>

	</tr>
	
</table>
</div>
</body>
</html>