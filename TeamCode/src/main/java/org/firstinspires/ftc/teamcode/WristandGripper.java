package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
public class WristandGripper extends LinearOpMode {
    ElapsedTime runtime;
    DigitalChannel sensor;
    Servo handServo;
    Servo wristServo;
    double open = 1.0;
    double close = 0.0;
    int stage = 1;

    long stageStart = 0;

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


        while (opModeIsActive()) {

            if (gamepad1.x) {
                grabTheBlock();
            }

            if (gamepad1.y) {
                dropTheBlock();
            }

            if (gamepad1.a) {
                double newPosition = wristServo.getPosition() + 0.001;
                wristServo.setPosition(newPosition);
            }

            if (gamepad1.b) {
                double newPosition = wristServo.getPosition() - 0.001;
                wristServo.setPosition(newPosition);
            }
        }
        telemetry.addLine("stopped");
        telemetry.update();
    }

    public void grabTheBlock() throws InterruptedException {

        telemetry.addData("Stage: ", stage);
        if (runtime == null) {
            runtime = new ElapsedTime();
        }

        telemetry.addLine("Grabbing block");
        if (stage == 1) {

            telemetry.addLine("Stage 1");
            handServo.setPosition(close);
            telemetry.addData("the seconds are: " , runtime.seconds());
            telemetry.update();

            if (runtime.seconds() >= 1.0) {
                telemetry.addLine("seconds");
                telemetry.update();
                stage = 2;
                runtime.reset();
            }

        }

        if (stage == 2) {
            telemetry.addLine("Stage 2");
            handServo.setPosition(open);
            telemetry.update();

            if (runtime.seconds() >= 1.0) {
                telemetry.addLine("seconds");
                telemetry.update();
                stage = 3;
                runtime.reset();
            }

        }

        if (stage == 3) {
            telemetry.addLine("Stage 3");
            handServo.setPosition(close);

            if (runtime.seconds() >= 1.0) {
                telemetry.addLine("seconds");
                telemetry.update();
                stage = 1;
                runtime.reset();
            }
        }


        //    telemetry.addLine("X is pressed.");
//
//    handServo.setPosition(close);
//
//    Thread.sleep(1000);
//
//    boolean detectObject = sensor.getState();
//    telemetry.addData("Detected object", detectObject);
//
//    if (!detectObject) {
//        handServo.setPosition(open);
//        Thread.sleep(1000);
//        handServo.setPosition(close);
//        Thread.sleep(1000);
//    }
//    telemetry.update();
    }

    public void dropTheBlock() throws InterruptedException {
        handServo.setPosition(open);

        telemetry.addLine("Y is pressed.");
        telemetry.update();
    }
}