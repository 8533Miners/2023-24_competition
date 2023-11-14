package org.firstinspires.ftc.teamcodetestbot.opmode.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcodetestbot.subsystems.menu.SelectionMenu;
@Disabled
@Autonomous(name="Testing_MenuAuton", group="menu")
public class Testing_MenuAuton extends LinearOpMode {
    SelectionMenu selectionMenu = new SelectionMenu(this,telemetry);
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.setAutoClear(false);

        while(!isStarted()) {
            selectionMenu.displayMenu();

            // Check for user input
            if (gamepad1.dpad_up) {
                selectionMenu.navigateUp();
            } else if (gamepad1.dpad_down) {
                selectionMenu.navigateDown();
            } else if (gamepad1.a) {
                selectionMenu.selectOption();
            } else if (gamepad1.b) {
                selectionMenu.navigateBack();
            }

            idle();
        }
        selectionMenu.setMenuState(SelectionMenu.MenuState.READY);
        selectionMenu.displayMenu();

        waitForStart();

        while (opModeIsActive()) {
            printMenuSelections();
            sleep(10000);
        }
    }
    private void printMenuSelections() {
        telemetry.clear();
        telemetry.addLine("Alliance Color: " + selectionMenu.getAllianceColor());
        telemetry.addLine("Start Position: " + selectionMenu.getFieldStartPosition());
        telemetry.addLine("Park Position: " + selectionMenu.getFieldParkPosition());
        telemetry.update();
    }
}
