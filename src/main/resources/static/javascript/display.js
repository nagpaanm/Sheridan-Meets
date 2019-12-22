/**
Used for the purpose of displaying/hiding and checking existing passwords/usernames
 */

function display(elementId){
	var element = document.getElementById(elementId);
	if(element.style.display == "block"){
		element.style.display = "none";
		var h2 = document.getElementById("h2-container").style.opacity="1";
	}else{
		element.style.display = "block";
		var h2 = document.getElementById("h2-container").style.opacity="0.2";
	}	
}

function performCheck(currentPassword){
	var pass1 = document.getElementById("pass1");
	var form = document.getElementById("change-password-form");
	if(pass1.value === currentPassword){
		form.submit();
	}else{
		var msg = document.getElementById("invalid");
		msg.style.display="block";
		msg.style.color="red";
	}
}

function getEncryptedPassword(url, userName){
	var pass1 = document.getElementById("pass1");
	var newPass = document.getElementById("pass2");
	var msg1 = document.getElementById("invalid-newpass");
	var msg2 = document.getElementById("invalid");
	//alert(url+pass1.value+"/"+userName); // ajax is very finicky, use this as a gauge
	fetch(url+pass1.value+"/"+userName)
		.then(data => data.json())
			.then(function(data){
				//alert(data.password);
				var form = document.getElementById("change-password-form");
				if(data.password === "true"){
					if(newPass.value.length > 0){
						form.submit();
					}else{
						msg1.style.display="block";
						msg1.style.color="red";
						pass2.focus();
						msg2.style.display="none";
					}
				}else{
					msg2.style.display="block";
					msg2.style.color="red";
					pass1.focus();
					msg1.style.display="none";
				}
			});
}


function checkUserName(url){
	var un = document.getElementById("un");
	//alert(url+pass1.value+"/"+userName); // ajax is very finicky, use this as a gauge
	fetch(url+un.value)
		.then(data => data.json())
			.then(function(data){
				//alert(data.password);
				var form = document.getElementById("register-form");
				if(data.password === "true"){
					var sub = document.getElementById("submit-btn").click();
					//form.submit();
				}else{
					var msg = document.getElementById("invalid");
					msg.style.display="block";
					msg.style.color="red";
					msg.style.position="absolute";
					msg.style.top="10px";
					un.focus();
				}
			});
}