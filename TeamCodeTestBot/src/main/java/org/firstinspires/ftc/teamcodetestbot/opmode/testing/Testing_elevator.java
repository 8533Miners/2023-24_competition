package org.firstinspires.ftc.teamcodetestbot.opmode.testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcodetestbot.subsystems.Elevator;
@Disabled
@TeleOp(group = "Testing")
public class Testing_elevator extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Elevator elevator = new Elevator(hardwareMap);

    }
}
