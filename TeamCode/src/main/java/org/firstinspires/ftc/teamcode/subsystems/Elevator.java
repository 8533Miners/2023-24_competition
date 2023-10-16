package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

public class Elevator {
    private DcMotorEx elevator_motor;
    private final int ELEVATOR_TARGET_POSITION_TOLERANCE = 0;//TODO
    private final double ELEVATOR_PK = 1.0;//TODO
    private final double ELEVATOR_IK = 0.0;//TODO
    private final double ELEVATOR_DK = 1.0;//TODO
    private final double ELEVATOR_FK = 1.0;//TODO
    private final PIDFCoefficients ELEVTATOR_PIDF_COEF = new PIDFCoefficients(
            ELEVATOR_PK,
            ELEVATOR_IK,
            ELEVATOR_DK,
            ELEVATOR_FK
    );

    public Elevator(HardwareMap hardwareMap) {
        elevator_motor = hardwareMap.get(DcMotorEx.class, "elevator");
        elevator_motor.setPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION, ELEVTATOR_PIDF_COEF);
        elevator_motor.setTargetPositionTolerance(ELEVATOR_TARGET_POSITION_TOLERANCE);

        //Possibly get position
        //Set initial seek position
        //Possibly do some error checking
    }

    public void update(int target_position) {
        elevator_motor.setTargetPosition(target_position);
    }


}
