package volvo.AppSkeleton_VolvoSafeAssist_Package;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initial test for Unit (Can be removed)
        Unit u = new Unit(this.getApplicationContext());
        u.login("user", "pass");
    }
}
