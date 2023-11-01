package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Placer;

/**
 * This is a simple teleop routine for testing localization. Drive the robot around like a normal
 * teleop routine and make sure the robot's estimated pose matches the robot's actual pose (slight
 * errors are not out of the ordinary, especially with sudden drive motions). The goal of this
 * exercise is to ascertain whether the localizer has been configured properly (note: the pure
 * encoder localizer heading may be significantly off if the track width has not been tuned).
 */


@TeleOp (group = "TeleOp")
public class Testing_Placer extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        Placer placer = new Placer(hardwareMap);
        boolean button_up_prev = false;
        boolean button_down_prev = false;
        boolean prev_a = false;
        boolean prev_y = false;

        int state = 0;
        final int state_max = Placer.PlacerState.values().length;
        Placer.PlacerState desired_state;

        waitForStart();

        while (!isStopRequested()) {
            boolean temp_up = gamepad2.dpad_up;
            boolean temp_down = gamepad2.dpad_down;
            boolean current_a = gamepad2.a;
            boolean current_y = gamepad2.y;


            if(!temp_up && button_up_prev) {
                state += 1;
            }
            button_up_prev = temp_up;

            if(!temp_down && button_down_prev) {
                state -= 1;
            }
            button_down_prev = temp_down;

            if (!current_a && prev_a && placer.place_level > 0) {
                placer.place_level -= 1;
            }
            if (!current_y && prev_y && placer.place_level < placer.PLACE_LEVEL_MAX){
                placer.place_level += 1;
            }

            prev_a = current_a;
            prev_y = current_y;

            if(state < 0 ) {
                state = 0;
            } else if (state > 5) {
                state = 0; //Wrap state in a circle
            }

            telemetry.addData("state", state);
            placer.log(telemetry);
            telemetry.update();

            switch(state) {
                case 0:
                    desired_state = Placer.PlacerState.READY_TO_INTAKE;
                    break;
                case 1:
                    desired_state = Placer.PlacerState.STOW;
                    break;
                case 2:
                    desired_state = Placer.PlacerState.DEPLOY;
                    break;
                case 3:
                    desired_state = Placer.PlacerState.PLACE_FIRST;
                    break;
                case 4:
                    desired_state = Placer.PlacerState.PLACE_SECOND;
                    break;
                case 5:
                    desired_state = Placer.PlacerState.READY_TO_CLIMB;
                    break;
                case 6:
                    desired_state = Placer.PlacerState.HANG;
                    break;
                default: desired_state = Placer.PlacerState.READY_TO_INTAKE; break;
            }
            placer.update(desired_state);

        }

    }
}
