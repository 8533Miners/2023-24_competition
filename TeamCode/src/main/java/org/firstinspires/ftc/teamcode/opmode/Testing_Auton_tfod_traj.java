package org.firstinspires.ftc.teamcode.opmode;

import android.util.Size;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.vision.SpikeMark;
import org.firstinspires.ftc.teamcode.subsystems.vision.TFObjectPropDetect;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.List;

@Autonomous(name = "Auton w/ TFOD", group = "TestAuton")
public class Testing_Auton_tfod_traj extends LinearOpMode {
    TFObjectPropDetect tfObjectPropDetect;
    public int CAMERA_WIDTH = 640;
    int CAMERA_HEIGHT = 480;
    public String labelToDetect = "prop";
    public boolean propDetected = false;
    float highestConf = 0;
    float highestXDistance = 0;
    String highestXDistanceLabel = " ";
    public ElapsedTime runtime = new ElapsedTime();
    private static final String TFOD_MODEL_ASSET = "model_20231023_193833.tflite";
    private static final String[] LABELS = {
            "prop"
    };
    private TfodProcessor tfod;
    public VisionPortal visionPortal;

    // This variable should be dynamically set by the user interface selection, or we need to make 4x classes for each starting position
    String startingGrid = "A2";
    boolean invertedDetection = false;
    int A4_starting_x = 16;
    int A4_starting_y = 62;
    int starting_heading_A = 270;


    //String startingGrid = "A2";
    int A2_starting_x = -40;
    int A2_starting_y = 62;


    //String startingGrid = "F4";
    int F4_starting_x = 16;
    int F4_starting_y = -62;
    int starting_heading_F = 90;


    //String startingGrid = "F2";
    int F2_starting_x = -40;
    int F2_starting_y = -62;

    double trackWidth = 11.0;
    double robotLength = 16.0;
    double robotWidth = 13.5;

