//Drive in TeleOp

//        switch (bayState) {
//            case OPENED:
//                if (Math.abs(bay.getPosition() - Bay.openPosLeft) < 0.05 && baybutton==ButtonPressState.PRESSED_GOOD) {
//                    bay.close();
//                    bayState = BayState.CLOSED;
//                    telemetry.addLine("bayState" + bayState);
//                    telemetry.update();
//                    telemetry.addLine("pos " + pos);
//                    telemetry.update();
//                }
//                telemetry.addLine("OPENED");
//                telemetry.update();
//                break;
//
//            case CLOSED:
//                if (Math.abs(bay.getPosition() - Bay.closedPosLeft) < 0.05 && baybutton==ButtonPressState.PRESSED_GOOD) {
//                    bay.open();
//                    bayState = BayState.OPENED;
//                    telemetry.addLine("bayState" + bayState);
//                    telemetry.update();
//                    telemetry.addLine("pos " + pos);
//                    telemetry.update();
//                }
//
//                double position = Math.abs(bay.getPosition() - 0);
//                telemetry.addLine("position " + position);
//                telemetry.addLine("pressed? " + baybutton.toString());
//                telemetry.update();
//                break;
//            default:
//                bayState = BayState.CLOSED;
//                telemetry.addLine("bayState" + bayState);
//                telemetry.update();
//        }

// SLIDES & INTAKE
//        switch (rotatorState) {
//            case SLIDE_DOWN:
//                if (Math.abs(slides.getRotatorEncoder() - 0) < 10) {
//                    if (rotatorButton == ButtonPressState.PRESSED_GOOD) {
//                        rotatorState = RotatorState.SLIDE_UP;
//                        telemetry.addData("rot", slides.getRotatorEncoder());
//                        telemetry.update();
//                        slides.up();
//                    }
//                }
//            case SLIDE_UP:
//                if (Math.abs(slides.getRotatorEncoder() - 30) < 10) {
//                    if (rotatorButton == ButtonPressState.PRESSED_GOOD) {
//                        rotatorState = RotatorState.SLIDE_DOWN;
//                        telemetry.addData("rot", slides.getRotatorEncoder());
//                        telemetry.update();
//                        slides.down();
//                    }
//                }
//            default:
//                rotatorState = RotatorState.SLIDE_DOWN;
//                telemetry.addLine("default");
//                telemetry.update();
//        }



// goofy ahh claw code
//claw open/close dpad
//if (gamepad1.dpad_left) {
//telemetry.addLine("claw close");
//telemetry.update();
//claw.close();
//}
//if (gamepad1.dpad_right) {
//claw.open();
//telemetry.addLine("claw open");
//telemetry.update();
//}

// Claw tilt left/right dpad
//if (gamepad1.dpad_up) {
//telemetry.addLine("claw tilt forward");
//telemetry.update();
//telemetry.addLine(claw.toString());
//claw.tiltForward();
//}
//if (gamepad1.dpad_down) {
//telemetry.addLine("claw titl backwards");
//telemetry.update();
//claw.tiltBack();
//}