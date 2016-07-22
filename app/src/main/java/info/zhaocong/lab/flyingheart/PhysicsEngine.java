package info.zhaocong.lab.flyingheart;


/**
 * Simple Physics Engine to manipulate the movement
 *
 * @author zhaocong
 */
public class PhysicsEngine {

    private static final float WIND = -0.1f;

    /**
     * Apply the simple physics to calculate the position
     * @param deltaTime million seconds
     */
    static void move(SparkBase spark, long deltaTime){
        //calculate the change in velocity
        //assume velocity X does not change over time
        //apply the drag
        //spark.mVelocity.scale((1 - spark.drag * delaTimeF / 200000f) * spark.drag);
        float timeDiff = (float)deltaTime / 1000f;
        //x
        spark.mVelocity.x += WIND * timeDiff;
        //y
        spark.mVelocity.y += timeDiff * spark.gravity;
        spark.mPosition.offset(timeDiff * spark.mVelocity.x , timeDiff * spark.mVelocity.y);
    }

}