    public void runOpMode() throws InterruptedException{
        int startingX = A4_starting_x;
        int startingY = A4_starting_y;
        int startingHeading = starting_heading_A;

        initTfod();

        tfObjectPropDetect = new TFObjectPropDetect(tfod, CAMERA_WIDTH, labelToDetect);

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        // set the starting position
        switch (startingGrid) {
            case "A4":
                invertedDetection = false;
                startingX = A4_starting_x;
                startingY = A4_starting_y;
                startingHeading = starting_heading_A;
                break;
            case "A2":
                invertedDetection = true;
                startingX = A2_starting_x;
                startingY = A2_starting_y;
                startingHeading = starting_heading_A;
                break;
            case "F4":
                invertedDetection = true;
                startingX = F4_starting_x;
                startingY = F4_starting_y;
                startingHeading = starting_heading_F;
                break;
            case "F2":
            default: // F2
                invertedDetection = false;
                startingX = F2_starting_x;
                startingY = F2_starting_y;
                startingHeading = starting_heading_F;
                break;
        }

        Pose2d startPose = new Pose2d(startingX,startingY, Math.toRadians(startingHeading));

        drive.setPoseEstimate(startPose);

        TrajectorySequence initialMove;

        TrajectorySequence leftTraj;
        // Set Left Trajectories
        switch (startingGrid) {
            case "A4":
                initialMove = drive.trajectorySequenceBuilder(startPose)
                        .strafeLeft(4)
                        .build();
                leftTraj = drive.trajectorySequenceBuilder(initialMove.end())
                        .strafeLeft(8)
                        .forward(26)
                        .waitSeconds(3) // place purple pixel
                        .back(12)
                        .turn(Math.toRadians(90))
                        .forward(22)
                        .waitSeconds(1) // place yellow pixel
                        .waitSeconds(1) // park
                        .build();
                break;
            case "A2":
                initialMove = drive.trajectorySequenceBuilder(startPose)
                        .strafeRight(4)
                        .build();
                leftTraj = drive.trajectorySequenceBuilder(initialMove.end())
                        .forward(28)
                        .turn(Math.toRadians(90))
                        .forward(6)
                        .waitSeconds(3) // place purple pixel
                        .back(6)
                        .strafeLeft(24)
                        .forward(86)
                        .waitSeconds(1) // place yellow pixel
                        .waitSeconds(1) // park
                        .build();
                break;
            case "F4":
                initialMove = drive.trajectorySequenceBuilder(startPose)
                        .strafeRight(4)
                        .build();
                leftTraj = drive.trajectorySequenceBuilder(initialMove.end())
                        .forward(28)
                        .turn(Math.toRadians(90))
                        .forward(6)
                        .waitSeconds(3) // place purple pixel
                        .back(6)
                        .turn(Math.toRadians(-90))
                        .back(14)
                        .turn(Math.toRadians(-90))
                        .forward(30)
                        .waitSeconds(1) // place yellow pixel
                        .waitSeconds(1) // park
                        .build();
                break;
            case "F2":
            default: // F2
                initialMove = drive.trajectorySequenceBuilder(startPose)
                        .strafeLeft(4)
                        .build();
                leftTraj = drive.trajectorySequenceBuilder(initialMove.end())
                        .strafeLeft(6)
                        .forward(26)
                        .waitSeconds(3) // place purple pixel
                        .back(14)
                        .turn(Math.toRadians(-90))
                        .strafeRight(10)
                        .forward(94)
                        .waitSeconds(1) // place yellow pixel
                        .waitSeconds(1) // park
                        .build();
                break;
        }

        TrajectorySequence centerTraj;
        // Set Center Trajectories
        switch (startingGrid) {
            case "A4":
                centerTraj = drive.trajectorySequenceBuilder(initialMove.end())
                        .forward(28)
                        .waitSeconds(3) // place purple pixel
                        .back(14)
                        .turn(Math.toRadians(90))
                        .forward(30)
                        .waitSeconds(1) // place yellow pixel
                        .waitSeconds(1) // park
                        .build();
                break;
            case "A2":
                centerTraj = drive.trajectorySequenceBuilder(initialMove.end())
                        .strafeLeft(2)
                        .forward(28)
                        .waitSeconds(3) // place purple pixel
                        .back(24)
                        .turn(Math.toRadians(90))
                        .forward(84)
                        .waitSeconds(1) // place yellow pixel
                        .waitSeconds(1) // park
                        .build();
                break;
            case "F4":
                centerTraj = drive.trajectorySequenceBuilder(initialMove.end())
                        .forward(28)
                        .waitSeconds(3) // place purple pixel
                        .back(14)
                        .turn(Math.toRadians(-90))
                        .forward(30)
                        .waitSeconds(1) // place yellow pixel
                        .waitSeconds(1) // park
                        .build();
                break;
            case "F2":
            default:
                centerTraj = drive.trajectorySequenceBuilder(initialMove.end())
                        .forward(28)
                        .waitSeconds(3) // place purple pixel
                        .back(24)
                        .turn(Math.toRadians(-90))
                        .forward(86)
                        .waitSeconds(1) // place yellow pixel
                        .waitSeconds(1) // park
                        .build();
                break;
        }

        TrajectorySequence rightTraj;
        // Set Right Trajectories
        switch (startingGrid) {
            case "A4":
                rightTraj = drive.trajectorySequenceBuilder(initialMove.end())
                        .forward(28)
                        .turn(Math.toRadians(-90))
                        .forward(6)
                        .waitSeconds(3) // place purple pixel
                        .back(6)
                        .turn(Math.toRadians(90))
                        .back(14)
                        .turn(Math.toRadians(90))
                        .forward(30)
                        .waitSeconds(1) // place yellow pixel
                        .waitSeconds(1) // park
                        .build();
                break;
            case "A2":
                rightTraj = drive.trajectorySequenceBuilder(initialMove.end())
                        .strafeRight(6)
                        .forward(26)
                        .waitSeconds(3) // place purple pixel
                        .back(14)
                        .turn(Math.toRadians(90))
                        .strafeLeft(10)
                        .forward(94)
                        .waitSeconds(1) // place yellow pixel
                        .waitSeconds(1) // park
                        .build();
                break;
            case "F4":
                rightTraj = drive.trajectorySequenceBuilder(initialMove.end())
                        .strafeRight(8)
                        .forward(26)
                        .waitSeconds(3) // place purple pixel
                        .back(12)
                        .turn(Math.toRadians(-90))
                        .forward(22)
                        .waitSeconds(1) // place yellow pixel
                        .waitSeconds(1) // park
                        .build();
                break;
            case "F2":
            default:
                rightTraj = drive.trajectorySequenceBuilder(initialMove.end())
                        .forward(28)
                        .turn(Math.toRadians(-90))
                        .forward(6)
                        .waitSeconds(3) // place purple pixel
                        .back(6)
                        .strafeRight(24)
                        .forward(86)
                        .waitSeconds(1) // place yellow pixel
                        .waitSeconds(1) // park
                        .build();
                break;
        }

        TrajectorySequence propPositionTrajectory = null;

        String trajectoryprint = "";
        SpikeMark location = null;
        waitForStart();
        if (opModeIsActive()){
            drive.followTrajectorySequence(initialMove);

            runtime.reset();
            while (opModeIsActive() & !propDetected & runtime.seconds() < 5) { // move on if detection taking longer than 5 seconds.
                telemetryTfod();
                location = tfObjectPropDetect.getSpikeMark(invertedDetection);
                telemetry.addData("Spike Mark Location", location.toString());
                telemetry.update();
                switch (location){
                    case NONE: // assume RIGHT
                        if(invertedDetection==true) {
                            trajectoryprint = "left";
                            propPositionTrajectory = leftTraj;
                        } else {

                            trajectoryprint = "right";
                            propPositionTrajectory = rightTraj;
                        }
                        propDetected = false;
                        break;
                    case LEFT:

                        trajectoryprint = "left";
                        propPositionTrajectory = leftTraj;
                        propDetected = true;
                        break;
                    case CENTER:

                        trajectoryprint = "center";
                        propPositionTrajectory = centerTraj;
                        propDetected = true;
                        break;
                    case RIGHT:

                        trajectoryprint = "right";
                        propPositionTrajectory = rightTraj;
                        propDetected = true;
                    default:
                        if(invertedDetection==true) {

                            trajectoryprint = "left";
                            propPositionTrajectory = leftTraj;
                        } else {

                            trajectoryprint = "right";
                            propPositionTrajectory = rightTraj;
                        }
                }
            }

            telemetry.addData("Spike Mark Location", location.toString());
            telemetry.addData("Trajectory", trajectoryprint);
            telemetry.update();
            drive.followTrajectorySequence(propPositionTrajectory);
        }
        visionPortal.close();
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
}