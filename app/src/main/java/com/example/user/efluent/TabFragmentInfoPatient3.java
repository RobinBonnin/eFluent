package com.example.user.efluent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BubbleChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.EntryXComparator;

import org.w3c.dom.Text;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class TabFragmentInfoPatient3 extends Fragment {

    //private ArrayList<Patient> patient_list;
    ArrayList<Exercise> exerciseList;
    List<Entry> entries = new ArrayList<Entry>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment_info_patient_3, container, false);
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        final LottieAnimationView animationView = (LottieAnimationView) getActivity().findViewById(R.id.animation_view);
        animationView.setAnimation("gears.json");
        animationView.playAnimation();
        animationView.setSpeed(0.2f);

        final FabSpeedDial fabSpeedDial = (FabSpeedDial) getActivity().findViewById(R.id.fab_speed_dial);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                LineChart linear = getActivity().findViewById(R.id.linearChart);
                BubbleChart bubble = getActivity().findViewById(R.id.bubbleChart);
                BarChart bar = getActivity().findViewById(R.id.barChart);
                switch (menuItem.getItemId()) {
                    case R.id.bubblechart:
                        linear.setVisibility(View.INVISIBLE);
                        bar.setVisibility(View.INVISIBLE);
                        animationView.setVisibility(View.INVISIBLE);
                        bubble.setVisibility(View.VISIBLE);
                        setBubbleChart(exerciseList);
                        break;
                    case R.id.graphbar:
                        linear.setVisibility(View.INVISIBLE);
                        bubble.setVisibility(View.INVISIBLE);
                        animationView.setVisibility(View.INVISIBLE);
                        bar.setVisibility(View.VISIBLE);
                        setBarChart(exerciseList);
                        break;
                    case R.id.linechart:
                        bubble.setVisibility(View.INVISIBLE);
                        bar.setVisibility(View.INVISIBLE);
                        animationView.setVisibility(View.INVISIBLE);
                        linear.setVisibility(View.VISIBLE);
                        setLineChart(exerciseList);
                        break;
                }
                return false;
            }
        });

        /*String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2" };*/
        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.rowlayout, R.id.label,values);
        setListAdapter(adapter);*/

        /*View addButton = getActivity().findViewById(R.id.GoAddPatient);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Test", "GoAddPatient");
                Intent intent = new Intent(getView().getContext(), AddPatientActivity.class);
                startActivity(intent);
            }
        });*/

    }
    public void setChart(ArrayList<Exercise> exercise_list){
        exerciseList = exercise_list;
    }

    public void setBarChart (ArrayList<Exercise> exerciseList) {
        BarChart chart = (BarChart) getActivity().findViewById(R.id.barChart);
        List<List<Integer>> listeOfdatesandCount = new ArrayList<List<Integer>>();
        listeOfdatesandCount.add(new ArrayList<Integer>());
        listeOfdatesandCount.add(new ArrayList<Integer>());
        final ArrayList<String> datesString = new ArrayList<String >();
        Integer index;
        Integer value;
        List<BarEntry> entries = new ArrayList<>();
        Collections.sort(exerciseList, new Comparator<Exercise>() {
            @Override
            public int compare(Exercise exercise, Exercise t1) {
                if (exercise.done) {
                    return exercise.successDate.compareTo(t1.successDate);
                }
                else return -1;
            }
        });
        Log.d("TEST", "This is exercise list " + exerciseList.get(0));
        for (Exercise data : exerciseList) {
            // turn your data into Entry objects
            if(!(data.successDate == null)){
                Log.d("TEST", "je suis dans le if " + data.successDate );
                android.text.format.DateFormat df = new android.text.format.DateFormat();
                String s = df.format("yyyyMMdd",data.successDate).toString();
                Integer dateNb = Integer.parseInt(s);
                if(!listeOfdatesandCount.get(0).contains(dateNb)) {
                    Log.d("ADDING", "Adding " + dateNb);
                    listeOfdatesandCount.get(0).add(dateNb);
                    Log.d("TEST","testing " + listeOfdatesandCount.get(0).get(0));
                    listeOfdatesandCount.get(1).add(1);
                }
                else {
                    index = listeOfdatesandCount.get(0).indexOf(dateNb);
                    value = listeOfdatesandCount.get(1).get(index);

                    Log.d("TEST", "Inside the else " + listeOfdatesandCount.get(0).get(index) + " " + value);

                    listeOfdatesandCount.get(1).set(index,value+1);
                }
            }
        }

        int i=0;
        for(Integer data : listeOfdatesandCount.get(0)) {
            Log.d("TEST", "Number of dates : " + listeOfdatesandCount.get(1).get(0) + listeOfdatesandCount.get(1).get(1));
            datesString.add(data.toString());
            entries.add(new BarEntry((float)i,
                    (float)listeOfdatesandCount.get(1).get(i)));
            i++;
        }
        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return datesString.get((int) value);
            }
        };


        Log.d("TEST", "Nombre d'entries : " + entries.size() + " "  );
        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);


        BarDataSet set = new BarDataSet(entries, "BarDataSet");

        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        chart.setData(data);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.invalidate(); // refresh

    }
    public void setBubbleChart(ArrayList<Exercise> exerciseList){
        Integer i=0;
        BubbleChart chart = (BubbleChart) getActivity().findViewById(R.id.bubbleChart);
        ArrayList<String> listOfExercises = new ArrayList<String>();
        final List<List<String>> listeOfExercisesandNbTrials = new ArrayList<List<String>>();
        listeOfExercisesandNbTrials.add(new ArrayList<String>());
        listeOfExercisesandNbTrials.add(new ArrayList<String>());
        listeOfExercisesandNbTrials.add(new ArrayList<String>());
        List<BubbleEntry> entries = new ArrayList<>();
        float f2,f3;
        int index;
        String value;
        for (Exercise data : exerciseList) {
            index = -1;
            value = "-1";
            // turn your data into Entry objects
            if (!(data.word == null)) {
                if (!listeOfExercisesandNbTrials.get(0).contains(data.word)) {
                    listeOfExercisesandNbTrials.get(0).add(data.word);
                    listeOfExercisesandNbTrials.get(1).add("1");
                    listeOfExercisesandNbTrials.get(2).add(data.numberOftrials.toString());
                } else {
                    index = listeOfExercisesandNbTrials.get(0).indexOf(data.word);
                    value = listeOfExercisesandNbTrials.get(1).get(index);
                    i  = Integer.parseInt(value);
                    i++;
                    listeOfExercisesandNbTrials.get(1).get(index).equals(i.toString());
                }
            }
        }
        for(String data : listeOfExercisesandNbTrials.get(0)) {
            f2 = Float.parseFloat(listeOfExercisesandNbTrials.get(1).get(i));
            f3 = Float.parseFloat(listeOfExercisesandNbTrials.get(2).get(i));
            entries.add(new BubbleEntry(i, f2,f3 ));
            i++;
        }
        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return listeOfExercisesandNbTrials.get(0).get((int) value);
            }
        };



        BubbleDataSet set = new BubbleDataSet(entries, "BubbleDataSet");
        BubbleData data = new BubbleData(set);
        XAxis x = chart.getXAxis();
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setAxisMaximum(4.5f);
        x.setAxisMinimum(-1f);
        YAxis y = chart.getAxisLeft();
        x.setValueFormatter(formatter);
        y.setAxisMaximum(4.5f);
        y.setAxisMinimum(0f);
        data.setHighlightCircleWidth(0.9f); // set custom bar width
        chart.setData(data);
        chart.invalidate(); // refresh
    }

    public void setLineChart(ArrayList<Exercise> exerciseList) {
        Integer i = 0;
        LineChart chart = (LineChart) getActivity().findViewById(R.id.linearChart);
        ArrayList<String> listOfExercises = new ArrayList<String>();
        final List<List<String>> listeOfExercisesandNbTrials = new ArrayList<List<String>>();
        listeOfExercisesandNbTrials.add(new ArrayList<String>());
        listeOfExercisesandNbTrials.add(new ArrayList<String>());
        listeOfExercisesandNbTrials.add(new ArrayList<String>());
        List<Entry> entries = new ArrayList<>();

        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return listeOfExercisesandNbTrials.get(0).get((int) value);
            }
        };
        entries.add(new Entry(1f, 2f));
        entries.add(new Entry(2f, 4f));
        entries.add(new Entry(3f, 6f));
        entries.add(new Entry(4f, 8f));

        LineDataSet set = new LineDataSet(entries, "BubbleDataSet");
        LineData data = new LineData(set);
        XAxis x = chart.getXAxis();
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setAxisMaximum(4.5f);
        x.setAxisMinimum(-1f);
        YAxis y = chart.getAxisLeft();
        y.setAxisMaximum(4.5f);
        y.setAxisMinimum(0f);
        chart.setData(data);
        chart.invalidate(); // refresh
    }
}
