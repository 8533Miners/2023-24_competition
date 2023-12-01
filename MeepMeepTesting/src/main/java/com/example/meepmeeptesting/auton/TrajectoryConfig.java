package com.example.meepmeeptesting.auton;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.roadrunner.SampleMecanumDrive;
import com.example.meepmeeptesting.auton.AutonVariables.StagePosition;
import com.example.meepmeeptesting.auton.AutonVariables.AllianceColor;
import com.example.meepmeeptesting.auton.AutonVariables.FieldParkPosition;
import com.example.meepmeeptesting.auton.AutonVariables.FieldStartPosition;
import com.example.meepmeeptesting.auton.AutonVariables.SpikeMark;



/**
 * stagePosition is managing the mirror for left vs. right start positions.
 * (a LEFT detection on the APRON start position is a mirror of a RIGHT detection
 * on the BACKSTAGE start position, etc)
 */
public class TrajectoryConfig {
    private SampleMecanumDrive rrdrive;

    public TrajectoryConfig() {

    }
    public Pose2d getStartPose(AllianceColor allianceColor, StagePosition stagePosition) {
        double pose_X;
        double pose_Y;
        double pose_H;

        pose_Y = (allianceColor == AllianceColor.RED) ? -62 : 62;
        pose_H = (allianceColor == AllianceColor.RED) ? 90 : 270;

        if (stagePosition == StagePosition.APRON) {
            pose_X = -40;
        } else {
            pose_X = 16;
        }

        return new Pose2d(pose_X, pose_Y, Math.toRadians(pose_H));
    }

    public Pose2d getInitialMovePose(AllianceColor allianceColor, StagePosition stagePosition) {
        double pose_X;
        double pose_Y;
        double pose_H;

        Pose2d currentPositionStartPose = getStartPose(allianceColor, stagePosition);
        pose_Y = currentPositionStartPose.getY();
        pose_H = currentPositionStartPose.getHeading();

        if (stagePosition == StagePosition.APRON) {
            pose_X = -44;
        } else {
            pose_X = 20;
        }

        return new Pose2d(pose_X, pose_Y, Math.toRadians(pose_H));
    }

    public Pose2d getSpikeMarkPose(SpikeMark detectedSpikeMark, AllianceColor allianceColor, StagePosition stagePosition) {
        double pose_X;
        double pose_Y;
        double pose_H;

        pose_Y = (allianceColor == AllianceColor.RED) ? -34 : 34;
        pose_H = 180;

        switch (detectedSpikeMark) {
            case LEFT:
                if (stagePosition == StagePosition.APRON) {
                    pose_X = (allianceColor == AllianceColor.RED) ? -45 : -29;
                    pose_H = (allianceColor == AllianceColor.RED) ? 90 : 315;
                } else {
                    pose_X = (allianceColor == AllianceColor.RED) ? 4 : 28;
                }
                break;
            case RIGHT:
                if (stagePosition == StagePosition.APRON) {
                    pose_X = (allianceColor == AllianceColor.RED) ? -29 : -45;
                    pose_H = (allianceColor == AllianceColor.RED) ? 55 : 270;
                } else {
                    pose_X = (allianceColor == AllianceColor.RED) ? 28 : 4;
                }
                break;
            case CENTER:
            default:
                if (stagePosition == StagePosition.APRON) {
                    pose_X = -40;
                    pose_Y = (allianceColor == AllianceColor.RED) ? -29 : 29;
                    pose_H = (allianceColor == AllianceColor.RED) ? 90 : 270;
                } else {
                    pose_X = 20;
                    pose_Y = (allianceColor == AllianceColor.RED) ? -24 : 26;
                }
                break;
        }
        return new Pose2d(pose_X, pose_Y, Math.toRadians(pose_H));
    }

    public Pose2d getCommonMarkPose(AllianceColor allianceColor) {
        double pose_X;
        double pose_Y;
        double pose_H;

        pose_X = 43;
        pose_Y = (allianceColor == AllianceColor.RED) ? -35 : 35;
        pose_H = 180;

        return new Pose2d(pose_X, pose_Y, Math.toRadians(pose_H));
    }

    public Pose2d getBoardPose(SpikeMark detectedSpikeMark, AllianceColor allianceColor, StagePosition stagePosition) {
        double pose_X;
        double pose_Y;
        double pose_H;

        pose_X = 53.5;
        pose_Y = (allianceColor == AllianceColor.RED) ? -35 : 35;
        pose_H = 180;

        if (stagePosition == StagePosition.APRON) {
            pose_X = 55.5;
        }

        if (detectedSpikeMark == SpikeMark.RIGHT) {
            pose_Y = (allianceColor == AllianceColor.RED) ? -42 : 28;
        } else if (detectedSpikeMark == SpikeMark.LEFT) {
            pose_Y = (allianceColor == AllianceColor.RED) ? -28 : 42;
        }

        return new Pose2d(pose_X, pose_Y, Math.toRadians(pose_H));
    }

    public Pose2d getParkPose(FieldParkPosition parkPosition, AllianceColor allianceColor) {
        double pose_X;
        double pose_Y;
        double pose_H;

        pose_H = 180;
        pose_X = 47;

        switch (parkPosition) {
            case NEAR_WALL:
                pose_Y = (allianceColor == AllianceColor.RED) ? -59 : 59;
                break;
            case NEAR_CENTER:
                pose_Y = (allianceColor == AllianceColor.RED) ? -11 : 11;
                break;
            default:
            case ON_BACKDROP:
                pose_Y = (allianceColor == AllianceColor.RED) ? -35 : 35;
                break;
        }
        return new Pose2d(pose_X, pose_Y, Math.toRadians(pose_H));
    }

    public Pose2d getApronSafePose(AllianceColor allianceColor) {
        double pose_X;
        double pose_Y;
        double pose_H;

        pose_X = -52;
        pose_Y = (allianceColor == AllianceColor.RED) ? -60 : 60;
        pose_H = 180;

        return new Pose2d(pose_X, pose_Y, Math.toRadians(pose_H));
    }

    public Pose2d getApronTrussPose(AllianceColor allianceColor) {
        double pose_X;
        double pose_Y;
        double pose_H;

        pose_X = 14;
        pose_Y = (allianceColor == AllianceColor.RED) ? -60 : 60;
        pose_H = 180;

        return new Pose2d(pose_X, pose_Y, Math.toRadians(pose_H));
    }
}