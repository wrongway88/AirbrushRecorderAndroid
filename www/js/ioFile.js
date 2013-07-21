var ioFile = {
	fail: function(error)
	{
		alert("fail");
	
		console.log(error.code);
	},

	gotFileWriter: function(writer)
	{
		alert("gotFileWriter");
	
		writer.onwriteend = function(evt)
		{
            console.log("contents of file now 'some sample text'");
            writer.truncate(11);
            writer.onwriteend = function(evt) {
                console.log("contents of file now 'some sample'");
                writer.seek(4);
                writer.write(" different text");
                writer.onwriteend = function(evt){
                    console.log("contents of file now 'some different text'");
                }
            };
        };
        writer.write("some sample text");
	},

	gotFileEntry: function(fileEntry)
	{
		alert("gotFile");
		fileEntry.createWriter(gotFileWriter, fail);

	},

	gotFS: function(fileSystem)
	{
		alert("gotFS");
		fileSystem.root.getFile("readme.txt", {create: true, exclusive: false}, gotFileEntry, fail);
	},

	initialize: function()
	{
		alert("initialize");
		window.requestFileSystem(window.PERSISTENT, 0, gotFS, fail);
	}
}