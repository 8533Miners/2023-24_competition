package com.example.meepmeeptesting.auton;

public class AutonVariables {
    public enum StagePosition {
        APRON,
        BACKSTAGE
    }
    public enum AllianceColor {
        RED,
        BLUE
    }

    public enum FieldStartPosition {
        LEFT,
        RIGHT
    }

    public enum FieldParkPosition {
        NEAR_WALL,
        ON_BACKDROP,
        NEAR_CENTER
    }
    public enum SpikeMark {
        NONE,
        LEFT,
        CENTER,
        RIGHT
    }

    public enum ScoreStrategy {
        SCORE,
        NO_SCORE
    }
}
