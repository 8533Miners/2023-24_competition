package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Gripper {
    private Servo claw;
    private Servo pusher;
    private final double CLAW_MIN_PERCENT = 0.0;
    private final double CLAW_MAX_PERCENT = 1.0;
    private final double PUSHER_UP_POSITION = 0.0;
    private final double PUSHER_DOWN_POSITION = 1.0;


    public Gripper(HardwareMap hardwareMap) {
        claw = hardwareMap.get(Servo.class, "claw");
        pusher = hardwareMap.get(Servo.class, "pusher");

        //claw.scaleRange(CLAW_MIN_PERCENT,CLAW_MAX_PERCENT);
        pusher.scaleRange(0.0,1.0);//PUSHER_MIN_PERCENT,PUSHER_MAX_PERCENT);
    }

    public void update(boolean clawOpen, boolean clawScore, boolean pusherDown) {
        if (clawOpen) {
            claw.setPosition(SubSystemConstants.ClawPosition.OPEN.getClawPosition());
        } else if (clawScore) {
            claw.setPosition(SubSystemConstants.ClawPosition.SCORE.getClawPosition());
        } else {
            claw.setPosition(SubSystemConstants.ClawPosition.CLOSED.getClawPosition());
        }

        if(pusherDown) {
            pusher.setPosition(PUSHER_DOWN_POSITION);
        } else {
            pusher.setPosition(PUSHER_UP_POSITION);
        }
    }

}
