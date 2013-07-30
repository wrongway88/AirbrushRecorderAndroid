var _writer = null;
var _signaled = false;

function writeToFile(message)
{
	//alert("trying file write");
	
	if(_writer != null)
	{
		if(_signaled == false)
		{
			alert("writing to file");
			_signaled = true;
		}
		
		_writer.write(message);
	}
}

function getWriter()
{
	//alert("getWriter");

	if(typeof(LocalFileSystem) != 'undefined')
	{
		window.requestFileSystem(LocalFileSystem.PERSISTENT, 1024*1024, gotFS, onError);
	}
	else
	{
		alert("Can't write to file, LocalFileSystem is undefined.");
	}
}

function gotFS(fileSystem)
{
	//alert("gotFS");

	var date = new Date();

	var fileName = "log";
	fileName += "_" + date.getHours() + "_" + date.getMinutes();
	fileName += ".txt"
	
	fileSystem.root.getFile(fileName, {create: true, exclusive: false}, gotFileEntry, onError);
}

function gotFileEntry(fileEntry)
{
	//alert("gotFileEntry");

	fileEntry.createWriter(gotFileWriter, onError);
}

function gotFileWriter(writer)
{
	alert("gotWriter");

	_writer = writer;
	
	/*
	
	
	var date = new Date();
	var message = "file @ " + date.getHours() + ":" + date.getMinutes();
	*/
	
	_writer.write(message);
}

function onError(error)
{
	alert('code: '    + error.code    + '\n' +
		  'message: ' + error.message + '\n');
}