package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class LeftA4Traj {
    public static void main(String[] args) {
        int A4_starting_x = 16;
        int A4_starting_y = 62;
        int start_heading_A = 270;
//        int parking_offset = 2;
//        int parking_offset = 24;
        int parking_offset = 48;

        MeepMeep meepMeep = new MeepMeep(800);
        RoadRunnerBotEntity myBot;

        myBot = new DefaultBotBuilder(meepMeep)
                .setDimensions(13.5, 16)
                .setConstraints(15, 30, Math.toRadians(280), Math.toRadians(60), 10.56)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(A4_starting_x, A4_starting_y, Math.toRadians(start_heading_A)))
                                .strafeLeft(4)//initial move
                                .strafeLeft(2)
                                .forward(26)
                                .back(3.5)
//                                .addDisplacementMarker(() -> { //start placing purple pixel
//                                    picker.update(Picker.PickerState.OUTAKE);
//                                })
//                                .waitSeconds(1)
//                                .addDisplacementMarker(() -> { //stop placing purple pixel
//                                    picker.update(Picker.PickerState.HOLD);
//                                })
                                .strafeLeft(24)
                                .build()
                );


        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }

}
