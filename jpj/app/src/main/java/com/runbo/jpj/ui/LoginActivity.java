package com.runbo.jpj.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.chat.entity.JpjUser;
import com.runbo.jpj.R;
import com.runbo.jpj.api.ApiErrorCode;
import com.runbo.jpj.api.PanLvApi;
import com.runbo.jpj.application.MyApplication;
import com.runbo.jpj.constants.Constants;
import com.runbo.jpj.preferences.LoginPrefereces;
import com.runbo.jpj.util.CipherUtil;
import com.runbo.jpj.util.TextUtil;
import com.runbo.jpj.util.ToastUtil;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity implements LoaderCallbacks<Cursor>,OnClickListener {

    private static final int REQUEST_READ_CONTACTS = 0;

    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    private AutoCompleteTextView mNumberView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private CheckBox mSavePswView;

    private String number;
    private String password;
    private boolean savePswStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        LoginPrefereces.setIp(this,LoginPrefereces.getIp(this));

        cacheLogin();

        initView();

        initInput();
    }

    /**
     * 缓存登录
     */
    private void cacheLogin() {
        String number = LoginPrefereces.getNumber(mContext);
        String password = LoginPrefereces.getPasswd(mContext);
        boolean loginStatus = LoginPrefereces.getLoginStatus(mContext);
        if(loginStatus && !TextUtils.isEmpty(number) && !TextUtils.isEmpty(password)){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mNumberView = (AutoCompleteTextView) findViewById(R.id.id_number);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.id_icon_set).setOnClickListener(this);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mSavePswView = (CheckBox) findViewById(R.id.id_save_psw);
    }

    private void initInput() {
        //初始化记住密码
        String number = LoginPrefereces.getNumber(mContext);
        String password = LoginPrefereces.getPasswd(mContext);
        boolean savePswStatus = LoginPrefereces.getSavePswStatus(mContext);
        if(!savePswStatus){
            mPasswordView.setText("");
        }else {
            mPasswordView.setText(password);
        }
        mNumberView.setText(number);
        mSavePswView.setChecked(savePswStatus);
    }

    /**
     * 验证 并 登录
     */
    private void attemptLogin() {
        mNumberView.setError(null);
        mPasswordView.setError(null);

        number = mNumberView.getText().toString();
        password = mPasswordView.getText().toString();
        savePswStatus = mSavePswView.isChecked();

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(Constants.IP)){
            ToastUtil.show(this,"服务器还未设置，请点击右下角设置");
            cancel = true;
            return;
        }
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(number)) {
            mNumberView.setError(getString(R.string.error_field_required));
            focusView = mNumberView;
            cancel = true;
        }
        if(TextUtils.isEmpty(password)){
            mPasswordView.setError(getString(R.string.error_invalid_password_not));
            focusView = mPasswordView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            if(!MyApplication.getInstance().checkNetWork()){
                ToastUtil.show(this,"网络异常");
            }
            String psw = CipherUtil.MD5Encode(password);
            //登录
            MyApplication.getInstance().getApi().login(number, psw,new LoginClickBack());
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * 保存登录状态
     * @param number
     * @param password
     * @param savePswStatus
     */
    private void saveLocalLogin(String number,String password,boolean savePswStatus){
        MyApplication.getInstance().saveLocalLogin(number,password,savePswStatus);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_in_button://登录
                attemptLogin();
                break;
            case R.id.id_icon_set://设置服务器IP
                startActivity(new Intent(LoginActivity.this,SetServiceIPActivity.class));
                break;
        }
    }


    private class LoginClickBack extends PanLvApi.ClientAjaxCallback{
        @Override
        public void onStart() {
            super.onStart();
            showProgress(true);
        }

        @Override
        public void onSuccess(String t) {
            super.onSuccess(t);
            showProgress(false);
            String data = ApiErrorCode.getData(t);
            if(data == null){
                ToastUtil.show(LoginActivity.this,ApiErrorCode.getErrCodeDescribe(t));
                return;
            }
            //保存用户信息
            JpjUser user = JSON.parseObject(data, JpjUser.class);
            MyApplication.getInstance().DbService.updataJpjUser(user);
            //保存登录状态
            saveLocalLogin(number,password,savePswStatus);
            //跳转界面
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }

        @Override
        public void onFailure(Throwable t, int errorNo, String strMsg) {
            super.onFailure(t, errorNo, strMsg);
            showProgress(false);
        }


    }









    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mNumberView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mNumberView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


}

