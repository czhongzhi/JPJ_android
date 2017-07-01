package com.runbo.jpj.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chat.entity.JpjUser;
import com.runbo.jpj.R;
import com.runbo.jpj.api.ApiErrorCode;
import com.runbo.jpj.api.PanLvApi;
import com.runbo.jpj.application.MyApplication;
import com.runbo.jpj.constants.Constants;
import com.runbo.jpj.preferences.LoginPrefereces;
import com.runbo.jpj.util.CipherUtil;
import com.runbo.jpj.util.ToastUtil;
import com.runbo.jpj.view.MyLoadingDialog;

/**
 * 修改密码
 */
public class SetPswActivity extends BaseActivity implements View.OnClickListener {
    private ImageView bar_back;
    private TextView bar_title, bar_right;
    private Button set_ok;

    private EditText set_oldpsw, set_newpsw1, set_newpsw2;

    private MyLoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_psw);

        initView();
    }

    private void initView() {
        bar_back = (ImageView) findViewById(R.id.bar_back);
        bar_right = (TextView) findViewById(R.id.bar_right);
        bar_title = (TextView) findViewById(R.id.bar_title);
        set_ok = (Button) findViewById(R.id.set_ok);
        bar_title.setText("密码");

        set_oldpsw = (EditText) findViewById(R.id.set_oldpsw);
        set_newpsw1 = (EditText) findViewById(R.id.set_newpsw1);
        set_newpsw2 = (EditText) findViewById(R.id.set_newpsw2);

        bar_right.setOnClickListener(this);
        bar_back.setOnClickListener(this);
        set_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bar_right:
            case R.id.bar_back:
                finish();
                break;
            case R.id.set_ok:
                updataUserinfo();
                break;
        }
    }

    private void updataUserinfo() {
        String oldpsw = set_oldpsw.getText().toString().trim();
        if (TextUtils.isEmpty(oldpsw)) {
            Toast.makeText(SetPswActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String newpsw1 = set_newpsw1.getText().toString().trim();
        if (TextUtils.isEmpty(newpsw1)) {
            Toast.makeText(SetPswActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String newpsw2 = set_newpsw2.getText().toString().trim();
        if (!newpsw1.equals(newpsw2)) {
            Toast.makeText(SetPswActivity.this, "两次新密码不一样", Toast.LENGTH_SHORT).show();
            return;
        }

        if (MyApplication.getInstance().checkNetWork()) {
            updata("password", CipherUtil.MD5Encode(oldpsw), CipherUtil.MD5Encode(newpsw1));
        } else {
            ToastUtil.show(this, "网络异常");
        }

    }

    private void updata(String type, String oldpsw, final String newpsw) {
        dialog = new MyLoadingDialog(this);
        dialog.show();

        String user_id = LoginPrefereces.getNumber(this);
        MyApplication.getInstance().getApi().updataUserinfoForJpj(user_id, type, oldpsw, newpsw, new PanLvApi.ClientAjaxCallback() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                cancelDialog();

                String data = ApiErrorCode.getData(t);
                if (data == null) {
                    ToastUtil.show(SetPswActivity.this, "修改失败");
                    return;
                }

                ToastUtil.show(SetPswActivity.this, "修改成功");

                //从sqlite数据库获取用户信息
                String userid = LoginPrefereces.getNumber(SetPswActivity.this);
                JpjUser user = MyApplication.getInstance().DbService.queryJpjUser(userid);
                user.setPassword(newpsw);
                MyApplication.getInstance().DbService.updataJpjUser(user);

                sendBroadcast(new Intent(Constants.BroadCastAction.BROADCAST_CHANGE_USERINFO));

                finish();
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.show(SetPswActivity.this, "修改失败");
                cancelDialog();
            }
        });
    }

    private void cancelDialog() {
        if (dialog != null) {
            dialog.cancel();
            dialog = null;
        }
    }
}
