package com.sir.app.wisdom.dialog;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.sir.app.wisdom.R;
import com.sir.library.base.BaseDialog;

/**
 * Created by zhuyinan on 2020/4/20.
 */
public class DatePickerDialog extends BaseDialog {

    DatePicker datePicker;

    public DatePickerDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public int bindLayout() {
        return R.layout.dialog_date_picker;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        datePicker = findViewById(R.id.dp_date);
    }

    @Override
    public void doBusiness() {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setDatePicker(DatePicker.OnDateChangedListener listener) {
        datePicker.setOnDateChangedListener(listener);
    }
}
