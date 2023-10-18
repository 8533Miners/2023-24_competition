package org.firstinspires.ftc.teamcode.subsystems;

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
        OPEN(0.0),
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
}
