package com.sir.app.wisdom.view.holder;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.sir.app.wisdom.R;
import com.sir.app.wisdom.model.entity.StatisticsBean;
import com.sir.app.wisdom.model.entity.VehicleTypeBean;
import com.sir.app.wisdom.view.weight.MyMarkerView;
import com.sir.library.com.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import butterknife.BindView;

/**
 * Created by zhuyinan on 2020/5/14.
 */
public class MainVehicleHolder extends BaseViewHolder {


    @BindView(R.id.rg_vehicle_type)
    RadioGroup rgVehicleType;
    @BindView(R.id.line_chart)
    LineChart lineChart;
    List<StatisticsBean> statistics;
    private XAxis xAxis;                //X轴
    private YAxis leftYAxis;            //左侧Y轴
    private YAxis rightYAxis;           //右侧Y轴
    private Legend legend;              //图例

    @Override
    public void doBusiness() {
        initChart();
    }

    /**
     * 初始化图表
     */
    private void initChart() {
        /***图表设置***/
        //是否展示网格线
        lineChart.setDrawGridBackground(false);
        //是否显示边界
        lineChart.setDrawBorders(true);
        //是否可以拖动
        lineChart.setDragEnabled(true);
        //是否有触摸事件
        lineChart.setTouchEnabled(true);
        //设置XY轴动画效果
        lineChart.animateY(2500);
        lineChart.animateX(1500);
        //取消描述文本
        lineChart.getDescription().setEnabled(false);

        //记号
        MyMarkerView mv = new MyMarkerView(mContent, R.layout.custom_marker_view);
        mv.setChartView(lineChart); // For bounds control
        lineChart.setMarker(mv); // Set the marker to the chart


        /***XY轴的设置***/
        xAxis = lineChart.getXAxis();
        leftYAxis = lineChart.getAxisLeft();
        rightYAxis = lineChart.getAxisRight();
        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);

        //格式化数据
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                try {
                    if (statistics == null || statistics.size() == 0) {
                        return "0";
                    }
                    return statistics.get((int) value).getDate();
                } catch (Exception ex) {
                    return "0";
                }
            }
        });


        //保证Y轴从0开始，不然会上移一点
        leftYAxis.setAxisMinimum(0f);
        leftYAxis.setTextColor(Color.WHITE);
        rightYAxis.setAxisMinimum(0f);

        /***折线图例 标签 设置***/
        legend = lineChart.getLegend();
        //设置显示类型，LINE CIRCLE SQUARE EMPTY 等等 多种方式，查看LegendForm 即可
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(12f);
        //取消标签文本
        legend.setEnabled(false);

        //显示位置 左下方
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //是否绘制在图表里面
        legend.setDrawInside(false);

        //背景颜色
        //lineChart.setBackgroundColor(Color.WHITE);
        //是否显示边界
        lineChart.setDrawBorders(false);
        //是否展示网格线
        lineChart.setDrawGridBackground(false);
        xAxis.setGridColor(Color.WHITE);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.WHITE);
        rightYAxis.setDrawGridLines(false);
        leftYAxis.setDrawGridLines(true);
        rightYAxis.setEnabled(false);
    }

    /**
     * 展示曲线
     *
     * @param name 曲线名称
     */
    public void showLineChart(String name, List<StatisticsBean> statistics) {

        YAxis yAxis;
        {   // // Y-Axis Style // //
            yAxis = lineChart.getAxisLeft();

            // disable dual axis (only use LEFT axis)
            lineChart.getAxisRight().setEnabled(false);

            // horizontal grid lines
            //yAxis.enableGridDashedLine(10f, 10f, 0f);

            // axis range
            //yAxis.setAxisMaximum(100f);
            yAxis.setAxisMinimum(0f);
        }

        this.statistics = statistics;
        List<Entry> entries = new ArrayList<>();
        List<Integer> threshold = new ArrayList<>();
        for (int i = 0; i < statistics.size(); i++) {
            Entry entry = new Entry(i, statistics.get(i).getCount());
            entries.add(entry);
            threshold.add(statistics.get(i).getCount());
        }

        //右边轴显示的数字是最大数字加10
        try {
            yAxis.setAxisMaximum(Collections.max(threshold) + 10);
            //yAxis.enableGridDashedLine(10f, 10f, 0f);
        } catch (NoSuchElementException ex) {

        }


        LineDataSet lineDataSet;
        if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0) {
            lineDataSet = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            lineDataSet.setValues(entries);
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
            lineChart.animateY(1500);
        } else {
            // 每一个LineDataSet代表一条线
            lineDataSet = new LineDataSet(entries, name);
            initLineDataSet(lineDataSet, LineDataSet.Mode.LINEAR);
            LineData lineData = new LineData(lineDataSet);
            lineData.setDrawValues(false);
            lineChart.setData(lineData);
        }
    }

    /**
     * 曲线初始化设置 一个LineDataSet 代表一条曲线
     *
     * @param lineDataSet 线条
     * @param mode
     */
    private void initLineDataSet(LineDataSet lineDataSet, LineDataSet.Mode mode) {
        lineDataSet.setDrawCircles(false);
        lineDataSet.setFillDrawable(ContextCompat.getDrawable(mContent, R.drawable.shape_fade));
        lineDataSet.setValueTextColor(Color.WHITE);
        lineDataSet.setLineWidth(1f);
        lineDataSet.setCircleRadius(3f);
        //设置曲线值的圆点是实心还是空心
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setValueTextSize(10f);
        //设置折线图填充
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setFormSize(15.f);
        if (mode == null) {
            //设置曲线展示为圆滑曲线（如果不设置则默认折线）
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        } else {
            lineDataSet.setMode(mode);
        }
    }


    /**
     * 设置车辆类型数据,默认选中第一条
     *
     * @param list
     */
    public void setVehicleType(List<VehicleTypeBean> list) {
        for (VehicleTypeBean bean : list) {
            RadioButton radio = (RadioButton) LayoutInflater.from(mContent).inflate(R.layout.view_vehicle_type, null);
            radio.setText(bean.getCar_Type_Name());
            radio.setTag(bean.getCar_Type_ID());
            rgVehicleType.addView(radio);
        }

        RadioButton radioButton = (RadioButton) rgVehicleType.getChildAt(0);

        if (radioButton != null) {
            radioButton.setChecked(true);
        }
    }
}
