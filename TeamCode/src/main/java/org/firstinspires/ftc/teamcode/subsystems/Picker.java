package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Picker {
    private final double RUNNING_POWER = 1.0;
    private DcMotorEx left_motor;
    private DcMotorEx right_motor;
    public enum PickerState {
        INTAKE,
        OUTAKE,
        HOLD
    }
    Picker(HardwareMap hardwareMap) {
        left_motor = hardwareMap.get(DcMotorEx.class, "picker_left");
        right_motor = hardwareMap.get(DcMotorEx.class, "picker_right");
        left_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        left_motor.setMotorEnable();
        right_motor.setMotorEnable();
    }
    void update (PickerState desired_state) {
        switch (desired_state) {
            case HOLD:
                left_motor.setPower(0.0);
                right_motor.setPower(0.0);
                break;
            case INTAKE:
                left_motor.setPower(RUNNING_POWER);
                right_motor.setPower(-RUNNING_POWER);
                break;
            case OUTAKE:
                left_motor.setPower(-RUNNING_POWER);
                right_motor.setPower(RUNNING_POWER);
                break;
        }
    }
}
