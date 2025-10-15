package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
public class WristandGripper extends LinearOpMode {
    double waitTime = 1.0;
    double wristspeed = 0.001;
    boolean isBusyGrabbing;
    ElapsedTime runtime;
    DigitalChannel sensor;
    Servo handServo;
    Servo wristServo;
    double open = 1.0;
    double close = 0.4;
    int stage = 1;


    @Override
    public void runOpMode() throws InterruptedException {

        // This section registers hardware components
        wristServo = hardwareMap.get(Servo.class, "wrist_servo");
        handServo = hardwareMap.get(Servo.class, "hand_servo");
        sensor = hardwareMap.get(DigitalChannel.class, "sensor");
        sensor.setMode(DigitalChannel.Mode.INPUT);

        telemetry.addLine("init success");
        telemetry.update();

        // START
        waitForStart();

        handServo.setPosition(open);
        isBusyGrabbing = false;

        while (opModeIsActive()) {

            if (gamepad1.x) {
                isBusyGrabbing = true;
            }
            if (isBusyGrabbing) {
                grabTheBlock();
            }

            if (gamepad1.y) {
                dropTheBlock();
            }

            if (gamepad1.a) {
                double newPosition = wristServo.getPosition() + wristspeed;
                wristServo.setPosition(newPosition);
            }

            if (gamepad1.b) {
                double newPosition = wristServo.getPosition() - wristspeed;
                wristServo.setPosition(newPosition);
            }

        }
    }

    public void grabTheBlock() throws InterruptedException {

        if (runtime == null) {
            runtime = new ElapsedTime();
        }

        if (stage == 1) {

            handServo.setPosition(close);

            if (runtime.seconds() >= waitTime) {
                stage = 2;
                runtime.reset();
            }
        }


        if (stage == 2) {
            boolean detectObject = !sensor.getState();
            if (detectObject) {
                telemetry.addLine("Detected object");
                telemetry.update();
                resetGripper();
            } else {
                telemetry.addLine("Not detected");
                telemetry.update();
                runtime.reset();
                stage = 3;
            }
        }

        if (stage == 3) {
                telemetry.update();
                handServo.setPosition(open);

                if (runtime.seconds() >= waitTime) {
                    telemetry.update();
                    resetGripper();
                }


        }


    }

    public void resetGripper() throws InterruptedException {
        stage = 1;
        runtime = null;
        isBusyGrabbing = false;
    }
    public void dropTheBlock() throws InterruptedException {
        handServo.setPosition(open);

    }
}