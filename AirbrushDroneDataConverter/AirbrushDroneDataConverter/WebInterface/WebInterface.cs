using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net;
using System.IO;

namespace AirbrushDroneDataConverter.WebInterface
{
    class WebInterface
    {
        private static string ADDRESS_POST_FLIGHT = "http://airbrush.nucular-bacon.com/api/flight";
        private static string ADDRESS_POST_LOGIN = "http://airbrush.nucular-bacon.com/api/session";

        private static string SESSION_KEY = "";

        public static void Login(int userId, string password)
        {
            try
            {
                string postData = "id=" + userId.ToString() + "&password=" + password;

                string response = SendPost(ADDRESS_POST_LOGIN, postData);

                string searchString = "\"sessionkey\":\"";
                int idx = response.IndexOf(searchString);

                string sessionKey = response.Substring(idx + searchString.Length);
                sessionKey = sessionKey.Substring(0, sessionKey.Length - 2);

                SESSION_KEY = sessionKey;
            }
            catch (Exception exception)
            {

            }
        }

        public static void PostFlight(Flight.Flight flight)
        {
            try
            {
                string postData = flight.ToHTTP();
                Cookie cookie = null;

                if (SESSION_KEY.Length > 0)
                {
                    cookie = new Cookie("airbrush_session", SESSION_KEY);
                }

                SendPost(ADDRESS_POST_FLIGHT, postData, cookie);
            }
            catch (Exception exception)
            {

            }
        }

        private static string SendPost(string address, string postData, Cookie cookie = null)
        {
            string responseString = "POST Request failed";

            try
            {
                HttpWebRequest httpRequest = (HttpWebRequest)WebRequest.Create(address);

                ASCIIEncoding encoding = new ASCIIEncoding();
                byte[] data = encoding.GetBytes(postData);

                httpRequest.Method = "POST";
                httpRequest.ContentType = "application/x-www-form-urlencoded";
                httpRequest.Accept = "application/json";
                httpRequest.ContentLength = data.Length;

                if (cookie != null)
                {
                    httpRequest.CookieContainer = new CookieContainer();
                    httpRequest.CookieContainer.Add(new Uri(address), cookie);
                }

                using (Stream stream = httpRequest.GetRequestStream())
                {
                    stream.Write(data, 0, data.Length);
                }

                HttpWebResponse response = (HttpWebResponse)httpRequest.GetResponse();
                responseString = new StreamReader(response.GetResponseStream()).ReadToEnd();
            }
            catch(Exception exception)
            {
                System.Windows.Forms.MessageBox.Show(exception.Message);

                throw (exception);
            }

            return responseString;
        }
    }
}
