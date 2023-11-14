package org.firstinspires.ftc.teamcodetestbot.subsystems;

import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.acmerobotics.roadrunner.control.PIDFController;
import com.acmerobotics.roadrunner.profile.MotionProfile;
import com.acmerobotics.roadrunner.profile.MotionProfileGenerator;
import com.acmerobotics.roadrunner.profile.MotionState;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;


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
    private PIDFController pidf_controller;

    public Elevator(HardwareMap hardwareMap) {
        elevator_motor = hardwareMap.get(DcMotorEx.class, "elevator");

        elevator_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elevator_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        elevator_motor.setMotorEnable();//TODO test removing this

        pidf_controller = new PIDFController(
                new PIDCoefficients(
                        0.01,
                        0.0,
                        0.0)
        );

        pidf_controller.setOutputBounds(-1.0,1.0);
        pidf_controller.setTargetPosition(0.0);
    }

    public void update(int target_position) {
        elevator_motor.setTargetPosition(target_position);//TODO test removing this

        pidf_controller.setTargetPosition(target_position);
        if(pidf_controller.getTargetPosition() == 0 && //Note 0 here means ELEVATOR_STOWED position
                elevator_motor.getCurrentPosition() < 60) {
            elevator_motor.setPower(0.0);
        } else {
            elevator_motor.setPower(pidf_controller.update(elevator_motor.getCurrentPosition()));
        }

    }
    public void log(Telemetry tele) {
        tele.addData("current encoder ticks", elevator_motor.getCurrentPosition());
        tele.addData("current target position", pidf_controller.getTargetPosition());
    }

}
