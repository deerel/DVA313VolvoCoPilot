package se.cpacsystems.safeassist.test_volvo4;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.volvo.softproduct.sensorextensionlibrary.db_enum.art_data;
import com.volvo.softproduct.sensorextensionlibrary.db_enum.exc_data;
import com.volvo.softproduct.sensorextensionlibrary.db_enum.gnss_data;
import com.volvo.softproduct.sensorextensionlibrary.db_enum.wlo_data;
import com.volvo.softproduct.sensorextensionlibrary.db_value.flag_value;
import com.volvo.softproduct.sensorextensionlibrary.db_value.float_value;
import com.volvo.softproduct.sensorextensionlibrary.db_value.long_value;
import com.volvo.softproduct.sensorextensionlibrary.managers.gnss_manager;
import com.volvo.softproduct.sensorextensionlibrary.managers.machine_manager;

import se.cpacsystems.common.Position;
import se.cpacsystems.position.ProjPosition;
import se.cpacsystems.position.PositionManager;

public class MainActivity extends AppCompatActivity {
    TextView unit_type;
    private Handler handlerMachineData;
    private Handler handlerGNSSData;
    private gnss_manager _gdm;
    private machine_manager _mdm;

    long startTime;
    long endTime;
    long lastCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unit_type = (TextView)findViewById(R.id.unit_type_TW);
        try{

            handlerMachineData = new Handler();
            handlerGNSSData = new Handler();

            _mdm = new machine_manager(this);
            _gdm = new gnss_manager(this);


            startTime = System.currentTimeMillis();
            handlerMachineData = new Handler();
            _mdm = new machine_manager(this);
            if(_mdm.Connect() == true) {
                handlerMachineData.post(runnableMachineData);
                Toast.makeText(getApplicationContext(), "You are on a copilot", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){

            Toast.makeText(getApplicationContext(), "You are on a handheld", Toast.LENGTH_SHORT).show();
        }
    }

    private Runnable runnableMachineData = new Runnable() {

        @Override
        public void run()
        {
            handlerMachineData.postDelayed(runnableMachineData, 500);

            float_value dvaluelat = _gdm.getFloatSignal(gnss_data.latitude.getCode());
            float_value dvaluelong = _gdm.getFloatSignal(gnss_data.longitude.getCode());
            float_value dvaluealt = _gdm.getFloatSignal(gnss_data.altitude.getCode());
            unit_type.setText(String.format("Lat %.5f Long %.5f Altitude %.5f\n", dvaluelat.getValue(), dvaluelong.getValue(), dvaluealt.getValue()));
            updateTime();
        }
    };


    private void updateTime(){
        lastCheck = endTime;
        endTime = System.currentTimeMillis();
    }
  /*  private Runnable gnssRunnable = new Runnable() {

        @Override
        public void run() {
            try {
            /*PositionManager pos_obj = new PositionManager(this);
            Position unit_pos = pos_obj.getPosition();
            Toast.makeText(getApplicationContext(), "Your position is: "+unit_pos.latitude, Toast.LENGTH_LONG).show();*/
  /*
                while (_gdm.Connect()==true) {
                    float_value dvaluelat = _gdm.getFloatSignal(gnss_data.latitude.getCode());
                    float_value dvaluelong = _gdm.getFloatSignal(gnss_data.longitude.getCode());
                    float_value dvaluealt = _gdm.getFloatSignal(gnss_data.altitude.getCode());
                    unit_type.setText(String.format("Lat %.5f Long %.5f Altitude %.5f\n", dvaluelat.getValue(), dvaluelong.getValue(), dvaluealt.getValue()));
                }
                    // unit_type.setText("You are on a CoPilot");
            } catch(Exception e){
                unit_type.setText("You are on a hand-held device");
            }
        }

    };
  */

    // public void check_unit(View v){gnssRunnable();}

    public void quit(View v){
        this.finish();
    }

}
