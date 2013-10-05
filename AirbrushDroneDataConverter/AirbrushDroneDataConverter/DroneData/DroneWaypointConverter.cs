using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Globalization;
using AirbrushDroneDataConverter.Flight;
using AirbrushDroneDataConverter.Utility;

namespace AirbrushDroneDataConverter.DroneData
{
    class DroneWaypointConverter
    {
        public static Waypoint ConvertWaypoint(string data, ref Date initialDate)
        {
            //System.Windows.Forms.MessageBox.Show("convert waypoint: " + data);

            Waypoint waypoint = null;
            
            if (data.Length > 0)
            {
                int idx = data.IndexOf(",");
                if (idx < 0) return null;
                String tmp = data.Substring(0, idx);
                data = data.Substring(idx + 1);

                int t = 0;

                Date date = ConvertDate(tmp);
                if (initialDate == null && date != null)
                {
                    initialDate = date;
                }
                else if (date != null)
                {
                    t = date.GetDeltaSeconds(initialDate);
                }
                else if (date == null)
                {
                    //MessageBox.Show("DroneWaypointConverter::ConvertWaypoint: Failed to convert date");
                    return null;
                }

                waypoint = new Waypoint();
                waypoint.T = t;

                idx = data.IndexOf(",");
                if (idx < 0) return null;
                tmp = data.Substring(0, idx);
                data = data.Substring(idx + 1);

                double result = 0.0;
                if (Double.TryParse(tmp, System.Globalization.NumberStyles.Any, CultureInfo.InvariantCulture, out result))
                {
                    waypoint.Latitude = result;
                }
                else
                {
                    //MessageBox.Show("DroneWaypointConverter::ConvertWaypoint: Failed to convert latitude");
                    return null;
                }

                idx = data.IndexOf(",");
                if (idx < 0) return null;
                tmp = data.Substring(0, idx);
                data = data.Substring(idx + 1);

                if (Double.TryParse(tmp, System.Globalization.NumberStyles.Any, CultureInfo.InvariantCulture, out result))
                {
                    waypoint.Longitude = result;
                }
                else
                {
                    //MessageBox.Show("DroneWaypointConverter::ConvertWaypoint: Failed to convert longitude");
                    return null;
                }

                idx = data.IndexOf(",");
                if (idx < 0) return null;
                tmp = data.Substring(0, idx);
                data = data.Substring(idx + 1);

                if (Double.TryParse(tmp, System.Globalization.NumberStyles.Any, CultureInfo.InvariantCulture, out result))
                {
                    waypoint.Altitude = result;
                }
                else
                {
                    //MessageBox.Show("DroneWaypointConverter::ConvertWaypoint: Failed to convert altitude");
                    return null;
                }

                CalculateLatLongCoordinates(ref waypoint);
            }

            return waypoint;
        }

        private static Date ConvertDate(string dateString)
        {
            Date date = null;

            if (dateString.Length > 0)
            {
                int idx = dateString.IndexOf("-");
                if (idx < 0) return null;
                String tmp = dateString.Substring(0, idx);
                dateString = dateString.Substring(idx + 1);

                int result = 0;
                if (Int32.TryParse(tmp, out result))
                {
                    date = new Date();
                    date.Year = result;
                }
                else
                    return null;

                idx = dateString.IndexOf("-");
                if (idx < 0) return null;
                tmp = dateString.Substring(0, idx);
                dateString = dateString.Substring(idx + 1);

                if (Int32.TryParse(tmp, out result))
                {
                    date.Month = result;
                }
                else
                    return null;

                idx = dateString.IndexOf("-");
                if (idx < 0) return null;
                tmp = dateString.Substring(0, idx);
                dateString = dateString.Substring(idx + 1);

                if(Int32.TryParse(tmp, out result))
                {
                    date.Day = result;
                }
                else
                    return null;

                idx = dateString.IndexOf(":");
                if (idx < 0) return null;
                tmp = dateString.Substring(0, idx);
                dateString = dateString.Substring(idx + 1);

                if (Int32.TryParse(tmp, out result))
                {
                    date.Hour = result;
                }
                else
                    return null;

                idx = dateString.IndexOf(":");
                if (idx < 0) return null;
                tmp = dateString.Substring(0, idx);
                dateString = dateString.Substring(idx + 1);

                if (Int32.TryParse(tmp, out result))
                {
                    date.Minute = result;
                }
                else
                    return null;

                idx = dateString.IndexOf(".");
                if (idx < 0) return null;
                tmp = dateString.Substring(0, idx);
                if (Int32.TryParse(tmp, out result))
                {
                    date.Second = result;
                }
                else
                    return null;
            }

            return date;
        }

        private static void CalculateLatLongCoordinates(ref Waypoint waypoint)
        {
            float x = (float)waypoint.Latitude / 10000.0f;
            float y = (float)waypoint.Longitude / 10000.0f;

            float r = 0.0f;
            float angle = 0.0f;

            Utility.Utility.CartesianToPolar(x, y, out r, out angle);

            /**
             * @source http://williams.best.vwh.net/avform.htm#LL
             */
            float lat = (float)Math.Asin(Math.Sin(0.0) * Math.Cos(r) + Math.Cos(0.0) * Math.Sin(r) * Math.Cos(angle));
            float dlon = (float)Math.Atan2((Math.Sin(angle) * Math.Sin(r) * Math.Cos(0.0)), (Math.Cos(r)-Math.Sin(0.0) * Math.Sin(lat)));
            float lon = (float)(((0.0 - dlon + Math.PI) % (2.0 * Math.PI)) - Math.PI);

            waypoint.Latitude = lat;

            if (dlon != 0.0f)
                dlon = dlon;

            waypoint.Longitude = lon;
        }
        
        /*
        fromCartesian: function(vector)
          {
           vector = vec3.clone(vector);
           var earthRadius = 6371.0;
           var r = vec3.length(vector);
           vec3.scale(vector, vector, 1.0 / r);
           var lat = radToDeg(Math.asin(vector[1]));
           var long = radToDeg(Math.atan2(-vector[2], vector[0]));
           var alt = 1000.0 * (r - earthRadius);
   
           return new AirbrushLib.Utility.GeographicCoordinate(lat, long, alt);
          }
         */

        /*
        private static void FromCartesian(float x, float y, float z)
        {
            float earthRadius = 6371.0f;
            float r = earthRadius + y;

            x = x / r;
            y = y / r;
            float lat = Math.Asin(
        }
        */
    }
}
