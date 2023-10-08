package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Testing_BlueAuton.SpikeMark.CENTER;
import static org.firstinspires.ftc.teamcode.Testing_BlueAuton.SpikeMark.LEFT;
import static org.firstinspires.ftc.teamcode.Testing_BlueAuton.SpikeMark.RIGHT;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.trajectorysequence.sequencesegment.TrajectorySegment;

@Autonomous(name = "Blue Backstage", group = "TestAuton")
/*
Starting tile A4 (Blue alliance, backstage)
 */
public class Testing_BlueAuton extends LinearOpMode {
    detectPropLocation propLocation;
    enum SpikeMark {
        LEFT,
        CENTER,
        RIGHT
    }
    public void runOpMode() throws InterruptedException{
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

            drive.followTrajectorySequence(initialMove);
            drive.followTrajectorySequence(propPositionTrajectory);
        }
    }
    public class detectPropLocation {
        /*
        Logic to detect, set, and return position of Prop
         */
        private volatile SpikeMark propPos = CENTER; //default prop position
        public SpikeMark getPropPos() {return propPos;}
    }

}
