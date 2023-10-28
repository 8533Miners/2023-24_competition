package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/*
 * Ideal Functions:
 * Seek elevator seeking a position
 *
 * Configurable Parameters:
 * Located in SubSystemConstants in:
 * ELEVTATOR_PIDF_COEF
 * Located in Elevator:
 * ELEVATOR_TARGET_POSITION_TOLERANCE
 *
 * Unintended Outputs:
 * Elevator belt system jams
 * Elevator Gripper jams when returning to stowed position
 */
public class Elevator {
    private DcMotorEx elevator_motor;
    PIDFCoefficients pidfCoefficients = new PIDFCoefficients(10, 0, 0, 0);

    private int target_pos = 0;

    private final int ELEVATOR_TARGET_POSITION_TOLERANCE = 50;//TODO
    public Elevator(HardwareMap hardwareMap) {
        elevator_motor = hardwareMap.get(DcMotorEx.class, "elevator");
//        elevator_motor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
//        elevator_motor.setPositionPIDFCoefficients(10.0);
//        elevator_motor.setTargetPosition(0);
//        elevator_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        elevator_motor.setTargetPositionTolerance(ELEVATOR_TARGET_POSITION_TOLERANCE);
//
//        //Possibly get position
//        //Set initial seek position
//        //Possibly do some error checking
//
        elevator_motor.setMotorEnable();


    }

    public void update(int target_position) {

        //elevator_motor.setTargetPosition(target_position);
        this.target_pos = target_position;

        int error = elevator_motor.getCurrentPosition() - target_pos;

        if(Math.abs(error) < 50) {
            elevator_motor.setPower(0.0);
        } else if (error > 0) {
            elevator_motor.setPower(-0.5);
        } else {
            elevator_motor.setPower(0.5);
        }
    }
    public void log(Telemetry tele){
        tele.addData("current encoder ticks", elevator_motor.getCurrentPosition());
        tele.addData("current target position", target_pos);
    }

}
