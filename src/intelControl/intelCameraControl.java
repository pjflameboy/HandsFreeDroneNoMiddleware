package intelControl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

//import positionData.*;
import movementProcessor.*;


import javax.swing.ImageIcon;

import de.yadrone.apps.tutorial.TutorialCommander;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import displayWindows.cameraDisplay;
import intel.pcsdk.PXCMGesture;
import intel.pcsdk.PXCMGesture.GeoNode;
import intel.pcsdk.PXCMCapture;
import intel.pcsdk.PXCMPoint3DF32;
import intel.pcsdk.PXCMPointF32;
import intel.pcsdk.PXCUPipeline;
import intel.pcsdk.PXCMGesture.Gesture;

public class intelCameraControl {
	//booleans that control what is shown on each bufferedimage
	public boolean showRGBVid = false;
	public boolean showDepth = true;
	public boolean showGeoNodes = true;
	public boolean showFingers = true;
	public boolean showPalms = true;
	public boolean showhandextremes = false;
	//PXCMGesture array for storing currently detected gestures
	//private Gesture gesture = new Gesture();
	//java c++ pipeline for entire intel sdk library
	private PXCUPipeline pipeline;
	//depth and colour capture sizes
	private int[] dsize;
	private int[] csize;
	//image we are taking from camera and manipulating
	BufferedImage image;
	//hands present images
	ImageIcon leftHandImage;
	ImageIcon rightHandImage;
	ImageIcon blankImage;
	private cameraDisplay camdisp;
	private gestureProcess gesproc;
	private geoNodeUpdate geonodeup;
	private CommandManager cmd;
	
