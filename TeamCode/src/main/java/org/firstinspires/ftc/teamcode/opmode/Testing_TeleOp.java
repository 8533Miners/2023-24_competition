package org.firstinspires.ftc.teamcode.opmode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.Gripper;

/**
 * This is a simple teleop routine for testing localization. Drive the robot around like a normal
 * teleop routine and make sure the robot's estimated pose matches the robot's actual pose (slight
 * errors are not out of the ordinary, especially with sudden drive motions). The goal of this
 * exercise is to ascertain whether the localizer has been configured properly (note: the pure
 * encoder localizer heading may be significantly off if the track width has not been tuned).
 */


@TeleOp (group = "TeleOp")
public class Testing_TeleOp extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        boolean is_field_oriented = false;
        boolean left_bumper_prev = false;

        //drive is declared in the SampleMecanumDrive
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        Gripper gripper = new Gripper(hardwareMap);

        waitForStart();

        while (!isStopRequested()) {
            boolean left_bumper_cur = gamepad1.left_bumper;
            if(left_bumper_prev && !left_bumper_cur) {is_field_oriented = !is_field_oriented;}
            left_bumper_prev = left_bumper_cur;

            float controller_forward = -gamepad1.left_stick_y;
            float controller_strafe = -gamepad1.left_stick_x;
            float controller_rotation = -gamepad1.right_stick_x;
            double robot_heading = drive.getExternalHeading();

            Pose2d field_oriented;
            if(is_field_oriented) {
                field_oriented = new Pose2d(
                        ((Math.cos(robot_heading) * controller_forward) -
                                (Math.sin(robot_heading) * controller_strafe)),
                        ((Math.sin(robot_heading) * controller_forward) +
                                (Math.cos(robot_heading) * controller_strafe)),
                        (controller_rotation)
                );
            } else {
                field_oriented = new Pose2d(
                        controller_forward,
                        controller_strafe,
                        controller_rotation
                );
            }
            drive.setWeightedDrivePower(field_oriented);

            drive.update();
            //gripper.update(gamepad2.dpad_up,gamepad2.dpad_down,gamepad2.right_bumper);

            Pose2d poseEstimate = drive.getPoseEstimate();
            telemetry.addData("x", poseEstimate.getX());
            telemetry.addData("y", poseEstimate.getY());
            telemetry.addData("heading", poseEstimate.getHeading());
            telemetry.addData("field_oriented", is_field_oriented);
            telemetry.update();
        }


    }
}
