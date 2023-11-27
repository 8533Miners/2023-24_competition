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

    enum StartingSide {
        CURTAIN,
        BACKSTAGE
    }
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
        final double CURTAIN_STARTING_X_POSITION = 16;
        // Starting Y is always the same for blue team (A), invert for red team (F)
        final double STARTING_Y = 62;
        // variable definitions to defaults
        double starting_x;// = BACKSTAGE_STARTING_X_POSITION;

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
        final double RIGHT_TAG = -5.5;
        final double CENTER_TAG = 0;
        final double LEFT_TAG = 5.5;
        double board_offset = CENTER_TAG; //default

        // invert = 1 for blue; invert = -1 for red
        final int NO_INVERT = 1;
        final int INVERT = -1;
        int invert; // = NO_INVERT; //default

        boolean invertedDetection = false; // invert detections based on starting position

        StartingSide startingSide = StartingSide.CURTAIN;

        // From Detection, only set for MeepMeep
        SpikeMark locationToSet = SpikeMark.RIGHT;

        AllianceColor allianceColor = AllianceColor.RED;
        FieldStartPosition fieldStartPosition = FieldStartPosition.LEFT;
        FieldParkPosition fieldParkPosition = FieldParkPosition.NEAR_WALL;

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

        /**
         * Set starting positions and inversion or not based on menu selection
         */
        double initialMovePos = 4;
        switch(allianceColor){
            case RED:
                invert = INVERT;
                if(FieldStartPosition.RIGHT == fieldStartPosition) {
                    starting_x = CURTAIN_STARTING_X_POSITION;
                    startingSide = StartingSide.BACKSTAGE;
                    invertedDetection = true; // F4 has inverted detections compared to A4
                } else {
                    starting_x = BACKSTAGE_STARTING_X_POSITION;
                    initialMovePos = -4;
                }
                break;
            case BLUE:
            default:
                invert = NO_INVERT;
                if(FieldStartPosition.RIGHT == fieldStartPosition) {
                    starting_x = BACKSTAGE_STARTING_X_POSITION;
                    initialMovePos = -4;
                    invertedDetection = true; // A2 has inverted detections compared to A4
                } else {
                    starting_x = CURTAIN_STARTING_X_POSITION;
                    startingSide = StartingSide.BACKSTAGE;
                }
                break;
        }

        // Stuff for MeepMeep. Don't need in real auton code
        MeepMeep meepMeep = new MeepMeep(800);
        RoadRunnerBotEntity myBot;
        Constraints constraints = new Constraints(15, 30, Math.toRadians(280), Math.toRadians(60), 10.56);

        Pose2d startPose = new Pose2d(starting_x, invert*STARTING_Y, Math.toRadians(invert*STARTING_HEADING));

        TrajectorySequence trajectorySequence;

        DriveShim driveShim = new DriveShim(DriveTrainType.MECANUM, constraints,startPose);

        /**
         * Detect prop and then choose locations based on data from detection
         */
        double spikeMark_X;
        double spikeMark_Y;
        double spikeMarkApproachAngle = Math.toRadians(invert*STARTING_HEADING);

        // only needed for MeepMeep since we don't have real TFOD
        SpikeMark location = locationToSet;



        if(startingSide.equals(StartingSide.CURTAIN)) {
            switch (location) {
                case LEFT:
                    board_offset = LEFT_TAG;
                    spikeMark_X = -29;
                    spikeMark_Y = invert * 34;
                    spikeMarkApproachAngle = Math.toRadians(invert * STARTING_HEADING+35*invert);
                    break;
                case RIGHT:
                    board_offset = RIGHT_TAG;
                    spikeMark_X = -45;
                    spikeMark_Y = invert * 34;
                    break;
                case CENTER:
                default:
                    board_offset = CENTER_TAG;
                    spikeMark_X = -40;
                    spikeMark_Y = invert * 29;
                    break;
            }

            // Trajectory code
            trajectorySequence = driveShim.trajectorySequenceBuilder(startPose)
                    .lineTo(new Vector2d(starting_x + initialMovePos, invert * STARTING_Y)) //initialMove
                    //.lineToLinearHeading(new Pose2d(-59, invert*62))
                    .lineToLinearHeading(new Pose2d(spikeMark_X, spikeMark_Y, spikeMarkApproachAngle))
                    .waitSeconds(1) // Drop Purple Pixel
                    .setTangent(Math.toRadians(180))
                    .lineToLinearHeading(new Pose2d(starting_x+initialMovePos, invert * 60, Math.toRadians(180)))
                    .lineToLinearHeading(new Pose2d(14, invert * 60, Math.toRadians(180)))
                    // ** Same code below (clean up?)
                    .splineToLinearHeading(new Pose2d(50, invert * 35 + board_offset, Math.toRadians(180)), Math.toRadians(0))
                    .waitSeconds(1) // Drop Yellow Pixel
                    .lineTo(new Vector2d(BOARD_CENTER_X, invert * BOARD_CENTER_Y + board_offset))
                    .splineToLinearHeading(new Pose2d(BOARD_CENTER_X, invert * BOARD_CENTER_Y + invert * parking_offset, Math.toRadians(180)), Math.toRadians(0))
                    .build();
            // ** end same code
        } else { //Backstage
            switch (location) {
                case LEFT:
                    spikeMark_X = 28;
                    spikeMark_Y = invert * 34;
                    board_offset = LEFT_TAG;
                    break;
                case RIGHT:
                    spikeMark_X = 6;
                    spikeMark_Y = invert * 34;
                    board_offset = RIGHT_TAG;
                    break;
                case CENTER:
                default:
                    spikeMark_X = 20;
                    spikeMark_Y = invert * 26;
                    board_offset = CENTER_TAG;
                    break;
            }

            // Trajectory code to bring over to real auton
            trajectorySequence = driveShim.trajectorySequenceBuilder(startPose)
                    .lineTo(new Vector2d(starting_x + initialMovePos, invert * STARTING_Y)) //initialMove
                    .splineToLinearHeading(new Pose2d(spikeMark_X, spikeMark_Y, Math.toRadians(invert * STARTING_HEADING-90*invert)), Math.toRadians(180))
                    .waitSeconds(1) // Drop Purple Pixel
                    .setTangent(Math.toRadians(0))
                    // ** Same code below (clean up?)
                    .splineToLinearHeading(new Pose2d(50, invert * 35 + board_offset, Math.toRadians(180)), Math.toRadians(0))
                    .waitSeconds(1) // Drop Yellow Pixel
                    .lineTo(new Vector2d(BOARD_CENTER_X, invert * BOARD_CENTER_Y + board_offset))
                    .splineToLinearHeading(new Pose2d(BOARD_CENTER_X, invert * BOARD_CENTER_Y + invert * parking_offset, Math.toRadians(180)), Math.toRadians(0))
                    .build();
            // ** end same code
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
