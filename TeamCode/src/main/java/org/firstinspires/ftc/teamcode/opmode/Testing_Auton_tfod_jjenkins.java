package org.firstinspires.ftc.teamcode.opmode;

import android.util.Size;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.Picker;
import org.firstinspires.ftc.teamcode.subsystems.menu.SelectionMenu;
import org.firstinspires.ftc.teamcode.subsystems.menu.SelectionMenu.AllianceColor;
import org.firstinspires.ftc.teamcode.subsystems.menu.SelectionMenu.FieldParkPosition;
import org.firstinspires.ftc.teamcode.subsystems.menu.SelectionMenu.FieldStartPosition;
import org.firstinspires.ftc.teamcode.subsystems.menu.SelectionMenu.MenuState;
import org.firstinspires.ftc.teamcode.subsystems.vision.SpikeMark;
import org.firstinspires.ftc.teamcode.subsystems.vision.TFObjectPropDetect;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.List;

@Autonomous(name = "jjenkins-auton", group = "TestAuton")
public class Testing_Auton_tfod_jjenkins extends LinearOpMode {
    // Random Robot Variables
    public ElapsedTime runtime = new ElapsedTime();

    // Camera Property Variables
    public int CAMERA_WIDTH = 640;
    int CAMERA_HEIGHT = 480;
    float highestConf = 0;
    float highestXDistance = 0;
    String highestXDistanceLabel = " ";

    // TFOD Variables
    TFObjectPropDetect tfObjectPropDetect;
    private static final String TFOD_MODEL_ASSET = "model_20231023_193833.tflite";
    private static final String[] LABELS = {
            "prop"
    };
    public String labelToDetect = "prop";
    private TfodProcessor tfod;
    public VisionPortal visionPortal;
    public boolean propDetected = false;

    // Menu variables
    SelectionMenu selectionMenu = new SelectionMenu(this,telemetry);

    // A4 Starting Parameters
    int A4_starting_x = 16;
    int A4_starting_y = 62;
    int starting_heading_A = 270;

    // A2 Starting Parameters
    int A2_starting_x = -40;
    int A2_starting_y = 62;

    // F4 Starting Parameters
    int F4_starting_x = 16;
    int F4_starting_y = -62;
    int starting_heading_F = 90;

    // F2 Starting Parameters
    int F2_starting_x = -40;
    int F2_starting_y = -62;

    Picker picker;

