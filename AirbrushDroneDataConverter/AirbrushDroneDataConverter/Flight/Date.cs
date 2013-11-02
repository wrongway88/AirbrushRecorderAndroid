using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AirbrushDroneDataConverter.Flight
{
    public class Date
    {
        private int _year = 1970;
        private int _month = 1;
        private int _day = 1;

        private int _hour = 0;
        private int _minute = 0;
        private int _second = 0;

        public Date()
        {
            Year = 1970;
            Month = 1;
            Day = 1;

            Hour = 0;
            Minute = 0;
            Second = 0;
        }

        public Date(int year, int month, int day, int hour, int minute, int second)
        {
            Year = year;
            Month = month;
            Day = day;

            Hour = hour;
            Minute = minute;
            Second = second;
        }

        public int Year
        {
            get { return _year; }
            set { _year = value; }
        }

        public int Month
        {
            get { return _month; }
            set
            {
                _month = value;
                if (_month < 1)
                    _month = 1;
                if (_month > 12)
                    _month = 12;
            }
        }

        public int Day
        {
            get { return _day; }
            set
            {
                _day = value;
                if (_day < 1)
                    _day = 1;
                if (_day > 31)
                    _day = 31;
            }
        }

        public int Hour
        {
            get { return _hour; }
            set
            {
                _hour = value;
                if (_hour < 0)
                    _hour = 0;
                if (_hour > 23)
                    _hour = 23;
            }
        }

        public int Minute
        {
            get { return _minute; }
            set
            {
                _minute = value;
                if (_minute < 0)
                    _minute = 0;
                if (_minute > 59)
                    _minute = 59;
            }
        }

        public int Second
        {
            get { return _second; }
            set
            {
                _second = value;
                if (_second < 0)
                    _second = 0;
                if (_second > 59)
                    _second = 59;
            }
        }

        public int GetDeltaSeconds(Date other)
        {
            int deltaSeconds = _second - other.Second;
            int deltaMinutes = _minute - other.Minute;
            int deltaHours = _hour - other.Hour;

            int deltaDays = _day - other.Day;
            int deltaMonths = _month - other.Month;
            int deltaYears = _year - other.Year;

            if (deltaSeconds < 0)
            {
                deltaSeconds = 60 - deltaSeconds;
                deltaMinutes--;
            }

            if (deltaMinutes < 0)
            {
                deltaMinutes = 60 - deltaMinutes;
                deltaHours--;
            }

            if (deltaHours < 0)
            {
                deltaHours = 24 - deltaHours;
                deltaDays--;
            }

            if (deltaDays < 0)
            {
                deltaDays = 31 - deltaDays;
                deltaMonths--;
            }

            if (deltaMonths < 0)
            {
                deltaMonths = 12 - deltaMonths;
                deltaYears--;
            }

            return deltaSeconds + (deltaMinutes*60) + (deltaHours*60*60) + (deltaDays*24*60*60) + (deltaMonths*31*24*60*60) + (deltaYears*365*24*60*60);
        }

        public String ToJSON()
        {
            String result = "";

            result = _year.ToString();
            result += "." + _month.ToString();
            result += "." + _day.ToString();
            
            result += " " + _hour.ToString();
            result += ":" + _minute.ToString();
            result += ":" + _second.ToString();

            return result;
        }

        public String ToHTTP()
        {
            String result = "";

            result += _year.ToString() + "-";
            if (_month < 10)
                result += "0";
            result += _month.ToString() + "-";
            if (_day < 10)
                result += "0";
            result += _day.ToString() + "T";

            //daytime
            if (_hour < 10)
                result += "0";
            result += _hour + "%3A";
            if (_minute < 10)
                result += "0";
            result += _minute + "%3A";
            if (_second < 10)
                result += "0";
            result += _second + "%2B";
            result += "00%3A00";

            return result;
        }
    }
}
