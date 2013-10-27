namespace AirbrushDroneDataConverter
{
    partial class FormLoginData
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
            this.propertyGridLoginData = new System.Windows.Forms.PropertyGrid();
            this.SuspendLayout();
            // 
            // propertyGridLoginData
            // 
            this.propertyGridLoginData.Location = new System.Drawing.Point(12, 12);
            this.propertyGridLoginData.Name = "propertyGridLoginData";
            this.propertyGridLoginData.Size = new System.Drawing.Size(300, 173);
            this.propertyGridLoginData.TabIndex = 0;
            // 
            // FormLoginData
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(324, 197);
            this.Controls.Add(this.propertyGridLoginData);
            this.Name = "FormLoginData";
            this.Text = "FormLoginData";
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.FormLoginData_Close);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.PropertyGrid propertyGridLoginData;



    }
}