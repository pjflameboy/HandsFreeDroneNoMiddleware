package displayWindows;

import javax.swing.*;

import intelControl.intelCameraControl;

public class cameraDisplay extends JFrame{
	private intelCameraControl camfeed;
	public cameraDisplay(intelCameraControl camcon) {
    System.out.println("CREATING GUI");    
    camfeed = camcon;

	initComponents();
	}
	
    private void initComponents() {

        jSeparator1 = new JSeparator();
        showRGB = new JRadioButton();
        commandsLabel = new JLabel();
        showNodes = new JRadioButton();
        palmCheckbox = new JCheckBox();
        handextremeCheckbox = new JCheckBox();
        fingerCheckbox = new JCheckBox();
        lastCommandLabel = new JLabel();
        lastcommandOutput = new JTextField();
        forceLandButton = new JButton();
        camFeedPanel = new JPanel();
        viewLabel = new JLabel();
        showDepth = new JRadioButton();
        jSeparator2 = new JSeparator();
        jSeparator3 = new JSeparator();
        jSeparator4 = new JSeparator();
        lastCommandLabel1 = new JLabel();
        leftHand = new JLabel();
        rightHand = new JLabel();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jSeparator1.setOrientation(SwingConstants.VERTICAL);

        showRGB.setText("RGB");
        showRGB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if(camfeed.showRGBVid){
                	camfeed.showRGBVid = false;
                }
                else{
                	camfeed.showRGBVid = true;
                }
            }
        });

        commandsLabel.setFont(new java.awt.Font("Tahoma", 0, 18));
        commandsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        commandsLabel.setText("  Options");

        showNodes.setText("Show Nodes");
        showNodes.setToolTipText("");
        showNodes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if(camfeed.showGeoNodes){
                	camfeed.showGeoNodes = false;
                	palmCheckbox.setEnabled(false);
                	fingerCheckbox.setEnabled(false);
                	handextremeCheckbox.setEnabled(false);
                }
                else{
                	camfeed.showGeoNodes = true;
                	palmCheckbox.setEnabled(true);
                	fingerCheckbox.setEnabled(true);
                	handextremeCheckbox.setEnabled(true);
                }
            }
        });
        showNodes.setSelected(true);

        palmCheckbox.setText("Palms");
        palmCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if(camfeed.showPalms){
                	camfeed.showPalms = false;
                }
                else{
                	camfeed.showPalms = true;
                }
            }
        });
        palmCheckbox.setSelected(true);

        handextremeCheckbox.setText("Hand Extremes");
        handextremeCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if(camfeed.showhandextremes){
                	camfeed.showhandextremes = false;
                }
                else{
                	camfeed.showhandextremes = true;
                }
            }
        });
        

        fingerCheckbox.setText("Fingertips");
        fingerCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if(camfeed.showFingers){
                	camfeed.showFingers = false;
                }
                else{
                	camfeed.showFingers = true;
                }
            }
        });
        fingerCheckbox.setSelected(true);

        lastCommandLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lastCommandLabel.setText("Last Command");

        lastcommandOutput.setHorizontalAlignment(JTextField.CENTER);
        lastcommandOutput.setText("Takeoff");

        forceLandButton.setText("Force Land");
        forceLandButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //forceLandButtonActionPerformed(evt);
            }
        });

        GroupLayout camFeedPanelLayout = new GroupLayout(camFeedPanel);
        camFeedPanel.setLayout(camFeedPanelLayout);
        camFeedPanelLayout.setHorizontalGroup(
            camFeedPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(viewLabel, GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
        );
        camFeedPanelLayout.setVerticalGroup(
            camFeedPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(viewLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        showDepth.setText("Depth Map");
        showDepth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if(camfeed.showDepth){
                	camfeed.showDepth = false;
                }
                else{
                	camfeed.showDepth = true;
                }
            }
        });
        showDepth.setSelected(true);

        lastCommandLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        lastCommandLabel1.setText("Hands Detected");

        leftHand.setBackground(new java.awt.Color(255, 255, 255));
        leftHand.setBorder(BorderFactory.createEtchedBorder());

        rightHand.setBackground(new java.awt.Color(255, 255, 255));
        rightHand.setBorder(BorderFactory.createEtchedBorder());

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(camFeedPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(handextremeCheckbox)
                            .addComponent(palmCheckbox)
                            .addComponent(fingerCheckbox))
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(lastCommandLabel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(jSeparator3, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lastCommandLabel, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(forceLandButton, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(leftHand, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(rightHand, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE))
                                    .addComponent(commandsLabel, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jSeparator4, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(showNodes)
                                    .addComponent(showRGB)
                                    .addComponent(showDepth)
                                    .addComponent(jSeparator2, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lastcommandOutput, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(commandsLabel, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(showRGB)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(showDepth)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, GroupLayout.PREFERRED_SIZE, 4, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(showNodes)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(palmCheckbox)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(handextremeCheckbox)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fingerCheckbox)
                .addGap(18, 18, 18)
                .addComponent(jSeparator3, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(lastCommandLabel1)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(rightHand)
                    .addComponent(leftHand))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                .addComponent(lastCommandLabel)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lastcommandOutput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator4, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(forceLandButton)
                .addGap(38, 38, 38))
            .addComponent(camFeedPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>                   
                                     
                                    

         
    public void setAction(String txt){
    	lastcommandOutput.setText(txt);
    }

           
	    
    private JPanel 				camFeedPanel;
    private JLabel 				commandsLabel;
    private JCheckBox 			handextremeCheckbox;
    private JCheckBox 			fingerCheckbox;
    private JButton 			forceLandButton;
    private JSeparator 			jSeparator1;
    private JSeparator 			jSeparator2;
    private JSeparator 			jSeparator3;
    private JSeparator 			jSeparator4;
    private JLabel 				lastCommandLabel;
    private JLabel 				lastCommandLabel1;
    private JTextField 			lastcommandOutput;
    public JLabel 				leftHand;
    private JCheckBox 			palmCheckbox;
    public JLabel 				rightHand;
    private JRadioButton 		showDepth;
    private JRadioButton		showNodes;
    private JRadioButton		showRGB;
    public JLabel 				viewLabel;

}
