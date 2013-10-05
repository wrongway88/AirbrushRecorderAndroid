namespace AirbrushDroneDataConverter
{
    partial class Form1
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.buttonOpenDirectory = new System.Windows.Forms.Button();
            this.listViewFlights = new System.Windows.Forms.ListView();
            this.id = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
            this.airplane = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
            this.date = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
            this.waypointCount = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
            this.buttonTestLogin = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // buttonOpenDirectory
            // 
            this.buttonOpenDirectory.Location = new System.Drawing.Point(12, 12);
            this.buttonOpenDirectory.Name = "buttonOpenDirectory";
            this.buttonOpenDirectory.Size = new System.Drawing.Size(107, 23);
            this.buttonOpenDirectory.TabIndex = 0;
            this.buttonOpenDirectory.Text = "Open Directory";
            this.buttonOpenDirectory.UseVisualStyleBackColor = true;
            this.buttonOpenDirectory.Click += new System.EventHandler(this.buttonOpenDirectory_Click);
            // 
            // listViewFlights
            // 
            this.listViewFlights.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
            this.id,
            this.airplane,
            this.date,
            this.waypointCount});
            this.listViewFlights.Location = new System.Drawing.Point(12, 41);
            this.listViewFlights.Name = "listViewFlights";
            this.listViewFlights.Size = new System.Drawing.Size(482, 283);
            this.listViewFlights.TabIndex = 1;
            this.listViewFlights.UseCompatibleStateImageBehavior = false;
            this.listViewFlights.View = System.Windows.Forms.View.Details;
            this.listViewFlights.DoubleClick += new System.EventHandler(this.listViewFlights_DoubleClick);
            // 
            // id
            // 
            this.id.Tag = "Id";
            // 
            // airplane
            // 
            this.airplane.Tag = "airplane";
            this.airplane.Width = 140;
            // 
            // date
            // 
            this.date.Tag = "date";
            this.date.Width = 140;
            // 
            // waypointCount
            // 
            this.waypointCount.Tag = "waypoints";
            this.waypointCount.Width = 140;
            // 
            // buttonTestLogin
            // 
            this.buttonTestLogin.Location = new System.Drawing.Point(125, 12);
            this.buttonTestLogin.Name = "buttonTestLogin";
            this.buttonTestLogin.Size = new System.Drawing.Size(75, 23);
            this.buttonTestLogin.TabIndex = 2;
            this.buttonTestLogin.Text = "Login";
            this.buttonTestLogin.UseVisualStyleBackColor = true;
            this.buttonTestLogin.Click += new System.EventHandler(this.buttonTestLogin_Click);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(506, 336);
            this.Controls.Add(this.buttonTestLogin);
            this.Controls.Add(this.listViewFlights);
            this.Controls.Add(this.buttonOpenDirectory);
            this.Name = "Form1";
            this.Text = "Form1";
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Button buttonOpenDirectory;
        private System.Windows.Forms.ListView listViewFlights;
        private System.Windows.Forms.ColumnHeader airplane;
        private System.Windows.Forms.ColumnHeader date;
        private System.Windows.Forms.ColumnHeader waypointCount;
        private System.Windows.Forms.ColumnHeader id;
        private System.Windows.Forms.Button buttonTestLogin;
    }
}

