package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
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
    private final int ELEVATOR_TARGET_POSITION_TOLERANCE = 0;//TODO
    public Elevator(HardwareMap hardwareMap) {
        elevator_motor = hardwareMap.get(DcMotorEx.class, "elevator");
        elevator_motor.setPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION,
                SubSystemConstants.ELEVTATOR_PIDF_COEF);
        elevator_motor.setTargetPositionTolerance(ELEVATOR_TARGET_POSITION_TOLERANCE);

        //Possibly get position
        //Set initial seek position
        //Possibly do some error checking

        elevator_motor.setMotorEnable();
    }

    public void update(int target_position) {
        elevator_motor.setTargetPosition(target_position);
    }

}
