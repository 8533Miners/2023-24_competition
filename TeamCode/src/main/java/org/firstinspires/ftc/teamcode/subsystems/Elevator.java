package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.acmerobotics.roadrunner.control.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;


import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

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
    private TouchSensor elevator_limit;
    private PIDFController pidf_controller;
    private ElapsedTime daryls_timer;
    private boolean overdrive = false;
    private boolean is_homed;

    public Elevator(HardwareMap hardwareMap) {
        elevator_motor = hardwareMap.get(DcMotorEx.class, "elevator");
        elevator_limit = hardwareMap.get(TouchSensor.class, "elevator_limit");

        elevator_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elevator_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        elevator_motor.setMotorEnable();//TODO test removing this

        pidf_controller = new PIDFController(
                new PIDCoefficients(
                        0.002,
                        0.0,
                        0.0),
                0.0,
                0.0,
                0.0,
                //anonymous function takes raw values and calculates additive term in output units (+/-%FS)
                (measuredPosition, measuredVelocity) -> {return 0.02;}
        );

        pidf_controller.setOutputBounds(-1.0,1.0);
        pidf_controller.setTargetPosition(0.0);

        //if homing button is not pressed on start we are in an error state, set !is_homed
        if(elevator_limit.isPressed()) {
            is_homed = true;
        } else {
            is_homed = false;
        }

        daryls_timer = new ElapsedTime();
        overdrive = false;
    }

    public void update(int target_position, boolean is_force_zero) {
        elevator_motor.setTargetPosition(target_position);//TODO test removing this

        pidf_controller.setTargetPosition(target_position);

        if(target_position != 0) {
            is_homed = false;
            overdrive = false;
        }

        if(target_position == 0 && is_homed && !overdrive) {
            overdrive = true;
            daryls_timer.reset();
        }

        //Only home if we are trying to go to home
        if(target_position == 0 && elevator_limit.isPressed()) {
            elevator_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            elevator_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            is_homed = true;
        }

        //Did we reach 0, but not press the homing button?
        if(target_position == 0 && (elevator_motor.getCurrentPosition() <= 0) && !is_homed) {
            //Yes, continue moving down
            elevator_motor.setPower(-0.25);
        } else if (target_position == 0 && overdrive && (daryls_timer.seconds() < 2.0)) {
            elevator_motor.setPower(-0.25);
        } else if(is_force_zero) {
            elevator_motor.setPower(0.0);
        } else {
            //No, normal motor control using pidf controller
            elevator_motor.setPower(pidf_controller.update(elevator_motor.getCurrentPosition()));
        }

    }
    public void log(Telemetry tele) {
        tele.addData("Elevator current encoder ticks",
                elevator_motor.getCurrentPosition());
        tele.addData("Elevator motor current (A)",
                elevator_motor.getCurrent(CurrentUnit.AMPS));
        tele.addData("Elevator current target position",
                pidf_controller.getTargetPosition());
        tele.addData("Elevator motor power (+/-%FS)",
                elevator_motor.getPower());
        tele.addData("Overdrive (seconds)", daryls_timer.seconds());
        tele.addData("is Overdrive?", overdrive);
    }

}
