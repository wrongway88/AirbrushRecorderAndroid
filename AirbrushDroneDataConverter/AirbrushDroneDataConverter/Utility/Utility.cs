using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Globalization;
using System.Text.RegularExpressions;

namespace AirbrushDroneDataConverter.Utility
{
    class Utility
    {
        private static readonly Regex rxScientific = new Regex(@"^(?<sign>-?)(?<head>\d+)(\.(?<tail>\d*?)0*)?E(?<exponent>[+\-]\d+)$", RegexOptions.IgnoreCase | RegexOptions.ExplicitCapture | RegexOptions.CultureInvariant);

        public static void CartesianToPolar(float x, float y, out float r, out float angle)
        {
            r = (float)Math.Sqrt((double)(x * x + y * y));

            if (Math.Abs(x) > 0.0000001)
            {
                if (Math.Abs(y) > 0.0000001)
                {
                    angle = (float)Math.Atan(y / x);
                }
                else
                {
                    if (x > 0.0)
                    {
                        angle = 0.0f;
                    }
                    else
                    {
                        angle = (float)Math.PI;
                    }
                }
            }
            else
            {
                if (y > 0.0)
                {
                    angle = (float)Math.PI * 0.5f;
                }
                else if (y < 0.0)
                {
                    angle = (float)Math.PI * -0.5f;
                }
                else
                {
                    angle = 0.0f;
                }
            }

            if (angle < 0.0)
            {
                angle = (float)Math.PI + (-angle);
            }
        }

        public static string ToFloatingPointString(double value, NumberFormatInfo formatInfo)
        {
            string result = value.ToString("r", NumberFormatInfo.InvariantInfo);
            Match match = rxScientific.Match(result);
            if (match.Success)
            {
                //Debug.WriteLine("Found scientific format: {0} => [{1}] [{2}] [{3}] [{4}]", result, match.Groups["sign"], match.Groups["head"], match.Groups["tail"], match.Groups["exponent"]);
                int exponent = int.Parse(match.Groups["exponent"].Value, NumberStyles.Integer, NumberFormatInfo.InvariantInfo);
                StringBuilder builder = new StringBuilder(result.Length + Math.Abs(exponent));
                builder.Append(match.Groups["sign"].Value);
                if (exponent >= 0)
                {
                    builder.Append(match.Groups["head"].Value);
                    string tail = match.Groups["tail"].Value;
                    if (exponent < tail.Length)
                    {
                        builder.Append(tail, 0, exponent);
                        builder.Append(formatInfo.NumberDecimalSeparator);
                        builder.Append(tail, exponent, tail.Length - exponent);
                    }
                    else
                    {
                        builder.Append(tail);
                        builder.Append('0', exponent - tail.Length);
                    }
                }
                else
                {
                    builder.Append('0');
                    builder.Append(formatInfo.NumberDecimalSeparator);
                    builder.Append('0', (-exponent) - 1);
                    builder.Append(match.Groups["head"].Value);
                    builder.Append(match.Groups["tail"].Value);
                }
                result = builder.ToString();
            }
            return result;
        }
    }
}