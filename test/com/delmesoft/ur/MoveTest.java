package com.delmesoft.ur;

import com.delmesoft.ur.utils.JointPosition;
import com.delmesoft.ur.utils.Pose;
import com.delmesoft.ur.utils.Vec3;

public class MoveTest {

	public static void main(String[] args) throws Exception {
		
		UR ur = new URImpl("192.168.0.14");
		ur.connect();
		
		ur.powerOn();
		
		Thread.sleep(1000);
		
		if(!ur.getRobotModeData().isReady()) {
			System.out.println("The robot is not ready");
			return;
		};
	
		ur.setTcp(new Pose());
		ur.setPayload(0.5F, Vec3.ZERO);

		float v = 1.5F;
		float a = 0.2F;
		
		JointPosition jointPosition = ur.getJointPosition();
		jointPosition.set(0.0,-1.4131490100450517,-1.817601333333272,-1.5075429674485905,1.5712359951106052,0.0);
		ur.movej(jointPosition, a, v, 0, 0);
		if(!ur.waitFor(jointPosition, 0.01)) {
			System.out.println("timeout0");
			System.exit(-1);
		}

		Pose pose = ur.getPose();
		
		for(int i = 0; i < 2; ++i) {
			
			pose.set(0.5898799117586578,-0.17409875672401107,1.0328387050834804,2.2086072133110712,-2.2086197908512806,-0.02909345590128312);
			ur.movel(pose, a, v, 0, 0);
			if(!ur.waitFor(pose, 0.01)) {
				System.out.println("timeout1");
				System.exit(-1);
			}
					
			pose.set(0.5898799118246233,-0.17409875672401068,0.6152963718331419,2.208607213311071,-2.2086197908512815,-0.02909345590127514);
			ur.movel(pose, a, v, 0, 0);
			if(!ur.waitFor(pose, 0.01, 2_000)) {
				System.out.println("timeout2");
				System.exit(-1);
			}
			
		}	
		
		Thread.sleep(1000);
		
		ur.powerOff();		
		ur.disconnect();
		
	}

}
