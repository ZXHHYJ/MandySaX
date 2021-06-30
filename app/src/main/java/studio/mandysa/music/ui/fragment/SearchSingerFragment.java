package studio.mandysa.music.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import mandysax.anna2.callback.Callback;
import studio.mandysa.music.R;
import studio.mandysa.music.logic.Model.NeteaseCloudMusicApi;
import studio.mandysa.music.logic.Model.SingerModel;
import studio.mandysa.music.logic.Network.ServiceCreator;
import studio.mandysa.music.ui.base.BaseLoadingFragment;
import studio.mandysa.music.ui.viewholder.SingerViewHolder;
import studio.mandysa.music.ui.viewmodel.SearchViewModel;
import studio.mandysa.music.utils.ImageLoader;
import studio.mandysa.music.utils.TextViewUtils;

public class SearchSingerFragment extends BaseLoadingFragment {

    private final ArrayList<SingerModel> mList = new ArrayList<>();

    private final SingerListAdapter mAdapter = new SingerListAdapter();

    private int mPage = 1;

    private SearchViewModel mViewModel;

    private RecyclerView mRvList;

    private void init() {

        mViewModel = getViewModel(SearchViewModel.class);

        mRvList = findViewById(R.id.rv_searchMusicAndSinger_view);

        mRvList.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvList.setHasFixedSize(true);
        mRvList.setAdapter(mAdapter);

        mRvList.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                assert layoutManager != null;
                int lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                if (lastCompletelyVisibleItemPosition == layoutManager.getItemCount() - 1) {
                    nextPage();
                }
            }
        });

        nextPage();

    }

    private void nextPage() {
        ServiceCreator.create(NeteaseCloudMusicApi.class).searchSinger(mViewModel.getSearchContent().getValue(), (mPage - 1) * 30, 100).set(new Callback<SingerModel>() {

            @Override
            public void onResponse(boolean loaded, SingerModel singerModel) {
                mList.add(singerModel);
                mAdapter.notifyItemInserted(mList.size());
                if (!loaded) return;
                if (mPage == 1)
                    hide();
                mPage++;
            }

            @Override
            public void onFailure(int code) {
                if (code != 200)
                    show();
            }
        });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        nextPage();
    }

    @Override
    protected void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    protected void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        mViewModel.getSearchContent().observe(this, p1 -> {
            mPage = 1;
            if (mRvList.getChildCount() > 0) {
                mRvList.removeAllViews();
                mList.clear();
                mAdapter.notifyDataSetChanged();
            }
            onRefresh();
        });
    }

    @Override
    protected int onCreateView() {
        return R.layout.search_music_and_singer;
    }

    public class SingerListAdapter extends RecyclerView.Adapter<SingerViewHolder> {

        @NonNull
        @Override
        public SingerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new SingerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.singer_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull SingerViewHolder holder, int position) {
            holder.singerName.setText(mList.get(position).name);
            TextViewUtils.markByColor(holder.singerName, mViewModel.getSearchContent().getValue());
            ImageLoader.getInstance().load(mList.get(position).picUrl).into(holder.avatar);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

}
