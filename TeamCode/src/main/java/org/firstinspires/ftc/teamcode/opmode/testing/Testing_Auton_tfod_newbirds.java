package org.firstinspires.ftc.teamcode.opmode.testing;

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

//@Disabled
@Autonomous(name = "Test Auton Camera Stream - new bird", group = "TestAuton")
public class Testing_Auton_tfod_newbirds extends LinearOpMode {
    TFObjectPropDetect tfObjectPropDetect;
    public int CAMERA_WIDTH = 640;
    int CAMERA_HEIGHT = 480;
    public String labelToDetect = "prop";
    public boolean propDetected = false;
    float highestConf = 0;
    float highestXDistance = 0;
    String highestXDistanceLabel = " ";
    public ElapsedTime runtime = new ElapsedTime();
    private static final String TFOD_MODEL_ASSET = "model_20231104_103203.tflite";
    private static final String[] LABELS = {
            "prop"
    };
    private TfodProcessor tfod;
    public VisionPortal visionPortal;

    public void runOpMode() throws InterruptedException{
        initTfod();

        tfObjectPropDetect = new TFObjectPropDetect(tfod, CAMERA_WIDTH, labelToDetect);

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        Pose2d startPose = new Pose2d(12,56, Math.toRadians(270));

        drive.setPoseEstimate(startPose);

        TrajectorySequence initialMove = drive.trajectorySequenceBuilder(startPose)
                .strafeLeft(4)
                .build();

        TrajectorySequence leftTraj = drive.trajectorySequenceBuilder(initialMove.end())
                .strafeLeft(5)
//                .turn(Math.toRadians(0)) // rotate robot towards center
//                .forward(12)
//                .waitSeconds(3) // place purple pixel
//                .turn(Math.toRadians(90)) // rotate towards backdrop
//                .forward(39)
//                .waitSeconds(3) // place yellow pixel
                .build();

        TrajectorySequence centerTraj = drive.trajectorySequenceBuilder(initialMove.end())
                .forward(5)
//                .turn(Math.toRadians(30)) // rotate robot towards center
//                .forward(12)
//                .waitSeconds(3) // place purple pixel
//                .turn(Math.toRadians(90)) // rotate towards backdrop
//                .forward(39)
//                .waitSeconds(3) // place yellow pixel
                .build();

        TrajectorySequence rightTraj = drive.trajectorySequenceBuilder(initialMove.end())
                .strafeRight(10)
//                .turn(Math.toRadians(60)) // rotate robot towards center
//                .forward(12)
//                .waitSeconds(3) // place purple pixel
//                .turn(Math.toRadians(90)) // rotate towards backdrop
//                .forward(39)
//                .waitSeconds(3) // place yellow pixel
                .build();

        TrajectorySequence propPositionTrajectory = rightTraj;

        boolean invertedDetection = false;

        waitForStart();
        if (opModeIsActive()){
            drive.followTrajectorySequence(initialMove);

            runtime.reset();
            while (opModeIsActive() & !propDetected & runtime.seconds() < 5) { // move on if detection taking longer than 5 seconds.
                telemetryTfod();
                SpikeMark location = tfObjectPropDetect.getSpikeMark(invertedDetection);
                telemetry.addData("Spike Mark Location", location.toString());
                telemetry.update();
                switch (location){
                    case NONE:
                        propDetected = false;
                        break;
                    case LEFT:
                        propPositionTrajectory = leftTraj;
                        propDetected = true;
                        break;
                    case CENTER:
                        propPositionTrajectory = centerTraj;
                        propDetected = true;
                        break;
                    default:
                        propPositionTrajectory = centerTraj;
                }
            }

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

        tfod.setMinResultConfidence(0.45f);
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