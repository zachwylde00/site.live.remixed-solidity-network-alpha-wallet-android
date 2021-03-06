package com.alphawallet.app.ui.widget.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.alphawallet.app.R;
import com.alphawallet.app.entity.tokens.Token;
import com.alphawallet.app.repository.EthereumNetworkRepository;
import com.alphawallet.app.ui.widget.entity.SortedItem;
import com.alphawallet.token.tools.Numeric;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TokenListAdapter extends RecyclerView.Adapter<TokenListAdapter.ViewHolder> {
    LayoutInflater inflater;
    Context context;
    List<Token> data;
    ItemClickListener listener;

    public TokenListAdapter(Context context, Token[] tokens, ItemClickListener listener) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.data = filterTokens(Arrays.asList(tokens));
        this.listener = listener;
    }

    private List<Token> filterTokens(List<Token> tokens) {
        ArrayList<Token> filteredList = new ArrayList<>();
        for (Token t : tokens) {
            if (t.tokenInfo.name != null) {
                if (!t.isEthereum() && !filteredList.contains(t)) {
                    filteredList.add(t);
                }
            }
        }

        Collections.sort(filteredList);
        return filteredList;
    }


    @NonNull
    @Override
    public TokenListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_manage_token, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TokenListAdapter.ViewHolder viewHolder, int i) {
        Token token = data.get(i);
        viewHolder.setIsRecyclable(false);
        viewHolder.tokenName.setText(token.getFullName());
        viewHolder.switchEnabled.setChecked(token.tokenInfo.isEnabled);
        viewHolder.switchEnabled.setTag(new Integer(i));
        viewHolder.switchEnabled.setOnCheckedChangeListener((v, b) -> {
            listener.onItemClick(data.get(i), b);
        });
    }

    public Token getItem(int id) {
        return data.get(id);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        RelativeLayout layout;
        TextView tokenName;
        Switch switchEnabled;

        ViewHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout_list_item);
            tokenName = itemView.findViewById(R.id.name);
            switchEnabled = itemView.findViewById(R.id.switch_enabled);
            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switchEnabled.setChecked(!switchEnabled.isChecked());
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            listener.onItemClick(data.get(getAdapterPosition()), isChecked);

        }
    }

    public interface ItemClickListener {
        void onItemClick(Token token, boolean enabled);
    }



}
