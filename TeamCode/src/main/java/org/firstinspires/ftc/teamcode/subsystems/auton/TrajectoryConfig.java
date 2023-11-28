package org.firstinspires.ftc.teamcode.subsystems.auton;

import com.acmerobotics.roadrunner.drive.MecanumDrive;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.opmode.testing.Testing_Auton_jjenkins.StagePosition;
import org.firstinspires.ftc.teamcode.subsystems.Picker;
import org.firstinspires.ftc.teamcode.subsystems.Placer;
import org.firstinspires.ftc.teamcode.subsystems.menu.SelectionMenu.FieldParkPosition;
import org.firstinspires.ftc.teamcode.subsystems.vision.SpikeMark;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

public class TrajectoryConfig {
    private SampleMecanumDrive rrdrive;
    Trajectory apronTraj;
    Trajectory backstageTraj;
    double spikeMark_Y;
    double spikeMark_X;
    double spikeMark_H;
    double starting_X;
    double starting_Y;
    double starting_H;
    boolean mirrored = false;
    public TrajectoryConfig(SampleMecanumDrive autonDrive){
        this.rrdrive = autonDrive;
    }
//    public Trajectory GenerateTrajectory(Pose2d startPose, Vector2d endPosition){
//        TrajectoryBuilder genTraj = rrdrive.trajectoryBuilder(startPose)
//                        .lineTo(new Vector2d(endPosition.getX(), endPosition.getY()));
//
//
//        return genTraj.build();
//    }
    public Pose2d GetParkPose(FieldParkPosition parkPosition, boolean invert, StagePosition stagePosition){
        return null;
    }
    public Pose2d GetStartPose(boolean invert, StagePosition stagePosition){
        starting_Y = invert ? 62 : -62;
        starting_H = invert ? 270 : 90;

        if (stagePosition == StagePosition.APRON){
            starting_X = -40;
            } else {
            starting_X = 16;
        }

        return new Pose2d(starting_X, starting_Y, Math.toRadians(starting_H));
    }
    public Pose2d GetSpikeMarkPose(SpikeMark detectedSpikeMark, boolean invert, StagePosition stagePosition){
        /**
         * invert is managing the inversion for red vs. blue alliance start positions.
         * stagePosition is managing the mirror for left vs. right start positions.
         * (a LEFT detection on the RIGHT start position is a mirror of a RIGHT detection
         * on the LEFT start position, etc)
         */
        spikeMark_Y = invert ? 34 : -34;

        switch(detectedSpikeMark){
            case LEFT:
                if (stagePosition == StagePosition.APRON){
                    spikeMark_X = -29;
                    spikeMark_H = Math.toRadians(305);
                } else {
                    spikeMark_X = 28;
                }
                break;
            case RIGHT:
                if (stagePosition == StagePosition.APRON) {
                    spikeMark_X = -45;
                } else {
                    spikeMark_X = 4;
                }
                break;
            case CENTER:
            default:
                if (stagePosition == StagePosition.APRON) {
                    spikeMark_X = -40;
                    spikeMark_Y = invert ? 29 : -29;
                } else {
                    spikeMark_X = 20;
                    spikeMark_Y = invert ? 24 : -24;
                }
                break;
        }
        return new Pose2d(spikeMark_X, spikeMark_Y, Math.toRadians(spikeMark_H));
    }
    public Pose2d GetCommonMarkPose(boolean invert, StagePosition stagePosition){

        return null;
    }
//    public Trajectory GetSpikeMarkTrajectory(Pose2d startPose, SpikeMark spikeMark){
//        return null;
//    }
//    public Trajectory GetCommonTrajectory(Pose2d startPose, )
}
