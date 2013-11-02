using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.IO;
using AirbrushDroneDataConverter.Flight;

namespace AirbrushDroneDataConverter.DroneData
{
    class DroneDataConverter
    {
        public static List<Flight.Flight> ConvertFile(StreamReader file, String fileName)
        {
            if (file == null)
            {
                return null;
            }

            List<Flight.Flight> flights = new List<Flight.Flight>();
            flights.Add(new Flight.Flight());
            flights.Last().AirplaneRegistration = fileName;

            Date initialDate = null;
            String line = "";
            bool dateSet = false;

            int t = 0;
            while ((line = file.ReadLine()) != null)
            {
                Waypoint waypoint = DroneWaypointConverter.ConvertWaypoint(line, ref initialDate);
                
                if (waypoint == null)
                    continue;

                //if the last waypoint came longer than 5 minutes ago, this one probably belongs to another flight
                if ((waypoint.T - t) > 300)
                {
                    flights.Add(new Flight.Flight());
                    flights.Last().AirplaneRegistration = fileName;
                    initialDate = null;
                    dateSet = false;

                    //flights.Last().Date = initialDate;

                }
                else
                {
                    flights.Last().PushWaypoint(waypoint);
                    if (initialDate != null && !dateSet)
                    {
                        flights.Last().Date = initialDate;
                        dateSet = true;
                    }
                }

                t = waypoint.T;
            }

            return flights;
        }
    }
}
