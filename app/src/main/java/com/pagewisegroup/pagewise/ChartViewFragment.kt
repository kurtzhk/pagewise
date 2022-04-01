package com.pagewisegroup.pagewise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.*

/**
 * A [Fragment] displaying a chart of pages read over the last 30 days
 */
class ChartViewFragment : Fragment() {
    private var pagesChart: BarChart? = null
    private var student: Student? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        student = requireActivity().intent.getSerializableExtra("STUDENT") as Student
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chart_view, container, false)
        pagesChart = view.findViewById(R.id.chart1)

        val entries = ArrayList<BarEntry>()
        //gets last 30 days
        val hist = student?.getReadingHistory(30)
        for(i in hist!!.indices){
            entries.add(BarEntry(30f-i,hist[i].toFloat()))
        }
        //updates chart
        val set = BarDataSet(entries,"Daily Pages")
        val data = BarData(set)
        data.barWidth = 0.5f

        pagesChart?.data = data
        pagesChart?.invalidate()

        return view
    }
}