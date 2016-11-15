package com.knowledgesecond.app.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.knowledgesecond.app.R;
import com.knowledgesecond.app.activity.MainActivity;
import com.knowledgesecond.app.model.NewaListItem;
import com.knowledgesecond.app.util.Constant;
import com.knowledgesecond.app.util.HttpUtils;
import com.knowledgesecond.app.util.PreUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zsg95 on 2016/11/14.
 */

public class MenuFragment extends BaseFragment implements OnClickListener {

    private ListView lvItem;
    private TextView tvDownload,tvMain,tvBackUp,tvLogin;
    private LinearLayout llMenu;
    private List<NewaListItem> items;
    private Handler handler=new Handler();
    private boolean isLight;
    private NewsTypeAdapter mAdapter;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.menu_layout,container,false);
        llMenu=(LinearLayout) view.findViewById(R.id.ll_menu);
        tvLogin=(TextView) view.findViewById(R.id.tv_login);
        tvBackUp=(TextView) view.findViewById(R.id.tv_backup);
        tvMain=(TextView) view.findViewById(R.id.tv_main);
        tvMain.setOnClickListener(this);
        tvDownload=(TextView) view.findViewById(R.id.tv_download);
        tvDownload.setOnClickListener(this);
        lvItem=(ListView) view.findViewById(R.id.lv_item);
        lvItem.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent,View view,int position,long id){
                getFragmentManager()
                        .beginTransaction().setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
                        .replace(R.id.fl_content, new NewsFragment(items.get(position).getId(), items.get(position).getTitle()), "news").commit();
                ((MainActivity) mActivity).setCurId(items.get(position).getId());
                ((MainActivity) mActivity).closeMenu();
            }
        });
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        isLight = ((MainActivity) mActivity).isLight();
        items = new ArrayList<NewaListItem>();
        if (HttpUtils.isNetworkConnected(mActivity)) {
            HttpUtils.get(Constant.THEMES, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    String json = response.toString();
                    PreUtils.putStringToDefault(mActivity, Constant.THEMES, json);
                    parseJson(response);
                }
            });
        } else {
            String json = PreUtils.getStringFromDefault(mActivity, Constant.THEMES, "");
            try {
                JSONObject jsonObject = new JSONObject(json);
                parseJson(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void parseJson(JSONObject response) {
        try {
            JSONArray itemsArray = response.getJSONArray("others");
            for (int i = 0; i < itemsArray.length(); i++) {
                NewaListItem newsListItem = new NewaListItem();
                JSONObject itemObject = itemsArray.getJSONObject(i);
                newsListItem.setTitle(itemObject.getString("name"));
                newsListItem.setId(itemObject.getString("id"));
                items.add(newsListItem);
            }
            mAdapter = new NewsTypeAdapter();
            lvItem.setAdapter(mAdapter);
            updateTheme();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class NewsTypeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.menu_item, parent, false);
            }
            TextView tv_item = (TextView) convertView
                    .findViewById(R.id.tv_item);
            tv_item.setTextColor(getResources().getColor(isLight ? R.color.light_menu_listview_textcolor : R.color.dark_menu_listview_textcolor));
            tv_item.setText(items.get(position).getTitle());
            return convertView;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_main:
                ((MainActivity) mActivity).loadLatest();
                ((MainActivity) mActivity).closeMenu();
                break;
        }
    }

    public void updateTheme() {
        isLight = ((MainActivity) mActivity).isLight();
        llMenu.setBackgroundColor(getResources().getColor(isLight ? R.color.light_menu_header : R.color.dark_menu_header));
        tvLogin.setTextColor(getResources().getColor(isLight ? R.color.light_menu_header_tv : R.color.dark_menu_header_tv));
        tvBackUp.setTextColor(getResources().getColor(isLight ? R.color.light_menu_header_tv : R.color.dark_menu_header_tv));
        tvDownload.setTextColor(getResources().getColor(isLight ? R.color.light_menu_header_tv : R.color.dark_menu_header_tv));
        tvMain.setBackgroundColor(getResources().getColor(isLight ? R.color.light_menu_index_background : R.color.dark_menu_index_background));
        lvItem.setBackgroundColor(getResources().getColor(isLight ? R.color.light_menu_listview_background : R.color.dark_menu_listview_background));
        mAdapter.notifyDataSetChanged();
    }





}
