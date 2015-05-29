package movementProcessor;


public class changeCalculation {
	
	private Coordinate leftStartPoint;
	private Coordinate rightStartPoint;
	private float maxRoll = 0.3f;
	private float minRoll = 0.1f;
	private float roll = 0f;
	private float yaw = 0f;
	private float pitch=0f;
	private float aspeed = 0f;
	
	//TEMP
    private float securityScaleBuffer = 0.8f;
    private float maxPitch = 0.2f * securityScaleBuffer;
    private static final float MIN_PITCH = 0.2f;

	
	
    public void setLeftHandStart(Coordinate leftStart) {
        leftStartPoint = leftStart;
    }

    public void setRightHandStart(Coordinate rightStart) {
        rightStartPoint = rightStart;
    }

    
    public void calculateMoves(Hand leftHand, Hand rightHand) {
        calculateYaw(leftHand, rightHand);
        calculateRoll(leftHand, rightHand);
        calculatePitch(leftHand, rightHand);
        calculateAltitudeSpeed(leftHand, rightHand);
    }
	
    private void calculateRoll(Hand leftHand, Hand rightHand) {
        float roll = leftHand.getCoordinate().getY() - rightHand.getCoordinate().getY();
        roll = roll/150;
        
        if (Math.abs(roll) <= minRoll) {
            roll = 0;
        }
        if (Math.abs(roll) >= maxRoll) {
        	roll = maxRoll*Math.signum(roll);
        }       


        this.roll = -roll;
    }
    private void calculateYaw(Hand leftHand, Hand rightHand) {
        float yaw = (leftHand.getCoordinate().getZ()-leftStartPoint.getZ()) + (rightStartPoint.getZ()-rightHand.getCoordinate().getZ());
        
        if(Math.abs(yaw)>0.05){
        	if(Math.abs(yaw)>0.15){
        		yaw = 0.3f*Math.signum(yaw);
        	}
        	else{
        		yaw= ((float)Math.round((yaw*2f)*1000))/1000;
        	}
        }
        else{
        	yaw = 0f;
        }
        //System.out.println(yaw);
        this.yaw = -yaw;
    }
    
    private void calculatePitch(Hand leftHand, Hand rightHand) {
//        float pitch = ((leftHand.getCoordinate().getZ() - leftStartPoint.getZ()) -
//                (rightHand.getCoordinate().getZ() - rightStartPoint.getZ()));
//        
//        pitch = (float)(Math.round(pitch*10000))/1000;
//        if(Math.abs(pitch)>0.05)
//        	System.out.println(pitch);
//
//
//        if(Math.abs(pitch)>0.01){
//
//        	if(Math.abs(pitch)>0.03){
//        		pitch = Math.signum(pitch) * 0.3f;
//        	}
//        	else{
//        		pitch = pitch*10;
//        	}
//        	
//        	
//        }
//        else
//        	pitch = 0f;
//        
//        this.pitch = pitch;
    	
    	

        float pitch = ((leftHand.getCoordinate().getZ() - leftStartPoint.getZ()) +
                (rightHand.getCoordinate().getZ() - rightStartPoint.getZ())) / 2;

        //System.out.println("pre" + pitch);
        //System.out.println(leftHand.getCoordinate().getZ() - leftStartPoint.getZ());
        if (Math.abs(pitch) >= Math.abs(maxPitch)) {
            maxPitch = Math.abs(pitch);
        }

        //Scale pitch using maxPitch
        pitch = pitch / maxPitch;


        if (Math.abs(pitch) <= 0.1) {
            pitch = 0;
        }

        pitch = pitch - Math.signum(pitch) * MIN_PITCH;
        if(Math.abs(pitch)>0.3){
        	pitch = Math.signum(pitch)*0.3f;
        }
        if(Math.abs(pitch)<0.1){
        	pitch = 0f;
        }       
        if(Math.abs(leftHand.getCoordinate().getZ() - leftStartPoint.getZ())<0.01||Math.abs(rightHand.getCoordinate().getZ() - rightStartPoint.getZ())<0.01||Math.signum(leftHand.getCoordinate().getZ() - leftStartPoint.getZ())!=Math.signum(rightHand.getCoordinate().getZ() - rightStartPoint.getZ())){
        	pitch = 0f;
        }
        //System.out.println("post " + pitch);
        //times -1 since with drone control forward is positive
        this.pitch = pitch;
    }
    
    private void calculateAltitudeSpeed(Hand leftHand, Hand rightHand) {
    	float altitude = ((leftHand.getCoordinate().getY() - leftStartPoint.getY()) +
                (rightHand.getCoordinate().getY() - rightStartPoint.getY()));
	
    	if((leftHand.getCoordinate().getY() - leftStartPoint.getY()>5)&&(rightHand.getCoordinate().getY() - rightStartPoint.getY()>5)){
    		altitude = altitude/2;	
    	}
    	else if((leftHand.getCoordinate().getY() - leftStartPoint.getY()<-10)&&(rightHand.getCoordinate().getY() - rightStartPoint.getY()<-10)){
    		altitude = altitude/4;
    	}
    	else{
    		altitude = -0f;
    	}
    	altitude = altitude/100;
    	if(Math.abs(altitude)>0.3)
    		altitude = Math.signum(altitude) * 0.3f;
    	
    	//possibly set minimum
    	
//    	if(Math.abs(altitude)>0.01){
//    		if(Math.abs(altitude)>0.07){
//    			altitude = Math.signum(altitude)*0.3f;
//    		}
//    		else{
//    			altitude = Math.signum(altitude)*(Math.abs(altitude)-0.01f)*5;
//    		}
//    	}   
//    	else{
//    		altitude = 0;
//    	}

    	//System.out.println(altitude);
    	aspeed = -altitude;

    }
    
    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

    public float getPitch() {
        return pitch;
    }

    public float getASpeed() {
        return aspeed;
    }

	
	
	
	
    public boolean hasHandStarts() {
        return (rightStartPoint != null) && (leftStartPoint != null);
    }	
	
	
	
}
