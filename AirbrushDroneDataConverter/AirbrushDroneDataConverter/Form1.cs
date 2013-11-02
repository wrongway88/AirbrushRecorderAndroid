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
using AirbrushDroneDataConverter.Utility;

namespace AirbrushDroneDataConverter
{
    public partial class Form1 : Form
    {
        private List<Flight.Flight> _flights = new List<Flight.Flight>();

        public Form1()
        {
            InitializeComponent(); 
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
                LoginData loginData = LoginData.LoadLoginData();
                if (loginData.MailAddress.Length > 0 && loginData.Password.Length > 0)
                {
                    int userId = WebInterface.WebInterface.GetUserID(loginData.MailAddress);
                    if (userId > -1)
                    {
                        loginData.UserId = userId;
                        WebInterface.WebInterface.Login(userId, Utility.Utility.SaltPassword(loginData.Password, loginData.MailAddress));
                        LoginData.SaveLoginData(loginData);
                    }
                    else
                    {
                        MessageBox.Show("Login failed, invalid login data!");
                        FormLoginData loginDataForm = new FormLoginData();
                        loginDataForm.Show();
                        return;
                    }
                }
                else
                {
                    MessageBox.Show("Set Login Data first");
                    FormLoginData loginDataForm = new FormLoginData();
                    loginDataForm.Show();
                    return;
                }

                if (selectedIndex >= 0 && selectedIndex < _flights.Count)
                {
                    Flight.Flight flight = _flights[selectedIndex];
                    WebInterface.WebInterface.PostFlight(flight);
                }
            }
        }

        private void loadToolStripMenuItem_Click(object sender, EventArgs e)
        {
            FolderBrowserDialog fbd = new FolderBrowserDialog();
            DialogResult result = fbd.ShowDialog();

            if (fbd.SelectedPath.Length > 0)
            {
                string[] files = Directory.GetFiles(fbd.SelectedPath);
                openFiles(files);
            }
        }

        private void preferencesToolStripMenuItem_Click(object sender, EventArgs e)
        {
            //WebInterface.WebInterface.GetUserID("test@test.com");

            FormLoginData loginDataForm = new FormLoginData();
            loginDataForm.Show();
        }
    }
}
