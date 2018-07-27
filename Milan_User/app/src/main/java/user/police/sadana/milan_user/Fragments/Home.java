package user.police.sadana.milan_user.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import butterknife.ButterKnife;
import user.police.sadana.milan_user.Constants.LostChild;
import user.police.sadana.milan_user.R;

public class Home extends Fragment {


    RecyclerView recyclerView;
    FirebaseRecyclerAdapter<LostChild,ChildRecyclerView> recyclerAdapter;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home,container,false);
        ButterKnife.bind(this, v);

        recyclerView = v.findViewById(R.id.lost_recycler);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        recyclerView.hasFixedSize();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("complaints");
        Query query = ref.limitToFirst(100);

        FirebaseRecyclerOptions<LostChild> options =
                new FirebaseRecyclerOptions.Builder<LostChild>()
                        .setQuery(query, LostChild.class)
                        .build();


        recyclerAdapter = new FirebaseRecyclerAdapter<LostChild, ChildRecyclerView>(options){

            @NonNull
            @Override
            public ChildRecyclerView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.recyclerview_lost_child, viewGroup, false);

                return new ChildRecyclerView(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ChildRecyclerView holder, int position, @NonNull final LostChild model) {

                if(!model.getPictureUrl().isEmpty()){
                    Picasso.with(getContext()).load(model.getPictureUrl()).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher) .into(holder.childImageView, new Callback() {
                        @Override
                        public void onSuccess() {}

                        @Override
                        public void onError() {
                            Picasso.with(getContext()).load(model.getPictureUrl()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(holder.childImageView);
                        }
                    });
                }


            holder.childImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LayoutInflater inflater = getLayoutInflater();
                    View alertDialogue = inflater.inflate(R.layout.dialogue_child_info,null);
                    AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                    dialog.setView(alertDialogue);

                    Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);


                    ImageView childImage = alertDialogue.findViewById(R.id.child_image);
                    Picasso.with(getContext()).load(model.getPictureUrl()).into(childImage);

                    TextView name = alertDialogue.findViewById(R.id.name);
                    name.setText(model.getName());

                    TextView age = alertDialogue.findViewById(R.id.age);
                    age.setText(model.getAge());

                    TextView description = alertDialogue.findViewById(R.id.description);
                    description.setText(model.getDescription());

                    TextView height = alertDialogue.findViewById(R.id.height);
                    height.setText(model.getHeight());

                    TextView weight = alertDialogue.findViewById(R.id.weight);
                    weight.setText(model.getWeight());

                    TextView identification = alertDialogue.findViewById(R.id.identification);
                    identification.setText(model.getId1()+" \n" + model.getId2());

                    TextView contact = alertDialogue.findViewById(R.id.contact);
                    contact.setText(model.getContact());

                    TextView incharge = alertDialogue.findViewById(R.id.incharge);
                    incharge.setText(model.getIncharge());

                    dialog.show();
                }
            });
            }
        };


        recyclerView.setAdapter(recyclerAdapter);
        return v;
    }



    public class ChildRecyclerView extends RecyclerView.ViewHolder{

        ImageView childImageView;

        ChildRecyclerView(@NonNull View itemView) {
            super(itemView);
            childImageView = itemView.findViewById(R.id.lostchild);

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerAdapter.startListening();


        /* ToDo Why?? */
        FirebaseApp.initializeApp(getContext());
    }

    @Override
    public void onStop() {
        super.onStop();
        recyclerAdapter.stopListening();
    }
}
