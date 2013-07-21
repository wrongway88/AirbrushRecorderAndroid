var ioFile = {
	fail: function(error)
	{
		console.log(error.code);
	},

	gotFileWriter: function(writer)
	{
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
		fileEntry.createWriter(gotFileWriter, fail);

	},

	gotFS: function(fileSystem)
	{
		fileSystem.root.getFile("readme.txt", {create: true, exclusive: false}, gotFileEntry, fail);
	},

	initialize: function()
	{
		window.requestFileSystem(LocalFileSystem.PERSISTENT, 0, gotFS, fail);
	}
}