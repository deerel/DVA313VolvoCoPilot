package se.cpacsystems.safeassist.test_volvo4;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import se.cpacsystems.common.Position;
import se.cpacsystems.position.ProjPosition;
import se.cpacsystems.position.PositionManager;
/*
import com.volvo.softproduct.sensorextensionlibrary.db_enum.art_data;
import com.volvo.softproduct.sensorextensionlibrary.db_enum.exc_data;
import com.volvo.softproduct.sensorextensionlibrary.db_enum.gnss_data;
import com.volvo.softproduct.sensorextensionlibrary.db_enum.wlo_data;
import com.volvo.softproduct.sensorextensionlibrary.db_value.float_value;
import com.volvo.softproduct.sensorextensionlibrary.managers.*;*/
public class MainActivity extends AppCompatActivity {
    TextView unit_type;
    //private gnss_manager _gdm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unit_type = (TextView)findViewById(R.id.unit_type_TW);
    }

    public void check_unit(View v){
        try{
           /* float_value dvaluelat = _gdm.getFloatSignal(gnss_data.latitude.getCode());
            float_value dvaluelong = _gdm.getFloatSignal(gnss_data.longitude.getCode());
            float_value dvaluealt = _gdm.getFloatSignal(gnss_data.altitude.getCode());*/
            //_gpsposition.setText(String.format("Lat %.5f Long %.5f Altitude %.5f\n",dvaluelat.getValue(),dvaluelong.getValue(),dvaluealt.getValue()));
            PositionManager pos_obj = new PositionManager(this);
            Position unit_pos = pos_obj.getPosition();
            //Toast.makeText(getApplicationContext(), "latitude is: "+dvaluelat.getValue()+ "AND longitude is: "+dvaluelong.getValue(), Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "longitutde is"+unit_pos.longitude, Toast.LENGTH_LONG).show();
            unit_type.setText("You are on a CoPilot");
        }catch (Exception e){
            unit_type.setText("You are on a hand-held device");
        }
    }

    public void quit(View v){
        this.finish();
    }
}
