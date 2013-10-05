using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.IO;
using AirbrushDroneDataConverter.DroneData;

namespace AirbrushDroneDataConverter
{
    public partial class Form1 : Form
    {
        private List<Flight.Flight> _flights = new List<Flight.Flight>();

        public Form1()
        {
            InitializeComponent(); 
        }

        public void onButtonOpenDirectoryPressed()
        {

        }

        private void buttonOpenDirectory_Click(object sender, EventArgs e)
        {
            FolderBrowserDialog fbd = new FolderBrowserDialog();
            DialogResult result = fbd.ShowDialog();

            string[] files = Directory.GetFiles(fbd.SelectedPath);
            //System.Windows.Forms.MessageBox.Show("Files found: " + files.Length.ToString(), "Message");

            openFiles(files);
        }

        private void openFiles(string[] filePaths)
        {
            foreach (string filePath in filePaths)
            {
                string ending = filePath.Substring(filePath.Length - 3, 3);

                if(ending == "log")
                {
                    StreamReader file = new StreamReader(filePath);

                    String fileName = filePath.Substring(filePath.LastIndexOf("\\")+1);
                    _flights.AddRange(DroneDataConverter.ConvertFile(file, fileName));

                    //MessageBox.Show(filePath + ": " + flights.Count);

                    /*
                    int i = 0;
                    foreach (Flight.Flight flight in flights)
                    {
                        i++;
                        if(i > 1)
                            WebInterface.WebInterface.PostFlight(flight);
                        //MessageBox.Show(flight.ToJSON());
                    }
                    */
                }
            }

            fillFlightList(_flights);
        }

        private void fillFlightList(List<Flight.Flight> flights)
        {
            int idx = 0;
            foreach(Flight.Flight flight in flights)
            {
                ListViewItem item = this.listViewFlights.Items.Add(new ListViewItem(idx.ToString()));
                item.SubItems.Add(flight.AirplaneRegistration);
                item.SubItems.Add(flight.Date.ToJSON());
                item.SubItems.Add(flight.GetWaypointCount().ToString());
                idx++;
            }
        }

        private void listViewFlights_DoubleClick(object sender, EventArgs e)
        {
            //MessageBox.Show("selected " + listViewFlights.SelectedItems[0].SubItems[0].Text);
            int selectedIndex = listViewFlights.SelectedItems[0].Index;

            DialogResult result = MessageBox.Show("Do you want to upload the selected flight?", "Upload", MessageBoxButtons.YesNo);

            if (result == DialogResult.Yes)
            {
                if (selectedIndex >= 0 && selectedIndex < _flights.Count)
                {
                    Flight.Flight flight = _flights[selectedIndex];
                    WebInterface.WebInterface.PostFlight(flight);
                }
            }
        }

        private void buttonTestLogin_Click(object sender, EventArgs e)
        {
            WebInterface.WebInterface.Login(3, "test");
        }
    }
}
