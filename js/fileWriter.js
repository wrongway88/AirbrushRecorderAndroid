var _writer = null;
var _signaled = false;

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
		window.requestFileSystem(LocalFileSystem.PERSISTENT, 1024*1024, gotFS, onError);
	}
	else
	{
		alert("Can't write to file, LocalFileSystem is undefined.");
	}
}

function gotFS(fileSystem)
{
	var date = new Date();

	var fileName = "log";
	fileName += "_" + date.getDay() + "-" + date.getMonth() + "_" + date.getHours() + ":" + date.getMinutes();
	fileName += ".txt"
	
	fileSystem.root.getFile(fileName, {create: true, exclusive: false}, gotFileEntry, onError);
}

function gotFileEntry(fileEntry)
{
	fileEntry.createWriter(gotFileWriter, onError);
}

function gotFileWriter(writer)
{
	_writer = writer;
	
	_writer.write(message);
}

function onError(error)
{
	alert('code: '    + error.code    + '\n' +
		  'message: ' + error.message + '\n');
}