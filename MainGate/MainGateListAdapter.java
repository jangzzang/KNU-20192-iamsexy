package com.example.continuousliving.MainGate;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.continuousliving.R;

import org.w3c.dom.Text;

import java.util.List;

public class MainGateListAdapter extends BaseAdapter {
    private Context context;
    private List<MainGate_Content> boardlist;

    public MainGateListAdapter(Context context, List<MainGate_Content> boardlist) {
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
        View v = View.inflate(context, R.layout.maingate_view, null);
        TextView mgla_datatext = (TextView)v.findViewById(R.id.maingate_view_date);
        TextView mgla_nametext = (TextView)v.findViewById(R.id.maingate_view_name);
        TextView mgla_titletext = (TextView)v.findViewById(R.id.maingate_view_title);

        mgla_datatext.setText(boardlist.get(position).getMgc_date());
        mgla_nametext.setText(boardlist.get(position).getMgc_username());
        mgla_titletext.setText(boardlist.get(position).getMgc_title());

        return v;
    }
}