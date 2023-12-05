package org.firstinspires.ftc.teamcode.opmode;

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
import org.firstinspires.ftc.teamcode.subsystems.auton.TrajectoryConfig;
import org.firstinspires.ftc.teamcode.subsystems.menu.SelectionMenu;
import org.firstinspires.ftc.teamcode.subsystems.menu.SelectionMenu.AllianceColor;
import org.firstinspires.ftc.teamcode.subsystems.menu.SelectionMenu.FieldParkPosition;
import org.firstinspires.ftc.teamcode.subsystems.menu.SelectionMenu.FieldStartPosition;
import org.firstinspires.ftc.teamcode.subsystems.menu.SelectionMenu.MenuState;
import org.firstinspires.ftc.teamcode.subsystems.menu.SelectionMenu.StartDelay;
import org.firstinspires.ftc.teamcode.subsystems.vision.SpikeMark;
import org.firstinspires.ftc.teamcode.subsystems.vision.TFObjectPropDetect;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.List;

@Autonomous(name = "Competition Auton", group = "Auton")
public class Production_Auton extends LinearOpMode {

    public ElapsedTime runtime = new ElapsedTime();
    public enum StagePosition {
        APRON,
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

    public String labelToDetect = "cup";
    private TfodProcessor tfod;

    public VisionPortal visionPortal;
    public boolean propDetected = false;
    SelectionMenu selectionMenu = new SelectionMenu(this,telemetry);
    boolean invertedDetection = false; // invert detections based on starting position
    StagePosition stagePosition = StagePosition.BACKSTAGE;
    Picker picker;
    Placer placer;

    public void runOpMode() throws InterruptedException{

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

        initTfod();
        tfObjectPropDetect = new TFObjectPropDetect(tfod, CAMERA_WIDTH, labelToDetect);

        picker = new Picker(hardwareMap);
        placer = new Placer(hardwareMap);

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        TrajectoryConfig trajectoryConfig = new TrajectoryConfig(drive);

        AllianceColor allianceColor = selectionMenu.getAllianceColor();
        FieldStartPosition fieldStartPosition = selectionMenu.getFieldStartPosition();
        FieldParkPosition fieldParkPosition = selectionMenu.getFieldParkPosition();
        double startDelay = selectionMenu.getStartDelay();



        switch(allianceColor){
            case RED:
                if(fieldStartPosition == FieldStartPosition.RIGHT) {
                    invertedDetection = true; // F4 RIGHT detect == A4 LEFT detect
                } else {
                    stagePosition = StagePosition.APRON;
                }
                break;
            case BLUE:
            default:
                if(fieldStartPosition == FieldStartPosition.RIGHT) {
                    stagePosition = StagePosition.APRON;
                    invertedDetection = true; // A2 has inverted detections compared to A4
                }
                break;
        }

        Pose2d startPose = trajectoryConfig.getStartPose(allianceColor, stagePosition);
        Pose2d initialMovePos = trajectoryConfig.getInitialMovePose(allianceColor, stagePosition);

        TrajectorySequence initialMove = drive.trajectorySequenceBuilder(startPose)
                .strafeTo(new Vector2d(initialMovePos.getX(), initialMovePos.getY()))
                .build();

        drive.setPoseEstimate(startPose);

        SpikeMark location = null;

        waitForStart();
        if (opModeIsActive()){

            runtime.reset();

            while (opModeIsActive() && runtime.seconds() < startDelay){
                telemetry.clear();
                telemetry.addData("Status", "Delaying for " + startDelay + " seconds...");
                telemetry.addData("Run time", "Seconds - " + runtime.seconds());
                telemetry.update();
            }

            drive.followTrajectorySequence(initialMove);

            runtime.reset();

            while (opModeIsActive() & !propDetected & runtime.seconds() < 3) {
                telemetryTfod();

                location = tfObjectPropDetect.getSpikeMark(invertedDetection);
                telemetry.addData("Spike Mark Location", location.toString());
                telemetry.update();

                if (location != SpikeMark.NONE && location != null){
                    propDetected = true;
                } else {
                    if (invertedDetection) {
                        location = SpikeMark.LEFT;
                    } else {
                        location = SpikeMark.RIGHT;
                    }
                }

            }

            Pose2d spikeMarkPos = trajectoryConfig.getSpikeMarkPose(location, allianceColor, stagePosition);
            Pose2d commonPos = trajectoryConfig.getCommonMarkPose(allianceColor);
            Pose2d boardPos = trajectoryConfig.getBoardPose(location, allianceColor, stagePosition);
            Pose2d parkPos = trajectoryConfig.getParkPose(fieldParkPosition, allianceColor);
            Pose2d apronSafePos = trajectoryConfig.getApronSafePose(allianceColor);
            Pose2d apronTrussPos = trajectoryConfig.getApronTrussPose(allianceColor);


            TrajectorySequence spikeMarkTraj;
            TrajectorySequence boardTraj;
            TrajectorySequence parkTraj;

            if (stagePosition == StagePosition.APRON){

                spikeMarkTraj = drive.trajectorySequenceBuilder(initialMove.end())
                        .lineToLinearHeading(spikeMarkPos) // line to spike mark
                        .build();


                boardTraj = drive.trajectorySequenceBuilder(spikeMarkTraj.end())
                        .lineToLinearHeading(apronSafePos) // get in position to go under truss
                        .lineToConstantHeading(new Vector2d(apronTrussPos.getX(), apronTrussPos.getY())) // go under truss
                        .splineToConstantHeading(new Vector2d(boardPos.getX(), boardPos.getY()), Math.toRadians(0)) // get to board
                        .build();

                parkTraj = drive.trajectorySequenceBuilder(boardTraj.end())
                        .splineToLinearHeading(parkPos, Math.toRadians(0))
                        .build();

            } else {

                spikeMarkTraj = drive.trajectorySequenceBuilder(initialMove.end())
                        .splineToLinearHeading(spikeMarkPos, Math.toRadians(180))
                        .build();

                boardTraj = drive.trajectorySequenceBuilder(spikeMarkTraj.end())
                        .lineToLinearHeading(boardPos)
                        .build();


                parkTraj = drive.trajectorySequenceBuilder(boardTraj.end())
                        .lineToLinearHeading(parkPos)
                        .build();

            }

            drive.followTrajectorySequence(spikeMarkTraj);
            picker.auton_place_spike(Picker.PickerState.AUTON, 1.5, runtime);
            picker.auton_place_spike(Picker.PickerState.HOLD, 0.1, runtime);
            placer.auton_deploy_elevator(Placer.PlacerState.STOW, 0.2, runtime);
            drive.followTrajectorySequence(boardTraj);
            placer.auton_deploy_elevator(Placer.PlacerState.DEPLOY, 2, runtime);
            placer.auton_deploy_elevator(Placer.PlacerState.PLACE_SECOND, 0.2, runtime);
            placer.auton_deploy_elevator(Placer.PlacerState.READY_TO_INTAKE, 1, runtime);
            drive.followTrajectorySequence(parkTraj);
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

        tfod.setMinResultConfidence(0.60f);
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
}