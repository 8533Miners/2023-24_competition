package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.subsystems.menu.AutonSelectionMenu;
import org.firstinspires.ftc.teamcode.subsystems.menu.SelectionMenu;
@Autonomous(name="Testing_AutonSelectionMenu", group="menu")
public class Testing_AutonSelectionMenu extends LinearOpMode {
    SelectionMenu selectionMenu = new SelectionMenu(telemetry);
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
//        selectionMenu.setMenuState(SelectionMenu.MenuState.READY);
//        selectionMenu.displayMenu();
        telemetry.addData("Status", "Ready for start");
        telemetry.update();
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
