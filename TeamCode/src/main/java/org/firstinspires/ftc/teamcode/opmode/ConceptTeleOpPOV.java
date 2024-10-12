package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Robot;

@TeleOp(name= "Competition Teleop - POV", group = "TeleOp")
public class ConceptTeleOpPOV extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        Robot robot = new Robot(hardwareMap);

        waitForStart();

        while (!isStopRequested()) {

            robot.updateTeleOp(gamepad1,gamepad2,telemetry);
        }
    }
}
