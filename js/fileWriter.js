var _writer = null;

function writeToFile(message)
{
	if(_writer != null)
	{
		_writer.write(message);
	}
}

function getWriter()
{
	if(typeof(LocalFileSystem) != 'undefined')
	{
		alert("getWriter");
		window.requestFileSystem(LocalFileSystem.PERSISTENT, 1024*1024, gotFS, onError);
	}
	else
	{
		alert("Can't write to file, LocalFileSystem is undefined.");
	}
}

function gotFS(fileSystem)
{
	alert("gotFS");
	var date = new Date();

	var fileName = "log";
	fileName += "_" + date.getHours() + "_" + date.getMinutes();
	fileName += ".txt"
	
	fileSystem.root.getFile(fileName, {create: true, exclusive: false}, gotFileEntry, onError);
}

function gotFileEntry(fileEntry)
{
	alert("gotFileEntry");
	fileEntry.createWriter(gotFileWriter, onError);
}

function gotFileWriter(writer)
{
	alert("gotWriter");
	_writer = writer;
}

function onError(error)
{
	alert('code: '    + error.code    + '\n' +
		  'message: ' + error.message + '\n');
}