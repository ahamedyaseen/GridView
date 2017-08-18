package customcalendar.ahamedyaseen.example.com.gridview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ExpandedMenuView;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

public class CalendarMainActivity extends AppCompatActivity {

    private static final String TAG = "CalendarMainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_main);

        HashSet<Date> events = new HashSet<>();
        //events.add(new Date());

        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = sdf.parse("29/08/2017");
            events.add(date);

        }
        catch (Exception e){

            Log.e(TAG,"exception"+e);
        }

        CalendarView cv = ((CalendarView)findViewById(R.id.calendar_view1));
        cv.updateCalendar(events);
    }
}
