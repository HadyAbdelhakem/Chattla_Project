package com.agri.chattla.ui.farmerMain;

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

public class MyConsultsAdapter extends RecyclerView.Adapter<MyConsultsAdapter.ViewHolder> {

    private Context mContext;
    private List<Consult> consults = new ArrayList<>();


    private final onItemClick onItemClick;

    public interface onItemClick {
        void onItemClick(Consult consult, LinearLayout layout);
    }

    public void setConsults(List<Consult> consults) {
        this.consults = consults;
        notifyDataSetChanged();
    }

    public MyConsultsAdapter(Context context, onItemClick onItemClick) {
        this.mContext = context;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_consult, parent, false);
        return new MyConsultsAdapter.ViewHolder(view);
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
        private LinearLayout lyContainer;
        private TextView tvDetails;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCategory = itemView.findViewById(R.id.tv_category);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvAcceptConsult = itemView.findViewById(R.id.tv_accept_consult);
            lyContainer = itemView.findViewById(R.id.ly_container);
            tvDetails = itemView.findViewById(R.id.tv_details);

            tvAcceptConsult.setVisibility(View.GONE);
            tvStatus.setVisibility(View.VISIBLE);
            tvDetails.setVisibility(View.GONE);


        }


        private void bind(final Consult consult, final onItemClick onItemClick) {

            tvCategory.setText(consult.getCategory());
            tvDescription.setText(consult.getDesc());
            tvTime.setText(consult.getTime());

            if (consult.getPaymentStatus().equals("paid")){
                if (consult.getStatus().equals("pending")) {
                    tvStatus.setText("جارى التواصل مع خبير..");
                    tvStatus.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.background_dialog_two));
                } else if (consult.getStatus().equals("accepted")) {
                    tvStatus.setText("الخبير يتحدث إليك الأن..");
                    tvStatus.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.background_dialog_two));
                } else if (consult.getStatus().equals("finished")) {
                    tvStatus.setText("تم إنهاء الاستشارة");
                    tvStatus.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.background_dialog_three));
                }
            }else if (consult.getPaymentStatus().equals("unPaid")){
                tvStatus.setText("لم يتم الدفع");
                tvStatus.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.background_dialog_one));
            }



            lyContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.onItemClick(consult, lyContainer);
                }
            });

        }

    }

}
