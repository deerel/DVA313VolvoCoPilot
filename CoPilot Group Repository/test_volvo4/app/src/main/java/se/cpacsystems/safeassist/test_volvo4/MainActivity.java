package se.cpacsystems.safeassist.test_volvo4;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import se.cpacsystems.common.Position;
import se.cpacsystems.position.ProjPosition;
import se.cpacsystems.position.PositionManager;
public class MainActivity extends AppCompatActivity {
    TextView unit_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unit_type = (TextView)findViewById(R.id.unit_type_TW);
    }

    public void check_unit(View v){
        try{
            PositionManager pos_obj = new PositionManager(this);
            Position unit_pos = pos_obj.getPosition();
            Toast.makeText(getApplicationContext(), "Your position is: "+unit_pos.toString(), Toast.LENGTH_LONG).show();
            unit_type.setText("You are on a CoPilot");
        }catch (Exception e){
            unit_type.setText("You are on a hand-held device");
        }
    }

    public void quit(View v){
        this.finish();
    }
}
