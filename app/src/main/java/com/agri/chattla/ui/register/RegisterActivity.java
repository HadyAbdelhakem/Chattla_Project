package com.agri.chattla.ui.register;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.agri.chattla.R;
import com.agri.chattla.model.Farmar;
import com.agri.chattla.model.UserFirbase;
import com.agri.chattla.ui.login.LoginActivity;
import com.agri.chattla.utils.AppPreferences;
import com.apkfuns.xprogressdialog.XProgressDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Random;

import es.dmoral.toasty.Toasty;


public class RegisterActivity extends AppCompatActivity {

    private XProgressDialog dialog;
    private Spinner spActivity, spCountry, spCity;
    private EditText etUserName, etPassword;
    private String Username, Password, s , Date = "Empty", Gov, Cit;
    private TextView textView;
    static final int DILOG_ID = 0;
    int year, month, day;
    private UserFirbase user;
    private Farmar farmar;
    private FirebaseDatabase database;
    private DatabaseReference refUsers;

    private String degree, experience;
    private Spinner spEducation;
    private EditText etExperience;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_register);

        //FirebaseAuth.getInstance().signOut();

        etExperience = findViewById(R.id.et_experience);

        dialog = new XProgressDialog(this, /*AddConsultActivity.this.getResources().getString(R.string.loading_login)*/ "انتظر", XProgressDialog.THEME_HORIZONTAL_SPOT);

        user = new UserFirbase();
        farmar = new Farmar();
        database = FirebaseDatabase.getInstance();
        refUsers = database.getReference();

        Random r = new Random();
        char c = (char) (r.nextInt(26) + 'a');
        final int random = new Random().nextInt(999) + 101;
        final String phoneNumber = getIntent().getStringExtra("pn");
        final String U_Code = c + String.valueOf(random) + phoneNumber.substring(8);


