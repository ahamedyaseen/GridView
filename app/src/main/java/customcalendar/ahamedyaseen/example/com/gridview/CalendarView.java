package customcalendar.ahamedyaseen.example.com.gridview;

/**
 * Created by Ahamed Yaseen on 14-08-2017.
 */

import android.content.Context;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.constraint.ConstraintLayout;


import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.GridView;


import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;


public class CalendarView extends ConstraintLayout
{
    // for logging
    private static final String LOGTAG = "Calendar View";

    // how many days to show, defaults to six weeks, 42 days
    private static final int DAYS_COUNT = 42;

    // default date format
    private static final String DATE_FORMAT = "MMM yyyy";

    private int currMonth;
    private int currYear;



    // current displayed month
    private Calendar currentDate = Calendar.getInstance();

    //event handling
    private EventHandler eventHandler = null;

    // internal components
    private ConstraintLayout header;
    //private ImageView btnPrev;
    //private ImageView btnNext;
    private TextView txtDate;
    private GridView grid;
    public Context context;

    private float x1,x2;
    static final int MIN_DISTANCE = 150;


    public CalendarView(Context context)
    {
        super(context);
    }

    public  CalendarView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initControl(context,attrs);

    }
    public  CalendarView(Context context, AttributeSet attrs,int defStyle)
    {
        super(context, attrs,defStyle);
        initControl(context,attrs);

    }





    /**
     * Load control xml layout
     */
    private void initControl(Context context,AttributeSet attrs)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.calendar_view3, this);

        assignUiElements();
        assignClickHandlers();

        updateCalendar();
    }


    private void assignUiElements()
    {
        // layout is inflated, assign local variables to components
        //header = findViewById(R.id.tvHeaderMonth);
        // btnPrev = (ImageView)findViewById(R.id.calendar_prev_button);
        // btnNext = (ImageView)findViewById(R.id.calendar_next_button);
        txtDate = (TextView)findViewById(R.id.tvHeaderMonth);
        grid = (GridView)findViewById(R.id.MonthGrid);
    }

    private void assignClickHandlers()
    {

        grid.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch(motionEvent.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        x1 = motionEvent.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = motionEvent.getX();
                        float deltaX = x2 - x1;

                        if (Math.abs(deltaX) > MIN_DISTANCE)
                        {
                            // Left to Right swipe action
                            if (x2 > x1)
                            {
                                currentDate.add(Calendar.MONTH, -1);
                                updateCalendar();
                            }

                            // Right to left swipe action
                            else
                            {
                                currentDate.add(Calendar.MONTH, 1);
                                updateCalendar();
                            }

                        }
                        else
                        {
                            // consider as something else - a screen tap for example
                        }
                        break;
                }
                return false;
            }
        });

    }

    /**
     * Display dates correctly in grid
     */
    public void updateCalendar()
    {
        updateCalendar(null);
    }

    /**
     * Display dates correctly in grid
     */
    public void updateCalendar(HashSet<Date> events)
    {
        ArrayList<Date> cells = new ArrayList<>();
        Calendar calendar = (Calendar)currentDate.clone();
        currMonth = currentDate.get(Calendar.MONTH);
        currYear=currentDate.get(Calendar.YEAR);

        Log.e(LOGTAG, "currMonth: "+currentDate.get(Calendar.MONTH) );
        Log.e(LOGTAG, "curryear: "+currentDate.get(Calendar.YEAR) );

        // determine the cell for current month's beginning
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        // move calendar backwards to the beginning of the week
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

        // fill cells
        while (cells.size() < DAYS_COUNT)
        {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);

        }

        // update grid
        grid.setAdapter(new CalendarAdapter(getContext(), cells, events));

        // update title
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        txtDate.setText(sdf.format(currentDate.getTime()));

        Log.e(LOGTAG,sdf.format(currentDate.getTime()));

        // set header color according to current season
        int month = currentDate.get(Calendar.MONTH);

        Log.e(LOGTAG,""+Calendar.MONTH);

        //header.setBackgroundColor(getResources().getColor(color));
    }


    private class CalendarAdapter extends ArrayAdapter<Date>
    {
        // days with events
        private HashSet<Date> eventDays;

        // for view inflation
        private LayoutInflater inflater;

        public CalendarAdapter(Context context, ArrayList<Date> days, HashSet<Date> eventDays)
        {
            super(context, R.layout.control_calendar_day, days);
            this.eventDays = eventDays;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent)
        {
            // day in question

            Calendar calendar = Calendar.getInstance();

            Log.e(LOGTAG,"Calendar pos"+ getItem(position));
            Date date = getItem(position);
            calendar.setTime(date);
            Log.e(LOGTAG,"date pos"+ date);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            // today
            Date today = new Date();

            // inflate item if it does not exist yet
            if (view == null)
                view = inflater.inflate(R.layout.control_calendar_day, parent, false);

            // if this day has an event, specify event image
            view.setBackgroundResource(0);
            if (eventDays != null)
            {
                for (Date eventDate : eventDays)
                {
                    if (eventDate.getDate() == day &&
                            eventDate.getMonth() == month &&
                            eventDate.getYear() == year)
                    {
                        // mark this day for event
                        //view.setBackgroundResource(R.drawable.reminder);
                        break;
                    }
                }
            }

            // clear styling
            ((TextView)view).setTypeface(null, Typeface.NORMAL);
            ((TextView)view).setTextColor(Color.BLACK);

            Log.e(LOGTAG,"year"+today.getYear());
            if (month != currentDate.get(Calendar.MONTH) || year != currentDate.get(Calendar.YEAR))
            {
                // if this day is outside current month, grey it out
                ((TextView)view).setTextColor(getResources().getColor(R.color.greyed_out));
            }
            else if (day == today.getDate())
            {
                // if it is today, set it to blue/bold
                ((TextView)view).setTypeface(null, Typeface.BOLD);
                ((TextView)view).setTextColor(getResources().getColor(R.color.today));
            }

            // set text
            ((TextView)view).setText(String.valueOf(date.getDate()));

            return view;
        }
    }

    /**
     * Assign event handler to be passed needed events
     */
    public void setEventHandler(EventHandler eventHandler)
    {
        this.eventHandler = eventHandler;
    }

    /**
     * This interface defines what events to be reported to
     * the outside world
     */
    public interface EventHandler
    {
        void onDayLongPress(Date date);
    }
}
