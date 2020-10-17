package com.chazzca.chismografoforte.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chazzca.chismografoforte.R;
import com.chazzca.chismografoforte.models.ModeloListado;

import java.util.ArrayList;

public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.MyViewHolder> {

    private ArrayList<ModeloListado> itemArrayList;
    private Context context;
    private LayoutInflater inflater;

    public ListaAdapter(ArrayList<ModeloListado> itemArrayList, Context context){

        this.itemArrayList = itemArrayList;
        this.context = context;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
    }

    @Override
    public ListaAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.listado_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }



    @Override
    public void onBindViewHolder(final ListaAdapter.MyViewHolder holder, final int position) {

        holder.txtUsuario.setText(itemArrayList.get(position).Username);
        Log.e("TEST",itemArrayList.toString());
        holder.txtP1.setText(itemArrayList.get(position).preguntas.get(0).respuesta);
        holder.txtP2.setText(itemArrayList.get(position).preguntas.get(1).respuesta);
        holder.txtP3.setText(itemArrayList.get(position).preguntas.get(2).respuesta);
        holder.txtP4.setText(itemArrayList.get(position).preguntas.get(3).respuesta);
        holder.txtP5.setText(itemArrayList.get(position).preguntas.get(4).respuesta);
        holder.txtP6.setText(itemArrayList.get(position).preguntas.get(5).respuesta);
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        protected TextView txtUsuario,txtP1,txtP2,txtP3,txtP4,txtP5,txtP6;


        public MyViewHolder(View itemView) {
            super(itemView);

            txtUsuario = itemView.findViewById(R.id.txtUsuario);
            txtP1 = (TextView) itemView.findViewById(R.id.txtP1);
            txtP2 = (TextView) itemView.findViewById(R.id.txtP2);
            txtP3 = (TextView) itemView.findViewById(R.id.txtP3);
            txtP4 = (TextView) itemView.findViewById(R.id.txtP4);
            txtP5 = (TextView) itemView.findViewById(R.id.txtP5);
            txtP6 = (TextView) itemView.findViewById(R.id.txtP6);

        }

    }
}