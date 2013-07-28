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
	
	gotFS: function(fileSystem)
	{
		alert("gotFS");
		fileSystem.root.getFile("foo.txt", {create: true, exclusive: false}, gotFileEntry, onError);
	},
	
	gotFileEntry: function(fileEntry)
	{
		alert("gotFileEntry");
		fileEntry.createWriter(gotFileWriter, onError);
	},
	
	gotFileWriter: function(writer)
	{
		alert("gotFileWriter");
		writer.write("moin");
	},

	writeToFile: function()
	{
		alert("writeToFile");
	
		if(typeof(LocalFileSystem) == 'undefined')
		{
			alert("localFileSystem is undefined");
		}
		else
		{
			window.requestFileSystem(LocalFileSystem.PERSISTENT, 0, gotFS, onError);
		}
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
		var compassOptions = {frequency:500};
		//compassID = navigator.compass.getCurrentHeading(onCompass, onError);//, compassOptions);
		
		alert("asdfkl");
		
		var options = {frequency:500,maximumAge: 0, timeout: 300000, enableHighAccuracy:true};
		//navigator.geolocation.getCurrentPosition(onSuccess, onError);
		watchID = navigator.geolocation.watchPosition(this.onSuccess, this.onError, options);
    }
};

var watchID = null;
var compassID = null;