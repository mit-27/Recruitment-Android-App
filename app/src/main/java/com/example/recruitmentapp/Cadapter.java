package com.example.recruitmentapp;

import android.app.AlertDialog;
import android.content.Context;

import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;

import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;


public class Cadapter extends RecyclerView.Adapter<Cadapter.ItemViewHolder> {


    private ArrayList<Candidate> list_members;

    View view;
    ItemViewHolder holder;
    private Context context;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    public Cadapter(ArrayList<Candidate> persons, Context context) {
        this.list_members = persons;
        this.context = context;
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view=LayoutInflater.from(parent.getContext()).inflate(R.layout.custommenu_item, parent, false);
        holder=new ItemViewHolder(view);

        return holder;
    }


    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        final Candidate list_items=list_members.get(position);

        holder.name.setText(list_items.getName());
        holder.jobtitle.setText(list_items.getJobtitle());
        holder.Schedule.setText("Schedule");
        holder.interview_status.setText(list_items.getStatus());
        if(list_items.isInterview_status())
        {
            holder.interview_scheduled.setText("Interview Scheduled");
            holder.Schedule.setText("Update Schedule");
            holder.dt_layout.setVisibility(LinearLayout.VISIBLE);
            holder.tv_date.setText(list_items.getIntv_date());
            holder.tv_time.setText(list_items.getIntv_time());
        }
        else
        {
            holder.interview_scheduled.setText("Interview not Scheduled");
            holder.dt_layout.setVisibility(LinearLayout.GONE);
//            holder.Schedule.setEnabled(true);

        }
        holder.Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(context,Schedule.class);
                i.putExtra("Can_name",list_items.getName());
                i.putExtra("Can_id",list_items.getCid());
                i.putExtra("Can_int_date",list_items.getIntv_date());
                i.putExtra("Can_int_time",list_items.getIntv_time());
                context.startActivity(i);

            }
        });





    }

    @Override
    public int getItemCount() {
        return list_members.size();
    }



    //View holder class, where all view components are defined
    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        public TextView name,jobtitle,interview_status,interview_scheduled,tv_date,tv_time;
        public Button Schedule;
        public LinearLayout item_layout,dt_layout;

        public ItemViewHolder(View itemView) {
            super(itemView);

            item_layout = itemView.findViewById(R.id.customMenu);
            name=itemView.findViewById(R.id.tv_candidate_name);
            jobtitle = itemView.findViewById(R.id.tv_candidate_jobtitle);
            interview_status = itemView.findViewById(R.id.tv_candidate_interview_status);
            interview_scheduled = itemView.findViewById(R.id.tv_candidate_interview_scheduled);
            Schedule = itemView.findViewById(R.id.btn_schedule);
            tv_date = itemView.findViewById(R.id.tv_int_date_value);
            tv_time = itemView.findViewById(R.id.tv_int_time_value);
            dt_layout = itemView.findViewById(R.id.l_intv_dt);
            item_layout.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(this.getAdapterPosition(),101,0,"Edit");
            contextMenu.add(this.getAdapterPosition(),102,1,"Delete");
        }




//        @Override
//        public void onClick(View v) {
//
//        }
    }
    public void removeItem(int position) {
        final Candidate list_items=list_members.get(position);
        String id = list_items.getCid();

         databaseReference = firebaseDatabase.getReference("Users").child(user.getUid()).child("Candidates_details").child(id);
        databaseReference.removeValue();

    }

    public void updateItem(int position)
    {
        final Candidate list_items=list_members.get(position);
        final String cid = list_items.getCid();
        String cName = list_items.getName();
        String cemail = list_items.getEmail();
        String cphone = list_items.getPhone();
        String cjobtitle = list_items.getJobtitle();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        final View dialogView = inflater.inflate(R.layout.update_candidate_dialoge, null);
        dialogBuilder.setView(dialogView);

        final EditText et_name = dialogView.findViewById(R.id.U_input_name);
        final EditText et_email = dialogView.findViewById(R.id.U_input_email);
        final EditText et_phone = dialogView.findViewById(R.id.U_input_phone);
        final EditText et_jobtitle = dialogView.findViewById(R.id.U_jobTitle);

        Button update_btn = dialogView.findViewById(R.id.btn_uUpdate);
        Button cancle_btn = dialogView.findViewById(R.id.btn_uCancle);
        et_name.setText(cName);
        et_phone.setText(cphone);
        et_email.setText(cemail);
        et_jobtitle.setText(cjobtitle);

        final AlertDialog update = dialogBuilder.create();
        update.show();

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cName = et_name.getText().toString();
                String cemail = et_email.getText().toString();
                String cphone = et_phone.getText().toString();
                String cjobtitle = et_jobtitle.getText().toString();
                String cgender = list_items.getGender();
                String csource = list_items.getSource();
                String cstatus = list_items.getStatus();
                boolean citerview = list_items.isInterview_status();
                Log.i("Name",cName);
                databaseReference = firebaseDatabase.getReference("Users").child(user.getUid()).child("Candidates_details").child(cid);
                Candidate candidate = new Candidate(cName,cemail,cgender,cjobtitle,cphone,csource,cstatus,cid,citerview);
//                HashMap<String,String> hashMap = new HashMap<>();
//                hashMap.put("name",cName);
//                hashMap.put("email",cemail);
//                hashMap.put("jobtitle",cjobtitle);
//                hashMap.put("phone",cphone);
//                hashMap.put("cid",cid);
                databaseReference.setValue(candidate);
                update.dismiss();

            }
        });

        cancle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update.dismiss();
            }
        });
    }

}