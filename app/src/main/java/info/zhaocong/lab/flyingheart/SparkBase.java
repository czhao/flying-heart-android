package info.zhaocong.lab.flyingheart;

import android.graphics.Canvas;
import android.graphics.PointF;

/**
 * PLEASE FILL IN THE CLASS DESCRIPTION
 *
 * @author zhaocong
 */
public abstract class SparkBase {

    protected final long startTime;

    //value manipulated by the physics engine
    protected PointF mPosition;
    protected PointF mVelocity;
    protected float gravity; //customized gravity
    protected float drag; //drag coefficient posed by the air

    public SparkBase(PointF p, PointF v) {
        startTime = System.currentTimeMillis();
        //copy the data
        mPosition = new PointF(p.x, p.y);
        mVelocity = new PointF(v.x, v.y);
    }

    public abstract void draw(Canvas canvas, float screenX, float screenY, float scale, boolean doEffects);

    public abstract boolean isExploding();
}
