using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;
using System.IO;

namespace AirbrushDroneDataConverter.Utility
{
    [Serializable()]
    class LoginData : ISerializable
    {
        private const string FILE_PATH = "data/config.zut";

        private string _mailAddress = "";
        private int _userId = -1;
        private string _password = "";

        static public LoginData LoadLoginData()
        {
            LoginData loginData = new LoginData();

            Stream stream = File.Open(FILE_PATH, FileMode.Open);
            if (stream != null)
            {
                BinaryFormatter formatter = new BinaryFormatter();
                loginData = (LoginData)formatter.Deserialize(stream);
                stream.Close();
            }

            return loginData;
        }

        static public void SaveLoginData(LoginData loginData)
        {
            Stream stream = File.Open(FILE_PATH, FileMode.OpenOrCreate);
            BinaryFormatter formatter = new BinaryFormatter();
            formatter.Serialize(stream, loginData);
            stream.Close();
        }

        public LoginData()
        {
        }

        public LoginData(SerializationInfo info, StreamingContext context)
        {
            _mailAddress = (string)info.GetValue("_mailAddress", typeof(string));
            _userId = (int)info.GetValue("_userId", typeof(int));
            _password = (string)info.GetValue("_password", typeof(string));
        }

        public void GetObjectData(SerializationInfo info, StreamingContext context)
        {
            info.AddValue("_mailAddress", _mailAddress);
            info.AddValue("_userId", _userId);
            info.AddValue("_password", _password);
        }

        public string MailAddress
        {
            get { return _mailAddress; }
            set { _mailAddress = value; }
        }

        public int UserId
        {
            get { return _userId; }
            set { _userId = value; }
        }

        public string Password
        {
            get { return _password; }
            set { _password = value; }
        }
    }
}
