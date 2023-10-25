package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class Placer {

    private Gripper gripper;
    private Elevator elevator;
    Placer(HardwareMap hardwareMap) {
        gripper = new Gripper(hardwareMap);
        elevator = new Elevator(hardwareMap);
    }

    public void update() {}

}
