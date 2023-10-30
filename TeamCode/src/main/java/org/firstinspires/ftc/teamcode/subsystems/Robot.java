package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

public class Robot {
    private Picker picker;
    private Placer placer;
    private DroneLauncher drone_launcher;
    private SampleMecanumDrive drive;
    private RobotState robot_state = RobotState.READY_TO_INTAKE;
    private boolean is_optr_dpad_up_prev = false;
    private boolean is_optr_dpad_dwn_prev = false;
    private boolean is_place_second_pixel_command_prev = false;
    public enum RobotState {
        READY_TO_INTAKE,
        HOLDING_PIXELS,
        READY_TO_SCORE,
        READY_TO_CLIMB,
        HANGING
    }
    public Robot(HardwareMap hardwareMap) {
        picker = new Picker(hardwareMap);
        placer = new Placer(hardwareMap);
        drone_launcher = new DroneLauncher(hardwareMap);
        drive = new SampleMecanumDrive(hardwareMap);
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    public void updateTeleOp(Gamepad driver_controller, Gamepad operator_controller, Telemetry telemetry) {
        Picker.PickerState new_picker_state;
        Placer.PlacerState new_placer_state;

        double forward = -driver_controller.left_stick_y;
        double strafe = -driver_controller.left_stick_x;
        double rotation = -driver_controller.right_stick_x;

        boolean is_optr_dpad_up_cur = operator_controller.dpad_up;
        boolean is_optr_dpad_dwn_cur = operator_controller.dpad_down;
        boolean is_dvr_rgt_trg = driver_controller.right_trigger > 0.5f;
        boolean is_optr_rgt_trg = operator_controller.right_trigger > 0.5f;
        boolean is_dvr_lft_bpr = driver_controller.left_bumper;
        boolean is_dvr_rgt_bpr = driver_controller.right_bumper;
        boolean is_optr_a = operator_controller.a;
        boolean is_optr_b = operator_controller.b;
        boolean is_drv_a = driver_controller.a;
        boolean is_drv_b = driver_controller.b;
        boolean is_drv_y = driver_controller.y;
        boolean is_dvr_lft_trg = driver_controller.left_trigger > 0.5f;
        boolean is_optr_lft_trg = operator_controller.left_trigger > 0.5f;
        boolean is_dvr_start = driver_controller.start;
        boolean is_dvr_back = driver_controller.back;
        boolean is_optr_back = operator_controller.back;

        boolean is_outake_command = is_dvr_lft_bpr;
        boolean is_intake_command = is_dvr_rgt_bpr;

        boolean is_hold_pixel_command = is_optr_a;
        boolean is_cancel_hold_command = is_optr_b;

        //When dpad_up is initially pressed down
        boolean is_prepare_to_score_command = is_optr_dpad_up_cur && !is_optr_dpad_up_prev;
        //When dpad_up is let go from press
        boolean is_place_level_increment_command = !is_optr_dpad_up_cur && is_optr_dpad_up_prev;
        //When dpad_down is let go from press
        boolean is_place_level_decrement_command = !is_optr_dpad_dwn_cur && is_optr_dpad_dwn_prev;
        boolean is_place_first_pixel_command = is_drv_a;
        boolean is_place_second_pixel_command = is_drv_y;
        boolean is_cancel_score_command = is_drv_b && is_optr_b;
        //When is_place_second_pixel_command is let go from being active
        boolean is_score_done_command = !is_place_second_pixel_command &&
                                         is_place_second_pixel_command_prev;

        boolean is_prepare_to_climb_command = is_dvr_start;
        boolean is_climb_command = is_dvr_lft_trg && is_optr_lft_trg;
        boolean is_climb_cancel_command = is_drv_b && is_optr_b;

        boolean is_drone_launch_command = is_dvr_rgt_trg && is_optr_rgt_trg;

        boolean is_safe_state_command = is_dvr_back && is_optr_back;

        //Picker control
        //Driver has picker control since driving into pieces will be needed to intake
        //Picker is controlled independent of robot state
        //TODO consider running intake when in the READY_TO_INTAKE state always and only
        //OUTAKE takes priority
        if(is_outake_command) {
            new_picker_state = Picker.PickerState.OUTAKE;
        } else if (is_intake_command) {
            new_picker_state = Picker.PickerState.INTAKE;
        } else {
            //default state is safe state HOLD
            new_picker_state = Picker.PickerState.HOLD;
        }

        //Placer next state is dependent of robot_state
        switch (robot_state) {
            default:
                //error case, fall into safe state READY_TO_INTAKE
            case READY_TO_INTAKE:
                //Command to prepare climb take priority
                if (is_prepare_to_climb_command) {
                    //Hand from any state
                    new_placer_state = Placer.PlacerState.READY_TO_CLIMB;
                    robot_state = RobotState.READY_TO_CLIMB;
                } else if (is_hold_pixel_command) {
                    //Operator tells robot to hold acquired pixels
                    //NOTE: if done prematurely the claw will block pixels intaking properly
                    new_placer_state = Placer.PlacerState.STOW;
                    robot_state = RobotState.HOLDING_PIXELS;
                } else {
                    new_placer_state = Placer.PlacerState.READY_TO_INTAKE;
                }
                break;
            case HOLDING_PIXELS:
                //Command to prepare climb take priority
                if (is_prepare_to_climb_command) {
                    new_placer_state = Placer.PlacerState.READY_TO_CLIMB;
                    robot_state = RobotState.READY_TO_CLIMB;
                } else if (is_prepare_to_score_command) {
                    new_placer_state = Placer.PlacerState.DEPLOY;
                    robot_state = RobotState.READY_TO_SCORE;
                } else if (is_cancel_hold_command) {
                    //A method to cancel holding in case more pixels need to picked up or a mistake
                    new_placer_state = Placer.PlacerState.READY_TO_INTAKE;
                    robot_state = RobotState.READY_TO_INTAKE;
                } else {
                    new_placer_state = Placer.PlacerState.STOW;
                }
            case READY_TO_SCORE:
                if(is_place_level_increment_command &&
                        placer.place_level <= placer.PLACE_LEVEL_MAX) {
                    placer.place_level += 1;
                }
                if(is_place_level_decrement_command && placer.place_level >= 0) {
                    placer.place_level -= 1;
                }
                //Command to prepare climb take priority
                if (is_prepare_to_climb_command) {
                    new_placer_state = Placer.PlacerState.READY_TO_CLIMB;
                    robot_state = RobotState.READY_TO_CLIMB;
                } else if (is_cancel_score_command) {
                    //No easy way to use ejector cradle when returning b/c mechanical interference
                    //TODO does canceling with 1 or 2 pixels cause mech. intf.
                    new_placer_state = Placer.PlacerState.STOW;
                    robot_state = RobotState.HOLDING_PIXELS;
                } else if (is_place_first_pixel_command) {
                    new_placer_state = Placer.PlacerState.PLACE_FIRST;
                } else if (is_place_second_pixel_command) {
                    new_placer_state = Placer.PlacerState.PLACE_SECOND;
                } else if (is_score_done_command) {
                    new_placer_state = Placer.PlacerState.READY_TO_INTAKE;
                } else {
                    /* TODO
                     * May need to hold placer in scored state current logic
                     * will have gripper return to transfer state intermittently when
                     * score command is let go. This was easier to implement for now.
                     */
                    new_placer_state = Placer.PlacerState.DEPLOY;
                }
                break;
            case READY_TO_CLIMB:
                //Command to climb take priority
                if(is_climb_command) {
                    new_placer_state = Placer.PlacerState.HANG;
                    robot_state = RobotState.HANGING;
                } else {
                    new_placer_state = Placer.PlacerState.READY_TO_CLIMB;
                }
                break;
            case HANGING:
                //Ability to cancel in case of fall or missed climb takes priority
                if(is_climb_cancel_command) {
                    new_placer_state = Placer.PlacerState.READY_TO_CLIMB;
                    robot_state = RobotState.READY_TO_CLIMB;
                } else {
                    new_placer_state = Placer.PlacerState.HANG;
                }
                break;
        }

        //Safe state can be invoked independent of current robot state
        if(is_safe_state_command) {
            //Failsafe to enter safe state of all mechanisms
            new_placer_state = Placer.PlacerState.READY_TO_INTAKE;
            new_picker_state = Picker.PickerState.HOLD;
            is_drone_launch_command = false;
            forward = 0.0;
            strafe = 0.0;
            rotation = 0.0;
            robot_state = RobotState.READY_TO_INTAKE;
        }

        //Chassis drive is run independent of robot state
        drive.setWeightedDrivePower(
                new Pose2d(
                        forward,
                        strafe,
                        rotation
                )
        );

        picker.update(new_picker_state);
        placer.update(new_placer_state);
        drone_launcher.update(is_drone_launch_command);
        drive.update();

        telemetry.addData("robot_state", robot_state.toString());
        telemetry.addData("place_level", placer.place_level);
        telemetry.update();

        is_optr_dpad_up_prev = is_optr_dpad_up_cur;
        is_optr_dpad_dwn_prev = is_optr_dpad_dwn_cur;
        is_place_second_pixel_command_prev = is_place_second_pixel_command;
    }
}
