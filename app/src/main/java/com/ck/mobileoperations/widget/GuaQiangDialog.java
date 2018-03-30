package com.ck.mobileoperations.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ck.mobileoperations.R;
import com.ck.mobileoperations.utils.SwipeListLayout;
import com.ck.mobileoperations.utils.cable.CableCache;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by CK on 2017/12/25.
 */

public class GuaQiangDialog extends Dialog {
    Activity context;
    private String jingdu;
    private String weidu;
    private TextView gqJd;
    private TextView gqWd;
    private Button sendMark;
    private Button cancelMark;
    public EditText gqname;
    private View.OnClickListener monClickListener;
    /*-------------*/
    private Button addcable;
    public ListView cablelistView;
    public List<CableCache> cableCacheList=new ArrayList<>(); //保存已经添加的光缆信息
    public List<String> cableguaqiangList=new ArrayList<>();
    private Set<SwipeListLayout> cablesets = new HashSet();

    private Button addNode;
    public ListView listView;
    public List<String> gqxList = new ArrayList<String>();
    private Set<SwipeListLayout> sets = new HashSet();
    public GuaQiangDialog( Activity context) {
        super(context);
        this.context=context;
    }

    public GuaQiangDialog(Activity context, int theme, View.OnClickListener onClickListener, String jingdu, String weidu) {
        super(context, theme);
        this.context=context;
        this.monClickListener=onClickListener;
        this.jingdu=jingdu;
        this.weidu=weidu;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.create_guaqiang_dialog);

        gqname=(EditText)findViewById(R.id.gq_name);
        gqJd=(TextView)findViewById(R.id.gq_jingdu);
        gqWd=(TextView)findViewById(R.id.gq_weidu);
        sendMark=(Button)findViewById(R.id.gq_send_mark);
        cancelMark=(Button)findViewById(R.id.gq_cancel_mark);

        addNode=(Button)findViewById(R.id.add_node_guaqiang);
        listView=(ListView)findViewById(R.id.guaqiang_node_name_list);
        addcable=(Button)findViewById(R.id.add_cable_guaqiang);
        cablelistView=(ListView)findViewById(R.id.guaqiang_cable_name_list);
         /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = this.getWindow();

        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        // p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.9); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(p);

        gqJd.setText(jingdu);
        gqWd.setText(weidu);

        addcable.setOnClickListener(monClickListener);
        addNode.setOnClickListener(monClickListener);
        sendMark.setOnClickListener(monClickListener);
        cancelMark.setOnClickListener(monClickListener);
        this.setCancelable(true);

        ListAdapter listAdapter=new ListAdapter();
        listAdapter.notifyDataSetChanged();

