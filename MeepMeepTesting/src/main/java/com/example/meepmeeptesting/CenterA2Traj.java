package com.example.meepmeeptesting;

import static com.example.meepmeeptesting.CenterA2Traj.StartDelay.NO_DELAY;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.example.meepmeeptesting.auton.AutonVariables.*;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.Constraints;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.DriveShim;
import com.noahbres.meepmeep.roadrunner.DriveTrainType;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence;
import com.example.meepmeeptesting.auton.TrajectoryConfig;


public class CenterA2Traj {
    enum StartDelay {
        NO_DELAY,
        ONE_SECOND,
        THREE_SECOND,
        FIVE_SECOND
    }
    private static StartDelay startDelay = StartDelay.FIVE_SECOND;

    public static double autonDelay = getStartDelay();
    public static void main(String[] args) {


        SpikeMark location = SpikeMark.RIGHT;
        AllianceColor allianceColor = AllianceColor.RED;
        StagePosition stagePosition = StagePosition.APRON;
        FieldParkPosition fieldParkPosition = FieldParkPosition.NEAR_WALL;
        ScoreStrategy scoreStrategy = ScoreStrategy.NO_SCORE;

        TrajectoryConfig trajectoryConfig = new TrajectoryConfig();

        Pose2d startPose = trajectoryConfig.getStartPose(allianceColor, stagePosition);
        Pose2d initialMove = trajectoryConfig.getInitialMovePose(allianceColor, stagePosition);
        Pose2d spikeMarkPos = trajectoryConfig.getSpikeMarkPose(location, allianceColor, stagePosition);
        Pose2d commonPos = trajectoryConfig.getCommonMarkPose(allianceColor);
        Pose2d boardPos = trajectoryConfig.getBoardPose(location, allianceColor, stagePosition);
        Pose2d parkPos = trajectoryConfig.getParkPose(fieldParkPosition, allianceColor);
        Pose2d apronSafePos = trajectoryConfig.getApronSafePose(allianceColor);
        Pose2d apronTrussPos = trajectoryConfig.getApronTrussPose(allianceColor);

        // Stuff for MeepMeep. Don't need in real auton code
        MeepMeep meepMeep = new MeepMeep(800);
        RoadRunnerBotEntity myBot;
        Constraints constraints = new Constraints(15, 30, Math.toRadians(280), Math.toRadians(60), 10.56);

        TrajectorySequence spikeMarkTraj;
        TrajectorySequence boardTraj;
        TrajectorySequence parkTraj;

        DriveShim driveShim = new DriveShim(DriveTrainType.MECANUM, constraints, startPose);

        if (scoreStrategy == ScoreStrategy.NO_SCORE){
            spikeMarkTraj = driveShim.trajectorySequenceBuilder(startPose)
                    .waitSeconds(autonDelay)
                    .strafeTo(new Vector2d(initialMove.getX(), initialMove.getY()))
                    .waitSeconds(3) // detect pixel
                    .lineToLinearHeading(spikeMarkPos)
                    .waitSeconds(2)// score purple/prep for yellow
                    .lineToLinearHeading(apronSafePos) // get in position to go under truss
                    .lineToConstantHeading(new Vector2d(apronTrussPos.getX(), apronTrussPos.getY())) // go under truss
                    .splineToLinearHeading(parkPos, Math.toRadians(0))
                    .build();
        } else {
            spikeMarkTraj = driveShim.trajectorySequenceBuilder(startPose)
                    .strafeTo(new Vector2d(initialMove.getX(), initialMove.getY()))
                    .waitSeconds(3) // detect pixel
                    .lineToLinearHeading(spikeMarkPos)
                    .waitSeconds(2)// score purple/prep for yellow
                    .lineToLinearHeading(apronSafePos) // get in position to go under truss
                    .lineToConstantHeading(new Vector2d(apronTrussPos.getX(), apronTrussPos.getY())) // go under truss
                    .splineToConstantHeading(new Vector2d(boardPos.getX(), boardPos.getY()), Math.toRadians(0)) // get to board
                    .waitSeconds(3)//score yellow
                    .splineToLinearHeading(parkPos, Math.toRadians(0))
                    .build();
        }

        myBot = new DefaultBotBuilder(meepMeep)
                .setDimensions(13.5, 16)
                .setConstraints(15, 30, Math.toRadians(280), Math.toRadians(60), 10.56)
                .followTrajectorySequence(spikeMarkTraj);

        // Meepmeep start
        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
    public static double getStartDelay() {
        switch (startDelay) {
            default:
            case NO_DELAY:
                return 0.0;
            case ONE_SECOND:
                return 1.0;
            case THREE_SECOND:
                return 3.0;
            case FIVE_SECOND:
                return 5.0;
        }
    }

}
