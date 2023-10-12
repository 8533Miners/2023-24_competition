package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Testing_Auton_tfod.SpikeMark.CENTER;

import android.util.Size;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.List;

@Autonomous(name = "Blue Backstage", group = "TestAuton")
public class Testing_Auton_tfod extends LinearOpMode {
    float highestConf = 0;
    float highestXDistance = 0;
    String highestXDistanceLabel = " ";
    public ElapsedTime runtime = new ElapsedTime();
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
    private TfodProcessor tfod;
    public VisionPortal visionPortal;
    detectPropLocation propLocation;
    enum SpikeMark {
        LEFT,
        CENTER,
        RIGHT
    }
    public void runOpMode() throws InterruptedException{
        initTfod();

        propLocation = new detectPropLocation();

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        Pose2d startPose = new Pose2d(12,56, Math.toRadians(270));

        drive.setPoseEstimate(startPose);

        TrajectorySequence initialMove = drive.trajectorySequenceBuilder(startPose)
                .forward(12) // Robot drives forward
                .turn(Math.toRadians(-30)) // robot pivots
                .waitSeconds(3) // do you Identify Prop
                .build();

        TrajectorySequence centerTraj = drive.trajectorySequenceBuilder(initialMove.end())
                .turn(Math.toRadians(30)) // rotate robot towards center
                .forward(12)
                .waitSeconds(3) // place purple pixel
                .turn(Math.toRadians(90)) // rotate towards backdrop
                .forward(39)
                .waitSeconds(3) // place yellow pixel
                .build();

        TrajectorySequence leftTraj = drive.trajectorySequenceBuilder(initialMove.end())
                .turn(Math.toRadians(30)) // rotate robot towards center
                .forward(12)
                .waitSeconds(3) // place purple pixel
                .turn(Math.toRadians(90)) // rotate towards backdrop
                .forward(39)
                .waitSeconds(3) // place yellow pixel
                .build();

        TrajectorySequence rightTraj = drive.trajectorySequenceBuilder(initialMove.end())
                .turn(Math.toRadians(30)) // rotate robot towards center
                .forward(12)
                .waitSeconds(3) // place purple pixel
                .turn(Math.toRadians(90)) // rotate towards backdrop
                .forward(39)
                .waitSeconds(3) // place yellow pixel
                .build();

        TrajectorySequence propPositionTrajectory = null;


        waitForStart();
        if (opModeIsActive()){
            runtime.reset();
            while (opModeIsActive() & runtime.seconds() < 5) { // move on if detection taking longer than 5 seconds.
                telemetryTfod();
                telemetry.update();
            }
            switch (propLocation.getPropPos()){
                case LEFT:
                    propPositionTrajectory = leftTraj;
                    break;
                case RIGHT:
                    propPositionTrajectory = rightTraj;
                    break;
                case CENTER:
                    propPositionTrajectory = centerTraj;
                    break;
                default:
                    propPositionTrajectory = centerTraj;
            }

            //drive.followTrajectorySequence(initialMove);
            //drive.followTrajectorySequence(propPositionTrajectory);
        }
        visionPortal.close();
    }
    public class detectPropLocation {
        /*
        Logic to detect, set, and return position of Prop
         */
        private SpikeMark identifyHorizontal() {
            double x = 0; // Defaults to the left position
            List<Recognition> currentRecognitions = tfod.getRecognitions();

            for (Recognition recognition : currentRecognitions) {
                if (recognition.getConfidence() > highestConf){
                    highestConf = recognition.getConfidence();
                    highestXDistance = recognition.getLeft();
                    highestXDistanceLabel = recognition.getLabel();

                    x = (recognition.getLeft() + recognition.getRight()) / 2 ;
                }
            }   // end for() loop
            if (x < 227) {
                return SpikeMark.LEFT;
            } else if (x >= 227 && x <= 453) {
                return SpikeMark.CENTER;
            } else return SpikeMark.RIGHT; //defaults to RIGHT
        }
        private volatile SpikeMark propPos = identifyHorizontal(); //default prop position
        public SpikeMark getPropPos() {return propPos;}
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

        builder.setCameraResolution(new Size(640, 480));

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
