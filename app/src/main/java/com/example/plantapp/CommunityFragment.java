package com.example.plantapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CommunityFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postLists;
    private TextView profile;

    private RecyclerView recyclerView_story;
    private StoryAdapter storyAdapter;
    private List<Story> storyList;

    private List<String> followingList;
    ProgressBar progressBar;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);

        recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        postLists=new ArrayList<>();
        postAdapter=new PostAdapter(getContext(),postLists);
        recyclerView.setAdapter(postAdapter);
        profile=view.findViewById(R.id.profile);
        progressBar=view.findViewById(R.id.progress_circular);

        recyclerView_story=view.findViewById(R.id.recycler_view_story);
        recyclerView_story.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView_story.setLayoutManager(linearLayoutManager1);
        storyList=new ArrayList<>();
        storyAdapter=new StoryAdapter(getContext(),storyList);
        recyclerView_story.setAdapter(storyAdapter);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BottomActivity)getActivity()).replaceprofile();
            }
        });

        checkFollowing();
        return view;

    }


    private void checkFollowing(){
        followingList=new ArrayList<>();

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Follow")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followingList.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    followingList.add(snapshot1.getKey());
                }

                readPosts();
                readStory();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readPosts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postLists.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    Post post=snapshot1.getValue(Post.class);
                    for(String id : followingList){
                        if(post.getPublisher().equals(id)){
                            postLists.add(post);
                        }
                    }
                }
                postAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readStory(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Story");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long timecurrent=System.currentTimeMillis();
                storyList.clear();
                storyList.add(new Story("",0,0,"",FirebaseAuth.getInstance().getCurrentUser().getUid()));
                for (String id: followingList){
                    int countStory=0;
                    Story story=null;
                    for (DataSnapshot snapshot1: snapshot.child(id).getChildren()){
                        story=snapshot1.getValue(Story.class);
                        if (timecurrent > story.getTimestart()&&timecurrent<story.getTimeend()){
                            countStory++;
                        }
                    }
                    if (countStory>0){
                        storyList.add(story);
                    }
                }
                storyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
