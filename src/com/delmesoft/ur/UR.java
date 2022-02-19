package com.delmesoft.ur;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import com.delmesoft.ur.utils.JointPosition;
import com.delmesoft.ur.utils.Pose;
import com.delmesoft.ur.utils.Vec3;

/*
 * Copyright (c) 2021, Sergio S.- sergi.ss4@gmail.com http://sergiosoriano.com
 */

public interface UR {
	
	public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.#################", new DecimalFormatSymbols(Locale.US));
	
	void connect() throws Exception;
	
	boolean isConnected();
	
	void disconnect();
	
	void powerOn() throws Exception;
	
	void powerOff() throws Exception;
	
	void setFreedriveMode(boolean b) throws Exception;
	
	void stopJ(double a) throws Exception;
	
	void stopL(double a) throws Exception;
	
	void speedl(double[] toolSpeed, double a, double t) throws Exception;
	
	void speedj(double[] jointSpeeds, double a, double t) throws Exception;

	/**
	 * Set the Tool Center Point Sets the transformation from the output flange
	 * coordinate system to the TCP as a pose.
	 * 
	 * @param pose
	 * @throws Exception
	 */
	void setTcp(Pose pose) throws Exception; // set_tcp(p[-0.11324,0.0,0.11216,0.0,-0.757,0.0])

	/**
	 * Set payload mass and center of gravity This function must be called, when the
	 * payload weight or weight distribution changes - i.e when the robot picks up
	 * or puts down a heavy workpiece.
	 * 
	 * @param mass         in kilograms
	 * @param centerOfMass in meters
	 * @throws Exception
	 */
	void setPayload(double mass, Vec3 centerOfMass) throws Exception; // set_payload(0.5, (0,0,0))

	/**
	 * Set the direction of the acceleration experienced by the robot. When the
	 * robot mounting is fixed, this corresponds to an accleration of gaway from the
	 * earth�s centre
	 * 
	 * @param gravity
	 * @throws Exception
	 */
	void setGravity(Vec3 gravity) throws Exception; // set_gravity([0.0, 9.82, 0.0])

	/**
	 * Move Circular: Move to position (circular in tool-space)
	 * 
	 * TCP moves on the circular arc segment from current pose, throughpose_via to
	 * pose_to. Accelerates to and moves with constant tool speed v. Use the mode
	 * parameter to define the orientation interpolation.
	 * 
	 * @param via  path point (note: only position is used). Pose_via can also be
	 *             specified as joint positions, then forward kinematics is used to
	 *             calculate the corresponding pose.
	 * 
	 * @param to   target pose (note: only position is used in Fixed orientation
	 *             mode). Pose_to can also be specified as joint positions, then
	 *             forward kinematics is used to calculate the corresponding pose
	 * @param a    tool acceleration [m/s^2]
	 * @param v    tool speed [m/s]
	 * @param r    blend radius (of target pose) [m]
	 * @param mode 0: Unconstrained mode. Interpolate orientation from current pose
	 *             to target pose (pose_to)
	 *             1: Fixed mode. Keep orientation constant relative to the tangent of the circular arc(starting from
	 *             current pose)
	 */
	void movec(Pose via, Pose to, double a, double v, double r, int mode) throws Exception; // movec(p[x,y,z,0,0,0], pose_to, a=1.2,v=0.25, r=0.05, mode=1)

	/**
	 * Move to position (linear in joint-space)
	 * 
	 * When using this command, the robot must be at a standstill or come from a
	 * movej or movel with a blend. The speed and acceleration parameters control
	 * the trapezoid speed profile of the move. Alternatively, the t parameter can
	 * be used to set the time for this move. Time setting has priority over speed
	 * and acceleration settings
	 * 
	 * @param q : joint positions (q can also be specified as a pose, then inverse
	 *          kinematics is used to calculate the corresponding joint positions)
	 * @param a joint acceleration of leading axis [rad/s^2]
	 * @param v joint speed of leading axis [rad/s]
	 * @param t time [S]
	 * @param r blend radius [m] 
	 * 			
	 * 			If a blend radius is set, the robot arm trajectory
	 *          will be modified to avoid the robot stopping at the point. However,
	 *          if the blend region of this move overlaps with the blend radius of
	 *          previous or following way points, this move will be skipped, and an
	 *          ’Overlapping Blends’ warning message will be generated.
	 * 
	 * @throws Exception
	 */
	void movej(JointPosition q, double a, double v, double t, double r) throws Exception; // movej([0,1.57,-1.57,3.14,-1.57,1.57],a=1.4,v=1.05,t=0,r=0)
	
