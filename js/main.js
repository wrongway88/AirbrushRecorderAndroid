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
	
    initialize: function()
	{
		alert("initialize");
	
        var self = this;
		//var options = {timeout: 60000};
		//navigator.geolocation.getCurrentPosition(onSuccess, onError);
		//watchID = navigator.geolocation.watchPosition(this.onSuccess, this.onError, options);
		window.requestFileSystem(LocalFileSystem.PERSISTENT, 0, gotFS, onError);
    }
};

var watchID = null;