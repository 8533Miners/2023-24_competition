package org.firstinspires.ftc.teamcodetestbot.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcodetestbot.subsystems.SubSystemConstants.*;

/*
* Ideal Functions:
* Ready to receive pixel (claw open, ejector retracted)
* Stowed (claw closed, ejector retracted)
* Transfer (claw closed, ejector cradled)
* Score first pixel (claw score, ejector push first)
* Score second pixel (claw score, ejector push both)
* Return (claw open, ejector retracted) [currently the same as ready]
*
* Configurable Parameters:
* Located in SubSystemConstants in:
* ClawPosition
* EjectorPosition
*
* Unintended Outputs:
* Ejector keeps gripper from being fully returned
* Ejector catches on claw when scoring pixel keeping the pixel from being pushed
* Ejector catches on claw when going back to retracted
*/
public class Gripper {
    private Servo claw;
    private Servo ejector;
    public enum GripperState {
        READY(ClawPosition.OPEN,
                EjectorPosition.RETRACT),
        STOWED(ClawPosition.CLOSED,
                EjectorPosition.RETRACT),
        TRANSFER(ClawPosition.CLOSED,
                EjectorPosition.CRADLE),
        SCORE_FIRST(ClawPosition.SCORE,
                EjectorPosition.PUSH_FIRST),
        SCORE_SECOND(ClawPosition.OPEN,
                EjectorPosition.PUSH_BOTH),
        RETURN(ClawPosition.OPEN,
                EjectorPosition.RETRACT);
        public final double claw_pos;
        public final double ejec_pos;
        GripperState(ClawPosition claw_pos, EjectorPosition ejec_pos) {
            this.claw_pos = claw_pos.getClawPosition();
            this.ejec_pos = ejec_pos.getEjectorPosition();
        }
    }
    public Gripper(HardwareMap hardwareMap) {
        claw = hardwareMap.get(Servo.class, "claw");
        ejector = hardwareMap.get(Servo.class, "ejector");

        //Unneeded when setting the MAX and MIN points with the REV servo programmer
        //claw.scaleRange(CLAW_MIN_PERCENT,CLAW_MAX_PERCENT);
        //pusher.scaleRange(0.0,1.0);//PUSHER_MIN_PERCENT,PUSHER_MAX_PERCENT);
    }

    public void update(GripperState desired_state) {
        /*
         * TODO we may need to consider timing to reach desired state to correct for some
         *  unintended states if so we would keep track of current state and move towards
         *  our desired state
         */
        claw.setPosition(desired_state.claw_pos);
        ejector.setPosition(desired_state.ejec_pos);
    }

    public void log(Telemetry tele) {
    }

}
