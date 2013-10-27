using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

using AirbrushDroneDataConverter.Utility;

namespace AirbrushDroneDataConverter
{
    public partial class FormLoginData : Form
    {
        public FormLoginData()
        {
            InitializeComponent();

            propertyGridLoginData.SelectedObject = LoginData.LoadLoginData();
        }

        private void FormLoginData_Close(object sender, EventArgs e)
        {
            LoginData loginData = propertyGridLoginData.SelectedObject as LoginData;

            if (loginData != null)
            {
                LoginData.SaveLoginData(loginData);
            }
        }
    }
}
