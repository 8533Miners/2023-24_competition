package org.firstinspires.ftc.teamcodetestbot.subsystems.menu;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class SelectionMenu {
    public enum MenuState {
        MAIN_MENU,
        ALLIANCE_COLOR,
        FIELD_START_POSITION,
        FIELD_PARK_POSITION,
        //        START_DELAY,
        CONFIRMATION,
        READY
    }
    Telemetry telemetry;
    private LinearOpMode opMode;
    public MenuState currentMenu = MenuState.MAIN_MENU;
    private AllianceColor allianceColor = AllianceColor.RED;
    private FieldStartPosition fieldStartPosition = FieldStartPosition.LEFT;
    private FieldParkPosition fieldParkPosition = FieldParkPosition.ON_BACKDROP;
    private int currentOptionIndex = 0; // Index of the currently selected option
    private ElapsedTime debounceTimer = new ElapsedTime();

    public enum AllianceColor {
        RED,
        BLUE
    }

    public enum FieldStartPosition {
        LEFT,
        RIGHT
    }

    public enum FieldParkPosition {
        NEAR_WALL,
        ON_BACKDROP,
        NEAR_CENTER
    }

    /**
     * constructor
     * @param telemetry
     */
    public SelectionMenu(LinearOpMode opmode, Telemetry telemetry) {
        this.opMode = opmode;
        this.telemetry = telemetry;
    }
    public void setMenuState(MenuState newState) {
        currentMenu = newState;
        telemetry.addLine("setting currentmenu to: " + newState);
    }

    public void displayMenu() {
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
            default:
                telemetry.addLine("" + currentMenu);
                break;
        }

        telemetry.update();
    }

    private void displayOption(String optionText, int optionIndex) {
        if (optionIndex == currentOptionIndex) {
            telemetry.addLine(">> " + optionText); // Highlight the current option
        } else {
            telemetry.addLine("  " + optionText);
        }
    }

    public void navigateUp()
    {
        currentOptionIndex--;
        opMode.sleep(300);
    }

    public void navigateDown()
    {
        currentOptionIndex++;
        opMode.sleep(300);
    }

    public void selectOption()
    {
        switch (currentMenu) {
            case MAIN_MENU:
                if (currentOptionIndex == 0) {
                    setMenuState(MenuState.ALLIANCE_COLOR);
                } else if (currentOptionIndex == 1) {
                    setMenuState(MenuState.FIELD_START_POSITION);
                } else if (currentOptionIndex == 2) {
                    setMenuState(MenuState.FIELD_PARK_POSITION);
                }
                break;
            case ALLIANCE_COLOR:
                if (currentOptionIndex == 0) {
                    allianceColor = AllianceColor.RED;
                } else if (currentOptionIndex == 1) {
                    allianceColor = AllianceColor.BLUE;
                }
                setMenuState(MenuState.FIELD_START_POSITION);
                break;
            case FIELD_START_POSITION:
                if (currentOptionIndex == 0) {
                    fieldStartPosition = FieldStartPosition.LEFT;
                } else if (currentOptionIndex == 1) {
                    fieldStartPosition = FieldStartPosition.RIGHT;
                }
                setMenuState(MenuState.FIELD_PARK_POSITION);
                break;
            case FIELD_PARK_POSITION:
                if (currentOptionIndex == 0){
                    fieldParkPosition = FieldParkPosition.NEAR_WALL;
                } else if (currentOptionIndex == 1){
                    fieldParkPosition = FieldParkPosition.ON_BACKDROP;
                } else if (currentOptionIndex == 2){
                    fieldParkPosition = FieldParkPosition.NEAR_CENTER;
                }
                setMenuState(MenuState.CONFIRMATION);
                break;
            case CONFIRMATION:
                setMenuState(MenuState.READY);
                break;
            default:
                telemetry.addLine("" + currentMenu);
                break;
        }
        currentOptionIndex = 0; // Reset the option index
        opMode.sleep(300); // Debounce
    }

    public void navigateBack() {
        switch (currentMenu) {
            case MAIN_MENU:
                // Do nothing, stay on the main menu
                break;
            case ALLIANCE_COLOR:
                setMenuState(MenuState.MAIN_MENU);
                break;
            case FIELD_START_POSITION:
                setMenuState(MenuState.ALLIANCE_COLOR);
                break;
            case FIELD_PARK_POSITION:
                setMenuState(MenuState.FIELD_START_POSITION);
                break;
            case CONFIRMATION:
                if (currentOptionIndex == 0) {
                    setMenuState(MenuState.FIELD_PARK_POSITION);
                }
                break;
        }
        currentOptionIndex = 0; // Reset the option index
    }
    public int getMenuOptionCount() {
        // Return the number of menu options for the current menu
        switch (currentMenu) {
            case MAIN_MENU:
                return MenuState.values().length - 3;
            case ALLIANCE_COLOR:
                return AllianceColor.values().length;
            case FIELD_START_POSITION:
                return FieldStartPosition.values().length;
            case FIELD_PARK_POSITION:
                return FieldParkPosition.values().length;
            default:
                return 0;
        }
    }

    public AllianceColor getAllianceColor() {
        return allianceColor;
    }
    public FieldStartPosition getFieldStartPosition() {
        return fieldStartPosition;
    }
    public FieldParkPosition getFieldParkPosition() {
        return fieldParkPosition;
    }
}
