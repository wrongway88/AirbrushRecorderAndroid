var app = {
	onSuccess: function(position)
	{	
		var date = new Date();
		var time = (date.getHours() * 60.0 * 60.0) + (date.getMinutes() * 60.0) + date.getSeconds();
		time = time - startTime;
	
		var message = 	'{\"t\": ' + time +
						',\"lat\": ' + position.coords.latitude + 
						',\"long\": ' + position.coords.longitude +
						',\"alt\": ' + position.coords.altitude + 
						',\"speed": ' + position.coords.speed + '},\n';
			
		writeToFile(message);
		
		var element = document.getElementById('geolocation');	
		element.innerHTML = message;
	},
	
	writeHeader: function()
	{
		/*
		var element = document.getElementById('textDeparture');
		trace(element.value);
		
		element = document.getElementById('textDestination');
		trace(element.value);
		*/
		
		writeToFile('{\"waypoints\":[\n');
	},
	
	writeFooter: function()
	{
		writeToFile(']}');
	},
	
	onError: function(error)
	{
		alert('code: '    + error.code    + '\n' +
			  'message: ' + error.message + '\n');
	},
	
	startRecording: function()
	{
		this.writeHeader();
	
		var options = {frequency:500,maximumAge: 0, timeout: 300000, enableHighAccuracy:true};
		watchID = navigator.geolocation.watchPosition(this.onSuccess, this.onError, options);
		
		var date = new Date();
		startTime = (date.getHours() * 60.0 * 60.0) + (date.getMinutes() * 60.0) + date.getSeconds();
		
		var element = document.getElementById('geolocation');	
		element.innerHTML = "Finding location...";
		
		element = document.getElementById('buttonRecord');
		element.value = "Stop";
		element.onclick= function(){app.stopRecording();};
	},
	
	stopRecording: function()
	{
		if(watchID != null)
		{
			this.writeFooter();
			
			navigator.geolocation.clearWatch(watchID);
			watchID = null;
			
			var element = document.getElementById('geolocation');	
			element.innerHTML = "";
			
			element = document.getElementById('buttonRecord');
			element.value = "Record";
			element.onclick= function(){app.startRecording();};
		}
	},
	
    initialize: function()
	{
		getWriter();
		
		var element = document.getElementById('buttonRecord');
		element.onclick = function(){app.startRecording();};
    }
};

var watchID = null;
var startTime = 0.0;


document.addEventListener("deviceready", onDeviceReady, false);
			
function onDeviceReady()
{
	app.initialize();
}