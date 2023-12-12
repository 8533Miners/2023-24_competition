package org.firstinspires.ftc.teamcode.drive.opmode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.util.Encoder;
@Disabled
@TeleOp(group = "drive")
public class Testing_outputEncoderValues extends LinearOpMode {
    private Encoder leftEncoder, rightEncoder, frontEncoder;

    public void runOpMode() throws InterruptedException{

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "front_left"));
        rightEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "front_right"));
        frontEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "back_left"));

        rightEncoder.setDirection(Encoder.Direction.REVERSE);
        leftEncoder.setDirection(Encoder.Direction.REVERSE);

        waitForStart();


        while (opModeIsActive() && !isStopRequested()) {
            drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            drive.setWeightedDrivePower(
                    new Pose2d(
                            -gamepad1.left_stick_y,
                            -gamepad1.left_stick_x,
                            -gamepad1.right_stick_x
                    )
            );
            telemetry.addData("Left Encoder position", leftEncoder.getCurrentPosition());
            telemetry.addData("Right Encoder position", rightEncoder.getCurrentPosition());
            telemetry.addData("Front Encoder position", frontEncoder.getCurrentPosition());
            telemetry.addData("Left encoder correct velocity", leftEncoder.getCorrectedVelocity());
            telemetry.addData("Right encoder correct velocity", rightEncoder.getCorrectedVelocity());
            telemetry.addData("Front encoder correct velocity", frontEncoder.getCorrectedVelocity());
            telemetry.addLine();

            telemetry.update();

            drive.update();
        }
    }

}
