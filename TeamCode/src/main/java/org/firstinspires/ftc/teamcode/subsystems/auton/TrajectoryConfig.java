package org.firstinspires.ftc.teamcode.subsystems.auton;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.menu.SelectionMenu.FieldParkPosition;
import org.firstinspires.ftc.teamcode.subsystems.menu.SelectionMenu.AllianceColor;
import org.firstinspires.ftc.teamcode.opmode.Production_Auton.StagePosition;
import org.firstinspires.ftc.teamcode.subsystems.vision.SpikeMark;

/**
 * stagePosition is managing the mirror for left vs. right start positions.
 * (a LEFT detection on the APRON start position is a mirror of a RIGHT detection
 * on the BACKSTAGE start position, etc)
 */
public class TrajectoryConfig {
    private SampleMecanumDrive rrdrive;
//    Trajectory apronTraj;
//    Trajectory backstageTraj;

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

    public Pose2d getStartPose(AllianceColor allianceColor, StagePosition stagePosition){
        double pose_X;
        double pose_Y;
        double pose_H;

        pose_Y = (allianceColor == AllianceColor.RED) ? -62 : 62;
        pose_H = (allianceColor == AllianceColor.RED) ? 90 : 270;

        if (stagePosition == StagePosition.APRON){
            pose_X = -40;
        } else {
            pose_X = 16;
        }

        return new Pose2d(pose_X, pose_Y, Math.toRadians(pose_H));
    }

    public Pose2d getInitialMovePose(AllianceColor allianceColor, StagePosition stagePosition){
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
    public Pose2d getSpikeMarkPose(SpikeMark detectedSpikeMark, AllianceColor allianceColor, StagePosition stagePosition){
        double pose_X;
        double pose_Y;
        double pose_H;

        pose_Y = (allianceColor == AllianceColor.RED) ? -34 : 34;
        pose_H = 180;

        switch(detectedSpikeMark){
            case LEFT:
                if (stagePosition == StagePosition.APRON){
                    pose_X = (allianceColor == AllianceColor.RED) ? -45 : -29;
                    pose_H = (allianceColor == AllianceColor.RED) ? 90 : 305;
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
    public Pose2d getCommonMarkPose(AllianceColor allianceColor){
        double pose_X;
        double pose_Y;
        double pose_H;

        pose_X = 43;
        pose_Y = (allianceColor == AllianceColor.RED) ? -35 : 35;
        pose_H = 180;

        return new Pose2d(pose_X, pose_Y, Math.toRadians(pose_H));
    }
    public Pose2d getBoardPose(SpikeMark detectedSpikeMark, AllianceColor allianceColor, StagePosition stagePosition){
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
        } else if (detectedSpikeMark == SpikeMark.LEFT){
            pose_Y = (allianceColor == AllianceColor.RED) ? -28: 42;
        }

        return new Pose2d(pose_X, pose_Y, Math.toRadians(pose_H));
    }
    public Pose2d getParkPose(FieldParkPosition parkPosition, AllianceColor allianceColor){
        double pose_X;
        double pose_Y;
        double pose_H;

        pose_H = 180;
        pose_X = 47;

        switch(parkPosition){
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

    public Pose2d getApronSafePose(AllianceColor allianceColor){
        double pose_X;
        double pose_Y;
        double pose_H;

        pose_X = -52;
        pose_Y = (allianceColor == AllianceColor.RED) ? -60 : 60;
        pose_H = 180;

        return new Pose2d(pose_X, pose_Y, Math.toRadians(pose_H));
    }

    public Pose2d getApronTrussPose(AllianceColor allianceColor){
        double pose_X;
        double pose_Y;
        double pose_H;

        pose_X = 14;
        pose_Y = (allianceColor == AllianceColor.RED) ? -60 : 60;
        pose_H = 180;

        return new Pose2d(pose_X, pose_Y, Math.toRadians(pose_H));
    }
//    public TrajectorySequence getSpikeMarkTrajectory(SpikeMark detectedSpikeMark, boolean invert, StagePosition stagePosition){
//
//        Pose2d spikeMarkPose = getSpikeMarkPose(detectedSpikeMark, invert, stagePosition);
//        TrajectorySequence spikeMarkTraj = null;
//
//        if (stagePosition == StagePosition.APRON){
//            spikeMarkTraj = rrdrive.trajectorySequenceBuilder(rrdrive.getPoseEstimate())
//                    .lineToLinearHeading(spikeMarkPose)
//                    .addDisplacementMarker(() -> { //start placing purple pixel
//                        picker.update(Picker.PickerState.OUTAKE);
//                    })
//                    .waitSeconds(2)
//                    .addDisplacementMarker(() -> { //stop placing purple pixel
//                        picker.update(Picker.PickerState.HOLD);
//                    })
//                    .build();
//        } else {
//            spikeMarkTraj = rrdrive.trajectorySequenceBuilder(rrdrive.getPoseEstimate())
//                    .splineToLinearHeading(spikeMarkPose, 180)
//                    .build();
//        }

//        TrajectoryBuilder spikeMarkTraj = rrdrive.trajectoryBuilder(currentPose)
//                .strafeTo(new Vector2d(spikeMarkPose.getX(), spikeMarkPose.getY()));

//        return spikeMarkTraj;
//    }
//    public Trajectory getCommonTrajectory(Pose2d startPose, )
}