    public void runOpMode() throws InterruptedException{
        picker = new Picker(hardwareMap);

        /**
         * Setup the selection menu
         */
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
        selectionMenu.setMenuState(MenuState.READY);
        selectionMenu.displayMenu();

        int startingX;
        int startingY;
        int startingHeading;
        int parking_offset = 0;

        // Init TFOD
        initTfod();
        tfObjectPropDetect = new TFObjectPropDetect(tfod, CAMERA_WIDTH, labelToDetect);

        // Setup hardware mapping and drive style
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        /**
         * Determine the starting position based on menu config
         */
        AllianceColor allianceColor = selectionMenu.getAllianceColor();
        FieldStartPosition fieldStartPosition = selectionMenu.getFieldStartPosition();
        FieldParkPosition fieldParkPosition = selectionMenu.getFieldParkPosition();

        // Determine parking offset amount
        switch(fieldParkPosition){
            default:
            case NEAR_WALL:
                parking_offset = 0;
                break;
            case ON_BACKDROP:
                parking_offset = 20;
                break;
            case NEAR_CENTER:
                parking_offset = 40;
                break;
        }

        TrajectorySequence initialMove;
        TrajectorySequence leftTraj;
        TrajectorySequence centerTraj;
        TrajectorySequence rightTraj;
        Pose2d startPose;


        boolean invertedDetection; // invert detections based on starting position



        /**
         * Configure starting X, Y, and heading for starting pose
         * Invert detection based on starting position if needed
         */
        switch (allianceColor) {
            case RED:
                if(fieldStartPosition == FieldStartPosition.RIGHT) {
                    // F2
                    invertedDetection = true; // F4 has inverted detections compared to A4
                    startingX = F4_starting_x;
                    startingY = F4_starting_y;
                    startingHeading = starting_heading_F;

                    startPose = new Pose2d(startingX, startingY, Math.toRadians(startingHeading));

                    initialMove = drive.trajectorySequenceBuilder(startPose)
                            .strafeLeft(4)
                            .build();

                    leftTraj = drive.trajectorySequenceBuilder(initialMove.end())
                            .strafeLeft(3)
                            .forward(26)
                            .addDisplacementMarker(() -> { //start placing purple pixel
                                picker.update(Picker.PickerState.OUTAKE);
                            })
                            .waitSeconds(1)
                            .addDisplacementMarker(() -> { //stop placing purple pixel
                                picker.update(Picker.PickerState.HOLD);
                            })
                            .back(14)
                            .turn(Math.toRadians(-90))
                            .strafeRight(7)
                            .forward(78)
                            .strafeLeft(parking_offset)
                            .forward(16)
                            .waitSeconds(1) // place yellow pixel
                            .waitSeconds(1) // park
                            .build();

                    centerTraj = drive.trajectorySequenceBuilder(initialMove.end())
                            .strafeRight(3)
                            .forward(32)
                            .addDisplacementMarker(() -> { //start placing purple pixel
                                picker.update(Picker.PickerState.OUTAKE);
                            })
                            .waitSeconds(1)
                            .addDisplacementMarker(() -> { //stop placing purple pixel
                                picker.update(Picker.PickerState.HOLD);
                            })
                            .back(27)
                            .turn(Math.toRadians(-90))
                            .forward(70)
                            .strafeLeft(parking_offset)
                            .forward(16)
                            .waitSeconds(1) // place yellow pixel
                            .waitSeconds(1) // park
                            .build();

                } else {
                    // F4
                    invertedDetection = false; // F2 has looks the same as A4, so don't invert
                    startingX = F2_starting_x;
                    startingY = F2_starting_y;
                    startingHeading = starting_heading_F;

                    startPose = new Pose2d(startingX,startingY, Math.toRadians(startingHeading));

                    initialMove = drive.trajectorySequenceBuilder(startPose)
                            .strafeRight(4)
                            .build();
                    leftTraj = drive.trajectorySequenceBuilder(initialMove.end())
                            .forward(28)
                            .turn(Math.toRadians(90))
                            .forward(6)
                            .addDisplacementMarker(() -> { //start placing purple pixel
                                picker.update(Picker.PickerState.OUTAKE);
                            })
                            .waitSeconds(1)
                            .addDisplacementMarker(() -> { //stop placing purple pixel
                                picker.update(Picker.PickerState.HOLD);
                            })
                            .back(6)
                            .turn(Math.toRadians(-90))
                            .back(14)
                            .turn(Math.toRadians(-90))
                            .forward(14)
                            .strafeLeft(parking_offset)
                            .forward(16)
                            .waitSeconds(1) // place yellow pixel
                            .waitSeconds(1) // park
                            .build();

                    centerTraj = drive.trajectorySequenceBuilder(initialMove.end())
                            .strafeLeft(3)
                            .forward(32)
                            .addDisplacementMarker(() -> { //start placing purple pixel
                                picker.update(Picker.PickerState.OUTAKE);
                            })
                            .waitSeconds(1)
                            .addDisplacementMarker(() -> { //stop placing purple pixel
                                picker.update(Picker.PickerState.HOLD);
                            })
                            .back(14)
                            .turn(Math.toRadians(-90))
                            .forward(14)
                            .strafeLeft(parking_offset)
                            .forward(16)
                            .waitSeconds(1) // place yellow pixel
                            .waitSeconds(1) // park
                            .build();

                    rightTraj = drive.trajectorySequenceBuilder(initialMove.end())
                            //.strafeRight(2)
                            .forward(26)
                            .addDisplacementMarker(() -> { //start placing purple pixel
                                picker.update(Picker.PickerState.OUTAKE);
                            })
                            .waitSeconds(1)
                            .addDisplacementMarker(() -> { //stop placing purple pixel
                                picker.update(Picker.PickerState.HOLD);
                            })
                            .back(14)
                            .turn(Math.toRadians(-90))
                            .forward(6)
                            .strafeLeft(parking_offset)
                            .forward(16)
                            .waitSeconds(1) // place yellow pixel
                            .waitSeconds(1) // park
                            .build();
                }
                break;
            case BLUE:
            default:
                if(fieldStartPosition == FieldStartPosition.LEFT) {
                    // A2

                    invertedDetection = true; // A2 has inverted detections compared to A4
                    startingX = A2_starting_x;
                    startingY = A2_starting_y;
                    startingHeading = starting_heading_A;

                    startPose = new Pose2d(startingX,startingY, Math.toRadians(startingHeading));

                    initialMove = drive.trajectorySequenceBuilder(startPose)
                            .strafeRight(4)
                            .build();

                    leftTraj = drive.trajectorySequenceBuilder(initialMove.end())
                            .forward(28)
                            .turn(Math.toRadians(90))
                            .forward(6)
                            .addDisplacementMarker(() -> { //start placing purple pixel
                                picker.update(Picker.PickerState.OUTAKE);
                            })
                            .waitSeconds(1)
                            .addDisplacementMarker(() -> { //stop placing purple pixel
                                picker.update(Picker.PickerState.HOLD);
                            })
                            .back(6)
                            .strafeLeft(24)
                            .forward(70)
                            .strafeRight(parking_offset)
                            .forward(16)
                            .waitSeconds(1) // place yellow pixel
                            .waitSeconds(1) // park
                            .build();

                    centerTraj = drive.trajectorySequenceBuilder(initialMove.end())
                            .strafeLeft(3)
                            .forward(32)
                            .addDisplacementMarker(() -> { //start placing purple pixel
                                picker.update(Picker.PickerState.OUTAKE);
                            })
                            .waitSeconds(1)
                            .addDisplacementMarker(() -> { //stop placing purple pixel
                                picker.update(Picker.PickerState.HOLD);
                            })
                            .back(30)
                            .turn(Math.toRadians(90))
                            .forward(68)
                            .strafeRight(parking_offset)
                            .forward(16)
                            .waitSeconds(1) // place yellow pixel
                            .waitSeconds(1) // park
                            .build();

                    rightTraj = drive.trajectorySequenceBuilder(initialMove.end())
                            .strafeRight(2)
                            .forward(26)
                            .addDisplacementMarker(() -> { //start placing purple pixel
                                picker.update(Picker.PickerState.OUTAKE);
                            })
                            .waitSeconds(1)
                            .addDisplacementMarker(() -> { //stop placing purple pixel
                                picker.update(Picker.PickerState.HOLD);
                            })
                            .back(14)
                            .turn(Math.toRadians(90))
                            .strafeLeft(10)
                            .forward(78)
                            .strafeRight(parking_offset)
                            .forward(16)
                            .waitSeconds(1) // place yellow pixel
                            .waitSeconds(1) // park
                            .build();

                } else {
                    // A4
                    invertedDetection = false;
                    startingX = A4_starting_x;
                    startingY = A4_starting_y;
                    startingHeading = starting_heading_A;

                    startPose = new Pose2d(startingX,startingY, Math.toRadians(startingHeading));

                    initialMove = drive.trajectorySequenceBuilder(startPose)
                            .strafeLeft(4)
                            .build();
                    leftTraj = drive.trajectorySequenceBuilder(initialMove.end())
                            .strafeLeft(8)
                            .forward(26)
                            .addDisplacementMarker(() -> { //start placing purple pixel
                                picker.update(Picker.PickerState.OUTAKE);
                            })
                            .waitSeconds(1)
                            .addDisplacementMarker(() -> { //stop placing purple pixel
                                picker.update(Picker.PickerState.HOLD);
                            })
                            .back(12)
                            .turn(Math.toRadians(90))
                            .forward(6)
                            .strafeRight(parking_offset)
                            .forward(16)
                            .waitSeconds(1) // place yellow pixel
                            .waitSeconds(1) // park
                            .build();

                    centerTraj = drive.trajectorySequenceBuilder(initialMove.end())
                            .strafeRight(3)
                            .forward(32)
                            .addDisplacementMarker(() -> { //start placing purple pixel
                                picker.update(Picker.PickerState.OUTAKE);
                            })
                            .waitSeconds(1)
                            .addDisplacementMarker(() -> { //stop placing purple pixel
                                picker.update(Picker.PickerState.HOLD);
                            })
                            .back(14)
                            .turn(Math.toRadians(90))
                            .forward(14)
                            .strafeRight(parking_offset)
                            .forward(16)
                            .waitSeconds(1) // place yellow pixel
                            .waitSeconds(1) // park
                            .build();

                    rightTraj = drive.trajectorySequenceBuilder(initialMove.end())
                            .forward(28)
                            .turn(Math.toRadians(-90))
                            .forward(6)
                            .addDisplacementMarker(() -> { //start placing purple pixel
                                picker.update(Picker.PickerState.OUTAKE);
                            })
                            .waitSeconds(1)
                            .addDisplacementMarker(() -> { //stop placing purple pixel
                                picker.update(Picker.PickerState.HOLD);
                            })
                            .back(6)
                            .turn(Math.toRadians(90))
                            .back(14)
                            .turn(Math.toRadians(90))
                            .forward(14)
                            .strafeRight(parking_offset)
                            .forward(16)
                            .waitSeconds(1) // place yellow pixel
                            .waitSeconds(1) // park
                            .build();
                }
                break;
        }

        // Set starting pose

        drive.setPoseEstimate(startPose);


        /**
         * Now that we have built the trajectories, we need to:
         * 1. Do the initial move for detection
         * 2. Detect the spike mark with the prop (or assume closest to truss)
         * 3. Load the proper trajectory based on starting location into the
         *    trajectory sequence
         * 4. Run the trajectory we build out
         */
        TrajectorySequence propPositionTrajectory = null;
        SpikeMark location = null;

        waitForStart();
        if (opModeIsActive()){
            // 1. Do the initial move
            drive.followTrajectorySequence(initialMove);

            runtime.reset();
            // 2. Attempt to detect the prop for 5 seconds assume closes to truss if not
            while (opModeIsActive() & !propDetected & runtime.seconds() < 5) {
                telemetryTfod();

                // 2. attempt to detect the spike mark location, returns NONE if no detection
                location = tfObjectPropDetect.getSpikeMark(invertedDetection);
                telemetry.addData("Spike Mark Location", location.toString());
                telemetry.update();

                // 3. Load the trajectory based on the detected spike mark
                switch (location){
                    case LEFT:
                        propPositionTrajectory = leftTraj;
                        propDetected = true;
                        break;
                    case CENTER:
                        propPositionTrajectory = centerTraj;
                        propDetected = true;
                        break;
                    case RIGHT:
                        propPositionTrajectory = rightTraj;
                        propDetected = true;
                        break;
                    case NONE: // assume "right trajectory" or invert to left
                    default:
                        if(invertedDetection==true) {
                            propPositionTrajectory = leftTraj;
                        } else {
                            propPositionTrajectory = rightTraj;
                        }
                        propDetected = false;
                        break;
                }
            }
            // 4. Run the trajectory we built out
            drive.followTrajectorySequence(propPositionTrajectory);
        }
        visionPortal.close();

        // Print out detected spike mark location for debugging
        telemetry.addData("Spike Mark Location", location.toString());
        telemetry.update();
    }

