package org.firstinspires.ftc.teamcodetestbot.subsystems;

import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

public class SubSystemConstants {

    public enum PlacerHeight {
        // backdrop + pixel height
        FIRST(100),
        SECOND(150),
        THIRD(200),
        FOURTH(250),
        FIFTH(300);
        private final int placerHeight;

        PlacerHeight(int placerHeight) {
            this.placerHeight = placerHeight;
        }
        public int getPlacerHeight() {
            return placerHeight;
        }
    }
    public enum ClawPosition {
        // Percent of claw servo range
        OPEN(0.52),
        SCORE(0.57),
        CLOSED(0.6);
        private final double clawPosition;

        ClawPosition(double clawPosition) {
            this.clawPosition = clawPosition;
        }
        public double getClawPosition() {
            return clawPosition;
        }
    }

    public enum EjectorPosition {
        // Percent of ejector servo range
        RETRACT(0.0),
        CRADLE(0.70),
        PUSH_FIRST(0.76),
        PUSH_BOTH(0.77);
        private final double ejectorPosition;
        EjectorPosition(double ejectorPosition) {
            this.ejectorPosition = ejectorPosition;
        }
        public double getEjectorPosition() {
            return ejectorPosition;
        }
    }
    public static final PIDCoefficients ELEVTATOR_PIDF_COEF = new PIDCoefficients(
            1.0,
            0.0,
            1.0
    );

}
