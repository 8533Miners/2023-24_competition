package org.firstinspires.ftc.teamcode.subsystems.menu;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "MenuExample", group = "TeleOp")
public class AutonSelectionMenu extends LinearOpMode {

    private enum MenuState {
        MAIN_MENU,
        ALLIANCE_COLOR,
        FIELD_START_POSITION,
        FIELD_PARK_POSITION,
//        START_DELAY,
        CONFIRMATION,
        READY
    }
    //defaults
    private MenuState currentMenu = MenuState.MAIN_MENU;
    private AllianceColor allianceColor = AllianceColor.RED;
    private FieldStartPosition fieldStartPosition = FieldStartPosition.LEFT;
    private FieldParkPosition fieldParkPosition = FieldParkPosition.ON_BACKDROP;
    private int currentOptionIndex = 0; // Index of the currently selected option

    private enum AllianceColor {
        RED,
        BLUE
    }

    private enum FieldStartPosition {
        LEFT,
        RIGHT
    }

    private enum FieldParkPosition {
        NEAR_WALL,
        ON_BACKDROP,
        NEAR_CENTER
    }
//
//    private enum Delay {
//        NONE(0),
//        ONESEC(1),
//        THREESEC(3),
//        FIVESEC(5);
//        private final int delay;
//        Delay(final int del){
//            delay = del;
//        }
//        private int getDelay() {return delay;}
//    }


    @Override
    public void runOpMode() {
        telemetry.setAutoClear(false);

        while (!isStarted()) {
            // Display the current menu
            displayMenu();

            // Check for user input
            if (gamepad1.dpad_up) {
                navigateUp();
            } else if (gamepad1.dpad_down) {
                navigateDown();
            } else if (gamepad1.a) {
                selectOption();
            } else if (gamepad1.b) {
                navigateBack();
            }

            // Allow for other initialization here
            idle();
        }

        // Exit the menu loop and display "Ready for start"
        currentMenu = MenuState.READY;
        displayMenu();
        telemetry.addData("Status", "Ready for start");
        telemetry.update();

        waitForStart();

        // Do something with the selected options
        doSomethingWithSelectedOptions();
        sleep(5000);
    }

    private void displayMenu() {
        telemetry.clear();

        switch (currentMenu) {
            case MAIN_MENU:
                telemetry.addLine("Main Menu:");
                displayOption("1. Select Alliance Color", 0);
                displayOption("2. Select Field Start Position", 1);
                displayOption("3. Select Field Park Position", 2);
                telemetry.addLine("Use DPAD UP/DOWN to navigate and Press 'A' to select");
                break;

            case ALLIANCE_COLOR:
                telemetry.addLine("Select Alliance Color:");
                displayOption("1. Red", 0);
                displayOption("2. Blue", 1);
                telemetry.addLine("Press 'A' to select or 'B' to go back.");
                break;

            case FIELD_START_POSITION:
                telemetry.addLine("Select Field Start Position:");
                displayOption("1. Left", 0);
                displayOption("2. Right", 1);
                telemetry.addLine("Press 'A' to select or 'B' to go back.");
                break;

            case FIELD_PARK_POSITION:
                telemetry.addLine("Select Field Park Position");
                displayOption("1. Near Wall", 0);
                displayOption("2. On Backdrop", 1);
                displayOption("3. Near Center", 2);
                telemetry.addLine("Press 'A' to select or 'B' to go back.");
                break;

            case CONFIRMATION:
                telemetry.addLine("Confirm Selection:");
                telemetry.addLine("Alliance Color: " + allianceColor.toString());
                telemetry.addLine("Field Start Position: " + fieldStartPosition.toString());
                telemetry.addLine("Field Park Position: " + fieldParkPosition.toString());
                telemetry.addLine("Press 'A' to confirm or 'B' to go back.");
                break;

            case READY:
                // Display "Ready for start" message
                telemetry.addData("Status", "Ready for start");
                break;
        }

        telemetry.update();
    }

