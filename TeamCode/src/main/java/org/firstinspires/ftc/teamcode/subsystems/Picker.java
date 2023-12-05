package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Picker {
    private final double RUNNING_POWER = 1.0;
    private DcMotorEx left_motor;
    private DcMotorEx right_motor;
    private Rev2mDistanceSensor distance_sensor;
    public enum PickerState {
        AUTON,
        INTAKE,
        OUTAKE,
        HOLD
    }
    public Picker(HardwareMap hardwareMap) {
        left_motor = hardwareMap.get(DcMotorEx.class, "picker_left");
        right_motor = hardwareMap.get(DcMotorEx.class, "picker_right");
//        distance_sensor = hardwareMap.get(Rev2mDistanceSensor.class, "distance");
        left_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        left_motor.setMotorEnable();
        right_motor.setMotorEnable();
    }
    public void update (PickerState desired_state) {
        switch (desired_state) {
            case AUTON:
                left_motor.setPower(-1);
                right_motor.setPower(1);
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

    public void log(Telemetry tele){
//        tele.addData("Distance sensor", distance_sensor.getDistance(DistanceUnit.MM));
    }

    public void auton_place_spike(PickerState desired_state, double time, ElapsedTime runtime) {
        //calculate target time by adding 1 second to current time
        double target_time = runtime.seconds() + time;
        //loop until target time is reached
        while(runtime.seconds() < target_time) {
            this.update(desired_state);
        }
    }
}
