package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class LeftF2Traj {
    public static void main(String[] args) {
        int F2_starting_x = -40;
        int F2_starting_y = -62;

        int A2_starting_x = -40;
        int A2_starting_y = 62;
        int start_heading_F = 90;
//        int parking_offset = 2;
        int parking_offset = 24;
//        int parking_offset = 48;


        int start_heading_A = 270;

        MeepMeep meepMeep = new MeepMeep(800);
        RoadRunnerBotEntity myBot;

        double board_offset = 6;

        myBot = new DefaultBotBuilder(meepMeep)
                .setDimensions(13.5, 16)
                .setConstraints(15, 30, Math.toRadians(280), Math.toRadians(60), 10.56)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(F2_starting_x, F2_starting_y, Math.toRadians(start_heading_A)))
                                .strafeRight(4)//initial move
                                .lineToLinearHeading(new Pose2d(-40, -31, Math.toRadians(start_heading_A)))
                                .waitSeconds(2)
                                .lineToLinearHeading(new Pose2d(A2_starting_x, -60, Math.toRadians(180)))
                                .lineToLinearHeading(new Pose2d(14, -60, Math.toRadians(180)))
                                .splineToLinearHeading(new Pose2d(50, -35+board_offset, Math.toRadians(180)), Math.toRadians(0))
                                .build()
                );


        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }

}
