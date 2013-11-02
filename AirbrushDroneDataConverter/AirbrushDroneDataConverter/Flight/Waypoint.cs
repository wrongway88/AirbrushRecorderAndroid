using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Globalization;

namespace AirbrushDroneDataConverter.Flight
{
    class Waypoint
    {
        private int _t = 0;
        private double _latitude = 0.0;
        private double _longitude = 0.0;
        private double _altitude = 0.0;
        private float _speed = 0.0f;

        public Waypoint()
        {
        }

        public Waypoint(int t, double latitude, double longitude, double altitude, float speed)
        {
            _t = t;
            _latitude = latitude;
            _longitude = longitude;
            _altitude = altitude;
            _speed = speed;
        }

        public int T
        {
            get { return _t; }
            set { _t = value; }
        }

        public double Latitude
        {
            get { return _latitude; }
            set { _latitude = value; }
        }

        public double Longitude
        {
            get { return _longitude; }
            set { _longitude = value; }
        }

        public double Altitude
        {
            get { return _altitude; }
            set { _altitude = value; }
        }

        public float Speed
        {
            get { return _speed; }
            set { _speed = value; }
        }

        public String ToJSON()
        {
            String result = "{";

            result += "\"t\":" + _t.ToString() + ",";
            result += "\"lat\":" + Utility.Utility.ToFloatingPointString(_latitude, NumberFormatInfo.InvariantInfo) + ","; //_latitude.ToString(CultureInfo.InvariantCulture) + ",";
            result += "\"long\":" + Utility.Utility.ToFloatingPointString(_longitude, NumberFormatInfo.InvariantInfo) + ","; //_longitude.ToString(CultureInfo.InvariantCulture) + ",";
            result += "\"alt\":" + Utility.Utility.ToFloatingPointString(_altitude, NumberFormatInfo.InvariantInfo) + ","; //_altitude.ToString(CultureInfo.InvariantCulture) + ",";
            result += "\"speed\":" + Utility.Utility.ToFloatingPointString(_speed, NumberFormatInfo.InvariantInfo); //_speed.ToString(CultureInfo.InvariantCulture);

            result += "}";

            return result;
        }
    }
}
