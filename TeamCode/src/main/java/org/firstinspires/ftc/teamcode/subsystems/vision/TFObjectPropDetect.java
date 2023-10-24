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

    public SpikeMark getSpikeMark() {
        List<Recognition> currentRecognitions = TFOD.getRecognitions();

        for (Recognition recognition : currentRecognitions) {
            if (recognition.getConfidence() > highestConf){
                highestConf = recognition.getConfidence();

                x = (recognition.getLeft() + recognition.getRight()) / 2 ;
            }
        }
        if (x < CAMERA_WIDTH/2) {
            spikeMark = SpikeMark.LEFT;
        } else if (x >= CAMERA_WIDTH/2) {
            spikeMark = SpikeMark.CENTER;
        } else spikeMark = SpikeMark.RIGHT; //defaults to RIGHT

        return spikeMark;
    }

}
