var app = {
	onSuccess: function(position)
	{
		var element = document.getElementById('geolocation');
		element.innerHTML = 'lat: ' + position.coords.latitude +
							' long: ' + position.coords.longitude;
	},
	
	onError: function(error)
	{
		alert('code: '    + error.code    + '\n' +
			  'message: ' + error.message + '\n');
	},

    initialize: function()
	{
        var self = this;
		var options = {timeout: 3000};
		watchID = navigator.geolocation.watchPosition(this.onSuccess, this.onError, options);
    }
};

var watchID = null;

app.initialize();