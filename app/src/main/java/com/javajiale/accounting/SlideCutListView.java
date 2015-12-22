package com.javajiale.accounting;

/**
 * Created by javajiale on 2015/12/15.
 */

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Scroller;
import android.widget.TextView;

import java.util.Date;

public class SlideCutListView extends ListView implements AbsListView.OnScrollListener {
    /**
     * 当前滑动的ListView　position
     */
    private int slidePosition;
    /**
     * 手指按下X的坐标
     */
    private int downY;
    /**
     * 手指按下Y的坐标
     */
    private int downX;
    /**
     * 屏幕宽度
     */
    private int screenWidth;
    /**
     * ListView的item
     */
    private View itemView;
    /**
     * 滑动类
     */
    private Scroller scroller;
    private static final int SNAP_VELOCITY = 600;
    /**
     * 速度追踪对象
     */
    private VelocityTracker velocityTracker;
    /**
     * 是否响应滑动，默认为不响应
     */
    private boolean isSlide = false;
    /**
     * 认为是用户滑动的最小距离
     */
    private int mTouchSlop;
    /**
     *  移除item后的回调接口
     */
    private RemoveListener mRemoveListener;
    /**
     * 用来指示item滑出屏幕的方向,向左或者向右,用一个枚举值来标记
     */
    private RemoveDirection removeDirection;

    // 滑动删除方向的枚举值
    public enum RemoveDirection {
        RIGHT, LEFT;
    }

    //************************************
    private final static int RELEASE_To_REFRESH = 0;// 下拉过程的状态值
    private final static int PULL_To_REFRESH = 1; // 从下拉返回到不刷新的状态值
    private final static int REFRESHING = 2;// 正在刷新的状态值
    private final static int DONE = 3;
    private final static int LOADING = 4;

    // 实际的padding的距离与界面上偏移距离的比例
    private final static int RATIO = 3;
    private LayoutInflater inflater;

    // ListView头部下拉刷新的布局
    private LinearLayout headerView;
    private TextView lvHeaderTipsTv;
    private TextView lvHeaderLastUpdatedTv;
    private ImageView lvHeaderArrowIv;
    private ProgressBar lvHeaderProgressBar;

    // 定义头部下拉刷新的布局的高度
    private int headerContentHeight;

    private RotateAnimation animation;
    private RotateAnimation reverseAnimation;

    private int startY;
    private int state;
    private boolean isBack;

    // 用于保证startY的值在一个完整的touch事件中只被记录一次
    private boolean isRecored;

    private OnRefreshListener refreshListener;

    private boolean isRefreshable;

