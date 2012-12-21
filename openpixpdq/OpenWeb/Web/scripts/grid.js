// No longer used as this functionality is built into extremecomponents
function highlightTableRows(tableId) { 
	var previousClass = null; 
	var table = document.getElementById(tableId);  
	var tbody = table.getElementsByTagName("tbody")[0]; 
	if (tbody == null) { 
	var rows = table.getElementsByTagName("tr"); 
	} else { 
	var rows = tbody.getElementsByTagName("tr"); 
	} 
	// add event handlers so rows light up
	for (i=0; i < rows.length; i++) { 
		rows[i].onmouseover = function() { previousClass=this.className;this.className+=' highlight' }; 
		rows[i].onmouseout = function() { this.className=previousClass }; 
	} 
}

// Use the domTT library to display a tooltip of an object child's inner text if the child is bigger than
// it's parent. This is used to display a tooltip on table cell overflow.
function overflowTooltip(object, event) {
	var child = object.firstChild;
	// need to add 4 because of the space the ellipse takes up
	if (object.offsetWidth < child.offsetWidth + 4) {
		domTT_activate(object, event, 'content', child.cloneNode(true), 'styleClass', 'domTTClassic', 'closeAction', 'destroy');
	}
}

// set the value of an input
function setInput(inputId, value) {
	var input = document.getElementById(inputId);
	input.value = value;
}
function onInvokeAction(id,action){
	
	    if(id == 'pdtag'){
	   setExportToLimit(id, '');  
	    
	    createHiddenInputFieldsForLimit(id);	 	
	var x= document.getElementsByName('action');
	x[0].click();
	}			
 }       
function onInvokeExportAction(id, action) { 
   var parameterString = createParameterStringForLimit(id); 
       location.href = 'Pdqquery.do?' + parameterString;
 }
 
 