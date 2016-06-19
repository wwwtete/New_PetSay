package com.petsay.activity.personalcustom.pay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.vo.personalcustom.OrderDTO;

import roboguice.inject.InjectView;

public class PaySuccessActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.iv_icon)
    private ImageView mIvIcon;
    @InjectView(R.id.tv_tip)
    private TextView mTvTip;
    @InjectView(R.id.tv_money)
    private TextView mTvMoney;
    @InjectView(R.id.iv_details)
    private ImageView mIvDetails;


    private OrderDTO mDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);
        mDto = (OrderDTO) getIntent().getSerializableExtra("orderdto");
        initView();
    }

    @Override
    protected void initView() {
        super.initView();

        if(mDto != null) {
            mTvMoney.setText("支付金额: ￥" + mDto.getAmount());
            mIvDetails.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_details:
                mIvDetails.setEnabled(false);
                Intent inten = new Intent(this,OrderDetailsActivity.class);
                inten.putExtra("orderid",mDto.getId());
                startActivity(inten);
                this.finish();
                break;
        }
    }
}