    private void init(Context context) {
        setCacheColorHint(context.getResources().getColor(R.color.colorPrimary));//随便弄的一个
        inflater = LayoutInflater.from(context);
        headerView = (LinearLayout) inflater.inflate(R.layout.lv_header, null);
        lvHeaderTipsTv = (TextView) headerView
                .findViewById(R.id.lvHeaderTipsTv);
        lvHeaderLastUpdatedTv = (TextView) headerView
                .findViewById(R.id.lvHeaderLastUpdatedTv);

        lvHeaderArrowIv = (ImageView) headerView
                .findViewById(R.id.lvHeaderArrowIv);
        // 设置下拉刷新图标的最小高度和宽度
        lvHeaderArrowIv.setMinimumWidth(70);
        lvHeaderArrowIv.setMinimumHeight(50);

        lvHeaderProgressBar = (ProgressBar) headerView
                .findViewById(R.id.lvHeaderProgressBar);
        measureView(headerView);
        headerContentHeight = headerView.getMeasuredHeight();
        // 设置内边距，正好距离顶部为一个负的整个布局的高度，正好把头部隐藏
        headerView.setPadding(0, -1 * headerContentHeight, 0, 0);
        // 重绘一下
        headerView.invalidate();
        // 将下拉刷新的布局加入ListView的顶部
        addHeaderView(headerView, null, false);
        // 设置滚动监听事件
        setOnScrollListener(this);

        // 设置旋转动画事件
        animation = new RotateAnimation(0, -180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(250);
        animation.setFillAfter(true);

        reverseAnimation = new RotateAnimation(-180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        reverseAnimation.setInterpolator(new LinearInterpolator());
        reverseAnimation.setDuration(200);
        reverseAnimation.setFillAfter(true);

        // 一开始的状态就是下拉刷新完的状态，所以为DONE
        state = DONE;
        // 是否正在刷新
        isRefreshable = false;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem == 0) {
                    isRefreshable = true;
             } else {
                    isRefreshable = false;
            }
    }


    // 当状态改变时候，调用该方法，以更新界面
    private void changeHeaderViewByState() {
        switch (state) {
            case RELEASE_To_REFRESH:
                lvHeaderArrowIv.setVisibility(View.VISIBLE);
                lvHeaderProgressBar.setVisibility(View.GONE);
                lvHeaderTipsTv.setVisibility(View.VISIBLE);
                lvHeaderLastUpdatedTv.setVisibility(View.VISIBLE);

                lvHeaderArrowIv.clearAnimation();// 清除动画
                lvHeaderArrowIv.startAnimation(animation);// 开始动画效果

                lvHeaderTipsTv.setText("松开刷新");
                break;
            case PULL_To_REFRESH:
                lvHeaderProgressBar.setVisibility(View.GONE);
                lvHeaderTipsTv.setVisibility(View.VISIBLE);
                lvHeaderLastUpdatedTv.setVisibility(View.VISIBLE);
                lvHeaderArrowIv.clearAnimation();
                lvHeaderArrowIv.setVisibility(View.VISIBLE);
                // 是由RELEASE_To_REFRESH状态转变来的
                if (isBack) {
                    isBack = false;
                    lvHeaderArrowIv.clearAnimation();
                    lvHeaderArrowIv.startAnimation(reverseAnimation);

                    lvHeaderTipsTv.setText("下拉刷新");
                } else {
                    lvHeaderTipsTv.setText("下拉刷新");
                }
                break;

            case REFRESHING:

                headerView.setPadding(0, 0, 0, 0);

                lvHeaderProgressBar.setVisibility(View.VISIBLE);
                lvHeaderArrowIv.clearAnimation();
                lvHeaderArrowIv.setVisibility(View.GONE);
                lvHeaderTipsTv.setText("正在刷新...");
                lvHeaderLastUpdatedTv.setVisibility(View.VISIBLE);
                break;
            case DONE:
                headerView.setPadding(0, -1 * headerContentHeight, 0, 0);

                lvHeaderProgressBar.setVisibility(View.GONE);
                lvHeaderArrowIv.clearAnimation();
                lvHeaderArrowIv.setImageResource(R.drawable.list_item); // 随便弄了个
                lvHeaderTipsTv.setText("下拉刷新");
                lvHeaderLastUpdatedTv.setVisibility(View.VISIBLE);
                break;
        }
    }

    // 此方法直接照搬自网络上的一个下拉刷新的demo，此处是“估计”headView的width以及height
    private void measureView(View child) {
        ViewGroup.LayoutParams params = child.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0,
                params.width);
        int lpHeight = params.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    public void setonRefreshListener(OnRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
        isRefreshable = true;
    }

    public interface OnRefreshListener {
        public void onRefresh();
    }

    public void onRefreshComplete() {
        state = DONE;
        lvHeaderLastUpdatedTv.setText("最近更新:" + new Date().toLocaleString());
        changeHeaderViewByState();
    }

    private void onLvRefresh() {
        if (refreshListener != null) {
            refreshListener.onRefresh();
        }
    }

    public void setAdapter(ArrayAdapter adapter) {
        lvHeaderLastUpdatedTv.setText("最近更新:" + new Date().toLocaleString());
        super.setAdapter(adapter);
    }

    //**************************************




    public SlideCutListView(Context context) {
        this(context, null);
        init(context);
    }

    public SlideCutListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context);
    }

    public SlideCutListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        screenWidth = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        scroller = new Scroller(context);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    /**
     * 设置滑动删除的回调接口
     * @param removeListener
     */
    public void setRemoveListener(RemoveListener removeListener) {
        this.mRemoveListener = removeListener;
    }

    /**
     * 分发事件，主要做的是判断点击的是那个item, 以及通过postDelayed来设置响应左右滑动事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                addVelocityTracker(event);

                // 假如scroller滚动还没有结束，我们直接返回
                if (!scroller.isFinished()) {
                    return super.dispatchTouchEvent(event);
                }
                downX = (int) event.getX();
                downY = (int) event.getY();

                slidePosition = pointToPosition(downX, downY);

                // 无效的position, 不做任何处理
                if (slidePosition == AdapterView.INVALID_POSITION) {
                    return super.dispatchTouchEvent(event);
                }

                // 获取我们点击的item view
                itemView = getChildAt(slidePosition - getFirstVisiblePosition());



                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (Math.abs(getScrollVelocity()) > SNAP_VELOCITY
                        || (Math.abs(event.getX() - downX) > mTouchSlop && Math
                        .abs(event.getY() - downY) < mTouchSlop)) {
                    isSlide = true;

                }
                break;
            }
            case MotionEvent.ACTION_UP:
                recycleVelocityTracker();
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    /**
     * 往右滑动，getScrollX()返回的是左边缘的距离，就是以View左边缘为原点到开始滑动的距离，所以向右边滑动为负值
     */
    private void scrollRight() {
        removeDirection = RemoveDirection.RIGHT;
        final int delta = (screenWidth + itemView.getScrollX());
        // 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item
        scroller.startScroll(itemView.getScrollX(), 0, -delta, 0,
                Math.abs(delta));
        postInvalidate(); // 刷新itemView
    }

    /**
     * 向左滑动，根据上面我们知道向左滑动为正值
     */
    private void scrollLeft() {
        removeDirection = RemoveDirection.LEFT;
        final int delta = (screenWidth - itemView.getScrollX());
        // 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item
        scroller.startScroll(itemView.getScrollX(), 0, delta, 0,
                Math.abs(delta));
        postInvalidate(); // 刷新itemView
    }

    /**
     * 根据手指滚动itemView的距离来判断是滚动到开始位置还是向左或者向右滚动
     */
    private void scrollByDistanceX() {
        // 如果向左滚动的距离大于屏幕的二分之一，就让其删除
        if (itemView.getScrollX() >= screenWidth / 2) {
            scrollLeft();
        } else if (itemView.getScrollX() <= -screenWidth / 2) {
            scrollRight();
        } else {
            // 滚回到原始位置,为了偷下懒这里是直接调用scrollTo滚动
            itemView.scrollTo(0, 0);
        }

    }

    /**
     * 处理我们拖动ListView item的逻辑
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isSlide && slidePosition != AdapterView.INVALID_POSITION) {
            requestDisallowInterceptTouchEvent(true);
            addVelocityTracker(ev);
            final int action = ev.getAction();
            int x = (int) ev.getX();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    if (!isRecored) {
                        isRecored = true;
                        startY = (int) ev.getY();// 手指按下时记录当前位置
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    int tempY = (int) ev.getY();
                    if (!isRecored) {
                        isRecored = true;
                        startY = tempY;
                    }
                    if (state != REFRESHING && isRecored && state != LOADING) {
                        // 保证在设置padding的过程中，当前的位置一直是在head，否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动
                        // 可以松手去刷新了
                        if (state == RELEASE_To_REFRESH) {
                            setSelection(0);
                            // 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
                            if (((tempY - startY) / RATIO < headerContentHeight)// 由松开刷新状态转变到下拉刷新状态
                                    && (tempY - startY) > 0) {
                                state = PULL_To_REFRESH;
                                changeHeaderViewByState();
                            }
                            // 一下子推到顶了
                            else if (tempY - startY <= 0) {// 由松开刷新状态转变到done状态
                                state = DONE;
                                changeHeaderViewByState();
                            }
                        }
                        // 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
                        if (state == PULL_To_REFRESH) {
                            setSelection(0);
                            // 下拉到可以进入RELEASE_TO_REFRESH的状态
                            if ((tempY - startY) / RATIO >= headerContentHeight) {// 由done或者下拉刷新状态转变到松开刷新
                                state = RELEASE_To_REFRESH;
                                isBack = true;
                                changeHeaderViewByState();
                            }
                            // 上推到顶了
                            else if (tempY - startY <= 0) {// 由DOne或者下拉刷新状态转变到done状态
                                state = DONE;
                                changeHeaderViewByState();
                            }
                        }
                        // done状态下
                        if (state == DONE) {
                            if (tempY - startY > 0) {
                                state = PULL_To_REFRESH;
                                changeHeaderViewByState();
                            }
                        }
                        // 更新headView的size
                        if (state == PULL_To_REFRESH) {
                            headerView.setPadding(0, -1 * headerContentHeight
                                    + (tempY - startY) / RATIO, 0, 0);

                        }
                        // 更新headView的paddingTop
                        if (state == RELEASE_To_REFRESH) {
                            headerView.setPadding(0, (tempY - startY) / RATIO
                                    - headerContentHeight, 0, 0);
                        }

                    }


                        MotionEvent cancelEvent = MotionEvent.obtain(ev);
                        cancelEvent.setAction(MotionEvent.ACTION_CANCEL |
                                (ev.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
                        onTouchEvent(cancelEvent);

                        int deltaX = downX - x;
                        downX = x;

                        // 手指拖动itemView滚动, deltaX大于0向左滚动，小于0向右滚
                        itemView.scrollBy(deltaX, 0);

                    return true;  //拖动的时候ListView不滚动
                case MotionEvent.ACTION_UP:
                    if (state != REFRESHING && state != LOADING) {
                        if (state == PULL_To_REFRESH) {
                            state = DONE;
                            changeHeaderViewByState();
                        }
                        if (state == RELEASE_To_REFRESH) {
                            state = REFRESHING;
                            changeHeaderViewByState();
                            onLvRefresh();
                        }
                    }
                    isRecored = false;
                    isBack = false;
                    int velocityX = getScrollVelocity();
                    if (velocityX > SNAP_VELOCITY) {
                        scrollRight();
                    } else if (velocityX < -SNAP_VELOCITY) {
                        scrollLeft();
                    } else {
                        scrollByDistanceX();
                    }

                    recycleVelocityTracker();
                    // 手指离开的时候就不响应左右滚动
                    isSlide = false;
                    break;
            }
        }

        //否则直接交给ListView来处理onTouchEvent事件
        return super.onTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        // 调用startScroll的时候scroller.computeScrollOffset()返回true，
        if (scroller.computeScrollOffset()) {
            // 让ListView item根据当前的滚动偏移量进行滚动
            itemView.scrollTo(scroller.getCurrX(), scroller.getCurrY());

            postInvalidate();

            // 滚动动画结束的时候调用回调接口
            if (scroller.isFinished()) {
                if (mRemoveListener == null) {
                    throw new NullPointerException("RemoveListener is null, we should called setRemoveListener()");
                }

                itemView.scrollTo(0, 0);
                mRemoveListener.removeItem(removeDirection, slidePosition);
            }
        }
    }

    /**
     * 添加用户的速度跟踪器
     *
     * @param event
     */
    private void addVelocityTracker(MotionEvent event) {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }

        velocityTracker.addMovement(event);
    }

    /**
     * 移除用户速度跟踪器
     */
    private void recycleVelocityTracker() {
        if (velocityTracker != null) {
            velocityTracker.recycle();
            velocityTracker = null;
        }
    }

    /**
     * 获取X方向的滑动速度,大于0向右滑动，反之向左
     *
     * @return
     */
    private int getScrollVelocity() {
        velocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) velocityTracker.getXVelocity();
        return velocity;
    }

    /**
     *
     * 当ListView item滑出屏幕，回调这个接口
     * 我们需要在回调方法removeItem()中移除该Item,然后刷新ListView
     *
     * @author xiaanming
     *
     */
    public interface RemoveListener {
        public void removeItem(RemoveDirection direction, int position);
    }

}