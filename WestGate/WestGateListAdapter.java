package com.example.continuousliving.WestGate;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.continuousliving.R;

import org.w3c.dom.Text;

import java.util.List;

public class WestGateListAdapter extends BaseAdapter {
    private Context context;
    private List<WestGate_Content> boardlist;

    public WestGateListAdapter(Context context, List<WestGate_Content> boardlist) {
        this.context = context;
        this.boardlist = boardlist;
    }

    @Override
    public int getCount() {
        return boardlist.size();
    }

    @Override
    public Object getItem(int position) {
        return boardlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.westgate_view, null);
        TextView wgla_datatext = (TextView)v.findViewById(R.id.westgate_view_date);
        TextView wgla_nametext = (TextView)v.findViewById(R.id.westgate_view_name);
        TextView wgla_titletext = (TextView)v.findViewById(R.id.westgate_view_title);

        wgla_datatext.setText(boardlist.get(position).getWgc_date());
        wgla_nametext.setText(boardlist.get(position).getWgc_username());
        wgla_titletext.setText(boardlist.get(position).getWgc_title());

        return v;
    }
}