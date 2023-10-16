package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Gripper {
    private Servo claw;
    //private Servo pusher;
    private final double CLAW_MIN_PERCENT = 0.0;
    private final double CLAW_MAX_PERCENT = 1.0;


    public Gripper(HardwareMap hardwareMap) {
        claw = hardwareMap.get(Servo.class, "claw");
        //pusher = hardwareMap.get(Servo.class, "pusher");

        claw.scaleRange(CLAW_MIN_PERCENT,CLAW_MAX_PERCENT);
        //pusher.scaleRange(PUSHER_MIN_PERCENT,PUSHER_MAX_PERCENT);
    }

    public void update(float gripper_close_percent, boolean pusher_down) {
        claw.setPosition(gripper_close_percent);

//        if(pusher_down) {
//            pusher.setPosition(PUSHER_DOWN_POSITION);
//        } else {
//            pusher.setPosition(PUSHER_UP_POSITION);
//        }
    }

}
