package com.example.administrator.httpbbs;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;



public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.BoardHolder> {

    List<Board> data;

    // 기존 방식과 조금 다르다는 것을 알 수 있다.
    public BoardAdapter() {

        data = new ArrayList<>();           // null 이 되지 않도록 초기에 텅 빈 값을 넣어주고 나중에 넣어서 notifyDataChanged 해 준다.
    }

    // 데이터가 모두 생성 완료된 후 다시 데이터를 세팅해 준다.
    public void setList(List<Board> data){

        this.data = data;
    }

    @Override
    public BoardHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

        return new BoardHolder(view);
    }

    @Override
    public void onBindViewHolder(BoardHolder holder, int position) {

        Board board = data.get(position);
        holder.no.setText(board.id+"");
        holder.title.setText(board.title);
        holder.author.setText(board.author);
        holder.content.setText(board.content);
        holder.date.setText(board.date);
    }

    @Override
    public int getItemCount() {
        Log.e("데이터 개수1", data.size()+"");
        return data.size();
    }

    public class BoardHolder extends RecyclerView.ViewHolder {

        TextView no, title, author, content, date;

        public BoardHolder(View itemView) {
            super(itemView);

            no = (TextView) itemView.findViewById(R.id.no);
            title = (TextView) itemView.findViewById(R.id.title);
            author = (TextView) itemView.findViewById(R.id.author);
            content = (TextView) itemView.findViewById(R.id.content);
            date = (TextView) itemView.findViewById(R.id.date);

        }
    }
}
