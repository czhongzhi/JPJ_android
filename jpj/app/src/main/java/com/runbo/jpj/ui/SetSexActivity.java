package com.runbo.jpj.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chat.entity.JpjUser;
import com.runbo.jpj.R;
import com.runbo.jpj.api.ApiErrorCode;
import com.runbo.jpj.api.PanLvApi;
import com.runbo.jpj.application.MyApplication;
import com.runbo.jpj.constants.Constants;
import com.runbo.jpj.preferences.LoginPrefereces;
import com.runbo.jpj.util.TextUtil;
import com.runbo.jpj.util.ToastUtil;
import com.runbo.jpj.view.MyLoadingDialog;

/**
 * 修改性别
 */
public class SetSexActivity extends BaseActivity implements View.OnClickListener,RadioGroup.OnCheckedChangeListener {
    private ImageView bar_back;
    private TextView bar_title,bar_right;
    private Button set_ok;
    private RadioGroup set_group_sex;
    private RadioButton set_man;
    private RadioButton set_woman;

    private String hint;

    private MyLoadingDialog dialog;
    private String currSex = "";
    private String updataSex ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_sex);

        hint = getIntent().getStringExtra("EditHint");
        currSex = updataSex = hint;

        initView();
    }

    private void initView() {
        bar_back = (ImageView) findViewById(R.id.bar_back);
        bar_right = (TextView) findViewById(R.id.bar_right);
        bar_title = (TextView) findViewById(R.id.bar_title);
        set_ok = (Button) findViewById(R.id.set_ok);
        bar_title.setText("性别");
        set_group_sex = (RadioGroup) findViewById(R.id.set_group_sex);
        set_man = (RadioButton) findViewById(R.id.set_man);
        set_woman = (RadioButton) findViewById(R.id.set_woman);

        bar_right.setOnClickListener(this);
        bar_back.setOnClickListener(this);
        set_ok.setOnClickListener(this);

        if (hint.equals("男")){
            set_group_sex.check(R.id.set_man);
        }else if (hint.equals("女")){
            set_group_sex.check(R.id.set_woman);
        }
        set_group_sex.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bar_right:
            case R.id.bar_back:
                finish();
                break;
            case R.id.set_ok:
                updataUserinfo();
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.set_man:
                updataSex = "男";
                break;
            case R.id.set_woman:
                updataSex = "女";
                break;
        }
    }

    private void updataUserinfo(){
        if(currSex.equals(updataSex)){
            ToastUtil.show(this,"内容没有变化，请修改您的性别");
            return;
        }else{
            if(MyApplication.getInstance().checkNetWork()){
                updata("sex",updataSex);
            }else {
                ToastUtil.show(this,"网络异常");
            }
        }
    }

    private void updata(String type, final String text){
        dialog = new MyLoadingDialog(this);
        dialog.show();

        String user_id = LoginPrefereces.getNumber(this);
        MyApplication.getInstance().getApi().updataUserinfoForJpj(user_id,type,text,new PanLvApi.ClientAjaxCallback(){
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                cancelDialog();

                String data = ApiErrorCode.getData(t);
                if(data == null){
                    ToastUtil.show(SetSexActivity.this,"修改失败");
                    return;
                }

                ToastUtil.show(SetSexActivity.this,"修改成功");

                //从sqlite数据库获取用户信息
                String userid = LoginPrefereces.getNumber(SetSexActivity.this);
                JpjUser user = MyApplication.getInstance().DbService.queryJpjUser(userid);
                user.setSex(text);
                MyApplication.getInstance().DbService.updataJpjUser(user);

                sendBroadcast(new Intent(Constants.BroadCastAction.BROADCAST_CHANGE_USERINFO));

                finish();
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.show(SetSexActivity.this,"修改失败");
                cancelDialog();
            }
        });
    }

    private void cancelDialog(){
        if (dialog != null){
            dialog.cancel();
            dialog = null;
        }
    }
}
