package info.zhaocong.lab.flyingheart;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/**
 * Heart flying animation
 *
 * @author zhaocong
 */
public class HeartView extends SurfaceView {

    long time;
    private boolean isShowOngoing = false;
    private List<SparkBase> sparks = new ArrayList<>();
    private ArrayList<SparkBase> recycleList = new ArrayList<>();
    private Random mRandom = new Random();
    private ConcurrentLinkedQueue<SparkBase> waitingList = new ConcurrentLinkedQueue<>();

    private Bitmap blue, pink, yellow;

    private Bitmap[] heartAssets = new Bitmap[3];
    SurfaceHolder mSurfaceHolder;

    private Paint paint = new Paint();

    public HeartView(Context context) {
        super(context);
        init(context);
    }

    public HeartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HeartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        Resources resources = context.getResources();

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);

        blue = BitmapFactory.decodeResource(resources, R.drawable.bubble_blue_icon);
        yellow = BitmapFactory.decodeResource(resources, R.drawable.bubble_yellow_icon);
        pink = BitmapFactory.decodeResource(resources, R.drawable.bubble_pink_icon);

        heartAssets[0] = blue;
        heartAssets[1] = yellow;
        heartAssets[2] = pink;


        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                play();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                stop();
            }
        });
    }

    protected void play(){
        time = System.currentTimeMillis();
        isShowOngoing = true;

        new Thread(){
            @Override
            public void run() {
                while (isShowOngoing) {
                    long newTime = System.currentTimeMillis();
                    long timeDelta = newTime - time;
                    Canvas canvas = getHolder().lockCanvas();
                    canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);
                    for (SparkBase s : sparks) {
                        if (s.isExploding()) {
                            recycleList.add(s);
                        } else {
                            PhysicsEngine.move(s, timeDelta);
                            s.draw(canvas, s.mPosition.x, s.mPosition.y ,0, true);
                        }
                    }
                    sparks.removeAll(recycleList);
                    recycleList.clear();
                    if (sparks.size() > 0) {
                        //do nothing
                    } else {
                        try {
                            //60fps if possible
                            Thread.sleep(16);
                        } catch (Exception e) {
                            //DO NOTHING
                        }
                    }
                    //randomFire();
                    time = newTime;
                    getHolder().unlockCanvasAndPost(canvas);
                    while (waitingList.size() > 0){
                        //remove the item
                        sparks.add(waitingList.poll());
                    }
                }
            }
        }.start();
    }

    protected void stop(){
        isShowOngoing = false;
    }

    public void add(){
        float x =  getMeasuredWidth() /2  + (mRandom.nextFloat() > .5f ? 1f : -1f) * mRandom.nextFloat() * getMeasuredWidth() /4;
        float y =  getMeasuredHeight();
        float z = 0;

        int lifeSpan = 3000 + mRandom.nextInt(2000); //ms

        Point3f pos = new Point3f(x, y, z);
        //the vertical speed cannot be faster than the frame rate
        Vector3f v = new Vector3f(30f, -y / (lifeSpan/1000), 20f);

        int choice = mRandom.nextInt(3);
        float scale = mRandom.nextFloat() * 0.4f + 0.3f;
        Heart h = new Heart(pos, v, heartAssets[choice], lifeSpan, scale);
        waitingList.add(h);
    }

    public  class Heart extends SparkBase {

        private Bitmap heart;
        private long mLifespan;
        private Rect latest;
        private int width, height;
        private int initWidth = 1, initHeight = 1;

        public Heart(Point3f p, Vector3f v, Bitmap choice, long lifeSpan, float scale) {
            super(p, v);
            gravity = 0;
            drag = 0f;
            heart = choice;
            mLifespan = lifeSpan;
            width = (int)(choice.getWidth() * scale);
            height = (int)(choice.getHeight() * scale);
            latest = new Rect(0,0,0,0);
        }

        @Override
        public void draw(Canvas canvas, float screenX, float screenY, float scale, boolean doEffects) {
            //compute the paint alpha
            int alpha = 255 - (int)((System.currentTimeMillis() - startTime) * 255 / mLifespan);
            paint.setAlpha(alpha);
            if (initWidth < width){
                latest.left = (int)screenX - initWidth /2;
                latest.right = latest.left + initWidth;
                initWidth += 5;
            }else {
                latest.left = (int)screenX - width /2;
                latest.right = latest.left + width;
            }

            if (initHeight < height){
                latest.top = (int)screenY + initHeight / 2;
                latest.bottom = latest.top + initHeight;
                initHeight += 5;
            }else {
                latest.top = (int)screenY  + height / 2;
                latest.bottom = latest.top + height;
            }
            canvas.drawBitmap(heart, null, latest, paint);
        }

        @Override
        public boolean isExploding() {
            return System.currentTimeMillis() - startTime >= mLifespan;
        }
    }

}
