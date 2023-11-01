package org.firstinspires.ftc.teamcode.subsystems;

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
        OPEN(0.50), //TODO find value
        SCORE(0.575),
        CLOSED(1.0);
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
        CRADLE(0.74),
        PUSH_FIRST(0.82),
        PUSH_BOTH(0.86);
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