	public intelCameraControl(){
		pipeline=new PXCUPipeline();	
		camdisp = new cameraDisplay(this);
		IARDrone drone = null;
		try
		{
			// Tutorial Section 1
			drone = new ARDrone();
			drone.start();
			
			//TutorialCommander commander = new TutorialCommander(drone);
			cmd = drone.getCommandManager();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		init();
		for(;;)
			nextFrame();
		
		//pipeline.Close();
	      // System.exit(0);

	}
	
	public void init(){
		if (!pipeline.Init(PXCUPipeline.COLOR_VGA|PXCUPipeline.GESTURE)) {
			System.out.print("Failed to initialize PXCUPipeline\n");
			System.exit(1);
	    }
		
		//set depth and colour image sizes in pixels
		dsize=new int[2];
	    pipeline.QueryDepthMapSize(dsize);
	    csize=new int[2];
	    pipeline.QueryRGBSize(csize);
	    gesproc = new gestureProcess(csize[0],csize[1]);
		
	   
	    
	    //images used to show hands detected in the displaywindow
	    try{
	    	leftHandImage = new ImageIcon("leftHand.jpg");
	    	rightHandImage = new ImageIcon("rightHand.jpg");
	    	blankImage = new ImageIcon("blank.jpg");
	    	camdisp.leftHand.setIcon(blankImage);
		    camdisp.rightHand.setIcon(blankImage);
	    }
	    catch(Exception e){
	    	System.out.println("ERROR hand picture resources not found");
	    	System.exit(2);
	    }
	    camdisp.setVisible(true);
	    
	    geonodeup = new geoNodeUpdate(cmd);

	}
	
	private void nextFrame(){
		
		if (!pipeline.AcquireFrame(true))
			return;
		
		//blank image to paint to
		image=new BufferedImage(csize[0],csize[1],BufferedImage.TYPE_INT_RGB);
		
		//one dimensional array of all depth points row by row
		short[]          depthmap=new short[dsize[0]*dsize[1]];
		//1D array of 3d depth points
	    PXCMPoint3DF32[] p3=new PXCMPoint3DF32[dsize[0]*dsize[1]];
	    //1D array of 2d depthpoints where depth to RGB locations will be stored
	    PXCMPointF32[]   p2=new PXCMPointF32[dsize[0]*dsize[1]];
	    //will determine whether a depth point is 'significant' and should be displayed
	    float[]          untrusted=new float[2];
	    pipeline.QueryDeviceProperty(PXCMCapture.Device.PROPERTY_DEPTH_SATURATION_VALUE,untrusted);
	    
	    //since the depth camera gives a z 'distance' x and y are arbitrary and in order row by row
	    for (int xy=0,y=0;y<dsize[1];y++)
	    	   for (int x=0;x<dsize[0];x++,xy++)
	    		   p3[xy]=new PXCMPoint3DF32(x,y,0);
	    
	    //if showRGB checkbox is true then we first set bufferedimage to the current RGB camera frame
	    if(showRGBVid){
     	   pipeline.QueryRGB(image);
        }
	    
	    //if there is a new depth map take it
	    if (pipeline.QueryDepthMap(depthmap)) {
	    	//set z depth values
			for (int xy=0;xy<p3.length;xy++)
				p3[xy].z=(float)depthmap[xy];
			
			//convert depth x,y locations to their associated positions on the RGB image
			//note the depth camera is of half the resolution of the RGB camera
			if (pipeline.MapDepthToColorCoordinates(p3,p2)) {
				for (int xy=0;xy<p2.length;xy++) {
					//if the depth coordinate is insignificant don't map it (or we would end up with all points and a black image!!)
					if (depthmap[xy]==untrusted[0] || depthmap[xy]==untrusted[1])
						continue;
					
					int x1=(int)p2[xy].x, y1=(int)p2[xy].y;

					if (x1<0 || x1>=csize[0] || y1<0 || y1>=csize[1])
						continue;
					//if showDepth==true then set the depth pixel in the RGB image to a red pixel
					if(showDepth){
						image.setRGB(x1,(int)(y1),(int)0xffff0000);
					}
				}
			}
			//System.out.println("102,50  " + p3[(dsize[0]*50)+102].z + "," + p2[(dsize[0]*50)+102].y);
			
			//mapping depth to colour is not as simple as a size scale so calculating finger positions
			//to draw on is done with mapdepthtocolorcoordinates

	    }
	    image = addcircle(image,(int)(csize[0]/4),(int)(3*csize[1]/5),40,Color.darkGray);
	    image = addcircle(image,(int)(3*csize[0]/4),(int)(3*csize[1]/5),40,Color.darkGray);
	    
	    //check for fingers/hands
	    PXCMGesture.GeoNode[][] HandNode = new PXCMGesture.GeoNode[2][10];
	    pipeline.QueryGeoNode(PXCMGesture.GeoNode.LABEL_BODY_HAND_LEFT,HandNode[0]);
	    pipeline.QueryGeoNode(PXCMGesture.GeoNode.LABEL_BODY_HAND_RIGHT,HandNode[1]);
	    		
	    ///working section
	    
	    PXCMGesture.GeoNode leftHandGeoNode = new PXCMGesture.GeoNode();
	    PXCMGesture.GeoNode rightHandGeoNode = new PXCMGesture.GeoNode();
	    PXCMGesture.Gesture gesture = new PXCMGesture.Gesture();
	    boolean thumbsup = false;
	    boolean thumbsdown = false;
	    if(pipeline.QueryGesture(PXCMGesture.Gesture.LABEL_ANY, gesture))
	    	if(gesture.label==gesture.LABEL_POSE_THUMB_UP)
	    		thumbsup = true;
	    if(pipeline.QueryGesture(PXCMGesture.Gesture.LABEL_ANY, gesture))
	    	if(gesture.label==gesture.LABEL_POSE_THUMB_DOWN)
	    		thumbsdown = true;

        pipeline.QueryGeoNode(PXCMGesture.GeoNode.LABEL_BODY_HAND_RIGHT, leftHandGeoNode);
        pipeline.QueryGeoNode(PXCMGesture.GeoNode.LABEL_BODY_HAND_LEFT, rightHandGeoNode);
//        if(rightHandGeoNode.positionWorld!=null){
//        	System.out.println(rightHandGeoNode.positionWorld.z);
//        }
	    geonodeup.updateFeatures(leftHandGeoNode,rightHandGeoNode,thumbsup,thumbsdown);
	    float newMove[] = new float[4];
	    newMove[0] = geonodeup.change.getRoll();
	    newMove[1] = geonodeup.change.getPitch();
	    newMove[3] = geonodeup.change.getYaw();
	    newMove[2] = geonodeup.change.getASpeed();
	    
	    	
//	    if (nextMove.equals("takeoff"))
//    	cmd.takeOff();
//    if(nextMove.equals("land"))
//    	cmd.landing();
	    if(geonodeup.flying)
	    	camdisp.setAction("flying");
	    if(newMove[0]!=0||newMove[1]!=0||newMove[2]!=0||newMove[3]!=0){
    		cmd.move(newMove[0], newMove[1], newMove[2], newMove[3]);
    		camdisp.setAction("move(" + newMove[0]+" , "+ newMove[1] + " , " +  newMove[2]+" , " + newMove[3]+ ")");
    		System.out.println("move(" + newMove[0]+" , "+ newMove[1]/2 + " , " +  newMove[2]+" , " + newMove[3]+ ")");
	    }
	    else{
	    	if(geonodeup.flying)
	    		cmd.hover();
	    }
	    //System.out.println(geonodeup.coCalc.getPitch());
	    

	    
	    
	    
//	    String nextMove = gesproc.calcNextMove(HandNode);
//	    if(nextMove.length()>0){
//	    	camdisp.setAction("cmd:" + nextMove);
//	    }
//	    if (nextMove.equals("takeoff"))
//	    	cmd.takeOff();
//	    if(nextMove.equals("land"))
//	    	cmd.landing();
//	    if(nextMove.contains("move")){
//	    	cmd.move(gesproc.moveCoords[0]/100, 0f, gesproc.moveCoords[2]/100, 0f);
//	    }
	    
	    
		PXCMPoint3DF32[] handpoints=new PXCMPoint3DF32[20];
		PXCMPointF32[]   newHP=new PXCMPointF32[20];
		for(int i = 0; i<20; i++){
			handpoints[i] = new PXCMPoint3DF32(0,0,200);
			newHP[i] = new PXCMPointF32(0,0);
		}
	    
	    for(int i = 0; i<2;i++)
	    	for(int j = 0; j<10;j++)
	    		if(HandNode[i][j].body!=0)
	    			handpoints[(i*10)+j] = new PXCMPoint3DF32(HandNode[i][j].positionImage.x,HandNode[i][j].positionImage.y,200);
	    pipeline.MapDepthToColorCoordinates(handpoints,newHP);
	    
	    //draw hand nodes as circles to the image
	    //note for some reason mapdepthtocolorcoordinates appears to map x coords 50ish off target
	    if(showPalms){
	    	if(HandNode[0][0].body!=0){
	    		image = addcircle(image,(int)newHP[0].x-50,(int)newHP[0].y-10,40,Color.green);
	    		camdisp.rightHand.setIcon(leftHandImage);
	    	}
	    	else{
	    		camdisp.rightHand.setIcon(blankImage);
	    	}
	    	if(HandNode[1][0].body!=0){
	    		image = addcircle(image,(int)newHP[10].x-50,(int)newHP[10].y-10,40,Color.green);
	    		camdisp.leftHand.setIcon(rightHandImage);
	    	}
	    	else{
	    		camdisp.leftHand.setIcon(blankImage);
	    	}
	    	
	    }
	    if(showFingers){
	    	for(int i = 0; i<5;i++){
	    		image = addcircle(image,(int)newHP[i+1].x-50,(int)newHP[i+1].y-10,20,Color.blue);	    		
	    		image = addcircle(image,(int)newHP[i+11].x-50,(int)newHP[i+11].y-10,20,Color.blue);
	    	}
	    	
	    }
	    if(showhandextremes){
	    	for(int i = 0; i<3;i++){
	    		image = addcircle(image,(int)newHP[i+6].x-50,(int)newHP[i+6].y-10,20,Color.gray);
	    		image = addcircle(image,(int)newHP[i+16].x-50,(int)newHP[i+16].y-10,20,Color.gray);
	    	}
	    }

	    //we want a mirror image on the screen for ease of understanding
	    image = getFlippedImage(image);

		camdisp.viewLabel.setIcon(new ImageIcon(image));
	    

	    
	    
	    
	    
	    pipeline.ReleaseFrame();
	}
	
	public BufferedImage getFlippedImage(BufferedImage bi) {
	       BufferedImage flipped = new BufferedImage(
	               bi.getWidth(),
	               bi.getHeight(),
	               bi.getType());
	       AffineTransform tran = AffineTransform.getTranslateInstance(bi.getWidth(), 0);
	       AffineTransform flip = AffineTransform.getScaleInstance(-1d, 1d);
	       tran.concatenate(flip);

	       Graphics2D g = flipped.createGraphics();
	       g.setTransform(tran);
	       g.drawImage(bi, 0, 0, null);
	       g.dispose();

	       return flipped;
	   }
	
	   public BufferedImage addcircle(BufferedImage bi,int x,int y,int radius,Color colour) {
		   if(x<10||y<10)
			   return bi;
	       BufferedImage circled = new BufferedImage(
	               bi.getWidth(),
	               bi.getHeight(),
	               bi.getType());
	       Graphics2D gp = circled.createGraphics();
	       gp.setColor(colour);
	       gp.drawImage(bi, 0, 0, null);
	       gp.drawOval(x,y,radius,radius);
	       gp.dispose();

	       return circled;
	   }
	
	
	
	
	
	
	
	
	
}