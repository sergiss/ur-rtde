package com.delmesoft.ur;
public class ReadPoseTest {
	
	public static void main(String[] args) throws Exception {

		UR ur = new URImpl("192.168.0.14", 30002);
		ur.connect();
		
		Thread.sleep(1000);

		while(true) {
			System.out.println(ur.getPose());
			System.out.println(ur.getJointPosition());
			Thread.sleep(1000);
		}
		
	}

}
