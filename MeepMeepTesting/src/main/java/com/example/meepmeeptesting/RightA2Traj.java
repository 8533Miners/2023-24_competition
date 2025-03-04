package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class RightA2Traj {
    public static void main(String[] args) {
        int A2_starting_x = -40;
        int A2_starting_y = 62;
        int start_heading_A = 270;
//        int parking_offset = 2;
//        int parking_offset = 24;
        int parking_offset = 48;
        double target_heading_A = 90;

        MeepMeep meepMeep = new MeepMeep(800);
        RoadRunnerBotEntity myBot;

        myBot = new DefaultBotBuilder(meepMeep)
                .setDimensions(13.5, 16)
                .setConstraints(15, 30, Math.toRadians(280), Math.toRadians(60), 10.56)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(A2_starting_x, A2_starting_y, Math.toRadians(start_heading_A)))
                                .strafeRight(4)//initial move
                                .lineToLinearHeading(new Pose2d(-29, 34, 270))
                                .lineToLinearHeading(new Pose2d(-52, 60, Math.toRadians(180)))
                                .lineToConstantHeading(new Vector2d(14, 60))
                                .splineToConstantHeading(new Vector2d(49, 35), Math.toRadians(90))
                                .splineToConstantHeading(new Vector2d(47, 59), Math.toRadians(0))
                                .build()
                );


        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }

}
