var app = {
	onSuccess: function(position)
	{
		var element = document.getElementById('geolocation');
		element.innerHTML = 'lat: ' + position.coords.latitude +
							' long: ' + position.coords.longitude +
							' alt: ' + position.coords.altitude + 
							' speed: ' + position.coords.speed;
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
    }
};

var watchID = null;
var compassID = null;