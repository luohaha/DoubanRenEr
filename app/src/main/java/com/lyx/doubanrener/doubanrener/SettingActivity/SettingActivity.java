package com.lyx.doubanrener.doubanrener.SettingActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lyx.doubanrener.doubanrener.DbModule.DatabaseClient;

import com.lyx.doubanrener.doubanrener.R;

/**
 * Created by root on 15-6-29.
 */
public class SettingActivity extends AppCompatActivity{
    private Toolbar toolbar;
    private TextView title2;


    private CheckBox mCheckBox1;
    private CheckBox mCheckBox2;
    private CheckBox mCheckBox3;
    private CheckBox mCheckBox4;
    private CheckBox mCheckBox5;

    private CheckBox mCheckBox6;
    private CheckBox mCheckBox7;
    private CheckBox mCheckBox8;
    private CheckBox mCheckBox9;
    private CheckBox mCheckBox10;

    private Button mButton;

    private int mCheckNum = 0;

    private String hashTag[] = new String[] {"", "", "", "", "", "", "", "", "", "", ""};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        toolbar = (Toolbar) findViewById(R.id.activity_setting_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            //设置左上角图标
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(0, R.anim.slide_bottom_out);
                }
            });
            toolbar.setTitle("设置");
        }
        setColor();
        initTextview();


        initCheckbox();
        mButton = (Button) findViewById(R.id.setting_checkbox_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheckNum < 3) {
                    Toast.makeText(SettingActivity.this, "必须选择3项", Toast.LENGTH_SHORT).show();

                } else {
                    String[] tmp = {"", "", ""};
                    int j = 0;
                    for (int i = 1; i <= 10; i++) {
                        if (!hashTag[i].equals("")) {
                            tmp[j++] = hashTag[i];
                        }
                    }

                    DatabaseClient databaseClient =new DatabaseClient(SettingActivity.this);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("one", tmp[0]);
                    contentValues.put("two", tmp[1]);
                    contentValues.put("three", tmp[2]);
                    databaseClient.updateData("tagspage", contentValues, null, null);

                    Toast.makeText(SettingActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    /**
     * 设置整个的主题颜色
     * */
    private void setColor() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
    }

    private void initTextview() {
        title2 = (TextView) findViewById(R.id.activity_setting_title2);
        title2.setText("首页设置");
    }

    private void initCheckbox() {
        mCheckBox1 = (CheckBox) findViewById(R.id.cb1);
        mCheckBox2 = (CheckBox) findViewById(R.id.cb2);
        mCheckBox3 = (CheckBox) findViewById(R.id.cb3);
        mCheckBox4 = (CheckBox) findViewById(R.id.cb4);

        mCheckBox5 = (CheckBox) findViewById(R.id.cb5);
        mCheckBox6 = (CheckBox) findViewById(R.id.cb6);
        mCheckBox7 = (CheckBox) findViewById(R.id.cb7);
        mCheckBox8 = (CheckBox) findViewById(R.id.cb8);

        mCheckBox9 = (CheckBox) findViewById(R.id.cb9);
        mCheckBox10 = (CheckBox) findViewById(R.id.cb10);

        mCheckBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (mCheckNum < 3) {
                        mCheckNum++;
                        hashTag[1] = "top250";
                    } else {
                        mCheckBox1.setChecked(false);
                        Toast.makeText(SettingActivity.this, "选择的栏目不能超过3个", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mCheckNum--;
                    hashTag[1] = "";
                }
            }
        });

        mCheckBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (mCheckNum < 3) {
                        mCheckNum++;
                        hashTag[2] = "热门";

                    } else {
                        mCheckBox2.setChecked(false);
                        Toast.makeText(SettingActivity.this, "选择的栏目不能超过3个", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mCheckNum--;
                    hashTag[2] = "";
                }
            }
        });

        mCheckBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (mCheckNum < 3) {
                        mCheckNum++;
                        hashTag[3] = "华语";

                    } else {
                        mCheckBox3.setChecked(false);
                        Toast.makeText(SettingActivity.this, "选择的栏目不能超过3个", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mCheckNum--;
                    hashTag[3] = "";
                }
            }
        });

        mCheckBox4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (mCheckNum < 3) {
                        mCheckNum++;
                        hashTag[4] = "欧美";

                    } else {
                        mCheckBox4.setChecked(false);
                        Toast.makeText(SettingActivity.this, "选择的栏目不能超过3个", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mCheckNum--;
                    hashTag[4] = "";
                }
            }
        });

        mCheckBox5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (mCheckNum < 3) {
                        mCheckNum++;
                        hashTag[5] = "动作";
                    } else {
                        mCheckBox5.setChecked(false);
                        Toast.makeText(SettingActivity.this, "选择的栏目不能超过3个", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mCheckNum--;
                    hashTag[5] = "";

                }
            }
        });

        mCheckBox6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (mCheckNum < 3) {
                        mCheckNum++;
                        hashTag[6] = "喜剧";

                    } else {
                        mCheckBox6.setChecked(false);
                        Toast.makeText(SettingActivity.this, "选择的栏目不能超过3个", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mCheckNum--;
                    hashTag[6] = "";
                }
            }
        });
        mCheckBox7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (mCheckNum < 3) {
                        mCheckNum++;
                        hashTag[7] = "爱情";

                    } else {
                        mCheckBox7.setChecked(false);
                        Toast.makeText(SettingActivity.this, "选择的栏目不能超过3个", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mCheckNum--;
                    hashTag[7] = "";

                }
            }
        });
        mCheckBox8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (mCheckNum < 3) {
                        mCheckNum++;
                        hashTag[8] = "科幻";

                    } else {
                        mCheckBox8.setChecked(false);
                        Toast.makeText(SettingActivity.this, "选择的栏目不能超过3个", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mCheckNum--;
                    hashTag[8] = "";

                }
            }
        });
        mCheckBox9.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (mCheckNum < 3) {
                        mCheckNum++;
                        hashTag[9] = "悬疑";


                    } else {
                        mCheckBox9.setChecked(false);
                        Toast.makeText(SettingActivity.this, "选择的栏目不能超过3个", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mCheckNum--;
                    hashTag[9] = "";

                }
            }
        });
        mCheckBox10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (mCheckNum < 3) {
                        mCheckNum++;
                        hashTag[10] = "恐怖";

                    } else {
                        mCheckBox10.setChecked(false);
                        Toast.makeText(SettingActivity.this, "选择的栏目不能超过3个", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mCheckNum--;
                    hashTag[10] = "";

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        overridePendingTransition(0, R.anim.slide_bottom_out);
    }
}
