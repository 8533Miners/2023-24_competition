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

public class FullTrajectory {
    enum AllianceColor {
        RED,
        BLUE
    }

    enum FieldStartPosition {
        LEFT,
        RIGHT
    }

    enum FieldParkPosition {
        NEAR_WALL,
        ON_BACKDROP,
        NEAR_CENTER
    }

    enum SpikeMark {
        NONE,
        LEFT,
        CENTER,
        RIGHT
    }

    public static void main(String[] args) {
        /**
         * Starting position constants
         */
        // Starting Position A2, invert for F2 position
        final double BACKSTAGE_STARTING_X_POSITION = -40;
        // Starting Position A4, invert for F4 position
        final double APRON_STARTING_X_POSITION = 16;
        // Starting Y is always the same for blue team (A), invert for red team (F)
        final double STARTING_Y_POSITION = 62;
        // variable definitions to defaults
        double starting_x;// = BACKSTAGE_STARTING_X_POSITION;
        double starting_y;// = STARTING_Y_POSITION;

        // Starting heading for blue team (A), invert for red team (F)
        final int STARTING_HEADING = 270;

        // Coordinates for board center on middle AprilTag on blue side(A).
        // Mirror Y for red side (F).
        final double BOARD_CENTER_X = 48;
        final double BOARD_CENTER_Y = 35;

        // Parking offset values. Determines where to park in the apron
        final double PARK_WALL = 24;
        final double PARK_BOARD = 0;
        final double PARK_CENTER = -24;
        double parking_offset;// = PARK_CENTER; //default

        // Offset value from center of board for the left/right april tags
        // The code will automatically invert if we are red vs blue
        final double RIGHT_TAG = -6;
        final double CENTER_TAG = 0;
        final double LEFT_TAG = 6;
        double board_offset = CENTER_TAG; //default

        // invert = 1 for blue; invert = -1 for red
        final int NO_INVERT = 1;
        final int INVERT = -1;
        int invert; // = NO_INVERT; //default




//        AllianceColor allianceColor = selectionMenu.getAllianceColor();
//        FieldStartPosition fieldStartPosition = selectionMenu.getFieldStartPosition();
//        FieldParkPosition fieldParkPosition = selectionMenu.getFieldParkPosition();
        AllianceColor allianceColor = AllianceColor.BLUE;
        FieldStartPosition fieldStartPosition = FieldStartPosition.LEFT;
        FieldParkPosition fieldParkPosition = FieldParkPosition.NEAR_WALL;

        // From Detection, only set for MeepMeep
        SpikeMark location = SpikeMark.RIGHT;


        String starting_side = "backStage";
        //String starting_side = "curtain";

        /**
         * Set starting positions and inversion or not based on menu selection
         */
        starting_y = STARTING_Y_POSITION; // Starting Y will be inverted by code so just set it
        switch(allianceColor){
            case RED:
                invert = INVERT;
                if(FieldStartPosition.RIGHT == fieldStartPosition) {
                    starting_x = APRON_STARTING_X_POSITION;
                    starting_side = "curtain";
                } else {
                    starting_x = BACKSTAGE_STARTING_X_POSITION;
                }
                break;
            case BLUE:
            default:
                invert = NO_INVERT;
                if(FieldStartPosition.RIGHT == fieldStartPosition) {
                    starting_x = BACKSTAGE_STARTING_X_POSITION;
                } else {
                    starting_x = APRON_STARTING_X_POSITION;
                    starting_side = "curtain";
                }
                break;
        }

        /**
         * Determine parking offset amount based on menu selection
         */
        switch(fieldParkPosition){
            default:
            case NEAR_WALL:
                parking_offset = PARK_WALL;
                break;
            case ON_BACKDROP:
                parking_offset = PARK_BOARD;
                break;
            case NEAR_CENTER:
                parking_offset = PARK_CENTER;
                break;
        }

        // Stuff for MeepMeep. Don't need in real auton code
        MeepMeep meepMeep = new MeepMeep(800);
        RoadRunnerBotEntity myBot;
        Constraints constraints = new Constraints(15, 30, Math.toRadians(280), Math.toRadians(60), 10.56);

        TrajectorySequence trajectorySequence; // A2 & F2
        TrajectorySequence curtainSequence; // A4 & F4

        Pose2d startPose = new Pose2d(starting_x, invert*starting_y, Math.toRadians(invert*STARTING_HEADING));
        DriveShim driveShim = new DriveShim(DriveTrainType.MECANUM, constraints,startPose);


        /**
         * Detect prop and then choose locations based on data from detection
         */
        // Backstage
        double spikeMark_X;
        double spikeMark_Y;
        double initialMove;

        if(starting_side.equals("backStage")) {
            initialMove = -4;
            switch (location) {
                case LEFT:
                    spikeMark_X = -32;
                    spikeMark_Y = invert * 34;
                    break;
                case RIGHT:
                    spikeMark_X = -54;
                    spikeMark_Y = invert * 34;
                    break;
                case CENTER:
                default:
                    spikeMark_X = -40;
                    spikeMark_Y = invert * 24;
                    break;
            }
            // Trajectory code to bring over to real auton
            trajectorySequence = driveShim.trajectorySequenceBuilder(startPose)
                    .lineTo(new Vector2d(starting_x + initialMove, invert * starting_y)) //initialMove
                    .splineToLinearHeading(new Pose2d(spikeMark_X, spikeMark_Y, Math.toRadians(invert * STARTING_HEADING+90)), Math.toRadians(360))
                    .waitSeconds(1) // Drop Purple Pixel
                    .setTangent(Math.toRadians(180))
                    .splineToLinearHeading(new Pose2d(starting_x, invert * 60, Math.toRadians(180)), Math.toRadians(360))
                    .lineToLinearHeading(new Pose2d(14, invert * 60, Math.toRadians(180)))
                    .splineToLinearHeading(new Pose2d(50, invert * 35 + board_offset, Math.toRadians(180)), Math.toRadians(0))
                    .waitSeconds(1) // Drop Yellow Pixel
                    .lineTo(new Vector2d(49, invert * 35 + board_offset))
                    .splineToLinearHeading(new Pose2d(BOARD_CENTER_X, invert * BOARD_CENTER_Y + invert * parking_offset, Math.toRadians(180)), Math.toRadians(0))
                    .build();
        } else {
            initialMove = 4;
            switch (location) {
                case LEFT:
                    spikeMark_X = -24+48;
                    spikeMark_Y = invert * 34;
                    break;
                case RIGHT:
                    spikeMark_X = -46+48;
                    spikeMark_Y = invert * 34;
                    break;
                case CENTER:
                default:
                    spikeMark_X = -40+48;
                    spikeMark_Y = invert * 31;
                    break;
            }
            // Trajectory code to bring over to real auton
            trajectorySequence = driveShim.trajectorySequenceBuilder(startPose)
                    .lineTo(new Vector2d(starting_x + initialMove, invert * starting_y)) //initialMove
                    .splineToLinearHeading(new Pose2d(spikeMark_X, spikeMark_Y, Math.toRadians(invert * STARTING_HEADING-90)), Math.toRadians(360))
                    .waitSeconds(1) // Drop Purple Pixel
                    .setTangent(Math.toRadians(-180))
                    .splineToLinearHeading(new Pose2d(starting_x, invert * 60, Math.toRadians(180)), Math.toRadians(360))
                    .lineToLinearHeading(new Pose2d(14, invert * 60, Math.toRadians(180)))
                    .splineToLinearHeading(new Pose2d(50, invert * 35 + board_offset, Math.toRadians(180)), Math.toRadians(0))
                    .waitSeconds(1) // Drop Yellow Pixel
                    .lineTo(new Vector2d(49, invert * 35 + board_offset))
                    .splineToLinearHeading(new Pose2d(BOARD_CENTER_X, invert * BOARD_CENTER_Y + invert * parking_offset, Math.toRadians(180)), Math.toRadians(0))
                    .build();
        }





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
