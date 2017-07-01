package com.runbo.jpj.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.runbo.jpj.R;
import com.runbo.jpj.constants.Constants;
import com.runbo.jpj.util.LogUtil;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by czz on 2017/3/29.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI iwxapi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iwxapi = WXAPIFactory.createWXAPI(getApplicationContext(), Constants.AK_WXCHAT, false);
        iwxapi.registerApp(Constants.AK_WXCHAT);
        //注意：
        //第三方开发者如果使用透明界面来实现WXEntryActivity，需要判断handleIntent的返回值，
        // 如果返回值为false，则说明入参不合法未被SDK处理，应finish当前透明界面，
        // 避免外部通过传递非法参数的Intent导致停留在透明界面，引起用户的疑惑
        try {
            iwxapi.handleIntent(getIntent(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        iwxapi.handleIntent(intent, this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq baseReq) {
        Toast.makeText(this, "onReq", Toast.LENGTH_SHORT).show();
        LogUtil.e("WXEntryActivity -- onReq");
        finish();
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp baseResp) {
        String result = null;
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK://分享成功
                result = "分享成功";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL://分享取消
                result = "分享取消";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED://分享被拒绝
                result = "分享被拒绝";
                break;
            default://分享返回
                result = "分享返回";
                break;
        }
        LogUtil.e("WXEntryActivity -- onResp");
        Toast.makeText(this, result+"", Toast.LENGTH_SHORT).show();
        finish();
    }
}
