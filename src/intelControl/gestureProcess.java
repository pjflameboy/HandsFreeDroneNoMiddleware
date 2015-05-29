package intelControl;

import java.awt.Color;

import intel.pcsdk.PXCMGesture;
import intel.pcsdk.PXCMGesture.GeoNode;

public class gestureProcess {
	private boolean flying;
	private boolean ready;
	private PXCMGesture.GeoNode[][] HandNode;
	//private int centX1, centX2, centY1, centY2;
	private int centX1 = 208;
	private int centX2 = 89;
	private int centY  = 153;
	private float startZ1, startZ2;
	PXCMGesture.GeoNode[][] geo;
	
	
	public gestureProcess(int X, int Y){
//		centX1 = X;
//		centY1 = 2*Y/3;
//		centX2 = 3*X/4;
//		centY2 = 2*Y/3;
		//image = addcircle(image,(int)(csize[0]/4),(int)(2*csize[1]/3),40,Color.darkGray);
	    //image = addcircle(image,(int)(3*csize[0]/4),(int)(2*csize[1]/3),40,Color.darkGray);
	}
	
	public String calcNextMove(PXCMGesture.GeoNode[][] geo1){
		//geonode sometimes swaps hands so a check needs to be made
		try{
			geo = new PXCMGesture.GeoNode[2][10];
			if(geo1[0][0].positionImage.x>geo1[1][0].positionImage.x){
				
				geo[0] = geo1[1];
				geo[1] = geo1[0];
			}
			else{
				geo = geo1;
			}
		}
		catch(Exception e){
			geo = geo1;
		}
		//checks to see if the conditions match those needed to takeoff
		try{
			if(flying==false){
				if(ready==true){
					for(int i = 0; i<5; i++){
						if(geo[0][i+1].body==0)
							ready = false;
						if(geo[1][i+1].body==0)
							ready = false;
					}
					if(ready==false){
						flying = true;
						startZ1 = geo[0][0].positionWorld.z;
						startZ2 = geo[1][0].positionWorld.z;
						return "takeoff";
					}
				}
			ready = true;
				if((geo[0][0].positionImage.x-centX2)<-5||(geo[0][0].positionImage.x-centX2)>5)
					ready = false;
				if((geo[1][0].positionImage.x-centX1)<-5||(geo[1][0].positionImage.x-centX1)>5)
					ready = false;
				if((geo[0][0].positionImage.y-centY)<-5||(geo[0][0].positionImage.y-centY)>5)
					ready = false;
				if((geo[1][0].positionImage.y-centY)<-5||(geo[1][0].positionImage.y-centY)>5)
					ready = false;
				for(int i = 0; i<5; i++){
					if(geo[0][i+1].body==0)
						ready = false;
					if(geo[1][i+1].body==0)
						ready = false;
				}
			
			if(ready==true){
				return "ready for takeoff";
			}
			return " ";
			}
		}
		catch(Exception e){
			ready = false;
		}
		//checks to make when flying
		//note should only reach this section if the drone is in the air
		
		//if fingers present land
		boolean landNow = true;
		try{

			for(int i = 0; i<5; i++){
				if(geo[0][i+1].body==0)
					landNow = false;
				if(geo[1][i+1].body==0)
					landNow = false;
			}
		}
		catch(Exception e){
			landNow = false;
		}
		try{
			if(geo[0][0].body==0)
				landNow = true;
			if(geo[1][0].body==0)
				landNow = true;

		}
		catch(Exception e){
			landNow = true;
		}
		if(landNow)
			return "land";
		
		float[] moveCoords = {0f,0f,0f,0f};
		
		try{
			//Note yadrone library takes negative value to mean go left (as does yDif)
			//NOTE!!!!! z appears to be y in world coordinates
			System.out.println(geo[0][0].positionWorld.z-geo[1][0].positionWorld.z);
			System.out.println(geo[0][0].positionImage.y-geo[1][0].positionImage.y);
			float yDif = geo[0][0].positionImage.y-geo[1][0].positionImage.y;
			if(yDif>10){
				if(yDif/3>30)
					moveCoords[0] = 30f;
				else
					moveCoords[0] = (Math.round((yDif/3)*100))/100;
	
			}
			if(yDif<-10){
				if(yDif/3<-30)
					moveCoords[0] = -30f;
				else
					moveCoords[0] = (Math.round((yDif/3)*100))/100;
	
			}
			
			if(geo[0][0].positionImage.y-centY>8&&geo[1][0].positionImage.y-centY>8){
				if(geo[0][0].positionImage.y-centY+geo[1][0].positionImage.y-centY>80){
					moveCoords[2] = -40f;
				}
				else{
					moveCoords[2] = -(geo[0][0].positionImage.y-centY+geo[1][0].positionImage.y-centY)/2;
				}
			}
			if(geo[0][0].positionImage.y-centY<-8&&geo[1][0].positionImage.y-centY<-8){
				if(geo[0][0].positionImage.y-centY+geo[1][0].positionImage.y-centY<-80){
					moveCoords[2] = 40f;
				}
				else{
					moveCoords[2] = -(geo[0][0].positionImage.y-centY+geo[1][0].positionImage.y-centY)/2;
				}
			}
			int intZ1 = Math.round((geo[0][0].positionWorld.z)*1000);
			int intZ2 = Math.round((geo[1][0].positionWorld.z)*1000);
			int intstartZ1 = Math.round((startZ1)*1000);
			int intstartZ2 = Math.round((startZ2)*1000);
			int diff1 = intZ1 - intstartZ1;
			int diff2 = intZ2 - intstartZ2;
			
			//System.out.println((Math.round((geo[0][0].positionWorld.z)*1000))-(Math.round((startZ1)*1000)));
			if(diff2>6&&diff2>6){
				if(diff1+diff2>60){
					moveCoords[1] = 30f;
				}
				else{
					moveCoords[1] = (diff1+diff2);
				}
			}
			if(diff2<-6&&diff2<-6){
				if(diff1+diff2<-60){
					moveCoords[1] = -30f;
				}
				else{
					moveCoords[1] = (diff1+diff2)/2;
				}
			}
			
			if(diff1-diff2>5){
				if(diff1-diff2>30){
					moveCoords[3] = 30f;
				}
				else{
					moveCoords[3] = (diff1-diff2);
				}
			}
			if(diff2-diff2<-5){
				if(diff1-diff2<-30){
					moveCoords[3] = -30f;
				}
				else{
					moveCoords[3] = (diff1-diff2);
				}
			}

			
			
		}
		catch(Exception E){
			E.printStackTrace();
		}
		
		if(moveCoords[0]!=0||moveCoords[1]!=0||moveCoords[2]!=0||moveCoords[3]!=0){
			//System.out.println("move(" + moveCoords[0] + "," + moveCoords[1] + "," + moveCoords[2] + "," +moveCoords[3] + ")");
			return "move(" + moveCoords[0] + "," + moveCoords[1] + "," + moveCoords[2] + "," +moveCoords[3] + ")";}
		
		
		
		
			
			
			
		return "";

		
	}

}
