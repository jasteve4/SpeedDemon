/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ECE556Frame.java
 *
 * Created on 2011-9-2, 12:27:41
 */
package examples;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;
import javax.swing.JFileChooser;

/**
 *
 * @author ECE556 TA Team
 */
public class ECE556Frame extends javax.swing.JFrame {

    /**
     * Creates new form ECE556Frame
     */
    public ECE556Frame() {
        initComponents();
        // init all the parameters, the definitions of the parameters are in the variable declaration part
        LS = 0;
        MS = 0;
        RS = 0;

        LSR = 0;
        MSR = 0;
        RSR = 0;

        leftSensorVector = new Vector(10, 1);
        middleSensorVector = new Vector(10, 1);
        rightSensorVector = new Vector(10, 1);

        eLS = 0;
        eMS = 0;
        eRS = 0;
        samplingTime = 0.2;
        C2 = 0;
        speed = 0;
        speedV = new Vector(10, 1);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        SaveButton = new javax.swing.JButton();
        RunButton = new javax.swing.JButton();
        GetReferenceButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        ConnectButton = new javax.swing.JButton();
        GetC2Button = new javax.swing.JButton();
        txtNXTName = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        LeftLightSensorRef = new javax.swing.JTextField();
        MiddleLightSensorRef = new javax.swing.JTextField();
        RightLightSensorRef = new javax.swing.JTextField();
        LeftLightSensorIns = new javax.swing.JTextField();
        MiddleLightSensorIns = new javax.swing.JTextField();
        RightLightSensorIns = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        C2Value = new javax.swing.JTextField();
        SpeedIns = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        SaveButton.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        SaveButton.setText("Save Speed Data");
        SaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveButtonActionPerformed(evt);
            }
        });
        jPanel1.add(SaveButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 320, 191, 33));

        RunButton.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        RunButton.setText("Run");
        RunButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RunButtonActionPerformed(evt);
            }
        });
        jPanel1.add(RunButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 191, 33));

        GetReferenceButton.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        GetReferenceButton.setText("Get Reference Value");
        GetReferenceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GetReferenceButtonActionPerformed(evt);
            }
        });
        jPanel1.add(GetReferenceButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, -1, 33));

        jLabel2.setText("Control Panel");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 2, 87, -1));

        ConnectButton.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        ConnectButton.setText("Connect");
        ConnectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConnectButtonActionPerformed(evt);
            }
        });
        jPanel1.add(ConnectButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 191, 33));

        GetC2Button.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        GetC2Button.setText("Get C2 Value");
        GetC2Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GetC2ButtonActionPerformed(evt);
            }
        });
        jPanel1.add(GetC2Button, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 260, 191, 33));

        txtNXTName.setText("Blue");
        txtNXTName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNXTNameActionPerformed(evt);
            }
        });
        jPanel1.add(txtNXTName, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 190, -1));

        jLabel9.setText("NXT BT Name");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 190, -1));

        jPanel5.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, 360));

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel12.setText("Instantaneous Value");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, -1, -1));

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel11.setText("Light Sensor Reading");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel4.setText("Instantaneous Speed");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, -1, -1));

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel7.setText("Middle");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 10, -1, -1));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel5.setText("Left");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel6.setText("Right");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 10, -1, -1));

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 40, 311, -1));

        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        LeftLightSensorRef.setEditable(false);
        jPanel4.add(LeftLightSensorRef, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 80, -1));

        MiddleLightSensorRef.setEditable(false);
        jPanel4.add(MiddleLightSensorRef, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 80, -1));

        RightLightSensorRef.setEditable(false);
        jPanel4.add(RightLightSensorRef, new org.netbeans.lib.awtextra.AbsoluteConstraints(239, 10, 80, -1));

        LeftLightSensorIns.setEditable(false);
        jPanel4.add(LeftLightSensorIns, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 80, -1));

        MiddleLightSensorIns.setEditable(false);
        jPanel4.add(MiddleLightSensorIns, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, 80, -1));

        RightLightSensorIns.setEditable(false);
        jPanel4.add(RightLightSensorIns, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 80, 80, -1));

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 110, -1, 110));

        jLabel3.setText("Display Panel");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 2, 87, -1));

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel13.setText("Reference Value");
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 120, -1, -1));

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel8.setText("C2 Value");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 320, -1, -1));

        C2Value.setEditable(false);
        jPanel2.add(C2Value, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 310, 110, 40));

        SpeedIns.setEditable(false);
        jPanel2.add(SpeedIns, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 250, 110, 40));

        jPanel5.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(242, 60, 540, 360));

        jLabel1.setBackground(new java.awt.Color(204, 0, 0));
        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(204, 0, 0));
        jLabel1.setText("ECE 556 Final Project GUI");
        jPanel5.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 10, 503, -1));

        getContentPane().add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 790, 440));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void SaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveButtonActionPerformed
        final JFileChooser fc = new JFileChooser();
        fc.showSaveDialog(fc);
        if (fc.getSelectedFile() != null) {
            try {

                BufferedWriter bw = new BufferedWriter(new FileWriter(fc.getSelectedFile(), true));
                bw.write("Reference values:\n");
                bw.append(String.valueOf(LS));
                bw.append("\t");
                bw.append(String.valueOf(MS));
                bw.append("\t");
                bw.append(String.valueOf(RS));
                 bw.write("\r\n");
                bw.write("Instantaneous Values:\r\n");
                for (int i = 0; i < leftSensorVector.size(); i++) {
                    //normalize the values 0-100 scale
                    String LSi = String.valueOf(leftSensorVector.get(i));
                    String MSi = String.valueOf(middleSensorVector.get(i));
                    String RSi = String.valueOf(rightSensorVector.get(i));
                    bw.append(LSi);
                    bw.append("\t");
                    bw.append(MSi);
                    bw.append("\t");
                    bw.append(RSi);
                    bw.append("\r\n");
                }
                bw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}//GEN-LAST:event_SaveButtonActionPerformed

    private void RunButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RunButtonActionPerformed
        // TODO add your handling code here: functions when the Run Button is clicked.

        try {
            communicator.send("run");
        } catch (IOException ex) {
            Logger.getLogger(ECE556Frame.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Display the instantaneous values of light sensors in the GUI
        LeftLightSensorIns.setText(String.valueOf(LSR));
        MiddleLightSensorIns.setText(String.valueOf(MSR));
        RightLightSensorIns.setText(String.valueOf(RSR));
        SpeedIns.setText(String.valueOf(speed));
    }//GEN-LAST:event_RunButtonActionPerformed

    private void ConnectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConnectButtonActionPerformed
        // TODO add your handling code here: functions (establish the bluetooth connection with robot) when the Connect Button is clicked.
        NXTName = txtNXTName.getText();
        communicator.connect(NXTName);
        try {
            communicator.send("connect");
        } catch (IOException ex) {
            Logger.getLogger(ECE556Frame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_ConnectButtonActionPerformed

    private void GetReferenceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GetReferenceButtonActionPerformed
        // TODO add your handling code here: functions when the GetRefernece Button is clicked.
        try {
            communicator.send("getR");
        } catch (IOException ex) {
            Logger.getLogger(ECE556Frame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_GetReferenceButtonActionPerformed

    public double getMaximumValue() {
        double max = 0;

        for (int i = 0; i < leftSensorVector.size(); i++) {
            double temp = Double.valueOf(String.valueOf(leftSensorVector.get(i)));
            if (temp > max) {
                max = temp;
            }
        }
        for (int i = 0; i < middleSensorVector.size(); i++) {
            double temp = Double.valueOf(String.valueOf(middleSensorVector.get(i)));
            if (temp > max) {
                max = temp;
            }
        }
        for (int i = 0; i < rightSensorVector.size(); i++) {
            double temp = Double.valueOf(String.valueOf(rightSensorVector.get(i)));
            if (temp > max) {
                max = temp;
            }
        }
        return max;
    }

    public double getMinimumValue() {
        double min = 0;
        for (int i = 0; i < leftSensorVector.size(); i++) {
            double temp = Double.valueOf(String.valueOf(leftSensorVector.get(i)));
            if (temp < min) {
                min = temp;
            }
        }
        for (int i = 0; i < middleSensorVector.size(); i++) {
            double temp = Double.valueOf(String.valueOf(middleSensorVector.get(i)));
            if (temp < min) {
                min = temp;
            }
        }
        for (int i = 0; i < rightSensorVector.size(); i++) {
            double temp = Double.valueOf(String.valueOf(rightSensorVector.get(i)));
            if (temp < min) {
                min = temp;
            }
        }
        return min;
    }

    private void GetC2ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GetC2ButtonActionPerformed

        C2 = 0;
        //get the range of values
        double range = Math.abs(getMaximumValue() - getMinimumValue());
        double N = 0;
        double LSi, MSi, RSi;

        //to avoid divide by zero errors
        if (range == 0) {
            range = 1;
        }

        //normalize the reference values between 0 - 100
        LS = LS * 100 / range;
        RS = RS * 100 / range;
        MS = MS * 100 / range;

        for (int i = 0; i < leftSensorVector.size(); i++) {
            //normalize the values 0-100 scale
            LSi = Double.valueOf(String.valueOf(leftSensorVector.get(i))) * 100 / range;
            MSi = Double.valueOf(String.valueOf(middleSensorVector.get(i))) * 100 / range;
            RSi = Double.valueOf(String.valueOf(rightSensorVector.get(i))) * 100 / range;

            //Calculate errors for LightSensors
            eLS = (LS - LSi) * (LS - LSi);
            eRS = (RS - RSi) * (RS - RSi);
            eMS = (MS - MSi) * (MS - MSi);
            C2 = C2 + (eMS + eRS + eLS) * (samplingTime / range);
            N = N + 1.0;

        }
        if (N > 0) {
            C2 = C2 / N;
        }
        String temp1 = String.valueOf(C2);
        if (temp1.length() > 8) {
            temp1 = temp1.substring(0, 8);
        }
        C2Value.setText(temp1);
    }//GEN-LAST:event_GetC2ButtonActionPerformed

    private void txtNXTNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNXTNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNXTNameActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ECE556Frame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField C2Value;
    private javax.swing.JButton ConnectButton;
    private javax.swing.JButton GetC2Button;
    private javax.swing.JButton GetReferenceButton;
    public javax.swing.JTextField LeftLightSensorIns;
    public javax.swing.JTextField LeftLightSensorRef;
    public javax.swing.JTextField MiddleLightSensorIns;
    public javax.swing.JTextField MiddleLightSensorRef;
    protected javax.swing.JTextField RightLightSensorIns;
    public javax.swing.JTextField RightLightSensorRef;
    private javax.swing.JButton RunButton;
    private javax.swing.JButton SaveButton;
    public javax.swing.JTextField SpeedIns;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JTextField txtNXTName;
    // End of variables declaration//GEN-END:variables
    public double LS, MS, RS;                //Variables for the reference values of light sensors
    private double LSR, MSR, RSR;             //Variables for the instantaneous values of light sensors
    public Vector leftSensorVector, middleSensorVector, rightSensorVector;          //Vectors for storing the instantaneous values of light sensors
    private double eLS, eMS, eRS;             //Variables for the Tracking errors
    private double samplingTime;              //Sampling time for calculating C2
    private double C2;                        //Variable for C2
    private double speed;                     //Variable for the instantaneous speed of the robot
    public Vector speedV;                    //Vector for storing the instantaneous speed of the robot
    public String NXTName;
    private BTSend communicator = new BTSend(this);

}
