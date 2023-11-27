package org.firstinspires.ftc.teamcode.subsystems.auton;

import com.acmerobotics.roadrunner.drive.MecanumDrive;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.opmode.testing.Testing_Auton;
import org.firstinspires.ftc.teamcode.subsystems.Picker;
import org.firstinspires.ftc.teamcode.subsystems.Placer;
import org.firstinspires.ftc.teamcode.subsystems.menu.SelectionMenu;
import org.firstinspires.ftc.teamcode.subsystems.vision.SpikeMark;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

public class TrajectoryConfig {
    private SampleMecanumDrive rrdrive;
    Trajectory apronTraj;
    Trajectory backstageTraj;
    double spikeMark_Y;
    double spikeMark_X;
    double spikeMark_H;
    boolean mirrored = false;
    public TrajectoryConfig(SampleMecanumDrive autonDrive){
        this.rrdrive = autonDrive;
    }
    public Trajectory GenerateTrajectory(Pose2d startPose, Vector2d endPosition){
        TrajectoryBuilder genTraj = rrdrive.trajectoryBuilder(startPose)
                        .lineTo(new Vector2d(endPosition.getX(), endPosition.getY()));


        return genTraj.build();
    }
    public Pose2d GetSpikeMarkPose(SpikeMark detectedSpikeMark, boolean invert, SelectionMenu.FieldStartPosition startPosition, SelectionMenu.AllianceColor allianceColor){
        /**
         * invert is managing the inversion for red vs. blue alliance start positions.
         * mirrored is managing the mirror for left vs. right start positions.
         * (a LEFT detection on the RIGHT start position is a mirror of a RIGHT detection
         * on the LEFT start position, etc)
         */
        spikeMark_Y = invert ? 34 : -34;
        if (startPosition == SelectionMenu.FieldStartPosition.RIGHT && allianceColor == SelectionMenu.AllianceColor.BLUE || startPosition == SelectionMenu.FieldStartPosition.LEFT && allianceColor == SelectionMenu.AllianceColor.RED){
            mirrored = true;
        }
        switch(detectedSpikeMark){
            case LEFT:
                if (mirrored){
                    spikeMark_X = -29;
                    spikeMark_H = Math.toRadians(305);
                } else {
                    spikeMark_X = 28;
                }
                break;
            case RIGHT:
                if (mirrored) {
                    spikeMark_X = -45;
                } else {
                    spikeMark_X = 4;
                }
                break;
            case CENTER:
            default:
                if (mirrored) {
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
    public Pose2d GetCommonMarkPose(boolean invert, SelectionMenu.FieldStartPosition startPosition, SelectionMenu.AllianceColor allianceColor){

    }
    public Trajectory GetSpikeMarkTrajectory(Pose2d startPose, SpikeMark spikeMark){
        return null;
    }
    public Trajectory GetCommonTrajectory(Pose2d startPose, )
}
