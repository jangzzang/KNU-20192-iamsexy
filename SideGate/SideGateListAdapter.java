package com.example.continuousliving.SideGate;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.continuousliving.R;

import org.w3c.dom.Text;

import java.util.List;

public class SideGateListAdapter extends BaseAdapter {
    private Context context;
    private List<SideGate_Content> boardlist;

    public SideGateListAdapter(Context context, List<SideGate_Content> boardlist) {
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
        View v = View.inflate(context, R.layout.sidegate_view, null);
        TextView sgla_datatext = (TextView)v.findViewById(R.id.sidegate_view_date);
        TextView sgla_nametext = (TextView)v.findViewById(R.id.sidegate_view_name);
        TextView sgla_titletext = (TextView)v.findViewById(R.id.sidegate_view_title);

        sgla_datatext.setText(boardlist.get(position).getSgc_date());
        sgla_nametext.setText(boardlist.get(position).getSgc_username());
        sgla_titletext.setText(boardlist.get(position).getSgc_title());

        return v;
    }
}
