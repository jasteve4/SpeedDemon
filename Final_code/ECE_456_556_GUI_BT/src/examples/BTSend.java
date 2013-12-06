package examples;

import lejos.pc.comm.*;
import java.io.*;

public class BTSend {

    public BTSend(ECE556Frame control) {
        this.control = control;
        System.out.println(" BT Communication start");
    }

    public void connect(String name) {

        connectorNXT = new NXTConnector();

        // Connect to any NXT over Bluetooth
        boolean connected = connectorNXT.connectTo(name);

        if (!connected) {
            System.err.println("Failed to connect to any NXT");
            System.exit(1);
        }

        System.out.println("Connect to " + name);

        dataOut = new DataOutputStream(connectorNXT.getOutputStream());// .getDataOut();
        dataIn = new DataInputStream(connectorNXT.getInputStream());

        if (!reader.isRunning) {
            reader.start();
        }

    }

    public void send(String command_GUI) throws IOException {

        String command_data = command_GUI;

        if (command_data.equals("run")) {
            try {
                runcheck = true;
                System.out.println("Sending " + command_data);
                dataOut.writeInt(Command.RUN.ordinal());
                dataOut.flush();

            } catch (IOException ioe) {
                System.out.println("IO Exception writing bytes:");
                System.out.println(ioe.getMessage());

            }
        } else if (command_data.equals("connect")) {
            try {
                System.out.println("Sending " + command_data);
                dataOut.writeInt(Command.CONNECT.ordinal());
                dataOut.flush();

            } catch (IOException ioe) {
                System.out.println("IO Exception writing bytes:");
                System.out.println(ioe.getMessage());

            }

        } else if (command_data.equals("getR")) {
            try {
                System.out.println("Sending " + command_data);
                dataOut.writeInt(Command.GETR.ordinal());
                dataOut.flush();

            } catch (IOException ioe) {
                System.out.println("IO Exception writing bytes:");
                System.out.println(ioe.getMessage());

            }

            reader.reading = true;
        }
    }

    enum Command {

        CONNECT, RUN, GETR;
    }

    class Reader extends Thread {

        public boolean reading = false;
        int count = 0;
        boolean isRunning = false;

        public void run() {
            isRunning = true;
            while (isRunning) {
                if (reading) //reads one message at a time
                {
                    System.out.println("reading ");
                    float LL = 0;
                    float LM = 0;
                    float LR = 0;
                    float A = 0;
                    float B = 0;
                    float speed = 0;
                    boolean ok = false;
                    try {

                        LL = dataIn.readFloat();
                        LM = dataIn.readFloat();
                        LR = dataIn.readFloat();
                        A = dataIn.readFloat();
                        B = dataIn.readFloat();
                        ok = true;
                    } catch (IOException e) {
                        System.out.println("connection lost");
                        count++;
                        isRunning = count < 100;// give up
                        ok = false;
                    }
                    if (ok) {

                        if (!runcheck) {
                            control.LS = LL;
                            control.MS = LM;
                            control.RS = LR;
                            control.LeftLightSensorRef.setText(String.valueOf(LL));
                            control.MiddleLightSensorRef.setText(String.valueOf(LM));
                            control.RightLightSensorRef.setText(String.valueOf(LR));

                        } else {
                            speed = (A + B) / 2;
                            control.speedV.add(speed);
                            control.leftSensorVector.add(LL);
                            control.middleSensorVector.add(LM);
                            control.rightSensorVector.add(LR);
                            control.LeftLightSensorIns.setText(String.valueOf(LL));
                            control.MiddleLightSensorIns.setText(String.valueOf(LM));
                            control.RightLightSensorIns.setText(String.valueOf(LR));
                            control.SpeedIns.setText(String.valueOf(speed));

                        }
                        //reading = false;
                        System.out.println("data  " + LL + " " + LM + " " + LR + " " + A + " " + B + " " + speed);
                    }
                }

                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                }
            }// if reading
        }//while is running

    }

    private NXTConnector connectorNXT;
    private DataInputStream dataIn;
    private DataOutputStream dataOut;
    private boolean runcheck = false;
    private final Reader reader = new Reader();
    private final ECE556Frame control;

}
