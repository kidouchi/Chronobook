package kidouchi.chronobook;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by iuy407 on 2/9/16.
 */
public class EventItemClickListener implements RecyclerView.OnItemTouchListener {

    public interface OnItemInteraction {
        void onItemClick(View view, int position);
    }

    private OnItemInteraction mListener;
    private GestureDetector mGestureDetector;

    public EventItemClickListener(Context context, OnItemInteraction listener) {
        this.mListener = listener;

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View view = rv.findChildViewUnder(e.getX(), e.getY());
        if (view != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(view, rv.getChildAdapterPosition(view));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
