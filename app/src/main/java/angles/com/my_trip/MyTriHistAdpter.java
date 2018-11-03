package angles.com.my_trip;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import angles.com.R;
import angles.com.home.MapFragment;

public class MyTriHistAdpter extends RecyclerView.Adapter<MyTriHistAdpter.MyViewHolder> {

    Context context;

    public MyTriHistAdpter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyTriHistAdpter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_trip_history,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyTriHistAdpter.MyViewHolder holder, int position) {

        holder.layout_myhist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MyTripHistoryActivity)context).FragmentTransfer();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView dt_myhist,pick_add_myhis,drop_add_myhis;
        LinearLayout layout_myhist;

        public MyViewHolder(View itemView) {
            super(itemView);
            dt_myhist=itemView.findViewById(R.id.dt_myhist);
            pick_add_myhis=itemView.findViewById(R.id.pick_add_myhis);
            drop_add_myhis=itemView.findViewById(R.id.drop_add_myhis);
            layout_myhist=itemView.findViewById(R.id.layout_myhist);
        }
    }
}