    private void displayOption(String optionText, int optionIndex) {
        if (optionIndex == currentOptionIndex) {
            telemetry.addLine("> " + optionText); // Highlight the current option
        } else {
            telemetry.addLine("  " + optionText);
        }
    }

    private void navigateUp() {
        currentOptionIndex = Math.max(0, currentOptionIndex - 1);
        sleep(300); // Debounce
    }

    private void navigateDown() {
        switch (currentMenu) {
            case MAIN_MENU:
            case FIELD_PARK_POSITION:
                currentOptionIndex = Math.min(2, currentOptionIndex + 1);
                break;
            case ALLIANCE_COLOR:
            case FIELD_START_POSITION:
                currentOptionIndex = Math.min(1, currentOptionIndex + 1);
                break;
//            case FIELD_PARK_POSITION:
//                currentOptionIndex = Math.min(2, currentOptionIndex + 1);
//                break;
        }
        sleep(300); // Debounce
    }

    private void selectOption() {
        switch (currentMenu) {
            case MAIN_MENU:
                if (currentOptionIndex == 0) {
                    currentMenu = MenuState.ALLIANCE_COLOR;
                } else if (currentOptionIndex == 1) {
                    currentMenu = MenuState.FIELD_START_POSITION;
                } else if (currentOptionIndex == 2) {
                    currentMenu = MenuState.FIELD_PARK_POSITION;
                }
                break;
            case ALLIANCE_COLOR:
                if (currentOptionIndex == 0) {
                    allianceColor = AllianceColor.RED;
                } else if (currentOptionIndex == 1) {
                    allianceColor = AllianceColor.BLUE;
                }
                currentMenu = MenuState.FIELD_START_POSITION;
                break;
            case FIELD_START_POSITION:
                if (currentOptionIndex == 0) {
                    fieldStartPosition = FieldStartPosition.LEFT;
                } else if (currentOptionIndex == 1) {
                    fieldStartPosition = FieldStartPosition.RIGHT;
                }
                currentMenu = MenuState.FIELD_PARK_POSITION;
                break;
            case FIELD_PARK_POSITION:
                if (currentOptionIndex == 0){
                    fieldParkPosition = FieldParkPosition.NEAR_WALL;
                } else if (currentOptionIndex == 1){
                    fieldParkPosition = FieldParkPosition.ON_BACKDROP;
                } else if (currentOptionIndex == 2){
                    fieldParkPosition = FieldParkPosition.NEAR_CENTER;
                }
                currentMenu = MenuState.CONFIRMATION;
                break;
            case CONFIRMATION:
                currentMenu = MenuState.READY;
                break;
        }
        currentOptionIndex = 0; // Reset the option index
        sleep(300); // Debounce
    }

    private void navigateBack() {
        switch (currentMenu) {
            case MAIN_MENU:
                // Do nothing, stay on the main menu
                break;
            case ALLIANCE_COLOR:
                currentMenu = MenuState.MAIN_MENU;
                break;
            case FIELD_START_POSITION:
                currentMenu = MenuState.ALLIANCE_COLOR;
                break;
            case FIELD_PARK_POSITION:
                currentMenu = MenuState.FIELD_START_POSITION;
                break;
            case CONFIRMATION:
                if (currentOptionIndex == 0) {
                    currentMenu = MenuState.FIELD_PARK_POSITION; // Go back to previous menu
                }
                break;
        }
        currentOptionIndex = 0; // Reset the option index
        sleep(300); // Debounce
    }

    private void doSomethingWithSelectedOptions() {
        // Example: Print the selected options to telemetry
        telemetry.clear();
        telemetry.addLine("Selected Alliance Color: " + allianceColor.toString());
        telemetry.addLine("Selected Field Start Position: " + fieldStartPosition.toString());
        telemetry.addLine("Selected Field Park Position: " + fieldParkPosition.toString());
        telemetry.update();
    }
}
