using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Web;

namespace AirbrushDroneDataConverter.Flight
{
    class Flight
    {
        private String _airplaneRegistration = "";

        private Date _date = new Date();
        private String _departure = "nA";
        private String _destination = "nA";
        private List<Waypoint> _waypoints = new List<Waypoint>();

        public Flight()
        {
        }

        public Flight(Date date, String departure, String destination)
        {
            _date = date;
            _departure = departure;
            _destination = destination;
        }

        public String AirplaneRegistration
        {
            get { return _airplaneRegistration; }
            set { _airplaneRegistration = value; }
        }

        public Date Date
        {
            get { return _date; }
            set { _date = value; }
        }

        public String Departure
        {
            get { return _departure; }
            set { _departure = value; }
        }

        public String Destination
        {
            get { return _destination; }
            set { _destination = value; }
        }

        public void PushWaypoint(Waypoint waypoint)
        {
            _waypoints.Add(waypoint);
        }

        public int GetWaypointCount()
        {
            return _waypoints.Count;
        }

        public String ToJSON()
        {
            if (_departure.Length <= 0)
                _departure = "nA";
            if (_destination.Length <= 0)
                _destination = "nA";

            String result = "{";

            result += "\"date\":\"" + _date.ToJSON() + "\",";
            result += "\"departure\":\"" + _departure + "\",";
            result += "\"destination\":\"" + _destination + "\",";
            result += "\"waypoints\":[";

            for(int i = 0; i < _waypoints.Count; i++)
            {
                result += _waypoints[i].ToJSON();
                if (i < _waypoints.Count - 1)
                    result += ",";
            }

            result += "]}";

            return result;
        }

        public String ToHTTP()
        {
            if (_departure.Length <= 0)
                _departure = "nA";
            if (_destination.Length <= 0)
                _destination = "nA";

            String result = "";

            result += "departure=" + _departure;
            result += "&arrival=" + _destination;
            result += "&date=" + _date.ToHTTP();
            result += "&waypointsCompressed=";

            String waypoints = WaypointsToHTTP();
            waypoints = Utility.Utility.Compress(waypoints);
            waypoints = HttpUtility.UrlEncode(waypoints);

            if (_waypoints.Count > 0)
            {
                result += waypoints;
            }
            else
            {
                result += "%5B%5D";
            }

            return result;
        }

        private String WaypointsToHTTP()
        {
            string result = "[";

            for (int i = 0; i < _waypoints.Count; i++)
            {
                result += _waypoints[i].ToJSON();

                if (i < _waypoints.Count-1)
                {
                    result += ",";
                }
            }

            result += "]";

            return result;
        }
    }
}
