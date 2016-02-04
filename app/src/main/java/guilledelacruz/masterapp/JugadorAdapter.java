package guilledelacruz.masterapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by guilledelacruz on 26/01/16.
 */
public class JugadorAdapter extends ArrayAdapter<Player> {

    private Player[] jugadores;

    public JugadorAdapter (Context context, Player[] jugadores){
        super(context, android.R.layout.simple_list_item_1, jugadores);
        this.jugadores = jugadores;
    }

    public View getView(int position, View convertView, ViewGroup group){

        View v = convertView;
        ViewHolder vh;

        if (v == null){

            LayoutInflater lay = LayoutInflater.from(getContext());
            v = lay.inflate(R.layout.adapter_listjugadores, null);
            vh = new ViewHolder();

            vh.title = (TextView) v.findViewById(R.id.adaptertext);

            v.setTag(vh);

        } else {
            vh = (ViewHolder) v.getTag();
        }

        vh.title.setText(jugadores[position].getNickname());

        return v;
    }

    static class ViewHolder {
        TextView title;
    }
}
