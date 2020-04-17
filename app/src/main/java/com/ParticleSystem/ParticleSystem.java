package com.ParticleSystem;

import android.graphics.Point;
import com.ParticleEffectCamera.CameraViewActivity;
import java.util.ArrayDeque;
import org.opencv.core.Mat;

public class ParticleSystem {

    static int duration = 0;
    static ArrayDeque<Particle> ptcspool = new ArrayDeque<>();

    public static void Configuration(int Initial_size,int Duration,int groups,int size,int velocity,
                                     boolean Trajectory,int TrajectoryLength,int[] TrajectoryColor,
                                     boolean Halo,int[] HaloColor,
                                     int[] config,int[] configGroup){
        duration = Duration;
        ptcspool.clear();
        for (int i = 0; i < Initial_size; i++) {
            ptcspool.add(new Particle());
        }
        for (int z = 0; z < ptcspool.size(); z++) {
            Particle ptc = ptcspool.removeFirst();
            //Particles' number configuration
            ptc.pNo = z;
            ptc.groupNo = z % (Initial_size / groups);
            ptc.group = new Double(Math.floor(z /(Initial_size / groups))).intValue();
            ptc.groupValue = Initial_size / groups;
            //Particles' shape configuration
            ptc.x = 0;
            ptc.y = 0;
            ptc.size = size;
            ptc.col[0] = 255;
            ptc.col[1] = 0;
            ptc.col[2] = 0;
            ptc.v = velocity;
            ptc.direction[0] = 0;
            ptc.direction[1] = 0;
            //Particles' life configuration
            ptc.life = false;
            ptc.lifetime = 0;
            ptc.duration = Duration;
            //Particle effect configuration
            ptc.config = config;
            ptc.configGroup = configGroup;//Number of group configuration should be less than 10
            ptc.trajectory = Trajectory;
            ptc.trajectoryColor = TrajectoryColor;
            ptc.trajectoryLength = TrajectoryLength;//Length should be less than 10
            ptc.halo = Halo;
            ptc.haloColor = HaloColor;
            //Save the initial state of the particle
            ptc.stateOfShape[0] = ptc.size;
            ptc.stateOfShape[1] = ptc.v;
            ptc.stateOfColor[0] = ptc.col[0];
            ptc.stateOfColor[1] = ptc.col[1];
            ptc.stateOfColor[2] = ptc.col[2];
            ptcspool.addLast(ptc);
        }
    }

    //加载粒子系统运动
    public static void runSystem(Mat frame, Point[] landmark, int t){
        int[][] key_position = new int[2][2];
        switch(CameraViewActivity.config[3]){
            case 0://eye
                key_position[0][0] = landmark[0].x;
                key_position[0][1] = landmark[0].y;
                key_position[1][0] = landmark[1].x;
                key_position[1][1] = landmark[1].y;
                break;
            case 1://nose
                key_position[0][0] = landmark[2].x;
                key_position[0][1] = landmark[2].y;
                break;
            case 2://mouth
                key_position[0][0] = (landmark[3].x + landmark[4].x)/2;
                key_position[0][1] = (landmark[3].y + landmark[4].y)/2;
                break;
        }
        int time = t % duration;//周期时间内
        for (int i = 0; i < ptcspool.size(); i++) {
            Particle ptc = ptcspool.removeFirst();
            ptc.update(time);
            ptc.Render(frame,key_position,CameraViewActivity.config[3]);
            ptcspool.addLast(ptc);
        }
    }
}
