package x.volvo.ipshow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity {

    Button exit_button, show_ip_button;
    TextView ip_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        exit_button = (Button)findViewById(R.id.exit_app);
        show_ip_button = (Button)findViewById(R.id.show_ip);
        ip_text = (TextView)findViewById(R.id.show_ip_TW);
    }

    public void show_ip(View v){
        String ip = getLocalIpAddress();
        //Toast.makeText(getApplicationContext(), ip, Toast.LENGTH_LONG).show();
        ip_text.setText(ip);
    }

    public void exit(View v){
        this.finish();
    }


    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
