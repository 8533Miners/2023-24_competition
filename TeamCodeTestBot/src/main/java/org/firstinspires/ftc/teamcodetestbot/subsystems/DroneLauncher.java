package org.firstinspires.ftc.teamcodetestbot.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class DroneLauncher {
    private final double LAUNCH_POSITION = 0.0;
    private final double HOLD_POSITION = 1.0;
    private Servo drone_servo;
    DroneLauncher(HardwareMap hardwareMap) {
        drone_servo = hardwareMap.get(Servo.class, "drone_servo");
    }
    void update(boolean launch) {
        if(launch) {
            drone_servo.setPosition(LAUNCH_POSITION);
        } else {
            drone_servo.setPosition(HOLD_POSITION);
        }
    }
}