        listView.setAdapter(listAdapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    //当listview开始滑动时，若有item的状态为Open，则Close，然后移除
                    case SCROLL_STATE_TOUCH_SCROLL:
                        if (sets.size() > 0) {
                            for (SwipeListLayout s : sets) {
                                s.setStatus(SwipeListLayout.Status.Close, true);
                                sets.remove(s);
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

            }
        });
        CableListAdapter cableListAdapter=new CableListAdapter();
        cableListAdapter.notifyDataSetChanged();
        cablelistView.setAdapter(cableListAdapter);
        cablelistView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    //当listview开始滑动时，若有item的状态为Open，则Close，然后移除
                    case SCROLL_STATE_TOUCH_SCROLL:
                        if (cablesets.size() > 0) {
                            for (SwipeListLayout s : cablesets) {
                                s.setStatus(SwipeListLayout.Status.Close, true);
                                cablesets.remove(s);
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

            }
        });
    }

    class MyOnSlipStatusListener implements SwipeListLayout.OnSwipeStatusListener {

        private SwipeListLayout slipListLayout;

        public MyOnSlipStatusListener(SwipeListLayout slipListLayout) {
            this.slipListLayout = slipListLayout;
        }

        @Override
        public void onStatusChanged(SwipeListLayout.Status status) {
            if (status == SwipeListLayout.Status.Open) {
                //若有其他的item的状态为Open，则Close，然后移除
                if (sets.size() > 0) {
                    for (SwipeListLayout s : sets) {
                        s.setStatus(SwipeListLayout.Status.Close, true);
                        sets.remove(s);
                    }
                }
                sets.add(slipListLayout);
            } else {
                if (sets.contains(slipListLayout))
                    sets.remove(slipListLayout);
            }
        }

        @Override
        public void onStartCloseAnimation() {

        }

        @Override
        public void onStartOpenAnimation() {

        }

    }

    class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return gqxList.size();
        }

        @Override
        public Object getItem(int arg0) {
            return gqxList.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(final int arg0, View view, ViewGroup arg2) {
            if (view == null) {
                view = LayoutInflater.from(context).inflate(
                        R.layout.slip_list_item, null);
            }
            TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_name.setText(gqxList.get(arg0));
            final SwipeListLayout sll_main = (SwipeListLayout) view
                    .findViewById(R.id.sll_main);
            TextView tv_top = (TextView) view.findViewById(R.id.tv_top);
            TextView tv_delete = (TextView) view.findViewById(R.id.tv_delete);
            sll_main.setOnSwipeStatusListener(new GuaQiangDialog.MyOnSlipStatusListener(
                    sll_main));
            tv_top.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    sll_main.setStatus(SwipeListLayout.Status.Close, true);
                    String str = gqxList.get(arg0);
                    gqxList.remove(arg0);
                    gqxList.add(0, str);
                    notifyDataSetChanged();
                }
            });
            tv_delete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    sll_main.setStatus(SwipeListLayout.Status.Close, true);
                    gqxList.remove(arg0);
                    notifyDataSetChanged();
                }
            });
            return view;
        }

    }
    //光缆adapter
    class cableMyOnSlipStatusListener implements SwipeListLayout.OnSwipeStatusListener {

        private SwipeListLayout slipListLayout;

        public cableMyOnSlipStatusListener(SwipeListLayout slipListLayout) {
            this.slipListLayout = slipListLayout;
        }

        @Override
        public void onStatusChanged(SwipeListLayout.Status status) {
            if (status == SwipeListLayout.Status.Open) {
                //若有其他的item的状态为Open，则Close，然后移除
                if (cablesets.size() > 0) {
                    for (SwipeListLayout s : cablesets) {
                        s.setStatus(SwipeListLayout.Status.Close, true);
                        cablesets.remove(s);
                    }
                }
                cablesets.add(slipListLayout);
            } else {
                if (cablesets.contains(slipListLayout))
                    cablesets.remove(slipListLayout);
            }
        }

        @Override
        public void onStartCloseAnimation() {

        }

        @Override
        public void onStartOpenAnimation() {

        }

    }
    class CableListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return cableguaqiangList.size();
        }

        @Override
        public Object getItem(int arg0) {
            return cableguaqiangList.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(final int arg0, View view, ViewGroup arg2) {
            if (view == null) {
                view = LayoutInflater.from(context).inflate(
                        R.layout.slip_list_item, null);
            }
            TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_name.setText(cableguaqiangList.get(arg0));
            final SwipeListLayout sll_main = (SwipeListLayout) view
                    .findViewById(R.id.sll_main);
            TextView tv_top = (TextView) view.findViewById(R.id.tv_top);
            TextView tv_delete = (TextView) view.findViewById(R.id.tv_delete);
            sll_main.setOnSwipeStatusListener(new GuaQiangDialog.cableMyOnSlipStatusListener(
                    sll_main));
            tv_top.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    sll_main.setStatus(SwipeListLayout.Status.Close, true);
                    String str = cableguaqiangList.get(arg0);
                    cableguaqiangList.remove(arg0);
                    cableguaqiangList.add(0, str);

                    CableCache cableCache=cableCacheList.get(arg0);
                    cableCacheList.remove(arg0);
                    cableCacheList.add(0,cableCache);

                    notifyDataSetChanged();
                }
            });
            tv_delete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    sll_main.setStatus(SwipeListLayout.Status.Close, true);

                    cableguaqiangList.remove(arg0);
                    cableCacheList.remove(arg0);

                    notifyDataSetChanged();
                }
            });
            return view;
        }

    }
}
