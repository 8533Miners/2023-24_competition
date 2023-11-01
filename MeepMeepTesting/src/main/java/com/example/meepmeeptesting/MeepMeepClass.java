package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence;

public class MeepMeepClass {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);
        RoadRunnerBotEntity myBot;

        String spikeMark = "left";
        //String spikeMark = "center";
        //String spikeMark = "right";

        String startingGrid = "A4";
        int A4_starting_x = 16;
        int A4_starting_y = 62;
        int starting_heading_A = 270;


        //String startingGrid = "A2";
        int A2_starting_x = -40;
        int A2_starting_y = 62;


        //String startingGrid = "F4";
        int F4_starting_x = 16;
        int F4_starting_y = -62;
        int starting_heading_F = 90;


        //String startingGrid = "F2";
        int F2_starting_x = -40;
        int F2_starting_y = -62;

        double trackWidth = 11.0;
        double robotLength = 16.0;
        double robotWidth = 13.5;

        switch(startingGrid) {
            case "A4":
                switch(spikeMark) {
                    case "left":
                        myBot = new DefaultBotBuilder(meepMeep)
                                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                                .setDimensions(robotWidth,robotLength)
                                .setConstraints(20, 30, Math.toRadians(180), Math.toRadians(180), trackWidth)
                                .followTrajectorySequence(drive ->
                                        drive.trajectorySequenceBuilder(new Pose2d(A4_starting_x, A4_starting_y, Math.toRadians(starting_heading_A)))
                                                .strafeLeft(8)
                                                .forward(26)
                                                .waitSeconds(1) // place purple pixel
                                                .back(12)
                                                .turn(Math.toRadians(90))
                                                .forward(22)
                                                .waitSeconds(1) // place yellow pixel
                                                .waitSeconds(1) // park
                                                .build()
                                );
                        break;
                    case "center":
                        myBot = new DefaultBotBuilder(meepMeep)
                                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                                .setDimensions(robotWidth,robotLength)
                                .setConstraints(20, 30, Math.toRadians(180), Math.toRadians(180), trackWidth)
                                .followTrajectorySequence(drive ->
                                        drive.trajectorySequenceBuilder(new Pose2d(A4_starting_x, A4_starting_y, Math.toRadians(starting_heading_A)))
                                                .forward(28)
                                                .waitSeconds(1) // place purple pixel
                                                .back(14)
                                                .turn(Math.toRadians(90))
                                                .forward(30)
                                                .waitSeconds(1) // place yellow pixel
                                                .waitSeconds(1) // park
                                                .build()
                                );
                        break;
                    default: //right
                        myBot = new DefaultBotBuilder(meepMeep)
                                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                                .setDimensions(robotWidth,robotLength)
                                .setConstraints(20, 30, Math.toRadians(180), Math.toRadians(180), trackWidth)
                                .followTrajectorySequence(drive ->
                                        drive.trajectorySequenceBuilder(new Pose2d(A4_starting_x, A4_starting_y, Math.toRadians(starting_heading_A)))
                                                .forward(28)
                                                .turn(Math.toRadians(-90))
                                                .forward(6)
                                                .waitSeconds(1) // place yellow pixel
                                                .back(6)
                                                .turn(Math.toRadians(90))
                                                .back(14)
                                                .turn(Math.toRadians(90))
                                                .forward(30)
                                                .waitSeconds(1) // place yellow pixel
                                                .waitSeconds(1) // park
                                                .build()
                                );
                        break;
                }

                break;
            case "A2":
                switch(spikeMark) {

                    case "right":
                        myBot = new DefaultBotBuilder(meepMeep)
                                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                                .setDimensions(robotWidth, robotLength)
                                .setConstraints(20, 30, Math.toRadians(180), Math.toRadians(180), trackWidth)
                                .followTrajectorySequence(drive ->
                                        drive.trajectorySequenceBuilder(new Pose2d(A2_starting_x, A2_starting_y, Math.toRadians(starting_heading_A)))
                                                .strafeRight(6)
                                                .forward(26)
                                                .waitSeconds(1) // place purple pixel
                                                .back(14)
                                                .turn(Math.toRadians(90))
                                                .strafeLeft(10)
                                                .forward(94)
                                                .waitSeconds(1) // place yellow pixel
                                                .waitSeconds(1) // park
                                                .build()
                                );
                        break;
                    case "center":
                        myBot = new DefaultBotBuilder(meepMeep)
                                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                                .setDimensions(robotWidth, robotLength)
                                .setConstraints(20, 30, Math.toRadians(180), Math.toRadians(180), trackWidth)
                                .followTrajectorySequence(drive ->
                                        drive.trajectorySequenceBuilder(new Pose2d(A2_starting_x, A2_starting_y, Math.toRadians(starting_heading_A)))
                                                .forward(28)
                                                .waitSeconds(1) // place purple pixel
                                                .back(24)
                                                .turn(Math.toRadians(90))
                                                .forward(86)
                                                .waitSeconds(1) // place yellow pixel
                                                .waitSeconds(1) // park
                                                .build()
                                );
                        break;
                    default: //left
                        myBot = new DefaultBotBuilder(meepMeep)
                                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                                .setDimensions(robotWidth, robotLength)
                                .setConstraints(20, 30, Math.toRadians(180), Math.toRadians(180), trackWidth)
                                .followTrajectorySequence(drive ->
                                        drive.trajectorySequenceBuilder(new Pose2d(A2_starting_x, A2_starting_y, Math.toRadians(starting_heading_A)))
                                                .forward(28)
                                                .turn(Math.toRadians(90))
                                                .forward(6)
                                                .waitSeconds(1) // place yellow pixel
                                                .back(6)
                                                .strafeLeft(24)
                                                .forward(86)
                                                .waitSeconds(1) // place yellow pixel
                                                .waitSeconds(1) // park
                                                .build()
                                );
                        break;
                }
                break;
            case "F4":
                switch(spikeMark) {
                    default: //right
                        myBot = new DefaultBotBuilder(meepMeep)
                                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                                .setDimensions(robotWidth, robotLength)
                                .setConstraints(20, 30, Math.toRadians(180), Math.toRadians(180), trackWidth)
                                .followTrajectorySequence(drive ->
                                        drive.trajectorySequenceBuilder(new Pose2d(F4_starting_x, F4_starting_y, Math.toRadians(starting_heading_F)))
                                                .strafeRight(8)
                                                .forward(26)
                                                .waitSeconds(1) // place purple pixel
                                                .back(12)
                                                .turn(Math.toRadians(-90))
                                                .forward(22)
                                                .waitSeconds(1) // place yellow pixel
                                                .waitSeconds(1) // park
                                                .build()
                                );
                        break;
                    case "center":
                        myBot = new DefaultBotBuilder(meepMeep)
                                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                                .setDimensions(robotWidth, robotLength)
                                .setConstraints(20, 30, Math.toRadians(180), Math.toRadians(180), trackWidth)
                                .followTrajectorySequence(drive ->
                                        drive.trajectorySequenceBuilder(new Pose2d(F4_starting_x, F4_starting_y, Math.toRadians(starting_heading_F)))
                                                .forward(28)
                                                .waitSeconds(1) // place purple pixel
                                                .back(14)
                                                .turn(Math.toRadians(-90))
                                                .forward(30)
                                                .waitSeconds(1) // place yellow pixel
                                                .waitSeconds(1) // park
                                                .build()
                                );
                        break;
                    case "left":
                        myBot = new DefaultBotBuilder(meepMeep)
                                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                                .setDimensions(robotWidth, robotLength)
                                .setConstraints(20, 30, Math.toRadians(180), Math.toRadians(180), trackWidth)
                                .followTrajectorySequence(drive ->
                                        drive.trajectorySequenceBuilder(new Pose2d(F4_starting_x, F4_starting_y, Math.toRadians(starting_heading_F)))
                                                .forward(28)
                                                .turn(Math.toRadians(90))
                                                .forward(6)
                                                .waitSeconds(1) // place yellow pixel
                                                .back(6)
                                                .turn(Math.toRadians(-90))
                                                .back(14)
                                                .turn(Math.toRadians(-90))
                                                .forward(30)
                                                .waitSeconds(1) // place yellow pixel
                                                .waitSeconds(1) // park
                                                .build()
                                );
                        break;
                }
                break;
            case "F2":
                switch(spikeMark) {
                    case "left":
                        myBot = new DefaultBotBuilder(meepMeep)
                                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                                .setDimensions(robotWidth, robotLength)
                                .setConstraints(20, 30, Math.toRadians(180), Math.toRadians(180), trackWidth)
                                .followTrajectorySequence(drive ->
                                        drive.trajectorySequenceBuilder(new Pose2d(F2_starting_x, F2_starting_y, Math.toRadians(starting_heading_F)))
                                                .strafeLeft(6)
                                                .forward(26)
                                                .waitSeconds(1) // place purple pixel
                                                .back(14)
                                                .turn(Math.toRadians(-90))
                                                .strafeRight(10)
                                                .forward(94)
                                                .waitSeconds(1) // place yellow pixel
                                                .waitSeconds(1) // park
                                                .build()
                                );
                        break;
                    case "center":
                        myBot = new DefaultBotBuilder(meepMeep)
                                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                                .setDimensions(robotWidth, robotLength)
                                .setConstraints(20, 30, Math.toRadians(180), Math.toRadians(180), trackWidth)
                                .followTrajectorySequence(drive ->
                                        drive.trajectorySequenceBuilder(new Pose2d(F2_starting_x, F2_starting_y, Math.toRadians(starting_heading_F)))
                                                .forward(28)
                                                .waitSeconds(1) // place purple pixel
                                                .back(24)
                                                .turn(Math.toRadians(-90))
                                                .forward(86)
                                                .waitSeconds(1) // place yellow pixel
                                                .waitSeconds(1) // park
                                                .build()
                                );
                        break;
                    default: //right
                        myBot = new DefaultBotBuilder(meepMeep)
                                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                                .setDimensions(robotWidth, robotLength)
                                .setConstraints(20, 30, Math.toRadians(180), Math.toRadians(180), trackWidth)
                                .followTrajectorySequence(drive ->
                                        drive.trajectorySequenceBuilder(new Pose2d(F2_starting_x, F2_starting_y, Math.toRadians(starting_heading_F)))
                                                .forward(28)
                                                .turn(Math.toRadians(-90))
                                                .forward(6)
                                                .waitSeconds(1) // place yellow pixel
                                                .back(6)
                                                //.turn(Math.toRadians(-90))
                                                .strafeRight(24)
                                                .forward(86)
                                                //.back(14)
                                                //.turn(Math.toRadians(-90))
                                                //.forward(30)
                                                .waitSeconds(1) // place yellow pixel
                                                .waitSeconds(1) // park
                                                .build()
                                );
                        break;
                }
                break;
            default: //right
                myBot = new DefaultBotBuilder(meepMeep)
                        // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                        .setDimensions(robotWidth, robotLength)
                        .setConstraints(20, 30, Math.toRadians(180), Math.toRadians(180), trackWidth)
                        .followTrajectorySequence(drive ->
                                drive.trajectorySequenceBuilder(new Pose2d(A2_starting_x, A2_starting_y, Math.toRadians(starting_heading_A)))
                                        .forward(28)
                                        .turn(Math.toRadians(90))
                                        .forward(6)
                                        .waitSeconds(1) // place yellow pixel
                                        .back(6)
                                        //.turn(Math.toRadians(-90))
                                        .strafeLeft(24)
                                        .forward(86)
                                        //.back(14)
                                        //.turn(Math.toRadians(-90))
                                        //.forward(30)
                                        .waitSeconds(1) // place yellow pixel
                                        .waitSeconds(1) // park
                                        .build()
                        );
                break;
        }



        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}