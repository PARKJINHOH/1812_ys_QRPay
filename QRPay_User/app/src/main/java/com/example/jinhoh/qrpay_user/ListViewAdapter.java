package com.example.jinhoh.qrpay_user;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<pay_historyBean> mData;

    public ListViewAdapter(Context mContext, ArrayList<pay_historyBean> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.pay_history_xml, null);
        }

        TextView orderDate = (TextView) convertView.findViewById(R.id.orderDate);
        TextView orderCompany = (TextView) convertView.findViewById(R.id.orderCompany);
        TextView orderHistory = (TextView) convertView.findViewById(R.id.orderHistory);
        TextView orderMoney = (TextView) convertView.findViewById(R.id.orderMoney);

        orderDate.setText(mData.get(position).orderDate);
        orderCompany.setText(mData.get(position).orderCompany);
        orderHistory.setText(mData.get(position).orderHistory);
        orderMoney.setText(mData.get(position).orderMoney);

        return convertView;
    }
}
