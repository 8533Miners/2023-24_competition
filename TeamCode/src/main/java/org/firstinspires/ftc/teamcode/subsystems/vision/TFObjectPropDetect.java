package org.firstinspires.ftc.teamcode.subsystems.vision;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.List;

public class TFObjectPropDetect {
    private SpikeMark spikeMark = SpikeMark.NONE;
    private String LABEL;
    private int CAMERA_WIDTH;
    private TfodProcessor TFOD;
    private float highestConf = 0;
    private double x = 0;
    public TFObjectPropDetect(){

    }
    public TFObjectPropDetect(TfodProcessor tfod, int cWidth, String label){
        TFOD = tfod;
        CAMERA_WIDTH = cWidth;
        LABEL = label;
    }

    public SpikeMark getSpikeMark(boolean invertDetect) {
        List<Recognition> currentRecognitions = TFOD.getRecognitions();
        boolean cupDetected = false;

        for (Recognition recognition : currentRecognitions) {
            if (recognition.getConfidence() > highestConf && recognition.getLabel() == LABEL){
                highestConf = recognition.getConfidence();
                cupDetected = true;

                x = (recognition.getLeft() + recognition.getRight()) / 2 ;
            }
        }

        if (currentRecognitions.size() >= 1 && cupDetected) {
            if (invertDetect) {
                spikeMark = (x >= CAMERA_WIDTH / 2) ? SpikeMark.RIGHT : SpikeMark.CENTER;
            } else {
                spikeMark = (x < CAMERA_WIDTH / 2) ? SpikeMark.LEFT : SpikeMark.CENTER;
            }
        }
        return spikeMark;
    }

}
