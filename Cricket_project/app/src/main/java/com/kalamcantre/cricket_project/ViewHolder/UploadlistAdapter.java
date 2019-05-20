package com.kalamcantre.cricket_project.ViewHolder;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kalamcantre.cricket_project.R;
import com.kalamcantre.cricket_project.RepairActivity2;

import java.util.List;

public class UploadlistAdapter  extends  RecyclerView.Adapter<UploadlistAdapter.ViewHolder>{

    public List<String> fileNameList;
    public List<String> fileDoneList;



    public UploadlistAdapter(List<String> fileNameList, List<String>fileDoneList){

        this.fileDoneList = fileDoneList;
        this.fileNameList = fileNameList;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v =  LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_single, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
       // RepairActivity2 repairActivity2=new RepairActivity2();

        String fileName = fileNameList.get(i);

        viewHolder.fileNameView.setText(fileName);

        String fileDone = fileDoneList.get(i);

        if(fileDone.equals("uploading")){


            viewHolder.fileDoneView.setImageResource(R.drawable.progress);

        } else {

            viewHolder.fileDoneView.setImageResource(R.drawable.checked);

        }

    }

    @Override
    public int getItemCount() {



       return fileNameList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View mView;


        public TextView fileNameView;
        public ImageView fileDoneView;


        public ViewHolder(@NonNull View itemView) {
        super(itemView);

        mView=itemView;

            fileNameView = (TextView) mView.findViewById(R.id.upload_filename);
            fileDoneView = (ImageView) mView.findViewById(R.id.upload_loading);
    }
}
}
