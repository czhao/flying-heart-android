package info.zhaocong.lab.flyingheart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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

    @OnClick(R.id.animation_panel)
    void panelClicked(){
        animationPanel.add();
    }

    @OnClick(R.id.trigger)
    void triggerClicked(){
        animationPanel.add();
    }

    @Override
    protected void onPause() {
        super.onPause();
        animationPanel.stop();
    }
}