//        final String[] n = getResources().getStringArray(R.array.nashat);
//
//        spActivity = findViewById(R.id.sp_activity);
//        ArrayAdapter<CharSequence> adapterActivity = ArrayAdapter.createFromResource(this, R.array.nashat, R.layout.layout_custom_spinner);
//        adapterActivity.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
//        spActivity.setAdapter(adapterActivity);
//        spActivity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                switch (position) {
//                    case 0:
//                        s = n[position];
//                        break;
//                    case 1:
//                        s = n[position];
//                        break;
//                    case 2:
//                        s = n[position];
//                        break;
//                    case 3:
//                        s = n[position];
//                        break;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        spCountry = findViewById(R.id.sp_country);
        spCity = findViewById(R.id.sp_city);

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.City, R.layout.layout_custom_spinner);
        final ArrayAdapter<CharSequence> adapter_0 = ArrayAdapter.createFromResource(this, R.array.A000, R.layout.layout_custom_spinner);
        final ArrayAdapter<CharSequence> adapter_1 = ArrayAdapter.createFromResource(this, R.array.Cairo_0, R.layout.layout_custom_spinner);
        final ArrayAdapter<CharSequence> adapter_2 = ArrayAdapter.createFromResource(this, R.array.Alex_1, R.layout.layout_custom_spinner);
        final ArrayAdapter<CharSequence> adapter_3 = ArrayAdapter.createFromResource(this, R.array.P_S_2, R.layout.layout_custom_spinner);
        final ArrayAdapter<CharSequence> adapter_4 = ArrayAdapter.createFromResource(this, R.array.S_C_3, R.layout.layout_custom_spinner);
        final ArrayAdapter<CharSequence> adapter_5 = ArrayAdapter.createFromResource(this, R.array.D_C_4, R.layout.layout_custom_spinner);
        final ArrayAdapter<CharSequence> adapter_6 = ArrayAdapter.createFromResource(this, R.array.DK_C_5, R.layout.layout_custom_spinner);
        final ArrayAdapter<CharSequence> adapter_7 = ArrayAdapter.createFromResource(this, R.array.SH_C_6, R.layout.layout_custom_spinner);
        final ArrayAdapter<CharSequence> adapter_8 = ArrayAdapter.createFromResource(this, R.array.Q_C_7, R.layout.layout_custom_spinner);
        final ArrayAdapter<CharSequence> adapter_9 = ArrayAdapter.createFromResource(this, R.array.K_SH_8, R.layout.layout_custom_spinner);
        final ArrayAdapter<CharSequence> adapter_10 = ArrayAdapter.createFromResource(this, R.array.G_C_9, R.layout.layout_custom_spinner);
        final ArrayAdapter<CharSequence> adapter_11 = ArrayAdapter.createFromResource(this, R.array.MN_C_10, R.layout.layout_custom_spinner);
        final ArrayAdapter<CharSequence> adapter_12 = ArrayAdapter.createFromResource(this, R.array.B7_C_11, R.layout.layout_custom_spinner);
        final ArrayAdapter<CharSequence> adapter_13 = ArrayAdapter.createFromResource(this, R.array.IS_C_12, R.layout.layout_custom_spinner);
        final ArrayAdapter<CharSequence> adapter_14 = ArrayAdapter.createFromResource(this, R.array.G_C_13, R.layout.layout_custom_spinner);
        final ArrayAdapter<CharSequence> adapter_15 = ArrayAdapter.createFromResource(this, R.array.BS_C_14, R.layout.layout_custom_spinner);
        final ArrayAdapter<CharSequence> adapter_16 = ArrayAdapter.createFromResource(this, R.array.F_C_15, R.layout.layout_custom_spinner);
        final ArrayAdapter<CharSequence> adapter_17 = ArrayAdapter.createFromResource(this, R.array.M_C_16, R.layout.layout_custom_spinner);
        final ArrayAdapter<CharSequence> adapter_18 = ArrayAdapter.createFromResource(this, R.array.AS_C_17, R.layout.layout_custom_spinner);
        final ArrayAdapter<CharSequence> adapter_19 = ArrayAdapter.createFromResource(this, R.array.SO_C_18, R.layout.layout_custom_spinner);
        final ArrayAdapter<CharSequence> adapter_20 = ArrayAdapter.createFromResource(this, R.array.QN_C_19, R.layout.layout_custom_spinner);
        final ArrayAdapter<CharSequence> adapter_21 = ArrayAdapter.createFromResource(this, R.array.AS_C_20, R.layout.layout_custom_spinner);
        final ArrayAdapter<CharSequence> adapter_22 = ArrayAdapter.createFromResource(this, R.array.Lu_C_21, R.layout.layout_custom_spinner);
        final ArrayAdapter<CharSequence> adapter_23 = ArrayAdapter.createFromResource(this, R.array.HG_C_22, R.layout.layout_custom_spinner);
        final ArrayAdapter<CharSequence> adapter_24 = ArrayAdapter.createFromResource(this, R.array.NW_C_23, R.layout.layout_custom_spinner);
        final ArrayAdapter<CharSequence> adapter_25 = ArrayAdapter.createFromResource(this, R.array.M_C_24, R.layout.layout_custom_spinner);
        final ArrayAdapter<CharSequence> adapter_26 = ArrayAdapter.createFromResource(this, R.array.NS_C_25, R.layout.layout_custom_spinner);
        final ArrayAdapter<CharSequence> adapter_27 = ArrayAdapter.createFromResource(this, R.array.SS_C_26, R.layout.layout_custom_spinner);

        spCountry.setAdapter(adapter);
        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Gov = (String) adapter.getItem(position);

                if (position == 0) {
                    spCity.setAdapter(adapter_0);
                    spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Cit = (String) adapter_0.getItem(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                if (position == 1) {
                    spCity.setAdapter(adapter_1);
                    spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Cit = (String) adapter_1.getItem(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                if (position == 2) {
                    spCity.setAdapter(adapter_2);
                    spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Cit = (String) adapter_2.getItem(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                if (position == 3) {
                    spCity.setAdapter(adapter_3);
                    spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Cit = (String) adapter_3.getItem(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                if (position == 4) {
                    spCity.setAdapter(adapter_4);
                    spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Cit = (String) adapter_4.getItem(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                if (position == 5) {
                    spCity.setAdapter(adapter_5);
                    spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Cit = (String) adapter_5.getItem(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                if (position == 6) {
                    spCity.setAdapter(adapter_6);
                    spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Cit = (String) adapter_6.getItem(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                if (position == 7) {
                    spCity.setAdapter(adapter_7);
                    spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Cit = (String) adapter_7.getItem(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                if (position == 8) {
                    spCity.setAdapter(adapter_8);
                    spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Cit = (String) adapter_8.getItem(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                if (position == 9) {
                    spCity.setAdapter(adapter_9);
                    spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Cit = (String) adapter_9.getItem(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                if (position == 10) {
                    spCity.setAdapter(adapter_10);
                    spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Cit = (String) adapter_10.getItem(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                if (position == 11) {
                    spCity.setAdapter(adapter_11);
                    spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Cit = (String) adapter_11.getItem(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                if (position == 12) {
                    spCity.setAdapter(adapter_12);
                    spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Cit = (String) adapter_12.getItem(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                if (position == 13) {
                    spCity.setAdapter(adapter_13);
                    spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Cit = (String) adapter_13.getItem(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                if (position == 14) {
                    spCity.setAdapter(adapter_14);
                    spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Cit = (String) adapter_14.getItem(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                if (position == 15) {
                    spCity.setAdapter(adapter_15);
                    spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Cit = (String) adapter_15.getItem(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                if (position == 16) {
                    spCity.setAdapter(adapter_16);
                    spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Cit = (String) adapter_16.getItem(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                if (position == 17) {
                    spCity.setAdapter(adapter_17);
                    spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Cit = (String) adapter_17.getItem(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                if (position == 18) {
                    spCity.setAdapter(adapter_18);
                    spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Cit = (String) adapter_18.getItem(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                if (position == 19) {
                    spCity.setAdapter(adapter_19);
                    spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Cit = (String) adapter_19.getItem(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                if (position == 20) {
                    spCity.setAdapter(adapter_20);
                    spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Cit = (String) adapter_20.getItem(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                if (position == 21) {
                    spCity.setAdapter(adapter_21);
                    spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Cit = (String) adapter_21.getItem(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                if (position == 22) {
                    spCity.setAdapter(adapter_22);
                    spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Cit = (String) adapter_22.getItem(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                if (position == 23) {
                    spCity.setAdapter(adapter_23);
                    spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Cit = (String) adapter_23.getItem(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                if (position == 24) {
                    spCity.setAdapter(adapter_24);
                    spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Cit = (String) adapter_24.getItem(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                if (position == 25) {
                    spCity.setAdapter(adapter_25);
                    spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Cit = (String) adapter_25.getItem(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                if (position == 26) {
                    spCity.setAdapter(adapter_26);
                    spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Cit = (String) adapter_26.getItem(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                if (position == 27) {
                    spCity.setAdapter(adapter_27);
                    spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Cit = (String) adapter_27.getItem(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findViewById(R.id.bt_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUserName = findViewById(R.id.et_userName);
                etPassword = findViewById(R.id.et_password);

                Username = etUserName.getText().toString();
                Password = etPassword.getText().toString();

                if (Username.isEmpty()) {
                    etUserName.setError("اسم المستخدم مطلوب");
                    etUserName.requestFocus();
                    return;
                }

                if (Username.length() < 6) {
                    etUserName.setError("اسم المستخدم قصير");
                    etUserName.requestFocus();
                    return;
                }

                if (Password.isEmpty()) {
                    etPassword.setError("من فضلك ادخل كلمة المرور");
                    etPassword.requestFocus();
                    return;
                }

                if (Password.length() < 6) {
                    etPassword.setError("كلمة المرور قصيرة");
                    etPassword.requestFocus();
                    return;
                }

//                if (year > 2005) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
//                    builder.setMessage("تاريخ الميلاد غير صحيح")
//                            .setPositiveButton("موافق", null);
//
//                    AlertDialog alertDialog = builder.create();
//                    alertDialog.show();
//                    return;
//                }

                if (Date == "Empty") {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("قم بتحديد تاريخ الميلاد")
                            .setPositiveButton("موافق", null);

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    return;
                }

                switch (Gov) {
                    case "- المحافظة -":
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setMessage("قم بتحديد محل الاقامة")
                                .setPositiveButton("موافق", null);

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        return;
                }

                switch (Cit) {
                    case "- الـمديـنة -":
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setMessage("قم بتحديد المدينة")
                                .setPositiveButton("موافق", null);

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        return;
                }

                switch (degree) {
                    case "- اخــــــــتر -":

                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setMessage("قم بتحديد الدرجة العلمية")
                                .setPositiveButton("موافق", null);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        return;
                }

                if (etExperience.getText().toString().isEmpty()) {
                    etExperience.setError("هذا الحقل مطلوب");
                    etExperience.requestFocus();
                    return;
                }

                dialog.show();
                user.setPhoneNumber(phoneNumber);
                user.setUserName(Username);
                user.setPassWord(Password);
                user.setAge(Date);
                user.setDegree(degree);
                user.setExperience(etExperience.getText().toString());
                user.setGov(Gov);
                user.setCit(Cit);
                user.setU_C(U_Code);
                user.setStatus("offline");
                user.setInfo("Farmer");
                user.setId(phoneNumber);
                refUsers.child("Farmers").child(phoneNumber).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                        AppPreferences.saveUserPhone(RegisterActivity.this, phoneNumber);
                        Toasty.success(RegisterActivity.this,"تم التسجيل بنجاح",Toasty.LENGTH_SHORT).show();
                        finish();
                        dialog.dismiss();
                        startActivity(i);
                    }
                });
            }
        });

        findViewById(R.id.imageview_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setMessage("هل انت متأكد من الغاء عملية التسجيل نهائيا؟")
                        .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).setNegativeButton("الغاء", null);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        final Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        showDilogOnButtonClick();


        final ArrayAdapter<CharSequence> adapterEducation = ArrayAdapter.createFromResource(this, R.array.dof, R.layout.layout_custom_spinner);
        spEducation = findViewById(R.id.sp_education);
        spEducation.setAdapter(adapterEducation);
        spEducation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                degree = (String) adapterEducation.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void showDilogOnButtonClick() {
        findViewById(R.id.img_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DILOG_ID);
            }
        });

        findViewById(R.id.tv_birthday).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DILOG_ID);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DILOG_ID)
            return new DatePickerDialog(this, dpickerListner, year, month, day);
        return null;
    }

    public DatePickerDialog.OnDateSetListener dpickerListner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            year = i;
            month = i1 + 1;
            day = i2;
            Date = day + "/" + month + "/" + year;
            textView = (TextView) findViewById(R.id.tv_display_birthDay);
            textView.setText(Date);

        }
    };


    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setMessage("هل انت متأكد من الغاء عملية التسجيل نهائيا؟")
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton("الغاء", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}
