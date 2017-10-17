package com.riking.calendar.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.riking.calendar.R;
import com.riking.calendar.listener.PullCallback;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.RecyclerViewPositionHelper;


public class PullToLoadViewWithoutFloatButton extends FrameLayout {
    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    protected CONST.ScrollDirection mCurScrollingDirection;
    protected int mPrevFirstVisibleItem = 0;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private PullCallback mPullCallback;
    private RecyclerViewPositionHelper mRecyclerViewHelper;
    private int mLoadMoreOffset = 5;
    private boolean mIsLoadMoreEnabled = false;
    private boolean mVisible;

    public PullToLoadViewWithoutFloatButton(Context context) {
        this(context, null);
    }

    public PullToLoadViewWithoutFloatButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("NewApi")
    public PullToLoadViewWithoutFloatButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.loadview_without_floatbutton, this, true);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mVisible = true;
        init();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mRecyclerViewHelper = RecyclerViewPositionHelper
                .createHelper(mRecyclerView);
    }

    private void init() {

        mSwipeRefreshLayout
                .setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if (null != mPullCallback) {
                            mPullCallback.onRefresh();
                        }
                    }
                });

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mCurScrollingDirection = null;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    hide();
                } else {
                    show();
                }
                if (mCurScrollingDirection == null) { // User has just started a
                    // scrolling motion
                    mCurScrollingDirection = CONST.ScrollDirection.SAME;
                    mPrevFirstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();
                } else {
                    final int firstVisibleItem = mRecyclerViewHelper
                            .findFirstVisibleItemPosition();
                    if (firstVisibleItem > mPrevFirstVisibleItem) {
                        // User is scrolling up
                        mCurScrollingDirection = CONST.ScrollDirection.UP;
                    } else if (firstVisibleItem < mPrevFirstVisibleItem) {
                        // User is scrolling down
                        mCurScrollingDirection = CONST.ScrollDirection.DOWN;
                    } else {
                        mCurScrollingDirection = CONST.ScrollDirection.SAME;
                    }
                    mPrevFirstVisibleItem = firstVisibleItem;
                }

                if (mIsLoadMoreEnabled
                        && (mCurScrollingDirection == CONST.ScrollDirection.UP)) {
                    // We only need to paginate if user scrolling near the end
                    // of the list
                    if (!mPullCallback.isLoading()
                            && !mPullCallback.hasLoadedAllItems()) {
                        // Only trigger a load more if a load operation is NOT
                        // happening AND all the items have not been loaded
                        final int totalItemCount = mRecyclerViewHelper
                                .getItemCount();
                        final int firstVisibleItem = mRecyclerViewHelper
                                .findFirstVisibleItemPosition();
                        final int visibleItemCount = Math
                                .abs(mRecyclerViewHelper
                                        .findLastVisibleItemPosition()
                                        - firstVisibleItem);
                        final int lastAdapterPosition = totalItemCount - 1;
                        final int lastVisiblePosition = (firstVisibleItem + visibleItemCount) - 1;
                        if (lastVisiblePosition >= (lastAdapterPosition - mLoadMoreOffset)) {
                            if (null != mPullCallback) {
                                mProgressBar.setVisibility(VISIBLE);
                                mPullCallback.onLoadMore();
                            }
                        }
                    } else if (mPullCallback.hasLoadedAllItems()) {
                        Toast.makeText(getContext(), "没有更多数据了",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void setComplete() {
        mProgressBar.setVisibility(GONE);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void initLoad() {
        if (null != mPullCallback) {
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
            });
            mPullCallback.onRefresh();
        }
    }

    public void setColorSchemeResources(int... colorResIds) {
        mSwipeRefreshLayout.setColorSchemeResources(colorResIds);
    }

    public RecyclerView getRecyclerView() {
        return this.mRecyclerView;
    }

    public void setPullCallback(PullCallback mPullCallback) {
        this.mPullCallback = mPullCallback;
    }

    public void setLoadMoreOffset(int mLoadMoreOffset) {
        this.mLoadMoreOffset = mLoadMoreOffset;
    }

    public void isLoadMoreEnabled(boolean mIsLoadMoreEnabled) {
        this.mIsLoadMoreEnabled = mIsLoadMoreEnabled;
    }

    public void show() {
        show(true);
    }

    public void hide() {
        hide(true);
    }

    public void show(boolean animate) {
        toggle(true, animate, false);
    }

    public void hide(boolean animate) {
        toggle(false, animate, false);
    }

    private void toggle(final boolean visible, final boolean animate,
                        boolean force) {

        Log.d("zzw", "toggle");
        if (mVisible != visible || force) {
            mVisible = visible;
            int height = getHeight();
            if (height == 0 && !force) {
                ViewTreeObserver vto = getViewTreeObserver();
                if (vto.isAlive()) {
                    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            ViewTreeObserver currentVto = getViewTreeObserver();
                            if (currentVto.isAlive()) {
                                currentVto.removeOnPreDrawListener(this);
                            }
                            toggle(visible, animate, true);
                            return true;
                        }
                    });
                    return;
                }
            }
        }
    }
}
