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
import com.runbo.jpj.util.TextUtil;
import com.runbo.jpj.util.ToastUtil;
import com.runbo.jpj.view.MyLoadingDialog;

/**
 * 修改用户名或手机号码
 */
public class SetUserActivity extends BaseActivity implements View.OnClickListener {
    private ImageView bar_back;
    private TextView bar_title,bar_right;
    private EditText set_content;
    private Button set_ok;

    private String titlebar;
    private String hint;
    private String type;  //类型

    private MyLoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_user);

        type = getIntent().getStringExtra("TITLE_BAR");
        if (type.equals("name")){
            titlebar = "用户名";
        }else if (type.equals("phone")){
            titlebar = "手机号";
        }
        hint = getIntent().getStringExtra("EditHint");
        initView();
    }

    private void initView() {
        bar_back = (ImageView) findViewById(R.id.bar_back);
        bar_right = (TextView) findViewById(R.id.bar_right);
        bar_title = (TextView) findViewById(R.id.bar_title);
        set_content = (EditText) findViewById(R.id.set_content);
        set_ok = (Button) findViewById(R.id.set_ok);

        bar_right.setOnClickListener(this);
        bar_back.setOnClickListener(this);
        set_ok.setOnClickListener(this);

        bar_title.setText(titlebar == null?"编辑":titlebar);
        if (TextUtils.isEmpty(hint))
            set_content.setHint("请输入" + titlebar);
        else
            set_content.setHint(hint);
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

    private void updataUserinfo(){
        String text = set_content.getText().toString().trim();
        if(TextUtil.isEmpty(text)){
            ToastUtil.show(this,"内容不能为空");
            return;
        }else{
            if(MyApplication.getInstance().checkNetWork()){
                String parm_type = null;
                if (type.equals("name")){
                    parm_type = "name";
                }else if (type.equals("phone")){
                    parm_type = "phone";
                }
                updata(parm_type,text);
            }else {
                ToastUtil.show(this,"网络异常");
            }
        }
    }

    private void updata(final String type, final String text){
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
                    ToastUtil.show(SetUserActivity.this,"修改失败");
                    return;
                }

                ToastUtil.show(SetUserActivity.this,"修改成功");

                //从sqlite数据库获取用户信息
                String userid = LoginPrefereces.getNumber(SetUserActivity.this);
                JpjUser user = MyApplication.getInstance().DbService.queryJpjUser(userid);
                if (type.equals("name")){
                    user.setUsername(text);
                }else if (type.equals("phone")){
                    user.setPhone(text);
                }
                MyApplication.getInstance().DbService.updataJpjUser(user);

                sendBroadcast(new Intent(Constants.BroadCastAction.BROADCAST_CHANGE_USERINFO));

                finish();
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.show(SetUserActivity.this,"修改失败");
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