    private void initTfod() {
        tfod = new TfodProcessor.Builder()
                .setModelAssetName(TFOD_MODEL_ASSET)
                .setModelLabels(LABELS)
                .setIsModelTensorFlow2(true)
                .setIsModelQuantized(true)
                .setModelInputSize(320)
                .setModelAspectRatio(16.0 / 9.0)
                .build();

        VisionPortal.Builder builder = new VisionPortal.Builder();

        builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));

        builder.setCameraResolution(new Size(CAMERA_WIDTH, CAMERA_HEIGHT));

        builder.enableLiveView(true);

        builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);

        builder.setAutoStopLiveView(false);

        builder.addProcessor(tfod);

        visionPortal = builder.build();

        tfod.setMinResultConfidence(0.50f);
    }

    private void telemetryTfod() {
        double x = 0;
        double y = 0;

        List<Recognition> currentRecognitions = tfod.getRecognitions();
        telemetry.addData("# Objects Detected", currentRecognitions.size());

        for (Recognition recognition : currentRecognitions) {
            if (recognition.getConfidence() > highestConf){
                highestConf = recognition.getConfidence();
                highestXDistance = recognition.getLeft();
                highestXDistanceLabel = recognition.getLabel();

                x = (recognition.getLeft() + recognition.getRight()) / 2 ;
                y = (recognition.getTop()  + recognition.getBottom()) / 2 ;
            }

            telemetry.addData(""," ");
            telemetry.addData("Image", "%s (%.0f %% Conf.)", highestXDistanceLabel, highestConf * 100);
            telemetry.addData("- Position", "%.0f / %.0f", x, y);
            telemetry.addData("- Size", "%.0f x %.0f", recognition.getWidth(), recognition.getHeight());
        }   // end for() loop
    }

    /**
     * TODO: Function to abstract setting initial move for TFOD
     * @param startingGrid
     * @return
     */
    public TrajectorySequence setInitialMove(String startingGrid) {
        return null;
    }

    /**
     * Print the menu selection
     */
    private void printMenuSelections() {
        telemetry.clear();
        telemetry.addLine("Alliance Color: " + selectionMenu.getAllianceColor());
        telemetry.addLine("Start Position: " + selectionMenu.getFieldStartPosition());
        telemetry.addLine("Park Position: " + selectionMenu.getFieldParkPosition());
        telemetry.update();
    }
}