package com.android.turntable.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.android.turntable.R;
import com.android.turntable.Todo;
import com.android.turntable.utils.TodoPreferences;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private List<Todo> todos;
    private TodoPreferences preferences;

    public TodoAdapter(List<Todo> todos, TodoPreferences preferences) {
        this.todos = todos;
        this.preferences = preferences;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_todo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Todo todo = todos.get(position);
        holder.textTodo.setText(todo.getContent());
        holder.checkbox.setChecked(todo.isCompleted());
        
        holder.checkbox.setOnClickListener(v -> {
            preferences.toggleTodo(todo.getId());
            refreshData();
        });
        
        holder.buttonDelete.setOnClickListener(v -> {
            preferences.deleteTodo(todo.getId());
            refreshData();
        });
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    public void refreshData() {
        todos = preferences.getTodos();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkbox;
        TextView textTodo;
        ImageButton buttonDelete;

        ViewHolder(View view) {
            super(view);
            checkbox = view.findViewById(R.id.checkbox);
            textTodo = view.findViewById(R.id.text_todo);
            buttonDelete = view.findViewById(R.id.button_delete);
        }
    }
} 
