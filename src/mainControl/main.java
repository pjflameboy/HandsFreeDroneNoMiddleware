package mainControl;
import javax.swing.JOptionPane;

import intelControl.intelCameraControl;

public class main {
	public static void main(String[] args){
		String messageDialog = "Place open hands over the circles (match palm to circle)\nClose hands to take off\nOpen hands to land";
		//JOptionPane.showMessageDialog(null, messageDialog);
		intelCameraControl icc = new intelCameraControl();
	}

}
