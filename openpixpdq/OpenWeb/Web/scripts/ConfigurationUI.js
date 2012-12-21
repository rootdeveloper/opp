var application_urls=new Array("Registration.do","Pdquery.do","Pixquery.do","Config.do","MessageStore.do");

    function loadApplication(index)
    {  
        for(var i=0;i<application_urls.length;i++)
		{
			if(i == index)
				document.getElementById('headertab'+i).className = "active";	
			else
			    document.getElementById('headertab'+i).className = "";	
		}    
    	iSelectedApplication.location = application_urls[index];
    }
    
    
	