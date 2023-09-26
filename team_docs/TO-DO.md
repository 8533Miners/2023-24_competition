
## MOTION

##### THREE-WHEEL ODO

- Verify X and Y multiplier values, and incorporate in StandardTrackingWheelLocalizer
- Tune kV, kA, kS with new robot weight
- datalog encoder vs. IMU data and process

Potential fixes/issues:
- re-route encoder wiring to avoid interference from motors
- ensure parallel encoders are on same center line

##### TWO-WHEEL ODO

- Reconfigure bot to drop one parallel dead wheel - https://learnroadrunner.com/dead-wheels.html#two-wheel-odometry
- Measure x/y offsets
	- 
- setLocalizer in SampleMecanumDrive
- Tune and test

##### TELEOP AUGMENTATION
- Automated/recorded position - https://github.com/NoahBres/road-runner-quickstart/blob/advanced-examples/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/drive/advanced/TeleOpAugmentedDriving.java
- 



## VISION

- webcam mount

##### APRILTAGS

- Custom tag generation?
- how to include custom tags in library for positional info
- how to incorporate visionpipeline in our code