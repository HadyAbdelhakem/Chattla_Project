package com.agri.chattla.ui.expertMain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agri.chattla.R;
import com.agri.chattla.model.Consult;

import java.util.ArrayList;
import java.util.List;

public class ExpertConsultsAdapter extends RecyclerView.Adapter<ExpertConsultsAdapter.ViewHolder> {

    private Context mContext;
    private List<Consult> consults = new ArrayList<>();

    private final onItemClick onItemClick;

    public interface onItemClick {
        void onItemClick(Consult consult, TextView textView);
    }

    public void setConsults(List<Consult> consults) {
        this.consults = consults;
        notifyDataSetChanged();
    }

    public ExpertConsultsAdapter(Context context, onItemClick onItemClick) {
        this.mContext = context;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_consult, parent, false);
        return new ExpertConsultsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final Consult consult = consults.get(position);

        holder.bind(consult, onItemClick);

    }

    @Override
    public int getItemCount() {
        return consults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvCategory;
        private TextView tvDescription;
        private TextView tvStatus;
        private TextView tvTime;
        private TextView tvAcceptConsult;
        private TextView tvConsultAccepted;
        private LinearLayout lyContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCategory = itemView.findViewById(R.id.tv_category);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvAcceptConsult = itemView.findViewById(R.id.tv_accept_consult);
            lyContainer = itemView.findViewById(R.id.ly_container);
            tvConsultAccepted = itemView.findViewById(R.id.tv_accept_status);

            tvStatus.setVisibility(View.GONE);
            tvAcceptConsult.setVisibility(View.VISIBLE);



        }


        private void bind(final Consult consult, final onItemClick onItemClick) {

            tvCategory.setText(consult.getCategory());
            tvDescription.setText(consult.getDesc());
            tvTime.setText(consult.getTime());

            if (consult.getStatus().equals("pending")) {
                tvStatus.setText("الحالة : جارى التواصل مع خبير");
            } else if (consult.getStatus().equals("accepted")) {
                tvStatus.setText("الحالة : تم قبول الاستشارة");
                tvAcceptConsult.setVisibility(View.GONE);
                tvConsultAccepted.setVisibility(View.VISIBLE);
            } else if (consult.getStatus().equals("finished")) {
                tvStatus.setText("الحالة : تم إنتهاء الاستشارة");
            }


            tvAcceptConsult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.onItemClick(consult, tvAcceptConsult);
                }
            });


        }

    }

}
