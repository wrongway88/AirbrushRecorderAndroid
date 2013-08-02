var app = {
	onSuccess: function(position)
	{	
		//alert("got position");
		
		var date = new Date();
		var time = (date.getHours() * 60.0 * 60.0) + (date.getMinutes() * 60.0) + date.getSeconds();
		time = time - startTime;
	
		var message = 	'{\"t\": ' + time +
						',\"lat\": ' + position.coords.latitude + 
						',\"long\": ' + position.coords.longitude +
						',\"alt\": ' + position.coords.altitude + 
						',\"speed": ' + position.coords.speed + '},';
			
		writeToFile(message);
		
		var element = document.getElementById('geolocation');	
		element.innerHTML = message;
	},
	
	onError: function(error)
	{
		alert('code: '    + error.code    + '\n' +
			  'message: ' + error.message + '\n');
	},
	
	onCompass: function(heading)
	{
		alert("compass");
		var element = document.getElementById('direction');
		element.innerHTML = 'direction: ' + heading.magneticHeading;
	},
	
    initialize: function()
	{
		alert("initialize");
	
        var self = this;
		
		//this.writeToFile();
		var compassOptions = {frequency:500, timeout: 300000};
		//compassID = navigator.compass.getCurrentHeading(onCompass, onError);//, compassOptions);
		
		var options = {frequency:500,maximumAge: 0, timeout: 300000, enableHighAccuracy:true};
		//navigator.geolocation.getCurrentPosition(onSuccess, onError);
		watchID = navigator.geolocation.watchPosition(this.onSuccess, this.onError, options);
		
		var date = new Date();
		startTime = (date.getHours() * 60.0 * 60.0) + (date.getMinutes() * 60.0) + date.getSeconds();
    }
};

var watchID = null;
var compassID = null;
var startTime = 0.0;