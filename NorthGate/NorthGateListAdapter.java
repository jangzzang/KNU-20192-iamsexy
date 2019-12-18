package com.example.continuousliving.NorthGate;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.continuousliving.R;

import org.w3c.dom.Text;

import java.util.List;

public class NorthGateListAdapter extends BaseAdapter {
    private Context context;
    private List<NorthGate_Content> boardlist;

    public NorthGateListAdapter(Context context, List<NorthGate_Content> boardlist) {
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
        View v = View.inflate(context, R.layout.northgate_view, null);
        TextView ngla_datatext = (TextView)v.findViewById(R.id.northgate_view_date);
        TextView ngla_nametext = (TextView)v.findViewById(R.id.northgate_view_name);
        TextView ngla_titletext = (TextView)v.findViewById(R.id.northgate_view_title);

        ngla_datatext.setText(boardlist.get(position).getNgc_date());
        ngla_nametext.setText(boardlist.get(position).getNgc_username());
        ngla_titletext.setText(boardlist.get(position).getNgc_title());

        return v;
    }
}
