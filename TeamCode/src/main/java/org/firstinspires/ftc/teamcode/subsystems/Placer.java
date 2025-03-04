package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Placer {
    public final int PLACE_LEVEL_MAX = 6;
    private final int ELEVATOR_STOWED = 0;
    private final int PIXEL_HEIGHT = 240;//TODO
    private final int BACKDROP_HEIGHT = 1466; //1566;
    private final int CLIMB_HEIGHT = 2300;
    private final int HANGING = 1000;//1280;
    private final int MAX_SAFE = 2800;
    private Gripper gripper;
    private Elevator elevator;
    private Gripper.GripperState gripper_state = Gripper.GripperState.READY;
    public int place_level = 0;
    public enum PlacerState {
        READY_TO_INTAKE,
        STOW,
        DEPLOY,
        PLACE_FIRST,
        PLACE_SECOND,
        READY_TO_CLIMB,
        HANG
    }
    public Placer(HardwareMap hardwareMap) {
        gripper = new Gripper(hardwareMap);
        elevator = new Elevator(hardwareMap);
    }
    public void update(PlacerState desired_state, boolean is_forced_zero) {
        Gripper.GripperState new_gripper_state;
        int new_elevator_position;
        switch (desired_state) {
            default:
                //error state set safe state by falling into READY_TO_INTAKE
            case READY_TO_INTAKE:
                new_gripper_state = Gripper.GripperState.READY;
                new_elevator_position = ELEVATOR_STOWED;
                break;
            case STOW:
                new_gripper_state = Gripper.GripperState.STOWED;
                new_elevator_position = ELEVATOR_STOWED;
                break;
            case DEPLOY:
                new_gripper_state = Gripper.GripperState.TRANSFER;
                new_elevator_position = place_level*PIXEL_HEIGHT + BACKDROP_HEIGHT;
                break;
            case PLACE_FIRST:
                new_gripper_state = Gripper.GripperState.SCORE_FIRST;
                new_elevator_position = place_level*PIXEL_HEIGHT + BACKDROP_HEIGHT;
                break;
            case PLACE_SECOND:
                new_gripper_state = Gripper.GripperState.SCORE_SECOND;
                new_elevator_position = place_level*PIXEL_HEIGHT + BACKDROP_HEIGHT;
                break;
            case READY_TO_CLIMB:
                new_gripper_state = Gripper.GripperState.READY;
                new_elevator_position = CLIMB_HEIGHT;
                break;
            case HANG:
                new_gripper_state = Gripper.GripperState.READY;
                new_elevator_position = HANGING;
                break;
        }

        //Set max height limit;
        new_elevator_position = Math.min(new_elevator_position,MAX_SAFE);

        gripper.update(new_gripper_state);
        elevator.update(new_elevator_position,false);
    }
    public void log(Telemetry tele){
        elevator.log(tele);
        gripper.log(tele);
        tele.addData("Gripper state", gripper_state.toString());
    }

    public void auton_deploy_elevator(PlacerState desired_state, double time, ElapsedTime runtime, boolean is_force_zero) {
        //calculate target time by adding 1 second to current time
        double target_time = runtime.seconds() + time;
        //loop until target time is reached
        while(runtime.seconds() < target_time) {
            this.update(desired_state, is_force_zero);
        }
    }

}
