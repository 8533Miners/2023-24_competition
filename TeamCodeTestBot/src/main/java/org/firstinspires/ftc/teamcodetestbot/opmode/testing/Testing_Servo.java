package org.firstinspires.ftc.teamcodetestbot.opmode.testing;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcodetestbot.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcodetestbot.subsystems.Gripper;

/**
 * This is a simple teleop routine for testing localization. Drive the robot around like a normal
 * teleop routine and make sure the robot's estimated pose matches the robot's actual pose (slight
 * errors are not out of the ordinary, especially with sudden drive motions). The goal of this
 * exercise is to ascertain whether the localizer has been configured properly (note: the pure
 * encoder localizer heading may be significantly off if the track width has not been tuned).
 */

@Disabled
@TeleOp (group = "TeleOp")
public class Testing_Servo extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        Gripper gripper = new Gripper(hardwareMap);
        boolean button_up_prev = false;
        boolean button_down_prev = false;
        int state = 0;
        final int state_max = Gripper.GripperState.values().length;
        Gripper.GripperState desired_state;

        waitForStart();

        while (!isStopRequested()) {
            boolean temp_up = gamepad2.dpad_up;
            boolean temp_down = gamepad2.dpad_down;

            if(!temp_up && button_up_prev) {
                state += 1;
            }
            button_up_prev = temp_up;

            if(!temp_down && button_down_prev) {
                state -= 1;
            }
            button_down_prev = temp_down;

            if(state < 0 ) {
                state = 0;
            } else if (state > 5) {
                state = 0; //Wrap state in a circle
            }

            telemetry.addData("state", state);
            telemetry.update();

            switch(state) {
                case 0: desired_state = Gripper.GripperState.READY; break;
                case 1: desired_state = Gripper.GripperState.STOWED; break;
                case 2: desired_state = Gripper.GripperState.TRANSFER; break;
                case 3: desired_state = Gripper.GripperState.SCORE_FIRST; break;
                case 4: desired_state = Gripper.GripperState.SCORE_SECOND; break;
                case 5: desired_state = Gripper.GripperState.RETURN; break;
                default: desired_state = Gripper.GripperState.READY; break;
            }
            gripper.update(desired_state);

        }
    }
}
