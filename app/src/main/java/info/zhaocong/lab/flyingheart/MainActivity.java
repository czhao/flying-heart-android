package info.zhaocong.lab.flyingheart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.animation_panel)
    HeartView animationPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.trigger)
    void fire(){
        animationPanel.add();
    }

    @Override
    protected void onPause() {
        super.onPause();
        animationPanel.stop();
    }
}