	void movej(Pose p, double a, double v, double t, double r) throws Exception; // movej(p[0,1.57,-1.57,3.14,-1.57,1.57],a=1.4,v=1.05,t=0,r=0)
	
	/**
	 * Move to position (linear in tool-space) See movej
	 * 
	 * @param p target pose (pose can also be specified as joint positions, then
	 *          forward kinematics is used to calculate the corresponding pose)
	 * @param a tool acceleration [m/s^2]
	 * @param v tool speed [m/s]
	 * @param t time [S]
	 * @param r blend radius [m]
	 * @throws Exception
	 */
	void movel(Pose p, double a, double v, double t, double r) throws Exception; // movel(p[0,1.57,-1.57,3.14,-1.57,1.57],a=1.2,v=0.25,t=0,r=0)

	void movel(JointPosition q, double a, double v, double t, double r) throws Exception; // movel([0,1.57,-1.57,3.14,-1.57,1.57],a=1.2,v=0.25,t=0,r=0)
	
	/**
	 * Move Process
	 * 
	 * Blend circular (in tool-space) and move linear (in tool-space) to position.
	 * Accelerates to and moves with constant tool speed v.
	 * 
	 * @param p target pose (pose can also be specified as joint positions, then
	 *          forward kinematics is used to calculate the corresponding pose)
	 * @param a tool acceleration [m/s^2]
	 * @param v tool speed [m/s]
	 * @param r blend radius [m]
	 * @throws Exception
	 */
	void movep(Pose p, double a, double v, double r) throws Exception; // movep(pose, a=1.2, v=0.25, r=0)
	
	void movep(JointPosition q, double a, double v, double r) throws Exception; // movep(q, a=1.2, v=0.25, r=0)
	
	/**
	 * Servo Circular 
	 * 
	 * Servo to position (circular in tool-space). Accelerates to and
	 * moves with constant tool speed v.
	 * 
	 * @param pose target pose (pose can also be specified as joint positions, then
	 *             forward kinematics is used to calculate the corresponding pose)
	 * @param a    tool acceleration [m/s^2]
	 * @param v    tool speed [m/s]
	 * @param r    blend radius (of target pose) [m]
	 * @throws Exception
	 */
	void servoc(Pose pose, double a, double v, double r) throws Exception; // servoc(p[0.2,0.3,0.5,0,0,3.14], a=1.2, v=0.25, r=0)
	
	void servoc(JointPosition q, double a, double v, double r) throws Exception; // servoc([0.2,0.3,0.5,0,0,3.14], a=1.2, v=0.25, r=0)
	
	/**
	 * Servoj can be used for online realtime control of joint positions.
	 * 
	 * The gain parameter works the same way as the P-term of a PID controller,
	 * where it adjusts the current position towards the desired (q). The higher the
	 * gain, the faster reaction the robot will have.
	 * 
	 * The parameter lookahead_time is used to project the current position forward
	 * in time with the current velocity. A low value gives fast reaction, a high
	 * value prevents overshoot.
	 * 
	 * @param q              joint angles in radians representing rotations of base,
	 *                       shoulder, elbow, wrist1, wrist2 and wrist3
	 * 
	 * @param a              not used in current version
	 * @param v              not used in current version
	 * @param t              time where the command is controlling the robot. The
	 *                       function is blocking for time t [S].
	 * @param lookahead_time time [S], range [0.03,0.2] smoothens the trajectory
	 *                       with this lookahead time
	 * 
	 * @param gain           proportional gain for following target position, range
	 *                       [100,2000]
	 * @throws Exception
	 */
	void servoj(JointPosition q, double a, double v, double t, double lookahead_time, double gain) throws Exception; // servoj([0.0,1.57,-1.57,0,0,3.14], 0, 0,0.008, 0.1, 300)
		
	void servoj(Pose p, double a, double v, double t, double lookahead_time, double gain) throws Exception; 
		
	Pose getPose();
	
	Pose getPose(Pose result);

	Pose getTcpOffset();

	JointData[] getJointData();
	
	JointPosition getJointPosition();

	JointPosition getJointPosition(JointPosition result);

	ToolData getToolData();
	
	RobotModeData getRobotModeData();

	boolean waitFor(Pose pose, double threshold, long timeout) throws Exception;
	boolean waitFor(Pose pose, double threshold) throws Exception;
	boolean waitFor(Pose pose) throws Exception;

	boolean waitFor(JointPosition jointPosition, double threshold, long timeout) throws Exception;
	boolean waitFor(JointPosition jointPosition, double threshold) throws Exception;
	boolean waitFor(JointPosition jointPosition) throws Exception;

}
