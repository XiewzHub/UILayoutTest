package com.example.uilayouttest;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.R.id.input;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: 创建布局");

        final Button button = (Button) findViewById(R.id.button_1);
        Button selectDateBtn = (Button) findViewById(R.id.select_date);
        final EditText dateText = (EditText) findViewById(R.id.dateStringMonth);
        //        Button button2 = (Button)findViewById(R.id.button_2);
        EditText inputYear = (EditText) findViewById(R.id.dateStringYear);
        EditText inputMonth = (EditText) findViewById(R.id.dateStringMonth);
        EditText inputDay = (EditText) findViewById(R.id.dateStringDay);
        inputYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        inputMonth.setText(String.valueOf(calendar.get(Calendar.MONTH)+1));
        inputDay.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText inputYear = (EditText) findViewById(R.id.dateStringYear);
                EditText inputMonth = (EditText) findViewById(R.id.dateStringMonth);
                EditText inputDay = (EditText) findViewById(R.id.dateStringDay);
                EditText output2 = (EditText) findViewById(R.id.dateString2);
                String year = inputYear.getText().toString();
                String month = inputMonth.getText().toString();
                String day = inputDay.getText().toString();

                String date = year + "-" + month + "-" + day;
                if(isInteger(year)&&isInteger(month)&&isInteger(day)&&isDateString(date)){
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR,Integer.parseInt(year));
                    calendar.set(Calendar.MONTH,Integer.parseInt(month)-1);
                    calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(day));
                    date = sdf.format(calendar.getTime());
                    Log.d(TAG, date);
                    String result = getPassword(date);
                    output2.setText(result);
                }else {
                    output2.setText(date+"不是正确的日期");

                }
            }

            private boolean isInteger(String string) {
                try {

                    Integer integer = Integer.valueOf(string);
                    return true;
                }catch (Exception e){
                    return false;
                }
            }

            public String getPassword(String date) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    if (date == null || "".equals(date)) {
                        Date date1 = new Date();
                        date = sdf.format(date1);
                    }
                    sdf.setLenient(true);
                    Date inputDate = sdf.parse(date);

                    String[] inputData = date.split("-");
                    String month = inputData[1];
                    String day = inputData[2];
                    Character[] calData = new Character[]{month.charAt(0), month.charAt(1), day.charAt(0), day.charAt(1)};
                    // 计算结果核心方法
                    String result = calPassword(calData);


                    return result;

                } catch (Exception e) {
                    return "日期不存在，请确认正确日期格式 如2018-04-15";
                }
            }

            public String calPassword(Character[] calData) {
                String result = "";
                Integer[] caldataInt = new Integer[4];
                for (int i = 0; i < calData.length; i++) {
                    Integer num = Character.getNumericValue(calData[i]);
                    caldataInt[i] = num;
                }

                Integer temp = 0;
                String tempString = "";
                // 生成第一位密码
                for (int i = 0; i < caldataInt.length; i++) {
                    temp = temp + caldataInt[i];
                }
                tempString = temp.toString();
                Integer first = Character.getNumericValue(tempString.charAt(tempString.length() - 1));

                result = result + first;
                // 生成第二位密码
                temp = caldataInt[3] - caldataInt[2] - caldataInt[1] - caldataInt[0];
                tempString = temp.toString();
                Integer second = Character.getNumericValue(tempString.charAt(tempString.length() - 1));
                result = result + second;
                // 生成第三位密码
                temp = 1;
                for (int i = 0; i < caldataInt.length; i++) {
                    if (caldataInt[i] != 0) {
                        temp = temp * caldataInt[i];
                    }
                }
                tempString = temp.toString();
                Integer third = Character.getNumericValue(tempString.charAt(tempString.length() - 1));
                result = result + third;
                // 生成第四位密码
                tempString = first + second + third + "";
                Integer fourth = Character.getNumericValue(tempString.charAt(tempString.length() - 1));
                result = result + fourth;
                return result;
            }

        });

        selectDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showSelect();
            }
        });


    }


    /**
     * 选择日期
     */
    public void showSelect() {
        final EditText yearText = (EditText) findViewById(R.id.dateStringYear);
        final EditText monthText = (EditText) findViewById(R.id.dateStringMonth);
        final EditText dayText = (EditText) findViewById(R.id.dateStringDay);
        String year = yearText.getText().toString();
        String month = monthText.getText().toString();
        String day = dayText.getText().toString();
        String date = year+"-"+month+"-"+day;
        if (date != null && !date.equals("")) {
            if (isDateString(date)) {
                try {
                    Date parse = sdf.parse(date);
                    calendar.setTime(parse);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        showDatePickerDialog(this, 5, yearText,monthText,dayText, calendar);
    }

    public boolean isDateString(String dataString) {
        try {

            sdf.setLenient(false);
            Date inputDate = sdf.parse(dataString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

//    final EditText monthText = (EditText) findViewById(R.id.dateStringMonth);
//    final EditText dayText = (EditText) findViewById(R.id.dateStringDay);
    public static void showDatePickerDialog(Activity activity, int themeResId, final TextView tv,final EditText monthText,final EditText dayText, final Calendar calendar) {
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(activity
                , themeResId
                // 绑定监听器(How the parent is notified that the date is set.)
                , new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                //                monthOfYear = monthOfYear+1;
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, monthOfYear);
                calendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date time = calendar1.getTime();
                String res = sdf.format(time);
                // 此处得到选择的时间，可以进行你想要的操作
//                tv.setText(year + "-" + (monthOfYear)
//                        + "-" + dayOfMonth + "");
                tv.setText(String.valueOf(year));
                monthText.setText(String.valueOf(monthOfYear+1));
                dayText.setText(String.valueOf(dayOfMonth));

            }
        }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    /**
     * 时间选择
     *
     * @param activity
     * @param themeResId
     * @param tv
     * @param calendar
     */
    public static void showTimePickerDialog(Activity activity, int themeResId, final TextView tv, Calendar calendar) {
        // Calendar c = Calendar.getInstance();
        // 创建一个TimePickerDialog实例，并把它显示出来
        // 解释一哈，Activity是context的子类
        new TimePickerDialog(activity, themeResId,
                // 绑定监听器
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tv.setText("您选择了：" + hourOfDay + "时" + minute + "分");
                    }
                }
                // 设置初始时间
                , calendar.get(Calendar.HOUR_OF_DAY)
                , calendar.get(Calendar.MINUTE)
                // true表示采用24小时制
                , true).show();
    }
}
