package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.subsystems.menu.FtcMenu;
import org.firstinspires.ftc.teamcode.subsystems.menu.HalDashboard;
//import com.qualcomm.robotcore.robocol.TelemetryMessage;
@Autonomous(name = "Testing FTC Menu", group = "TestAuton")

public class Testing_ftcMenu extends LinearOpMode implements FtcMenu.MenuButtons {
    private enum Alliance
    {
        BLUE_ALLIANCE,
        RED_ALLIANCE
    }
    private enum StartPosition
    {
        LEFT,
        RIGHT
    }

    private enum ParkLocation
    {
        NEAR_WALL,
        ON_BACKDROP,
        NEAR_CENTER
    }
    private int delay = 0;
    private HalDashboard halDashboard;
    private Alliance alliance = Alliance.RED_ALLIANCE;
    private StartPosition startPosition = StartPosition.LEFT;
    private ParkLocation parkLocation = ParkLocation.NEAR_WALL;
    //private TelemetryMessage telemetryMessage;

    public void runOpMode() throws InterruptedException {
        //telemetryMessage = new TelemetryMessage();
        halDashboard = HalDashboard.getInstance(telemetry);

        while (opModeInInit())
        {
            doMenus();
        }
        while (opModeIsActive())
        {
            telemetry.addData("Alliance", alliance);
            telemetry.addData("Start Position", startPosition);
            telemetry.addData("Park Position", parkLocation);
            telemetry.addData("Delay", delay);
            telemetry.update();
        }



    }
    @Override
    public boolean isMenuUpButton()
    {
        return gamepad1.dpad_up;
    }   //isMenuUpButton

    @Override
    public boolean isMenuDownButton()
    {
        return gamepad1.dpad_down;
    }   //isMenuDownButton

    @Override
    public boolean isMenuEnterButton()
    {
        return gamepad1.a;
    }   //isMenuEnterButton

    @Override
    public boolean isMenuBackButton()
    {
        return gamepad1.dpad_left;
    }   //isMenuBackButton

    private void doMenus()
    {

        FtcMenu allianceMenu = new FtcMenu("Alliance:", null, this);
        FtcMenu positionMenu = new FtcMenu("Start Position:", allianceMenu, this);
        FtcMenu parkMenu = new FtcMenu("Park Strategy:", positionMenu, this);
        FtcMenu delayMenu = new FtcMenu("Start Delay", parkMenu, this);




        allianceMenu.addChoice("Red", Alliance.RED_ALLIANCE, positionMenu);
        allianceMenu.addChoice("Blue", Alliance.BLUE_ALLIANCE, positionMenu);

        positionMenu.addChoice("Left", StartPosition.LEFT, parkMenu);
        positionMenu.addChoice("Right", StartPosition.RIGHT, parkMenu);

        parkMenu.addChoice("On Wall", ParkLocation.NEAR_WALL, delayMenu);
        parkMenu.addChoice("On Backdrop", ParkLocation.ON_BACKDROP, delayMenu);
        parkMenu.addChoice("On Center", ParkLocation.NEAR_CENTER, delayMenu);

        delayMenu.addChoice("0 Sec Delay", 0);
        delayMenu.addChoice("1 Sec Delay", 1);
        delayMenu.addChoice("3 Sec Delay", 3);
        delayMenu.addChoice("5 Sec Delay", 5);

        FtcMenu.walkMenuTree(allianceMenu);

        alliance = (Alliance)allianceMenu.getCurrentChoiceObject();
        startPosition = (StartPosition)positionMenu.getCurrentChoiceObject();
        parkLocation = (ParkLocation)parkMenu.getCurrentChoiceObject();
        delay = delayMenu.getCurrentChoice();

        halDashboard.displayPrintf(4, "Selections: %s", allianceMenu.getCurrentChoiceText());
    }
}

