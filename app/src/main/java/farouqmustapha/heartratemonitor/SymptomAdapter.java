package farouqmustapha.heartratemonitor;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class SymptomAdapter extends RecyclerView.Adapter<SymptomAdapter.MyViewHolder> {

    private List<Symptom> symptomList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView activity, date, time;

        public MyViewHolder(View view) {
            super(view);
            activity = (TextView) view.findViewById(R.id.txtViewActivity);
            date = (TextView) view.findViewById(R.id.txtViewDate);
            time = (TextView) view.findViewById(R.id.txtViewTime);
        }
    }


    public SymptomAdapter(List<Symptom> symptomList) {
        this.symptomList = symptomList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.symptom_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Symptom symptom = symptomList.get(position);
        holder.activity.setText(symptom.getActivity());
        holder.date.setText(symptom.getDate());
        holder.time.setText(symptom.getTime());
    }

    @Override
    public int getItemCount() {
        return symptomList.size();
    }
}
