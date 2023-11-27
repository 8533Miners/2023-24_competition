package org.firstinspires.ftc.teamcode.opmode.testing;

import android.util.Size;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.Picker;
import org.firstinspires.ftc.teamcode.subsystems.Placer;
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

@Autonomous(name = "New Trajectory Test", group = "TestAuton")
public class Testing_Auton_jjenkins extends LinearOpMode {
    // Random Robot Variables
    public ElapsedTime runtime = new ElapsedTime();
    public ElapsedTime purpleTime = new ElapsedTime();
    public ElapsedTime yellowTime = new ElapsedTime();
    public int purpleWait = 1;
    public int yellowWait = 1;
    enum State {
        INITIAL_MOVE,
        DETECTION_TRAJECTORY,
        SCORE_PURPLE_PIXEL,
        SCORE_PURPLE_WAIT,
        MOVE_TO_COMMON_TRAJECTORY,
        SCORE_YELLOW_PIXEL,
        SCORE_YELLOW_WAIT,
        PARK_TRAJECTORY,
        IDLE
    }

    State currentState = State.IDLE;

    enum StartingSide {
        CURTAIN,
        BACKSTAGE
    }

    // Camera Property Variables
    public int CAMERA_WIDTH = 640;
    int CAMERA_HEIGHT = 480;
    float highestConf = 0;
    float highestXDistance = 0;
    String highestXDistanceLabel = " ";

    // TFOD Variables
    TFObjectPropDetect tfObjectPropDetect;

    // BIRD MODEL
    //private static final String TFOD_MODEL_ASSET = "model_20231104_103203.tflite";
    //private static final String[] LABELS = {
    //        "prop"
    //};

    // CUP MODEL
    private static final String TFOD_MODEL_ASSET = "ssd_mobilenet_v2_320x320_coco17_tpu_8.tflite";
    private static final String[] LABELS = {
            "person",
            "bicycle",
            "car",
            "motorcycle",
            "airplane",
            "bus",
            "train",
            "truck",
            "boat",
            "traffic light",
            "fire hydrant",
            "???",
            "stop sign",
            "parking meter",
            "bench",
            "bird",
            "cat",
            "dog",
            "horse",
            "sheep",
            "cow",
            "elephant",
            "bear",
            "zebra",
            "giraffe",
            "???",
            "backpack",
            "umbrella",
            "???",
            "???",
            "handbag",
            "tie",
            "suitcase",
            "frisbee",
            "skis",
            "snowboard",
            "sports ball",
            "kite",
            "baseball bat",
            "baseball glove",
            "skateboard",
            "surfboard",
            "tennis racket",
            "bottle",
            "???",
            "wine glass",
            "cup",
            "fork",
            "knife",
            "spoon",
            "bowl",
            "banana",
            "apple",
            "sandwich",
            "orange",
            "broccoli",
            "carrot",
            "hot dog",
            "pizza",
            "donut",
            "cake",
            "chair",
            "couch",
            "potted plant",
            "bed",
            "???",
            "dining table",
            "???",
            "???",
            "toilet",
            "???",
            "tv",
            "laptop",
            "mouse",
            "remote",
            "keyboard",
            "cell phone",
            "microwave",
            "oven",
            "toaster",
            "sink",
            "refrigerator",
            "???",
            "book",
            "clock",
            "vase",
            "scissors",
            "teddy bear",
            "hair drier",
            "toothbrush",
    };

    public String labelToDetect = "prop";
    private TfodProcessor tfod;
    public VisionPortal visionPortal;
    public boolean propDetected = false;

    // Menu variables
    SelectionMenu selectionMenu = new SelectionMenu(this,telemetry);

    /**
     * Starting position constants
     */
    // Starting Position A2, invert for F2 position
    final double BACKSTAGE_STARTING_X_POSITION = -40;
    // Starting Position A4, invert for F4 position
    final double CURTAIN_STARTING_X_POSITION = 16;
    // Starting Y is always the same for blue team (A), invert for red team (F)
    final double STARTING_Y = 62;
    // variable definitions to defaults
    double starting_x;// = BACKSTAGE_STARTING_X_POSITION;

    // Starting heading for blue team (A), invert for red team (F)
    final int STARTING_HEADING = 270;

    // Coordinates for board center on middle AprilTag on blue side(A).
    // Mirror Y for red side (F).
    final double BOARD_CENTER_X = 48;
    final double BOARD_CENTER_Y = 35;

    // Parking offset values. Determines where to park in the apron
    final double PARK_WALL = 24;
    final double PARK_BOARD = 0;
    final double PARK_CENTER = -24;
    double parking_offset;// = PARK_CENTER; //default

    // Offset value from center of board for the left/right april tags
    // The code will automatically invert if we are red vs blue
    final double RIGHT_TAG = -6;
    final double CENTER_TAG = 0;
    final double LEFT_TAG = 6;
    double board_offset = CENTER_TAG; //default

