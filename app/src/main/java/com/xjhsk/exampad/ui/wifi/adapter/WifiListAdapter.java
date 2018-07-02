package com.xjhsk.exampad.ui.wifi.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.bumptech.glide.Glide;
import com.kongqw.wifilibrary.WiFiManager;
import com.weidingqiang.rxfiflibrary2.utils.LogUtil;
import com.xjhsk.exampad.R;
import com.xjhsk.exampad.base.RxBus;
import com.xjhsk.exampad.model.bean.WifiVO;
import com.xjhsk.exampad.model.event.WifiViewEvent;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.utils.AutoUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：weidingqiang on 2018/1/13 15:34
 * 邮箱：dqwei@iflytek.com
 */

public class WifiListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = WifiListAdapter.class.getSimpleName();

    public List<WifiVO> getScanResults() {
        return scanResults;
    }

    private List<WifiVO> scanResults;

    private Context mContext;

    //连接的wifi 才有
    private String bssid;

    public WifiListAdapter(Context context) {
        mContext = context.getApplicationContext();
        this.scanResults = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WifiListAdapter.WifiViewHolder(LayoutInflater.from(mContext).inflate(R.layout.wifi_list_item, parent, false));

    }

    public void refreshData(List<WifiVO> scanResults) {
        LogUtil.debug(TAG,"刷新列表数据");
        if (null != scanResults) {
            Log.i(TAG, "refreshData 1 : " + scanResults.size());
            // 去重
//            scanResults = WiFiManager.excludeRepetition(scanResults);
            Log.i(TAG, "refreshData 2 : " + scanResults.size());
            // 清空数据
            this.scanResults.clear();
            // 更新数据
            this.scanResults.addAll(scanResults);

            if(NetworkUtils.isConnected()){
                bssid = WiFiManager.getInstance(mContext).getConnectionInfo().getBSSID();
            }
            else {
                bssid = "";
            }
            LogUtil.debug(TAG,"如果bssid有值 则wifi已连接。  bssid值为 " + bssid);


        }
        notifyDataSetChanged();

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final WifiVO wifiVO = scanResults.get(position);

        final ScanResult scanResult = wifiVO.getScanResult();

        WifiViewHolder viewHolder = (WifiViewHolder) holder;

//        viewHolder.ssid_tv.setText("热点名称：" + scanResult.SSID + "\n信号强度：" + WifiManager.calculateSignalLevel(scanResult.level, 5) + "/5\n加密方式：" + scanResult.capabilities);



        if(EmptyUtils.isNotEmpty(bssid) && bssid.equals(scanResult.BSSID)){
            viewHolder.wifi_connect_img.setVisibility(View.VISIBLE);
        }else {
            viewHolder.wifi_connect_img.setVisibility(View.INVISIBLE);
        }

        viewHolder.ssid_tv.setText(scanResult.SSID);

        switch (WifiManager.calculateSignalLevel(scanResult.level, 5)){
            case 0:
            case 1:
                Glide.with(mContext)
                        .load(R.drawable.icon_wifi_1)
                        .into(viewHolder.wifi_state_img);
                break;
            case 2:
                Glide.with(mContext)
                        .load(R.drawable.icon_wifi_2)
                        .into(viewHolder.wifi_state_img);
                break;
            case 3:
                Glide.with(mContext)
                        .load(R.drawable.icon_wifi_3)
                        .into(viewHolder.wifi_state_img);
                break;
            case 4:
                Glide.with(mContext)
                        .load(R.drawable.icon_wifi_4)
                        .into(viewHolder.wifi_state_img);
                break;
        }

        ((WifiViewHolder) holder).root_layout.setSelected(wifiVO.getSelect());

        ((WifiViewHolder) holder).root_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtil.debug(TAG,"点击wifi列表中的item。 scanResult.BSSID为 "+scanResult.BSSID );
                RxBus.getDefault().post(new WifiViewEvent(WifiViewEvent.SELECT_ITEM,scanResult.BSSID));
            }
        });



    }

    @Override
    public int getItemCount() {
        return scanResults.size();
    }

    class WifiViewHolder extends RecyclerView.ViewHolder{

        public TextView ssid_tv;

        public AutoLinearLayout root_layout;

        public ImageView wifi_connect_img;

        public ImageView wifi_state_img;

        public WifiViewHolder(View itemView){
            super(itemView);

            AutoUtils.autoSize(itemView);

            ssid_tv = (TextView) itemView.findViewById(R.id.ssid_tv);

            root_layout = (AutoLinearLayout) itemView.findViewById(R.id.root_layout);

            wifi_connect_img = (ImageView) itemView.findViewById(R.id.wifi_connect_img);

            wifi_state_img = (ImageView) itemView.findViewById(R.id.wifi_state_img);
        }
    }
}
