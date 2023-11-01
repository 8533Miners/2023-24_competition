package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.subsystems.Elevator;

@TeleOp(group = "TeleOp")
public class Testing_Elevator_Encoder extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotorEx elevator = hardwareMap.get(DcMotorEx.class, "elevator");

        waitForStart();

        while (!isStopRequested()) {
            int cur_enc = elevator.getCurrentPosition();

            telemetry.addData("encoder", cur_enc);
            telemetry.update();
        }
    }

}