    // invert = 1 for blue; invert = -1 for red
    final int NO_INVERT = 1;
    final int INVERT = -1;
    int invert; // = NO_INVERT; //default

    boolean invertedDetection = false; // invert detections based on starting position

    StartingSide startingSide = StartingSide.CURTAIN;

    Picker picker;
    Placer placer;

    public void runOpMode() throws InterruptedException{
        double waitTime1 = 1.5;
        ElapsedTime waitTimer1 = new ElapsedTime();

        picker = new Picker(hardwareMap);
        placer = new Placer(hardwareMap);

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

        /**
         * Determine parking offset amount based on menu selection
         */
        switch(fieldParkPosition){
            default:
            case NEAR_WALL:
                parking_offset = PARK_WALL;
                break;
            case ON_BACKDROP:
                parking_offset = PARK_BOARD;
                break;
            case NEAR_CENTER:
                parking_offset = PARK_CENTER;
                break;
        }

        /**
         * Set starting positions and inversion or not based on menu selection
         */
        double initialMovePos = 4;
        switch(allianceColor){
            case RED:
                invert = INVERT;
                if(FieldStartPosition.RIGHT == fieldStartPosition) {
                    starting_x = CURTAIN_STARTING_X_POSITION;
                    startingSide = StartingSide.BACKSTAGE;
                    invertedDetection = true; // F4 has inverted detections compared to A4
                } else {
                    starting_x = BACKSTAGE_STARTING_X_POSITION;
                    initialMovePos = -4;
                }
                break;
            case BLUE:
            default:
                invert = NO_INVERT;
                if(FieldStartPosition.RIGHT == fieldStartPosition) {
                    starting_x = BACKSTAGE_STARTING_X_POSITION;
                    initialMovePos = -4;
                    invertedDetection = true; // A2 has inverted detections compared to A4
                } else {
                    starting_x = CURTAIN_STARTING_X_POSITION;
                    startingSide = StartingSide.BACKSTAGE;
                }
                break;
        }

        //telemetry.addData("starting X", starting_x);
        //telemetry.addData("starting y", STARTING_Y);
        //telemetry.update();

        Pose2d startPose = new Pose2d(starting_x, invert*STARTING_Y, Math.toRadians(invert*STARTING_HEADING));

        // Set starting pose
        drive.setPoseEstimate(startPose);

        TrajectorySequence initialMove = drive.trajectorySequenceBuilder(startPose)
                .lineTo(new Vector2d(starting_x + initialMovePos, invert * STARTING_Y)) //initialMove
                .build();

        TrajectorySequence trajectorySequence;

        /**
         * We need to:
         * 1. Do the initial move for detection
         * 2. Detect the spike mark with the prop (or assume closest to truss)
         * 3. Build the proper trajectory based on prop location intro trajectory
         *    sequence.
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
                    case CENTER:
                    case RIGHT:
                        propDetected = true;
                        break;
                    case NONE: // assume "right trajectory" or invert to left
                    default:
                        if(invertedDetection==true) {
                            location = SpikeMark.LEFT;
                        } else {
                            location = SpikeMark.RIGHT;
                        }
                        propDetected = false;
                        break;
                }
            }

            /**
             * Detect prop and then choose locations based on data from detection
             */
            double spikeMark_X;
            double spikeMark_Y;

            /*
            // Flip the coordinates if our spike marks detection was inverted
            if(invertedDetection==true) {
                if(location.equals(SpikeMark.LEFT)){
                    location = SpikeMark.RIGHT;
                } else if(location.equals(SpikeMark.RIGHT)) {
                    location = SpikeMark.LEFT;
                }
            } */

            if(startingSide.equals(StartingSide.CURTAIN)) {
                switch (location) {
                    case LEFT:
                        spikeMark_X = -32;
                        spikeMark_Y = invert * 34;
                        break;
                    case RIGHT:
                        spikeMark_X = -54;
                        spikeMark_Y = invert * 34;
                        break;
                    case CENTER:
                    default:
                        spikeMark_X = -40;
                        spikeMark_Y = invert * 24;
                        break;
                }

                // Trajectory code
                trajectorySequence = drive.trajectorySequenceBuilder(startPose)
                        .splineToLinearHeading(new Pose2d(spikeMark_X, spikeMark_Y, Math.toRadians(invert * STARTING_HEADING+90*invert)), Math.toRadians(360))
                        .addDisplacementMarker(() -> { //start placing purple pixel
                            picker.update(Picker.PickerState.OUTAKE);
                        })
                        .waitSeconds(1)
                        .addDisplacementMarker(() -> { //stop placing purple pixel
                            picker.update(Picker.PickerState.HOLD);
                        })
                        .setTangent(Math.toRadians(180))
                        .splineToLinearHeading(new Pose2d(starting_x, invert * 60, Math.toRadians(180)), Math.toRadians(360))
                        .lineToLinearHeading(new Pose2d(14, invert * 60, Math.toRadians(180)))
                        // ** Same code below (clean up?)
                        .splineToLinearHeading(new Pose2d(53.5, invert * 35 + board_offset, Math.toRadians(180)), Math.toRadians(0))
                        .addDisplacementMarker(() -> {
                            placer.update(Placer.PlacerState.DEPLOY);
                        })
                        .waitSeconds(1) // Wait for elevator/placer to get to position
                        .addDisplacementMarker(() -> {
                            placer.update(Placer.PlacerState.PLACE_SECOND);
                        })
                        .waitSeconds(0.5) // Wait for gripper to release
                        .addDisplacementMarker(() -> {
                            placer.update(Placer.PlacerState.READY_TO_INTAKE);
                        })
                        .lineTo(new Vector2d(49, invert * 35 + board_offset))
                        .splineToLinearHeading(new Pose2d(BOARD_CENTER_X, invert * BOARD_CENTER_Y + invert * parking_offset, Math.toRadians(180)), Math.toRadians(0))
                        .build();
                // ** end same code
            } else {
                switch (location) {
                    case LEFT:
                        spikeMark_X = 32;
                        spikeMark_Y = invert * 34;
                        break;
                    case RIGHT:
                        spikeMark_X = 4;
                        spikeMark_Y = invert * 34;
                        break;
                    case CENTER:
                    default:
                        spikeMark_X = 20;
                        spikeMark_Y = invert * 24;
                        break;
                }
                // Actual trajectory code
                trajectorySequence = drive.trajectorySequenceBuilder(startPose)
                        .lineTo(new Vector2d(starting_x + initialMovePos, invert * STARTING_Y)) //initialMove
                        .splineToLinearHeading(new Pose2d(spikeMark_X, spikeMark_Y, Math.toRadians(invert * STARTING_HEADING-90*invert)), Math.toRadians(180))
                        .addDisplacementMarker(() -> { //start placing purple pixel
                            picker.update(Picker.PickerState.OUTAKE);
                        })
                        .waitSeconds(1)
                        .addDisplacementMarker(() -> { //stop placing purple pixel
                            picker.update(Picker.PickerState.HOLD);
                        })
                        .setTangent(Math.toRadians(0))
                        // ** Same code below (clean up?)
                        .splineToLinearHeading(new Pose2d(53.5, invert * 35 + board_offset, Math.toRadians(180)), Math.toRadians(0))
                        .addDisplacementMarker(() -> {
                            placer.update(Placer.PlacerState.DEPLOY);
                        })
                        .waitSeconds(1) // Wait for elevator/placer to get to position
                        .addDisplacementMarker(() -> {
                            placer.update(Placer.PlacerState.PLACE_SECOND);
                        })
                        .waitSeconds(0.5) // Wait for gripper to release
                        .addDisplacementMarker(() -> {
                            placer.update(Placer.PlacerState.READY_TO_INTAKE);
                        })
                        .lineTo(new Vector2d(49, invert * 35 + board_offset))
                        .splineToLinearHeading(new Pose2d(BOARD_CENTER_X, invert * BOARD_CENTER_Y + invert * parking_offset, Math.toRadians(180)), Math.toRadians(0))
                        .build();
                // ** end same code
            }

            while(opModeIsActive() && !isStopRequested()){
                purpleTime.reset();
                switch (currentState) {
                    case INITIAL_MOVE:
                        if(!drive.isBusy()){
                            currentState = State.DETECTION_TRAJECTORY;
                            drive.followTrajectorySequenceAsync(trajectorySequence);
                        }
                    case DETECTION_TRAJECTORY:
                        if(!drive.isBusy()){
                            currentState = State.SCORE_PURPLE_PIXEL;
                            drive.followTrajectorySequenceAsync(trajectorySequence); // TODO: move trajectory sequence logic into separate trajectory to enable separate state tracking
                        }
                    case SCORE_PURPLE_PIXEL:
                        if(!drive.isBusy()){
                            currentState = State.MOVE_TO_COMMON_TRAJECTORY;
                            picker.update(Picker.PickerState.OUTAKE);
                        }
                    case SCORE_PURPLE_WAIT:
                        if(purpleTime.seconds() <= purpleWait){
                            picker.update(Picker.PickerState.OUTAKE);
                        }
                }


            }


            // 4. Run the trajectory we built out
            drive.followTrajectorySequence(trajectorySequence);
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

        tfod.setMinResultConfidence(0.45f);
    }

    private void telemetryTfod() {
        double x = 0;
        double y = 0;

        List<Recognition> currentRecognitions = tfod.getRecognitions();

        telemetry.clearAll();
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