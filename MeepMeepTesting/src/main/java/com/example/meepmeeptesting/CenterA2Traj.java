package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.Constraints;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.DriveShim;
import com.noahbres.meepmeep.roadrunner.DriveTrainType;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence;

public class CenterA2Traj {
    public static void main(String[] args) {
        // Starting Position A2, invert for F2 position
        double backStage_starting_x = -40;
        double backStage_starting_Y = 62;

        // Starting heading for blue team, invert for red
        int starting_heading = 270;

        // Parking Offset 0 = park on board; 24 = park on wall; -24 = park in center
        final double PARK_WALL = 24;
        final double PARK_BOARD = 0;
        final double PARK_CENTER = -24;
        double parking_offset = PARK_CENTER;

        // Coordinates for board center on middle AprilTag on blue side. Mirror Y for Red.
        double board_center_x = 48;
        double board_center_y = 35;

        // Offset value from center of board for the left/right april tags
        // -6 = Right; 0 = Center; 6 = Left
        // The code will automatically invert if we are red vs blue
        final double RIGHT_TAG = -6;
        final double CENTER_TAG = 0;
        final double LEFT_TAG = 6;
        double board_offset = RIGHT_TAG;

        // invert = 1 for blue; invert = -1 for red
        final int NO_INVERT = 1;
        final int INVERT = -1;
        int invert = INVERT;

        // Stuff for MeepMeep. Don't need in real auton code
        MeepMeep meepMeep = new MeepMeep(800);
        RoadRunnerBotEntity myBot;
        Constraints constraints = new Constraints(15, 30, Math.toRadians(280), Math.toRadians(60), 10.56);
        TrajectorySequence trajectorySequence;
        DriveShim driveShim = new DriveShim(DriveTrainType.MECANUM, constraints,new Pose2d(backStage_starting_x, backStage_starting_Y, Math.toRadians(invert*starting_heading)));

        // Trajectory code to bring over to real auton
        trajectorySequence = driveShim.trajectorySequenceBuilder(new Pose2d(backStage_starting_x, invert*backStage_starting_Y, Math.toRadians(invert*starting_heading)))
                .lineTo(new Vector2d(backStage_starting_x+(4*invert), invert*backStage_starting_Y))
                .lineToLinearHeading(new Pose2d(-40, invert*31, Math.toRadians(invert*starting_heading)))
                .waitSeconds(1) // Drop Purple Pixel
                .lineToLinearHeading(new Pose2d(backStage_starting_x, invert*60, Math.toRadians(180)))
                .lineToLinearHeading(new Pose2d(14, invert*60, Math.toRadians(180)))
                .splineToLinearHeading(new Pose2d(50, invert*35+board_offset, Math.toRadians(180)), Math.toRadians(0))
                .waitSeconds(1) // Drop Yellow Pixel
                .lineTo(new Vector2d(49, invert*35+board_offset))
                .splineToLinearHeading(new Pose2d(board_center_x, invert*board_center_y+invert*parking_offset, Math.toRadians(180)), Math.toRadians(0))
                .build();

        myBot = new DefaultBotBuilder(meepMeep)
                .setDimensions(13.5, 16)
                .setConstraints(15, 30, Math.toRadians(280), Math.toRadians(60), 10.56)
                .followTrajectorySequence(trajectorySequence);

        // Meepmeep start
        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }

}
