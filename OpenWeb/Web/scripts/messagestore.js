
function onInvokeAction(id,action){
		
	    if(id == 'tag'){
	    setExportToLimit(id, '');  
	    
	    createHiddenInputFieldsForLimit(id);
	    
	  var x= document.getElementsByName('action');
	x[0].click();
	}			
 }       
 
  function onInvokeExportAction(id, action) { 
     var parameterString = createParameterStringForLimit(id); 
         location.href = 'MessageStore.do?' + parameterString;
   }
   
  
function displayDetails(message,rCount,title,percentMessageWidth,xpos,ypos,evt)
  {
 	 if(!document.getElementById('detailsDiv'+rCount))
 	   {
 			var contextRoot=document.getElementById('contextRoot').value; 
			var divName = document.createElement('div');
			var frame = document.createElement('iframe');
			frame.setAttribute('id','detailsFrame'+rCount);	
			divName.setAttribute('id','detailsDiv'+rCount);
			//var contextRoot=document.getElementById('contextRoot').value;
			divName.style.position = 'absolute'; 
			frame.style.position = 'absolute';
			divName.innerHTML  = "<table width=100% height=100% id='messageTable' bgcolor='white' bordercolor='black' border='1px' cellpadding='0px' cellspacing='0px' >" 
			 +"<font id='messageheader'></font>"
			 +"	<tr bordercolor='white' style='background-color:#013741;color:white;font-weight:bold' class='errormessage' >"
			 +"	<td colspan='2' width='96%' onmousemove='dragThisDivObject(event,\""+divName.id+"\",\""+rCount+"\");' onmousedown='setDistance(event); changeCursor(this,event,\"move\");'>"+title+"</td>"
			 +"	<td align='right' width='4%'><img id='messagebuttonid' src=\'"
			 +contextRoot
			 +"/images/close-orange.gif' onclick='return setFocus1(\""+divName.id+"\",\""+rCount+"\");'></td>"
			 +"	</tr><tr bordercolor='white'><td width='5%'></td>"
			 +"	<td colspan='3'width='4%' class='errormessage' align='left'><div style='white-space:nowrap;height:100px;width:550px;overflow:scroll;font-size:12px;font-family:Arial,Verdana'>"+message+ "</div></td>" 
			 +"	</tr><tr bordercolor='white'><td class='errormessage' colspan='2' align='center'>"
			 +"	<img id='messagebuttonid' src=\'"
			 +contextRoot
			 + "/images/alert-okbutton.gif' onclick='return setFocus1(\""+divName.id+"\",\""+rCount+"\");'>"
			 +"	</td></tr></table>";
			divName.style.left =xpos;
			divName.style.top=ypos;
			divName.style.width=percentMessageWidth;
			divName.style.height='250px';
			divName.style.wordwrap = 'normal';
			divName.style.zIndex=500;
			frame.style.width = divName.style.width;
		    frame.style.height = divName.style.height;
		    frame.style.top = divName.style.top;
		    frame.style.left = divName.style.left;
		    frame.frameborder='2';
		    frame.style.zIndex = divName.style.zIndex - 1;
			document.getElementById("tabletag").appendChild(divName);
			document.getElementById("tabletag").appendChild(frame);
		}	
	}
	
function dispLogDetails()
{
var table = document.getElementById("tag");
	// Get the rows from the table
	var trArray = table.getElementsByTagName("tr");
	//Iterate through the rows to get the label and value for the element
	//&nbsp;&nbsp;&nbsp is used as new line character for displaying in Div in Auditlog
	for(var k=4;k<trArray.length;k++)
	{
			
		var row = trArray.item(k);
		var tdArray = row.getElementsByTagName("td");
		var td = tdArray.item(9);
		var message= k+'in';
		td.id = message;
		var td1 = tdArray.item(10);	
		var message1= k+'out';	
		td1.id = message1;
		td.onmouseover = function(){
			if(this.innerHTML.length >0){
				this.style.cursor='hand';
				this.style.textDecoration = 'underline';
			}
		
		};
		
		td.onmouseout = function(){
			//this.style.cursor='hand';
			this.style.textDecoration = 'none';		
		};
		td.onclick = function () { 
			var ypos = this.offsetTop-18;
			var xpos = this.offsetLeft-650;
			var replaceString = this.innerHTML;
			var displayString = replaceInString(replaceString,"&nbsp;&nbsp;&nbsp;","<br>");
				displayDetails(displayString,this.id,'InputMessage','30%',xpos,ypos,this.event); 
		}
		td1.onmouseover = function(){
			if(this.innerHTML.length >0){
				this.style.cursor='hand';
				this.style.textDecoration = 'underline';
			}
		
		};
		
		td1.onmouseout = function(){
			//this.style.cursor='hand';
			this.style.textDecoration = 'none';		
		};
		td1.onclick = function () { 
			var ypos = this.offsetTop-18;
			var xpos = this.offsetLeft-700;
			var replaceString = this.innerHTML;
			var displayString = replaceInString(replaceString,"&nbsp;&nbsp;&nbsp;","<br>");
				displayDetails(displayString,this.id,'OutputMessage','30%',xpos,ypos,this.event); 
		}
	}
	
}function replaceInString(input,str,rstr){
		var output = "";
		arr1=input.split(str);
		for(i=0;i<arr1.length-1;i++)
			output += arr1[i]+rstr;
		output += arr1[arr1.length-1];
		return output;
}
	
	function setFocus1(obj,rc)
   {
   obj1 = document.getElementById(obj);
	    var frameObj = document.getElementById('detailsFrame'+rc);
		obj1.style.display = 'none';
	 	frameObj.style.display = 'none';
 		obj1.id =null;
	    frameObj.id =null;	   	
   }

var distX = 0;
var distY = 0;
function dragThisDivObject(evt, obj, rc)
{	
	// if left button pressed
	if (evt.button == 1) 
	{
	obj1 = document.getElementById(obj);
		// get the mouse position relative to the parent frame
		obj1.style.top =  evt.clientY - distY;
		obj1.style.left = evt.clientX - distX;
			obj2 = document.getElementById('detailsFrame'+rc);
			obj2.style.top = obj1.style.top;
			obj2.style.left = obj1.style.left;
	}	
	obj.onselectstart = function() { return false; }
}

function setDistance(evt) 
{	
	// gets the mouseposition within the div.
	distX = evt.offsetX;
	distY = evt.offsetY; 
	
}

//to change the cursor icon
function changeCursor(obj,evt, val,rc){
//obj1=document.getElementById('detailsFrame'+rc);
	if(evt.button==1){
		obj.style.cursor=val;
	}
}
function overflowTooltip(object, event) {
	var child = object.firstChild;
	
	//alert(child);
	// need to add 4 because of the space the ellipse takes up
	if (object.offsetWidth < child.offsetWidth + 4) {
		domTT_activate(object, event, 'content', child.cloneNode(true), 'styleClass', 'domTTClassic', 'closeAction', 'destroy');
	}
}

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

// set the value of an input
function setInput(inputId, value) {
	var input = document.getElementById(inputId);
	input.value = value;
}
