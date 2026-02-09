/*
 * Fwd: the port forwarding app
 * Copyright (C) 2016  Elixsr Ltd
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.elixsr.portforwarder.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.elixsr.portforwarder.R;
import com.elixsr.portforwarder.forwarding.ForwardingManager;
import com.elixsr.portforwarder.models.RuleModel;
import com.elixsr.portforwarder.ui.rules.EditRuleActivity;

import java.util.List;

/**
 * Created by Niall McShane on 01/03/2016.
 */
public class RuleListAdapter extends RecyclerView.Adapter<RuleListAdapter.RuleViewHolder> {

    private List<RuleModel> ruleModels;
    private ForwardingManager forwardingManager;

    public static class RuleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView ruleProtocolText;
        public TextView ruleNameText;
        public TextView ruleFromPortText;
        public TextView ruleTargetPortText;
        private ForwardingManager forwardingManager;
        private long ruleId;

        public RuleViewHolder(View v, ForwardingManager forwardingManager) {
            super(v);
            this.forwardingManager = forwardingManager;
            ruleProtocolText = (TextView) v.findViewById(R.id.rule_item_protocol);
            ruleNameText = (TextView) v.findViewById(R.id.rule_item_name);
            ruleFromPortText = (TextView) v.findViewById(R.id.rule_item_from_port);
            ruleTargetPortText = (TextView) v.findViewById(R.id.rule_item_target_port);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (!forwardingManager.isEnabled()) {
                Intent editRuleIntent = new Intent(view.getContext(), EditRuleActivity.class);
                editRuleIntent.putExtra("RuleModelLocation", getAdapterPosition());
                editRuleIntent.putExtra("RuleModelId", this.ruleId);
                view.getContext().startActivity(editRuleIntent);
            }
        }
    }

    public RuleListAdapter(List<RuleModel> ruleModels, ForwardingManager forwardingManager, Context context) {
        this.ruleModels = ruleModels;
        this.forwardingManager = forwardingManager;
    }

    @Override
    public RuleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rule_item_view, parent, false);
        return new RuleViewHolder(view, forwardingManager);
    }

    @Override
    public void onBindViewHolder(RuleViewHolder holder, int position) {
        RuleModel ruleModel = ruleModels.get(position);
        holder.ruleId = ruleModel.getId();
        holder.ruleProtocolText.setText(ruleModel.protocolToString());

        if (!ruleModel.isEnabled()) {
            holder.ruleNameText.setAlpha(0.4f);
            holder.ruleProtocolText.setBackgroundResource(R.drawable.bg_grey);
        } else {
            holder.ruleNameText.setAlpha(1f);
            holder.ruleProtocolText.setBackgroundResource(R.drawable.bg_red);
        }
        holder.ruleNameText.setText(ruleModel.getName());
        holder.ruleFromPortText.setText(String.valueOf(ruleModel.getFromPort()));
        holder.ruleTargetPortText.setText(String.valueOf(ruleModel.getTarget().getPort()));
    }

    @Override
    public int getItemCount() {
        return ruleModels.size();
    }
}
