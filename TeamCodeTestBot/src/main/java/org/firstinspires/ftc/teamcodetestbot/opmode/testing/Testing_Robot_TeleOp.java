package org.firstinspires.ftc.teamcodetestbot.opmode.testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcodetestbot.subsystems.Robot;
@Disabled
@TeleOp(group = "TeleOp")
public class Testing_Robot_TeleOp extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        Robot robot = new Robot(hardwareMap);

        waitForStart();

        while (!isStopRequested()) {

            robot.updateTeleOp(gamepad1,gamepad2,telemetry);
        }
    }
}
